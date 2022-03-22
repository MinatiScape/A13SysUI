package com.android.keyguard;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.AnimatedStateListDrawable;
import android.hardware.biometrics.BiometricSourceType;
import android.hardware.biometrics.SensorLocationInternal;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.Process;
import android.os.VibrationAttributes;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.MathUtils;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.preference.R$id;
import com.android.systemui.Dumpable;
import com.android.systemui.R$anim;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.AuthRippleController;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.wifitrackerlib.BaseWifiTracker$$ExternalSyntheticLambda0;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class LockIconViewController extends ViewController<LockIconView> implements Dumpable {
    public final AuthController mAuthController;
    public final AuthRippleController mAuthRippleController;
    public int mBottomPaddingPx;
    public boolean mCanDismissLockScreen;
    public Runnable mCancelDelayedUpdateVisibilityRunnable;
    public final ConfigurationController mConfigurationController;
    public boolean mDownDetected;
    public final DelayableExecutor mExecutor;
    public final FalsingManager mFalsingManager;
    public float mHeightPixels;
    public final AnimatedStateListDrawable mIcon;
    public float mInterpolatedDarkAmount;
    public boolean mIsBouncerShowing;
    public boolean mIsDozing;
    public boolean mIsKeyguardShowing;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final KeyguardViewController mKeyguardViewController;
    public String mLockedLabel;
    public Runnable mLongPressCancelRunnable;
    public final int mMaxBurnInOffsetX;
    public final int mMaxBurnInOffsetY;
    public Runnable mOnGestureDetectedRunnable;
    public boolean mQsExpanded;
    public boolean mRunningFPS;
    public boolean mShowAodLockIcon;
    public boolean mShowAodUnlockedIcon;
    public boolean mShowLockIcon;
    public boolean mShowUnlockIcon;
    public int mStatusBarState;
    public final StatusBarStateController mStatusBarStateController;
    public boolean mUdfpsEnrolled;
    public boolean mUdfpsSupported;
    public String mUnlockedLabel;
    public boolean mUserUnlockedWithBiometric;
    public VelocityTracker mVelocityTracker;
    public final VibratorHelper mVibrator;
    public float mWidthPixels;
    public static final int sLockIconRadiusPx = (int) ((DisplayMetrics.DENSITY_DEVICE_STABLE / 160.0f) * 36.0f);
    public static final VibrationAttributes TOUCH_VIBRATION_ATTRIBUTES = VibrationAttributes.createForUsage(18);
    public int mActivePointerId = -1;
    public final Rect mSensorTouchLocation = new Rect();
    public final AnonymousClass1 mAccessibilityDelegate = new View.AccessibilityDelegate() { // from class: com.android.keyguard.LockIconViewController.1
        public final AccessibilityNodeInfo.AccessibilityAction mAccessibilityAuthenticateHint;
        public final AccessibilityNodeInfo.AccessibilityAction mAccessibilityEnterHint;

        {
            LockIconViewController.this = this;
            this.mAccessibilityAuthenticateHint = new AccessibilityNodeInfo.AccessibilityAction(16, this.getResources().getString(2131951674));
            this.mAccessibilityEnterHint = new AccessibilityNodeInfo.AccessibilityAction(16, this.getResources().getString(2131951717));
        }

        @Override // android.view.View.AccessibilityDelegate
        public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            boolean z;
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            LockIconViewController lockIconViewController = LockIconViewController.this;
            Objects.requireNonNull(lockIconViewController);
            if (lockIconViewController.mUdfpsSupported || lockIconViewController.mShowUnlockIcon) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                LockIconViewController lockIconViewController2 = LockIconViewController.this;
                if (lockIconViewController2.mShowLockIcon) {
                    accessibilityNodeInfo.addAction(this.mAccessibilityAuthenticateHint);
                } else if (lockIconViewController2.mShowUnlockIcon) {
                    accessibilityNodeInfo.addAction(this.mAccessibilityEnterHint);
                }
            }
        }
    };
    public AnonymousClass2 mStatusBarStateListener = new StatusBarStateController.StateListener() { // from class: com.android.keyguard.LockIconViewController.2
        {
            LockIconViewController.this = this;
        }

        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public final void onDozeAmountChanged(float f, float f2) {
            LockIconViewController lockIconViewController = LockIconViewController.this;
            lockIconViewController.mInterpolatedDarkAmount = f2;
            LockIconView lockIconView = (LockIconView) lockIconViewController.mView;
            Objects.requireNonNull(lockIconView);
            lockIconView.mDozeAmount = f2;
            lockIconView.updateColorAndBackgroundVisibility();
            LockIconViewController.this.updateBurnInOffsets();
        }

        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public final void onDozingChanged(boolean z) {
            LockIconViewController lockIconViewController = LockIconViewController.this;
            lockIconViewController.mIsDozing = z;
            lockIconViewController.updateBurnInOffsets();
            LockIconViewController.this.updateVisibility();
        }

        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public final void onStateChanged(int i) {
            LockIconViewController lockIconViewController = LockIconViewController.this;
            lockIconViewController.mStatusBarState = i;
            lockIconViewController.updateVisibility();
        }
    };
    public final AnonymousClass3 mKeyguardUpdateMonitorCallback = new AnonymousClass3();
    public final AnonymousClass4 mKeyguardStateCallback = new KeyguardStateController.Callback() { // from class: com.android.keyguard.LockIconViewController.4
        {
            LockIconViewController.this = this;
        }

        @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
        public final void onKeyguardFadingAwayChanged() {
            LockIconViewController.this.updateKeyguardShowing();
            LockIconViewController.this.updateVisibility();
        }

        @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
        public final void onKeyguardShowingChanged() {
            LockIconViewController lockIconViewController = LockIconViewController.this;
            lockIconViewController.mCanDismissLockScreen = lockIconViewController.mKeyguardStateController.canDismissLockScreen();
            LockIconViewController.this.updateKeyguardShowing();
            LockIconViewController lockIconViewController2 = LockIconViewController.this;
            if (lockIconViewController2.mIsKeyguardShowing) {
                lockIconViewController2.mUserUnlockedWithBiometric = lockIconViewController2.mKeyguardUpdateMonitor.getUserUnlockedWithBiometric(KeyguardUpdateMonitor.getCurrentUser());
            }
            LockIconViewController.this.updateVisibility();
        }

        @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
        public final void onUnlockedChanged() {
            LockIconViewController lockIconViewController = LockIconViewController.this;
            lockIconViewController.mCanDismissLockScreen = lockIconViewController.mKeyguardStateController.canDismissLockScreen();
            LockIconViewController.this.updateKeyguardShowing();
            LockIconViewController.this.updateVisibility();
        }
    };
    public final AnonymousClass5 mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.keyguard.LockIconViewController.5
        {
            LockIconViewController.this = this;
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onConfigChanged(Configuration configuration) {
            LockIconViewController.this.updateConfiguration();
            LockIconViewController lockIconViewController = LockIconViewController.this;
            Objects.requireNonNull(lockIconViewController);
            ((LockIconView) lockIconViewController.mView).updateColorAndBackgroundVisibility();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onThemeChanged() {
            LockIconViewController lockIconViewController = LockIconViewController.this;
            Objects.requireNonNull(lockIconViewController);
            ((LockIconView) lockIconViewController.mView).updateColorAndBackgroundVisibility();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onUiModeChanged() {
            LockIconViewController lockIconViewController = LockIconViewController.this;
            Objects.requireNonNull(lockIconViewController);
            ((LockIconView) lockIconViewController.mView).updateColorAndBackgroundVisibility();
        }
    };
    public final AnonymousClass6 mAuthControllerCallback = new AuthController.Callback() { // from class: com.android.keyguard.LockIconViewController.6
        {
            LockIconViewController.this = this;
        }

        @Override // com.android.systemui.biometrics.AuthController.Callback
        public final void onAllAuthenticatorsRegistered() {
            LockIconViewController lockIconViewController = LockIconViewController.this;
            Objects.requireNonNull(lockIconViewController);
            lockIconViewController.mExecutor.execute(new LockIconViewController$$ExternalSyntheticLambda0(lockIconViewController, 0));
        }

        @Override // com.android.systemui.biometrics.AuthController.Callback
        public final void onEnrollmentsChanged() {
            LockIconViewController lockIconViewController = LockIconViewController.this;
            Objects.requireNonNull(lockIconViewController);
            lockIconViewController.mExecutor.execute(new LockIconViewController$$ExternalSyntheticLambda0(lockIconViewController, 0));
        }
    };

    /* renamed from: com.android.keyguard.LockIconViewController$3 */
    /* loaded from: classes.dex */
    public class AnonymousClass3 extends KeyguardUpdateMonitorCallback {
        public AnonymousClass3() {
            LockIconViewController.this = r1;
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onBiometricRunningStateChanged(boolean z, BiometricSourceType biometricSourceType) {
            LockIconViewController lockIconViewController = LockIconViewController.this;
            boolean z2 = lockIconViewController.mRunningFPS;
            boolean z3 = lockIconViewController.mUserUnlockedWithBiometric;
            lockIconViewController.mUserUnlockedWithBiometric = lockIconViewController.mKeyguardUpdateMonitor.getUserUnlockedWithBiometric(KeyguardUpdateMonitor.getCurrentUser());
            if (biometricSourceType == BiometricSourceType.FINGERPRINT) {
                LockIconViewController lockIconViewController2 = LockIconViewController.this;
                lockIconViewController2.mRunningFPS = z;
                if (z2 && !z) {
                    Runnable runnable = lockIconViewController2.mCancelDelayedUpdateVisibilityRunnable;
                    if (runnable != null) {
                        runnable.run();
                    }
                    LockIconViewController lockIconViewController3 = LockIconViewController.this;
                    lockIconViewController3.mCancelDelayedUpdateVisibilityRunnable = lockIconViewController3.mExecutor.executeDelayed(new KeyguardStatusView$$ExternalSyntheticLambda0(this, 1), 50L);
                    return;
                }
            }
            LockIconViewController lockIconViewController4 = LockIconViewController.this;
            if (z3 != lockIconViewController4.mUserUnlockedWithBiometric || z2 != lockIconViewController4.mRunningFPS) {
                lockIconViewController4.updateVisibility();
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onKeyguardBouncerChanged(boolean z) {
            LockIconViewController lockIconViewController = LockIconViewController.this;
            lockIconViewController.mIsBouncerShowing = z;
            lockIconViewController.updateVisibility();
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onKeyguardVisibilityChanged(boolean z) {
            LockIconViewController lockIconViewController = LockIconViewController.this;
            lockIconViewController.mIsBouncerShowing = lockIconViewController.mKeyguardViewController.isBouncerShowing();
            LockIconViewController.this.updateVisibility();
        }
    }

    public final void cancelTouches() {
        this.mDownDetected = false;
        Runnable runnable = this.mLongPressCancelRunnable;
        if (runnable != null) {
            runnable.run();
        }
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
        VibratorHelper vibratorHelper = this.mVibrator;
        Objects.requireNonNull(vibratorHelper);
        if (vibratorHelper.hasVibrator()) {
            Executor executor = vibratorHelper.mExecutor;
            Vibrator vibrator = vibratorHelper.mVibrator;
            Objects.requireNonNull(vibrator);
            executor.execute(new BaseWifiTracker$$ExternalSyntheticLambda0(vibrator, 4));
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        int[] state;
        StringBuilder m = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(VendorAtomValue$$ExternalSyntheticOutline1.m("mUdfpsSupported: "), this.mUdfpsSupported, printWriter, "mUdfpsEnrolled: "), this.mUdfpsEnrolled, printWriter, "mIsKeyguardShowing: ");
        m.append(this.mIsKeyguardShowing);
        printWriter.println(m.toString());
        printWriter.println(" mIcon: ");
        for (int i : this.mIcon.getState()) {
            printWriter.print(" " + i);
        }
        printWriter.println();
        StringBuilder sb = new StringBuilder();
        sb.append(" mShowUnlockIcon: ");
        StringBuilder m2 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(sb, this.mShowUnlockIcon, printWriter, " mShowLockIcon: "), this.mShowLockIcon, printWriter, " mShowAodUnlockedIcon: "), this.mShowAodUnlockedIcon, printWriter, "  mIsDozing: "), this.mIsDozing, printWriter, "  mIsBouncerShowing: "), this.mIsBouncerShowing, printWriter, "  mUserUnlockedWithBiometric: "), this.mUserUnlockedWithBiometric, printWriter, "  mRunningFPS: "), this.mRunningFPS, printWriter, "  mCanDismissLockScreen: "), this.mCanDismissLockScreen, printWriter, "  mStatusBarState: ");
        m2.append(R$id.toShortString(this.mStatusBarState));
        printWriter.println(m2.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("  mQsExpanded: ");
        StringBuilder m3 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(sb2, this.mQsExpanded, printWriter, "  mInterpolatedDarkAmount: ");
        m3.append(this.mInterpolatedDarkAmount);
        printWriter.println(m3.toString());
        T t = this.mView;
        if (t != 0) {
            ((LockIconView) t).dump(fileDescriptor, printWriter, strArr);
        }
    }

    @Override // com.android.systemui.util.ViewController
    public final void onInit() {
        ((LockIconView) this.mView).setAccessibilityDelegate(this.mAccessibilityDelegate);
    }

    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean z;
        boolean z2;
        if (!this.mSensorTouchLocation.contains((int) motionEvent.getX(), (int) motionEvent.getY()) || ((LockIconView) this.mView).getVisibility() != 0) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            if (this.mUdfpsSupported || this.mShowUnlockIcon) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2) {
                if (motionEvent.getActionMasked() == 0) {
                    return true;
                }
                return this.mDownDetected;
            }
        }
        return false;
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        AuthController authController = this.mAuthController;
        AnonymousClass6 r1 = this.mAuthControllerCallback;
        Objects.requireNonNull(authController);
        authController.mCallbacks.remove(r1);
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardUpdateMonitorCallback);
        this.mStatusBarStateController.removeCallback(this.mStatusBarStateListener);
        this.mKeyguardStateController.removeCallback(this.mKeyguardStateCallback);
        Runnable runnable = this.mCancelDelayedUpdateVisibilityRunnable;
        if (runnable != null) {
            runnable.run();
            this.mCancelDelayedUpdateVisibilityRunnable = null;
        }
    }

    public final void updateBurnInOffsets() {
        float lerp = MathUtils.lerp(0.0f, R$anim.getBurnInOffset(this.mMaxBurnInOffsetX * 2, true) - this.mMaxBurnInOffsetX, this.mInterpolatedDarkAmount);
        float lerp2 = MathUtils.lerp(0.0f, R$anim.getBurnInOffset(this.mMaxBurnInOffsetY * 2, false) - this.mMaxBurnInOffsetY, this.mInterpolatedDarkAmount);
        MathUtils.lerp(0.0f, R$anim.zigzag(((float) System.currentTimeMillis()) / 60000.0f, 1.0f, 89.0f), this.mInterpolatedDarkAmount);
        ((LockIconView) this.mView).setTranslationX(lerp);
        ((LockIconView) this.mView).setTranslationY(lerp2);
    }

    public final void updateIsUdfpsEnrolled() {
        boolean z = this.mUdfpsSupported;
        boolean z2 = this.mUdfpsEnrolled;
        boolean isUdfpsSupported = this.mKeyguardUpdateMonitor.isUdfpsSupported();
        this.mUdfpsSupported = isUdfpsSupported;
        LockIconView lockIconView = (LockIconView) this.mView;
        Objects.requireNonNull(lockIconView);
        lockIconView.mUseBackground = isUdfpsSupported;
        lockIconView.updateColorAndBackgroundVisibility();
        boolean isUdfpsEnrolled = this.mKeyguardUpdateMonitor.isUdfpsEnrolled();
        this.mUdfpsEnrolled = isUdfpsEnrolled;
        if (z != this.mUdfpsSupported || z2 != isUdfpsEnrolled) {
            updateVisibility();
        }
    }

    public final void updateKeyguardShowing() {
        boolean z;
        if (!this.mKeyguardStateController.isShowing() || this.mKeyguardStateController.isKeyguardGoingAway()) {
            z = false;
        } else {
            z = true;
        }
        this.mIsKeyguardShowing = z;
    }

    /* JADX WARN: Removed duplicated region for block: B:57:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0080 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x008f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:77:0x00a8  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x00c1  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0146  */
    /* JADX WARN: Removed duplicated region for block: B:96:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateVisibility() {
        /*
            Method dump skipped, instructions count: 348
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.LockIconViewController.updateVisibility():void");
    }

    public static void $r8$lambda$h98ceOtiS5JD1Nfnu1Y0fyk_1uo(LockIconViewController lockIconViewController) {
        AuthRippleController authRippleController;
        Objects.requireNonNull(lockIconViewController);
        lockIconViewController.cancelTouches();
        if (lockIconViewController.mFalsingManager.isFalseTouch(14)) {
            Log.v("LockIconViewController", "lock icon long-press rejected by the falsing manager.");
            return;
        }
        lockIconViewController.mIsBouncerShowing = true;
        if (lockIconViewController.mUdfpsSupported && lockIconViewController.mShowUnlockIcon && (authRippleController = lockIconViewController.mAuthRippleController) != null) {
            authRippleController.showRipple(BiometricSourceType.FINGERPRINT);
        }
        lockIconViewController.updateVisibility();
        Runnable runnable = lockIconViewController.mOnGestureDetectedRunnable;
        if (runnable != null) {
            runnable.run();
        }
        lockIconViewController.mVibrator.vibrate(Process.myUid(), lockIconViewController.getContext().getOpPackageName(), UdfpsController.EFFECT_CLICK, "lock-icon-device-entry", TOUCH_VIBRATION_ATTRIBUTES);
        lockIconViewController.mKeyguardViewController.showBouncer(true);
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.keyguard.LockIconViewController$1] */
    /* JADX WARN: Type inference failed for: r0v3, types: [com.android.keyguard.LockIconViewController$2] */
    /* JADX WARN: Type inference failed for: r0v5, types: [com.android.keyguard.LockIconViewController$4] */
    /* JADX WARN: Type inference failed for: r0v6, types: [com.android.keyguard.LockIconViewController$5] */
    /* JADX WARN: Type inference failed for: r0v7, types: [com.android.keyguard.LockIconViewController$6] */
    public LockIconViewController(LockIconView lockIconView, StatusBarStateController statusBarStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardViewController keyguardViewController, KeyguardStateController keyguardStateController, FalsingManager falsingManager, AuthController authController, DumpManager dumpManager, ConfigurationController configurationController, DelayableExecutor delayableExecutor, VibratorHelper vibratorHelper, AuthRippleController authRippleController, Resources resources) {
        super(lockIconView);
        this.mStatusBarStateController = statusBarStateController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mAuthController = authController;
        this.mKeyguardViewController = keyguardViewController;
        this.mKeyguardStateController = keyguardStateController;
        this.mFalsingManager = falsingManager;
        this.mConfigurationController = configurationController;
        this.mExecutor = delayableExecutor;
        this.mVibrator = vibratorHelper;
        this.mAuthRippleController = authRippleController;
        this.mMaxBurnInOffsetX = resources.getDimensionPixelSize(2131167259);
        this.mMaxBurnInOffsetY = resources.getDimensionPixelSize(2131167260);
        AnimatedStateListDrawable animatedStateListDrawable = (AnimatedStateListDrawable) resources.getDrawable(2131232731, lockIconView.getContext().getTheme());
        this.mIcon = animatedStateListDrawable;
        lockIconView.mLockIcon.setImageDrawable(animatedStateListDrawable);
        if (lockIconView.mUseBackground) {
            if (animatedStateListDrawable == null) {
                lockIconView.mBgView.setVisibility(4);
            } else {
                lockIconView.mBgView.setVisibility(0);
            }
        }
        this.mUnlockedLabel = resources.getString(2131951816);
        this.mLockedLabel = resources.getString(2131951753);
        dumpManager.registerDumpable("LockIconViewController", this);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        updateIsUdfpsEnrolled();
        updateConfiguration();
        updateKeyguardShowing();
        this.mUserUnlockedWithBiometric = false;
        this.mIsBouncerShowing = this.mKeyguardViewController.isBouncerShowing();
        this.mIsDozing = this.mStatusBarStateController.isDozing();
        this.mInterpolatedDarkAmount = this.mStatusBarStateController.getDozeAmount();
        this.mRunningFPS = this.mKeyguardUpdateMonitor.isFingerprintDetectionRunning();
        this.mCanDismissLockScreen = this.mKeyguardStateController.canDismissLockScreen();
        this.mStatusBarState = this.mStatusBarStateController.getState();
        ((LockIconView) this.mView).updateColorAndBackgroundVisibility();
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        this.mAuthController.addCallback(this.mAuthControllerCallback);
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
        this.mStatusBarStateController.addCallback(this.mStatusBarStateListener);
        this.mKeyguardStateController.addCallback(this.mKeyguardStateCallback);
        this.mDownDetected = false;
        updateBurnInOffsets();
        updateVisibility();
    }

    public final void updateConfiguration() {
        Rect bounds = ((WindowManager) getContext().getSystemService(WindowManager.class)).getCurrentWindowMetrics().getBounds();
        this.mWidthPixels = bounds.right;
        this.mHeightPixels = bounds.bottom;
        this.mBottomPaddingPx = getResources().getDimensionPixelSize(2131166121);
        this.mUnlockedLabel = ((LockIconView) this.mView).getContext().getResources().getString(2131951816);
        this.mLockedLabel = ((LockIconView) this.mView).getContext().getResources().getString(2131951753);
        if (this.mUdfpsSupported) {
            AuthController authController = this.mAuthController;
            Objects.requireNonNull(authController);
            SensorLocationInternal location = ((FingerprintSensorPropertiesInternal) authController.mUdfpsProps.get(0)).getLocation();
            ((LockIconView) this.mView).setCenterLocation(new PointF(location.sensorLocationX, location.sensorLocationY), location.sensorRadius);
        } else {
            float f = this.mHeightPixels - this.mBottomPaddingPx;
            int i = sLockIconRadiusPx;
            ((LockIconView) this.mView).setCenterLocation(new PointF(this.mWidthPixels / 2.0f, f - i), i);
        }
        ((LockIconView) this.mView).getHitRect(this.mSensorTouchLocation);
    }
}
