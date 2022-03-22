package com.android.systemui.animation;

import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.LaunchAnimator;
/* compiled from: DelegateLaunchAnimatorController.kt */
/* loaded from: classes.dex */
public class DelegateLaunchAnimatorController implements ActivityLaunchAnimator.Controller {
    public final ActivityLaunchAnimator.Controller delegate;

    @Override // com.android.systemui.animation.LaunchAnimator.Controller
    public final LaunchAnimator.State createAnimatorState() {
        return this.delegate.createAnimatorState();
    }

    @Override // com.android.systemui.animation.LaunchAnimator.Controller
    public final ViewGroup getLaunchContainer() {
        return this.delegate.getLaunchContainer();
    }

    @Override // com.android.systemui.animation.LaunchAnimator.Controller
    public final View getOpeningWindowSyncView() {
        return this.delegate.getOpeningWindowSyncView();
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public final boolean isDialogLaunch() {
        return this.delegate.isDialogLaunch();
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void onIntentStarted(boolean z) {
        this.delegate.onIntentStarted(z);
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void onLaunchAnimationCancelled() {
        this.delegate.onLaunchAnimationCancelled();
    }

    @Override // com.android.systemui.animation.LaunchAnimator.Controller
    public final void onLaunchAnimationProgress(LaunchAnimator.State state, float f, float f2) {
        this.delegate.onLaunchAnimationProgress(state, f, f2);
    }

    @Override // com.android.systemui.animation.LaunchAnimator.Controller
    public void onLaunchAnimationStart(boolean z) {
        this.delegate.onLaunchAnimationStart(z);
    }

    @Override // com.android.systemui.animation.LaunchAnimator.Controller
    public final void setLaunchContainer(ViewGroup viewGroup) {
        this.delegate.setLaunchContainer(viewGroup);
    }

    public DelegateLaunchAnimatorController(ActivityLaunchAnimator.Controller controller) {
        this.delegate = controller;
    }
}
