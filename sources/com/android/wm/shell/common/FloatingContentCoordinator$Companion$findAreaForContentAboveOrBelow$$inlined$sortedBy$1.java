package com.android.wm.shell.common;

import android.graphics.Rect;
import androidx.fragment.R$styleable;
import java.util.Comparator;
/* compiled from: Comparisons.kt */
/* loaded from: classes.dex */
public final class FloatingContentCoordinator$Companion$findAreaForContentAboveOrBelow$$inlined$sortedBy$1<T> implements Comparator {
    public final /* synthetic */ boolean $findAbove$inlined;

    public FloatingContentCoordinator$Companion$findAreaForContentAboveOrBelow$$inlined$sortedBy$1(boolean z) {
        this.$findAbove$inlined = z;
    }

    @Override // java.util.Comparator
    public final int compare(T t, T t2) {
        int i;
        boolean z = this.$findAbove$inlined;
        int i2 = ((Rect) t).top;
        if (z) {
            i2 = -i2;
        }
        Integer valueOf = Integer.valueOf(i2);
        Rect rect = (Rect) t2;
        if (this.$findAbove$inlined) {
            i = -rect.top;
        } else {
            i = rect.top;
        }
        return R$styleable.compareValues(valueOf, Integer.valueOf(i));
    }
}
