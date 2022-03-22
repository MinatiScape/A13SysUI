package com.android.systemui.dreams.complication;

import java.util.function.ToIntFunction;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ComplicationUtils$$ExternalSyntheticLambda1 implements ToIntFunction {
    public static final /* synthetic */ ComplicationUtils$$ExternalSyntheticLambda1 INSTANCE = new ComplicationUtils$$ExternalSyntheticLambda1();

    @Override // java.util.function.ToIntFunction
    public final int applyAsInt(Object obj) {
        int intValue = ((Integer) obj).intValue();
        int i = 1;
        if (intValue != 1) {
            i = 2;
            if (intValue != 2) {
                if (intValue == 3) {
                    return 4;
                }
                if (intValue != 4) {
                    return intValue != 5 ? 0 : 16;
                }
                return 8;
            }
        }
        return i;
    }
}
