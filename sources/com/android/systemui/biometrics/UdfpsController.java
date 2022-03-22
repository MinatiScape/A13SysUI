package com.android.systemui.biometrics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.PointF;
import android.graphics.RectF;
import android.hardware.biometrics.SensorLocationInternal;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.hardware.fingerprint.IUdfpsOverlayController;
import android.hardware.fingerprint.IUdfpsOverlayControllerCallback;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.Trace;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.LatencyTracker;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline1;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline3;
import com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda1;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.biometrics.UdfpsEnrollHelper;
import com.android.systemui.biometrics.UdfpsSurfaceView;
import com.android.systemui.doze.DozeReceiver;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda19;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.time.SystemClock;
import com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda0;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda2;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import kotlin.jvm.functions.Function3;
/* loaded from: classes.dex */
public final class UdfpsController implements DozeReceiver {
    public final AccessibilityManager mAccessibilityManager;
    public final ActivityLaunchAnimator mActivityLaunchAnimator;
    public Runnable mAodInterruptRunnable;
    public boolean mAttemptedToDismissKeyguard;
    public Runnable mCancelAodTimeoutAction;
    public final ConfigurationController mConfigurationController;
    public final Context mContext;
    public final SystemUIDialogManager mDialogManager;
    public final DumpManager mDumpManager;
    public final Execution mExecution;
    public final FalsingManager mFalsingManager;
    public final DelayableExecutor mFgExecutor;
    public final FingerprintManager mFingerprintManager;
    public boolean mGoodCaptureReceived;
    public final UdfpsHbmProvider mHbmProvider;
    public final LayoutInflater mInflater;
    public boolean mIsAodInterruptActive;
    public final KeyguardBypassController mKeyguardBypassController;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final StatusBarKeyguardViewManager mKeyguardViewManager;
    public final LatencyTracker mLatencyTracker;
    public final LockscreenShadeTransitionController mLockscreenShadeTransitionController;
    public boolean mOnFingerDown;
    @VisibleForTesting
    public final BiometricDisplayListener mOrientationListener;
    public UdfpsControllerOverlay mOverlay;
    public final PanelExpansionStateManager mPanelExpansionStateManager;
    public final PowerManager mPowerManager;
    public final AnonymousClass1 mScreenObserver;
    public boolean mScreenOn;
    @VisibleForTesting
    public final FingerprintSensorPropertiesInternal mSensorProps;
    public final StatusBarStateController mStatusBarStateController;
    public final SystemClock mSystemClock;
    public long mTouchLogTime;
    public final UnlockedScreenOffAnimationController mUnlockedScreenOffAnimationController;
    public VelocityTracker mVelocityTracker;
    public final VibratorHelper mVibrator;
    public final WindowManager mWindowManager;
    @VisibleForTesting
    public static final VibrationAttributes VIBRATION_ATTRIBUTES = new VibrationAttributes.Builder().setUsage(65).build();
    public static final VibrationEffect EFFECT_CLICK = VibrationEffect.get(0);
    public int mActivePointerId = -1;
    public final HashSet mCallbacks = new HashSet();
    public final AnonymousClass2 mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.biometrics.UdfpsController.2
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            UdfpsControllerOverlay udfpsControllerOverlay = UdfpsController.this.mOverlay;
            if (udfpsControllerOverlay != null) {
                Objects.requireNonNull(udfpsControllerOverlay);
                if (udfpsControllerOverlay.requestReason != 4 && "android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(intent.getAction())) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ACTION_CLOSE_SYSTEM_DIALOGS received, mRequestReason: ");
                    UdfpsControllerOverlay udfpsControllerOverlay2 = UdfpsController.this.mOverlay;
                    Objects.requireNonNull(udfpsControllerOverlay2);
                    KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(m, udfpsControllerOverlay2.requestReason, "UdfpsController");
                    UdfpsControllerOverlay udfpsControllerOverlay3 = UdfpsController.this.mOverlay;
                    Objects.requireNonNull(udfpsControllerOverlay3);
                    try {
                        udfpsControllerOverlay3.controllerCallback.onUserCanceled();
                    } catch (RemoteException e) {
                        Log.e("UdfpsControllerOverlay", "Remote exception", e);
                    }
                    UdfpsController.this.hideUdfpsOverlay();
                }
            }
        }
    };

    /* loaded from: classes.dex */
    public interface Callback {
        void onFingerDown();

        void onFingerUp();
    }

    /* loaded from: classes.dex */
    public class UdfpsOverlayController extends IUdfpsOverlayController.Stub {
        public static final /* synthetic */ int $r8$clinit = 0;

        public UdfpsOverlayController() {
        }

        public final void hideUdfpsOverlay(int i) {
            UdfpsController.this.mFgExecutor.execute(new TaskView$$ExternalSyntheticLambda2(this, 2));
        }

        public final void onAcquiredGood(final int i) {
            UdfpsController.this.mFgExecutor.execute(new Runnable() { // from class: com.android.systemui.biometrics.UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    UdfpsController.UdfpsOverlayController udfpsOverlayController = UdfpsController.UdfpsOverlayController.this;
                    int i2 = i;
                    Objects.requireNonNull(udfpsOverlayController);
                    UdfpsController udfpsController = UdfpsController.this;
                    UdfpsControllerOverlay udfpsControllerOverlay = udfpsController.mOverlay;
                    if (udfpsControllerOverlay == null) {
                        KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m("Null request when onAcquiredGood for sensorId: ", i2, "UdfpsController");
                        return;
                    }
                    udfpsController.mGoodCaptureReceived = true;
                    UdfpsView udfpsView = udfpsControllerOverlay.overlayView;
                    if (udfpsView != null) {
                        udfpsView.stopIllumination();
                    }
                    UdfpsControllerOverlay udfpsControllerOverlay2 = UdfpsController.this.mOverlay;
                    Objects.requireNonNull(udfpsControllerOverlay2);
                    UdfpsEnrollHelper udfpsEnrollHelper = udfpsControllerOverlay2.enrollHelper;
                    if (udfpsEnrollHelper != null) {
                        UdfpsEnrollHelper.Listener listener = udfpsEnrollHelper.mListener;
                        if (listener == null) {
                            Log.e("UdfpsEnrollHelper", "animateIfLastStep, null listener");
                            return;
                        }
                        int i3 = udfpsEnrollHelper.mRemainingSteps;
                        if (i3 <= 2 && i3 >= 0) {
                            UdfpsEnrollView udfpsEnrollView = (UdfpsEnrollView) UdfpsEnrollViewController.this.mView;
                            Objects.requireNonNull(udfpsEnrollView);
                            Handler handler = udfpsEnrollView.mHandler;
                            UdfpsEnrollProgressBarDrawable udfpsEnrollProgressBarDrawable = udfpsEnrollView.mFingerprintProgressDrawable;
                            Objects.requireNonNull(udfpsEnrollProgressBarDrawable);
                            handler.post(new AccessPoint$$ExternalSyntheticLambda1(udfpsEnrollProgressBarDrawable, 1));
                        }
                    }
                }
            });
        }

        public final void onEnrollmentHelp(int i) {
            UdfpsController.this.mFgExecutor.execute(new StatusBar$$ExternalSyntheticLambda19(this, 1));
        }

        public final void onEnrollmentProgress(int i, final int i2) {
            UdfpsController.this.mFgExecutor.execute(new Runnable() { // from class: com.android.systemui.biometrics.UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    UdfpsController.UdfpsOverlayController udfpsOverlayController = UdfpsController.UdfpsOverlayController.this;
                    int i3 = i2;
                    Objects.requireNonNull(udfpsOverlayController);
                    UdfpsControllerOverlay udfpsControllerOverlay = UdfpsController.this.mOverlay;
                    if (udfpsControllerOverlay == null) {
                        Log.e("UdfpsController", "onEnrollProgress received but serverRequest is null");
                        return;
                    }
                    UdfpsEnrollHelper udfpsEnrollHelper = udfpsControllerOverlay.enrollHelper;
                    if (udfpsEnrollHelper != null) {
                        if (udfpsEnrollHelper.mTotalSteps == -1) {
                            udfpsEnrollHelper.mTotalSteps = i3;
                        }
                        if (i3 != udfpsEnrollHelper.mRemainingSteps) {
                            udfpsEnrollHelper.mLocationsEnrolled++;
                            if (udfpsEnrollHelper.isCenterEnrollmentStage()) {
                                udfpsEnrollHelper.mCenterTouchCount++;
                            }
                        }
                        udfpsEnrollHelper.mRemainingSteps = i3;
                        UdfpsEnrollHelper.Listener listener = udfpsEnrollHelper.mListener;
                        if (listener != null) {
                            int i4 = udfpsEnrollHelper.mTotalSteps;
                            UdfpsEnrollView udfpsEnrollView = (UdfpsEnrollView) UdfpsEnrollViewController.this.mView;
                            Objects.requireNonNull(udfpsEnrollView);
                            udfpsEnrollView.mHandler.post(new UdfpsEnrollView$$ExternalSyntheticLambda1(udfpsEnrollView, i3, i4));
                        }
                    }
                }
            });
        }

        public final void setDebugMessage(int i, String str) {
            UdfpsController.this.mFgExecutor.execute(new UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda0(this, str, 0));
        }

        public final void showUdfpsOverlay(int i, final int i2, final IUdfpsOverlayControllerCallback iUdfpsOverlayControllerCallback) {
            UdfpsController.this.mFgExecutor.execute(new Runnable() { // from class: com.android.systemui.biometrics.UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda3
                /* JADX WARN: Type inference failed for: r1v7, types: [com.android.systemui.biometrics.UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda4] */
                @Override // java.lang.Runnable
                public final void run() {
                    UdfpsController.UdfpsOverlayController udfpsOverlayController = UdfpsController.UdfpsOverlayController.this;
                    int i3 = i2;
                    IUdfpsOverlayControllerCallback iUdfpsOverlayControllerCallback2 = iUdfpsOverlayControllerCallback;
                    Objects.requireNonNull(udfpsOverlayController);
                    final UdfpsController udfpsController = UdfpsController.this;
                    udfpsController.showUdfpsOverlay(new UdfpsControllerOverlay(udfpsController.mContext, udfpsController.mFingerprintManager, udfpsController.mInflater, udfpsController.mWindowManager, udfpsController.mAccessibilityManager, udfpsController.mStatusBarStateController, udfpsController.mPanelExpansionStateManager, udfpsController.mKeyguardViewManager, udfpsController.mKeyguardUpdateMonitor, udfpsController.mDialogManager, udfpsController.mDumpManager, udfpsController.mLockscreenShadeTransitionController, udfpsController.mConfigurationController, udfpsController.mSystemClock, udfpsController.mKeyguardStateController, udfpsController.mUnlockedScreenOffAnimationController, udfpsController.mSensorProps, udfpsController.mHbmProvider, i3, iUdfpsOverlayControllerCallback2, new Function3() { // from class: com.android.systemui.biometrics.UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda4
                        @Override // kotlin.jvm.functions.Function3
                        public final Object invoke(Object obj, Object obj2, Object obj3) {
                            boolean booleanValue = ((Boolean) obj3).booleanValue();
                            VibrationAttributes vibrationAttributes = UdfpsController.VIBRATION_ATTRIBUTES;
                            return Boolean.valueOf(UdfpsController.this.onTouch((View) obj, (MotionEvent) obj2, booleanValue));
                        }
                    }, udfpsController.mActivityLaunchAnimator));
                }
            });
        }
    }

    /* JADX WARN: Type inference failed for: r3v2, types: [com.android.systemui.biometrics.UdfpsController$1, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r4v0, types: [com.android.systemui.biometrics.UdfpsController$2] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public UdfpsController(android.content.Context r10, com.android.systemui.util.concurrency.Execution r11, android.view.LayoutInflater r12, android.hardware.fingerprint.FingerprintManager r13, android.view.WindowManager r14, com.android.systemui.plugins.statusbar.StatusBarStateController r15, com.android.systemui.util.concurrency.DelayableExecutor r16, com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager r17, com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager r18, com.android.systemui.dump.DumpManager r19, com.android.keyguard.KeyguardUpdateMonitor r20, com.android.systemui.plugins.FalsingManager r21, android.os.PowerManager r22, android.view.accessibility.AccessibilityManager r23, com.android.systemui.statusbar.LockscreenShadeTransitionController r24, com.android.systemui.keyguard.ScreenLifecycle r25, com.android.systemui.statusbar.VibratorHelper r26, com.android.systemui.biometrics.UdfpsHapticsSimulator r27, java.util.Optional<com.android.systemui.biometrics.UdfpsHbmProvider> r28, com.android.systemui.statusbar.policy.KeyguardStateController r29, com.android.systemui.statusbar.phone.KeyguardBypassController r30, android.hardware.display.DisplayManager r31, android.os.Handler r32, com.android.systemui.statusbar.policy.ConfigurationController r33, com.android.systemui.util.time.SystemClock r34, com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController r35, com.android.systemui.statusbar.phone.SystemUIDialogManager r36, com.android.internal.util.LatencyTracker r37, com.android.systemui.animation.ActivityLaunchAnimator r38) {
        /*
            Method dump skipped, instructions count: 251
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.UdfpsController.<init>(android.content.Context, com.android.systemui.util.concurrency.Execution, android.view.LayoutInflater, android.hardware.fingerprint.FingerprintManager, android.view.WindowManager, com.android.systemui.plugins.statusbar.StatusBarStateController, com.android.systemui.util.concurrency.DelayableExecutor, com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager, com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager, com.android.systemui.dump.DumpManager, com.android.keyguard.KeyguardUpdateMonitor, com.android.systemui.plugins.FalsingManager, android.os.PowerManager, android.view.accessibility.AccessibilityManager, com.android.systemui.statusbar.LockscreenShadeTransitionController, com.android.systemui.keyguard.ScreenLifecycle, com.android.systemui.statusbar.VibratorHelper, com.android.systemui.biometrics.UdfpsHapticsSimulator, java.util.Optional, com.android.systemui.statusbar.policy.KeyguardStateController, com.android.systemui.statusbar.phone.KeyguardBypassController, android.hardware.display.DisplayManager, android.os.Handler, com.android.systemui.statusbar.policy.ConfigurationController, com.android.systemui.util.time.SystemClock, com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController, com.android.systemui.statusbar.phone.SystemUIDialogManager, com.android.internal.util.LatencyTracker, com.android.systemui.animation.ActivityLaunchAnimator):void");
    }

    public final boolean isWithinSensorArea(UdfpsView udfpsView, float f, float f2, boolean z) {
        UdfpsAnimationViewController<?> udfpsAnimationViewController;
        boolean z2;
        UdfpsAnimationViewController<?> udfpsAnimationViewController2 = null;
        PointF pointF = null;
        if (z) {
            Objects.requireNonNull(udfpsView);
            UdfpsAnimationViewController<?> udfpsAnimationViewController3 = udfpsView.animationViewController;
            if (udfpsAnimationViewController3 != null) {
                pointF = udfpsAnimationViewController3.getTouchTranslation();
            }
            if (pointF == null) {
                pointF = new PointF(0.0f, 0.0f);
            }
            float centerX = udfpsView.sensorRect.centerX() + pointF.x;
            float centerY = udfpsView.sensorRect.centerY() + pointF.y;
            RectF rectF = udfpsView.sensorRect;
            float f3 = (rectF.bottom - rectF.top) / 2.0f;
            float f4 = udfpsView.sensorTouchAreaCoefficient;
            float f5 = ((rectF.right - rectF.left) / 2.0f) * f4;
            if (f > centerX - f5 && f < f5 + centerX) {
                float f6 = f3 * f4;
                if (f2 > centerY - f6 && f2 < f6 + centerY) {
                    UdfpsAnimationViewController<?> udfpsAnimationViewController4 = udfpsView.animationViewController;
                    if (udfpsAnimationViewController4 == null) {
                        z2 = false;
                    } else {
                        z2 = udfpsAnimationViewController4.shouldPauseAuth();
                    }
                    if (!z2) {
                        return true;
                    }
                }
            }
            return false;
        }
        UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
        if (udfpsControllerOverlay != null) {
            UdfpsView udfpsView2 = udfpsControllerOverlay.overlayView;
            if (udfpsView2 == null) {
                udfpsAnimationViewController = null;
            } else {
                udfpsAnimationViewController = udfpsView2.animationViewController;
            }
            if (udfpsAnimationViewController != null) {
                Objects.requireNonNull(udfpsControllerOverlay);
                UdfpsView udfpsView3 = udfpsControllerOverlay.overlayView;
                if (udfpsView3 != null) {
                    udfpsAnimationViewController2 = udfpsView3.animationViewController;
                }
                if (udfpsAnimationViewController2.shouldPauseAuth() || !getSensorLocation().contains(f, f2)) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    @Override // com.android.systemui.doze.DozeReceiver
    public final void dozeTimeTick() {
        UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
        if (udfpsControllerOverlay != null) {
            Objects.requireNonNull(udfpsControllerOverlay);
            UdfpsView udfpsView = udfpsControllerOverlay.overlayView;
            if (udfpsView != null) {
                udfpsView.dozeTimeTick();
            }
        }
    }

    public final RectF getSensorLocation() {
        SensorLocationInternal location = this.mSensorProps.getLocation();
        int i = location.sensorLocationX;
        int i2 = location.sensorRadius;
        int i3 = location.sensorLocationY;
        return new RectF(i - i2, i3 - i2, i + i2, i3 + i2);
    }

    public final void hideUdfpsOverlay() {
        boolean z;
        this.mExecution.assertIsMainThread();
        UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
        if (udfpsControllerOverlay != null) {
            UdfpsView udfpsView = udfpsControllerOverlay.overlayView;
            if (udfpsView != null) {
                onFingerUp(udfpsView);
            }
            UdfpsControllerOverlay udfpsControllerOverlay2 = this.mOverlay;
            Objects.requireNonNull(udfpsControllerOverlay2);
            UdfpsView udfpsView2 = udfpsControllerOverlay2.overlayView;
            if (udfpsView2 != null) {
                z = true;
            } else {
                z = false;
            }
            if (udfpsView2 != null) {
                if (udfpsView2.isIlluminationRequested) {
                    udfpsView2.stopIllumination();
                }
                udfpsControllerOverlay2.windowManager.removeView(udfpsView2);
                udfpsView2.setOnTouchListener(null);
                udfpsView2.setOnHoverListener(null);
                udfpsView2.animationViewController = null;
                UdfpsControllerOverlay$show$1$1 udfpsControllerOverlay$show$1$1 = udfpsControllerOverlay2.overlayTouchListener;
                if (udfpsControllerOverlay$show$1$1 != null) {
                    udfpsControllerOverlay2.accessibilityManager.removeTouchExplorationStateChangeListener(udfpsControllerOverlay$show$1$1);
                }
            }
            udfpsControllerOverlay2.overlayView = null;
            udfpsControllerOverlay2.overlayTouchListener = null;
            if (this.mKeyguardViewManager.isShowingAlternateAuth()) {
                this.mKeyguardViewManager.resetAlternateAuth(true);
            }
            Log.v("UdfpsController", "hideUdfpsOverlay | removing window: " + z);
        } else {
            Log.v("UdfpsController", "hideUdfpsOverlay | the overlay is already hidden");
        }
        this.mOverlay = null;
        this.mOrientationListener.disable();
    }

    public final void onCancelUdfps() {
        UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
        if (udfpsControllerOverlay != null) {
            Objects.requireNonNull(udfpsControllerOverlay);
            if (udfpsControllerOverlay.overlayView != null) {
                UdfpsControllerOverlay udfpsControllerOverlay2 = this.mOverlay;
                Objects.requireNonNull(udfpsControllerOverlay2);
                onFingerUp(udfpsControllerOverlay2.overlayView);
            }
        }
        if (this.mIsAodInterruptActive) {
            Runnable runnable = this.mCancelAodTimeoutAction;
            if (runnable != null) {
                runnable.run();
                this.mCancelAodTimeoutAction = null;
            }
            this.mIsAodInterruptActive = false;
        }
    }

    public final void onFingerDown(int i, int i2, float f, float f2) {
        UdfpsAnimationViewController<?> udfpsAnimationViewController;
        boolean z;
        this.mExecution.assertIsMainThread();
        UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
        if (udfpsControllerOverlay == null) {
            Log.w("UdfpsController", "Null request in onFingerDown");
            return;
        }
        UdfpsView udfpsView = udfpsControllerOverlay.overlayView;
        if (udfpsView == null) {
            udfpsAnimationViewController = null;
        } else {
            udfpsAnimationViewController = udfpsView.animationViewController;
        }
        if ((udfpsAnimationViewController instanceof UdfpsKeyguardViewController) && !this.mStatusBarStateController.isDozing()) {
            KeyguardBypassController keyguardBypassController = this.mKeyguardBypassController;
            Objects.requireNonNull(keyguardBypassController);
            keyguardBypassController.userHasDeviceEntryIntent = true;
        }
        if (!this.mOnFingerDown) {
            playStartHaptic();
            KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateMonitor;
            Objects.requireNonNull(keyguardUpdateMonitor);
            if (keyguardUpdateMonitor.mFaceRunningState == 1) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                this.mKeyguardUpdateMonitor.requestFaceAuth(false);
            }
        }
        this.mOnFingerDown = true;
        this.mFingerprintManager.onPointerDown(this.mSensorProps.sensorId, i, i2, f, f2);
        Trace.endAsyncSection("UdfpsController.e2e.onPointerDown", 0);
        UdfpsControllerOverlay udfpsControllerOverlay2 = this.mOverlay;
        Objects.requireNonNull(udfpsControllerOverlay2);
        final UdfpsView udfpsView2 = udfpsControllerOverlay2.overlayView;
        if (udfpsView2 != null) {
            Trace.beginAsyncSection("UdfpsController.e2e.startIllumination", 0);
            StandardWifiEntry$$ExternalSyntheticLambda0 standardWifiEntry$$ExternalSyntheticLambda0 = new StandardWifiEntry$$ExternalSyntheticLambda0(this, 2);
            udfpsView2.isIlluminationRequested = true;
            UdfpsAnimationViewController<?> udfpsAnimationViewController2 = udfpsView2.animationViewController;
            if (udfpsAnimationViewController2 != null) {
                udfpsAnimationViewController2.getView().onIlluminationStarting();
                udfpsAnimationViewController2.getView().postInvalidate();
            }
            UdfpsSurfaceView udfpsSurfaceView = udfpsView2.ghbmView;
            if (udfpsSurfaceView != null) {
                udfpsSurfaceView.mGhbmIlluminationListener = new UdfpsSurfaceView.GhbmIlluminationListener() { // from class: com.android.systemui.biometrics.UdfpsView$startIllumination$1
                    @Override // com.android.systemui.biometrics.UdfpsSurfaceView.GhbmIlluminationListener
                    public final void enableGhbm(Surface surface, Runnable runnable) {
                        UdfpsView udfpsView3 = UdfpsView.this;
                        int i3 = UdfpsView.$r8$clinit;
                        udfpsView3.doIlluminate(surface, runnable);
                    }
                };
                udfpsSurfaceView.setVisibility(0);
                UdfpsSurfaceView.GhbmIlluminationListener ghbmIlluminationListener = udfpsSurfaceView.mGhbmIlluminationListener;
                if (ghbmIlluminationListener == null) {
                    Log.e("UdfpsSurfaceView", "startIllumination | mGhbmIlluminationListener is null");
                } else if (!udfpsSurfaceView.mHasValidSurface) {
                    udfpsSurfaceView.mAwaitingSurfaceToStartIllumination = true;
                    udfpsSurfaceView.mOnIlluminatedRunnable = standardWifiEntry$$ExternalSyntheticLambda0;
                } else if (ghbmIlluminationListener == null) {
                    Log.e("UdfpsSurfaceView", "doIlluminate | mGhbmIlluminationListener is null");
                } else {
                    ghbmIlluminationListener.enableGhbm(udfpsSurfaceView.mHolder.getSurface(), standardWifiEntry$$ExternalSyntheticLambda0);
                }
            } else {
                udfpsView2.doIlluminate(null, standardWifiEntry$$ExternalSyntheticLambda0);
            }
        }
        Iterator it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            ((Callback) it.next()).onFingerDown();
        }
    }

    public final void onFingerUp(UdfpsView udfpsView) {
        this.mExecution.assertIsMainThread();
        this.mActivePointerId = -1;
        this.mGoodCaptureReceived = false;
        if (this.mOnFingerDown) {
            this.mFingerprintManager.onPointerUp(this.mSensorProps.sensorId);
            Iterator it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((Callback) it.next()).onFingerUp();
            }
        }
        this.mOnFingerDown = false;
        Objects.requireNonNull(udfpsView);
        if (udfpsView.isIlluminationRequested) {
            udfpsView.stopIllumination();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x0084  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onTouch(android.view.View r18, android.view.MotionEvent r19, boolean r20) {
        /*
            Method dump skipped, instructions count: 584
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.UdfpsController.onTouch(android.view.View, android.view.MotionEvent, boolean):boolean");
    }

    @VisibleForTesting
    public void playStartHaptic() {
        this.mVibrator.vibrate(Process.myUid(), this.mContext.getOpPackageName(), EFFECT_CLICK, "udfps-onStart-click", VIBRATION_ATTRIBUTES);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0043 A[Catch: RuntimeException -> 0x0075, TryCatch #0 {RuntimeException -> 0x0075, blocks: (B:5:0x0012, B:7:0x001e, B:9:0x002e, B:10:0x0033, B:19:0x0043, B:20:0x0046, B:23:0x0067, B:24:0x006a, B:25:0x006d, B:26:0x0074), top: B:35:0x0012 }] */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0066  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0067 A[Catch: RuntimeException -> 0x0075, TryCatch #0 {RuntimeException -> 0x0075, blocks: (B:5:0x0012, B:7:0x001e, B:9:0x002e, B:10:0x0033, B:19:0x0043, B:20:0x0046, B:23:0x0067, B:24:0x006a, B:25:0x006d, B:26:0x0074), top: B:35:0x0012 }] */
    /* JADX WARN: Type inference failed for: r5v6, types: [android.view.accessibility.AccessibilityManager$TouchExplorationStateChangeListener, com.android.systemui.biometrics.UdfpsControllerOverlay$show$1$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void showUdfpsOverlay(final com.android.systemui.biometrics.UdfpsControllerOverlay r10) {
        /*
            r9 = this;
            com.android.systemui.util.concurrency.Execution r0 = r9.mExecution
            r0.assertIsMainThread()
            r9.mOverlay = r10
            com.android.systemui.biometrics.UdfpsView r0 = r10.overlayView
            java.lang.String r1 = "UdfpsControllerOverlay"
            java.lang.String r2 = "showUdfpsOverlay | the overlay is already showing"
            r3 = 0
            if (r0 != 0) goto L_0x007d
            r0 = 1
            android.view.LayoutInflater r4 = r10.inflater     // Catch: RuntimeException -> 0x0075
            r5 = 2131624641(0x7f0e02c1, float:1.8876467E38)
            r6 = 0
            android.view.View r4 = r4.inflate(r5, r6, r3)     // Catch: RuntimeException -> 0x0075
            if (r4 == 0) goto L_0x006d
            com.android.systemui.biometrics.UdfpsView r4 = (com.android.systemui.biometrics.UdfpsView) r4     // Catch: RuntimeException -> 0x0075
            android.hardware.fingerprint.FingerprintSensorPropertiesInternal r5 = r10.sensorProps     // Catch: RuntimeException -> 0x0075
            r4.sensorProperties = r5     // Catch: RuntimeException -> 0x0075
            com.android.systemui.biometrics.UdfpsHbmProvider r5 = r10.hbmProvider     // Catch: RuntimeException -> 0x0075
            r4.hbmProvider = r5     // Catch: RuntimeException -> 0x0075
            com.android.systemui.biometrics.UdfpsAnimationViewController r5 = r10.inflateUdfpsAnimation(r4, r9)     // Catch: RuntimeException -> 0x0075
            if (r5 == 0) goto L_0x0033
            r5.init()     // Catch: RuntimeException -> 0x0075
            r4.animationViewController = r5     // Catch: RuntimeException -> 0x0075
        L_0x0033:
            int r6 = r10.requestReason     // Catch: RuntimeException -> 0x0075
            r7 = 2
            if (r6 == r0) goto L_0x0040
            if (r6 == r7) goto L_0x0040
            r8 = 3
            if (r6 != r8) goto L_0x003e
            goto L_0x0040
        L_0x003e:
            r6 = r3
            goto L_0x0041
        L_0x0040:
            r6 = r0
        L_0x0041:
            if (r6 == 0) goto L_0x0046
            r4.setImportantForAccessibility(r7)     // Catch: RuntimeException -> 0x0075
        L_0x0046:
            android.view.WindowManager r6 = r10.windowManager     // Catch: RuntimeException -> 0x0075
            android.view.WindowManager$LayoutParams r7 = r10.coreLayoutParams     // Catch: RuntimeException -> 0x0075
            android.hardware.fingerprint.FingerprintSensorPropertiesInternal r8 = r10.sensorProps     // Catch: RuntimeException -> 0x0075
            android.hardware.biometrics.SensorLocationInternal r8 = r8.getLocation()     // Catch: RuntimeException -> 0x0075
            r10.updateForLocation(r7, r8, r5)     // Catch: RuntimeException -> 0x0075
            r6.addView(r4, r7)     // Catch: RuntimeException -> 0x0075
            com.android.systemui.biometrics.UdfpsControllerOverlay$show$1$1 r5 = new com.android.systemui.biometrics.UdfpsControllerOverlay$show$1$1     // Catch: RuntimeException -> 0x0075
            r5.<init>()     // Catch: RuntimeException -> 0x0075
            r10.overlayTouchListener = r5     // Catch: RuntimeException -> 0x0075
            android.view.accessibility.AccessibilityManager r6 = r10.accessibilityManager     // Catch: RuntimeException -> 0x0075
            r6.addTouchExplorationStateChangeListener(r5)     // Catch: RuntimeException -> 0x0075
            com.android.systemui.biometrics.UdfpsControllerOverlay$show$1$1 r5 = r10.overlayTouchListener     // Catch: RuntimeException -> 0x0075
            if (r5 != 0) goto L_0x0067
            goto L_0x006a
        L_0x0067:
            r5.onTouchExplorationStateChanged(r0)     // Catch: RuntimeException -> 0x0075
        L_0x006a:
            r10.overlayView = r4     // Catch: RuntimeException -> 0x0075
            goto L_0x0081
        L_0x006d:
            java.lang.NullPointerException r4 = new java.lang.NullPointerException     // Catch: RuntimeException -> 0x0075
            java.lang.String r5 = "null cannot be cast to non-null type com.android.systemui.biometrics.UdfpsView"
            r4.<init>(r5)     // Catch: RuntimeException -> 0x0075
            throw r4     // Catch: RuntimeException -> 0x0075
        L_0x0075:
            r4 = move-exception
            java.lang.String r5 = "showUdfpsOverlay | failed to add window"
            android.util.Log.e(r1, r5, r4)
            goto L_0x0081
        L_0x007d:
            android.util.Log.v(r1, r2)
            r0 = r3
        L_0x0081:
            java.lang.String r1 = "UdfpsController"
            if (r0 == 0) goto L_0x00a2
            java.lang.String r0 = "showUdfpsOverlay | adding window reason="
            java.lang.StringBuilder r0 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1.m(r0)
            int r10 = r10.requestReason
            r0.append(r10)
            java.lang.String r10 = r0.toString()
            android.util.Log.v(r1, r10)
            r9.mOnFingerDown = r3
            r9.mAttemptedToDismissKeyguard = r3
            com.android.systemui.biometrics.BiometricDisplayListener r9 = r9.mOrientationListener
            r9.enable()
            goto L_0x00a5
        L_0x00a2:
            android.util.Log.v(r1, r2)
        L_0x00a5:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.UdfpsController.showUdfpsOverlay(com.android.systemui.biometrics.UdfpsControllerOverlay):void");
    }
}
