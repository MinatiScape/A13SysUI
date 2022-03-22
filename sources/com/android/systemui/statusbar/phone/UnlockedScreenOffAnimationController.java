package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.keyguard.KeyguardVisibilityHelper$$ExternalSyntheticLambda0;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.LightRevealScrim;
import com.android.systemui.statusbar.StatusBarStateControllerImpl;
import com.android.systemui.statusbar.notification.AnimatableProperty;
import com.android.systemui.statusbar.notification.PropertyAnimator;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.settings.GlobalSettings;
import dagger.Lazy;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
/* compiled from: UnlockedScreenOffAnimationController.kt */
/* loaded from: classes.dex */
public final class UnlockedScreenOffAnimationController implements WakefulnessLifecycle.Observer, ScreenOffAnimation {
    public boolean aodUiAnimationPlaying;
    public final Context context;
    public Boolean decidedToAnimateGoingToSleep;
    public final Lazy<DozeParameters> dozeParameters;
    public final GlobalSettings globalSettings;
    public final Handler handler;
    public final InteractionJankMonitor interactionJankMonitor;
    public final KeyguardStateController keyguardStateController;
    public final Lazy<KeyguardViewMediator> keyguardViewMediatorLazy;
    public boolean lightRevealAnimationPlaying;
    public final ValueAnimator lightRevealAnimator;
    public LightRevealScrim lightRevealScrim;
    public final PowerManager powerManager;
    public boolean shouldAnimateInKeyguard;
    public StatusBar statusBar;
    public final StatusBarStateControllerImpl statusBarStateControllerImpl;
    public final WakefulnessLifecycle wakefulnessLifecycle;
    public float animatorDurationScale = 1.0f;
    public HashSet<Callback> callbacks = new HashSet<>();
    public final UnlockedScreenOffAnimationController$animatorDurationScaleObserver$1 animatorDurationScaleObserver = new ContentObserver() { // from class: com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController$animatorDurationScaleObserver$1
        {
            super(null);
        }

        @Override // android.database.ContentObserver
        public final void onChange(boolean z) {
            UnlockedScreenOffAnimationController unlockedScreenOffAnimationController = UnlockedScreenOffAnimationController.this;
            Objects.requireNonNull(unlockedScreenOffAnimationController);
            unlockedScreenOffAnimationController.animatorDurationScale = unlockedScreenOffAnimationController.globalSettings.getFloat();
        }
    };

    /* compiled from: UnlockedScreenOffAnimationController.kt */
    /* loaded from: classes.dex */
    public interface Callback {
        void onUnlockedScreenOffProgressUpdate(float f, float f2);
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final void animateInKeyguard(final View view, final KeyguardVisibilityHelper$$ExternalSyntheticLambda0 keyguardVisibilityHelper$$ExternalSyntheticLambda0) {
        this.shouldAnimateInKeyguard = false;
        view.setAlpha(0.0f);
        view.setVisibility(0);
        float y = view.getY();
        view.setY(y - (view.getHeight() * 0.1f));
        AnimatableProperty.AnonymousClass7 r1 = AnimatableProperty.Y;
        ValueAnimator valueAnimator = (ValueAnimator) view.getTag(r1.getAnimatorTag());
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        AnimationProperties animationProperties = new AnimationProperties();
        long j = 500;
        animationProperties.duration = j;
        PropertyAnimator.setProperty(view, r1, y, animationProperties, true);
        view.animate().setDuration(j).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).alpha(1.0f).setListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController$animateInKeyguard$1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationCancel(Animator animator) {
                UnlockedScreenOffAnimationController.this.interactionJankMonitor.cancel(41);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                UnlockedScreenOffAnimationController unlockedScreenOffAnimationController = UnlockedScreenOffAnimationController.this;
                unlockedScreenOffAnimationController.aodUiAnimationPlaying = false;
                KeyguardViewMediator keyguardViewMediator = unlockedScreenOffAnimationController.keyguardViewMediatorLazy.get();
                Objects.requireNonNull(keyguardViewMediator);
                if (keyguardViewMediator.mPendingLock && !keyguardViewMediator.mScreenOffAnimationController.isKeyguardShowDelayed()) {
                    keyguardViewMediator.doKeyguardLocked(null);
                    keyguardViewMediator.mPendingLock = false;
                }
                StatusBar statusBar = UnlockedScreenOffAnimationController.this.statusBar;
                if (statusBar == null) {
                    statusBar = null;
                }
                statusBar.updateIsKeyguard();
                keyguardVisibilityHelper$$ExternalSyntheticLambda0.run();
                UnlockedScreenOffAnimationController.this.decidedToAnimateGoingToSleep = null;
                view.animate().setListener(null);
                UnlockedScreenOffAnimationController.this.interactionJankMonitor.end(41);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationStart(Animator animator) {
                UnlockedScreenOffAnimationController unlockedScreenOffAnimationController = UnlockedScreenOffAnimationController.this;
                InteractionJankMonitor interactionJankMonitor = unlockedScreenOffAnimationController.interactionJankMonitor;
                StatusBar statusBar = unlockedScreenOffAnimationController.statusBar;
                if (statusBar == null) {
                    statusBar = null;
                }
                Objects.requireNonNull(statusBar);
                interactionJankMonitor.begin(statusBar.mNotificationShadeWindowView, 41);
            }
        }).start();
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean isKeyguardHideDelayed() {
        return false;
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final void onAlwaysOnChanged(boolean z) {
    }

    @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
    public final void onFinishedWakingUp() {
        this.aodUiAnimationPlaying = false;
        if (this.dozeParameters.get().canControlUnlockedScreenOff()) {
            StatusBar statusBar = this.statusBar;
            if (statusBar == null) {
                statusBar = null;
            }
            statusBar.updateIsKeyguard(true);
        }
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final void onScrimOpaqueChanged(boolean z) {
    }

    @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
    public final void onStartedWakingUp() {
        this.decidedToAnimateGoingToSleep = null;
        this.shouldAnimateInKeyguard = false;
        this.lightRevealAnimator.cancel();
        this.handler.removeCallbacksAndMessages(null);
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean shouldAnimateClockChange() {
        return true;
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean shouldAnimateDozingChange() {
        return true;
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final void initialize(StatusBar statusBar, LightRevealScrim lightRevealScrim) {
        this.lightRevealScrim = lightRevealScrim;
        this.statusBar = statusBar;
        this.animatorDurationScale = this.globalSettings.getFloat();
        this.globalSettings.registerContentObserver(Settings.Global.getUriFor("animator_duration_scale"), false, this.animatorDurationScaleObserver);
        WakefulnessLifecycle wakefulnessLifecycle = this.wakefulnessLifecycle;
        Objects.requireNonNull(wakefulnessLifecycle);
        wakefulnessLifecycle.mObservers.add(this);
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean isAnimationPlaying() {
        if (this.lightRevealAnimationPlaying || this.aodUiAnimationPlaying) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean shouldDelayDisplayDozeTransition() {
        DozeParameters dozeParameters = this.dozeParameters.get();
        Objects.requireNonNull(dozeParameters);
        if (!dozeParameters.canControlUnlockedScreenOff() || !dozeParameters.mUnlockedScreenOffAnimationController.shouldPlayUnlockedScreenOffAnimation()) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x003b, code lost:
        if (r0.mNotificationPanelViewController.isFullyCollapsed() == false) goto L_0x003d;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean shouldPlayUnlockedScreenOffAnimation() {
        /*
            r3 = this;
            java.lang.Boolean r0 = r3.decidedToAnimateGoingToSleep
            java.lang.Boolean r1 = java.lang.Boolean.FALSE
            boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual(r0, r1)
            r1 = 0
            if (r0 == 0) goto L_0x000c
            return r1
        L_0x000c:
            android.content.Context r0 = r3.context
            android.content.ContentResolver r0 = r0.getContentResolver()
            java.lang.String r2 = "animator_duration_scale"
            java.lang.String r0 = android.provider.Settings.Global.getString(r0, r2)
            java.lang.String r2 = "0"
            boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual(r0, r2)
            if (r0 == 0) goto L_0x0021
            return r1
        L_0x0021:
            com.android.systemui.statusbar.StatusBarStateControllerImpl r0 = r3.statusBarStateControllerImpl
            java.util.Objects.requireNonNull(r0)
            int r0 = r0.mState
            if (r0 == 0) goto L_0x002b
            return r1
        L_0x002b:
            com.android.systemui.statusbar.phone.StatusBar r0 = r3.statusBar
            if (r0 == 0) goto L_0x003d
            if (r0 != 0) goto L_0x0032
            r0 = 0
        L_0x0032:
            java.util.Objects.requireNonNull(r0)
            com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = r0.mNotificationPanelViewController
            boolean r0 = r0.isFullyCollapsed()
            if (r0 != 0) goto L_0x0044
        L_0x003d:
            boolean r0 = r3.isAnimationPlaying()
            if (r0 != 0) goto L_0x0044
            return r1
        L_0x0044:
            com.android.systemui.statusbar.policy.KeyguardStateController r0 = r3.keyguardStateController
            boolean r0 = r0.isKeyguardScreenRotationAllowed()
            if (r0 != 0) goto L_0x0059
            android.content.Context r3 = r3.context
            android.view.Display r3 = r3.getDisplay()
            int r3 = r3.getRotation()
            if (r3 == 0) goto L_0x0059
            return r1
        L_0x0059:
            r3 = 1
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController.shouldPlayUnlockedScreenOffAnimation():boolean");
    }

    /* JADX WARN: Type inference failed for: r1v6, types: [com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController$animatorDurationScaleObserver$1] */
    public UnlockedScreenOffAnimationController(Context context, WakefulnessLifecycle wakefulnessLifecycle, StatusBarStateControllerImpl statusBarStateControllerImpl, Lazy<KeyguardViewMediator> lazy, KeyguardStateController keyguardStateController, Lazy<DozeParameters> lazy2, GlobalSettings globalSettings, InteractionJankMonitor interactionJankMonitor, PowerManager powerManager, Handler handler) {
        this.context = context;
        this.wakefulnessLifecycle = wakefulnessLifecycle;
        this.statusBarStateControllerImpl = statusBarStateControllerImpl;
        this.keyguardViewMediatorLazy = lazy;
        this.keyguardStateController = keyguardStateController;
        this.dozeParameters = lazy2;
        this.globalSettings = globalSettings;
        this.interactionJankMonitor = interactionJankMonitor;
        this.powerManager = powerManager;
        this.handler = handler;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
        ofFloat.setDuration(750L);
        ofFloat.setInterpolator(Interpolators.LINEAR);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController$lightRevealAnimator$1$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                boolean z;
                LightRevealScrim lightRevealScrim = UnlockedScreenOffAnimationController.this.lightRevealScrim;
                LightRevealScrim lightRevealScrim2 = null;
                if (lightRevealScrim == null) {
                    lightRevealScrim = null;
                }
                Object animatedValue = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                lightRevealScrim.setRevealAmount(((Float) animatedValue).floatValue());
                UnlockedScreenOffAnimationController unlockedScreenOffAnimationController = UnlockedScreenOffAnimationController.this;
                float animatedFraction = 1.0f - valueAnimator.getAnimatedFraction();
                Object animatedValue2 = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue2, "null cannot be cast to non-null type kotlin.Float");
                float floatValue = 1.0f - ((Float) animatedValue2).floatValue();
                Objects.requireNonNull(unlockedScreenOffAnimationController);
                Iterator<UnlockedScreenOffAnimationController.Callback> it = unlockedScreenOffAnimationController.callbacks.iterator();
                while (it.hasNext()) {
                    it.next().onUnlockedScreenOffProgressUpdate(animatedFraction, floatValue);
                }
                LightRevealScrim lightRevealScrim3 = UnlockedScreenOffAnimationController.this.lightRevealScrim;
                if (lightRevealScrim3 != null) {
                    lightRevealScrim2 = lightRevealScrim3;
                }
                Objects.requireNonNull(lightRevealScrim2);
                if (lightRevealScrim2.interpolatedRevealAmount < 0.1f) {
                    z = true;
                } else {
                    z = false;
                }
                if (z && UnlockedScreenOffAnimationController.this.interactionJankMonitor.isInstrumenting(40)) {
                    UnlockedScreenOffAnimationController.this.interactionJankMonitor.end(40);
                }
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController$lightRevealAnimator$1$2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationCancel(Animator animator) {
                LightRevealScrim lightRevealScrim = UnlockedScreenOffAnimationController.this.lightRevealScrim;
                if (lightRevealScrim == null) {
                    lightRevealScrim = null;
                }
                lightRevealScrim.setRevealAmount(1.0f);
                UnlockedScreenOffAnimationController unlockedScreenOffAnimationController = UnlockedScreenOffAnimationController.this;
                unlockedScreenOffAnimationController.lightRevealAnimationPlaying = false;
                Iterator<UnlockedScreenOffAnimationController.Callback> it = unlockedScreenOffAnimationController.callbacks.iterator();
                while (it.hasNext()) {
                    it.next().onUnlockedScreenOffProgressUpdate(0.0f, 0.0f);
                }
                UnlockedScreenOffAnimationController.this.interactionJankMonitor.cancel(40);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                UnlockedScreenOffAnimationController unlockedScreenOffAnimationController = UnlockedScreenOffAnimationController.this;
                unlockedScreenOffAnimationController.lightRevealAnimationPlaying = false;
                unlockedScreenOffAnimationController.interactionJankMonitor.end(40);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationStart(Animator animator) {
                UnlockedScreenOffAnimationController unlockedScreenOffAnimationController = UnlockedScreenOffAnimationController.this;
                InteractionJankMonitor interactionJankMonitor2 = unlockedScreenOffAnimationController.interactionJankMonitor;
                StatusBar statusBar = unlockedScreenOffAnimationController.statusBar;
                if (statusBar == null) {
                    statusBar = null;
                }
                Objects.requireNonNull(statusBar);
                interactionJankMonitor2.begin(statusBar.mNotificationShadeWindowView, 40);
            }
        });
        this.lightRevealAnimator = ofFloat;
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean isKeyguardShowDelayed() {
        return isAnimationPlaying();
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean overrideNotificationsDozeAmount() {
        if (!shouldPlayUnlockedScreenOffAnimation() || !isAnimationPlaying()) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean shouldAnimateAodIcons() {
        return shouldPlayUnlockedScreenOffAnimation();
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean shouldDelayKeyguardShow() {
        return shouldPlayUnlockedScreenOffAnimation();
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean shouldPlayAnimation() {
        return shouldPlayUnlockedScreenOffAnimation();
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean shouldShowAodIconsWhenShade() {
        return isAnimationPlaying();
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean startAnimation() {
        if (shouldPlayUnlockedScreenOffAnimation()) {
            this.decidedToAnimateGoingToSleep = Boolean.TRUE;
            this.shouldAnimateInKeyguard = true;
            this.lightRevealAnimationPlaying = true;
            this.lightRevealAnimator.start();
            this.handler.postDelayed(new Runnable() { // from class: com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController$startAnimation$1
                @Override // java.lang.Runnable
                public final void run() {
                    if (!UnlockedScreenOffAnimationController.this.powerManager.isInteractive()) {
                        UnlockedScreenOffAnimationController unlockedScreenOffAnimationController = UnlockedScreenOffAnimationController.this;
                        unlockedScreenOffAnimationController.aodUiAnimationPlaying = true;
                        StatusBar statusBar = unlockedScreenOffAnimationController.statusBar;
                        if (statusBar == null) {
                            statusBar = null;
                        }
                        Objects.requireNonNull(statusBar);
                        statusBar.mNotificationPanelViewController.showAodUi();
                    }
                }
            }, ((float) 600) * this.animatorDurationScale);
            return true;
        }
        this.decidedToAnimateGoingToSleep = Boolean.FALSE;
        return false;
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean shouldAnimateInKeyguard() {
        return this.shouldAnimateInKeyguard;
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean shouldHideScrimOnWakeUp() {
        return this.lightRevealAnimationPlaying;
    }
}
