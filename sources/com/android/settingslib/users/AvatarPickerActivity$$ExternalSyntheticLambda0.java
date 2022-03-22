package com.android.settingslib.users;

import android.app.IStopUserCallback;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.screenrecord.ScreenRecordDialog;
import com.android.systemui.screenrecord.ScreenRecordingAudioSource;
import com.android.systemui.statusbar.KeyguardIndicationController;
import com.android.systemui.statusbar.notification.row.NotificationInfo;
import java.util.List;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class AvatarPickerActivity$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ AvatarPickerActivity$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                AvatarPickerActivity avatarPickerActivity = (AvatarPickerActivity) this.f$0;
                int i = AvatarPickerActivity.$r8$clinit;
                Objects.requireNonNull(avatarPickerActivity);
                avatarPickerActivity.setResult(0);
                avatarPickerActivity.finish();
                return;
            case 1:
                ScreenRecordDialog screenRecordDialog = (ScreenRecordDialog) this.f$0;
                List<ScreenRecordingAudioSource> list = ScreenRecordDialog.MODES;
                Objects.requireNonNull(screenRecordDialog);
                screenRecordDialog.dismiss();
                return;
            case 2:
                KeyguardIndicationController keyguardIndicationController = (KeyguardIndicationController) this.f$0;
                Objects.requireNonNull(keyguardIndicationController);
                if (!keyguardIndicationController.mFalsingManager.isFalseTap(1)) {
                    int currentUser = KeyguardUpdateMonitor.getCurrentUser();
                    try {
                        keyguardIndicationController.mIActivityManager.switchUser(0);
                        keyguardIndicationController.mIActivityManager.stopUser(currentUser, true, (IStopUserCallback) null);
                        return;
                    } catch (RemoteException e) {
                        Log.e("KeyguardIndication", "Failed to logout user", e);
                        return;
                    }
                } else {
                    return;
                }
            default:
                NotificationInfo notificationInfo = (NotificationInfo) this.f$0;
                int i2 = NotificationInfo.$r8$clinit;
                Objects.requireNonNull(notificationInfo);
                notificationInfo.mIsAutomaticChosen = true;
                notificationInfo.applyAlertingBehavior(2, true);
                return;
        }
    }
}
