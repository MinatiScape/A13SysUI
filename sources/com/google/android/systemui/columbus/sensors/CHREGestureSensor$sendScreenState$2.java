package com.google.android.systemui.columbus.sensors;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: CHREGestureSensor.kt */
/* loaded from: classes.dex */
public final class CHREGestureSensor$sendScreenState$2 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ CHREGestureSensor this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CHREGestureSensor$sendScreenState$2(CHREGestureSensor cHREGestureSensor) {
        super(0);
        this.this$0 = cHREGestureSensor;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        this.this$0.screenStateUpdated = false;
        return Unit.INSTANCE;
    }
}
