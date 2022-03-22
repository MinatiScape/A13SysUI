package com.android.systemui.controls.management;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
/* compiled from: ControlAdapter.kt */
/* loaded from: classes.dex */
public /* synthetic */ class ControlHolder$accessibilityDelegate$1 extends FunctionReferenceImpl implements Function1<Boolean, CharSequence> {
    public ControlHolder$accessibilityDelegate$1(Object obj) {
        super(1, obj, ControlHolder.class, "stateDescription", "stateDescription(Z)Ljava/lang/CharSequence;", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    public final CharSequence invoke(Boolean bool) {
        boolean booleanValue = bool.booleanValue();
        int i = ControlHolder.$r8$clinit;
        return ((ControlHolder) this.receiver).stateDescription(booleanValue);
    }
}
