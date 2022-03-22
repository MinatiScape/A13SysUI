package com.google.android.systemui.columbus.sensors;

import com.google.android.systemui.columbus.ColumbusEvent;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: CHREGestureSensor.kt */
/* loaded from: classes.dex */
public final class CHREGestureSensor$startRecognizer$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ CHREGestureSensor this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CHREGestureSensor$startRecognizer$1(CHREGestureSensor cHREGestureSensor) {
        super(0);
        this.this$0 = cHREGestureSensor;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        this.this$0.uiEventLogger.log(ColumbusEvent.COLUMBUS_MODE_LOW_POWER_ACTIVE);
        return Unit.INSTANCE;
    }
}
