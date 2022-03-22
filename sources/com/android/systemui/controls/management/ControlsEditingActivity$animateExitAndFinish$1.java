package com.android.systemui.controls.management;
/* compiled from: ControlsEditingActivity.kt */
/* loaded from: classes.dex */
public final class ControlsEditingActivity$animateExitAndFinish$1 implements Runnable {
    public final /* synthetic */ ControlsEditingActivity this$0;

    public ControlsEditingActivity$animateExitAndFinish$1(ControlsEditingActivity controlsEditingActivity) {
        this.this$0 = controlsEditingActivity;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.this$0.finish();
    }
}
