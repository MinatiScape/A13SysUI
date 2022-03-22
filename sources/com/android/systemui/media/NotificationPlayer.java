package com.android.systemui.media;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import java.lang.Thread;
import java.util.LinkedList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class NotificationPlayer implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    @GuardedBy({"mQueueAudioFocusLock"})
    public AudioManager mAudioManagerWithAudioFocus;
    @GuardedBy({"mCompletionHandlingLock"})
    public CreationAndCompletionThread mCompletionThread;
    @GuardedBy({"mCompletionHandlingLock"})
    public Looper mLooper;
    @GuardedBy({"mPlayerLock"})
    public MediaPlayer mPlayer;
    @GuardedBy({"mCmdQueue"})
    public CmdThread mThread;
    @GuardedBy({"mCmdQueue"})
    public PowerManager.WakeLock mWakeLock;
    public final LinkedList<Command> mCmdQueue = new LinkedList<>();
    public final Object mCompletionHandlingLock = new Object();
    public final Object mPlayerLock = new Object();
    public final Object mQueueAudioFocusLock = new Object();
    public int mNotificationRampTimeMs = 0;
    public int mState = 2;
    public String mTag = "RingtonePlayer";

    /* loaded from: classes.dex */
    public final class CmdThread extends Thread {
        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public CmdThread() {
            /*
                r1 = this;
                com.android.systemui.media.NotificationPlayer.this = r2
                java.lang.String r0 = "NotificationPlayer-"
                java.lang.StringBuilder r0 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1.m(r0)
                java.lang.String r2 = r2.mTag
                r0.append(r2)
                java.lang.String r2 = r0.toString()
                r1.<init>(r2)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.NotificationPlayer.CmdThread.<init>(com.android.systemui.media.NotificationPlayer):void");
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public final void run() {
            Command removeFirst;
            NotificationPlayer notificationPlayer;
            MediaPlayer mediaPlayer;
            while (true) {
                synchronized (NotificationPlayer.this.mCmdQueue) {
                    removeFirst = NotificationPlayer.this.mCmdQueue.removeFirst();
                }
                int i = removeFirst.code;
                if (i == 1) {
                    NotificationPlayer.m70$$Nest$mstartSound(NotificationPlayer.this, removeFirst);
                } else if (i == 2) {
                    synchronized (NotificationPlayer.this.mPlayerLock) {
                        notificationPlayer = NotificationPlayer.this;
                        mediaPlayer = notificationPlayer.mPlayer;
                        notificationPlayer.mPlayer = null;
                    }
                    if (mediaPlayer != null) {
                        long uptimeMillis = SystemClock.uptimeMillis() - removeFirst.requestTime;
                        if (uptimeMillis > 1000) {
                            String str = NotificationPlayer.this.mTag;
                            Log.w(str, "Notification stop delayed by " + uptimeMillis + "msecs");
                        }
                        try {
                            mediaPlayer.stop();
                        } catch (Exception unused) {
                        }
                        mediaPlayer.release();
                        synchronized (NotificationPlayer.this.mQueueAudioFocusLock) {
                            AudioManager audioManager = NotificationPlayer.this.mAudioManagerWithAudioFocus;
                            if (audioManager != null) {
                                audioManager.abandonAudioFocus(null);
                                NotificationPlayer.this.mAudioManagerWithAudioFocus = null;
                            }
                        }
                        synchronized (NotificationPlayer.this.mCompletionHandlingLock) {
                            Looper looper = NotificationPlayer.this.mLooper;
                            if (!(looper == null || looper.getThread().getState() == Thread.State.TERMINATED)) {
                                NotificationPlayer.this.mLooper.quit();
                            }
                        }
                    } else {
                        Log.w(notificationPlayer.mTag, "STOP command without a player");
                    }
                }
                synchronized (NotificationPlayer.this.mCmdQueue) {
                    if (NotificationPlayer.this.mCmdQueue.size() == 0) {
                        break;
                    }
                }
            }
            NotificationPlayer notificationPlayer2 = NotificationPlayer.this;
            notificationPlayer2.mThread = null;
            Objects.requireNonNull(notificationPlayer2);
            PowerManager.WakeLock wakeLock = notificationPlayer2.mWakeLock;
            if (wakeLock != null) {
                wakeLock.release();
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class Command {
        public AudioAttributes attributes;
        public int code;
        public Context context;
        public boolean looping;
        public long requestTime;
        public Uri uri;

        public Command() {
        }

        public Command(int i) {
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("{ code=");
            m.append(this.code);
            m.append(" looping=");
            m.append(this.looping);
            m.append(" attributes=");
            m.append(this.attributes);
            m.append(" uri=");
            m.append(this.uri);
            m.append(" }");
            return m.toString();
        }
    }

    /* loaded from: classes.dex */
    public final class CreationAndCompletionThread extends Thread {
        public Command mCmd;

        public CreationAndCompletionThread(Command command) {
            this.mCmd = command;
        }

        /* JADX WARN: Removed duplicated region for block: B:48:0x00fd A[Catch: all -> 0x0119, TRY_ENTER, TryCatch #7 {, blocks: (B:4:0x000c, B:6:0x0019, B:7:0x001e, B:9:0x0024, B:10:0x0039, B:12:0x0063, B:14:0x0069, B:16:0x0077, B:18:0x007d, B:19:0x0081, B:30:0x00a8, B:31:0x00a9, B:33:0x00b3, B:34:0x00bc, B:39:0x00c6, B:40:0x00ca, B:41:0x00ed, B:46:0x00f8, B:47:0x00fc, B:51:0x0106, B:53:0x010b, B:54:0x010e, B:55:0x0111, B:56:0x0114, B:48:0x00fd, B:49:0x0103, B:42:0x00ee, B:44:0x00f2, B:45:0x00f7), top: B:72:0x000c }] */
        @Override // java.lang.Thread, java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void run() {
            /*
                Method dump skipped, instructions count: 290
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.NotificationPlayer.CreationAndCompletionThread.run():void");
        }
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public final void onCompletion(MediaPlayer mediaPlayer) {
        synchronized (this.mQueueAudioFocusLock) {
            AudioManager audioManager = this.mAudioManagerWithAudioFocus;
            if (audioManager != null) {
                audioManager.abandonAudioFocus(null);
                this.mAudioManagerWithAudioFocus = null;
            }
        }
        synchronized (this.mCmdQueue) {
            synchronized (this.mCompletionHandlingLock) {
                if (this.mCmdQueue.size() == 0) {
                    Looper looper = this.mLooper;
                    if (looper != null) {
                        looper.quit();
                    }
                    this.mCompletionThread = null;
                }
            }
        }
        synchronized (this.mPlayerLock) {
            if (mediaPlayer == this.mPlayer) {
                this.mPlayer = null;
            }
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public final boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        String str = this.mTag;
        Log.e(str, "error " + i + " (extra=" + i2 + ") playing notification");
        onCompletion(mediaPlayer);
        return true;
    }

    /* renamed from: -$$Nest$mstartSound  reason: not valid java name */
    public static void m70$$Nest$mstartSound(NotificationPlayer notificationPlayer, Command command) {
        Objects.requireNonNull(notificationPlayer);
        try {
            synchronized (notificationPlayer.mCompletionHandlingLock) {
                Looper looper = notificationPlayer.mLooper;
                if (!(looper == null || looper.getThread().getState() == Thread.State.TERMINATED)) {
                    notificationPlayer.mLooper.quit();
                }
                CreationAndCompletionThread creationAndCompletionThread = new CreationAndCompletionThread(command);
                notificationPlayer.mCompletionThread = creationAndCompletionThread;
                synchronized (creationAndCompletionThread) {
                    notificationPlayer.mCompletionThread.start();
                    notificationPlayer.mCompletionThread.wait();
                }
            }
            long uptimeMillis = SystemClock.uptimeMillis() - command.requestTime;
            if (uptimeMillis > 1000) {
                String str = notificationPlayer.mTag;
                Log.w(str, "Notification sound delayed by " + uptimeMillis + "msecs");
            }
        } catch (Exception e) {
            String str2 = notificationPlayer.mTag;
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("error loading sound for ");
            m.append(command.uri);
            Log.w(str2, m.toString(), e);
        }
    }
}
