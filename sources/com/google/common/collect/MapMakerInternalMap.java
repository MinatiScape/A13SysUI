package com.google.common.collect;

import androidx.leanback.R$layout;
import com.google.common.base.Equivalence;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.MapMakerInternalMap.InternalEntry;
import com.google.common.collect.MapMakerInternalMap.Segment;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.concurrent.GuardedBy;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class MapMakerInternalMap<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, Serializable {
    public static final AnonymousClass1 UNSET_WEAK_VALUE_REFERENCE = new WeakValueReference<Object, Object, DummyInternalEntry>() { // from class: com.google.common.collect.MapMakerInternalMap.1
        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueReference
        public final void clear() {
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueReference
        public final Object get() {
            return null;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueReference
        public final /* bridge */ /* synthetic */ DummyInternalEntry getEntry() {
            return null;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueReference
        public final WeakValueReference copyFor(ReferenceQueue referenceQueue, WeakValueEntry weakValueEntry) {
            DummyInternalEntry dummyInternalEntry = (DummyInternalEntry) weakValueEntry;
            return this;
        }
    };
    private static final long serialVersionUID = 5;
    public final int concurrencyLevel;
    public final transient InternalEntryHelper<K, V, E, S> entryHelper;
    public transient EntrySet entrySet;
    public final Equivalence<Object> keyEquivalence;
    public transient KeySet keySet;
    public final transient int segmentMask;
    public final transient int segmentShift;
    public final transient Segment<K, V, E, S>[] segments;
    public transient Values values;

    /* loaded from: classes.dex */
    public static abstract class AbstractSerializationProxy<K, V> extends ForwardingConcurrentMap<K, V> implements Serializable {
        private static final long serialVersionUID = 3;
        public final int concurrencyLevel;
        public transient ConcurrentMap<K, V> delegate;
        public final Equivalence<Object> keyEquivalence;
        public final Strength keyStrength;
        public final Equivalence<Object> valueEquivalence;
        public final Strength valueStrength;

        @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
        /* renamed from: delegate */
        public final Object mo179delegate() {
            return this.delegate;
        }

        @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
        /* renamed from: delegate  reason: collision with other method in class */
        public final Map mo179delegate() {
            return this.delegate;
        }

        public AbstractSerializationProxy(Strength strength, Strength strength2, Equivalence<Object> equivalence, Equivalence<Object> equivalence2, int i, ConcurrentMap<K, V> concurrentMap) {
            this.keyStrength = strength;
            this.valueStrength = strength2;
            this.keyEquivalence = equivalence;
            this.valueEquivalence = equivalence2;
            this.concurrencyLevel = i;
            this.delegate = concurrentMap;
        }
    }

    /* loaded from: classes.dex */
    public static final class DummyInternalEntry implements InternalEntry<Object, Object, DummyInternalEntry> {
        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final int getHash() {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final Object getKey() {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final DummyInternalEntry getNext() {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final Object getValue() {
            throw new AssertionError();
        }

        public DummyInternalEntry() {
            throw new AssertionError();
        }
    }

    /* loaded from: classes.dex */
    public final class EntrySet extends SafeToArraySet<Map.Entry<K, V>> {
        public EntrySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final void clear() {
            MapMakerInternalMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean contains(Object obj) {
            Map.Entry entry;
            Object key;
            Object obj2;
            if ((obj instanceof Map.Entry) && (key = (entry = (Map.Entry) obj).getKey()) != null && (obj2 = MapMakerInternalMap.this.get(key)) != null && MapMakerInternalMap.this.valueEquivalence().equivalent(entry.getValue(), obj2)) {
                return true;
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public final Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator(MapMakerInternalMap.this);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean remove(Object obj) {
            Map.Entry entry;
            Object key;
            if ((obj instanceof Map.Entry) && (key = (entry = (Map.Entry) obj).getKey()) != null && MapMakerInternalMap.this.remove(key, entry.getValue())) {
                return true;
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final int size() {
            return MapMakerInternalMap.this.size();
        }
    }

    /* loaded from: classes.dex */
    public abstract class HashIterator<T> implements Iterator<T> {
        public Segment<K, V, E, S> currentSegment;
        public AtomicReferenceArray<E> currentTable;
        public MapMakerInternalMap<K, V, E, S>.WriteThroughEntry lastReturned;
        public E nextEntry;
        public MapMakerInternalMap<K, V, E, S>.WriteThroughEntry nextExternal;
        public int nextSegmentIndex;
        public int nextTableIndex = -1;

        public final void advance() {
            boolean z;
            this.nextExternal = null;
            E e = this.nextEntry;
            if (e != null) {
                while (true) {
                    E e2 = (E) e.getNext();
                    this.nextEntry = e2;
                    if (e2 == null) {
                        break;
                    } else if (advanceTo(e2)) {
                        z = true;
                        break;
                    } else {
                        e = this.nextEntry;
                    }
                }
            }
            z = false;
            if (!z && !nextInTable()) {
                while (true) {
                    int i = this.nextSegmentIndex;
                    if (i >= 0) {
                        Segment<K, V, E, S>[] segmentArr = MapMakerInternalMap.this.segments;
                        this.nextSegmentIndex = i - 1;
                        Segment<K, V, E, S> segment = segmentArr[i];
                        this.currentSegment = segment;
                        if (segment.count != 0) {
                            AtomicReferenceArray<E> atomicReferenceArray = this.currentSegment.table;
                            this.currentTable = atomicReferenceArray;
                            this.nextTableIndex = atomicReferenceArray.length() - 1;
                            if (nextInTable()) {
                                return;
                            }
                        }
                    } else {
                        return;
                    }
                }
            }
        }

        public HashIterator() {
            this.nextSegmentIndex = MapMakerInternalMap.this.segments.length - 1;
            advance();
        }

        @Override // java.util.Iterator
        public final boolean hasNext() {
            if (this.nextExternal != null) {
                return true;
            }
            return false;
        }

        public final MapMakerInternalMap<K, V, E, S>.WriteThroughEntry nextEntry() {
            MapMakerInternalMap<K, V, E, S>.WriteThroughEntry writeThroughEntry = this.nextExternal;
            if (writeThroughEntry != null) {
                this.lastReturned = writeThroughEntry;
                advance();
                return this.lastReturned;
            }
            throw new NoSuchElementException();
        }

        public final boolean nextInTable() {
            while (true) {
                int i = this.nextTableIndex;
                boolean z = false;
                if (i < 0) {
                    return false;
                }
                AtomicReferenceArray<E> atomicReferenceArray = this.currentTable;
                this.nextTableIndex = i - 1;
                E e = atomicReferenceArray.get(i);
                this.nextEntry = e;
                if (e != null) {
                    if (advanceTo(e)) {
                        break;
                    }
                    E e2 = this.nextEntry;
                    if (e2 != null) {
                        while (true) {
                            E e3 = (E) e2.getNext();
                            this.nextEntry = e3;
                            if (e3 == null) {
                                break;
                            } else if (advanceTo(e3)) {
                                z = true;
                                break;
                            } else {
                                e2 = this.nextEntry;
                            }
                        }
                    }
                    if (z) {
                        break;
                    }
                }
            }
            return true;
        }

        @Override // java.util.Iterator
        public final void remove() {
            boolean z;
            if (this.lastReturned != null) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkState(z, "no calls to next() since the last call to remove()");
            MapMakerInternalMap mapMakerInternalMap = MapMakerInternalMap.this;
            MapMakerInternalMap<K, V, E, S>.WriteThroughEntry writeThroughEntry = this.lastReturned;
            Objects.requireNonNull(writeThroughEntry);
            mapMakerInternalMap.remove(writeThroughEntry.key);
            this.lastReturned = null;
        }

        public final boolean advanceTo(E e) {
            Object obj;
            boolean z;
            try {
                Object key = e.getKey();
                Objects.requireNonNull(MapMakerInternalMap.this);
                if (e.getKey() == null) {
                    obj = null;
                } else {
                    obj = e.getValue();
                }
                if (obj != null) {
                    this.nextExternal = new WriteThroughEntry(key, obj);
                    z = true;
                } else {
                    z = false;
                }
                return z;
            } finally {
                this.currentSegment.postReadCleanup();
            }
        }
    }

    /* loaded from: classes.dex */
    public interface InternalEntry<K, V, E extends InternalEntry<K, V, E>> {
        int getHash();

        K getKey();

        E getNext();

        V getValue();
    }

    /* loaded from: classes.dex */
    public interface InternalEntryHelper<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>> {
        E copy(S s, E e, E e2);

        Strength keyStrength();

        E newEntry(S s, K k, int i, E e);

        Segment newSegment(MapMakerInternalMap mapMakerInternalMap, int i);

        void setValue(S s, E e, V v);

        Strength valueStrength();
    }

    /* loaded from: classes.dex */
    public final class KeySet extends SafeToArraySet<K> {
        public KeySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final void clear() {
            MapMakerInternalMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean contains(Object obj) {
            return MapMakerInternalMap.this.containsKey(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public final Iterator<K> iterator() {
            return new KeyIterator(MapMakerInternalMap.this);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean remove(Object obj) {
            if (MapMakerInternalMap.this.remove(obj) != null) {
                return true;
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final int size() {
            return MapMakerInternalMap.this.size();
        }
    }

    /* loaded from: classes.dex */
    public static abstract class SafeToArraySet<E> extends AbstractSet<E> {
        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final Object[] toArray() {
            return MapMakerInternalMap.access$900(this).toArray();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final <T> T[] toArray(T[] tArr) {
            return (T[]) MapMakerInternalMap.access$900(this).toArray(tArr);
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Segment<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>> extends ReentrantLock {
        public static final /* synthetic */ int $r8$clinit = 0;
        public volatile int count;
        public final MapMakerInternalMap<K, V, E, S> map;
        public int modCount;
        public volatile AtomicReferenceArray<E> table;
        public int threshold;
        public final AtomicInteger readCount = new AtomicInteger();
        public final int maxSegmentSize = -1;

        public abstract E castForTesting(InternalEntry<K, V, ?> internalEntry);

        /* JADX WARN: Finally extract failed */
        @GuardedBy("this")
        public final void drainKeyReferenceQueue(ReferenceQueue<K> referenceQueue) {
            int i = 0;
            do {
                Reference<? extends K> poll = referenceQueue.poll();
                if (poll != null) {
                    InternalEntry internalEntry = (InternalEntry) poll;
                    MapMakerInternalMap<K, V, E, S> mapMakerInternalMap = this.map;
                    Objects.requireNonNull(mapMakerInternalMap);
                    int hash = internalEntry.getHash();
                    Segment<K, V, E, S> segmentFor = mapMakerInternalMap.segmentFor(hash);
                    Objects.requireNonNull(segmentFor);
                    segmentFor.lock();
                    try {
                        AtomicReferenceArray<E> atomicReferenceArray = segmentFor.table;
                        int length = hash & (atomicReferenceArray.length() - 1);
                        E e = atomicReferenceArray.get(length);
                        InternalEntry internalEntry2 = e;
                        while (true) {
                            if (internalEntry2 == null) {
                                break;
                            } else if (internalEntry2 == internalEntry) {
                                segmentFor.modCount++;
                                atomicReferenceArray.set(length, segmentFor.removeFromChain(e, internalEntry2));
                                segmentFor.count--;
                                break;
                            } else {
                                internalEntry2 = internalEntry2.getNext();
                            }
                        }
                        segmentFor.unlock();
                        i++;
                    } catch (Throwable th) {
                        segmentFor.unlock();
                        throw th;
                    }
                } else {
                    return;
                }
            } while (i != 16);
        }

        /* JADX WARN: Finally extract failed */
        @GuardedBy("this")
        public final void drainValueReferenceQueue(ReferenceQueue<V> referenceQueue) {
            int i = 0;
            do {
                Reference<? extends V> poll = referenceQueue.poll();
                if (poll != null) {
                    WeakValueReference<K, V, E> weakValueReference = (WeakValueReference) poll;
                    MapMakerInternalMap<K, V, E, S> mapMakerInternalMap = this.map;
                    Objects.requireNonNull(mapMakerInternalMap);
                    E entry = weakValueReference.getEntry();
                    int hash = entry.getHash();
                    Segment<K, V, E, S> segmentFor = mapMakerInternalMap.segmentFor(hash);
                    Object key = entry.getKey();
                    Objects.requireNonNull(segmentFor);
                    segmentFor.lock();
                    try {
                        AtomicReferenceArray<E> atomicReferenceArray = segmentFor.table;
                        int length = (atomicReferenceArray.length() - 1) & hash;
                        E e = atomicReferenceArray.get(length);
                        E e2 = e;
                        while (true) {
                            if (e2 == null) {
                                break;
                            }
                            Object key2 = e2.getKey();
                            if (e2.getHash() != hash || key2 == null || !segmentFor.map.keyEquivalence.equivalent(key, key2)) {
                                e2 = (E) e2.getNext();
                            } else if (((WeakValueEntry) e2).getValueReference() == weakValueReference) {
                                segmentFor.modCount++;
                                atomicReferenceArray.set(length, segmentFor.removeFromChain(e, e2));
                                segmentFor.count--;
                            }
                        }
                        segmentFor.unlock();
                        i++;
                    } catch (Throwable th) {
                        segmentFor.unlock();
                        throw th;
                    }
                } else {
                    return;
                }
            } while (i != 16);
        }

        public void maybeClearReferenceQueues() {
        }

        @GuardedBy("this")
        public void maybeDrainReferenceQueues() {
        }

        public abstract S self();

        /* JADX WARN: Finally extract failed */
        /* JADX WARN: Multi-variable type inference failed */
        public boolean containsValue(Object obj) {
            try {
                if (this.count != 0) {
                    AtomicReferenceArray<E> atomicReferenceArray = this.table;
                    int length = atomicReferenceArray.length();
                    for (int i = 0; i < length; i++) {
                        for (E e = atomicReferenceArray.get(i); e != null; e = e.getNext()) {
                            Object liveValue = getLiveValue(e);
                            if (liveValue != null && this.map.valueEquivalence().equivalent(obj, liveValue)) {
                                postReadCleanup();
                                return true;
                            }
                        }
                    }
                }
                postReadCleanup();
                return false;
            } catch (Throwable th) {
                postReadCleanup();
                throw th;
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @GuardedBy("this")
        public final void expand() {
            AtomicReferenceArray<E> atomicReferenceArray = this.table;
            int length = atomicReferenceArray.length();
            if (length < 1073741824) {
                int i = this.count;
                AtomicReferenceArray<E> atomicReferenceArray2 = (AtomicReferenceArray<E>) new AtomicReferenceArray(length << 1);
                this.threshold = (atomicReferenceArray2.length() * 3) / 4;
                int length2 = atomicReferenceArray2.length() - 1;
                for (int i2 = 0; i2 < length; i2++) {
                    E e = atomicReferenceArray.get(i2);
                    if (e != null) {
                        InternalEntry next = e.getNext();
                        int hash = e.getHash() & length2;
                        if (next == null) {
                            atomicReferenceArray2.set(hash, e);
                        } else {
                            InternalEntry internalEntry = e;
                            while (next != null) {
                                int hash2 = next.getHash() & length2;
                                if (hash2 != hash) {
                                    internalEntry = next;
                                    hash = hash2;
                                }
                                next = next.getNext();
                            }
                            atomicReferenceArray2.set(hash, internalEntry);
                            while (e != internalEntry) {
                                int hash3 = e.getHash() & length2;
                                InternalEntry copy = this.map.entryHelper.copy(self(), e, (InternalEntry) atomicReferenceArray2.get(hash3));
                                if (copy != null) {
                                    atomicReferenceArray2.set(hash3, copy);
                                } else {
                                    i--;
                                }
                                e = e.getNext();
                            }
                        }
                    }
                }
                this.table = atomicReferenceArray2;
                this.count = i;
            }
        }

        public final E getLiveEntry(Object obj, int i) {
            if (this.count != 0) {
                AtomicReferenceArray<E> atomicReferenceArray = this.table;
                for (E e = atomicReferenceArray.get((atomicReferenceArray.length() - 1) & i); e != null; e = (E) e.getNext()) {
                    if (e.getHash() == i) {
                        Object key = e.getKey();
                        if (key == null) {
                            tryDrainReferenceQueues();
                        } else if (this.map.keyEquivalence.equivalent(obj, key)) {
                            return e;
                        }
                    }
                }
            }
            return null;
        }

        public final void postReadCleanup() {
            if ((this.readCount.incrementAndGet() & 63) == 0) {
                runLockedCleanup();
            }
        }

        @GuardedBy("this")
        public final E removeFromChain(E e, E e2) {
            int i = this.count;
            E e3 = (E) e2.getNext();
            while (e != e2) {
                E copy = this.map.entryHelper.copy(self(), e, e3);
                if (copy != null) {
                    e3 = copy;
                } else {
                    i--;
                }
                e = (E) e.getNext();
            }
            this.count = i;
            return e3;
        }

        public final void setValue(E e, V v) {
            this.map.entryHelper.setValue(self(), e, v);
        }

        public Segment(MapMakerInternalMap mapMakerInternalMap, int i) {
            this.map = mapMakerInternalMap;
            AtomicReferenceArray<E> atomicReferenceArray = new AtomicReferenceArray<>(i);
            int length = (atomicReferenceArray.length() * 3) / 4;
            this.threshold = length;
            if (length == -1) {
                this.threshold = length + 1;
            }
            this.table = atomicReferenceArray;
        }

        public final V getLiveValue(E e) {
            if (e.getKey() == null) {
                tryDrainReferenceQueues();
                return null;
            }
            V v = (V) e.getValue();
            if (v != null) {
                return v;
            }
            tryDrainReferenceQueues();
            return null;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public final V put(K k, int i, V v, boolean z) {
            lock();
            try {
                runLockedCleanup();
                int i2 = this.count + 1;
                if (i2 > this.threshold) {
                    expand();
                    i2 = this.count + 1;
                }
                AtomicReferenceArray<E> atomicReferenceArray = this.table;
                int length = (atomicReferenceArray.length() - 1) & i;
                E e = atomicReferenceArray.get(length);
                for (InternalEntry internalEntry = e; internalEntry != null; internalEntry = internalEntry.getNext()) {
                    Object key = internalEntry.getKey();
                    if (internalEntry.getHash() == i && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        V v2 = (V) internalEntry.getValue();
                        if (v2 == null) {
                            this.modCount++;
                            setValue(internalEntry, v);
                            this.count = this.count;
                            return null;
                        } else if (z) {
                            return v2;
                        } else {
                            this.modCount++;
                            setValue(internalEntry, v);
                            return v2;
                        }
                    }
                }
                this.modCount++;
                E newEntry = this.map.entryHelper.newEntry(self(), k, i, e);
                setValue(newEntry, v);
                atomicReferenceArray.set(length, newEntry);
                this.count = i2;
                return null;
            } finally {
                unlock();
            }
        }

        public final void runLockedCleanup() {
            if (tryLock()) {
                try {
                    maybeDrainReferenceQueues();
                    this.readCount.set(0);
                } finally {
                    unlock();
                }
            }
        }

        public final void tryDrainReferenceQueues() {
            if (tryLock()) {
                try {
                    maybeDrainReferenceQueues();
                } finally {
                    unlock();
                }
            }
        }
    }

    /* JADX WARN: Failed to restore enum class, 'enum' modifier removed */
    /* loaded from: classes.dex */
    public static abstract class Strength extends Enum<Strength> {
        public static final /* synthetic */ Strength[] $VALUES;
        public static final AnonymousClass1 STRONG;
        public static final AnonymousClass2 WEAK;

        public Strength() {
            throw null;
        }

        public Strength(String str, int i) {
        }

        public abstract Equivalence<Object> defaultEquivalence();

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v0, types: [com.google.common.collect.MapMakerInternalMap$Strength$1] */
        /* JADX WARN: Type inference failed for: r1v0, types: [com.google.common.collect.MapMakerInternalMap$Strength$2] */
        /* JADX WARN: Unknown variable types count: 2 */
        static {
            /*
                com.google.common.collect.MapMakerInternalMap$Strength$1 r0 = new com.google.common.collect.MapMakerInternalMap$Strength$1
                r0.<init>()
                com.google.common.collect.MapMakerInternalMap.Strength.STRONG = r0
                com.google.common.collect.MapMakerInternalMap$Strength$2 r1 = new com.google.common.collect.MapMakerInternalMap$Strength$2
                r1.<init>()
                com.google.common.collect.MapMakerInternalMap.Strength.WEAK = r1
                r2 = 2
                com.google.common.collect.MapMakerInternalMap$Strength[] r2 = new com.google.common.collect.MapMakerInternalMap.Strength[r2]
                r3 = 0
                r2[r3] = r0
                r0 = 1
                r2[r0] = r1
                com.google.common.collect.MapMakerInternalMap.Strength.$VALUES = r2
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.MapMakerInternalMap.Strength.<clinit>():void");
        }

        public static Strength valueOf(String str) {
            return (Strength) Enum.valueOf(Strength.class, str);
        }

        public static Strength[] values() {
            return (Strength[]) $VALUES.clone();
        }
    }

    /* loaded from: classes.dex */
    public static final class StrongKeyWeakValueEntry<K, V> extends AbstractStrongKeyEntry<K, V, StrongKeyWeakValueEntry<K, V>> implements WeakValueEntry<K, V, StrongKeyWeakValueEntry<K, V>> {
        public volatile WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> valueReference = MapMakerInternalMap.UNSET_WEAK_VALUE_REFERENCE;

        /* loaded from: classes.dex */
        public static final class Helper<K, V> implements InternalEntryHelper<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> {
            public static final Helper<?, ?> INSTANCE = new Helper<>();

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public final InternalEntry copy(Segment segment, InternalEntry internalEntry, InternalEntry internalEntry2) {
                boolean z;
                StrongKeyWeakValueSegment strongKeyWeakValueSegment = (StrongKeyWeakValueSegment) segment;
                StrongKeyWeakValueEntry strongKeyWeakValueEntry = (StrongKeyWeakValueEntry) internalEntry;
                StrongKeyWeakValueEntry strongKeyWeakValueEntry2 = (StrongKeyWeakValueEntry) internalEntry2;
                int i = Segment.$r8$clinit;
                if (strongKeyWeakValueEntry.getValue() == null) {
                    z = true;
                } else {
                    z = false;
                }
                if (z) {
                    return null;
                }
                ReferenceQueue referenceQueue = strongKeyWeakValueSegment.queueForValues;
                StrongKeyWeakValueEntry strongKeyWeakValueEntry3 = new StrongKeyWeakValueEntry(strongKeyWeakValueEntry.key, strongKeyWeakValueEntry.hash, strongKeyWeakValueEntry2);
                strongKeyWeakValueEntry3.valueReference = strongKeyWeakValueEntry.valueReference.copyFor(referenceQueue, strongKeyWeakValueEntry3);
                return strongKeyWeakValueEntry3;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public final InternalEntry newEntry(Segment segment, Object obj, int i, InternalEntry internalEntry) {
                StrongKeyWeakValueSegment strongKeyWeakValueSegment = (StrongKeyWeakValueSegment) segment;
                return new StrongKeyWeakValueEntry(obj, i, (StrongKeyWeakValueEntry) internalEntry);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public final Segment newSegment(MapMakerInternalMap mapMakerInternalMap, int i) {
                return new StrongKeyWeakValueSegment(mapMakerInternalMap, i);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public final void setValue(Segment segment, InternalEntry internalEntry, Object obj) {
                StrongKeyWeakValueEntry strongKeyWeakValueEntry = (StrongKeyWeakValueEntry) internalEntry;
                ReferenceQueue referenceQueue = ((StrongKeyWeakValueSegment) segment).queueForValues;
                Objects.requireNonNull(strongKeyWeakValueEntry);
                WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> weakValueReference = strongKeyWeakValueEntry.valueReference;
                strongKeyWeakValueEntry.valueReference = new WeakValueReferenceImpl(referenceQueue, obj, strongKeyWeakValueEntry);
                weakValueReference.clear();
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public final Strength keyStrength() {
                return Strength.STRONG;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public final Strength valueStrength() {
                return Strength.WEAK;
            }
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final V getValue() {
            return this.valueReference.get();
        }

        public StrongKeyWeakValueEntry(K k, int i, StrongKeyWeakValueEntry<K, V> strongKeyWeakValueEntry) {
            super(k, i, strongKeyWeakValueEntry);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueEntry
        public final WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> getValueReference() {
            return this.valueReference;
        }
    }

    /* loaded from: classes.dex */
    public static final class StrongKeyWeakValueSegment<K, V> extends Segment<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> {
        private final ReferenceQueue<V> queueForValues = new ReferenceQueue<>();

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public final Segment self() {
            return this;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public final void maybeClearReferenceQueues() {
            do {
            } while (this.queueForValues.poll() != null);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public final void maybeDrainReferenceQueues() {
            drainValueReferenceQueue(this.queueForValues);
        }

        public StrongKeyWeakValueSegment(MapMakerInternalMap mapMakerInternalMap, int i) {
            super(mapMakerInternalMap, i);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public final InternalEntry castForTesting(InternalEntry internalEntry) {
            return (StrongKeyWeakValueEntry) internalEntry;
        }
    }

    /* loaded from: classes.dex */
    public final class Values extends AbstractCollection<V> {
        @Override // java.util.AbstractCollection, java.util.Collection
        public final Object[] toArray() {
            return MapMakerInternalMap.access$900(this).toArray();
        }

        public Values() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public final void clear() {
            MapMakerInternalMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public final boolean contains(Object obj) {
            return MapMakerInternalMap.this.containsValue(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public final boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public final Iterator<V> iterator() {
            return new ValueIterator(MapMakerInternalMap.this);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public final int size() {
            return MapMakerInternalMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public final <T> T[] toArray(T[] tArr) {
            return (T[]) MapMakerInternalMap.access$900(this).toArray(tArr);
        }
    }

    /* loaded from: classes.dex */
    public static final class WeakKeyStrongValueSegment<K, V> extends Segment<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> {
        private final ReferenceQueue<K> queueForKeys = new ReferenceQueue<>();

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public final Segment self() {
            return this;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public final void maybeClearReferenceQueues() {
            do {
            } while (this.queueForKeys.poll() != null);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public final void maybeDrainReferenceQueues() {
            drainKeyReferenceQueue(this.queueForKeys);
        }

        public WeakKeyStrongValueSegment(MapMakerInternalMap mapMakerInternalMap, int i) {
            super(mapMakerInternalMap, i);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public final InternalEntry castForTesting(InternalEntry internalEntry) {
            return (WeakKeyStrongValueEntry) internalEntry;
        }
    }

    /* loaded from: classes.dex */
    public static final class WeakKeyWeakValueEntry<K, V> extends AbstractWeakKeyEntry<K, V, WeakKeyWeakValueEntry<K, V>> implements WeakValueEntry<K, V, WeakKeyWeakValueEntry<K, V>> {
        public volatile WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> valueReference = MapMakerInternalMap.UNSET_WEAK_VALUE_REFERENCE;

        /* loaded from: classes.dex */
        public static final class Helper<K, V> implements InternalEntryHelper<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> {
            public static final Helper<?, ?> INSTANCE = new Helper<>();

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public final InternalEntry copy(Segment segment, InternalEntry internalEntry, InternalEntry internalEntry2) {
                boolean z;
                WeakKeyWeakValueSegment weakKeyWeakValueSegment = (WeakKeyWeakValueSegment) segment;
                WeakKeyWeakValueEntry weakKeyWeakValueEntry = (WeakKeyWeakValueEntry) internalEntry;
                WeakKeyWeakValueEntry weakKeyWeakValueEntry2 = (WeakKeyWeakValueEntry) internalEntry2;
                Objects.requireNonNull(weakKeyWeakValueEntry);
                if (weakKeyWeakValueEntry.get() != null) {
                    int i = Segment.$r8$clinit;
                    if (weakKeyWeakValueEntry.getValue() == null) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (!z) {
                        ReferenceQueue referenceQueue = weakKeyWeakValueSegment.queueForKeys;
                        ReferenceQueue referenceQueue2 = weakKeyWeakValueSegment.queueForValues;
                        WeakKeyWeakValueEntry weakKeyWeakValueEntry3 = new WeakKeyWeakValueEntry(referenceQueue, weakKeyWeakValueEntry.get(), weakKeyWeakValueEntry.hash, weakKeyWeakValueEntry2);
                        weakKeyWeakValueEntry3.valueReference = weakKeyWeakValueEntry.valueReference.copyFor(referenceQueue2, weakKeyWeakValueEntry3);
                        return weakKeyWeakValueEntry3;
                    }
                }
                return null;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public final InternalEntry newEntry(Segment segment, Object obj, int i, InternalEntry internalEntry) {
                return new WeakKeyWeakValueEntry(((WeakKeyWeakValueSegment) segment).queueForKeys, obj, i, (WeakKeyWeakValueEntry) internalEntry);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public final Segment newSegment(MapMakerInternalMap mapMakerInternalMap, int i) {
                return new WeakKeyWeakValueSegment(mapMakerInternalMap, i);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public final void setValue(Segment segment, InternalEntry internalEntry, Object obj) {
                WeakKeyWeakValueEntry weakKeyWeakValueEntry = (WeakKeyWeakValueEntry) internalEntry;
                ReferenceQueue referenceQueue = ((WeakKeyWeakValueSegment) segment).queueForValues;
                Objects.requireNonNull(weakKeyWeakValueEntry);
                WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> weakValueReference = weakKeyWeakValueEntry.valueReference;
                weakKeyWeakValueEntry.valueReference = new WeakValueReferenceImpl(referenceQueue, obj, weakKeyWeakValueEntry);
                weakValueReference.clear();
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public final Strength keyStrength() {
                return Strength.WEAK;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public final Strength valueStrength() {
                return Strength.WEAK;
            }
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final V getValue() {
            return this.valueReference.get();
        }

        public WeakKeyWeakValueEntry(ReferenceQueue<K> referenceQueue, K k, int i, WeakKeyWeakValueEntry<K, V> weakKeyWeakValueEntry) {
            super(referenceQueue, k, i, weakKeyWeakValueEntry);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueEntry
        public final WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> getValueReference() {
            return this.valueReference;
        }
    }

    /* loaded from: classes.dex */
    public static final class WeakKeyWeakValueSegment<K, V> extends Segment<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> {
        private final ReferenceQueue<K> queueForKeys = new ReferenceQueue<>();
        private final ReferenceQueue<V> queueForValues = new ReferenceQueue<>();

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public final Segment self() {
            return this;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public final void maybeClearReferenceQueues() {
            do {
            } while (this.queueForKeys.poll() != null);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public final void maybeDrainReferenceQueues() {
            drainKeyReferenceQueue(this.queueForKeys);
            drainValueReferenceQueue(this.queueForValues);
        }

        public WeakKeyWeakValueSegment(MapMakerInternalMap mapMakerInternalMap, int i) {
            super(mapMakerInternalMap, i);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public final InternalEntry castForTesting(InternalEntry internalEntry) {
            return (WeakKeyWeakValueEntry) internalEntry;
        }
    }

    /* loaded from: classes.dex */
    public interface WeakValueEntry<K, V, E extends InternalEntry<K, V, E>> extends InternalEntry<K, V, E> {
        WeakValueReference<K, V, E> getValueReference();
    }

    /* loaded from: classes.dex */
    public interface WeakValueReference<K, V, E extends InternalEntry<K, V, E>> {
        void clear();

        WeakValueReference copyFor(ReferenceQueue referenceQueue, WeakValueEntry weakValueEntry);

        V get();

        E getEntry();
    }

    /* loaded from: classes.dex */
    public static final class WeakValueReferenceImpl<K, V, E extends InternalEntry<K, V, E>> extends WeakReference<V> implements WeakValueReference<K, V, E> {
        public final E entry;

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueReference
        public final WeakValueReference copyFor(ReferenceQueue referenceQueue, WeakValueEntry weakValueEntry) {
            return new WeakValueReferenceImpl(referenceQueue, get(), weakValueEntry);
        }

        public WeakValueReferenceImpl(ReferenceQueue<V> referenceQueue, V v, E e) {
            super(v, referenceQueue);
            this.entry = e;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueReference
        public final E getEntry() {
            return this.entry;
        }
    }

    /* loaded from: classes.dex */
    public final class WriteThroughEntry extends AbstractMapEntry<K, V> {
        public final K key;
        public V value;

        public WriteThroughEntry(K k, V v) {
            this.key = k;
            this.value = v;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public final boolean equals(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            if (!this.key.equals(entry.getKey()) || !this.value.equals(entry.getValue())) {
                return false;
            }
            return true;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public final int hashCode() {
            return this.value.hashCode() ^ this.key.hashCode();
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public final V setValue(V v) {
            V v2 = (V) MapMakerInternalMap.this.put(this.key, v);
            this.value = v;
            return v2;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public final K getKey() {
            return this.key;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public final V getValue() {
            return this.value;
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final boolean containsKey(Object obj) {
        E liveEntry;
        boolean z = false;
        if (obj == null) {
            return false;
        }
        int hash = hash(obj);
        Segment<K, V, E, S> segmentFor = segmentFor(hash);
        Objects.requireNonNull(segmentFor);
        try {
            if (!(segmentFor.count == 0 || (liveEntry = segmentFor.getLiveEntry(obj, hash)) == null)) {
                if (liveEntry.getValue() != null) {
                    z = true;
                }
            }
            return z;
        } finally {
            segmentFor.postReadCleanup();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final V get(Object obj) {
        V v = null;
        if (obj == null) {
            return null;
        }
        int hash = hash(obj);
        Segment<K, V, E, S> segmentFor = segmentFor(hash);
        Objects.requireNonNull(segmentFor);
        try {
            E liveEntry = segmentFor.getLiveEntry(obj, hash);
            if (liveEntry != null && (v = (V) liveEntry.getValue()) == null) {
                segmentFor.tryDrainReferenceQueues();
            }
            return v;
        } finally {
            segmentFor.postReadCleanup();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x003d, code lost:
        r10 = (V) r6.getValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0041, code lost:
        if (r10 == null) goto L_0x0044;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0048, code lost:
        if (r6.getValue() != null) goto L_0x004c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x004a, code lost:
        r1 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x004c, code lost:
        r1 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x004d, code lost:
        if (r1 == false) goto L_0x006a;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x004f, code lost:
        r9.modCount++;
        r2.set(r3, r9.removeFromChain(r5, r6));
        r9.count--;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:?, code lost:
        return r10;
     */
    @Override // java.util.AbstractMap, java.util.Map
    @com.google.errorprone.annotations.CanIgnoreReturnValue
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final V remove(java.lang.Object r10) {
        /*
            r9 = this;
            r0 = 0
            if (r10 != 0) goto L_0x0004
            return r0
        L_0x0004:
            int r1 = r9.hash(r10)
            com.google.common.collect.MapMakerInternalMap$Segment r9 = r9.segmentFor(r1)
            java.util.Objects.requireNonNull(r9)
            r9.lock()
            r9.runLockedCleanup()     // Catch: all -> 0x006e
            java.util.concurrent.atomic.AtomicReferenceArray<E extends com.google.common.collect.MapMakerInternalMap$InternalEntry<K, V, E>> r2 = r9.table     // Catch: all -> 0x006e
            int r3 = r2.length()     // Catch: all -> 0x006e
            r4 = 1
            int r3 = r3 - r4
            r3 = r3 & r1
            java.lang.Object r5 = r2.get(r3)     // Catch: all -> 0x006e
            com.google.common.collect.MapMakerInternalMap$InternalEntry r5 = (com.google.common.collect.MapMakerInternalMap.InternalEntry) r5     // Catch: all -> 0x006e
            r6 = r5
        L_0x0025:
            if (r6 == 0) goto L_0x006a
            java.lang.Object r7 = r6.getKey()     // Catch: all -> 0x006e
            int r8 = r6.getHash()     // Catch: all -> 0x006e
            if (r8 != r1) goto L_0x0065
            if (r7 == 0) goto L_0x0065
            com.google.common.collect.MapMakerInternalMap<K, V, E extends com.google.common.collect.MapMakerInternalMap$InternalEntry<K, V, E>, S extends com.google.common.collect.MapMakerInternalMap$Segment<K, V, E, S>> r8 = r9.map     // Catch: all -> 0x006e
            com.google.common.base.Equivalence<java.lang.Object> r8 = r8.keyEquivalence     // Catch: all -> 0x006e
            boolean r7 = r8.equivalent(r10, r7)     // Catch: all -> 0x006e
            if (r7 == 0) goto L_0x0065
            java.lang.Object r10 = r6.getValue()     // Catch: all -> 0x006e
            if (r10 == 0) goto L_0x0044
            goto L_0x004f
        L_0x0044:
            java.lang.Object r1 = r6.getValue()     // Catch: all -> 0x006e
            if (r1 != 0) goto L_0x004c
            r1 = r4
            goto L_0x004d
        L_0x004c:
            r1 = 0
        L_0x004d:
            if (r1 == 0) goto L_0x006a
        L_0x004f:
            int r0 = r9.modCount     // Catch: all -> 0x006e
            int r0 = r0 + r4
            r9.modCount = r0     // Catch: all -> 0x006e
            com.google.common.collect.MapMakerInternalMap$InternalEntry r0 = r9.removeFromChain(r5, r6)     // Catch: all -> 0x006e
            int r1 = r9.count     // Catch: all -> 0x006e
            int r1 = r1 - r4
            r2.set(r3, r0)     // Catch: all -> 0x006e
            r9.count = r1     // Catch: all -> 0x006e
            r9.unlock()
            r0 = r10
            goto L_0x006d
        L_0x0065:
            com.google.common.collect.MapMakerInternalMap$InternalEntry r6 = r6.getNext()     // Catch: all -> 0x006e
            goto L_0x0025
        L_0x006a:
            r9.unlock()
        L_0x006d:
            return r0
        L_0x006e:
            r10 = move-exception
            r9.unlock()
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.MapMakerInternalMap.remove(java.lang.Object):java.lang.Object");
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    @CanIgnoreReturnValue
    public final V replace(K k, V v) {
        Objects.requireNonNull(k);
        Objects.requireNonNull(v);
        int hash = hash(k);
        Segment<K, V, E, S> segmentFor = segmentFor(hash);
        Objects.requireNonNull(segmentFor);
        segmentFor.lock();
        try {
            segmentFor.runLockedCleanup();
            AtomicReferenceArray<E> atomicReferenceArray = segmentFor.table;
            int length = (atomicReferenceArray.length() - 1) & hash;
            E e = atomicReferenceArray.get(length);
            E e2 = e;
            while (true) {
                if (e2 == null) {
                    break;
                }
                Object key = e2.getKey();
                if (e2.getHash() != hash || key == null || !segmentFor.map.keyEquivalence.equivalent(k, key)) {
                    e2 = (E) e2.getNext();
                } else {
                    V v2 = (V) e2.getValue();
                    if (v2 == null) {
                        if (e2.getValue() == null) {
                            segmentFor.modCount++;
                            atomicReferenceArray.set(length, segmentFor.removeFromChain(e, e2));
                            segmentFor.count--;
                        }
                    } else {
                        segmentFor.modCount++;
                        segmentFor.setValue(e2, v);
                        return v2;
                    }
                }
            }
            return null;
        } finally {
            segmentFor.unlock();
        }
    }

    /* loaded from: classes.dex */
    public static abstract class AbstractStrongKeyEntry<K, V, E extends InternalEntry<K, V, E>> implements InternalEntry<K, V, E> {
        public final int hash;
        public final K key;
        public final E next;

        public AbstractStrongKeyEntry(K k, int i, E e) {
            this.key = k;
            this.hash = i;
            this.next = e;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final int getHash() {
            return this.hash;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final K getKey() {
            return this.key;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final E getNext() {
            return this.next;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class AbstractWeakKeyEntry<K, V, E extends InternalEntry<K, V, E>> extends WeakReference<K> implements InternalEntry<K, V, E> {
        public final int hash;
        public final E next;

        public AbstractWeakKeyEntry(ReferenceQueue<K> referenceQueue, K k, int i, E e) {
            super(k, referenceQueue);
            this.hash = i;
            this.next = e;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final K getKey() {
            return get();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final int getHash() {
            return this.hash;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final E getNext() {
            return this.next;
        }
    }

    /* loaded from: classes.dex */
    public final class EntryIterator extends MapMakerInternalMap<K, V, E, S>.HashIterator<Map.Entry<K, V>> {
        @Override // java.util.Iterator
        public final Object next() {
            return nextEntry();
        }

        public EntryIterator(MapMakerInternalMap mapMakerInternalMap) {
            super();
        }
    }

    /* loaded from: classes.dex */
    public final class KeyIterator extends MapMakerInternalMap<K, V, E, S>.HashIterator<K> {
        @Override // java.util.Iterator
        public final K next() {
            MapMakerInternalMap<K, V, E, S>.WriteThroughEntry nextEntry = nextEntry();
            Objects.requireNonNull(nextEntry);
            return nextEntry.key;
        }

        public KeyIterator(MapMakerInternalMap mapMakerInternalMap) {
            super();
        }
    }

    /* loaded from: classes.dex */
    public static final class SerializationProxy<K, V> extends AbstractSerializationProxy<K, V> {
        private static final long serialVersionUID = 3;

        /* JADX WARN: Multi-variable type inference failed */
        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            boolean z;
            boolean z2;
            boolean z3;
            boolean z4;
            boolean z5;
            boolean z6;
            objectInputStream.defaultReadObject();
            int readInt = objectInputStream.readInt();
            MapMaker mapMaker = new MapMaker();
            int i = mapMaker.initialCapacity;
            boolean z7 = true;
            if (i == -1) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                if (readInt >= 0) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (z2) {
                    mapMaker.initialCapacity = readInt;
                    Strength strength = this.keyStrength;
                    Strength strength2 = mapMaker.keyStrength;
                    if (strength2 == null) {
                        z3 = true;
                    } else {
                        z3 = false;
                    }
                    Preconditions.checkState(z3, "Key strength was already set to %s", strength2);
                    Objects.requireNonNull(strength);
                    mapMaker.keyStrength = strength;
                    Strength.AnonymousClass1 r2 = Strength.STRONG;
                    if (strength != r2) {
                        mapMaker.useCustomMap = true;
                    }
                    Strength strength3 = this.valueStrength;
                    Strength strength4 = mapMaker.valueStrength;
                    if (strength4 == null) {
                        z4 = true;
                    } else {
                        z4 = false;
                    }
                    Preconditions.checkState(z4, "Value strength was already set to %s", strength4);
                    Objects.requireNonNull(strength3);
                    mapMaker.valueStrength = strength3;
                    if (strength3 != r2) {
                        mapMaker.useCustomMap = true;
                    }
                    Equivalence<Object> equivalence = this.keyEquivalence;
                    Equivalence<Object> equivalence2 = mapMaker.keyEquivalence;
                    if (equivalence2 == null) {
                        z5 = true;
                    } else {
                        z5 = false;
                    }
                    Preconditions.checkState(z5, "key equivalence was already set to %s", equivalence2);
                    Objects.requireNonNull(equivalence);
                    mapMaker.keyEquivalence = equivalence;
                    mapMaker.useCustomMap = true;
                    int i2 = this.concurrencyLevel;
                    int i3 = mapMaker.concurrencyLevel;
                    if (i3 == -1) {
                        z6 = true;
                    } else {
                        z6 = false;
                    }
                    if (z6) {
                        if (i2 <= 0) {
                            z7 = false;
                        }
                        if (z7) {
                            mapMaker.concurrencyLevel = i2;
                            this.delegate = mapMaker.makeMap();
                            while (true) {
                                Object readObject = objectInputStream.readObject();
                                if (readObject != null) {
                                    this.delegate.put(readObject, objectInputStream.readObject());
                                } else {
                                    return;
                                }
                            }
                        } else {
                            throw new IllegalArgumentException();
                        }
                    } else {
                        throw new IllegalStateException(Strings.lenientFormat("concurrency level was already set to %s", Integer.valueOf(i3)));
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            } else {
                throw new IllegalStateException(Strings.lenientFormat("initial capacity was already set to %s", Integer.valueOf(i)));
            }
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.defaultWriteObject();
            objectOutputStream.writeInt(this.delegate.size());
            for (Map.Entry<K, V> entry : this.delegate.entrySet()) {
                objectOutputStream.writeObject(entry.getKey());
                objectOutputStream.writeObject(entry.getValue());
            }
            objectOutputStream.writeObject(null);
        }

        public SerializationProxy(Strength strength, Strength strength2, Equivalence<Object> equivalence, Equivalence<Object> equivalence2, int i, ConcurrentMap<K, V> concurrentMap) {
            super(strength, strength2, equivalence, equivalence2, i, concurrentMap);
        }

        private Object readResolve() {
            return this.delegate;
        }
    }

    /* loaded from: classes.dex */
    public static final class StrongKeyStrongValueEntry<K, V> extends AbstractStrongKeyEntry<K, V, StrongKeyStrongValueEntry<K, V>> {
        public volatile V value = null;

        /* loaded from: classes.dex */
        public static final class Helper<K, V> implements InternalEntryHelper<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> {
            public static final Helper<?, ?> INSTANCE = new Helper<>();

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public final InternalEntry copy(Segment segment, InternalEntry internalEntry, InternalEntry internalEntry2) {
                StrongKeyStrongValueSegment strongKeyStrongValueSegment = (StrongKeyStrongValueSegment) segment;
                StrongKeyStrongValueEntry strongKeyStrongValueEntry = (StrongKeyStrongValueEntry) internalEntry;
                Objects.requireNonNull(strongKeyStrongValueEntry);
                StrongKeyStrongValueEntry strongKeyStrongValueEntry2 = new StrongKeyStrongValueEntry(strongKeyStrongValueEntry.key, strongKeyStrongValueEntry.hash, (StrongKeyStrongValueEntry) internalEntry2);
                strongKeyStrongValueEntry2.value = strongKeyStrongValueEntry.value;
                return strongKeyStrongValueEntry2;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public final InternalEntry newEntry(Segment segment, Object obj, int i, InternalEntry internalEntry) {
                StrongKeyStrongValueSegment strongKeyStrongValueSegment = (StrongKeyStrongValueSegment) segment;
                return new StrongKeyStrongValueEntry(obj, i, (StrongKeyStrongValueEntry) internalEntry);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public final Segment newSegment(MapMakerInternalMap mapMakerInternalMap, int i) {
                return new StrongKeyStrongValueSegment(mapMakerInternalMap, i);
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public final void setValue(Segment segment, InternalEntry internalEntry, Object obj) {
                StrongKeyStrongValueSegment strongKeyStrongValueSegment = (StrongKeyStrongValueSegment) segment;
                StrongKeyStrongValueEntry strongKeyStrongValueEntry = (StrongKeyStrongValueEntry) internalEntry;
                Objects.requireNonNull(strongKeyStrongValueEntry);
                strongKeyStrongValueEntry.value = obj;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public final Strength keyStrength() {
                return Strength.STRONG;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public final Strength valueStrength() {
                return Strength.STRONG;
            }
        }

        public StrongKeyStrongValueEntry(K k, int i, StrongKeyStrongValueEntry<K, V> strongKeyStrongValueEntry) {
            super(k, i, strongKeyStrongValueEntry);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final V getValue() {
            return this.value;
        }
    }

    /* loaded from: classes.dex */
    public final class ValueIterator extends MapMakerInternalMap<K, V, E, S>.HashIterator<V> {
        @Override // java.util.Iterator
        public final V next() {
            MapMakerInternalMap<K, V, E, S>.WriteThroughEntry nextEntry = nextEntry();
            Objects.requireNonNull(nextEntry);
            return nextEntry.value;
        }

        public ValueIterator(MapMakerInternalMap mapMakerInternalMap) {
            super();
        }
    }

    /* loaded from: classes.dex */
    public static final class WeakKeyStrongValueEntry<K, V> extends AbstractWeakKeyEntry<K, V, WeakKeyStrongValueEntry<K, V>> {
        public volatile V value = null;

        /* loaded from: classes.dex */
        public static final class Helper<K, V> implements InternalEntryHelper<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> {
            public static final Helper<?, ?> INSTANCE = new Helper<>();

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public final InternalEntry copy(Segment segment, InternalEntry internalEntry, InternalEntry internalEntry2) {
                WeakKeyStrongValueSegment weakKeyStrongValueSegment = (WeakKeyStrongValueSegment) segment;
                WeakKeyStrongValueEntry weakKeyStrongValueEntry = (WeakKeyStrongValueEntry) internalEntry;
                WeakKeyStrongValueEntry weakKeyStrongValueEntry2 = (WeakKeyStrongValueEntry) internalEntry2;
                Objects.requireNonNull(weakKeyStrongValueEntry);
                if (weakKeyStrongValueEntry.get() == null) {
                    return null;
                }
                WeakKeyStrongValueEntry weakKeyStrongValueEntry3 = new WeakKeyStrongValueEntry(weakKeyStrongValueSegment.queueForKeys, weakKeyStrongValueEntry.get(), weakKeyStrongValueEntry.hash, weakKeyStrongValueEntry2);
                weakKeyStrongValueEntry3.value = weakKeyStrongValueEntry.value;
                return weakKeyStrongValueEntry3;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public final InternalEntry newEntry(Segment segment, Object obj, int i, InternalEntry internalEntry) {
                return new WeakKeyStrongValueEntry(((WeakKeyStrongValueSegment) segment).queueForKeys, obj, i, (WeakKeyStrongValueEntry) internalEntry);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public final Segment newSegment(MapMakerInternalMap mapMakerInternalMap, int i) {
                return new WeakKeyStrongValueSegment(mapMakerInternalMap, i);
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public final void setValue(Segment segment, InternalEntry internalEntry, Object obj) {
                WeakKeyStrongValueSegment weakKeyStrongValueSegment = (WeakKeyStrongValueSegment) segment;
                WeakKeyStrongValueEntry weakKeyStrongValueEntry = (WeakKeyStrongValueEntry) internalEntry;
                Objects.requireNonNull(weakKeyStrongValueEntry);
                weakKeyStrongValueEntry.value = obj;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public final Strength keyStrength() {
                return Strength.WEAK;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public final Strength valueStrength() {
                return Strength.STRONG;
            }
        }

        public WeakKeyStrongValueEntry(ReferenceQueue<K> referenceQueue, K k, int i, WeakKeyStrongValueEntry<K, V> weakKeyStrongValueEntry) {
            super(referenceQueue, k, i, weakKeyStrongValueEntry);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final V getValue() {
            return this.value;
        }
    }

    public static ArrayList access$900(Collection collection) {
        ArrayList arrayList = new ArrayList(collection.size());
        Iterator it = collection.iterator();
        Objects.requireNonNull(it);
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return arrayList;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final void clear() {
        Segment<K, V, E, S>[] segmentArr = this.segments;
        int length = segmentArr.length;
        for (int i = 0; i < length; i++) {
            Segment<K, V, E, S> segment = segmentArr[i];
            Objects.requireNonNull(segment);
            if (segment.count != 0) {
                segment.lock();
                try {
                    AtomicReferenceArray<E> atomicReferenceArray = segment.table;
                    for (int i2 = 0; i2 < atomicReferenceArray.length(); i2++) {
                        atomicReferenceArray.set(i2, null);
                    }
                    segment.maybeClearReferenceQueues();
                    segment.readCount.set(0);
                    segment.modCount++;
                    segment.count = 0;
                } finally {
                    segment.unlock();
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v0 */
    /* JADX WARN: Type inference failed for: r10v1, types: [int] */
    /* JADX WARN: Type inference failed for: r11v0, types: [com.google.common.collect.MapMakerInternalMap$Segment] */
    /* JADX WARN: Type inference failed for: r13v0 */
    /* JADX WARN: Type inference failed for: r13v1, types: [int] */
    /* JADX WARN: Type inference failed for: r3v0, types: [com.google.common.collect.MapMakerInternalMap$Segment<K, V, E extends com.google.common.collect.MapMakerInternalMap$InternalEntry<K, V, E>, S extends com.google.common.collect.MapMakerInternalMap$Segment<K, V, E, S>>[]] */
    /* JADX WARN: Unknown variable types count: 1 */
    @Override // java.util.AbstractMap, java.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean containsValue(java.lang.Object r17) {
        /*
            r16 = this;
            r0 = r17
            r1 = 0
            if (r0 != 0) goto L_0x0006
            return r1
        L_0x0006:
            r2 = r16
            com.google.common.collect.MapMakerInternalMap$Segment<K, V, E extends com.google.common.collect.MapMakerInternalMap$InternalEntry<K, V, E>, S extends com.google.common.collect.MapMakerInternalMap$Segment<K, V, E, S>>[] r3 = r2.segments
            r4 = -1
            r6 = r1
        L_0x000d:
            r7 = 3
            if (r6 >= r7) goto L_0x005a
            r7 = 0
            int r9 = r3.length
            r10 = r1
        L_0x0014:
            if (r10 >= r9) goto L_0x004f
            r11 = r3[r10]
            int r12 = r11.count
            java.util.concurrent.atomic.AtomicReferenceArray<E extends com.google.common.collect.MapMakerInternalMap$InternalEntry<K, V, E>> r12 = r11.table
            r13 = r1
        L_0x001d:
            int r14 = r12.length()
            if (r13 >= r14) goto L_0x0047
            java.lang.Object r14 = r12.get(r13)
            com.google.common.collect.MapMakerInternalMap$InternalEntry r14 = (com.google.common.collect.MapMakerInternalMap.InternalEntry) r14
        L_0x0029:
            if (r14 == 0) goto L_0x0043
            java.lang.Object r15 = r11.getLiveValue(r14)
            if (r15 == 0) goto L_0x003d
            com.google.common.base.Equivalence r1 = r16.valueEquivalence()
            boolean r1 = r1.equivalent(r0, r15)
            if (r1 == 0) goto L_0x003d
            r0 = 1
            return r0
        L_0x003d:
            com.google.common.collect.MapMakerInternalMap$InternalEntry r14 = r14.getNext()
            r1 = 0
            goto L_0x0029
        L_0x0043:
            int r13 = r13 + 1
            r1 = 0
            goto L_0x001d
        L_0x0047:
            int r1 = r11.modCount
            long r11 = (long) r1
            long r7 = r7 + r11
            int r10 = r10 + 1
            r1 = 0
            goto L_0x0014
        L_0x004f:
            int r1 = (r7 > r4 ? 1 : (r7 == r4 ? 0 : -1))
            if (r1 != 0) goto L_0x0055
            r0 = 0
            goto L_0x005b
        L_0x0055:
            int r6 = r6 + 1
            r4 = r7
            r1 = 0
            goto L_0x000d
        L_0x005a:
            r0 = r1
        L_0x005b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.MapMakerInternalMap.containsValue(java.lang.Object):boolean");
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final Set<Map.Entry<K, V>> entrySet() {
        EntrySet entrySet = this.entrySet;
        if (entrySet != null) {
            return entrySet;
        }
        EntrySet entrySet2 = new EntrySet();
        this.entrySet = entrySet2;
        return entrySet2;
    }

    public final int hash(Object obj) {
        int i;
        Equivalence<Object> equivalence = this.keyEquivalence;
        if (obj == null) {
            Objects.requireNonNull(equivalence);
            i = 0;
        } else {
            i = equivalence.doHash(obj);
        }
        int i2 = i + ((i << 15) ^ (-12931));
        int i3 = i2 ^ (i2 >>> 10);
        int i4 = i3 + (i3 << 3);
        int i5 = i4 ^ (i4 >>> 6);
        int i6 = (i5 << 2) + (i5 << 14) + i5;
        return (i6 >>> 16) ^ i6;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final boolean isEmpty() {
        Segment<K, V, E, S>[] segmentArr = this.segments;
        long j = 0;
        for (int i = 0; i < segmentArr.length; i++) {
            if (segmentArr[i].count != 0) {
                return false;
            }
            j += segmentArr[i].modCount;
        }
        if (j == 0) {
            return true;
        }
        for (int i2 = 0; i2 < segmentArr.length; i2++) {
            if (segmentArr[i2].count != 0) {
                return false;
            }
            j -= segmentArr[i2].modCount;
        }
        if (j == 0) {
            return true;
        }
        return false;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final Set<K> keySet() {
        KeySet keySet = this.keySet;
        if (keySet != null) {
            return keySet;
        }
        KeySet keySet2 = new KeySet();
        this.keySet = keySet2;
        return keySet2;
    }

    public Strength keyStrength() {
        return this.entryHelper.keyStrength();
    }

    public final Segment<K, V, E, S> segmentFor(int i) {
        return this.segments[this.segmentMask & (i >>> this.segmentShift)];
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final int size() {
        long j = 0;
        for (Segment<K, V, E, S> segment : this.segments) {
            j += segment.count;
        }
        return R$layout.saturatedCast(j);
    }

    public Equivalence<Object> valueEquivalence() {
        return this.entryHelper.valueStrength().defaultEquivalence();
    }

    public Strength valueStrength() {
        return this.entryHelper.valueStrength();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final Collection<V> values() {
        Values values = this.values;
        if (values != null) {
            return values;
        }
        Values values2 = new Values();
        this.values = values2;
        return values2;
    }

    public Object writeReplace() {
        return new SerializationProxy(this.entryHelper.keyStrength(), this.entryHelper.valueStrength(), this.keyEquivalence, this.entryHelper.valueStrength().defaultEquivalence(), this.concurrencyLevel, this);
    }

    public MapMakerInternalMap(MapMaker mapMaker, InternalEntryHelper<K, V, E, S> internalEntryHelper) {
        Objects.requireNonNull(mapMaker);
        int i = mapMaker.concurrencyLevel;
        this.concurrencyLevel = Math.min(i == -1 ? 4 : i, 65536);
        Equivalence<Object> equivalence = mapMaker.keyEquivalence;
        Equivalence<Object> defaultEquivalence = mapMaker.getKeyStrength().defaultEquivalence();
        if (equivalence == null) {
            Objects.requireNonNull(defaultEquivalence, "Both parameters are null");
            equivalence = defaultEquivalence;
        }
        this.keyEquivalence = equivalence;
        this.entryHelper = internalEntryHelper;
        int i2 = mapMaker.initialCapacity;
        int min = Math.min(i2 == -1 ? 16 : i2, 1073741824);
        int i3 = 0;
        int i4 = 1;
        int i5 = 0;
        int i6 = 1;
        while (i6 < this.concurrencyLevel) {
            i5++;
            i6 <<= 1;
        }
        this.segmentShift = 32 - i5;
        this.segmentMask = i6 - 1;
        this.segments = new Segment[i6];
        int i7 = min / i6;
        while (i4 < (i6 * i7 < min ? i7 + 1 : i7)) {
            i4 <<= 1;
        }
        while (true) {
            Segment<K, V, E, S>[] segmentArr = this.segments;
            if (i3 < segmentArr.length) {
                segmentArr[i3] = this.entryHelper.newSegment(this, i4);
                i3++;
            } else {
                return;
            }
        }
    }

    public E copyEntry(E e, E e2) {
        Segment<K, V, E, S> segmentFor = segmentFor(e.getHash());
        Objects.requireNonNull(segmentFor);
        return segmentFor.map.entryHelper.copy(segmentFor.self(), e, e2);
    }

    public boolean isLiveForTesting(InternalEntry<K, V, ?> internalEntry) {
        Segment<K, V, E, S> segmentFor = segmentFor(internalEntry.getHash());
        Objects.requireNonNull(segmentFor);
        if (segmentFor.getLiveValue(segmentFor.castForTesting(internalEntry)) != null) {
            return true;
        }
        return false;
    }

    @Override // java.util.AbstractMap, java.util.Map
    @CanIgnoreReturnValue
    public final V put(K k, V v) {
        Objects.requireNonNull(k);
        Objects.requireNonNull(v);
        int hash = hash(k);
        return segmentFor(hash).put(k, hash, v, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractMap, java.util.Map
    public final void putAll(Map<? extends K, ? extends V> map) {
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    @CanIgnoreReturnValue
    public final V putIfAbsent(K k, V v) {
        Objects.requireNonNull(k);
        Objects.requireNonNull(v);
        int hash = hash(k);
        return segmentFor(hash).put(k, hash, v, true);
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x004e, code lost:
        if (r9.map.valueEquivalence().equivalent(r11, r6.getValue()) == false) goto L_0x0052;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0050, code lost:
        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0056, code lost:
        if (r6.getValue() != null) goto L_0x005a;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0058, code lost:
        r10 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x005a, code lost:
        r10 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x005b, code lost:
        if (r10 == false) goto L_0x0074;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x005d, code lost:
        r9.modCount++;
        r2.set(r3, r9.removeFromChain(r5, r6));
        r9.count--;
     */
    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    @com.google.errorprone.annotations.CanIgnoreReturnValue
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean remove(java.lang.Object r10, java.lang.Object r11) {
        /*
            r9 = this;
            r0 = 0
            if (r10 == 0) goto L_0x007d
            if (r11 != 0) goto L_0x0007
            goto L_0x007d
        L_0x0007:
            int r1 = r9.hash(r10)
            com.google.common.collect.MapMakerInternalMap$Segment r9 = r9.segmentFor(r1)
            java.util.Objects.requireNonNull(r9)
            r9.lock()
            r9.runLockedCleanup()     // Catch: all -> 0x0078
            java.util.concurrent.atomic.AtomicReferenceArray<E extends com.google.common.collect.MapMakerInternalMap$InternalEntry<K, V, E>> r2 = r9.table     // Catch: all -> 0x0078
            int r3 = r2.length()     // Catch: all -> 0x0078
            r4 = 1
            int r3 = r3 - r4
            r3 = r3 & r1
            java.lang.Object r5 = r2.get(r3)     // Catch: all -> 0x0078
            com.google.common.collect.MapMakerInternalMap$InternalEntry r5 = (com.google.common.collect.MapMakerInternalMap.InternalEntry) r5     // Catch: all -> 0x0078
            r6 = r5
        L_0x0028:
            if (r6 == 0) goto L_0x0074
            java.lang.Object r7 = r6.getKey()     // Catch: all -> 0x0078
            int r8 = r6.getHash()     // Catch: all -> 0x0078
            if (r8 != r1) goto L_0x006f
            if (r7 == 0) goto L_0x006f
            com.google.common.collect.MapMakerInternalMap<K, V, E extends com.google.common.collect.MapMakerInternalMap$InternalEntry<K, V, E>, S extends com.google.common.collect.MapMakerInternalMap$Segment<K, V, E, S>> r8 = r9.map     // Catch: all -> 0x0078
            com.google.common.base.Equivalence<java.lang.Object> r8 = r8.keyEquivalence     // Catch: all -> 0x0078
            boolean r7 = r8.equivalent(r10, r7)     // Catch: all -> 0x0078
            if (r7 == 0) goto L_0x006f
            java.lang.Object r10 = r6.getValue()     // Catch: all -> 0x0078
            com.google.common.collect.MapMakerInternalMap<K, V, E extends com.google.common.collect.MapMakerInternalMap$InternalEntry<K, V, E>, S extends com.google.common.collect.MapMakerInternalMap$Segment<K, V, E, S>> r1 = r9.map     // Catch: all -> 0x0078
            com.google.common.base.Equivalence r1 = r1.valueEquivalence()     // Catch: all -> 0x0078
            boolean r10 = r1.equivalent(r11, r10)     // Catch: all -> 0x0078
            if (r10 == 0) goto L_0x0052
            r0 = r4
            goto L_0x005d
        L_0x0052:
            java.lang.Object r10 = r6.getValue()     // Catch: all -> 0x0078
            if (r10 != 0) goto L_0x005a
            r10 = r4
            goto L_0x005b
        L_0x005a:
            r10 = r0
        L_0x005b:
            if (r10 == 0) goto L_0x0074
        L_0x005d:
            int r10 = r9.modCount     // Catch: all -> 0x0078
            int r10 = r10 + r4
            r9.modCount = r10     // Catch: all -> 0x0078
            com.google.common.collect.MapMakerInternalMap$InternalEntry r10 = r9.removeFromChain(r5, r6)     // Catch: all -> 0x0078
            int r11 = r9.count     // Catch: all -> 0x0078
            int r11 = r11 - r4
            r2.set(r3, r10)     // Catch: all -> 0x0078
            r9.count = r11     // Catch: all -> 0x0078
            goto L_0x0074
        L_0x006f:
            com.google.common.collect.MapMakerInternalMap$InternalEntry r6 = r6.getNext()     // Catch: all -> 0x0078
            goto L_0x0028
        L_0x0074:
            r9.unlock()
            return r0
        L_0x0078:
            r10 = move-exception
            r9.unlock()
            throw r10
        L_0x007d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.MapMakerInternalMap.remove(java.lang.Object, java.lang.Object):boolean");
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    @CanIgnoreReturnValue
    public final boolean replace(K k, V v, V v2) {
        Objects.requireNonNull(k);
        Objects.requireNonNull(v2);
        if (v == null) {
            return false;
        }
        int hash = hash(k);
        Segment<K, V, E, S> segmentFor = segmentFor(hash);
        Objects.requireNonNull(segmentFor);
        segmentFor.lock();
        try {
            segmentFor.runLockedCleanup();
            AtomicReferenceArray<E> atomicReferenceArray = segmentFor.table;
            int length = (atomicReferenceArray.length() - 1) & hash;
            E e = atomicReferenceArray.get(length);
            E e2 = e;
            while (true) {
                if (e2 == null) {
                    break;
                }
                Object key = e2.getKey();
                if (e2.getHash() != hash || key == null || !segmentFor.map.keyEquivalence.equivalent(k, key)) {
                    e2 = (E) e2.getNext();
                } else {
                    Object value = e2.getValue();
                    if (value == null) {
                        if (e2.getValue() == null) {
                            segmentFor.modCount++;
                            atomicReferenceArray.set(length, segmentFor.removeFromChain(e, e2));
                            segmentFor.count--;
                        }
                    } else if (segmentFor.map.valueEquivalence().equivalent(v, value)) {
                        segmentFor.modCount++;
                        segmentFor.setValue(e2, v2);
                        return true;
                    }
                }
            }
            return false;
        } finally {
            segmentFor.unlock();
        }
    }

    /* loaded from: classes.dex */
    public static final class StrongKeyStrongValueSegment<K, V> extends Segment<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> {
        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public final Segment self() {
            return this;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public final InternalEntry castForTesting(InternalEntry internalEntry) {
            return (StrongKeyStrongValueEntry) internalEntry;
        }

        public StrongKeyStrongValueSegment(MapMakerInternalMap mapMakerInternalMap, int i) {
            super(mapMakerInternalMap, i);
        }
    }
}
