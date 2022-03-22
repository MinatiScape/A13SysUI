package com.android.wifitrackerlib;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.ICompatCameraControlCallback;
import android.content.res.Configuration;
import android.util.Log;
import android.view.ViewRootImpl;
import android.view.animation.PathInterpolator;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.android.systemui.ScreenDecorations$$ExternalSyntheticLambda1;
import com.android.systemui.accessibility.MagnificationModeSwitch;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.navigationbar.gestural.NavigationBarEdgePanel;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.QSFgsManagerFooter$$ExternalSyntheticLambda0;
import com.android.systemui.screenshot.ScreenshotController;
import com.android.systemui.statusbar.phone.StatusBarHeadsUpChangeListener;
import com.android.wifitrackerlib.WifiPickerTracker;
import com.android.wm.shell.legacysplitscreen.DividerImeController;
import com.android.wm.shell.legacysplitscreen.DividerView;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.onehanded.OneHandedController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BaseWifiTracker$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ BaseWifiTracker$$ExternalSyntheticLambda1(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ValueAnimator valueAnimator;
        switch (this.$r8$classId) {
            case 0:
                BaseWifiTracker baseWifiTracker = (BaseWifiTracker) this.f$0;
                Objects.requireNonNull(baseWifiTracker);
                if (!baseWifiTracker.mIsStarted) {
                    baseWifiTracker.mIsStarted = true;
                    baseWifiTracker.handleOnStart();
                    return;
                }
                return;
            case 1:
                MagnificationModeSwitch magnificationModeSwitch = (MagnificationModeSwitch) this.f$0;
                Objects.requireNonNull(magnificationModeSwitch);
                magnificationModeSwitch.removeButton();
                return;
            case 2:
                ((NavigationBarView) this.f$0).updateStates();
                return;
            case 3:
                NavigationBarEdgePanel navigationBarEdgePanel = (NavigationBarEdgePanel) this.f$0;
                PathInterpolator pathInterpolator = NavigationBarEdgePanel.RUBBER_BAND_INTERPOLATOR;
                Objects.requireNonNull(navigationBarEdgePanel);
                navigationBarEdgePanel.mAngleOffset = Math.max(0.0f, navigationBarEdgePanel.mAngleOffset + 8.0f);
                navigationBarEdgePanel.updateAngle(true);
                SpringAnimation springAnimation = navigationBarEdgePanel.mTranslationAnimation;
                SpringForce springForce = navigationBarEdgePanel.mTriggerBackSpring;
                Objects.requireNonNull(springAnimation);
                springAnimation.mSpring = springForce;
                navigationBarEdgePanel.setDesiredTranslation(navigationBarEdgePanel.mDesiredTranslation - (navigationBarEdgePanel.mDensity * 32.0f), true);
                navigationBarEdgePanel.animate().alpha(0.0f).setDuration(80L).withEndAction(new QSFgsManagerFooter$$ExternalSyntheticLambda0(navigationBarEdgePanel, 3));
                navigationBarEdgePanel.mArrowDisappearAnimation.start();
                navigationBarEdgePanel.mHandler.removeCallbacks(navigationBarEdgePanel.mFailsafeRunnable);
                navigationBarEdgePanel.mHandler.postDelayed(navigationBarEdgePanel.mFailsafeRunnable, 200L);
                return;
            case 4:
                final ScreenshotController screenshotController = (ScreenshotController) this.f$0;
                ScreenshotController.AnonymousClass1 r0 = ScreenshotController.SCREENSHOT_REMOTE_RUNNER;
                Objects.requireNonNull(screenshotController);
                screenshotController.requestScrollCapture();
                screenshotController.mWindow.peekDecorView().getViewRootImpl().setActivityConfigCallback(new ViewRootImpl.ActivityConfigCallback() { // from class: com.android.systemui.screenshot.ScreenshotController.4
                    public final void onConfigurationChanged(Configuration configuration, int i) {
                        ScreenshotController screenshotController2 = screenshotController;
                        if (screenshotController2.mConfigChanges.applyNewConfig(screenshotController2.mContext.getResources())) {
                            ScreenshotView screenshotView = screenshotController.mScreenshotView;
                            Objects.requireNonNull(screenshotView);
                            screenshotView.mScrollChip.setVisibility(8);
                            ScreenshotController screenshotController3 = screenshotController;
                            screenshotController3.mScreenshotHandler.postDelayed(new ScreenDecorations$$ExternalSyntheticLambda1(screenshotController3, 4), 150L);
                            ScreenshotController screenshotController4 = screenshotController;
                            screenshotController4.mScreenshotView.updateInsets(screenshotController4.mWindowManager.getCurrentWindowMetrics().getWindowInsets());
                            AnimatorSet animatorSet = screenshotController.mScreenshotAnimation;
                            if (animatorSet != null && animatorSet.isRunning()) {
                                screenshotController.mScreenshotAnimation.end();
                            }
                        }
                    }

                    public final void requestCompatCameraControl(boolean z, boolean z2, ICompatCameraControlCallback iCompatCameraControlCallback) {
                        AnonymousClass1 r02 = ScreenshotController.SCREENSHOT_REMOTE_RUNNER;
                        Log.w("Screenshot", "Unexpected requestCompatCameraControl callback");
                    }
                });
                return;
            case 5:
                StatusBarHeadsUpChangeListener statusBarHeadsUpChangeListener = (StatusBarHeadsUpChangeListener) this.f$0;
                Objects.requireNonNull(statusBarHeadsUpChangeListener);
                statusBarHeadsUpChangeListener.mNotificationShadeWindowController.setForceWindowCollapsed(false);
                return;
            case FalsingManager.VERSION /* 6 */:
                Objects.requireNonNull((WifiPickerTracker.WifiPickerTrackerCallback) this.f$0);
                return;
            case 7:
                DividerImeController dividerImeController = (DividerImeController) this.f$0;
                Objects.requireNonNull(dividerImeController);
                if (dividerImeController.mPaused) {
                    dividerImeController.mPaused = false;
                    dividerImeController.mTargetAdjusted = dividerImeController.mPausedTargetAdjusted;
                    dividerImeController.updateDimTargets();
                    LegacySplitScreenController legacySplitScreenController = dividerImeController.mSplits.mSplitScreenController;
                    Objects.requireNonNull(legacySplitScreenController);
                    DividerView dividerView = legacySplitScreenController.mView;
                    if (dividerImeController.mTargetAdjusted != dividerImeController.mAdjusted) {
                        LegacySplitScreenController legacySplitScreenController2 = dividerImeController.mSplits.mSplitScreenController;
                        Objects.requireNonNull(legacySplitScreenController2);
                        if (!(legacySplitScreenController2.mMinimized || dividerView == null || (valueAnimator = dividerView.mCurrentAnimator) == null)) {
                            valueAnimator.end();
                        }
                    }
                    dividerImeController.updateImeAdjustState(false);
                    dividerImeController.startAsyncAnimation();
                    return;
                }
                return;
            case 8:
                ((OneHandedController) this.f$0).onSwipeToNotificationEnabledChanged();
                return;
            default:
                ((ValueAnimator) this.f$0).start();
                return;
        }
    }
}
