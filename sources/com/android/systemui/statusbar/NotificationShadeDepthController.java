package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.SystemClock;
import android.util.IndentingPrintWriter;
import android.util.MathUtils;
import android.view.Choreographer;
import android.view.View;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.WallpaperController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationShadeDepthController.kt */
/* loaded from: classes.dex */
public final class NotificationShadeDepthController implements PanelExpansionListener, Dumpable {
    public final BiometricUnlockController biometricUnlockController;
    public View blurRoot;
    public final BlurUtils blurUtils;
    public boolean blursDisabledForAppLaunch;
    public boolean blursDisabledForUnlock;
    public final Choreographer choreographer;
    public final DozeParameters dozeParameters;
    public boolean isBlurred;
    public boolean isOpen;
    public Animator keyguardAnimator;
    public final KeyguardStateController keyguardStateController;
    public int lastAppliedBlur;
    public final NotificationShadeWindowController notificationShadeWindowController;
    public float panelPullDownMinFraction;
    public int prevShadeDirection;
    public float prevShadeVelocity;
    public boolean prevTracking;
    public float qsPanelExpansion;
    public View root;
    public boolean scrimsVisible;
    public float shadeExpansion;
    public final StatusBarStateController statusBarStateController;
    public float transitionToFullShadeProgress;
    public boolean updateScheduled;
    public float wakeAndUnlockBlurRadius;
    public final WallpaperController wallpaperController;
    public boolean isClosed = true;
    public ArrayList listeners = new ArrayList();
    public long prevTimestamp = -1;
    public DepthAnimation shadeAnimation = new DepthAnimation();
    public DepthAnimation brightnessMirrorSpring = new DepthAnimation();
    public final NotificationShadeDepthController$updateBlurCallback$1 updateBlurCallback = new Choreographer.FrameCallback() { // from class: com.android.systemui.statusbar.NotificationShadeDepthController$updateBlurCallback$1
        /* JADX WARN: Code restructure failed: missing block: B:9:0x008e, code lost:
            if (r0.blursDisabledForUnlock != false) goto L_0x0090;
         */
        @Override // android.view.Choreographer.FrameCallback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void doFrame(long r8) {
            /*
                Method dump skipped, instructions count: 343
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.NotificationShadeDepthController$updateBlurCallback$1.doFrame(long):void");
        }
    };

    /* compiled from: NotificationShadeDepthController.kt */
    /* loaded from: classes.dex */
    public final class DepthAnimation {
        public int pendingRadius = -1;
        public float radius;
        public SpringAnimation springAnimation;
        public View view;

        public static void animateTo$default(DepthAnimation depthAnimation, int i) {
            Objects.requireNonNull(depthAnimation);
            if (depthAnimation.pendingRadius != i || !Intrinsics.areEqual(depthAnimation.view, null)) {
                depthAnimation.view = null;
                depthAnimation.pendingRadius = i;
                depthAnimation.springAnimation.animateToFinalPosition(i);
            }
        }

        public DepthAnimation() {
            NotificationShadeDepthController.this = r3;
            SpringAnimation springAnimation = new SpringAnimation(this, new FloatPropertyCompat<DepthAnimation>() { // from class: com.android.systemui.statusbar.NotificationShadeDepthController$DepthAnimation$springAnimation$1
                @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
                public final float getValue(NotificationShadeDepthController.DepthAnimation depthAnimation) {
                    NotificationShadeDepthController.DepthAnimation depthAnimation2 = NotificationShadeDepthController.DepthAnimation.this;
                    Objects.requireNonNull(depthAnimation2);
                    return depthAnimation2.radius;
                }

                @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
                public final void setValue(NotificationShadeDepthController.DepthAnimation depthAnimation, float f) {
                    NotificationShadeDepthController.DepthAnimation depthAnimation2 = NotificationShadeDepthController.DepthAnimation.this;
                    Objects.requireNonNull(depthAnimation2);
                    depthAnimation2.radius = f;
                    r2.scheduleUpdate(NotificationShadeDepthController.DepthAnimation.this.view);
                }
            });
            this.springAnimation = springAnimation;
            SpringForce springForce = new SpringForce(0.0f);
            Objects.requireNonNull(springAnimation);
            springAnimation.mSpring = springForce;
            SpringAnimation springAnimation2 = this.springAnimation;
            Objects.requireNonNull(springAnimation2);
            springAnimation2.mSpring.setDampingRatio(1.0f);
            SpringAnimation springAnimation3 = this.springAnimation;
            Objects.requireNonNull(springAnimation3);
            springAnimation3.mSpring.setStiffness(10000.0f);
            this.springAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener() { // from class: com.android.systemui.statusbar.NotificationShadeDepthController.DepthAnimation.1
                {
                    DepthAnimation.this = this;
                }

                @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
                public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
                    DepthAnimation.this.pendingRadius = -1;
                }
            });
        }
    }

    /* compiled from: NotificationShadeDepthController.kt */
    /* loaded from: classes.dex */
    public interface DepthListener {
        default void onBlurRadiusChanged(int i) {
        }

        void onWallpaperZoomOutChanged(float f);
    }

    public static /* synthetic */ void getBrightnessMirrorSpring$annotations() {
    }

    public static /* synthetic */ void getShadeExpansion$annotations() {
    }

    public static /* synthetic */ void getUpdateBlurCallback$annotations() {
    }

    public final void animateBlur(boolean z, float f) {
        float f2;
        this.isBlurred = z;
        if (!z || !shouldApplyShadeBlur()) {
            f2 = 0.0f;
        } else {
            f2 = 1.0f;
        }
        DepthAnimation depthAnimation = this.shadeAnimation;
        Objects.requireNonNull(depthAnimation);
        SpringAnimation springAnimation = depthAnimation.springAnimation;
        Objects.requireNonNull(springAnimation);
        springAnimation.mVelocity = f;
        DepthAnimation.animateTo$default(this.shadeAnimation, (int) this.blurUtils.blurRadiusOfRatio(f2));
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("StatusBarWindowBlurController:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println(Intrinsics.stringPlus("shadeExpansion: ", Float.valueOf(this.shadeExpansion)));
        indentingPrintWriter.println(Intrinsics.stringPlus("shouldApplyShaeBlur: ", Boolean.valueOf(shouldApplyShadeBlur())));
        DepthAnimation depthAnimation = this.shadeAnimation;
        Objects.requireNonNull(depthAnimation);
        indentingPrintWriter.println(Intrinsics.stringPlus("shadeAnimation: ", Float.valueOf(depthAnimation.radius)));
        DepthAnimation depthAnimation2 = this.brightnessMirrorSpring;
        Objects.requireNonNull(depthAnimation2);
        indentingPrintWriter.println(Intrinsics.stringPlus("brightnessMirrorRadius: ", Float.valueOf(depthAnimation2.radius)));
        indentingPrintWriter.println(Intrinsics.stringPlus("wakeAndUnlockBlur: ", Float.valueOf(this.wakeAndUnlockBlurRadius)));
        indentingPrintWriter.println(Intrinsics.stringPlus("blursDisabledForAppLaunch: ", Boolean.valueOf(this.blursDisabledForAppLaunch)));
        indentingPrintWriter.println(Intrinsics.stringPlus("qsPanelExpansion: ", Float.valueOf(this.qsPanelExpansion)));
        indentingPrintWriter.println(Intrinsics.stringPlus("transitionToFullShadeProgress: ", Float.valueOf(this.transitionToFullShadeProgress)));
        indentingPrintWriter.println(Intrinsics.stringPlus("lastAppliedBlur: ", Integer.valueOf(this.lastAppliedBlur)));
    }

    public final void scheduleUpdate(View view) {
        if (!this.updateScheduled) {
            this.updateScheduled = true;
            this.blurRoot = view;
            this.choreographer.postFrameCallback(this.updateBlurCallback);
        }
    }

    public final boolean shouldApplyShadeBlur() {
        int state = this.statusBarStateController.getState();
        if ((state == 0 || state == 2) && !this.keyguardStateController.isKeyguardFadingAway()) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.statusbar.NotificationShadeDepthController$1] */
    /* JADX WARN: Type inference failed for: r2v6, types: [com.android.systemui.statusbar.NotificationShadeDepthController$updateBlurCallback$1] */
    public NotificationShadeDepthController(StatusBarStateController statusBarStateController, BlurUtils blurUtils, BiometricUnlockController biometricUnlockController, KeyguardStateController keyguardStateController, Choreographer choreographer, WallpaperController wallpaperController, NotificationShadeWindowController notificationShadeWindowController, DozeParameters dozeParameters, DumpManager dumpManager) {
        this.statusBarStateController = statusBarStateController;
        this.blurUtils = blurUtils;
        this.biometricUnlockController = biometricUnlockController;
        this.keyguardStateController = keyguardStateController;
        this.choreographer = choreographer;
        this.wallpaperController = wallpaperController;
        this.notificationShadeWindowController = notificationShadeWindowController;
        this.dozeParameters = dozeParameters;
        KeyguardStateController.Callback notificationShadeDepthController$keyguardStateCallback$1 = new KeyguardStateController.Callback() { // from class: com.android.systemui.statusbar.NotificationShadeDepthController$keyguardStateCallback$1
            @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
            public final void onKeyguardFadingAwayChanged() {
                if (NotificationShadeDepthController.this.keyguardStateController.isKeyguardFadingAway()) {
                    BiometricUnlockController biometricUnlockController2 = NotificationShadeDepthController.this.biometricUnlockController;
                    Objects.requireNonNull(biometricUnlockController2);
                    if (biometricUnlockController2.mMode == 1) {
                        Animator animator = NotificationShadeDepthController.this.keyguardAnimator;
                        if (animator != null) {
                            animator.cancel();
                        }
                        NotificationShadeDepthController notificationShadeDepthController = NotificationShadeDepthController.this;
                        ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
                        final NotificationShadeDepthController notificationShadeDepthController2 = NotificationShadeDepthController.this;
                        DozeParameters dozeParameters2 = notificationShadeDepthController2.dozeParameters;
                        Objects.requireNonNull(dozeParameters2);
                        ofFloat.setDuration(dozeParameters2.mAlwaysOnPolicy.wallpaperFadeOutDuration);
                        ofFloat.setStartDelay(notificationShadeDepthController2.keyguardStateController.getKeyguardFadingAwayDelay());
                        ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
                        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.NotificationShadeDepthController$keyguardStateCallback$1$onKeyguardFadingAwayChanged$1$1
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                                boolean z;
                                NotificationShadeDepthController notificationShadeDepthController3 = NotificationShadeDepthController.this;
                                BlurUtils blurUtils2 = notificationShadeDepthController3.blurUtils;
                                Object animatedValue = valueAnimator.getAnimatedValue();
                                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                                float blurRadiusOfRatio = blurUtils2.blurRadiusOfRatio(((Float) animatedValue).floatValue());
                                if (notificationShadeDepthController3.wakeAndUnlockBlurRadius == blurRadiusOfRatio) {
                                    z = true;
                                } else {
                                    z = false;
                                }
                                if (!z) {
                                    notificationShadeDepthController3.wakeAndUnlockBlurRadius = blurRadiusOfRatio;
                                    notificationShadeDepthController3.scheduleUpdate(null);
                                }
                            }
                        });
                        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.NotificationShadeDepthController$keyguardStateCallback$1$onKeyguardFadingAwayChanged$1$2
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public final void onAnimationEnd(Animator animator2) {
                                NotificationShadeDepthController notificationShadeDepthController3 = NotificationShadeDepthController.this;
                                notificationShadeDepthController3.keyguardAnimator = null;
                                notificationShadeDepthController3.scheduleUpdate(null);
                            }
                        });
                        ofFloat.start();
                        notificationShadeDepthController.keyguardAnimator = ofFloat;
                    }
                }
            }

            @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
            public final void onKeyguardShowingChanged() {
                if (NotificationShadeDepthController.this.keyguardStateController.isShowing()) {
                    Animator animator = NotificationShadeDepthController.this.keyguardAnimator;
                    if (animator != null) {
                        animator.cancel();
                    }
                    Objects.requireNonNull(NotificationShadeDepthController.this);
                }
            }
        };
        StatusBarStateController.StateListener notificationShadeDepthController$statusBarStateCallback$1 = new StatusBarStateController.StateListener() { // from class: com.android.systemui.statusbar.NotificationShadeDepthController$statusBarStateCallback$1
            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public final void onDozeAmountChanged(float f, float f2) {
                boolean z;
                NotificationShadeDepthController notificationShadeDepthController = NotificationShadeDepthController.this;
                float blurRadiusOfRatio = notificationShadeDepthController.blurUtils.blurRadiusOfRatio(f2);
                if (notificationShadeDepthController.wakeAndUnlockBlurRadius == blurRadiusOfRatio) {
                    z = true;
                } else {
                    z = false;
                }
                if (!z) {
                    notificationShadeDepthController.wakeAndUnlockBlurRadius = blurRadiusOfRatio;
                    notificationShadeDepthController.scheduleUpdate(null);
                }
                NotificationShadeDepthController.this.scheduleUpdate(null);
            }

            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public final void onDozingChanged(boolean z) {
                if (z) {
                    NotificationShadeDepthController notificationShadeDepthController = NotificationShadeDepthController.this;
                    Objects.requireNonNull(notificationShadeDepthController);
                    NotificationShadeDepthController.DepthAnimation depthAnimation = notificationShadeDepthController.shadeAnimation;
                    Objects.requireNonNull(depthAnimation);
                    SpringAnimation springAnimation = depthAnimation.springAnimation;
                    Objects.requireNonNull(springAnimation);
                    if (springAnimation.mRunning) {
                        depthAnimation.springAnimation.skipToEnd();
                    }
                    NotificationShadeDepthController notificationShadeDepthController2 = NotificationShadeDepthController.this;
                    Objects.requireNonNull(notificationShadeDepthController2);
                    NotificationShadeDepthController.DepthAnimation depthAnimation2 = notificationShadeDepthController2.brightnessMirrorSpring;
                    Objects.requireNonNull(depthAnimation2);
                    SpringAnimation springAnimation2 = depthAnimation2.springAnimation;
                    Objects.requireNonNull(springAnimation2);
                    if (springAnimation2.mRunning) {
                        depthAnimation2.springAnimation.skipToEnd();
                    }
                }
            }

            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public final void onStateChanged(int i) {
                NotificationShadeDepthController notificationShadeDepthController = NotificationShadeDepthController.this;
                Objects.requireNonNull(notificationShadeDepthController);
                float f = notificationShadeDepthController.shadeExpansion;
                NotificationShadeDepthController notificationShadeDepthController2 = NotificationShadeDepthController.this;
                notificationShadeDepthController.updateShadeAnimationBlur(f, notificationShadeDepthController2.prevTracking, notificationShadeDepthController2.prevShadeVelocity, notificationShadeDepthController2.prevShadeDirection);
                NotificationShadeDepthController.this.scheduleUpdate(null);
            }
        };
        dumpManager.registerDumpable(NotificationShadeDepthController.class.getName(), this);
        keyguardStateController.addCallback(notificationShadeDepthController$keyguardStateCallback$1);
        statusBarStateController.addCallback(notificationShadeDepthController$statusBarStateCallback$1);
        notificationShadeWindowController.setScrimsVisibilityListener(new Consumer() { // from class: com.android.systemui.statusbar.NotificationShadeDepthController.1
            {
                NotificationShadeDepthController.this = this;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                boolean z;
                Integer num = (Integer) obj;
                NotificationShadeDepthController notificationShadeDepthController = NotificationShadeDepthController.this;
                if (num != null && num.intValue() == 2) {
                    z = true;
                } else {
                    z = false;
                }
                Objects.requireNonNull(notificationShadeDepthController);
                if (notificationShadeDepthController.scrimsVisible != z) {
                    notificationShadeDepthController.scrimsVisible = z;
                    notificationShadeDepthController.scheduleUpdate(null);
                }
            }
        });
        DepthAnimation depthAnimation = this.shadeAnimation;
        Objects.requireNonNull(depthAnimation);
        SpringAnimation springAnimation = depthAnimation.springAnimation;
        Objects.requireNonNull(springAnimation);
        springAnimation.mSpring.setStiffness(200.0f);
        DepthAnimation depthAnimation2 = this.shadeAnimation;
        Objects.requireNonNull(depthAnimation2);
        SpringAnimation springAnimation2 = depthAnimation2.springAnimation;
        Objects.requireNonNull(springAnimation2);
        springAnimation2.mSpring.setDampingRatio(1.0f);
    }

    @Override // com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener
    public final void onPanelExpansionChanged(float f, boolean z, boolean z2) {
        boolean z3;
        long elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
        float f2 = this.panelPullDownMinFraction;
        float f3 = 1.0f;
        float saturate = MathUtils.saturate((f - f2) / (1.0f - f2));
        if (this.shadeExpansion == saturate) {
            z3 = true;
        } else {
            z3 = false;
        }
        if (!z3 || this.prevTracking != z2) {
            long j = this.prevTimestamp;
            if (j < 0) {
                this.prevTimestamp = elapsedRealtimeNanos;
            } else {
                f3 = MathUtils.constrain((float) ((elapsedRealtimeNanos - j) / 1.0E9d), 1.0E-5f, 1.0f);
            }
            float f4 = saturate - this.shadeExpansion;
            int signum = (int) Math.signum(f4);
            float constrain = MathUtils.constrain((f4 * 100.0f) / f3, -3000.0f, 3000.0f);
            updateShadeAnimationBlur(saturate, z2, constrain, signum);
            this.prevShadeDirection = signum;
            this.prevShadeVelocity = constrain;
            this.shadeExpansion = saturate;
            this.prevTracking = z2;
            this.prevTimestamp = elapsedRealtimeNanos;
            scheduleUpdate(null);
            return;
        }
        this.prevTimestamp = elapsedRealtimeNanos;
    }

    public final void updateShadeAnimationBlur(float f, boolean z, float f2, int i) {
        boolean z2;
        if (!shouldApplyShadeBlur()) {
            animateBlur(false, 0.0f);
            this.isClosed = true;
            this.isOpen = false;
        } else if (f > 0.0f) {
            if (this.isClosed) {
                animateBlur(true, f2);
                this.isClosed = false;
            }
            if (z && !this.isBlurred) {
                animateBlur(true, 0.0f);
            }
            if (!z && i < 0 && this.isBlurred) {
                animateBlur(false, f2);
            }
            if (f == 1.0f) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (!z2) {
                this.isOpen = false;
            } else if (!this.isOpen) {
                this.isOpen = true;
                if (!this.isBlurred) {
                    animateBlur(true, f2);
                }
            }
        } else if (!this.isClosed) {
            this.isClosed = true;
            if (this.isBlurred) {
                animateBlur(false, f2);
            }
        }
    }
}
