package com.google.common.collect;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
/* loaded from: classes.dex */
public final class ObjectArrays {
    @CanIgnoreReturnValue
    public static Object[] checkElementsNotNull(Object[] objArr, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            if (objArr[i2] == null) {
                throw new NullPointerException(VendorAtomValue$$ExternalSyntheticOutline0.m("at index ", i2));
            }
        }
        return objArr;
    }
}
