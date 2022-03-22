package com.android.systemui.controls.ui;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: ControlViewHolder.kt */
/* loaded from: classes.dex */
public final class ControlViewHolder$setErrorStatus$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ String $text;
    public final /* synthetic */ ControlViewHolder this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ControlViewHolder$setErrorStatus$1(ControlViewHolder controlViewHolder, String str) {
        super(0);
        this.this$0 = controlViewHolder;
        this.$text = str;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        this.this$0.setStatusText(this.$text, true);
        return Unit.INSTANCE;
    }
}
