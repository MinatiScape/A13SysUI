package com.android.systemui.media;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.IAudioService;
import android.media.IRingtonePlayer;
import android.media.Ringtone;
import android.media.VolumeShaper;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.MediaStore;
import android.util.Log;
import com.android.systemui.CoreStartable;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import com.android.systemui.media.NotificationPlayer;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Objects;
/* loaded from: classes.dex */
public class RingtonePlayer extends CoreStartable {
    public final NotificationPlayer mAsyncPlayer = new NotificationPlayer();
    public final HashMap<IBinder, Client> mClients = new HashMap<>();
    public AnonymousClass1 mCallback = new IRingtonePlayer.Stub() { // from class: com.android.systemui.media.RingtonePlayer.1
        public final void play(IBinder iBinder, Uri uri, AudioAttributes audioAttributes, float f, boolean z) throws RemoteException {
            playWithVolumeShaping(iBinder, uri, audioAttributes, f, z, null);
        }

        public final boolean isPlaying(IBinder iBinder) {
            Client client;
            synchronized (RingtonePlayer.this.mClients) {
                client = RingtonePlayer.this.mClients.get(iBinder);
            }
            if (client != null) {
                return client.mRingtone.isPlaying();
            }
            return false;
        }

        public final void playWithVolumeShaping(IBinder iBinder, Uri uri, AudioAttributes audioAttributes, float f, boolean z, VolumeShaper.Configuration configuration) throws RemoteException {
            Client client;
            synchronized (RingtonePlayer.this.mClients) {
                client = RingtonePlayer.this.mClients.get(iBinder);
                if (client == null) {
                    client = new Client(iBinder, uri, Binder.getCallingUserHandle(), audioAttributes, configuration);
                    iBinder.linkToDeath(client, 0);
                    RingtonePlayer.this.mClients.put(iBinder, client);
                }
            }
            client.mRingtone.setLooping(z);
            client.mRingtone.setVolume(f);
            client.mRingtone.play();
        }

        public final void setPlaybackProperties(IBinder iBinder, float f, boolean z, boolean z2) {
            Client client;
            synchronized (RingtonePlayer.this.mClients) {
                client = RingtonePlayer.this.mClients.get(iBinder);
            }
            if (client != null) {
                client.mRingtone.setVolume(f);
                client.mRingtone.setLooping(z);
                client.mRingtone.setHapticGeneratorEnabled(z2);
            }
        }

        public final void stop(IBinder iBinder) {
            Client remove;
            synchronized (RingtonePlayer.this.mClients) {
                remove = RingtonePlayer.this.mClients.remove(iBinder);
            }
            if (remove != null) {
                remove.mToken.unlinkToDeath(remove, 0);
                remove.mRingtone.stop();
            }
        }

        public final String getTitle(Uri uri) {
            return Ringtone.getTitle(RingtonePlayer.m71$$Nest$mgetContextForUser(RingtonePlayer.this, Binder.getCallingUserHandle()), uri, false, false);
        }

        public final ParcelFileDescriptor openRingtone(Uri uri) {
            ContentResolver contentResolver = RingtonePlayer.m71$$Nest$mgetContextForUser(RingtonePlayer.this, Binder.getCallingUserHandle()).getContentResolver();
            if (uri.toString().startsWith(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString())) {
                Cursor query = contentResolver.query(uri, new String[]{"is_ringtone", "is_alarm", "is_notification"}, null, null, null);
                try {
                    if (!query.moveToFirst() || (query.getInt(0) == 0 && query.getInt(1) == 0 && query.getInt(2) == 0)) {
                        query.close();
                    } else {
                        try {
                            ParcelFileDescriptor openFileDescriptor = contentResolver.openFileDescriptor(uri, "r");
                            query.close();
                            return openFileDescriptor;
                        } catch (IOException e) {
                            throw new SecurityException(e);
                        }
                    }
                } catch (Throwable th) {
                    if (query != null) {
                        try {
                            query.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            }
            throw new SecurityException("Uri is not ringtone, alarm, or notification: " + uri);
        }

        public final void playAsync(Uri uri, UserHandle userHandle, boolean z, AudioAttributes audioAttributes) {
            if (Binder.getCallingUid() == 1000) {
                if (UserHandle.ALL.equals(userHandle)) {
                    userHandle = UserHandle.SYSTEM;
                }
                RingtonePlayer ringtonePlayer = RingtonePlayer.this;
                NotificationPlayer notificationPlayer = ringtonePlayer.mAsyncPlayer;
                Context context = RingtonePlayer.m71$$Nest$mgetContextForUser(ringtonePlayer, userHandle);
                Objects.requireNonNull(notificationPlayer);
                NotificationPlayer.Command command = new NotificationPlayer.Command(0);
                command.requestTime = SystemClock.uptimeMillis();
                command.code = 1;
                command.context = context;
                command.uri = uri;
                command.looping = z;
                command.attributes = audioAttributes;
                synchronized (notificationPlayer.mCmdQueue) {
                    notificationPlayer.mCmdQueue.add(command);
                    if (notificationPlayer.mThread == null) {
                        PowerManager.WakeLock wakeLock = notificationPlayer.mWakeLock;
                        if (wakeLock != null) {
                            wakeLock.acquire();
                        }
                        NotificationPlayer.CmdThread cmdThread = new NotificationPlayer.CmdThread();
                        notificationPlayer.mThread = cmdThread;
                        cmdThread.start();
                    }
                    notificationPlayer.mState = 1;
                }
                return;
            }
            throw new SecurityException("Async playback only available from system UID.");
        }

        public final void stopAsync() {
            if (Binder.getCallingUid() == 1000) {
                NotificationPlayer notificationPlayer = RingtonePlayer.this.mAsyncPlayer;
                Objects.requireNonNull(notificationPlayer);
                synchronized (notificationPlayer.mCmdQueue) {
                    if (notificationPlayer.mState != 2) {
                        NotificationPlayer.Command command = new NotificationPlayer.Command(0);
                        command.requestTime = SystemClock.uptimeMillis();
                        command.code = 2;
                        notificationPlayer.mCmdQueue.add(command);
                        if (notificationPlayer.mThread == null) {
                            PowerManager.WakeLock wakeLock = notificationPlayer.mWakeLock;
                            if (wakeLock != null) {
                                wakeLock.acquire();
                            }
                            NotificationPlayer.CmdThread cmdThread = new NotificationPlayer.CmdThread();
                            notificationPlayer.mThread = cmdThread;
                            cmdThread.start();
                        }
                        notificationPlayer.mState = 2;
                    }
                }
                return;
            }
            throw new SecurityException("Async playback only available from system UID.");
        }
    };

    /* loaded from: classes.dex */
    public class Client implements IBinder.DeathRecipient {
        public final Ringtone mRingtone;
        public final IBinder mToken;

        public Client(IBinder iBinder, Uri uri, UserHandle userHandle, AudioAttributes audioAttributes, VolumeShaper.Configuration configuration) {
            this.mToken = iBinder;
            Ringtone ringtone = new Ringtone(RingtonePlayer.m71$$Nest$mgetContextForUser(RingtonePlayer.this, userHandle), false);
            this.mRingtone = ringtone;
            ringtone.setAudioAttributes(audioAttributes);
            ringtone.setUri(uri, configuration);
        }

        @Override // android.os.IBinder.DeathRecipient
        public final void binderDied() {
            synchronized (RingtonePlayer.this.mClients) {
                RingtonePlayer.this.mClients.remove(this.mToken);
            }
            this.mRingtone.stop();
        }
    }

    @Override // com.android.systemui.CoreStartable, com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("Clients:");
        synchronized (this.mClients) {
            for (Client client : this.mClients.values()) {
                printWriter.print("  mToken=");
                printWriter.print(client.mToken);
                printWriter.print(" mUri=");
                printWriter.println(client.mRingtone.getUri());
            }
        }
    }

    @Override // com.android.systemui.CoreStartable
    public final void start() {
        NotificationPlayer notificationPlayer = this.mAsyncPlayer;
        Context context = this.mContext;
        Objects.requireNonNull(notificationPlayer);
        synchronized (notificationPlayer.mCmdQueue) {
            if (notificationPlayer.mWakeLock == null && notificationPlayer.mThread == null) {
                notificationPlayer.mWakeLock = ((PowerManager) context.getSystemService(GlobalActionsDialogLite.GLOBAL_ACTION_KEY_POWER)).newWakeLock(1, notificationPlayer.mTag);
            } else {
                throw new RuntimeException("assertion failed mWakeLock=" + notificationPlayer.mWakeLock + " mThread=" + notificationPlayer.mThread);
            }
        }
        try {
            IAudioService.Stub.asInterface(ServiceManager.getService("audio")).setRingtonePlayer(this.mCallback);
        } catch (RemoteException e) {
            Log.e("RingtonePlayer", "Problem registering RingtonePlayer: " + e);
        }
    }

    /* renamed from: -$$Nest$mgetContextForUser  reason: not valid java name */
    public static Context m71$$Nest$mgetContextForUser(RingtonePlayer ringtonePlayer, UserHandle userHandle) {
        Objects.requireNonNull(ringtonePlayer);
        try {
            Context context = ringtonePlayer.mContext;
            return context.createPackageContextAsUser(context.getPackageName(), 0, userHandle);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public RingtonePlayer(Context context) {
        super(context);
    }
}
