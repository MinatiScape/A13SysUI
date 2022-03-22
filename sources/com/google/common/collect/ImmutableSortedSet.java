package com.google.common.collect;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.DoNotCall;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.SortedSet;
import okio.Okio__OkioKt;
/* loaded from: classes.dex */
public abstract class ImmutableSortedSet<E> extends ImmutableSortedSetFauxverideShim<E> implements NavigableSet<E>, SortedIterable<E> {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final transient Comparator<? super E> comparator;
    @LazyInit
    public transient ImmutableSortedSet<E> descendingSet;

    /* loaded from: classes.dex */
    public static class SerializedForm<E> implements Serializable {
        private static final long serialVersionUID = 0;
        public final Comparator<? super E> comparator;
        public final Object[] elements;

        public Object readResolve() {
            RegularImmutableSortedSet regularImmutableSortedSet;
            Builder builder = new Builder(this.comparator);
            Object[] objArr = this.elements;
            if (builder.hashTable != null) {
                for (Object obj : objArr) {
                    builder.add(obj);
                }
            } else {
                int length = objArr.length;
                ObjectArrays.checkElementsNotNull(objArr, length);
                builder.getReadyToExpandTo(builder.size + length);
                System.arraycopy(objArr, 0, builder.contents, builder.size, length);
                builder.size += length;
            }
            Object[] objArr2 = builder.contents;
            Comparator<? super E> comparator = builder.comparator;
            int i = builder.size;
            int i2 = ImmutableSortedSet.$r8$clinit;
            if (i == 0) {
                regularImmutableSortedSet = ImmutableSortedSet.emptySet(comparator);
            } else {
                ObjectArrays.checkElementsNotNull(objArr2, i);
                Arrays.sort(objArr2, 0, i, comparator);
                int i3 = 1;
                for (int i4 = 1; i4 < i; i4++) {
                    Object obj2 = objArr2[i4];
                    if (comparator.compare(obj2, objArr2[i3 - 1]) != 0) {
                        objArr2[i3] = obj2;
                        i3++;
                    }
                }
                Arrays.fill(objArr2, i3, i, (Object) null);
                if (i3 < objArr2.length / 2) {
                    objArr2 = Arrays.copyOf(objArr2, i3);
                }
                regularImmutableSortedSet = new RegularImmutableSortedSet(ImmutableList.asImmutableList(objArr2, i3), comparator);
            }
            builder.size = regularImmutableSortedSet.size();
            builder.forceCopy = true;
            return regularImmutableSortedSet;
        }

        public SerializedForm(Comparator<? super E> comparator, Object[] objArr) {
            this.comparator = comparator;
            this.elements = objArr;
        }
    }

    public abstract ImmutableSortedSet<E> createDescendingSet();

    @Override // java.util.NavigableSet
    public abstract ImmutableList.Itr descendingIterator();

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableSet
    public final NavigableSet headSet(Object obj, boolean z) {
        Objects.requireNonNull(obj);
        return headSetImpl(obj, z);
    }

    public abstract ImmutableSortedSet<E> headSetImpl(E e, boolean z);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableSet
    public final NavigableSet subSet(Object obj, boolean z, Object obj2, boolean z2) {
        Objects.requireNonNull(obj);
        Objects.requireNonNull(obj2);
        if (this.comparator.compare(obj, obj2) <= 0) {
            return subSetImpl(obj, z, obj2, z2);
        }
        throw new IllegalArgumentException();
    }

    public abstract ImmutableSortedSet<E> subSetImpl(E e, boolean z, E e2, boolean z2);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableSet
    public final NavigableSet tailSet(Object obj, boolean z) {
        Objects.requireNonNull(obj);
        return tailSetImpl(obj, z);
    }

    public abstract ImmutableSortedSet<E> tailSetImpl(E e, boolean z);

    /* loaded from: classes.dex */
    public static final class Builder<E> extends ImmutableSet.Builder<E> {
        public final Comparator<? super E> comparator;

        public Builder(Comparator<? super E> comparator) {
            Objects.requireNonNull(comparator);
            this.comparator = comparator;
        }

        @CanIgnoreReturnValue
        public final ImmutableSet.Builder add(Object obj) {
            Objects.requireNonNull(obj);
            if (this.hashTable != null) {
                int chooseTableSize = ImmutableSet.chooseTableSize(this.size);
                Object[] objArr = this.hashTable;
                if (chooseTableSize <= objArr.length) {
                    int length = objArr.length - 1;
                    int smear = Okio__OkioKt.smear(obj.hashCode());
                    while (true) {
                        int i = smear & length;
                        Object[] objArr2 = this.hashTable;
                        Object obj2 = objArr2[i];
                        if (obj2 == null) {
                            objArr2[i] = obj;
                            getReadyToExpandTo(this.size + 1);
                            Object[] objArr3 = this.contents;
                            int i2 = this.size;
                            this.size = i2 + 1;
                            objArr3[i2] = obj;
                            break;
                        } else if (obj2.equals(obj)) {
                            break;
                        } else {
                            smear = i + 1;
                        }
                    }
                    return this;
                }
            }
            this.hashTable = null;
            getReadyToExpandTo(this.size + 1);
            Object[] objArr4 = this.contents;
            int i3 = this.size;
            this.size = i3 + 1;
            objArr4[i3] = obj;
            return this;
        }
    }

    public static <E> RegularImmutableSortedSet<E> emptySet(Comparator<? super E> comparator) {
        if (NaturalOrdering.INSTANCE.equals(comparator)) {
            return (RegularImmutableSortedSet<E>) RegularImmutableSortedSet.NATURAL_EMPTY_SET;
        }
        return new RegularImmutableSortedSet<>(RegularImmutableList.EMPTY, comparator);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    @Override // java.util.NavigableSet
    public final NavigableSet descendingSet() {
        ImmutableSortedSet<E> immutableSortedSet = this.descendingSet;
        if (immutableSortedSet != null) {
            return immutableSortedSet;
        }
        ImmutableSortedSet<E> createDescendingSet = createDescendingSet();
        this.descendingSet = createDescendingSet;
        createDescendingSet.descendingSet = this;
        return createDescendingSet;
    }

    @Override // java.util.NavigableSet
    @CanIgnoreReturnValue
    @DoNotCall("Always throws UnsupportedOperationException")
    @Deprecated
    public final E pollFirst() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.NavigableSet
    @CanIgnoreReturnValue
    @DoNotCall("Always throws UnsupportedOperationException")
    @Deprecated
    public final E pollLast() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
    public Object writeReplace() {
        return new SerializedForm(this.comparator, toArray());
    }

    public ImmutableSortedSet(Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    @Override // java.util.NavigableSet
    public E ceiling(E e) {
        Objects.requireNonNull(e);
        return (E) Iterators.getNext(tailSetImpl(e, true).iterator());
    }

    @Override // java.util.SortedSet
    public E first() {
        return iterator().next();
    }

    @Override // java.util.NavigableSet
    public E floor(E e) {
        Objects.requireNonNull(e);
        return (E) Iterators.getNext(headSetImpl(e, true).descendingIterator());
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableSet, java.util.SortedSet
    public final SortedSet headSet(Object obj) {
        Objects.requireNonNull(obj);
        return headSetImpl(obj, false);
    }

    @Override // java.util.NavigableSet
    public E higher(E e) {
        Objects.requireNonNull(e);
        return (E) Iterators.getNext(tailSetImpl(e, false).iterator());
    }

    @Override // java.util.SortedSet
    public E last() {
        return descendingIterator().next();
    }

    @Override // java.util.NavigableSet
    public E lower(E e) {
        Objects.requireNonNull(e);
        return (E) Iterators.getNext(headSetImpl(e, false).descendingIterator());
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableSet, java.util.SortedSet
    public final SortedSet tailSet(Object obj) {
        Objects.requireNonNull(obj);
        return tailSetImpl(obj, true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableSet, java.util.SortedSet
    public final SortedSet subSet(Object obj, Object obj2) {
        Objects.requireNonNull(obj);
        Objects.requireNonNull(obj2);
        if (this.comparator.compare(obj, obj2) <= 0) {
            return subSetImpl(obj, true, obj2, false);
        }
        throw new IllegalArgumentException();
    }

    @Override // java.util.SortedSet, com.google.common.collect.SortedIterable
    public final Comparator<? super E> comparator() {
        return this.comparator;
    }
}
