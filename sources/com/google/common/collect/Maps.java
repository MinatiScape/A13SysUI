package com.google.common.collect;

import androidx.recyclerview.R$dimen;
import com.google.common.collect.AbstractMapBasedMultimap;
import com.google.common.collect.AbstractNavigableMap;
import com.google.common.collect.Sets;
import com.google.common.collect.TreeRangeSet$RangesByUpperBound;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
/* loaded from: classes.dex */
public final class Maps {

    /* loaded from: classes.dex */
    public static abstract class DescendingMap<K, V> extends ForwardingMap<K, V> implements NavigableMap<K, V> {
        public transient Ordering comparator;
        public transient C1EntrySetImpl entrySet;
        public transient NavigableKeySet navigableKeySet;

        @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
        /* renamed from: delegate */
        public final Object mo179delegate() {
            return AbstractNavigableMap.this;
        }

        @Override // java.util.NavigableMap
        public final NavigableMap<K, V> headMap(K k, boolean z) {
            return AbstractNavigableMap.this.tailMap(k, z).descendingMap();
        }

        @Override // java.util.NavigableMap
        public final NavigableMap<K, V> subMap(K k, boolean z, K k2, boolean z2) {
            return AbstractNavigableMap.this.subMap(k2, z2, k, z).descendingMap();
        }

        @Override // java.util.NavigableMap
        public final NavigableMap<K, V> tailMap(K k, boolean z) {
            return AbstractNavigableMap.this.headMap(k, z).descendingMap();
        }

        @Override // java.util.NavigableMap
        public final Map.Entry<K, V> ceilingEntry(K k) {
            return AbstractNavigableMap.this.floorEntry(k);
        }

        @Override // java.util.NavigableMap
        public final K ceilingKey(K k) {
            return AbstractNavigableMap.this.floorKey(k);
        }

        @Override // java.util.SortedMap
        public final Comparator<? super K> comparator() {
            Ordering ordering;
            Ordering ordering2 = this.comparator;
            if (ordering2 != null) {
                return ordering2;
            }
            Comparator<? super K> comparator = AbstractNavigableMap.this.comparator();
            if (comparator == null) {
                comparator = NaturalOrdering.INSTANCE;
            }
            if (comparator instanceof Ordering) {
                ordering = (Ordering) comparator;
            } else {
                ordering = new ComparatorOrdering(comparator);
            }
            Ordering reverse = ordering.reverse();
            this.comparator = reverse;
            return reverse;
        }

        @Override // java.util.NavigableMap
        public final NavigableSet<K> descendingKeySet() {
            return AbstractNavigableMap.this.navigableKeySet();
        }

        @Override // java.util.NavigableMap
        public final NavigableMap<K, V> descendingMap() {
            return AbstractNavigableMap.this;
        }

        /* JADX WARN: Type inference failed for: r0v1, types: [com.google.common.collect.Maps$DescendingMap$1EntrySetImpl, java.util.Set<java.util.Map$Entry<K, V>>] */
        @Override // com.google.common.collect.ForwardingMap, java.util.Map
        public final Set<Map.Entry<K, V>> entrySet() {
            C1EntrySetImpl r0 = this.entrySet;
            if (r0 != null) {
                return r0;
            }
            EntrySet<Object, Object> entrySet = new EntrySet<Object, Object>() { // from class: com.google.common.collect.Maps.DescendingMap.1EntrySetImpl
                @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
                public final Iterator<Map.Entry<Object, Object>> iterator() {
                    AbstractNavigableMap.DescendingMap descendingMap = (AbstractNavigableMap.DescendingMap) DescendingMap.this;
                    Objects.requireNonNull(descendingMap);
                    return AbstractNavigableMap.this.descendingEntryIterator();
                }

                @Override // com.google.common.collect.Maps.EntrySet
                public final Map<Object, Object> map() {
                    return DescendingMap.this;
                }
            };
            this.entrySet = entrySet;
            return entrySet;
        }

        @Override // java.util.NavigableMap
        public final Map.Entry<K, V> firstEntry() {
            return AbstractNavigableMap.this.lastEntry();
        }

        @Override // java.util.SortedMap
        public final K firstKey() {
            return AbstractNavigableMap.this.lastKey();
        }

        @Override // java.util.NavigableMap
        public final Map.Entry<K, V> floorEntry(K k) {
            return AbstractNavigableMap.this.ceilingEntry(k);
        }

        @Override // java.util.NavigableMap
        public final K floorKey(K k) {
            return AbstractNavigableMap.this.ceilingKey(k);
        }

        @Override // java.util.NavigableMap
        public final Map.Entry<K, V> higherEntry(K k) {
            return AbstractNavigableMap.this.lowerEntry(k);
        }

        @Override // java.util.NavigableMap
        public final K higherKey(K k) {
            return AbstractNavigableMap.this.lowerKey(k);
        }

        @Override // java.util.NavigableMap
        public final Map.Entry<K, V> lastEntry() {
            return AbstractNavigableMap.this.firstEntry();
        }

        @Override // java.util.SortedMap
        public final K lastKey() {
            return AbstractNavigableMap.this.firstKey();
        }

        @Override // java.util.NavigableMap
        public final Map.Entry<K, V> lowerEntry(K k) {
            return AbstractNavigableMap.this.higherEntry(k);
        }

        @Override // java.util.NavigableMap
        public final K lowerKey(K k) {
            return AbstractNavigableMap.this.higherKey(k);
        }

        @Override // java.util.NavigableMap
        public final NavigableSet<K> navigableKeySet() {
            NavigableKeySet navigableKeySet = this.navigableKeySet;
            if (navigableKeySet != null) {
                return navigableKeySet;
            }
            NavigableKeySet navigableKeySet2 = new NavigableKeySet(this);
            this.navigableKeySet = navigableKeySet2;
            return navigableKeySet2;
        }

        @Override // java.util.NavigableMap
        public final Map.Entry<K, V> pollFirstEntry() {
            return AbstractNavigableMap.this.pollLastEntry();
        }

        @Override // java.util.NavigableMap
        public final Map.Entry<K, V> pollLastEntry() {
            return AbstractNavigableMap.this.pollFirstEntry();
        }

        @Override // com.google.common.collect.ForwardingMap, java.util.Map
        public final Collection<V> values() {
            return new Values(this);
        }

        @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
        /* renamed from: delegate  reason: collision with other method in class */
        public final Map<K, V> mo179delegate() {
            return AbstractNavigableMap.this;
        }

        @Override // com.google.common.collect.ForwardingMap, java.util.Map
        public final Set<K> keySet() {
            return navigableKeySet();
        }

        @Override // com.google.common.collect.ForwardingObject
        public final String toString() {
            return Maps.toStringImpl(this);
        }

        @Override // java.util.NavigableMap, java.util.SortedMap
        public final SortedMap<K, V> headMap(K k) {
            return headMap(k, false);
        }

        @Override // java.util.NavigableMap, java.util.SortedMap
        public final SortedMap<K, V> subMap(K k, K k2) {
            return subMap(k, true, k2, false);
        }

        @Override // java.util.NavigableMap, java.util.SortedMap
        public final SortedMap<K, V> tailMap(K k) {
            return tailMap(k, true);
        }
    }

    /* loaded from: classes.dex */
    public static abstract class EntrySet<K, V> extends Sets.ImprovedAbstractSet<Map.Entry<K, V>> {
        public abstract Map<K, V> map();

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            V v;
            if (obj instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) obj;
                Object key = entry.getKey();
                Map<K, V> map = map();
                Objects.requireNonNull(map);
                try {
                    v = map.get(key);
                } catch (ClassCastException | NullPointerException unused) {
                    v = null;
                }
                if (R$dimen.equal(v, entry.getValue()) && (v != null || map().containsKey(key))) {
                    return true;
                }
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final void clear() {
            map().clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean isEmpty() {
            return map().isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            if (!contains(obj) || !(obj instanceof Map.Entry)) {
                return false;
            }
            return map().keySet().remove(((Map.Entry) obj).getKey());
        }

        @Override // com.google.common.collect.Sets.ImprovedAbstractSet, java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean removeAll(Collection<?> collection) {
            try {
                Objects.requireNonNull(collection);
                return Sets.removeAllImpl(this, collection);
            } catch (UnsupportedOperationException unused) {
                Iterator<?> it = collection.iterator();
                boolean z = false;
                while (it.hasNext()) {
                    z |= this.remove(it.next());
                }
                return z;
            }
        }

        @Override // com.google.common.collect.Sets.ImprovedAbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean retainAll(Collection<?> collection) {
            int i;
            try {
                Objects.requireNonNull(collection);
                return super.retainAll(collection);
            } catch (UnsupportedOperationException unused) {
                int size = collection.size();
                if (size < 3) {
                    CollectPreconditions.checkNonnegative(size, "expectedSize");
                    i = size + 1;
                } else if (size < 1073741824) {
                    i = (int) ((size / 0.75f) + 1.0f);
                } else {
                    i = Integer.MAX_VALUE;
                }
                HashSet hashSet = new HashSet(i);
                for (Object obj : collection) {
                    if (this.contains(obj) && (obj instanceof Map.Entry)) {
                        hashSet.add(((Map.Entry) obj).getKey());
                    }
                }
                return this.map().keySet().retainAll(hashSet);
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final int size() {
            return map().size();
        }
    }

    /* loaded from: classes.dex */
    public static abstract class IteratorBasedAbstractMap<K, V> extends AbstractMap<K, V> {
        public abstract TreeRangeSet$RangesByUpperBound.AnonymousClass1 entryIterator();

        @Override // java.util.AbstractMap, java.util.Map
        public final Set<Map.Entry<K, V>> entrySet() {
            return new EntrySet<K, V>() { // from class: com.google.common.collect.Maps.IteratorBasedAbstractMap.1
                @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
                public final Iterator<Map.Entry<K, V>> iterator() {
                    return IteratorBasedAbstractMap.this.entryIterator();
                }

                @Override // com.google.common.collect.Maps.EntrySet
                public final Map<K, V> map() {
                    return IteratorBasedAbstractMap.this;
                }
            };
        }

        @Override // java.util.AbstractMap, java.util.Map
        public final void clear() {
            Iterators.clear(entryIterator());
        }
    }

    /* loaded from: classes.dex */
    public static class NavigableKeySet<K, V> extends SortedKeySet<K, V> implements NavigableSet<K> {
        @Override // java.util.NavigableSet
        public final NavigableSet<K> headSet(K k, boolean z) {
            return ((NavigableMap) this.map).headMap(k, z).navigableKeySet();
        }

        @Override // java.util.NavigableSet
        public final NavigableSet<K> subSet(K k, boolean z, K k2, boolean z2) {
            return ((NavigableMap) this.map).subMap(k, z, k2, z2).navigableKeySet();
        }

        @Override // java.util.NavigableSet
        public final NavigableSet<K> tailSet(K k, boolean z) {
            return ((NavigableMap) this.map).tailMap(k, z).navigableKeySet();
        }

        @Override // java.util.NavigableSet
        public final K ceiling(K k) {
            return (K) ((NavigableMap) this.map).ceilingKey(k);
        }

        @Override // java.util.NavigableSet
        public final NavigableSet<K> descendingSet() {
            return ((NavigableMap) this.map).descendingKeySet();
        }

        @Override // java.util.NavigableSet
        public final K floor(K k) {
            return (K) ((NavigableMap) this.map).floorKey(k);
        }

        @Override // java.util.NavigableSet
        public final K higher(K k) {
            return (K) ((NavigableMap) this.map).higherKey(k);
        }

        @Override // java.util.NavigableSet
        public final K lower(K k) {
            return (K) ((NavigableMap) this.map).lowerKey(k);
        }

        @Override // com.google.common.collect.Maps.KeySet
        public final Map map() {
            return (NavigableMap) this.map;
        }

        @Override // java.util.NavigableSet
        public final K pollFirst() {
            return (K) Maps.keyOrNull(((NavigableMap) this.map).pollFirstEntry());
        }

        @Override // java.util.NavigableSet
        public final K pollLast() {
            return (K) Maps.keyOrNull(((NavigableMap) this.map).pollLastEntry());
        }

        @Override // java.util.NavigableSet
        public final Iterator<K> descendingIterator() {
            return descendingSet().iterator();
        }

        @Override // java.util.SortedSet, java.util.NavigableSet
        public final SortedSet<K> headSet(K k) {
            return headSet(k, false);
        }

        @Override // java.util.SortedSet, java.util.NavigableSet
        public final SortedSet<K> subSet(K k, K k2) {
            return subSet(k, true, k2, false);
        }

        @Override // java.util.SortedSet, java.util.NavigableSet
        public final SortedSet<K> tailSet(K k) {
            return tailSet(k, true);
        }

        public NavigableKeySet(NavigableMap<K, V> navigableMap) {
            super(navigableMap);
        }
    }

    /* loaded from: classes.dex */
    public static class SortedKeySet<K, V> extends KeySet<K, V> implements SortedSet<K> {
        @Override // java.util.SortedSet
        public final Comparator<? super K> comparator() {
            return ((NavigableMap) ((NavigableKeySet) this).map).comparator();
        }

        @Override // java.util.SortedSet
        public final K first() {
            return (K) ((NavigableMap) ((NavigableKeySet) this).map).firstKey();
        }

        @Override // java.util.SortedSet
        public final K last() {
            return (K) ((NavigableMap) ((NavigableKeySet) this).map).lastKey();
        }

        public SortedKeySet(SortedMap<K, V> sortedMap) {
            super(sortedMap);
        }
    }

    /* loaded from: classes.dex */
    public static class Values<K, V> extends AbstractCollection<V> {
        public final Map<K, V> map;

        @Override // java.util.AbstractCollection, java.util.Collection
        public final void clear() {
            this.map.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public final boolean contains(Object obj) {
            return this.map.containsValue(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public final boolean isEmpty() {
            return this.map.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public final Iterator<V> iterator() {
            return new TransformedIterator<Map.Entry<Object, Object>, Object>(this.map.entrySet().iterator()) { // from class: com.google.common.collect.Maps.2
                @Override // com.google.common.collect.TransformedIterator
                public final Object transform(Map.Entry<Object, Object> entry) {
                    return entry.getValue();
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public final int size() {
            return this.map.size();
        }

        public Values(Map<K, V> map) {
            Objects.requireNonNull(map);
            this.map = map;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public final boolean remove(Object obj) {
            try {
                return super.remove(obj);
            } catch (UnsupportedOperationException unused) {
                for (Map.Entry<K, V> entry : this.map.entrySet()) {
                    if (R$dimen.equal(obj, entry.getValue())) {
                        this.map.remove(entry.getKey());
                        return true;
                    }
                }
                return false;
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public final boolean removeAll(Collection<?> collection) {
            try {
                Objects.requireNonNull(collection);
                return super.removeAll(collection);
            } catch (UnsupportedOperationException unused) {
                HashSet hashSet = new HashSet();
                for (Map.Entry<K, V> entry : this.map.entrySet()) {
                    if (collection.contains(entry.getValue())) {
                        hashSet.add(entry.getKey());
                    }
                }
                return this.map.keySet().removeAll(hashSet);
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public final boolean retainAll(Collection<?> collection) {
            try {
                Objects.requireNonNull(collection);
                return super.retainAll(collection);
            } catch (UnsupportedOperationException unused) {
                HashSet hashSet = new HashSet();
                for (Map.Entry<K, V> entry : this.map.entrySet()) {
                    if (collection.contains(entry.getValue())) {
                        hashSet.add(entry.getKey());
                    }
                }
                return this.map.keySet().retainAll(hashSet);
            }
        }
    }

    /* loaded from: classes.dex */
    public static abstract class ViewCachingAbstractMap<K, V> extends AbstractMap<K, V> {
        public transient AbstractMapBasedMultimap.AsMap.AsMapEntries entrySet;
        public transient Values values;

        @Override // java.util.AbstractMap, java.util.Map
        public final Set<Map.Entry<K, V>> entrySet() {
            AbstractMapBasedMultimap.AsMap.AsMapEntries asMapEntries = this.entrySet;
            if (asMapEntries != null) {
                return asMapEntries;
            }
            AbstractMapBasedMultimap.AsMap.AsMapEntries asMapEntries2 = new AbstractMapBasedMultimap.AsMap.AsMapEntries();
            this.entrySet = asMapEntries2;
            return asMapEntries2;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public final Collection<V> values() {
            Values values = this.values;
            if (values != null) {
                return values;
            }
            Values values2 = new Values(this);
            this.values = values2;
            return values2;
        }
    }

    /* loaded from: classes.dex */
    public static class KeySet<K, V> extends Sets.ImprovedAbstractSet<K> {
        public final Map<K, V> map;

        public KeySet(Map<K, V> map) {
            Objects.requireNonNull(map);
            this.map = map;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            map().clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean contains(Object obj) {
            return map().containsKey(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean isEmpty() {
            return map().isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return new TransformedIterator<Map.Entry<Object, Object>, Object>(map().entrySet().iterator()) { // from class: com.google.common.collect.Maps.1
                @Override // com.google.common.collect.TransformedIterator
                public final Object transform(Map.Entry<Object, Object> entry) {
                    return entry.getKey();
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            if (!contains(obj)) {
                return false;
            }
            map().remove(obj);
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final int size() {
            return map().size();
        }

        public Map<K, V> map() {
            return this.map;
        }
    }

    public static <K> K keyOrNull(Map.Entry<K, ?> entry) {
        if (entry == null) {
            return null;
        }
        return entry.getKey();
    }

    public static String toStringImpl(Map<?, ?> map) {
        int size = map.size();
        CollectPreconditions.checkNonnegative(size, "size");
        StringBuilder sb = new StringBuilder((int) Math.min(size * 8, 1073741824L));
        sb.append('{');
        boolean z = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!z) {
                sb.append(", ");
            }
            z = false;
            sb.append(entry.getKey());
            sb.append('=');
            sb.append(entry.getValue());
        }
        sb.append('}');
        return sb.toString();
    }
}
