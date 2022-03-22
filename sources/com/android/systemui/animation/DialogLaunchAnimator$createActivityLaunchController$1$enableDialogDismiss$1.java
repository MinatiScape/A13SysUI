package com.android.systemui.animation;
/* compiled from: DialogLaunchAnimator.kt */
/* loaded from: classes.dex */
public final /* synthetic */ class DialogLaunchAnimator$createActivityLaunchController$1$enableDialogDismiss$1 implements Runnable {
    public final /* synthetic */ AnimatedDialog $tmp0;

    public DialogLaunchAnimator$createActivityLaunchController$1$enableDialogDismiss$1(AnimatedDialog animatedDialog) {
        this.$tmp0 = animatedDialog;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.$tmp0.onDialogDismissed();
    }
}
