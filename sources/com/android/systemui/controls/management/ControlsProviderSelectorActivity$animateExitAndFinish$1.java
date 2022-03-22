package com.android.systemui.controls.management;
/* compiled from: ControlsProviderSelectorActivity.kt */
/* loaded from: classes.dex */
public final class ControlsProviderSelectorActivity$animateExitAndFinish$1 implements Runnable {
    public final /* synthetic */ ControlsProviderSelectorActivity this$0;

    public ControlsProviderSelectorActivity$animateExitAndFinish$1(ControlsProviderSelectorActivity controlsProviderSelectorActivity) {
        this.this$0 = controlsProviderSelectorActivity;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.this$0.finish();
    }
}
