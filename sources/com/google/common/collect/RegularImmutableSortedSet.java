package com.google.common.collect;

import androidx.leanback.R$id;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class RegularImmutableSortedSet<E> extends ImmutableSortedSet<E> {
    public static final RegularImmutableSortedSet<Comparable> NATURAL_EMPTY_SET = new RegularImmutableSortedSet<>(RegularImmutableList.EMPTY, NaturalOrdering.INSTANCE);
    public final transient ImmutableList<E> elements;

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    public final E ceiling(E e) {
        int tailIndex = tailIndex(e, true);
        if (tailIndex == size()) {
            return null;
        }
        return this.elements.get(tailIndex);
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection
    public final boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            return Collections.binarySearch(this.elements, obj, this.comparator) >= 0;
        } catch (ClassCastException unused) {
            return false;
        }
    }

    @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.util.Set
    public final boolean equals(Object obj) {
        Object next;
        E next2;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Set)) {
            return false;
        }
        Set set = (Set) obj;
        if (size() != set.size()) {
            return false;
        }
        if (isEmpty()) {
            return true;
        }
        if (!R$id.hasSameComparator(this.comparator, set)) {
            return containsAll(set);
        }
        Iterator<E> it = set.iterator();
        try {
            UnmodifiableIterator<E> it2 = mo174iterator();
            do {
                AbstractIndexedListIterator abstractIndexedListIterator = (AbstractIndexedListIterator) it2;
                if (abstractIndexedListIterator.hasNext()) {
                    next = abstractIndexedListIterator.next();
                    next2 = it.next();
                    if (next2 == null) {
                        break;
                    }
                } else {
                    return true;
                }
            } while (this.comparator.compare(next, next2) == 0);
            return false;
        } catch (ClassCastException | NoSuchElementException unused) {
            return false;
        }
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    public final E floor(E e) {
        int headIndex = headIndex(e, true) - 1;
        if (headIndex == -1) {
            return null;
        }
        return this.elements.get(headIndex);
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    public final E higher(E e) {
        int tailIndex = tailIndex(e, false);
        if (tailIndex == size()) {
            return null;
        }
        return this.elements.get(tailIndex);
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    public final E lower(E e) {
        int headIndex = headIndex(e, false) - 1;
        if (headIndex == -1) {
            return null;
        }
        return this.elements.get(headIndex);
    }

    static {
        ImmutableList.Itr itr = ImmutableList.EMPTY_ITR;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean containsAll(Collection<?> collection) {
        if (collection instanceof Multiset) {
            collection = ((Multiset) collection).elementSet();
        }
        if (!R$id.hasSameComparator(this.comparator, collection) || collection.size() <= 1) {
            return super.containsAll(collection);
        }
        UnmodifiableIterator<E> it = mo174iterator();
        Iterator<?> it2 = collection.iterator();
        AbstractIndexedListIterator abstractIndexedListIterator = (AbstractIndexedListIterator) it;
        if (!abstractIndexedListIterator.hasNext()) {
            return false;
        }
        Object next = it2.next();
        Object next2 = abstractIndexedListIterator.next();
        while (true) {
            try {
                int compare = this.comparator.compare(next2, next);
                if (compare < 0) {
                    if (!abstractIndexedListIterator.hasNext()) {
                        return false;
                    }
                    next2 = abstractIndexedListIterator.next();
                } else if (compare == 0) {
                    if (!it2.hasNext()) {
                        return true;
                    }
                    next = it2.next();
                } else if (compare > 0) {
                    break;
                }
            } catch (ClassCastException | NullPointerException unused) {
            }
        }
        return false;
    }

    @Override // com.google.common.collect.ImmutableCollection
    public final int copyIntoArray(Object[] objArr) {
        return this.elements.copyIntoArray(objArr);
    }

    @Override // com.google.common.collect.ImmutableSortedSet
    public final ImmutableSortedSet<E> createDescendingSet() {
        Comparator reverseOrder = Collections.reverseOrder(this.comparator);
        if (isEmpty()) {
            return ImmutableSortedSet.emptySet(reverseOrder);
        }
        return new RegularImmutableSortedSet(this.elements.reverse(), reverseOrder);
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    public final ImmutableList.Itr descendingIterator() {
        ImmutableList<E> reverse = this.elements.reverse();
        Objects.requireNonNull(reverse);
        return reverse.listIterator(0);
    }

    public final RegularImmutableSortedSet<E> getSubSet(int i, int i2) {
        if (i == 0 && i2 == size()) {
            return this;
        }
        if (i < i2) {
            return new RegularImmutableSortedSet<>(this.elements.subList(i, i2), this.comparator);
        }
        return ImmutableSortedSet.emptySet(this.comparator);
    }

    public final int headIndex(E e, boolean z) {
        ImmutableList<E> immutableList = this.elements;
        Objects.requireNonNull(e);
        int binarySearch = Collections.binarySearch(immutableList, e, this.comparator);
        if (binarySearch < 0) {
            return ~binarySearch;
        }
        if (z) {
            return binarySearch + 1;
        }
        return binarySearch;
    }

    @Override // com.google.common.collect.ImmutableCollection
    public final Object[] internalArray() {
        return this.elements.internalArray();
    }

    @Override // com.google.common.collect.ImmutableCollection
    public final int internalArrayEnd() {
        return this.elements.internalArrayEnd();
    }

    @Override // com.google.common.collect.ImmutableCollection
    public final int internalArrayStart() {
        return this.elements.internalArrayStart();
    }

    @Override // com.google.common.collect.ImmutableSortedSetFauxverideShim, com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    /* renamed from: iterator */
    public final UnmodifiableIterator<E> mo174iterator() {
        ImmutableList<E> immutableList = this.elements;
        Objects.requireNonNull(immutableList);
        return immutableList.listIterator(0);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final int size() {
        return this.elements.size();
    }

    public final int tailIndex(E e, boolean z) {
        ImmutableList<E> immutableList = this.elements;
        Objects.requireNonNull(e);
        int binarySearch = Collections.binarySearch(immutableList, e, this.comparator);
        if (binarySearch < 0) {
            return ~binarySearch;
        }
        if (z) {
            return binarySearch;
        }
        return binarySearch + 1;
    }

    public RegularImmutableSortedSet(ImmutableList<E> immutableList, Comparator<? super E> comparator) {
        super(comparator);
        this.elements = immutableList;
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.SortedSet
    public final E first() {
        if (!isEmpty()) {
            return this.elements.get(0);
        }
        throw new NoSuchElementException();
    }

    @Override // com.google.common.collect.ImmutableSortedSet
    public final ImmutableSortedSet<E> headSetImpl(E e, boolean z) {
        return getSubSet(0, headIndex(e, z));
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.SortedSet
    public final E last() {
        if (!isEmpty()) {
            return this.elements.get(size() - 1);
        }
        throw new NoSuchElementException();
    }

    @Override // com.google.common.collect.ImmutableSortedSet
    public final ImmutableSortedSet<E> subSetImpl(E e, boolean z, E e2, boolean z2) {
        RegularImmutableSortedSet regularImmutableSortedSet = (RegularImmutableSortedSet) tailSetImpl(e, z);
        Objects.requireNonNull(regularImmutableSortedSet);
        return regularImmutableSortedSet.getSubSet(0, regularImmutableSortedSet.headIndex(e2, z2));
    }

    @Override // com.google.common.collect.ImmutableSortedSet
    public final ImmutableSortedSet<E> tailSetImpl(E e, boolean z) {
        return getSubSet(tailIndex(e, z), size());
    }

    @Override // com.google.common.collect.ImmutableSet
    public final ImmutableList<E> asList() {
        return this.elements;
    }
}
