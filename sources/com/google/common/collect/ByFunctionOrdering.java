package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.util.concurrent.FuturesGetChecked;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
final class ByFunctionOrdering<F, T> extends Ordering<F> implements Serializable {
    private static final long serialVersionUID = 0;
    public final Function<F, ? extends T> function;
    public final Ordering<T> ordering;

    @Override // java.util.Comparator
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ByFunctionOrdering)) {
            return false;
        }
        ByFunctionOrdering byFunctionOrdering = (ByFunctionOrdering) obj;
        return this.function.equals(byFunctionOrdering.function) && this.ordering.equals(byFunctionOrdering.ordering);
    }

    public final int hashCode() {
        return Arrays.hashCode(new Object[]{this.function, this.ordering});
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.Ordering, java.util.Comparator
    public final int compare(F f, F f2) {
        return this.ordering.compare(this.function.apply(f), this.function.apply(f2));
    }

    public final String toString() {
        return this.ordering + ".onResultOf(" + this.function + ")";
    }

    public ByFunctionOrdering(FuturesGetChecked.AnonymousClass1 r1, Ordering ordering) {
        this.function = r1;
        Objects.requireNonNull(ordering);
        this.ordering = ordering;
    }
}
