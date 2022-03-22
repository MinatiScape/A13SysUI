package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.util.MathUtils;
import android.view.View;
import androidx.preference.R$id;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline0;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.time.SystemClock;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes.dex */
public final class UdfpsKeyguardViewController extends UdfpsAnimationViewController<UdfpsKeyguardView> {
    public final ActivityLaunchAnimator mActivityLaunchAnimator;
    public float mActivityLaunchProgress;
    public final ConfigurationController mConfigurationController;
    public float mInputBouncerHiddenAmount;
    public boolean mIsBouncerVisible;
    public boolean mIsLaunchingActivity;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final StatusBarKeyguardViewManager mKeyguardViewManager;
    public float mLastDozeAmount;
    public boolean mLaunchTransitionFadingAway;
    public final LockscreenShadeTransitionController mLockScreenShadeTransitionController;
    public boolean mQsExpanded;
    public boolean mShowingUdfpsBouncer;
    public float mStatusBarExpansion;
    public int mStatusBarState;
    public final SystemClock mSystemClock;
    public float mTransitionToFullShadeProgress;
    public final UdfpsController mUdfpsController;
    public boolean mUdfpsRequested;
    public final UnlockedScreenOffAnimationController mUnlockedScreenOffAnimationController;
    public long mLastUdfpsBouncerShowTime = -1;
    public final AnonymousClass1 mStateListener = new AnonymousClass1();
    public final AnonymousClass2 mAlternateAuthInterceptor = new AnonymousClass2();
    public final AnonymousClass3 mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.biometrics.UdfpsKeyguardViewController.3
        {
            UdfpsKeyguardViewController.this = this;
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onConfigChanged(Configuration configuration) {
            ((UdfpsKeyguardView) UdfpsKeyguardViewController.this.mView).updateColor();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onThemeChanged() {
            ((UdfpsKeyguardView) UdfpsKeyguardViewController.this.mView).updateColor();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onUiModeChanged() {
            ((UdfpsKeyguardView) UdfpsKeyguardViewController.this.mView).updateColor();
        }
    };
    public final AnonymousClass4 mPanelExpansionListener = new PanelExpansionListener() { // from class: com.android.systemui.biometrics.UdfpsKeyguardViewController.4
        {
            UdfpsKeyguardViewController.this = this;
        }

        @Override // com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener
        public final void onPanelExpansionChanged(float f, boolean z, boolean z2) {
            UdfpsKeyguardViewController udfpsKeyguardViewController = UdfpsKeyguardViewController.this;
            udfpsKeyguardViewController.mStatusBarExpansion = f;
            udfpsKeyguardViewController.updateAlpha();
        }
    };
    public final AnonymousClass5 mKeyguardStateControllerCallback = new KeyguardStateController.Callback() { // from class: com.android.systemui.biometrics.UdfpsKeyguardViewController.5
        {
            UdfpsKeyguardViewController.this = this;
        }

        @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
        public final void onLaunchTransitionFadingAwayChanged() {
            UdfpsKeyguardViewController udfpsKeyguardViewController = UdfpsKeyguardViewController.this;
            udfpsKeyguardViewController.mLaunchTransitionFadingAway = udfpsKeyguardViewController.mKeyguardStateController.isLaunchTransitionFadingAway();
            UdfpsKeyguardViewController.this.updatePauseAuth();
        }
    };
    public final UdfpsKeyguardViewController$$ExternalSyntheticLambda0 mUnlockedScreenOffCallback = new UnlockedScreenOffAnimationController.Callback() { // from class: com.android.systemui.biometrics.UdfpsKeyguardViewController$$ExternalSyntheticLambda0
        @Override // com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController.Callback
        public final void onUnlockedScreenOffProgressUpdate(float f, float f2) {
            UdfpsKeyguardViewController udfpsKeyguardViewController = UdfpsKeyguardViewController.this;
            Objects.requireNonNull(udfpsKeyguardViewController);
            udfpsKeyguardViewController.mStateListener.onDozeAmountChanged(f, f2);
        }
    };
    public final AnonymousClass6 mActivityLaunchAnimatorListener = new ActivityLaunchAnimator.Listener() { // from class: com.android.systemui.biometrics.UdfpsKeyguardViewController.6
        {
            UdfpsKeyguardViewController.this = this;
        }

        @Override // com.android.systemui.animation.ActivityLaunchAnimator.Listener
        public final void onLaunchAnimationEnd() {
            UdfpsKeyguardViewController udfpsKeyguardViewController = UdfpsKeyguardViewController.this;
            udfpsKeyguardViewController.mIsLaunchingActivity = false;
            udfpsKeyguardViewController.updateAlpha();
        }

        @Override // com.android.systemui.animation.ActivityLaunchAnimator.Listener
        public final void onLaunchAnimationProgress(float f) {
            UdfpsKeyguardViewController udfpsKeyguardViewController = UdfpsKeyguardViewController.this;
            udfpsKeyguardViewController.mActivityLaunchProgress = f;
            udfpsKeyguardViewController.updateAlpha();
        }

        @Override // com.android.systemui.animation.ActivityLaunchAnimator.Listener
        public final void onLaunchAnimationStart() {
            UdfpsKeyguardViewController udfpsKeyguardViewController = UdfpsKeyguardViewController.this;
            udfpsKeyguardViewController.mIsLaunchingActivity = true;
            udfpsKeyguardViewController.mActivityLaunchProgress = 0.0f;
            udfpsKeyguardViewController.updateAlpha();
        }
    };

    /* renamed from: com.android.systemui.biometrics.UdfpsKeyguardViewController$1 */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements StatusBarStateController.StateListener {
        public AnonymousClass1() {
            UdfpsKeyguardViewController.this = r1;
        }

        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public final void onDozeAmountChanged(float f, float f2) {
            UdfpsKeyguardViewController udfpsKeyguardViewController = UdfpsKeyguardViewController.this;
            if (udfpsKeyguardViewController.mLastDozeAmount < f) {
                UdfpsKeyguardViewController.m31$$Nest$mshowUdfpsBouncer(udfpsKeyguardViewController, false);
            }
            UdfpsKeyguardView udfpsKeyguardView = (UdfpsKeyguardView) UdfpsKeyguardViewController.this.mView;
            Objects.requireNonNull(udfpsKeyguardView);
            udfpsKeyguardView.mInterpolatedDarkAmount = f2;
            udfpsKeyguardView.updateAlpha();
            udfpsKeyguardView.updateBurnInOffsets();
            UdfpsKeyguardViewController udfpsKeyguardViewController2 = UdfpsKeyguardViewController.this;
            udfpsKeyguardViewController2.mLastDozeAmount = f;
            udfpsKeyguardViewController2.updatePauseAuth();
        }

        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public final void onStateChanged(int i) {
            UdfpsKeyguardViewController udfpsKeyguardViewController = UdfpsKeyguardViewController.this;
            udfpsKeyguardViewController.mStatusBarState = i;
            Objects.requireNonNull((UdfpsKeyguardView) udfpsKeyguardViewController.mView);
            UdfpsKeyguardViewController.this.updatePauseAuth();
        }
    }

    /* renamed from: com.android.systemui.biometrics.UdfpsKeyguardViewController$2 */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements StatusBarKeyguardViewManager.AlternateAuthInterceptor {
        public AnonymousClass2() {
            UdfpsKeyguardViewController.this = r1;
        }
    }

    /* JADX WARN: Type inference failed for: r0v4, types: [com.android.systemui.biometrics.UdfpsKeyguardViewController$3] */
    /* JADX WARN: Type inference failed for: r0v5, types: [com.android.systemui.biometrics.UdfpsKeyguardViewController$4] */
    /* JADX WARN: Type inference failed for: r0v6, types: [com.android.systemui.biometrics.UdfpsKeyguardViewController$5] */
    /* JADX WARN: Type inference failed for: r0v7, types: [com.android.systemui.biometrics.UdfpsKeyguardViewController$$ExternalSyntheticLambda0] */
    /* JADX WARN: Type inference failed for: r0v8, types: [com.android.systemui.biometrics.UdfpsKeyguardViewController$6] */
    public UdfpsKeyguardViewController(UdfpsKeyguardView udfpsKeyguardView, StatusBarStateController statusBarStateController, PanelExpansionStateManager panelExpansionStateManager, StatusBarKeyguardViewManager statusBarKeyguardViewManager, KeyguardUpdateMonitor keyguardUpdateMonitor, DumpManager dumpManager, LockscreenShadeTransitionController lockscreenShadeTransitionController, ConfigurationController configurationController, SystemClock systemClock, KeyguardStateController keyguardStateController, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, SystemUIDialogManager systemUIDialogManager, UdfpsController udfpsController, ActivityLaunchAnimator activityLaunchAnimator) {
        super(udfpsKeyguardView, statusBarStateController, panelExpansionStateManager, systemUIDialogManager, dumpManager);
        this.mKeyguardViewManager = statusBarKeyguardViewManager;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mLockScreenShadeTransitionController = lockscreenShadeTransitionController;
        this.mConfigurationController = configurationController;
        this.mSystemClock = systemClock;
        this.mKeyguardStateController = keyguardStateController;
        this.mUdfpsController = udfpsController;
        this.mUnlockedScreenOffAnimationController = unlockedScreenOffAnimationController;
        this.mActivityLaunchAnimator = activityLaunchAnimator;
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController
    public final String getTag() {
        return "UdfpsKeyguardViewController";
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController, com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println(Intrinsics.stringPlus("mNotificationShadeVisible=", Boolean.valueOf(this.notificationShadeVisible)));
        printWriter.println(Intrinsics.stringPlus("shouldPauseAuth()=", Boolean.valueOf(shouldPauseAuth())));
        printWriter.println(Intrinsics.stringPlus("isPauseAuth=", Boolean.valueOf(getView().mPauseAuth)));
        printWriter.println("mShowingUdfpsBouncer=" + this.mShowingUdfpsBouncer);
        printWriter.println("mFaceDetectRunning=false");
        printWriter.println("mStatusBarState=" + R$id.toShortString(this.mStatusBarState));
        StringBuilder sb = new StringBuilder();
        sb.append("mQsExpanded=");
        StringBuilder m = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(sb, this.mQsExpanded, printWriter, "mIsBouncerVisible="), this.mIsBouncerVisible, printWriter, "mInputBouncerHiddenAmount=");
        m.append(this.mInputBouncerHiddenAmount);
        printWriter.println(m.toString());
        printWriter.println("mStatusBarExpansion=" + this.mStatusBarExpansion);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("unpausedAlpha=");
        UdfpsKeyguardView udfpsKeyguardView = (UdfpsKeyguardView) this.mView;
        Objects.requireNonNull(udfpsKeyguardView);
        sb2.append(udfpsKeyguardView.mAlpha);
        printWriter.println(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append("mUdfpsRequested=");
        StringBuilder m2 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(sb3, this.mUdfpsRequested, printWriter, "mView.mUdfpsRequested="), ((UdfpsKeyguardView) this.mView).mUdfpsRequested, printWriter, "mLaunchTransitionFadingAway=");
        m2.append(this.mLaunchTransitionFadingAway);
        printWriter.println(m2.toString());
    }

    @Override // com.android.systemui.util.ViewController
    public final void onInit() {
        StatusBarKeyguardViewManager statusBarKeyguardViewManager = this.mKeyguardViewManager;
        AnonymousClass2 r2 = this.mAlternateAuthInterceptor;
        Objects.requireNonNull(statusBarKeyguardViewManager);
        if (!Objects.equals(statusBarKeyguardViewManager.mAlternateAuthInterceptor, r2)) {
            statusBarKeyguardViewManager.mAlternateAuthInterceptor = r2;
            statusBarKeyguardViewManager.resetAlternateAuth(false);
        }
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController
    public final void onTouchOutsideView() {
        boolean z;
        if (this.mShowingUdfpsBouncer) {
            if (this.mSystemClock.uptimeMillis() - this.mLastUdfpsBouncerShowTime > 200) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                this.mKeyguardViewManager.showBouncer(true);
            }
        }
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController
    public final boolean shouldPauseAuth() {
        if (this.mShowingUdfpsBouncer) {
            return false;
        }
        if (this.mUdfpsRequested && !this.notificationShadeVisible && ((!this.mIsBouncerVisible || this.mInputBouncerHiddenAmount != 0.0f) && this.mKeyguardStateController.isShowing())) {
            return false;
        }
        if (!this.dialogManager.shouldHideAffordance() && !this.mLaunchTransitionFadingAway && this.mStatusBarState == 1 && !this.mQsExpanded && this.mInputBouncerHiddenAmount >= 0.5f && !this.mIsBouncerVisible) {
            return false;
        }
        return true;
    }

    public final void updateAlpha() {
        float f;
        int i;
        if (this.mUdfpsRequested) {
            f = this.mInputBouncerHiddenAmount;
        } else {
            f = this.mStatusBarExpansion;
        }
        if (this.mShowingUdfpsBouncer) {
            i = 255;
        } else {
            i = (int) MathUtils.constrain(MathUtils.map(0.5f, 0.9f, 0.0f, 255.0f, f), 0.0f, 255.0f);
        }
        if (!this.mShowingUdfpsBouncer) {
            i = (int) ((1.0f - this.mTransitionToFullShadeProgress) * i);
            if (this.mIsLaunchingActivity && !this.mUdfpsRequested) {
                i = (int) ((1.0f - this.mActivityLaunchProgress) * i);
            }
        }
        UdfpsKeyguardView udfpsKeyguardView = (UdfpsKeyguardView) this.mView;
        Objects.requireNonNull(udfpsKeyguardView);
        udfpsKeyguardView.mAlpha = i;
        udfpsKeyguardView.updateAlpha();
    }

    /* renamed from: -$$Nest$mshowUdfpsBouncer */
    public static boolean m31$$Nest$mshowUdfpsBouncer(UdfpsKeyguardViewController udfpsKeyguardViewController, boolean z) {
        Objects.requireNonNull(udfpsKeyguardViewController);
        if (udfpsKeyguardViewController.mShowingUdfpsBouncer == z) {
            return false;
        }
        boolean shouldPauseAuth = udfpsKeyguardViewController.shouldPauseAuth();
        udfpsKeyguardViewController.mShowingUdfpsBouncer = z;
        if (z) {
            udfpsKeyguardViewController.mLastUdfpsBouncerShowTime = udfpsKeyguardViewController.mSystemClock.uptimeMillis();
        }
        if (udfpsKeyguardViewController.mShowingUdfpsBouncer) {
            if (shouldPauseAuth) {
                UdfpsKeyguardView udfpsKeyguardView = (UdfpsKeyguardView) udfpsKeyguardViewController.mView;
                Objects.requireNonNull(udfpsKeyguardView);
                if (!udfpsKeyguardView.mBackgroundInAnimator.isRunning() && udfpsKeyguardView.mFullyInflated) {
                    AnimatorSet animatorSet = new AnimatorSet();
                    udfpsKeyguardView.mBackgroundInAnimator = animatorSet;
                    animatorSet.playTogether(ObjectAnimator.ofFloat(udfpsKeyguardView.mBgProtection, View.ALPHA, 0.0f, 1.0f), ObjectAnimator.ofFloat(udfpsKeyguardView.mBgProtection, View.SCALE_X, 0.0f, 1.0f), ObjectAnimator.ofFloat(udfpsKeyguardView.mBgProtection, View.SCALE_Y, 0.0f, 1.0f));
                    udfpsKeyguardView.mBackgroundInAnimator.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
                    udfpsKeyguardView.mBackgroundInAnimator.setDuration(500L);
                    udfpsKeyguardView.mBackgroundInAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.biometrics.UdfpsKeyguardView.1
                        public final /* synthetic */ Runnable val$onEndAnimation = null;

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public final void onAnimationEnd(Animator animator) {
                            Runnable runnable = this.val$onEndAnimation;
                            if (runnable != null) {
                                runnable.run();
                            }
                        }
                    });
                    udfpsKeyguardView.mBackgroundInAnimator.start();
                }
            }
            StatusBarKeyguardViewManager statusBarKeyguardViewManager = udfpsKeyguardViewController.mKeyguardViewManager;
            Objects.requireNonNull(statusBarKeyguardViewManager);
            if (statusBarKeyguardViewManager.mOccluded) {
                KeyguardUpdateMonitor keyguardUpdateMonitor = udfpsKeyguardViewController.mKeyguardUpdateMonitor;
                Objects.requireNonNull(keyguardUpdateMonitor);
                keyguardUpdateMonitor.mOccludingAppRequestingFace = true;
                keyguardUpdateMonitor.updateFaceListeningState(2);
            }
            UdfpsKeyguardView udfpsKeyguardView2 = (UdfpsKeyguardView) udfpsKeyguardViewController.mView;
            udfpsKeyguardView2.announceForAccessibility(udfpsKeyguardView2.getContext().getString(2131951720));
        } else {
            KeyguardUpdateMonitor keyguardUpdateMonitor2 = udfpsKeyguardViewController.mKeyguardUpdateMonitor;
            Objects.requireNonNull(keyguardUpdateMonitor2);
            keyguardUpdateMonitor2.mOccludingAppRequestingFace = false;
            keyguardUpdateMonitor2.updateFaceListeningState(2);
        }
        udfpsKeyguardViewController.updateAlpha();
        udfpsKeyguardViewController.updatePauseAuth();
        return true;
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController, com.android.systemui.util.ViewController
    public final void onViewAttached() {
        super.onViewAttached();
        float dozeAmount = this.statusBarStateController.getDozeAmount();
        this.mLastDozeAmount = dozeAmount;
        this.mStateListener.onDozeAmountChanged(dozeAmount, dozeAmount);
        this.statusBarStateController.addCallback(this.mStateListener);
        this.mUdfpsRequested = false;
        this.mLaunchTransitionFadingAway = this.mKeyguardStateController.isLaunchTransitionFadingAway();
        this.mKeyguardStateController.addCallback(this.mKeyguardStateControllerCallback);
        this.mStatusBarState = this.statusBarStateController.getState();
        StatusBarKeyguardViewManager statusBarKeyguardViewManager = this.mKeyguardViewManager;
        Objects.requireNonNull(statusBarKeyguardViewManager);
        this.mQsExpanded = statusBarKeyguardViewManager.mQsExpanded;
        this.mInputBouncerHiddenAmount = 1.0f;
        this.mIsBouncerVisible = this.mKeyguardViewManager.bouncerIsOrWillBeShowing();
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        this.panelExpansionStateManager.addExpansionListener(this.mPanelExpansionListener);
        updateAlpha();
        updatePauseAuth();
        StatusBarKeyguardViewManager statusBarKeyguardViewManager2 = this.mKeyguardViewManager;
        AnonymousClass2 r2 = this.mAlternateAuthInterceptor;
        Objects.requireNonNull(statusBarKeyguardViewManager2);
        if (!Objects.equals(statusBarKeyguardViewManager2.mAlternateAuthInterceptor, r2)) {
            statusBarKeyguardViewManager2.mAlternateAuthInterceptor = r2;
            statusBarKeyguardViewManager2.resetAlternateAuth(false);
        }
        LockscreenShadeTransitionController lockscreenShadeTransitionController = this.mLockScreenShadeTransitionController;
        Objects.requireNonNull(lockscreenShadeTransitionController);
        lockscreenShadeTransitionController.udfpsKeyguardViewController = this;
        UnlockedScreenOffAnimationController unlockedScreenOffAnimationController = this.mUnlockedScreenOffAnimationController;
        UdfpsKeyguardViewController$$ExternalSyntheticLambda0 udfpsKeyguardViewController$$ExternalSyntheticLambda0 = this.mUnlockedScreenOffCallback;
        Objects.requireNonNull(unlockedScreenOffAnimationController);
        unlockedScreenOffAnimationController.callbacks.add(udfpsKeyguardViewController$$ExternalSyntheticLambda0);
        ActivityLaunchAnimator activityLaunchAnimator = this.mActivityLaunchAnimator;
        AnonymousClass6 r4 = this.mActivityLaunchAnimatorListener;
        Objects.requireNonNull(activityLaunchAnimator);
        activityLaunchAnimator.listeners.add(r4);
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController, com.android.systemui.util.ViewController
    public final void onViewDetached() {
        super.onViewDetached();
        this.mKeyguardStateController.removeCallback(this.mKeyguardStateControllerCallback);
        this.statusBarStateController.removeCallback(this.mStateListener);
        StatusBarKeyguardViewManager statusBarKeyguardViewManager = this.mKeyguardViewManager;
        AnonymousClass2 r1 = this.mAlternateAuthInterceptor;
        Objects.requireNonNull(statusBarKeyguardViewManager);
        if (Objects.equals(statusBarKeyguardViewManager.mAlternateAuthInterceptor, r1)) {
            statusBarKeyguardViewManager.mAlternateAuthInterceptor = null;
            statusBarKeyguardViewManager.resetAlternateAuth(true);
        }
        KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateMonitor;
        Objects.requireNonNull(keyguardUpdateMonitor);
        keyguardUpdateMonitor.mOccludingAppRequestingFace = false;
        keyguardUpdateMonitor.updateFaceListeningState(2);
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        PanelExpansionStateManager panelExpansionStateManager = this.panelExpansionStateManager;
        AnonymousClass4 r12 = this.mPanelExpansionListener;
        Objects.requireNonNull(panelExpansionStateManager);
        panelExpansionStateManager.expansionListeners.remove(r12);
        LockscreenShadeTransitionController lockscreenShadeTransitionController = this.mLockScreenShadeTransitionController;
        Objects.requireNonNull(lockscreenShadeTransitionController);
        if (lockscreenShadeTransitionController.udfpsKeyguardViewController == this) {
            LockscreenShadeTransitionController lockscreenShadeTransitionController2 = this.mLockScreenShadeTransitionController;
            Objects.requireNonNull(lockscreenShadeTransitionController2);
            lockscreenShadeTransitionController2.udfpsKeyguardViewController = null;
        }
        UnlockedScreenOffAnimationController unlockedScreenOffAnimationController = this.mUnlockedScreenOffAnimationController;
        UdfpsKeyguardViewController$$ExternalSyntheticLambda0 udfpsKeyguardViewController$$ExternalSyntheticLambda0 = this.mUnlockedScreenOffCallback;
        Objects.requireNonNull(unlockedScreenOffAnimationController);
        unlockedScreenOffAnimationController.callbacks.remove(udfpsKeyguardViewController$$ExternalSyntheticLambda0);
        ActivityLaunchAnimator activityLaunchAnimator = this.mActivityLaunchAnimator;
        AnonymousClass6 r3 = this.mActivityLaunchAnimatorListener;
        Objects.requireNonNull(activityLaunchAnimator);
        activityLaunchAnimator.listeners.remove(r3);
    }
}
