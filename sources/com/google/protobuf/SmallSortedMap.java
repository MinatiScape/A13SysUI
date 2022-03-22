package com.google.protobuf;

import com.google.protobuf.FieldSet;
import java.lang.Comparable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
/* loaded from: classes.dex */
public class SmallSortedMap<K extends Comparable<K>, V> extends AbstractMap<K, V> {
    public static final /* synthetic */ int $r8$clinit = 0;
    public boolean isImmutable;
    public volatile SmallSortedMap<K, V>.EntrySet lazyEntrySet;
    public final int maxArraySize;
    public List<SmallSortedMap<K, V>.Entry> entryList = Collections.emptyList();
    public Map<K, V> overflowEntries = Collections.emptyMap();
    public Map<K, V> overflowEntriesDescending = Collections.emptyMap();

    /* renamed from: com.google.protobuf.SmallSortedMap$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass1 extends SmallSortedMap<Object, Object> {
        @Override // com.google.protobuf.SmallSortedMap
        public final void makeImmutable() {
            if (!this.isImmutable) {
                for (int i = 0; i < getNumArrayEntries(); i++) {
                    ((FieldSet.FieldDescriptorLite) getArrayEntryAt(i).getKey()).isRepeated();
                }
                for (Map.Entry<Object, Object> entry : getOverflowEntries()) {
                    ((FieldSet.FieldDescriptorLite) entry.getKey()).isRepeated();
                }
            }
            SmallSortedMap.super.makeImmutable();
        }

        public AnonymousClass1(int i) {
            super(i);
        }
    }

    /* loaded from: classes.dex */
    public static class EmptySet {
        public static final AnonymousClass1 ITERATOR = new Iterator<Object>() { // from class: com.google.protobuf.SmallSortedMap.EmptySet.1
            @Override // java.util.Iterator
            public final boolean hasNext() {
                return false;
            }

            @Override // java.util.Iterator
            public final Object next() {
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public final void remove() {
                throw new UnsupportedOperationException();
            }
        };
        public static final AnonymousClass2 ITERABLE = new Iterable<Object>() { // from class: com.google.protobuf.SmallSortedMap.EmptySet.2
            @Override // java.lang.Iterable
            public final Iterator<Object> iterator() {
                return EmptySet.ITERATOR;
            }
        };
    }

    /* loaded from: classes.dex */
    public class Entry implements Map.Entry<K, V>, Comparable<SmallSortedMap<K, V>.Entry> {
        public final K key;
        public V value;

        public Entry() {
            throw null;
        }

        public Entry(K k, V v) {
            this.key = k;
            this.value = v;
        }

        @Override // java.util.Map.Entry
        public final boolean equals(Object obj) {
            boolean z;
            boolean z2;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            K k = this.key;
            Object key = entry.getKey();
            if (k != null) {
                z = k.equals(key);
            } else if (key == null) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                V v = this.value;
                Object value = entry.getValue();
                if (v != null) {
                    z2 = v.equals(value);
                } else if (value == null) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (z2) {
                    return true;
                }
            }
            return false;
        }

        @Override // java.lang.Comparable
        public final int compareTo(Object obj) {
            Entry entry = (Entry) obj;
            K k = this.key;
            Objects.requireNonNull(entry);
            return k.compareTo(entry.key);
        }

        @Override // java.util.Map.Entry
        public final int hashCode() {
            int i;
            K k = this.key;
            int i2 = 0;
            if (k == null) {
                i = 0;
            } else {
                i = k.hashCode();
            }
            V v = this.value;
            if (v != null) {
                i2 = v.hashCode();
            }
            return i ^ i2;
        }

        @Override // java.util.Map.Entry
        public final V setValue(V v) {
            SmallSortedMap smallSortedMap = SmallSortedMap.this;
            int i = SmallSortedMap.$r8$clinit;
            smallSortedMap.checkMutable();
            V v2 = this.value;
            this.value = v;
            return v2;
        }

        public final String toString() {
            return this.key + "=" + this.value;
        }

        @Override // java.util.Map.Entry
        public final Object getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public final V getValue() {
            return this.value;
        }
    }

    /* loaded from: classes.dex */
    public class EntryIterator implements Iterator<Map.Entry<K, V>> {
        public Iterator<Map.Entry<K, V>> lazyOverflowIterator;
        public boolean nextCalledBeforeRemove;
        public int pos = -1;

        @Override // java.util.Iterator
        public final Object next() {
            this.nextCalledBeforeRemove = true;
            int i = this.pos + 1;
            this.pos = i;
            if (i < SmallSortedMap.this.entryList.size()) {
                return SmallSortedMap.this.entryList.get(this.pos);
            }
            return getOverflowIterator().next();
        }

        public EntryIterator() {
        }

        public final Iterator<Map.Entry<K, V>> getOverflowIterator() {
            if (this.lazyOverflowIterator == null) {
                this.lazyOverflowIterator = SmallSortedMap.this.overflowEntries.entrySet().iterator();
            }
            return this.lazyOverflowIterator;
        }

        @Override // java.util.Iterator
        public final boolean hasNext() {
            if (this.pos + 1 < SmallSortedMap.this.entryList.size()) {
                return true;
            }
            if (SmallSortedMap.this.overflowEntries.isEmpty() || !getOverflowIterator().hasNext()) {
                return false;
            }
            return true;
        }

        @Override // java.util.Iterator
        public final void remove() {
            if (this.nextCalledBeforeRemove) {
                this.nextCalledBeforeRemove = false;
                SmallSortedMap smallSortedMap = SmallSortedMap.this;
                int i = SmallSortedMap.$r8$clinit;
                smallSortedMap.checkMutable();
                if (this.pos < SmallSortedMap.this.entryList.size()) {
                    SmallSortedMap smallSortedMap2 = SmallSortedMap.this;
                    int i2 = this.pos;
                    this.pos = i2 - 1;
                    smallSortedMap2.removeArrayEntryAt(i2);
                    return;
                }
                getOverflowIterator().remove();
                return;
            }
            throw new IllegalStateException("remove() was called before next()");
        }
    }

    /* loaded from: classes.dex */
    public class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        public EntrySet() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean add(Object obj) {
            Map.Entry entry = (Map.Entry) obj;
            if (contains(entry)) {
                return false;
            }
            SmallSortedMap.this.put((SmallSortedMap) ((Comparable) entry.getKey()), (Comparable) entry.getValue());
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final void clear() {
            SmallSortedMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean contains(Object obj) {
            Map.Entry entry = (Map.Entry) obj;
            Object obj2 = SmallSortedMap.this.get(entry.getKey());
            Object value = entry.getValue();
            if (obj2 == value || (obj2 != null && obj2.equals(value))) {
                return true;
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean remove(Object obj) {
            Map.Entry entry = (Map.Entry) obj;
            if (!contains(entry)) {
                return false;
            }
            SmallSortedMap.this.remove(entry.getKey());
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final int size() {
            return SmallSortedMap.this.size();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SmallSortedMap)) {
            return super.equals(obj);
        }
        SmallSortedMap smallSortedMap = (SmallSortedMap) obj;
        int size = size();
        if (size != smallSortedMap.size()) {
            return false;
        }
        int numArrayEntries = getNumArrayEntries();
        if (numArrayEntries != smallSortedMap.getNumArrayEntries()) {
            return entrySet().equals(smallSortedMap.entrySet());
        }
        for (int i = 0; i < numArrayEntries; i++) {
            if (!getArrayEntryAt(i).equals(smallSortedMap.getArrayEntryAt(i))) {
                return false;
            }
        }
        if (numArrayEntries != size) {
            return this.overflowEntries.equals(smallSortedMap.overflowEntries);
        }
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractMap, java.util.Map
    public final /* bridge */ /* synthetic */ Object put(Object obj, Object obj2) {
        return put((SmallSortedMap<K, V>) ((Comparable) obj), (Comparable) obj2);
    }

    public final int binarySearchInArray(K k) {
        int size = this.entryList.size() - 1;
        if (size >= 0) {
            SmallSortedMap<K, V>.Entry entry = this.entryList.get(size);
            Objects.requireNonNull(entry);
            int compareTo = k.compareTo(entry.key);
            if (compareTo > 0) {
                return -(size + 2);
            }
            if (compareTo == 0) {
                return size;
            }
        }
        int i = 0;
        while (i <= size) {
            int i2 = (i + size) / 2;
            SmallSortedMap<K, V>.Entry entry2 = this.entryList.get(i2);
            Objects.requireNonNull(entry2);
            int compareTo2 = k.compareTo(entry2.key);
            if (compareTo2 < 0) {
                size = i2 - 1;
            } else if (compareTo2 <= 0) {
                return i2;
            } else {
                i = i2 + 1;
            }
        }
        return -(i + 1);
    }

    public final void checkMutable() {
        if (this.isImmutable) {
            throw new UnsupportedOperationException();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractMap, java.util.Map
    public final boolean containsKey(Object obj) {
        Comparable comparable = (Comparable) obj;
        if (binarySearchInArray(comparable) >= 0 || this.overflowEntries.containsKey(comparable)) {
            return true;
        }
        return false;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final Set<Map.Entry<K, V>> entrySet() {
        if (this.lazyEntrySet == null) {
            this.lazyEntrySet = new EntrySet();
        }
        return this.lazyEntrySet;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractMap, java.util.Map
    public final V get(Object obj) {
        Comparable comparable = (Comparable) obj;
        int binarySearchInArray = binarySearchInArray(comparable);
        if (binarySearchInArray < 0) {
            return this.overflowEntries.get(comparable);
        }
        SmallSortedMap<K, V>.Entry entry = this.entryList.get(binarySearchInArray);
        Objects.requireNonNull(entry);
        return entry.value;
    }

    public final Map.Entry<K, V> getArrayEntryAt(int i) {
        return this.entryList.get(i);
    }

    public final int getNumArrayEntries() {
        return this.entryList.size();
    }

    public final Iterable<Map.Entry<K, V>> getOverflowEntries() {
        if (this.overflowEntries.isEmpty()) {
            return EmptySet.ITERABLE;
        }
        return this.overflowEntries.entrySet();
    }

    public void makeImmutable() {
        Map<K, V> map;
        Map<K, V> map2;
        if (!this.isImmutable) {
            if (this.overflowEntries.isEmpty()) {
                map = Collections.emptyMap();
            } else {
                map = Collections.unmodifiableMap(this.overflowEntries);
            }
            this.overflowEntries = map;
            if (this.overflowEntriesDescending.isEmpty()) {
                map2 = Collections.emptyMap();
            } else {
                map2 = Collections.unmodifiableMap(this.overflowEntriesDescending);
            }
            this.overflowEntriesDescending = map2;
            this.isImmutable = true;
        }
    }

    public final V put(K k, V v) {
        checkMutable();
        int binarySearchInArray = binarySearchInArray(k);
        if (binarySearchInArray >= 0) {
            return this.entryList.get(binarySearchInArray).setValue(v);
        }
        checkMutable();
        if (this.entryList.isEmpty() && !(this.entryList instanceof ArrayList)) {
            this.entryList = new ArrayList(this.maxArraySize);
        }
        int i = -(binarySearchInArray + 1);
        if (i >= this.maxArraySize) {
            return getOverflowEntriesMutable().put(k, v);
        }
        int size = this.entryList.size();
        int i2 = this.maxArraySize;
        if (size == i2) {
            SmallSortedMap<K, V>.Entry remove = this.entryList.remove(i2 - 1);
            SortedMap<K, V> overflowEntriesMutable = getOverflowEntriesMutable();
            Objects.requireNonNull(remove);
            overflowEntriesMutable.put(remove.key, remove.value);
        }
        this.entryList.add(i, new Entry(k, v));
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final int size() {
        return this.overflowEntries.size() + this.entryList.size();
    }

    public SmallSortedMap(int i) {
        this.maxArraySize = i;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final void clear() {
        checkMutable();
        if (!this.entryList.isEmpty()) {
            this.entryList.clear();
        }
        if (!this.overflowEntries.isEmpty()) {
            this.overflowEntries.clear();
        }
    }

    public final SortedMap<K, V> getOverflowEntriesMutable() {
        checkMutable();
        if (this.overflowEntries.isEmpty() && !(this.overflowEntries instanceof TreeMap)) {
            TreeMap treeMap = new TreeMap();
            this.overflowEntries = treeMap;
            this.overflowEntriesDescending = treeMap.descendingMap();
        }
        return (SortedMap) this.overflowEntries;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final int hashCode() {
        int numArrayEntries = getNumArrayEntries();
        int i = 0;
        for (int i2 = 0; i2 < numArrayEntries; i2++) {
            i += this.entryList.get(i2).hashCode();
        }
        if (this.overflowEntries.size() > 0) {
            return i + this.overflowEntries.hashCode();
        }
        return i;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractMap, java.util.Map
    public final V remove(Object obj) {
        checkMutable();
        Comparable comparable = (Comparable) obj;
        int binarySearchInArray = binarySearchInArray(comparable);
        if (binarySearchInArray >= 0) {
            return (V) removeArrayEntryAt(binarySearchInArray);
        }
        if (this.overflowEntries.isEmpty()) {
            return null;
        }
        return this.overflowEntries.remove(comparable);
    }

    public final V removeArrayEntryAt(int i) {
        checkMutable();
        SmallSortedMap<K, V>.Entry remove = this.entryList.remove(i);
        Objects.requireNonNull(remove);
        V v = remove.value;
        if (!this.overflowEntries.isEmpty()) {
            Iterator<Map.Entry<K, V>> it = getOverflowEntriesMutable().entrySet().iterator();
            List<SmallSortedMap<K, V>.Entry> list = this.entryList;
            Map.Entry<K, V> next = it.next();
            list.add(new Entry(next.getKey(), next.getValue()));
            it.remove();
        }
        return v;
    }
}
