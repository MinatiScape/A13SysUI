package com.google.common.collect;

import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class TransformedIterator<F, T> implements Iterator<T> {
    public final Iterator<? extends F> backingIterator;

    public abstract T transform(F f);

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.backingIterator.hasNext();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Iterator
    public final T next() {
        return (T) transform(this.backingIterator.next());
    }

    @Override // java.util.Iterator
    public final void remove() {
        this.backingIterator.remove();
    }

    public TransformedIterator(Iterator<? extends F> it) {
        Objects.requireNonNull(it);
        this.backingIterator = it;
    }
}
