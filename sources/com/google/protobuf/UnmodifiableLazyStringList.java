package com.google.protobuf;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
/* loaded from: classes.dex */
public final class UnmodifiableLazyStringList extends AbstractList<String> implements LazyStringList, RandomAccess {
    public final LazyStringList list;

    @Override // com.google.protobuf.LazyStringList
    public final LazyStringList getUnmodifiableView() {
        return this;
    }

    @Override // java.util.AbstractList, java.util.List
    public final Object get(int i) {
        return (String) this.list.get(i);
    }

    @Override // com.google.protobuf.LazyStringList
    public final List<?> getUnderlyingElements() {
        return this.list.getUnderlyingElements();
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public final Iterator<String> iterator() {
        return new Iterator<String>(this) { // from class: com.google.protobuf.UnmodifiableLazyStringList.2
            public Iterator<String> iter;

            @Override // java.util.Iterator
            public final boolean hasNext() {
                return this.iter.hasNext();
            }

            @Override // java.util.Iterator
            public final String next() {
                return this.iter.next();
            }

            @Override // java.util.Iterator
            public final void remove() {
                throw new UnsupportedOperationException();
            }

            {
                this.iter = this.list.iterator();
            }
        };
    }

    @Override // java.util.AbstractList, java.util.List
    public final ListIterator<String> listIterator(int i) {
        return new ListIterator<String>(this, i) { // from class: com.google.protobuf.UnmodifiableLazyStringList.1
            public ListIterator<String> iter;

            @Override // java.util.ListIterator
            public final void add(String str) {
                throw new UnsupportedOperationException();
            }

            @Override // java.util.ListIterator, java.util.Iterator
            public final boolean hasNext() {
                return this.iter.hasNext();
            }

            @Override // java.util.ListIterator
            public final boolean hasPrevious() {
                return this.iter.hasPrevious();
            }

            @Override // java.util.ListIterator, java.util.Iterator
            public final Object next() {
                return this.iter.next();
            }

            @Override // java.util.ListIterator
            public final int nextIndex() {
                return this.iter.nextIndex();
            }

            @Override // java.util.ListIterator
            public final String previous() {
                return this.iter.previous();
            }

            @Override // java.util.ListIterator
            public final int previousIndex() {
                return this.iter.previousIndex();
            }

            @Override // java.util.ListIterator, java.util.Iterator
            public final void remove() {
                throw new UnsupportedOperationException();
            }

            @Override // java.util.ListIterator
            public final void set(String str) {
                throw new UnsupportedOperationException();
            }

            {
                this.iter = this.list.listIterator(i);
            }
        };
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.list.size();
    }

    public UnmodifiableLazyStringList(LazyStringList lazyStringList) {
        this.list = lazyStringList;
    }
}
