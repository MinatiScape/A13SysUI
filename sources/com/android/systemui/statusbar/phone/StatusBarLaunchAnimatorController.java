package com.android.systemui.statusbar.phone;

import android.graphics.PorterDuffXfermode;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.LaunchAnimator;
import java.util.Objects;
/* compiled from: StatusBarLaunchAnimatorController.kt */
/* loaded from: classes.dex */
public final class StatusBarLaunchAnimatorController implements ActivityLaunchAnimator.Controller {
    public final ActivityLaunchAnimator.Controller delegate;
    public final boolean isLaunchForActivity;
    public final StatusBar statusBar;

    @Override // com.android.systemui.animation.LaunchAnimator.Controller
    public final LaunchAnimator.State createAnimatorState() {
        return this.delegate.createAnimatorState();
    }

    @Override // com.android.systemui.animation.LaunchAnimator.Controller
    public final ViewGroup getLaunchContainer() {
        return this.delegate.getLaunchContainer();
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public final boolean isDialogLaunch() {
        return this.delegate.isDialogLaunch();
    }

    @Override // com.android.systemui.animation.LaunchAnimator.Controller
    public final void setLaunchContainer(ViewGroup viewGroup) {
        this.delegate.setLaunchContainer(viewGroup);
    }

    @Override // com.android.systemui.animation.LaunchAnimator.Controller
    public final View getOpeningWindowSyncView() {
        StatusBar statusBar = this.statusBar;
        Objects.requireNonNull(statusBar);
        return statusBar.mNotificationShadeWindowView;
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public final void onIntentStarted(boolean z) {
        this.delegate.onIntentStarted(z);
        if (!z) {
            this.statusBar.collapsePanelOnMainThread();
        }
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public final void onLaunchAnimationCancelled() {
        this.delegate.onLaunchAnimationCancelled();
        StatusBar statusBar = this.statusBar;
        boolean z = this.isLaunchForActivity;
        Objects.requireNonNull(statusBar);
        if (!statusBar.mPresenter.isPresenterFullyCollapsed() || statusBar.mPresenter.isCollapsing() || !z) {
            statusBar.mShadeController.collapsePanel(true);
        } else {
            statusBar.onClosingFinished();
        }
    }

    @Override // com.android.systemui.animation.LaunchAnimator.Controller
    public final void onLaunchAnimationEnd(boolean z) {
        this.delegate.onLaunchAnimationEnd(z);
        StatusBar statusBar = this.statusBar;
        Objects.requireNonNull(statusBar);
        NotificationPanelViewController notificationPanelViewController = statusBar.mNotificationPanelViewController;
        Objects.requireNonNull(notificationPanelViewController);
        notificationPanelViewController.mIsLaunchAnimationRunning = false;
        StatusBar statusBar2 = this.statusBar;
        Objects.requireNonNull(statusBar2);
        if (!statusBar2.mPresenter.isCollapsing()) {
            statusBar2.onClosingFinished();
        }
        if (z) {
            statusBar2.instantCollapseNotificationPanel();
        }
    }

    @Override // com.android.systemui.animation.LaunchAnimator.Controller
    public final void onLaunchAnimationProgress(LaunchAnimator.State state, float f, float f2) {
        boolean z;
        this.delegate.onLaunchAnimationProgress(state, f, f2);
        StatusBar statusBar = this.statusBar;
        Objects.requireNonNull(statusBar);
        NotificationPanelViewController notificationPanelViewController = statusBar.mNotificationPanelViewController;
        Objects.requireNonNull(notificationPanelViewController);
        LaunchAnimator.Timings timings = ActivityLaunchAnimator.TIMINGS;
        long j = NotificationPanelViewController.ANIMATION_DELAY_ICON_FADE_IN;
        PorterDuffXfermode porterDuffXfermode = LaunchAnimator.SRC_MODE;
        if (LaunchAnimator.Companion.getProgress(timings, f2, j, 100L) == 0.0f) {
            z = true;
        } else {
            z = false;
        }
        if (z != notificationPanelViewController.mHideIconsDuringLaunchAnimation) {
            notificationPanelViewController.mHideIconsDuringLaunchAnimation = z;
            if (!z) {
                notificationPanelViewController.mCommandQueue.recomputeDisableFlags(notificationPanelViewController.mDisplayId, true);
            }
        }
    }

    @Override // com.android.systemui.animation.LaunchAnimator.Controller
    public final void onLaunchAnimationStart(boolean z) {
        this.delegate.onLaunchAnimationStart(z);
        StatusBar statusBar = this.statusBar;
        Objects.requireNonNull(statusBar);
        NotificationPanelViewController notificationPanelViewController = statusBar.mNotificationPanelViewController;
        Objects.requireNonNull(notificationPanelViewController);
        notificationPanelViewController.mIsLaunchAnimationRunning = true;
        if (!z) {
            StatusBar statusBar2 = this.statusBar;
            LaunchAnimator.Timings timings = ActivityLaunchAnimator.TIMINGS;
            Objects.requireNonNull(statusBar2);
            NotificationPanelViewController notificationPanelViewController2 = statusBar2.mNotificationPanelViewController;
            Objects.requireNonNull(notificationPanelViewController2);
            notificationPanelViewController2.mFixedDuration = (int) 500;
            notificationPanelViewController2.collapse(false, 1.0f);
            notificationPanelViewController2.mFixedDuration = -1;
        }
    }

    public StatusBarLaunchAnimatorController(ActivityLaunchAnimator.Controller controller, StatusBar statusBar, boolean z) {
        this.delegate = controller;
        this.statusBar = statusBar;
        this.isLaunchForActivity = z;
    }
}
