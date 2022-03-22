package com.android.systemui.screenrecord;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline1;
import androidx.fragment.app.DialogFragment$$ExternalSyntheticOutline0;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardPINView$$ExternalSyntheticLambda0;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.screenrecord.ScreenMediaRecorder;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class RecordingService extends Service implements MediaRecorder.OnInfoListener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public ScreenRecordingAudioSource mAudioSource;
    public final RecordingController mController;
    public final KeyguardDismissUtil mKeyguardDismissUtil;
    public final Executor mLongExecutor;
    public final NotificationManager mNotificationManager;
    public boolean mOriginalShowTaps;
    public ScreenMediaRecorder mRecorder;
    public boolean mShowTaps;
    public final UiEventLogger mUiEventLogger;
    public final UserContextProvider mUserContextTracker;

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        return null;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.app.Service
    public final int onStartCommand(Intent intent, int i, int i2) {
        char c;
        boolean z;
        Object[] objArr;
        if (intent == null) {
            return 2;
        }
        String action = intent.getAction();
        DialogFragment$$ExternalSyntheticOutline0.m("onStartCommand ", action, "RecordingService");
        int userId = this.mUserContextTracker.getUserContext().getUserId();
        final UserHandle userHandle = new UserHandle(userId);
        Objects.requireNonNull(action);
        switch (action.hashCode()) {
            case -1688140755:
                if (action.equals("com.android.systemui.screenrecord.SHARE")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -1687783248:
                if (action.equals("com.android.systemui.screenrecord.START")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -470086188:
                if (action.equals("com.android.systemui.screenrecord.STOP")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -288359034:
                if (action.equals("com.android.systemui.screenrecord.STOP_FROM_NOTIF")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                final Intent putExtra = new Intent("android.intent.action.SEND").setType("video/mp4").putExtra("android.intent.extra.STREAM", Uri.parse(intent.getStringExtra("extra_path")));
                this.mKeyguardDismissUtil.executeWhenUnlocked(new ActivityStarter.OnDismissAction() { // from class: com.android.systemui.screenrecord.RecordingService$$ExternalSyntheticLambda0
                    @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
                    public final boolean onDismiss() {
                        RecordingService recordingService = RecordingService.this;
                        Intent intent2 = putExtra;
                        UserHandle userHandle2 = userHandle;
                        int i3 = RecordingService.$r8$clinit;
                        Objects.requireNonNull(recordingService);
                        recordingService.startActivity(Intent.createChooser(intent2, recordingService.getResources().getString(2131953214)).setFlags(268435456));
                        recordingService.mNotificationManager.cancelAsUser(null, 4273, userHandle2);
                        return false;
                    }
                }, false, false);
                sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
                break;
            case 1:
                this.mAudioSource = ScreenRecordingAudioSource.values()[intent.getIntExtra("extra_useAudio", 0)];
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("recording with audio source");
                m.append(this.mAudioSource);
                Log.d("RecordingService", m.toString());
                this.mShowTaps = intent.getBooleanExtra("extra_showTaps", false);
                if (Settings.System.getInt(getApplicationContext().getContentResolver(), "show_touches", 0) != 0) {
                    z = true;
                } else {
                    z = false;
                }
                this.mOriginalShowTaps = z;
                Settings.System.putInt(getContentResolver(), "show_touches", this.mShowTaps ? 1 : 0);
                this.mRecorder = new ScreenMediaRecorder(this.mUserContextTracker.getUserContext(), userId, this.mAudioSource, this);
                try {
                    getRecorder().start();
                    objArr = 1;
                } catch (RemoteException | IOException | RuntimeException e) {
                    showErrorToast(2131953216);
                    e.printStackTrace();
                    objArr = null;
                }
                if (objArr != null) {
                    updateState(true);
                    createRecordingNotification();
                    this.mUiEventLogger.log(Events$ScreenRecordEvent.SCREEN_RECORD_START);
                    break;
                } else {
                    updateState(false);
                    createErrorNotification();
                    stopForeground(true);
                    stopSelf();
                    return 2;
                }
            case 2:
            case 3:
                if ("com.android.systemui.screenrecord.STOP_FROM_NOTIF".equals(action)) {
                    this.mUiEventLogger.log(Events$ScreenRecordEvent.SCREEN_RECORD_END_NOTIFICATION);
                } else {
                    this.mUiEventLogger.log(Events$ScreenRecordEvent.SCREEN_RECORD_END_QS_TILE);
                }
                int intExtra = intent.getIntExtra("android.intent.extra.user_handle", -1);
                if (intExtra == -1) {
                    intExtra = this.mUserContextTracker.getUserContext().getUserId();
                }
                ExifInterface$$ExternalSyntheticOutline1.m("notifying for user ", intExtra, "RecordingService");
                Settings.System.putInt(getContentResolver(), "show_touches", this.mOriginalShowTaps ? 1 : 0);
                if (getRecorder() != null) {
                    ScreenMediaRecorder recorder = getRecorder();
                    Objects.requireNonNull(recorder);
                    recorder.mMediaRecorder.stop();
                    recorder.mMediaRecorder.release();
                    recorder.mInputSurface.release();
                    recorder.mVirtualDisplay.release();
                    recorder.mMediaProjection.stop();
                    recorder.mMediaRecorder = null;
                    recorder.mMediaProjection = null;
                    ScreenRecordingAudioSource screenRecordingAudioSource = recorder.mAudioSource;
                    if (screenRecordingAudioSource == ScreenRecordingAudioSource.INTERNAL || screenRecordingAudioSource == ScreenRecordingAudioSource.MIC_AND_INTERNAL) {
                        ScreenInternalAudioRecorder screenInternalAudioRecorder = recorder.mAudio;
                        Objects.requireNonNull(screenInternalAudioRecorder);
                        screenInternalAudioRecorder.mAudioRecord.stop();
                        if (screenInternalAudioRecorder.mMic) {
                            screenInternalAudioRecorder.mAudioRecordMic.stop();
                        }
                        screenInternalAudioRecorder.mAudioRecord.release();
                        if (screenInternalAudioRecorder.mMic) {
                            screenInternalAudioRecorder.mAudioRecordMic.release();
                        }
                        try {
                            screenInternalAudioRecorder.mThread.join();
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        screenInternalAudioRecorder.mCodec.stop();
                        screenInternalAudioRecorder.mCodec.release();
                        screenInternalAudioRecorder.mMuxer.stop();
                        screenInternalAudioRecorder.mMuxer.release();
                        screenInternalAudioRecorder.mThread = null;
                        recorder.mAudio = null;
                    }
                    Log.d("ScreenMediaRecorder", "end recording");
                    UserHandle userHandle2 = new UserHandle(intExtra);
                    this.mNotificationManager.notifyAsUser(null, 4275, createProcessingNotification(), userHandle2);
                    this.mLongExecutor.execute(new KeyguardPINView$$ExternalSyntheticLambda0(this, userHandle2, 2));
                } else {
                    Log.e("RecordingService", "stopRecording called, but recorder was null");
                }
                updateState(false);
                this.mNotificationManager.cancel(4274);
                stopSelf();
                break;
        }
        return 1;
    }

    @VisibleForTesting
    public void showErrorToast(int i) {
        Toast.makeText(this, i, 1).show();
    }

    @Override // android.media.MediaRecorder.OnInfoListener
    public final void onInfo(MediaRecorder mediaRecorder, int i, int i2) {
        Log.d("RecordingService", "Media recorder info: " + i);
        onStartCommand(new Intent(this, RecordingService.class).setAction("com.android.systemui.screenrecord.STOP").putExtra("android.intent.extra.user_handle", getUserId()), 0, 0);
    }

    public final void updateState(boolean z) {
        if (this.mUserContextTracker.getUserContext().getUserId() == 0) {
            this.mController.updateState(z);
            return;
        }
        Intent intent = new Intent("com.android.systemui.screenrecord.UPDATE_STATE");
        intent.putExtra("extra_state", z);
        intent.addFlags(1073741824);
        sendBroadcast(intent, "com.android.systemui.permission.SELF");
    }

    public RecordingService(RecordingController recordingController, Executor executor, UiEventLogger uiEventLogger, NotificationManager notificationManager, UserContextProvider userContextProvider, KeyguardDismissUtil keyguardDismissUtil) {
        this.mController = recordingController;
        this.mLongExecutor = executor;
        this.mUiEventLogger = uiEventLogger;
        this.mNotificationManager = notificationManager;
        this.mUserContextTracker = userContextProvider;
        this.mKeyguardDismissUtil = keyguardDismissUtil;
    }

    @VisibleForTesting
    public void createErrorNotification() {
        Resources resources = getResources();
        NotificationChannel notificationChannel = new NotificationChannel("screen_record", getString(2131953209), 3);
        notificationChannel.setDescription(getString(2131953202));
        notificationChannel.enableVibration(true);
        this.mNotificationManager.createNotificationChannel(notificationChannel);
        Bundle bundle = new Bundle();
        bundle.putString("android.substName", resources.getString(2131953209));
        startForeground(4274, new Notification.Builder(this, "screen_record").setSmallIcon(2131232251).setContentTitle(resources.getString(2131953216)).addExtras(bundle).build());
    }

    @VisibleForTesting
    public Notification createProcessingNotification() {
        String str;
        Resources resources = getApplicationContext().getResources();
        if (this.mAudioSource == ScreenRecordingAudioSource.NONE) {
            str = resources.getString(2131953211);
        } else {
            str = resources.getString(2131953210);
        }
        Bundle bundle = new Bundle();
        bundle.putString("android.substName", resources.getString(2131953209));
        return new Notification.Builder(getApplicationContext(), "screen_record").setContentTitle(str).setContentText(getResources().getString(2131953201)).setSmallIcon(2131232251).addExtras(bundle).build();
    }

    @VisibleForTesting
    public void createRecordingNotification() {
        String str;
        Resources resources = getResources();
        NotificationChannel notificationChannel = new NotificationChannel("screen_record", getString(2131953209), 3);
        notificationChannel.setDescription(getString(2131953202));
        notificationChannel.enableVibration(true);
        this.mNotificationManager.createNotificationChannel(notificationChannel);
        Bundle bundle = new Bundle();
        bundle.putString("android.substName", resources.getString(2131953209));
        if (this.mAudioSource == ScreenRecordingAudioSource.NONE) {
            str = resources.getString(2131953211);
        } else {
            str = resources.getString(2131953210);
        }
        startForeground(4274, new Notification.Builder(this, "screen_record").setSmallIcon(2131232251).setContentTitle(str).setUsesChronometer(true).setColorized(true).setColor(getResources().getColor(2131099661)).setOngoing(true).setForegroundServiceBehavior(1).addAction(new Notification.Action.Builder(Icon.createWithResource(this, 2131231751), getResources().getString(2131953218), PendingIntent.getService(this, 2, new Intent(this, RecordingService.class).setAction("com.android.systemui.screenrecord.STOP_FROM_NOTIF"), 201326592)).build()).addExtras(bundle).build());
    }

    @VisibleForTesting
    public Notification createSaveNotification(ScreenMediaRecorder.SavedRecording savedRecording) {
        Objects.requireNonNull(savedRecording);
        Uri uri = savedRecording.mUri;
        Intent dataAndType = new Intent("android.intent.action.VIEW").setFlags(268435457).setDataAndType(uri, "video/mp4");
        Notification.Action build = new Notification.Action.Builder(Icon.createWithResource(this, 2131232251), getResources().getString(2131953214), PendingIntent.getService(this, 2, new Intent(this, RecordingService.class).setAction("com.android.systemui.screenrecord.SHARE").putExtra("extra_path", uri.toString()), 201326592)).build();
        Bundle bundle = new Bundle();
        bundle.putString("android.substName", getResources().getString(2131953209));
        Notification.Builder addExtras = new Notification.Builder(this, "screen_record").setSmallIcon(2131232251).setContentTitle(getResources().getString(2131953213)).setContentText(getResources().getString(2131953212)).setContentIntent(PendingIntent.getActivity(this, 2, dataAndType, 67108864)).addAction(build).setAutoCancel(true).addExtras(bundle);
        Bitmap bitmap = savedRecording.mThumbnailBitmap;
        if (bitmap != null) {
            addExtras.setStyle(new Notification.BigPictureStyle().bigPicture(bitmap).showBigPictureWhenCollapsed(true));
        }
        return addExtras.build();
    }

    @VisibleForTesting
    public ScreenMediaRecorder getRecorder() {
        return this.mRecorder;
    }

    @Override // android.app.Service
    public final void onCreate() {
        super.onCreate();
    }
}
