package com.android.systemui.animation;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import java.util.Objects;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: DialogLaunchAnimator.kt */
/* loaded from: classes.dex */
public final class AnimatedDialog$decorView$2 extends Lambda implements Function0<ViewGroup> {
    public final /* synthetic */ AnimatedDialog this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AnimatedDialog$decorView$2(AnimatedDialog animatedDialog) {
        super(0);
        this.this$0 = animatedDialog;
    }

    @Override // kotlin.jvm.functions.Function0
    public final ViewGroup invoke() {
        AnimatedDialog animatedDialog = this.this$0;
        Objects.requireNonNull(animatedDialog);
        Window window = animatedDialog.dialog.getWindow();
        Intrinsics.checkNotNull(window);
        View decorView = window.getDecorView();
        Objects.requireNonNull(decorView, "null cannot be cast to non-null type android.view.ViewGroup");
        return (ViewGroup) decorView;
    }
}
