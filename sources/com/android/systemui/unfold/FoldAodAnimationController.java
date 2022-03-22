package com.android.systemui.unfold;

import android.os.Handler;
import android.view.View;
import com.android.keyguard.KeyguardVisibilityHelper$$ExternalSyntheticLambda0;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.LightRevealScrim;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.ScreenOffAnimation;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.util.settings.GlobalSettings;
import java.util.ArrayList;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: FoldAodAnimationController.kt */
/* loaded from: classes.dex */
public final class FoldAodAnimationController implements CallbackController<FoldAodAnimationStatus>, ScreenOffAnimation, WakefulnessLifecycle.Observer {
    public boolean alwaysOnEnabled;
    public final GlobalSettings globalSettings;
    public final Handler handler;
    public boolean isAnimationPlaying;
    public boolean isScrimOpaque;
    public Runnable pendingScrimReadyCallback;
    public boolean shouldPlayAnimation;
    public StatusBar statusBar;
    public final WakefulnessLifecycle wakefulnessLifecycle;
    public final ArrayList<FoldAodAnimationStatus> statusListeners = new ArrayList<>();
    public final FoldAodAnimationController$startAnimationRunnable$1 startAnimationRunnable = new Runnable() { // from class: com.android.systemui.unfold.FoldAodAnimationController$startAnimationRunnable$1
        /* JADX WARN: Type inference failed for: r1v0, types: [com.android.systemui.unfold.FoldAodAnimationController$startAnimationRunnable$1$1] */
        /* JADX WARN: Unknown variable types count: 1 */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void run() {
            /*
                r4 = this;
                com.android.systemui.unfold.FoldAodAnimationController r0 = com.android.systemui.unfold.FoldAodAnimationController.this
                com.android.systemui.statusbar.phone.StatusBar r0 = r0.statusBar
                if (r0 != 0) goto L_0x0007
                r0 = 0
            L_0x0007:
                java.util.Objects.requireNonNull(r0)
                com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = r0.mNotificationPanelViewController
                com.android.systemui.unfold.FoldAodAnimationController$startAnimationRunnable$1$1 r1 = new com.android.systemui.unfold.FoldAodAnimationController$startAnimationRunnable$1$1
                com.android.systemui.unfold.FoldAodAnimationController r4 = com.android.systemui.unfold.FoldAodAnimationController.this
                r1.<init>()
                java.util.Objects.requireNonNull(r0)
                com.android.systemui.statusbar.phone.NotificationPanelView r4 = r0.mView
                android.view.ViewPropertyAnimator r4 = r4.animate()
                r2 = 0
                android.view.ViewPropertyAnimator r4 = r4.translationX(r2)
                r2 = 1065353216(0x3f800000, float:1.0)
                android.view.ViewPropertyAnimator r4 = r4.alpha(r2)
                r2 = 600(0x258, double:2.964E-321)
                android.view.ViewPropertyAnimator r4 = r4.setDuration(r2)
                android.view.animation.PathInterpolator r2 = com.android.systemui.animation.Interpolators.EMPHASIZED_DECELERATE
                android.view.ViewPropertyAnimator r4 = r4.setInterpolator(r2)
                com.android.systemui.statusbar.phone.NotificationPanelViewController$15 r2 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$15
                r2.<init>()
                android.view.ViewPropertyAnimator r4 = r4.setListener(r2)
                r4.start()
                com.android.keyguard.KeyguardStatusViewController r4 = r0.mKeyguardStatusViewController
                java.util.Objects.requireNonNull(r4)
                com.android.keyguard.KeyguardClockSwitchController r4 = r4.mKeyguardClockSwitchController
                java.util.Objects.requireNonNull(r4)
                com.android.keyguard.AnimatableClockController r0 = r4.mClockViewController
                if (r0 == 0) goto L_0x0055
                r0.animateFoldAppear()
                com.android.keyguard.AnimatableClockController r4 = r4.mLargeClockViewController
                r4.animateFoldAppear()
            L_0x0055:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.unfold.FoldAodAnimationController$startAnimationRunnable$1.run():void");
        }
    };

    /* compiled from: FoldAodAnimationController.kt */
    /* loaded from: classes.dex */
    public interface FoldAodAnimationStatus {
        void onFoldToAodAnimationChanged();
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean isKeyguardShowDelayed() {
        return false;
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean overrideNotificationsDozeAmount() {
        return false;
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean shouldAnimateInKeyguard() {
        return false;
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean shouldDelayKeyguardShow() {
        return false;
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean shouldHideScrimOnWakeUp() {
        return false;
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(FoldAodAnimationStatus foldAodAnimationStatus) {
        this.statusListeners.add(foldAodAnimationStatus);
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final void initialize(StatusBar statusBar, LightRevealScrim lightRevealScrim) {
        this.statusBar = statusBar;
        WakefulnessLifecycle wakefulnessLifecycle = this.wakefulnessLifecycle;
        Objects.requireNonNull(wakefulnessLifecycle);
        wakefulnessLifecycle.mObservers.add(this);
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final void onScrimOpaqueChanged(boolean z) {
        this.isScrimOpaque = z;
        if (z) {
            Runnable runnable = this.pendingScrimReadyCallback;
            if (runnable != null) {
                runnable.run();
            }
            this.pendingScrimReadyCallback = null;
        }
    }

    @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
    public final void onStartedWakingUp() {
        if (this.isAnimationPlaying) {
            this.handler.removeCallbacks(this.startAnimationRunnable);
            StatusBar statusBar = this.statusBar;
            if (statusBar == null) {
                statusBar = null;
            }
            Objects.requireNonNull(statusBar);
            NotificationPanelViewController notificationPanelViewController = statusBar.mNotificationPanelViewController;
            Objects.requireNonNull(notificationPanelViewController);
            notificationPanelViewController.mView.animate().cancel();
            notificationPanelViewController.mView.setAlpha(1.0f);
            notificationPanelViewController.mView.setTranslationX(0.0f);
        }
        setAnimationState(false);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(FoldAodAnimationStatus foldAodAnimationStatus) {
        this.statusListeners.remove(foldAodAnimationStatus);
    }

    public final void setAnimationState(boolean z) {
        this.shouldPlayAnimation = z;
        this.isAnimationPlaying = z;
        for (FoldAodAnimationStatus foldAodAnimationStatus : this.statusListeners) {
            foldAodAnimationStatus.onFoldToAodAnimationChanged();
        }
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean shouldAnimateAodIcons() {
        return !this.shouldPlayAnimation;
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean shouldAnimateClockChange() {
        return !this.isAnimationPlaying;
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean shouldAnimateDozingChange() {
        return !this.shouldPlayAnimation;
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean startAnimation() {
        if (this.alwaysOnEnabled) {
            WakefulnessLifecycle wakefulnessLifecycle = this.wakefulnessLifecycle;
            Objects.requireNonNull(wakefulnessLifecycle);
            if (wakefulnessLifecycle.mLastSleepReason == 13 && !Intrinsics.areEqual(this.globalSettings.getString("animator_duration_scale"), "0")) {
                setAnimationState(true);
                StatusBar statusBar = this.statusBar;
                if (statusBar == null) {
                    statusBar = null;
                }
                Objects.requireNonNull(statusBar);
                NotificationPanelViewController notificationPanelViewController = statusBar.mNotificationPanelViewController;
                Objects.requireNonNull(notificationPanelViewController);
                notificationPanelViewController.showAodUi();
                notificationPanelViewController.mView.setTranslationX(-notificationPanelViewController.mView.getResources().getDimensionPixelSize(2131165364));
                notificationPanelViewController.mView.setAlpha(0.0f);
                return true;
            }
        }
        setAnimationState(false);
        return false;
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.unfold.FoldAodAnimationController$startAnimationRunnable$1] */
    public FoldAodAnimationController(Handler handler, WakefulnessLifecycle wakefulnessLifecycle, GlobalSettings globalSettings) {
        this.handler = handler;
        this.wakefulnessLifecycle = wakefulnessLifecycle;
        this.globalSettings = globalSettings;
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final void onAlwaysOnChanged(boolean z) {
        this.alwaysOnEnabled = z;
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final void animateInKeyguard(View view, KeyguardVisibilityHelper$$ExternalSyntheticLambda0 keyguardVisibilityHelper$$ExternalSyntheticLambda0) {
        keyguardVisibilityHelper$$ExternalSyntheticLambda0.run();
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean isAnimationPlaying() {
        return this.isAnimationPlaying;
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean isKeyguardHideDelayed() {
        return this.isAnimationPlaying;
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean shouldDelayDisplayDozeTransition() {
        return this.shouldPlayAnimation;
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean shouldPlayAnimation() {
        return this.shouldPlayAnimation;
    }

    @Override // com.android.systemui.statusbar.phone.ScreenOffAnimation
    public final boolean shouldShowAodIconsWhenShade() {
        return this.shouldPlayAnimation;
    }
}
