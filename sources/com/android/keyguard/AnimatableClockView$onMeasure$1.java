package com.android.keyguard;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: AnimatableClockView.kt */
/* loaded from: classes.dex */
public final class AnimatableClockView$onMeasure$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ AnimatableClockView this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AnimatableClockView$onMeasure$1(AnimatableClockView animatableClockView) {
        super(0);
        this.this$0 = animatableClockView;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        this.this$0.invalidate();
        return Unit.INSTANCE;
    }
}
