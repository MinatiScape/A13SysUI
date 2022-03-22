package com.android.systemui.util.condition;

import java.util.Objects;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class Monitor$$ExternalSyntheticLambda3 implements Function {
    public static final /* synthetic */ Monitor$$ExternalSyntheticLambda3 INSTANCE = new Monitor$$ExternalSyntheticLambda3();

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        Condition condition = (Condition) obj;
        Objects.requireNonNull(condition);
        return Boolean.valueOf(condition.mIsConditionMet);
    }
}
