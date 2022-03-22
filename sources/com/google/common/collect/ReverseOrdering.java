package com.google.common.collect;

import java.io.Serializable;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ReverseOrdering<T> extends Ordering<T> implements Serializable {
    private static final long serialVersionUID = 0;
    public final Ordering<? super T> forwardOrder;

    @Override // com.google.common.collect.Ordering, java.util.Comparator
    public final int compare(T t, T t2) {
        return this.forwardOrder.compare(t2, t);
    }

    @Override // java.util.Comparator
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ReverseOrdering) {
            return this.forwardOrder.equals(((ReverseOrdering) obj).forwardOrder);
        }
        return false;
    }

    public final int hashCode() {
        return -this.forwardOrder.hashCode();
    }

    public final String toString() {
        return this.forwardOrder + ".reverse()";
    }

    public ReverseOrdering(Ordering<? super T> ordering) {
        Objects.requireNonNull(ordering);
        this.forwardOrder = ordering;
    }

    @Override // com.google.common.collect.Ordering
    public final <S extends T> Ordering<S> reverse() {
        return (Ordering<? super T>) this.forwardOrder;
    }
}
