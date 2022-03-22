package com.android.systemui.volume;

import android.view.animation.LinearInterpolator;
import com.android.systemui.qs.tiles.dialog.InternetDialog;
import com.android.systemui.screenshot.ImageLoader$$ExternalSyntheticLambda0;
import com.android.systemui.shared.rotation.RotationButtonController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.wm.shell.pip.phone.PipInputConsumer;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class VolumeDialogImpl$$ExternalSyntheticLambda11 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ VolumeDialogImpl$$ExternalSyntheticLambda11(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                VolumeDialogImpl volumeDialogImpl = (VolumeDialogImpl) this.f$0;
                Objects.requireNonNull(volumeDialogImpl);
                volumeDialogImpl.mRingerDrawerContainer.setVisibility(4);
                return;
            case 1:
                InternetDialog internetDialog = (InternetDialog) this.f$0;
                boolean z = InternetDialog.DEBUG;
                Objects.requireNonNull(internetDialog);
                internetDialog.updateDialog(true);
                return;
            case 2:
                RotationButtonController rotationButtonController = (RotationButtonController) this.f$0;
                LinearInterpolator linearInterpolator = RotationButtonController.LINEAR_INTERPOLATOR;
                Objects.requireNonNull(rotationButtonController);
                rotationButtonController.mPendingRotationSuggestion = false;
                return;
            case 3:
                NotificationPanelViewController.KeyguardAffordanceHelperCallback keyguardAffordanceHelperCallback = (NotificationPanelViewController.KeyguardAffordanceHelperCallback) this.f$0;
                Objects.requireNonNull(keyguardAffordanceHelperCallback);
                NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
                notificationPanelViewController.mKeyguardBottomArea.launchCamera(notificationPanelViewController.mLastCameraLaunchSource);
                return;
            default:
                PipInputConsumer pipInputConsumer = (PipInputConsumer) this.f$0;
                Objects.requireNonNull(pipInputConsumer);
                PipInputConsumer.RegistrationListener registrationListener = pipInputConsumer.mRegistrationListener;
                if (registrationListener != null) {
                    ((PipTouchHandler) ((ImageLoader$$ExternalSyntheticLambda0) registrationListener).f$0).onRegistrationChanged(false);
                    return;
                }
                return;
        }
    }
}
