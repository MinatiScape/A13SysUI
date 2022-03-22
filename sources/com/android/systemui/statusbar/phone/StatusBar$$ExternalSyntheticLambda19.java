package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.animation.PathInterpolator;
import androidx.dynamicanimation.animation.DynamicAnimation;
import com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda0;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.biometrics.UdfpsControllerOverlay;
import com.android.systemui.biometrics.UdfpsEnrollHelper;
import com.android.systemui.biometrics.UdfpsEnrollView;
import com.android.systemui.biometrics.UdfpsEnrollViewController;
import com.android.systemui.keyguard.DismissCallbackWrapper;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.recents.OverviewProxyRecentsImpl;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.KeyguardIndicationController;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.legacysplitscreen.DividerView;
import com.android.wm.shell.pip.phone.PipController;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import com.google.android.systemui.dreamliner.DockGestureController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBar$$ExternalSyntheticLambda19 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ StatusBar$$ExternalSyntheticLambda19(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        UdfpsEnrollHelper.Listener listener;
        switch (this.$r8$classId) {
            case 0:
                ((StatusBar) this.f$0).makeExpandedInvisible();
                return;
            case 1:
                UdfpsController.UdfpsOverlayController udfpsOverlayController = (UdfpsController.UdfpsOverlayController) this.f$0;
                int i = UdfpsController.UdfpsOverlayController.$r8$clinit;
                Objects.requireNonNull(udfpsOverlayController);
                UdfpsControllerOverlay udfpsControllerOverlay = UdfpsController.this.mOverlay;
                if (udfpsControllerOverlay == null) {
                    Log.e("UdfpsController", "onEnrollmentHelp received but serverRequest is null");
                    return;
                }
                UdfpsEnrollHelper udfpsEnrollHelper = udfpsControllerOverlay.enrollHelper;
                if (udfpsEnrollHelper != null && (listener = udfpsEnrollHelper.mListener) != null) {
                    final int i2 = udfpsEnrollHelper.mRemainingSteps;
                    final int i3 = udfpsEnrollHelper.mTotalSteps;
                    final UdfpsEnrollView udfpsEnrollView = (UdfpsEnrollView) UdfpsEnrollViewController.this.mView;
                    Objects.requireNonNull(udfpsEnrollView);
                    udfpsEnrollView.mHandler.post(new Runnable() { // from class: com.android.systemui.biometrics.UdfpsEnrollView$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            UdfpsEnrollView udfpsEnrollView2 = UdfpsEnrollView.this;
                            int i4 = i2;
                            int i5 = i3;
                            int i6 = UdfpsEnrollView.$r8$clinit;
                            Objects.requireNonNull(udfpsEnrollView2);
                            UdfpsEnrollProgressBarDrawable udfpsEnrollProgressBarDrawable = udfpsEnrollView2.mFingerprintProgressDrawable;
                            Objects.requireNonNull(udfpsEnrollProgressBarDrawable);
                            udfpsEnrollProgressBarDrawable.updateState(i4, i5, true);
                        }
                    });
                    return;
                }
                return;
            case 2:
                DismissCallbackWrapper dismissCallbackWrapper = (DismissCallbackWrapper) this.f$0;
                Objects.requireNonNull(dismissCallbackWrapper);
                try {
                    dismissCallbackWrapper.mCallback.onDismissSucceeded();
                    return;
                } catch (RemoteException e) {
                    Log.i("DismissCallbackWrapper", "Failed to call callback", e);
                    return;
                }
            case 3:
                OverviewProxyRecentsImpl overviewProxyRecentsImpl = (OverviewProxyRecentsImpl) this.f$0;
                Objects.requireNonNull(overviewProxyRecentsImpl);
                try {
                    OverviewProxyService overviewProxyService = overviewProxyRecentsImpl.mOverviewProxyService;
                    Objects.requireNonNull(overviewProxyService);
                    if (overviewProxyService.mOverviewProxy != null) {
                        OverviewProxyService overviewProxyService2 = overviewProxyRecentsImpl.mOverviewProxyService;
                        Objects.requireNonNull(overviewProxyService2);
                        overviewProxyService2.mOverviewProxy.onOverviewToggle();
                        OverviewProxyService overviewProxyService3 = overviewProxyRecentsImpl.mOverviewProxyService;
                        Objects.requireNonNull(overviewProxyService3);
                        int size = overviewProxyService3.mConnectionCallbacks.size();
                        while (true) {
                            size--;
                            if (size >= 0) {
                                ((OverviewProxyService.OverviewProxyListener) overviewProxyService3.mConnectionCallbacks.get(size)).onToggleRecentApps();
                            } else {
                                return;
                            }
                        }
                    } else {
                        return;
                    }
                } catch (RemoteException e2) {
                    Log.e("OverviewProxyRecentsImpl", "Cannot send toggle recents through proxy service.", e2);
                    return;
                }
            case 4:
                PanelViewController panelViewController = (PanelViewController) this.f$0;
                int i4 = PanelViewController.$r8$clinit;
                Objects.requireNonNull(panelViewController);
                panelViewController.notifyExpandingFinished();
                NotificationPanelViewController notificationPanelViewController = (NotificationPanelViewController) panelViewController;
                StatusBar statusBar = notificationPanelViewController.mStatusBar;
                Objects.requireNonNull(statusBar);
                KeyguardIndicationController keyguardIndicationController = statusBar.mKeyguardIndicationController;
                Objects.requireNonNull(keyguardIndicationController);
                KeyguardIndicationController.AnonymousClass5 r3 = keyguardIndicationController.mHandler;
                r3.sendMessageDelayed(r3.obtainMessage(1), 1200L);
                NotificationStackScrollLayoutController notificationStackScrollLayoutController = notificationPanelViewController.mNotificationStackScrollLayoutController;
                Objects.requireNonNull(notificationStackScrollLayoutController);
                NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
                Objects.requireNonNull(notificationStackScrollLayout);
                AmbientState ambientState = notificationStackScrollLayout.mAmbientState;
                Objects.requireNonNull(ambientState);
                ambientState.mUnlockHintRunning = false;
                panelViewController.mHintAnimationRunning = false;
                return;
            case 5:
                StatusBarKeyguardViewManager statusBarKeyguardViewManager = (StatusBarKeyguardViewManager) this.f$0;
                Objects.requireNonNull(statusBarKeyguardViewManager);
                statusBarKeyguardViewManager.mStatusBar.hideKeyguard();
                return;
            case FalsingManager.VERSION /* 6 */:
                DividerView dividerView = (DividerView) this.f$0;
                PathInterpolator pathInterpolator = DividerView.IME_ADJUST_INTERPOLATOR;
                Objects.requireNonNull(dividerView);
                if (dividerView.getViewRootImpl() != null) {
                    if (dividerView.isHorizontalDivision()) {
                        dividerView.mTmpMatrix.setTranslate(0.0f, dividerView.mDividerPositionY - dividerView.mDividerInsets);
                    } else {
                        dividerView.mTmpMatrix.setTranslate(dividerView.mDividerPositionX - dividerView.mDividerInsets, 0.0f);
                    }
                    dividerView.mTmpMatrix.getValues(dividerView.mTmpValues);
                    try {
                        dividerView.getViewRootImpl().getAccessibilityEmbeddedConnection().setScreenMatrix(dividerView.mTmpValues);
                        return;
                    } catch (RemoteException unused) {
                        return;
                    }
                } else {
                    return;
                }
            case 7:
                PipController.PipImpl pipImpl = (PipController.PipImpl) this.f$0;
                Objects.requireNonNull(pipImpl);
                PipController pipController = PipController.this;
                Objects.requireNonNull(pipController);
                PipTouchHandler pipTouchHandler = pipController.mTouchHandler;
                Objects.requireNonNull(pipTouchHandler);
                pipTouchHandler.mPipDismissTargetHandler.init();
                Context context = pipController.mContext;
                pipController.onDisplayChanged(new DisplayLayout(context, context.getDisplay()), false);
                return;
            case 8:
                ((PipTouchHandler) this.f$0).updateMovementBounds();
                return;
            case 9:
                ((AssistManager.UiController) this.f$0).hide();
                return;
            default:
                DockGestureController dockGestureController = (DockGestureController) this.f$0;
                int i5 = DockGestureController.$r8$clinit;
                Objects.requireNonNull(dockGestureController);
                PhysicsAnimator<View> physicsAnimator = dockGestureController.mPreviewTargetAnimator;
                physicsAnimator.spring(DynamicAnimation.TRANSLATION_X, dockGestureController.mPhotoPreview.getRight(), dockGestureController.mVelocityX, dockGestureController.mTargetSpringConfig);
                physicsAnimator.withEndActions(new CarrierTextManager$$ExternalSyntheticLambda0(dockGestureController, 12));
                physicsAnimator.start();
                return;
        }
    }
}
