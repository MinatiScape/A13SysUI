package androidx.collection;

import java.lang.reflect.Array;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes.dex */
public final class ArrayMap<K, V> extends SimpleArrayMap<K, V> implements Map<K, V> {
    public ArrayMap<K, V>.EntrySet mEntrySet;
    public ArrayMap<K, V>.KeySet mKeySet;
    public ArrayMap<K, V>.ValueCollection mValues;

    /* loaded from: classes.dex */
    public final class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        public EntrySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public final Iterator<Map.Entry<K, V>> iterator() {
            return new MapIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final int size() {
            return ArrayMap.this.mSize;
        }
    }

    /* loaded from: classes.dex */
    public final class KeyIterator extends IndexBasedArrayIterator<K> {
        public KeyIterator() {
            super(ArrayMap.this.mSize);
        }

        @Override // androidx.collection.IndexBasedArrayIterator
        public final K elementAt(int i) {
            return ArrayMap.this.keyAt(i);
        }

        @Override // androidx.collection.IndexBasedArrayIterator
        public final void removeAt(int i) {
            ArrayMap.this.removeAt(i);
        }
    }

    /* loaded from: classes.dex */
    public final class KeySet implements Set<K> {
        @Override // java.util.Set, java.util.Collection
        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof Set) {
                Set set = (Set) obj;
                try {
                    if (size() == set.size()) {
                        if (containsAll(set)) {
                            return true;
                        }
                    }
                } catch (ClassCastException | NullPointerException unused) {
                }
            }
            return false;
        }

        @Override // java.util.Set, java.util.Collection
        public final Object[] toArray() {
            int i = ArrayMap.this.mSize;
            Object[] objArr = new Object[i];
            for (int i2 = 0; i2 < i; i2++) {
                objArr[i2] = ArrayMap.this.keyAt(i2);
            }
            return objArr;
        }

        public KeySet() {
        }

        @Override // java.util.Set, java.util.Collection
        public final boolean add(K k) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public final boolean addAll(Collection<? extends K> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public final void clear() {
            ArrayMap.this.clear();
        }

        @Override // java.util.Set, java.util.Collection
        public final boolean contains(Object obj) {
            return ArrayMap.this.containsKey(obj);
        }

        @Override // java.util.Set, java.util.Collection
        public final boolean containsAll(Collection<?> collection) {
            ArrayMap arrayMap = ArrayMap.this;
            Objects.requireNonNull(arrayMap);
            Iterator<?> it = collection.iterator();
            while (it.hasNext()) {
                if (!arrayMap.containsKey(it.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // java.util.Set, java.util.Collection
        public final int hashCode() {
            int i;
            int i2 = 0;
            for (int i3 = ArrayMap.this.mSize - 1; i3 >= 0; i3--) {
                K keyAt = ArrayMap.this.keyAt(i3);
                if (keyAt == null) {
                    i = 0;
                } else {
                    i = keyAt.hashCode();
                }
                i2 += i;
            }
            return i2;
        }

        @Override // java.util.Set, java.util.Collection
        public final boolean isEmpty() {
            return ArrayMap.this.isEmpty();
        }

        @Override // java.util.Set, java.util.Collection, java.lang.Iterable
        public final Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override // java.util.Set, java.util.Collection
        public final boolean remove(Object obj) {
            int indexOfKey = ArrayMap.this.indexOfKey(obj);
            if (indexOfKey < 0) {
                return false;
            }
            ArrayMap.this.removeAt(indexOfKey);
            return true;
        }

        @Override // java.util.Set, java.util.Collection
        public final boolean removeAll(Collection<?> collection) {
            ArrayMap arrayMap = ArrayMap.this;
            Objects.requireNonNull(arrayMap);
            int i = arrayMap.mSize;
            Iterator<?> it = collection.iterator();
            while (it.hasNext()) {
                arrayMap.remove(it.next());
            }
            if (i != arrayMap.mSize) {
                return true;
            }
            return false;
        }

        @Override // java.util.Set, java.util.Collection
        public final boolean retainAll(Collection<?> collection) {
            return ArrayMap.this.retainAll(collection);
        }

        @Override // java.util.Set, java.util.Collection
        public final int size() {
            return ArrayMap.this.mSize;
        }

        @Override // java.util.Set, java.util.Collection
        public final <T> T[] toArray(T[] tArr) {
            return (T[]) ArrayMap.this.toArrayHelper(tArr, 0);
        }
    }

    /* loaded from: classes.dex */
    public final class MapIterator implements Iterator<Map.Entry<K, V>>, Map.Entry<K, V> {
        public int mEnd;
        public boolean mEntryValid;
        public int mIndex = -1;

        public MapIterator() {
            this.mEnd = ArrayMap.this.mSize - 1;
        }

        @Override // java.util.Map.Entry
        public final boolean equals(Object obj) {
            boolean z;
            boolean z2;
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            } else if (!(obj instanceof Map.Entry)) {
                return false;
            } else {
                Map.Entry entry = (Map.Entry) obj;
                Object key = entry.getKey();
                K keyAt = ArrayMap.this.keyAt(this.mIndex);
                if (key == keyAt || (key != null && key.equals(keyAt))) {
                    z = true;
                } else {
                    z = false;
                }
                if (!z) {
                    return false;
                }
                Object value = entry.getValue();
                V valueAt = ArrayMap.this.valueAt(this.mIndex);
                if (value == valueAt || (value != null && value.equals(valueAt))) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (z2) {
                    return true;
                }
                return false;
            }
        }

        @Override // java.util.Map.Entry
        public final K getKey() {
            if (this.mEntryValid) {
                return ArrayMap.this.keyAt(this.mIndex);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        @Override // java.util.Map.Entry
        public final V getValue() {
            if (this.mEntryValid) {
                return ArrayMap.this.valueAt(this.mIndex);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        @Override // java.util.Iterator
        public final boolean hasNext() {
            if (this.mIndex < this.mEnd) {
                return true;
            }
            return false;
        }

        @Override // java.util.Map.Entry
        public final int hashCode() {
            int i;
            if (this.mEntryValid) {
                K keyAt = ArrayMap.this.keyAt(this.mIndex);
                V valueAt = ArrayMap.this.valueAt(this.mIndex);
                int i2 = 0;
                if (keyAt == null) {
                    i = 0;
                } else {
                    i = keyAt.hashCode();
                }
                if (valueAt != null) {
                    i2 = valueAt.hashCode();
                }
                return i ^ i2;
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        @Override // java.util.Iterator
        public final void remove() {
            if (this.mEntryValid) {
                ArrayMap.this.removeAt(this.mIndex);
                this.mIndex--;
                this.mEnd--;
                this.mEntryValid = false;
                return;
            }
            throw new IllegalStateException();
        }

        @Override // java.util.Map.Entry
        public final V setValue(V v) {
            if (this.mEntryValid) {
                ArrayMap arrayMap = ArrayMap.this;
                int i = this.mIndex;
                Objects.requireNonNull(arrayMap);
                int i2 = (i << 1) + 1;
                Object[] objArr = arrayMap.mArray;
                V v2 = (V) objArr[i2];
                objArr[i2] = v;
                return v2;
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        public final String toString() {
            return getKey() + "=" + getValue();
        }

        @Override // java.util.Iterator
        public final Object next() {
            if (hasNext()) {
                this.mIndex++;
                this.mEntryValid = true;
                return this;
            }
            throw new NoSuchElementException();
        }
    }

    /* loaded from: classes.dex */
    public final class ValueCollection implements Collection<V> {
        @Override // java.util.Collection
        public final Object[] toArray() {
            int i = ArrayMap.this.mSize;
            Object[] objArr = new Object[i];
            for (int i2 = 0; i2 < i; i2++) {
                objArr[i2] = ArrayMap.this.valueAt(i2);
            }
            return objArr;
        }

        public ValueCollection() {
        }

        @Override // java.util.Collection
        public final boolean add(V v) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public final boolean addAll(Collection<? extends V> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public final void clear() {
            ArrayMap.this.clear();
        }

        @Override // java.util.Collection
        public final boolean contains(Object obj) {
            if (ArrayMap.this.indexOfValue(obj) >= 0) {
                return true;
            }
            return false;
        }

        @Override // java.util.Collection
        public final boolean isEmpty() {
            return ArrayMap.this.isEmpty();
        }

        @Override // java.util.Collection, java.lang.Iterable
        public final Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override // java.util.Collection
        public final boolean remove(Object obj) {
            int indexOfValue = ArrayMap.this.indexOfValue(obj);
            if (indexOfValue < 0) {
                return false;
            }
            ArrayMap.this.removeAt(indexOfValue);
            return true;
        }

        @Override // java.util.Collection
        public final boolean removeAll(Collection<?> collection) {
            int i = ArrayMap.this.mSize;
            int i2 = 0;
            boolean z = false;
            while (i2 < i) {
                if (collection.contains(ArrayMap.this.valueAt(i2))) {
                    ArrayMap.this.removeAt(i2);
                    i2--;
                    i--;
                    z = true;
                }
                i2++;
            }
            return z;
        }

        @Override // java.util.Collection
        public final boolean retainAll(Collection<?> collection) {
            int i = ArrayMap.this.mSize;
            int i2 = 0;
            boolean z = false;
            while (i2 < i) {
                if (!collection.contains(ArrayMap.this.valueAt(i2))) {
                    ArrayMap.this.removeAt(i2);
                    i2--;
                    i--;
                    z = true;
                }
                i2++;
            }
            return z;
        }

        @Override // java.util.Collection
        public final int size() {
            return ArrayMap.this.mSize;
        }

        @Override // java.util.Collection
        public final boolean containsAll(Collection<?> collection) {
            Iterator<?> it = collection.iterator();
            while (it.hasNext()) {
                if (!contains(it.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // java.util.Collection
        public final <T> T[] toArray(T[] tArr) {
            return (T[]) ArrayMap.this.toArrayHelper(tArr, 1);
        }
    }

    /* loaded from: classes.dex */
    public final class ValueIterator extends IndexBasedArrayIterator<V> {
        public ValueIterator() {
            super(ArrayMap.this.mSize);
        }

        @Override // androidx.collection.IndexBasedArrayIterator
        public final V elementAt(int i) {
            return ArrayMap.this.valueAt(i);
        }

        @Override // androidx.collection.IndexBasedArrayIterator
        public final void removeAt(int i) {
            ArrayMap.this.removeAt(i);
        }
    }

    public ArrayMap() {
    }

    public ArrayMap(ArrayMap arrayMap) {
        if (arrayMap != null) {
            int i = arrayMap.mSize;
            ensureCapacity(this.mSize + i);
            if (this.mSize != 0) {
                for (int i2 = 0; i2 < i; i2++) {
                    put(arrayMap.keyAt(i2), arrayMap.valueAt(i2));
                }
            } else if (i > 0) {
                System.arraycopy(arrayMap.mHashes, 0, this.mHashes, 0, i);
                System.arraycopy(arrayMap.mArray, 0, this.mArray, 0, i << 1);
                this.mSize = i;
            }
        }
    }

    @Override // java.util.Map
    public final Set<Map.Entry<K, V>> entrySet() {
        ArrayMap<K, V>.EntrySet entrySet = this.mEntrySet;
        if (entrySet != null) {
            return entrySet;
        }
        ArrayMap<K, V>.EntrySet entrySet2 = new EntrySet();
        this.mEntrySet = entrySet2;
        return entrySet2;
    }

    @Override // java.util.Map
    public final Set<K> keySet() {
        ArrayMap<K, V>.KeySet keySet = this.mKeySet;
        if (keySet != null) {
            return keySet;
        }
        ArrayMap<K, V>.KeySet keySet2 = new KeySet();
        this.mKeySet = keySet2;
        return keySet2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    public final void putAll(Map<? extends K, ? extends V> map) {
        ensureCapacity(map.size() + this.mSize);
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public final boolean retainAll(Collection<?> collection) {
        int i = this.mSize;
        for (int i2 = i - 1; i2 >= 0; i2--) {
            if (!collection.contains(keyAt(i2))) {
                removeAt(i2);
            }
        }
        if (i != this.mSize) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <T> T[] toArrayHelper(T[] tArr, int i) {
        int i2 = this.mSize;
        if (tArr.length < i2) {
            tArr = (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), i2));
        }
        for (int i3 = 0; i3 < i2; i3++) {
            tArr[i3] = this.mArray[(i3 << 1) + i];
        }
        if (tArr.length > i2) {
            tArr[i2] = null;
        }
        return tArr;
    }

    @Override // java.util.Map
    public final Collection<V> values() {
        ArrayMap<K, V>.ValueCollection valueCollection = this.mValues;
        if (valueCollection != null) {
            return valueCollection;
        }
        ArrayMap<K, V>.ValueCollection valueCollection2 = new ValueCollection();
        this.mValues = valueCollection2;
        return valueCollection2;
    }
}
