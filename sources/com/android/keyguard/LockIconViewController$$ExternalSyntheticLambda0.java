package com.android.keyguard;

import com.android.systemui.dreams.DreamOverlayService;
import com.android.systemui.qs.tiles.dialog.InternetDialog;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.connectivity.NetworkControllerImpl;
import com.google.android.systemui.assist.OpaLayout;
import java.util.HashSet;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class LockIconViewController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ LockIconViewController$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                LockIconViewController lockIconViewController = (LockIconViewController) this.f$0;
                Objects.requireNonNull(lockIconViewController);
                lockIconViewController.updateIsUdfpsEnrolled();
                lockIconViewController.updateConfiguration();
                return;
            case 1:
                ((DreamOverlayService) this.f$0).requestExit();
                return;
            case 2:
                InternetDialog internetDialog = (InternetDialog) this.f$0;
                boolean z = InternetDialog.DEBUG;
                Objects.requireNonNull(internetDialog);
                internetDialog.updateDialog(true);
                return;
            case 3:
                NotificationMediaManager notificationMediaManager = (NotificationMediaManager) this.f$0;
                HashSet<Integer> hashSet = NotificationMediaManager.PAUSED_MEDIA_STATES;
                Objects.requireNonNull(notificationMediaManager);
                notificationMediaManager.mBackdrop.setVisibility(8);
                notificationMediaManager.mBackdropFront.animate().cancel();
                notificationMediaManager.mBackdropBack.setImageDrawable(null);
                notificationMediaManager.mMainExecutor.execute(notificationMediaManager.mHideBackdropFront);
                return;
            case 4:
                ((NetworkControllerImpl) this.f$0).handleConfigurationChanged();
                return;
            default:
                OpaLayout opaLayout = (OpaLayout) this.f$0;
                int i = OpaLayout.$r8$clinit;
                Objects.requireNonNull(opaLayout);
                if (opaLayout.mCurrentAnimators.isEmpty()) {
                    opaLayout.startDiamondAnimation();
                    return;
                }
                return;
        }
    }
}
