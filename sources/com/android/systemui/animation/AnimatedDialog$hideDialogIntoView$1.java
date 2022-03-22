package com.android.systemui.animation;

import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: DialogLaunchAnimator.kt */
/* loaded from: classes.dex */
final class AnimatedDialog$hideDialogIntoView$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ AnimatedDialog this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AnimatedDialog$hideDialogIntoView$1(AnimatedDialog animatedDialog) {
        super(0);
        this.this$0 = animatedDialog;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        AnimatedDialog animatedDialog = this.this$0;
        Objects.requireNonNull(animatedDialog);
        animatedDialog.dialog.getWindow().clearFlags(2);
        return Unit.INSTANCE;
    }
}
