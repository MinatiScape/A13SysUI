package com.android.systemui.media.dialog;

import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable21;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.media.LocalMediaManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MediaOutputDialog extends MediaOutputBaseDialog {
    public final UiEventLogger mUiEventLogger;

    /* JADX WARN: Failed to restore enum class, 'enum' modifier removed */
    @VisibleForTesting
    /* loaded from: classes.dex */
    public static final class MediaOutputEvent extends Enum<MediaOutputEvent> implements UiEventLogger.UiEventEnum {
        public static final /* synthetic */ MediaOutputEvent[] $VALUES;
        public static final MediaOutputEvent MEDIA_OUTPUT_DIALOG_SHOW;
        private final int mId = 655;

        static {
            MediaOutputEvent mediaOutputEvent = new MediaOutputEvent();
            MEDIA_OUTPUT_DIALOG_SHOW = mediaOutputEvent;
            $VALUES = new MediaOutputEvent[]{mediaOutputEvent};
        }

        public static MediaOutputEvent valueOf(String str) {
            return (MediaOutputEvent) Enum.valueOf(MediaOutputEvent.class, str);
        }

        public static MediaOutputEvent[] values() {
            return (MediaOutputEvent[]) $VALUES.clone();
        }

        public final int getId() {
            return this.mId;
        }
    }

    @Override // com.android.systemui.media.dialog.MediaOutputBaseDialog
    public final void getHeaderIconRes() {
    }

    @Override // com.android.systemui.media.dialog.MediaOutputBaseDialog
    public final Drawable getAppSourceIcon() {
        MediaOutputController mediaOutputController = this.mMediaOutputController;
        Objects.requireNonNull(mediaOutputController);
        if (mediaOutputController.mPackageName.isEmpty()) {
            return null;
        }
        try {
            Log.d("MediaOutputController", "try to get app icon");
            return mediaOutputController.mContext.getPackageManager().getApplicationIcon(mediaOutputController.mPackageName);
        } catch (PackageManager.NameNotFoundException unused) {
            Log.d("MediaOutputController", "icon not found");
            return null;
        }
    }

    @Override // com.android.systemui.media.dialog.MediaOutputBaseDialog
    public final IconCompat getHeaderIcon() {
        IconCompat iconCompat;
        Bitmap iconBitmap;
        MediaOutputController mediaOutputController = this.mMediaOutputController;
        Objects.requireNonNull(mediaOutputController);
        MediaController mediaController = mediaOutputController.mMediaController;
        if (mediaController == null) {
            return null;
        }
        MediaMetadata metadata = mediaController.getMetadata();
        if (metadata == null || (iconBitmap = metadata.getDescription().getIconBitmap()) == null) {
            if (MediaOutputController.DEBUG) {
                Log.d("MediaOutputController", "Media meta data does not contain icon information");
            }
            if (TextUtils.isEmpty(mediaOutputController.mPackageName)) {
                return null;
            }
            for (NotificationEntry notificationEntry : mediaOutputController.mNotifCollection.getAllNotifs()) {
                Objects.requireNonNull(notificationEntry);
                Notification notification = notificationEntry.mSbn.getNotification();
                if (notification.isMediaNotification() && TextUtils.equals(notificationEntry.mSbn.getPackageName(), mediaOutputController.mPackageName)) {
                    Icon largeIcon = notification.getLargeIcon();
                    if (largeIcon == null) {
                        return null;
                    }
                    String str = IconCompat.EXTRA_TYPE;
                    int type = largeIcon.getType();
                    if (type == 2) {
                        iconCompat = IconCompat.createWithResource(null, largeIcon.getResPackage(), largeIcon.getResId());
                    } else if (type == 4) {
                        iconCompat = IconCompat.createWithContentUri(largeIcon.getUri());
                    } else if (type != 6) {
                        IconCompat iconCompat2 = new IconCompat(-1);
                        iconCompat2.mObj1 = largeIcon;
                        return iconCompat2;
                    } else {
                        iconCompat = IconCompat.createWithAdaptiveBitmapContentUri(largeIcon.getUri());
                    }
                    return iconCompat;
                }
            }
            return null;
        }
        Context context = mediaOutputController.mContext;
        Bitmap createBitmap = Bitmap.createBitmap(iconBitmap.getWidth(), iconBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        RoundedBitmapDrawable21 roundedBitmapDrawable21 = new RoundedBitmapDrawable21(context.getResources(), iconBitmap);
        roundedBitmapDrawable21.mPaint.setAntiAlias(true);
        roundedBitmapDrawable21.invalidateSelf();
        roundedBitmapDrawable21.setCornerRadius(context.getResources().getDimensionPixelSize(2131166344));
        Canvas canvas = new Canvas(createBitmap);
        roundedBitmapDrawable21.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        roundedBitmapDrawable21.draw(canvas);
        return IconCompat.createWithBitmap(createBitmap);
    }

    @Override // com.android.systemui.media.dialog.MediaOutputBaseDialog
    public final int getHeaderIconSize() {
        return ((MediaOutputBaseDialog) this).mContext.getResources().getDimensionPixelSize(2131166341);
    }

    @Override // com.android.systemui.media.dialog.MediaOutputBaseDialog
    public final CharSequence getHeaderSubtitle() {
        MediaMetadata metadata;
        MediaOutputController mediaOutputController = this.mMediaOutputController;
        Objects.requireNonNull(mediaOutputController);
        MediaController mediaController = mediaOutputController.mMediaController;
        if (mediaController == null || (metadata = mediaController.getMetadata()) == null) {
            return null;
        }
        return metadata.getDescription().getSubtitle();
    }

    @Override // com.android.systemui.media.dialog.MediaOutputBaseDialog
    public final CharSequence getHeaderText() {
        MediaMetadata metadata;
        MediaOutputController mediaOutputController = this.mMediaOutputController;
        Objects.requireNonNull(mediaOutputController);
        MediaController mediaController = mediaOutputController.mMediaController;
        if (mediaController == null || (metadata = mediaController.getMetadata()) == null) {
            return mediaOutputController.mContext.getText(2131952193);
        }
        return metadata.getDescription().getTitle();
    }

    @Override // com.android.systemui.media.dialog.MediaOutputBaseDialog
    public final int getStopButtonVisibility() {
        MediaOutputController mediaOutputController = this.mMediaOutputController;
        Objects.requireNonNull(mediaOutputController);
        LocalMediaManager localMediaManager = mediaOutputController.mLocalMediaManager;
        Objects.requireNonNull(localMediaManager);
        if (MediaOutputController.isActiveRemoteDevice(localMediaManager.mCurrentConnectedDevice)) {
            return 0;
        }
        return 8;
    }

    public MediaOutputDialog(Context context, boolean z, MediaOutputController mediaOutputController, UiEventLogger uiEventLogger, SystemUIDialogManager systemUIDialogManager) {
        super(context, mediaOutputController, systemUIDialogManager);
        this.mUiEventLogger = uiEventLogger;
        this.mAdapter = new MediaOutputAdapter(this.mMediaOutputController);
        if (!z) {
            getWindow().setType(2038);
        }
    }

    @Override // com.android.systemui.media.dialog.MediaOutputBaseDialog, com.android.systemui.statusbar.phone.SystemUIDialog, android.app.AlertDialog, android.app.Dialog
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mUiEventLogger.log(MediaOutputEvent.MEDIA_OUTPUT_DIALOG_SHOW);
    }
}
