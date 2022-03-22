package com.android.wifitrackerlib;

import android.graphics.PointF;
import android.os.Trace;
import android.os.VibrationAttributes;
import android.widget.FrameLayout;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.lifecycle.Lifecycle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.android.settingslib.fuelgauge.Estimate;
import com.android.settingslib.utils.PowerUtil;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.navigationbar.NavigationBar;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tiles.ScreenRecordTile;
import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import com.android.systemui.statusbar.connectivity.AccessPointControllerImpl;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.BatteryControllerImpl;
import com.android.wifitrackerlib.WifiEntry;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.bubbles.BubblePositioner;
import com.android.wm.shell.bubbles.BubbleStackView;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda19;
import com.android.wm.shell.bubbles.BubbleViewProvider;
import com.android.wm.shell.bubbles.animation.AnimatableScaleMatrix;
import com.android.wm.shell.onehanded.OneHandedController;
import java.util.Iterator;
import java.util.Objects;
import kotlin.jvm.functions.Function1;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class StandardWifiEntry$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ StandardWifiEntry$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        String str;
        boolean z;
        int i;
        float f;
        float f2;
        switch (this.$r8$classId) {
            case 0:
                ((InternetDialogController.WifiEntryConnectCallback) ((WifiEntry.ConnectCallback) this.f$0)).onConnectResult(1);
                return;
            case 1:
                SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) this.f$0;
                int i2 = SwipeRefreshLayout.CIRCLE_DIAMETER;
                Objects.requireNonNull(swipeRefreshLayout);
                swipeRefreshLayout.reset();
                return;
            case 2:
                UdfpsController udfpsController = (UdfpsController) this.f$0;
                VibrationAttributes vibrationAttributes = UdfpsController.VIBRATION_ATTRIBUTES;
                Objects.requireNonNull(udfpsController);
                udfpsController.mFingerprintManager.onUiReady(udfpsController.mSensorProps.sensorId);
                udfpsController.mLatencyTracker.onActionEnd(14);
                Trace.endAsyncSection("UdfpsController.e2e.startIllumination", 0);
                return;
            case 3:
                NavigationBar navigationBar = (NavigationBar) this.f$0;
                Objects.requireNonNull(navigationBar);
                NavigationBarView navigationBarView = navigationBar.mNavigationBarView;
                Objects.requireNonNull(navigationBarView);
                navigationBarView.mLayoutTransitionsEnabled = true;
                navigationBarView.updateLayoutTransitionsEnabled();
                return;
            case 4:
                ScreenRecordTile screenRecordTile = (ScreenRecordTile) this.f$0;
                int i3 = ScreenRecordTile.$r8$clinit;
                Objects.requireNonNull(screenRecordTile);
                screenRecordTile.mDialogLaunchAnimator.disableAllCurrentDialogsExitAnimations();
                screenRecordTile.mHost.collapsePanels();
                return;
            case 5:
                AccessPointControllerImpl accessPointControllerImpl = (AccessPointControllerImpl) this.f$0;
                boolean z2 = AccessPointControllerImpl.DEBUG;
                Objects.requireNonNull(accessPointControllerImpl);
                accessPointControllerImpl.mLifecycle.setCurrentState(Lifecycle.State.STARTED);
                return;
            case FalsingManager.VERSION /* 6 */:
                ((NotificationStackScrollLayout) this.f$0).updateBackgroundDimming();
                return;
            case 7:
                StatusBar statusBar = (StatusBar) this.f$0;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                statusBar.hideKeyguard();
                statusBar.mStatusBarKeyguardViewManager.onKeyguardFadedAway();
                return;
            case 8:
                BatteryControllerImpl batteryControllerImpl = (BatteryControllerImpl) this.f$0;
                boolean z3 = BatteryControllerImpl.DEBUG;
                Objects.requireNonNull(batteryControllerImpl);
                synchronized (batteryControllerImpl.mFetchCallbacks) {
                    synchronized (batteryControllerImpl.mFetchCallbacks) {
                        Estimate estimate = batteryControllerImpl.mEstimate;
                        if (estimate == null) {
                            str = null;
                        } else {
                            str = PowerUtil.getBatteryRemainingShortStringFormatted(batteryControllerImpl.mContext, estimate.estimateMillis);
                        }
                    }
                    Iterator<BatteryController.EstimateFetchCompletion> it = batteryControllerImpl.mFetchCallbacks.iterator();
                    while (it.hasNext()) {
                        it.next().onBatteryRemainingEstimateRetrieved(str);
                    }
                    batteryControllerImpl.mFetchCallbacks.clear();
                }
                return;
            case 9:
                BubbleStackView bubbleStackView = (BubbleStackView) this.f$0;
                int i4 = BubbleStackView.FLYOUT_HIDE_AFTER;
                Objects.requireNonNull(bubbleStackView);
                if (bubbleStackView.mIsExpanded) {
                    bubbleStackView.mIsBubbleSwitchAnimating = true;
                    FrameLayout frameLayout = bubbleStackView.mAnimatingOutSurfaceContainer;
                    Function1<Object, ? extends PhysicsAnimator<?>> function1 = PhysicsAnimator.instanceConstructor;
                    PhysicsAnimator.Companion.getInstance(frameLayout).cancel();
                    bubbleStackView.mAnimatingOutSurfaceAlphaAnimator.reverse();
                    bubbleStackView.mExpandedViewAlphaAnimator.start();
                    if (bubbleStackView.mPositioner.showBubblesVertically()) {
                        if (bubbleStackView.mStackAnimationController.isStackOnLeftSide()) {
                            f2 = bubbleStackView.mAnimatingOutSurfaceContainer.getTranslationX() + (bubbleStackView.mBubbleSize * 2);
                        } else {
                            f2 = bubbleStackView.mAnimatingOutSurfaceContainer.getTranslationX();
                        }
                        PhysicsAnimator instance = PhysicsAnimator.Companion.getInstance(bubbleStackView.mAnimatingOutSurfaceContainer);
                        instance.spring(DynamicAnimation.TRANSLATION_X, f2, 0.0f, bubbleStackView.mTranslateSpringConfig);
                        instance.start();
                    } else {
                        PhysicsAnimator instance2 = PhysicsAnimator.Companion.getInstance(bubbleStackView.mAnimatingOutSurfaceContainer);
                        instance2.spring(DynamicAnimation.TRANSLATION_Y, bubbleStackView.mAnimatingOutSurfaceContainer.getTranslationY() - bubbleStackView.mBubbleSize, 0.0f, bubbleStackView.mTranslateSpringConfig);
                        instance2.start();
                    }
                    BubbleViewProvider bubbleViewProvider = bubbleStackView.mExpandedBubble;
                    if (bubbleViewProvider == null || !bubbleViewProvider.getKey().equals("Overflow")) {
                        z = false;
                    } else {
                        z = true;
                    }
                    BubblePositioner bubblePositioner = bubbleStackView.mPositioner;
                    if (z) {
                        i = bubbleStackView.mBubbleContainer.getChildCount() - 1;
                    } else {
                        i = bubbleStackView.mBubbleData.getBubbles().indexOf(bubbleStackView.mExpandedBubble);
                    }
                    PointF expandedBubbleXY = bubblePositioner.getExpandedBubbleXY(i, bubbleStackView.getState());
                    bubbleStackView.mExpandedViewContainer.setAlpha(1.0f);
                    bubbleStackView.mExpandedViewContainer.setVisibility(0);
                    if (bubbleStackView.mPositioner.showBubblesVertically()) {
                        float f3 = expandedBubbleXY.y;
                        float f4 = bubbleStackView.mBubbleSize;
                        float f5 = (f4 / 2.0f) + f3;
                        if (bubbleStackView.mStackOnLeftOrWillBe) {
                            f = expandedBubbleXY.x + f4 + bubbleStackView.mExpandedViewPadding;
                        } else {
                            f = expandedBubbleXY.x - bubbleStackView.mExpandedViewPadding;
                        }
                        bubbleStackView.mExpandedViewContainerMatrix.setScale(0.9f, 0.9f, f, f5);
                    } else {
                        AnimatableScaleMatrix animatableScaleMatrix = bubbleStackView.mExpandedViewContainerMatrix;
                        float f6 = expandedBubbleXY.x;
                        float f7 = bubbleStackView.mBubbleSize;
                        animatableScaleMatrix.setScale(0.9f, 0.9f, (f7 / 2.0f) + f6, expandedBubbleXY.y + f7 + bubbleStackView.mExpandedViewPadding);
                    }
                    bubbleStackView.mExpandedViewContainer.setAnimationMatrix(bubbleStackView.mExpandedViewContainerMatrix);
                    bubbleStackView.mDelayedAnimationExecutor.executeDelayed(new BubbleStackView$$ExternalSyntheticLambda19(bubbleStackView, 0), 25L);
                    return;
                }
                return;
            default:
                OneHandedController.OneHandedImpl oneHandedImpl = (OneHandedController.OneHandedImpl) this.f$0;
                int i5 = OneHandedController.OneHandedImpl.$r8$clinit;
                Objects.requireNonNull(oneHandedImpl);
                OneHandedController.this.stopOneHanded();
                return;
        }
    }
}
