package com.google.common.collect;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.DoNotCall;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
/* loaded from: classes.dex */
public final class ImmutableSortedMap<K, V> extends ImmutableSortedMapFauxverideShim<K, V> implements NavigableMap<K, V> {
    public static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP;
    private static final long serialVersionUID = 0;
    public transient ImmutableSortedMap<K, V> descendingMap;
    public final transient RegularImmutableSortedSet<K> keySet;
    public final transient ImmutableList<V> valueList;

    /* loaded from: classes.dex */
    public static class Builder<K, V> extends ImmutableMap.Builder<K, V> {
        public final Comparator<? super K> comparator;
        public transient Object[] keys = new Object[4];
        public transient Object[] values = new Object[4];

        public Builder(Comparator<? super K> comparator) {
            super(4);
            Objects.requireNonNull(comparator);
            this.comparator = comparator;
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public final ImmutableSortedMap<K, V> buildOrThrow() {
            int i = this.size;
            if (i == 0) {
                return ImmutableSortedMap.emptyMap(this.comparator);
            }
            if (i != 1) {
                Object[] copyOf = Arrays.copyOf(this.keys, i);
                Arrays.sort(copyOf, this.comparator);
                int i2 = this.size;
                Object[] objArr = new Object[i2];
                for (int i3 = 0; i3 < this.size; i3++) {
                    if (i3 > 0) {
                        int i4 = i3 - 1;
                        if (this.comparator.compare(copyOf[i4], copyOf[i3]) == 0) {
                            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("keys required to be distinct but compared as equal: ");
                            m.append(copyOf[i4]);
                            m.append(" and ");
                            m.append(copyOf[i3]);
                            throw new IllegalArgumentException(m.toString());
                        }
                    }
                    Object obj = this.keys[i3];
                    Objects.requireNonNull(obj);
                    int binarySearch = Arrays.binarySearch(copyOf, obj, this.comparator);
                    Object obj2 = this.values[i3];
                    Objects.requireNonNull(obj2);
                    objArr[binarySearch] = obj2;
                }
                return new ImmutableSortedMap<>(new RegularImmutableSortedSet(ImmutableList.asImmutableList(copyOf, copyOf.length), this.comparator), ImmutableList.asImmutableList(objArr, i2), null);
            }
            Comparator<? super K> comparator = this.comparator;
            Object obj3 = this.keys[0];
            Objects.requireNonNull(obj3);
            Object obj4 = this.values[0];
            Objects.requireNonNull(obj4);
            Object[] objArr2 = {obj3};
            ObjectArrays.checkElementsNotNull(objArr2, 1);
            ImmutableList asImmutableList = ImmutableList.asImmutableList(objArr2, 1);
            Objects.requireNonNull(comparator);
            RegularImmutableSortedSet regularImmutableSortedSet = new RegularImmutableSortedSet(asImmutableList, comparator);
            Object[] objArr3 = {obj4};
            ObjectArrays.checkElementsNotNull(objArr3, 1);
            return new ImmutableSortedMap<>(regularImmutableSortedSet, ImmutableList.asImmutableList(objArr3, 1), null);
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        @CanIgnoreReturnValue
        public final ImmutableMap.Builder put(Object obj, Object obj2) {
            int i = this.size + 1;
            Object[] objArr = this.keys;
            if (i > objArr.length) {
                int expandedCapacity = ImmutableCollection.Builder.expandedCapacity(objArr.length, i);
                this.keys = Arrays.copyOf(this.keys, expandedCapacity);
                this.values = Arrays.copyOf(this.values, expandedCapacity);
            }
            CollectPreconditions.checkEntryNotNull(obj, obj2);
            Object[] objArr2 = this.keys;
            int i2 = this.size;
            objArr2[i2] = obj;
            this.values[i2] = obj2;
            this.size = i2 + 1;
            return this;
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public final ImmutableMap build() {
            return buildOrThrow();
        }
    }

    /* loaded from: classes.dex */
    public static class SerializedForm<K, V> extends ImmutableMap.SerializedForm<K, V> {
        private static final long serialVersionUID = 0;
        private final Comparator<? super K> comparator;

        @Override // com.google.common.collect.ImmutableMap.SerializedForm
        public final ImmutableMap.Builder makeBuilder(int i) {
            return new Builder(this.comparator);
        }

        public SerializedForm(ImmutableSortedMap<K, V> immutableSortedMap) {
            super(immutableSortedMap);
            this.comparator = immutableSortedMap.comparator();
        }
    }

    @Override // java.util.NavigableMap
    public final Map.Entry<K, V> ceilingEntry(K k) {
        return tailMap((ImmutableSortedMap<K, V>) k, true).firstEntry();
    }

    @Override // java.util.NavigableMap
    public final K ceilingKey(K k) {
        return (K) Maps.keyOrNull(tailMap((ImmutableSortedMap<K, V>) k, true).firstEntry());
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    /* renamed from: entrySet */
    public final ImmutableSet<Map.Entry<K, V>> mo175entrySet() {
        return super.entrySet();
    }

    @Override // java.util.NavigableMap
    public final Map.Entry<K, V> floorEntry(K k) {
        return headMap((ImmutableSortedMap<K, V>) k, true).lastEntry();
    }

    @Override // java.util.NavigableMap
    public final K floorKey(K k) {
        return (K) Maps.keyOrNull(headMap((ImmutableSortedMap<K, V>) k, true).lastEntry());
    }

    @Override // java.util.NavigableMap
    public final Map.Entry<K, V> higherEntry(K k) {
        return tailMap((ImmutableSortedMap<K, V>) k, false).firstEntry();
    }

    @Override // java.util.NavigableMap
    public final K higherKey(K k) {
        return (K) Maps.keyOrNull(tailMap((ImmutableSortedMap<K, V>) k, false).firstEntry());
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    /* renamed from: keySet */
    public final ImmutableSet mo176keySet() {
        return this.keySet;
    }

    @Override // java.util.NavigableMap
    public final Map.Entry<K, V> lowerEntry(K k) {
        return headMap((ImmutableSortedMap<K, V>) k, false).lastEntry();
    }

    @Override // java.util.NavigableMap
    public final K lowerKey(K k) {
        return (K) Maps.keyOrNull(headMap((ImmutableSortedMap<K, V>) k, false).lastEntry());
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    /* renamed from: values */
    public final ImmutableCollection<V> mo177values() {
        return this.valueList;
    }

    static {
        RegularImmutableSortedSet emptySet = ImmutableSortedSet.emptySet(NaturalOrdering.INSTANCE);
        ImmutableList.Itr itr = ImmutableList.EMPTY_ITR;
        NATURAL_EMPTY_MAP = new ImmutableSortedMap<>(emptySet, RegularImmutableList.EMPTY, null);
    }

    public static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<? super K> comparator) {
        if (NaturalOrdering.INSTANCE.equals(comparator)) {
            return (ImmutableSortedMap<K, V>) NATURAL_EMPTY_MAP;
        }
        return new ImmutableSortedMap<>(ImmutableSortedSet.emptySet(comparator), RegularImmutableList.EMPTY, null);
    }

    @Override // java.util.SortedMap
    public final Comparator<? super K> comparator() {
        RegularImmutableSortedSet<K> regularImmutableSortedSet = this.keySet;
        Objects.requireNonNull(regularImmutableSortedSet);
        return regularImmutableSortedSet.comparator;
    }

    @Override // com.google.common.collect.ImmutableMap
    public final ImmutableSet<K> createKeySet() {
        throw new AssertionError("should never be called");
    }

    @Override // com.google.common.collect.ImmutableMap
    public final ImmutableCollection<V> createValues() {
        throw new AssertionError("should never be called");
    }

    @Override // java.util.NavigableMap
    public final NavigableSet descendingKeySet() {
        RegularImmutableSortedSet<K> regularImmutableSortedSet = this.keySet;
        Objects.requireNonNull(regularImmutableSortedSet);
        ImmutableSortedSet<K> immutableSortedSet = regularImmutableSortedSet.descendingSet;
        if (immutableSortedSet != null) {
            return immutableSortedSet;
        }
        ImmutableSortedSet immutableSortedSet2 = (ImmutableSortedSet<K>) regularImmutableSortedSet.createDescendingSet();
        regularImmutableSortedSet.descendingSet = immutableSortedSet2;
        immutableSortedSet2.descendingSet = regularImmutableSortedSet;
        return immutableSortedSet2;
    }

    @Override // java.util.NavigableMap
    public final NavigableMap descendingMap() {
        Ordering ordering;
        ImmutableSortedMap<K, V> immutableSortedMap = this.descendingMap;
        if (immutableSortedMap == null) {
            if (isEmpty()) {
                Comparator<? super K> comparator = comparator();
                if (comparator instanceof Ordering) {
                    ordering = (Ordering) comparator;
                } else {
                    ordering = new ComparatorOrdering(comparator);
                }
                return emptyMap(ordering.reverse());
            }
            RegularImmutableSortedSet<K> regularImmutableSortedSet = this.keySet;
            Objects.requireNonNull(regularImmutableSortedSet);
            ImmutableSortedSet immutableSortedSet = (ImmutableSortedSet<K>) regularImmutableSortedSet.descendingSet;
            ImmutableSortedSet immutableSortedSet2 = immutableSortedSet;
            if (immutableSortedSet == null) {
                ImmutableSortedSet immutableSortedSet3 = (ImmutableSortedSet<K>) regularImmutableSortedSet.createDescendingSet();
                regularImmutableSortedSet.descendingSet = immutableSortedSet3;
                immutableSortedSet3.descendingSet = regularImmutableSortedSet;
                immutableSortedSet2 = immutableSortedSet3;
            }
            immutableSortedMap = new ImmutableSortedMap<>((RegularImmutableSortedSet) immutableSortedSet2, this.valueList.reverse(), this);
        }
        return immutableSortedMap;
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    /* renamed from: entrySet  reason: collision with other method in class */
    public final Set mo175entrySet() {
        return super.entrySet();
    }

    @Override // java.util.SortedMap
    public final K firstKey() {
        return this.keySet.first();
    }

    /* JADX WARN: Code restructure failed: missing block: B:5:0x0011, code lost:
        if (r4 >= 0) goto L_0x0015;
     */
    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final V get(java.lang.Object r4) {
        /*
            r3 = this;
            com.google.common.collect.RegularImmutableSortedSet<K> r0 = r3.keySet
            java.util.Objects.requireNonNull(r0)
            r1 = -1
            if (r4 != 0) goto L_0x0009
            goto L_0x0014
        L_0x0009:
            com.google.common.collect.ImmutableList<E> r2 = r0.elements     // Catch: ClassCastException -> 0x0014
            java.util.Comparator<? super E> r0 = r0.comparator     // Catch: ClassCastException -> 0x0014
            int r4 = java.util.Collections.binarySearch(r2, r4, r0)     // Catch: ClassCastException -> 0x0014
            if (r4 < 0) goto L_0x0014
            goto L_0x0015
        L_0x0014:
            r4 = r1
        L_0x0015:
            if (r4 != r1) goto L_0x0019
            r3 = 0
            goto L_0x001f
        L_0x0019:
            com.google.common.collect.ImmutableList<V> r3 = r3.valueList
            java.lang.Object r3 = r3.get(r4)
        L_0x001f:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.ImmutableSortedMap.get(java.lang.Object):java.lang.Object");
    }

    public final ImmutableSortedMap<K, V> getSubMap(int i, int i2) {
        if (i == 0 && i2 == size()) {
            return this;
        }
        if (i == i2) {
            return emptyMap(comparator());
        }
        return new ImmutableSortedMap<>(this.keySet.getSubSet(i, i2), this.valueList.subList(i, i2), null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap, java.util.SortedMap
    public final SortedMap headMap(Object obj) {
        return headMap((ImmutableSortedMap<K, V>) obj, false);
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    /* renamed from: keySet  reason: collision with other method in class */
    public final Set mo176keySet() {
        return this.keySet;
    }

    @Override // java.util.SortedMap
    public final K lastKey() {
        return this.keySet.last();
    }

    @Override // java.util.NavigableMap
    @CanIgnoreReturnValue
    @DoNotCall("Always throws UnsupportedOperationException")
    @Deprecated
    public final Map.Entry<K, V> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.NavigableMap
    @CanIgnoreReturnValue
    @DoNotCall("Always throws UnsupportedOperationException")
    @Deprecated
    public final Map.Entry<K, V> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    public final int size() {
        return this.valueList.size();
    }

    @Override // java.util.NavigableMap
    public final ImmutableSortedMap<K, V> subMap(K k, boolean z, K k2, boolean z2) {
        Objects.requireNonNull(k);
        Objects.requireNonNull(k2);
        if (comparator().compare(k, k2) <= 0) {
            return headMap((ImmutableSortedMap<K, V>) k2, z2).tailMap((ImmutableSortedMap<K, V>) k, z);
        }
        throw new IllegalArgumentException(Strings.lenientFormat("expected fromKey <= toKey but %s > %s", k, k2));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap, java.util.SortedMap
    public final SortedMap tailMap(Object obj) {
        return tailMap((ImmutableSortedMap<K, V>) obj, true);
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    /* renamed from: values  reason: collision with other method in class */
    public final Collection mo177values() {
        return this.valueList;
    }

    @Override // com.google.common.collect.ImmutableMap
    public Object writeReplace() {
        return new SerializedForm(this);
    }

    public ImmutableSortedMap(RegularImmutableSortedSet<K> regularImmutableSortedSet, ImmutableList<V> immutableList, ImmutableSortedMap<K, V> immutableSortedMap) {
        this.keySet = regularImmutableSortedSet;
        this.valueList = immutableList;
        this.descendingMap = immutableSortedMap;
    }

    @Override // com.google.common.collect.ImmutableMap
    public final ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        if (!isEmpty()) {
            return new ImmutableMapEntrySet<K, V>() { // from class: com.google.common.collect.ImmutableSortedMap.1EntrySet
                @Override // com.google.common.collect.ImmutableSet
                public final ImmutableList<Map.Entry<K, V>> createAsList() {
                    return new ImmutableList<Map.Entry<K, V>>() { // from class: com.google.common.collect.ImmutableSortedMap.1EntrySet.1
                        @Override // java.util.List
                        public final Object get(int i) {
                            RegularImmutableSortedSet<K> regularImmutableSortedSet = ImmutableSortedMap.this.keySet;
                            Objects.requireNonNull(regularImmutableSortedSet);
                            return new AbstractMap.SimpleImmutableEntry(regularImmutableSortedSet.elements.get(i), ImmutableSortedMap.this.valueList.get(i));
                        }

                        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                        public final int size() {
                            return ImmutableSortedMap.this.size();
                        }
                    };
                }

                @Override // com.google.common.collect.ImmutableMapEntrySet, com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
                /* renamed from: iterator */
                public final UnmodifiableIterator<Map.Entry<K, V>> mo174iterator() {
                    ImmutableList<Map.Entry<K, V>> asList = asList();
                    Objects.requireNonNull(asList);
                    return asList.listIterator(0);
                }

                @Override // com.google.common.collect.ImmutableMapEntrySet
                public final ImmutableSortedMap map() {
                    return ImmutableSortedMap.this;
                }
            };
        }
        int i = ImmutableSet.$r8$clinit;
        return RegularImmutableSet.EMPTY;
    }

    @Override // java.util.NavigableMap
    public final Map.Entry<K, V> firstEntry() {
        if (isEmpty()) {
            return null;
        }
        return super.entrySet().asList().get(0);
    }

    @Override // java.util.NavigableMap
    public final ImmutableSortedMap<K, V> headMap(K k, boolean z) {
        RegularImmutableSortedSet<K> regularImmutableSortedSet = this.keySet;
        Objects.requireNonNull(k);
        return getSubMap(0, regularImmutableSortedSet.headIndex(k, z));
    }

    @Override // java.util.NavigableMap
    public final Map.Entry<K, V> lastEntry() {
        if (isEmpty()) {
            return null;
        }
        return super.entrySet().asList().get(size() - 1);
    }

    @Override // java.util.NavigableMap
    public final ImmutableSortedMap<K, V> tailMap(K k, boolean z) {
        RegularImmutableSortedSet<K> regularImmutableSortedSet = this.keySet;
        Objects.requireNonNull(k);
        return getSubMap(regularImmutableSortedSet.tailIndex(k, z), size());
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap, java.util.SortedMap
    public final SortedMap subMap(Object obj, Object obj2) {
        return subMap((boolean) obj, true, (boolean) obj2, false);
    }

    @Override // java.util.NavigableMap
    public final NavigableSet navigableKeySet() {
        return this.keySet;
    }
}
