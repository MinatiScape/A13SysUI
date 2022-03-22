package com.android.wm.shell.bubbles;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: DismissView.kt */
/* loaded from: classes.dex */
public final class DismissView$hide$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ DismissView this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DismissView$hide$1(DismissView dismissView) {
        super(0);
        this.this$0 = dismissView;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        this.this$0.setVisibility(4);
        return Unit.INSTANCE;
    }
}
