package com.google.common.base;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import java.io.Serializable;
/* loaded from: classes.dex */
class Suppliers$MemoizingSupplier<T> implements Supplier<T>, Serializable {
    private static final long serialVersionUID = 0;
    public final Supplier<T> delegate;

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Suppliers.memoize(");
        m.append(this.delegate);
        m.append(")");
        return m.toString();
    }
}
