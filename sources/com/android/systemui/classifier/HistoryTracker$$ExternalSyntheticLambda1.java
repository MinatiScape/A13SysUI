package com.android.systemui.classifier;

import java.util.function.BinaryOperator;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class HistoryTracker$$ExternalSyntheticLambda1 implements BinaryOperator {
    public static final /* synthetic */ HistoryTracker$$ExternalSyntheticLambda1 INSTANCE = new HistoryTracker$$ExternalSyntheticLambda1();

    @Override // java.util.function.BiFunction
    public final Object apply(Object obj, Object obj2) {
        Double d = (Double) obj;
        Double d2 = (Double) obj2;
        int i = HistoryTracker.$r8$clinit;
        return Double.valueOf((d2.doubleValue() * d.doubleValue()) / (((1.0d - d2.doubleValue()) * (1.0d - d.doubleValue())) + (d2.doubleValue() * d.doubleValue())));
    }
}
