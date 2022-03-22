package com.google.android.systemui.elmyra.feedback;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import com.android.systemui.statusbar.phone.KeyguardBottomAreaView;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import java.util.Objects;
/* loaded from: classes.dex */
public final class OpaLockscreen implements FeedbackEffect {
    public KeyguardBottomAreaView mKeyguardBottomAreaView;
    public final KeyguardStateController mKeyguardStateController;
    public FeedbackEffect mLockscreenOpaLayout;
    public final StatusBar mStatusBar;

    static {
        new DecelerateInterpolator();
        new AccelerateInterpolator();
    }

    public final void refreshLockscreenOpaLayout() {
        StatusBar statusBar = this.mStatusBar;
        Objects.requireNonNull(statusBar);
        NotificationPanelViewController notificationPanelViewController = statusBar.mNotificationPanelViewController;
        Objects.requireNonNull(notificationPanelViewController);
        if (notificationPanelViewController.mKeyguardBottomArea == null || !this.mKeyguardStateController.isShowing()) {
            this.mKeyguardBottomAreaView = null;
            this.mLockscreenOpaLayout = null;
            return;
        }
        StatusBar statusBar2 = this.mStatusBar;
        Objects.requireNonNull(statusBar2);
        NotificationPanelViewController notificationPanelViewController2 = statusBar2.mNotificationPanelViewController;
        Objects.requireNonNull(notificationPanelViewController2);
        KeyguardBottomAreaView keyguardBottomAreaView = notificationPanelViewController2.mKeyguardBottomArea;
        if (this.mLockscreenOpaLayout == null || !keyguardBottomAreaView.equals(this.mKeyguardBottomAreaView)) {
            this.mKeyguardBottomAreaView = keyguardBottomAreaView;
            FeedbackEffect feedbackEffect = this.mLockscreenOpaLayout;
            if (feedbackEffect != null) {
                feedbackEffect.onRelease();
            }
            this.mLockscreenOpaLayout = (FeedbackEffect) keyguardBottomAreaView.findViewById(2131428284);
        }
    }

    public OpaLockscreen(StatusBar statusBar, KeyguardStateController keyguardStateController) {
        this.mStatusBar = statusBar;
        this.mKeyguardStateController = keyguardStateController;
        refreshLockscreenOpaLayout();
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public final void onProgress(float f, int i) {
        refreshLockscreenOpaLayout();
        FeedbackEffect feedbackEffect = this.mLockscreenOpaLayout;
        if (feedbackEffect != null) {
            feedbackEffect.onProgress(f, i);
        }
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public final void onRelease() {
        refreshLockscreenOpaLayout();
        FeedbackEffect feedbackEffect = this.mLockscreenOpaLayout;
        if (feedbackEffect != null) {
            feedbackEffect.onRelease();
        }
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public final void onResolve(GestureSensor.DetectionProperties detectionProperties) {
        refreshLockscreenOpaLayout();
        FeedbackEffect feedbackEffect = this.mLockscreenOpaLayout;
        if (feedbackEffect != null) {
            feedbackEffect.onResolve(detectionProperties);
        }
    }
}
