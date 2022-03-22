package kotlin.collections;

import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline0;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
/* compiled from: AbstractList.kt */
/* loaded from: classes.dex */
public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E> {

    /* compiled from: AbstractList.kt */
    /* loaded from: classes.dex */
    public class IteratorImpl implements Iterator<E>, KMappedMarker {
        public int index;

        @Override // java.util.Iterator
        public final void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        public IteratorImpl() {
        }

        @Override // java.util.Iterator
        public final boolean hasNext() {
            int i = this.index;
            AbstractList<E> abstractList = AbstractList.this;
            Objects.requireNonNull(abstractList);
            if (i < abstractList.getSize()) {
                return true;
            }
            return false;
        }

        @Override // java.util.Iterator
        public final E next() {
            if (hasNext()) {
                AbstractList<E> abstractList = AbstractList.this;
                int i = this.index;
                this.index = i + 1;
                return abstractList.get(i);
            }
            throw new NoSuchElementException();
        }
    }

    /* compiled from: AbstractList.kt */
    /* loaded from: classes.dex */
    public class ListIteratorImpl extends AbstractList<E>.IteratorImpl implements ListIterator<E> {
        @Override // java.util.ListIterator
        public final void add(E e) {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        @Override // java.util.ListIterator
        public final void set(E e) {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        public ListIteratorImpl(int i) {
            super();
            Objects.requireNonNull(AbstractList.this);
            int size = AbstractList.this.getSize();
            if (i < 0 || i > size) {
                throw new IndexOutOfBoundsException("index: " + i + ", size: " + size);
            }
            this.index = i;
        }

        @Override // java.util.ListIterator
        public final boolean hasPrevious() {
            if (this.index > 0) {
                return true;
            }
            return false;
        }

        @Override // java.util.ListIterator
        public final int previousIndex() {
            return this.index - 1;
        }

        @Override // java.util.ListIterator
        public final E previous() {
            if (hasPrevious()) {
                AbstractList<E> abstractList = AbstractList.this;
                int i = this.index - 1;
                this.index = i;
                return abstractList.get(i);
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.ListIterator
        public final int nextIndex() {
            return this.index;
        }
    }

    /* compiled from: AbstractList.kt */
    /* loaded from: classes.dex */
    public static final class SubList<E> extends AbstractList<E> implements RandomAccess {
        public int _size;
        public final int fromIndex;
        public final AbstractList<E> list;

        @Override // kotlin.collections.AbstractList, java.util.List
        public final E get(int i) {
            int i2 = this._size;
            if (i >= 0 && i < i2) {
                return this.list.get(this.fromIndex + i);
            }
            throw new IndexOutOfBoundsException("index: " + i + ", size: " + i2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public SubList(AbstractList<? extends E> abstractList, int i, int i2) {
            this.list = abstractList;
            this.fromIndex = i;
            Objects.requireNonNull(abstractList);
            int size = abstractList.getSize();
            if (i < 0 || i2 > size) {
                StringBuilder m = GridLayoutManager$$ExternalSyntheticOutline0.m("fromIndex: ", i, ", toIndex: ", i2, ", size: ");
                m.append(size);
                throw new IndexOutOfBoundsException(m.toString());
            } else if (i <= i2) {
                this._size = i2 - i;
            } else {
                throw new IllegalArgumentException("fromIndex: " + i + " > toIndex: " + i2);
            }
        }

        @Override // kotlin.collections.AbstractCollection
        public final int getSize() {
            return this._size;
        }
    }

    @Override // java.util.List
    public final void add(int i, E e) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.List
    public final boolean addAll(int i, Collection<? extends E> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection, java.util.List
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof List)) {
            return false;
        }
        Collection collection = (Collection) obj;
        if (size() != collection.size()) {
            return false;
        }
        Iterator<E> it = collection.iterator();
        for (E e : this) {
            if (!Intrinsics.areEqual(e, it.next())) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.List
    public abstract E get(int i);

    @Override // java.util.List
    public final ListIterator<E> listIterator() {
        return new ListIteratorImpl(0);
    }

    @Override // java.util.List
    public final E remove(int i) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.List
    public final E set(int i, E e) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection, java.lang.Iterable, java.util.List
    public final Iterator<E> iterator() {
        return new IteratorImpl();
    }

    @Override // java.util.List
    public final ListIterator<E> listIterator(int i) {
        return new ListIteratorImpl(i);
    }

    @Override // java.util.List
    public final List<E> subList(int i, int i2) {
        return new SubList(this, i, i2);
    }

    @Override // java.util.Collection, java.util.List
    public final int hashCode() {
        int i;
        int i2 = 1;
        for (E e : this) {
            int i3 = i2 * 31;
            if (e == null) {
                i = 0;
            } else {
                i = e.hashCode();
            }
            i2 = i3 + i;
        }
        return i2;
    }

    @Override // java.util.List
    public int indexOf(E e) {
        int i = 0;
        for (E e2 : this) {
            if (Intrinsics.areEqual(e2, e)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    @Override // java.util.List
    public int lastIndexOf(E e) {
        ListIterator<E> listIterator = listIterator(size());
        while (listIterator.hasPrevious()) {
            if (Intrinsics.areEqual(listIterator.previous(), e)) {
                return listIterator.nextIndex();
            }
        }
        return -1;
    }
}
