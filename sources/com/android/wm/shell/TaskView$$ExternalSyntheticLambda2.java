package com.android.wm.shell;

import android.util.Log;
import androidx.activity.ComponentActivity;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.qs.QSAnimator;
import com.android.systemui.qs.QSFragment;
import com.android.systemui.qs.QSPanelController;
import com.android.systemui.statusbar.notification.collection.coordinator.CommunalCoordinator;
import com.android.systemui.statusbar.notification.row.StackScrollerDecorView;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class TaskView$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ TaskView$$ExternalSyntheticLambda2(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                TaskView taskView = (TaskView) this.f$0;
                int i = TaskView.$r8$clinit;
                Objects.requireNonNull(taskView);
                taskView.mListener.onInitialized();
                return;
            case 1:
                ComponentActivity componentActivity = (ComponentActivity) this.f$0;
                Objects.requireNonNull(componentActivity);
                componentActivity.invalidateOptionsMenu();
                return;
            case 2:
                UdfpsController.UdfpsOverlayController udfpsOverlayController = (UdfpsController.UdfpsOverlayController) this.f$0;
                int i2 = UdfpsController.UdfpsOverlayController.$r8$clinit;
                Objects.requireNonNull(udfpsOverlayController);
                if (UdfpsController.this.mKeyguardUpdateMonitor.isFingerprintDetectionRunning()) {
                    Log.d("UdfpsController", "hiding udfps overlay when mKeyguardUpdateMonitor.isFingerprintDetectionRunning()=true");
                }
                UdfpsController.this.hideUdfpsOverlay();
                return;
            case 3:
                QSFragment qSFragment = (QSFragment) this.f$0;
                int i3 = QSFragment.$r8$clinit;
                Objects.requireNonNull(qSFragment);
                QSPanelController qSPanelController = qSFragment.mQSPanelController;
                Objects.requireNonNull(qSPanelController);
                qSPanelController.mMediaHost.getHostView().setAlpha(1.0f);
                QSAnimator qSAnimator = qSFragment.mQSAnimator;
                Objects.requireNonNull(qSAnimator);
                qSAnimator.mNeedsAnimatorUpdate = true;
                return;
            case 4:
                CommunalCoordinator.AnonymousClass2 r2 = (CommunalCoordinator.AnonymousClass2) this.f$0;
                Objects.requireNonNull(r2);
                CommunalCoordinator.this.mFilter.invalidateList();
                CommunalCoordinator.this.mNotificationEntryManager.updateNotifications("Communal mode state changed");
                return;
            default:
                StackScrollerDecorView stackScrollerDecorView = (StackScrollerDecorView) this.f$0;
                int i4 = StackScrollerDecorView.$r8$clinit;
                Objects.requireNonNull(stackScrollerDecorView);
                stackScrollerDecorView.mSecondaryAnimating = false;
                if (stackScrollerDecorView.mSecondaryView != null && stackScrollerDecorView.getVisibility() != 8 && stackScrollerDecorView.mSecondaryView.getVisibility() != 8 && !stackScrollerDecorView.mIsSecondaryVisible) {
                    stackScrollerDecorView.mSecondaryView.setVisibility(8);
                    return;
                }
                return;
        }
    }
}
