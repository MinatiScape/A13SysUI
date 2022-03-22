package com.google.common.collect;

import com.airbnb.lottie.parser.moshi.JsonReader$$ExternalSyntheticOutline0;
import com.google.common.collect.ImmutableList;
import java.util.NoSuchElementException;
import java.util.Objects;
/* loaded from: classes.dex */
final class SingletonImmutableSet<E> extends ImmutableSet<E> {
    public final transient E element;

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final int size() {
        return 1;
    }

    @Override // com.google.common.collect.ImmutableSet
    public final ImmutableList<E> asList() {
        E e = this.element;
        ImmutableList.Itr itr = ImmutableList.EMPTY_ITR;
        Object[] objArr = {e};
        ObjectArrays.checkElementsNotNull(objArr, 1);
        return ImmutableList.asImmutableList(objArr, 1);
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection
    public final boolean contains(Object obj) {
        return this.element.equals(obj);
    }

    @Override // com.google.common.collect.ImmutableCollection
    public final int copyIntoArray(Object[] objArr) {
        objArr[0] = this.element;
        return 1;
    }

    @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.util.Set
    public final int hashCode() {
        return this.element.hashCode();
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    /* renamed from: iterator */
    public final UnmodifiableIterator<E> mo174iterator() {
        final E e = this.element;
        return (UnmodifiableIterator<E>) new UnmodifiableIterator<Object>() { // from class: com.google.common.collect.Iterators.9
            public boolean done;

            @Override // java.util.Iterator
            public final boolean hasNext() {
                return !this.done;
            }

            @Override // java.util.Iterator
            public final Object next() {
                if (!this.done) {
                    this.done = true;
                    return e;
                }
                throw new NoSuchElementException();
            }
        };
    }

    @Override // java.util.AbstractCollection
    public final String toString() {
        StringBuilder m = JsonReader$$ExternalSyntheticOutline0.m('[');
        m.append(this.element.toString());
        m.append(']');
        return m.toString();
    }

    public SingletonImmutableSet(E e) {
        Objects.requireNonNull(e);
        this.element = e;
    }
}
