package com.android.systemui.qs.tiles.dialog;

import android.content.ClipData;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.view.View;
import com.android.settingslib.users.AvatarPhotoController;
import com.android.settingslib.users.AvatarPickerActivity;
import com.android.systemui.qs.carrier.QSCarrierGroupController;
import com.android.systemui.statusbar.notification.row.NotificationInfo;
import com.google.android.systemui.gamedashboard.GameMenuActivity;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class InternetDialog$$ExternalSyntheticLambda3 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ InternetDialog$$ExternalSyntheticLambda3(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                InternetDialogController internetDialogController = (InternetDialogController) this.f$0;
                Objects.requireNonNull(internetDialogController);
                Intent intent = new Intent("android.settings.WIFI_SCANNING_SETTINGS");
                intent.addFlags(268435456);
                internetDialogController.startActivity(intent, view);
                return;
            case 1:
                AvatarPickerActivity.AvatarAdapter avatarAdapter = (AvatarPickerActivity.AvatarAdapter) this.f$0;
                Objects.requireNonNull(avatarAdapter);
                AvatarPhotoController avatarPhotoController = AvatarPickerActivity.this.mAvatarPhotoController;
                Objects.requireNonNull(avatarPhotoController);
                Intent intent2 = new Intent("android.media.action.IMAGE_CAPTURE_SECURE");
                Uri uri = avatarPhotoController.mTakePictureUri;
                intent2.putExtra("output", uri);
                intent2.addFlags(3);
                intent2.setClipData(ClipData.newRawUri("output", uri));
                avatarPhotoController.mActivity.startActivityForResult(intent2, 1002);
                return;
            case 2:
                QSCarrierGroupController qSCarrierGroupController = (QSCarrierGroupController) this.f$0;
                Objects.requireNonNull(qSCarrierGroupController);
                if (view.isVisibleToUser()) {
                    qSCarrierGroupController.mActivityStarter.postStartActivityDismissingKeyguard(new Intent("android.settings.WIRELESS_SETTINGS"), 0);
                    return;
                }
                return;
            case 3:
                NotificationInfo notificationInfo = (NotificationInfo) this.f$0;
                int i = NotificationInfo.$r8$clinit;
                Objects.requireNonNull(notificationInfo);
                notificationInfo.mChosenImportance = 2;
                notificationInfo.mIsAutomaticChosen = false;
                notificationInfo.applyAlertingBehavior(1, true);
                return;
            default:
                GameMenuActivity gameMenuActivity = (GameMenuActivity) this.f$0;
                IntentFilter intentFilter = GameMenuActivity.DND_FILTER;
                Objects.requireNonNull(gameMenuActivity);
                gameMenuActivity.onGameModeSelectionChanged(view);
                return;
        }
    }
}
