package com.google.android.systemui.elmyra.feedback;

import com.android.systemui.Dependency;
import com.android.systemui.navigationbar.NavigationBarController;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
/* loaded from: classes.dex */
public final class NavUndimEffect implements FeedbackEffect {
    public final NavigationBarController mNavBarController = (NavigationBarController) Dependency.get(NavigationBarController.class);

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public final void onProgress(float f, int i) {
        this.mNavBarController.touchAutoDim(0);
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public final void onRelease() {
        this.mNavBarController.touchAutoDim(0);
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public final void onResolve(GestureSensor.DetectionProperties detectionProperties) {
        this.mNavBarController.touchAutoDim(0);
    }
}
