package com.android.wm.shell;

import android.graphics.Rect;
import android.os.Trace;
import android.util.Log;
import com.android.systemui.biometrics.AuthBiometricView;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.PanelViewController;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.wm.shell.bubbles.BubbleStackView;
import com.android.wm.shell.compatui.letterboxedu.LetterboxEduWindowManager;
import com.google.android.systemui.dreamliner.DockGestureController;
import com.google.android.systemui.reversecharging.ReverseChargingController;
import com.google.android.systemui.reversecharging.ReverseWirelessCharger;
import java.util.Objects;
import vendor.google.wireless_charger.V1_2.IWirelessCharger;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class TaskView$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ TaskView$$ExternalSyntheticLambda6(Object obj, int i) {
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
                if (taskView.mTaskToken != null) {
                    if (taskView.isUsingShellTransitions()) {
                        taskView.mTaskViewTransitions.setTaskViewVisible(taskView, true);
                        return;
                    }
                    taskView.mTransaction.reparent(taskView.mTaskLeash, taskView.getSurfaceControl()).show(taskView.mTaskLeash).apply();
                    taskView.updateTaskVisibility();
                    return;
                }
                return;
            case 1:
                AuthBiometricView.m28$r8$lambda$2rwDSgFbOdOVjOmncQYw0Cq8((AuthBiometricView) this.f$0);
                return;
            case 2:
                NotificationStackScrollLayoutController notificationStackScrollLayoutController = (NotificationStackScrollLayoutController) this.f$0;
                Objects.requireNonNull(notificationStackScrollLayoutController);
                notificationStackScrollLayoutController.updateFooter();
                if (!notificationStackScrollLayoutController.mNotifPipelineFlags.isNewPipelineEnabled()) {
                    Trace.beginSection("NSSLC.updateSectionBoundaries");
                    NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
                    Objects.requireNonNull(notificationStackScrollLayout);
                    notificationStackScrollLayout.mSectionsManager.updateSectionBoundaries("dynamic privacy changed");
                    Trace.endSection();
                    return;
                }
                return;
            case 3:
                NotificationPanelViewController notificationPanelViewController = (NotificationPanelViewController) this.f$0;
                Rect rect = NotificationPanelViewController.M_DUMMY_DIRTY_RECT;
                Objects.requireNonNull(notificationPanelViewController);
                QS qs = notificationPanelViewController.mQs;
                if (qs != null) {
                    qs.animateHeaderSlidingOut();
                    return;
                }
                return;
            case 4:
                PanelViewController panelViewController = (PanelViewController) this.f$0;
                int i2 = PanelViewController.$r8$clinit;
                Objects.requireNonNull(panelViewController);
                panelViewController.mLatencyTracker.onActionEnd(0);
                return;
            case 5:
                StatusBarKeyguardViewManager statusBarKeyguardViewManager = (StatusBarKeyguardViewManager) this.f$0;
                Objects.requireNonNull(statusBarKeyguardViewManager);
                statusBarKeyguardViewManager.mNotificationShadeWindowController.setKeyguardFadingAway(false);
                return;
            case FalsingManager.VERSION /* 6 */:
                BubbleStackView bubbleStackView = (BubbleStackView) this.f$0;
                int i3 = BubbleStackView.FLYOUT_HIDE_AFTER;
                Objects.requireNonNull(bubbleStackView);
                bubbleStackView.mExpandedViewTemporarilyHidden = false;
                bubbleStackView.mIsBubbleSwitchAnimating = false;
                bubbleStackView.mExpandedViewContainer.setAnimationMatrix(null);
                return;
            case 7:
                LetterboxEduWindowManager.$r8$lambda$4SqjxbhVhyBXNciFEvygSHxAi3k((LetterboxEduWindowManager) this.f$0);
                return;
            case 8:
                DockGestureController dockGestureController = (DockGestureController) this.f$0;
                int i4 = DockGestureController.$r8$clinit;
                Objects.requireNonNull(dockGestureController);
                dockGestureController.hidePhotoPreview(false);
                return;
            default:
                ReverseChargingController reverseChargingController = (ReverseChargingController) this.f$0;
                boolean z = ReverseChargingController.DEBUG;
                if (z) {
                    Objects.requireNonNull(reverseChargingController);
                    Log.d("ReverseChargingControl", "requestReverseInformation()");
                }
                if (reverseChargingController.mRtxChargerManagerOptional.isPresent()) {
                    ReverseWirelessCharger reverseWirelessCharger = reverseChargingController.mRtxChargerManagerOptional.get();
                    Objects.requireNonNull(reverseWirelessCharger);
                    reverseWirelessCharger.initHALInterface();
                    IWirelessCharger iWirelessCharger = reverseWirelessCharger.mWirelessCharger;
                    if (iWirelessCharger != null) {
                        try {
                            iWirelessCharger.getRtxInformation(reverseWirelessCharger.mLocalRtxInformationCallback);
                            return;
                        } catch (Exception e) {
                            Log.i("ReverseWirelessCharger", "getRtxInformation fail: ", e);
                            return;
                        }
                    } else {
                        return;
                    }
                } else if (z) {
                    Log.d("ReverseChargingControl", "requestReverseInformation(): mRtxChargerManagerOptional is not present!");
                    return;
                } else {
                    return;
                }
        }
    }
}
