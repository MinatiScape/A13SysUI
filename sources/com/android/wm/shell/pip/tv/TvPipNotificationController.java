package com.android.wm.shell.pip.tv;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.os.Handler;
import android.text.TextUtils;
import com.android.wm.shell.pip.PipMediaController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class TvPipNotificationController {
    public final ActionBroadcastReceiver mActionBroadcastReceiver = new ActionBroadcastReceiver();
    public Bitmap mArt;
    public final Context mContext;
    public String mDefaultTitle;
    public Delegate mDelegate;
    public final Handler mMainHandler;
    public String mMediaTitle;
    public final Notification.Builder mNotificationBuilder;
    public final NotificationManager mNotificationManager;
    public boolean mNotified;
    public final PackageManager mPackageManager;
    public String mPackageName;

    /* loaded from: classes.dex */
    public class ActionBroadcastReceiver extends BroadcastReceiver {
        public final IntentFilter mIntentFilter;
        public boolean mRegistered = false;

        public ActionBroadcastReceiver() {
            IntentFilter intentFilter = new IntentFilter();
            this.mIntentFilter = intentFilter;
            intentFilter.addAction("com.android.wm.shell.pip.tv.notification.action.CLOSE_PIP");
            intentFilter.addAction("com.android.wm.shell.pip.tv.notification.action.SHOW_PIP_MENU");
        }

        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("com.android.wm.shell.pip.tv.notification.action.SHOW_PIP_MENU".equals(action)) {
                TvPipController tvPipController = (TvPipController) TvPipNotificationController.this.mDelegate;
                Objects.requireNonNull(tvPipController);
                if (tvPipController.mState != 0) {
                    tvPipController.setState(2);
                    tvPipController.movePinnedStack();
                }
            } else if ("com.android.wm.shell.pip.tv.notification.action.CLOSE_PIP".equals(action)) {
                ((TvPipController) TvPipNotificationController.this.mDelegate).closePip();
            }
        }
    }

    /* loaded from: classes.dex */
    public interface Delegate {
    }

    public final void update() {
        String str;
        this.mNotified = true;
        Notification.Builder when = this.mNotificationBuilder.setWhen(System.currentTimeMillis());
        if (!TextUtils.isEmpty(this.mMediaTitle)) {
            str = this.mMediaTitle;
        } else {
            try {
                str = this.mPackageManager.getApplicationLabel(this.mPackageManager.getApplicationInfo(this.mPackageName, 0)).toString();
            } catch (PackageManager.NameNotFoundException unused) {
                str = null;
            }
            if (TextUtils.isEmpty(str)) {
                str = this.mDefaultTitle;
            }
        }
        when.setContentTitle(str);
        if (this.mArt != null) {
            this.mNotificationBuilder.setStyle(new Notification.BigPictureStyle().bigPicture(this.mArt));
        } else {
            this.mNotificationBuilder.setStyle(null);
        }
        this.mNotificationManager.notify("TvPip", 1100, this.mNotificationBuilder.build());
    }

    public TvPipNotificationController(Context context, PipMediaController pipMediaController, Handler handler) {
        MediaMetadata mediaMetadata;
        this.mContext = context;
        this.mPackageManager = context.getPackageManager();
        this.mNotificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
        this.mMainHandler = handler;
        this.mNotificationBuilder = new Notification.Builder(context, "TVPIP").setLocalOnly(true).setOngoing(false).setCategory("sys").setShowWhen(true).setSmallIcon(2131232566).extend(new Notification.TvExtender().setContentIntent(PendingIntent.getBroadcast(context, 0, new Intent("com.android.wm.shell.pip.tv.notification.action.SHOW_PIP_MENU").setPackage(context.getPackageName()), 335544320)).setDeleteIntent(PendingIntent.getBroadcast(context, 0, new Intent("com.android.wm.shell.pip.tv.notification.action.CLOSE_PIP").setPackage(context.getPackageName()), 335544320)));
        PipMediaController.MetadataListener tvPipNotificationController$$ExternalSyntheticLambda0 = new PipMediaController.MetadataListener() { // from class: com.android.wm.shell.pip.tv.TvPipNotificationController$$ExternalSyntheticLambda0
            @Override // com.android.wm.shell.pip.PipMediaController.MetadataListener
            public final void onMediaMetadataChanged(MediaMetadata mediaMetadata2) {
                Bitmap bitmap;
                boolean z;
                TvPipNotificationController tvPipNotificationController = TvPipNotificationController.this;
                String str = null;
                if (mediaMetadata2 != null) {
                    Objects.requireNonNull(tvPipNotificationController);
                    str = mediaMetadata2.getString("android.media.metadata.DISPLAY_TITLE");
                    if (TextUtils.isEmpty(str)) {
                        str = mediaMetadata2.getString("android.media.metadata.TITLE");
                    }
                    Bitmap bitmap2 = mediaMetadata2.getBitmap("android.media.metadata.ALBUM_ART");
                    if (bitmap2 == null) {
                        bitmap = mediaMetadata2.getBitmap("android.media.metadata.ART");
                    } else {
                        bitmap = bitmap2;
                    }
                } else {
                    bitmap = null;
                }
                if (!TextUtils.equals(str, tvPipNotificationController.mMediaTitle) || !Objects.equals(bitmap, tvPipNotificationController.mArt)) {
                    tvPipNotificationController.mMediaTitle = str;
                    tvPipNotificationController.mArt = bitmap;
                    z = true;
                } else {
                    z = false;
                }
                if (z && tvPipNotificationController.mNotified) {
                    tvPipNotificationController.update();
                }
            }
        };
        Objects.requireNonNull(pipMediaController);
        if (!pipMediaController.mMetadataListeners.contains(tvPipNotificationController$$ExternalSyntheticLambda0)) {
            pipMediaController.mMetadataListeners.add(tvPipNotificationController$$ExternalSyntheticLambda0);
            MediaController mediaController = pipMediaController.mMediaController;
            if (mediaController != null) {
                mediaMetadata = mediaController.getMetadata();
            } else {
                mediaMetadata = null;
            }
            tvPipNotificationController$$ExternalSyntheticLambda0.onMediaMetadataChanged(mediaMetadata);
        }
        this.mDefaultTitle = context.getResources().getString(2131952982);
        if (this.mNotified) {
            update();
        }
    }
}
