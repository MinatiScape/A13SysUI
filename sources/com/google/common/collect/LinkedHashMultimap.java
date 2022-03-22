package com.google.common.collect;

import androidx.recyclerview.R$dimen;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractMapBasedMultimap;
import com.google.common.collect.AbstractMultimap;
import com.google.common.collect.CompactHashMap;
import com.google.common.collect.Sets;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import okio.Okio__OkioKt;
/* loaded from: classes.dex */
public final class LinkedHashMultimap<K, V> extends LinkedHashMultimapGwtSerializationDependencies<K, V> {
    public static final double VALUE_SET_LOAD_FACTOR = 1.0d;
    private static final long serialVersionUID = 1;
    public transient ValueEntry<K, V> multimapHeaderEntry;
    public transient int valueSetCapacity;

    /* loaded from: classes.dex */
    public static final class ValueEntry<K, V> extends ImmutableEntry<K, V> implements ValueSetLink<K, V> {
        public ValueEntry<K, V> nextInValueBucket;
        public ValueEntry<K, V> predecessorInMultimap;
        public ValueSetLink<K, V> predecessorInValueSet;
        public final int smearedValueHash;
        public ValueEntry<K, V> successorInMultimap;
        public ValueSetLink<K, V> successorInValueSet;

        public final ValueSetLink<K, V> getPredecessorInValueSet() {
            ValueSetLink<K, V> valueSetLink = this.predecessorInValueSet;
            Objects.requireNonNull(valueSetLink);
            return valueSetLink;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.ValueSetLink
        public final ValueSetLink<K, V> getSuccessorInValueSet() {
            ValueSetLink<K, V> valueSetLink = this.successorInValueSet;
            Objects.requireNonNull(valueSetLink);
            return valueSetLink;
        }

        public ValueEntry(K k, V v, int i, ValueEntry<K, V> valueEntry) {
            super(k, v);
            this.smearedValueHash = i;
            this.nextInValueBucket = valueEntry;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.ValueSetLink
        public final void setPredecessorInValueSet(ValueSetLink<K, V> valueSetLink) {
            this.predecessorInValueSet = valueSetLink;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.ValueSetLink
        public final void setSuccessorInValueSet(ValueSetLink<K, V> valueSetLink) {
            this.successorInValueSet = valueSetLink;
        }
    }

    /* loaded from: classes.dex */
    public final class ValueSet extends Sets.ImprovedAbstractSet<V> implements ValueSetLink<K, V> {
        public ValueEntry<K, V>[] hashTable;
        public final K key;
        public int size = 0;
        public int modCount = 0;
        public ValueSetLink<K, V> firstEntry = this;
        public ValueSetLink<K, V> lastEntry = this;

        public ValueSet(K k, int i) {
            this.key = k;
            int max = Math.max(i, 2);
            int highestOneBit = Integer.highestOneBit(max);
            if (max > ((int) (highestOneBit * 1.0d))) {
                int i2 = highestOneBit << 1;
                highestOneBit = i2 <= 0 ? 1073741824 : i2;
            }
            this.hashTable = new ValueEntry[highestOneBit];
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final void clear() {
            Arrays.fill(this.hashTable, (Object) null);
            this.size = 0;
            for (ValueSetLink<K, V> valueSetLink = this.firstEntry; valueSetLink != this; valueSetLink = valueSetLink.getSuccessorInValueSet()) {
                ValueEntry valueEntry = (ValueEntry) valueSetLink;
                Objects.requireNonNull(valueEntry);
                ValueEntry<K, V> valueEntry2 = valueEntry.predecessorInMultimap;
                Objects.requireNonNull(valueEntry2);
                ValueEntry<K, V> valueEntry3 = valueEntry.successorInMultimap;
                Objects.requireNonNull(valueEntry3);
                LinkedHashMultimap.succeedsInMultimap(valueEntry2, valueEntry3);
            }
            this.firstEntry = this;
            this.lastEntry = this;
            this.modCount++;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public final Iterator<V> iterator() {
            return new Iterator<V>() { // from class: com.google.common.collect.LinkedHashMultimap.ValueSet.1
                public int expectedModCount;
                public ValueSetLink<K, V> nextEntry;
                public ValueEntry<K, V> toRemove;

                {
                    this.nextEntry = ValueSet.this.firstEntry;
                    this.expectedModCount = ValueSet.this.modCount;
                }

                @Override // java.util.Iterator
                public final boolean hasNext() {
                    ValueSet valueSet = ValueSet.this;
                    if (valueSet.modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    } else if (this.nextEntry != valueSet) {
                        return true;
                    } else {
                        return false;
                    }
                }

                @Override // java.util.Iterator
                public final void remove() {
                    boolean z;
                    if (ValueSet.this.modCount == this.expectedModCount) {
                        if (this.toRemove != null) {
                            z = true;
                        } else {
                            z = false;
                        }
                        Preconditions.checkState(z, "no calls to next() since the last call to remove()");
                        ValueSet valueSet = ValueSet.this;
                        ValueEntry<K, V> valueEntry = this.toRemove;
                        Objects.requireNonNull(valueEntry);
                        valueSet.remove(valueEntry.value);
                        this.expectedModCount = ValueSet.this.modCount;
                        this.toRemove = null;
                        return;
                    }
                    throw new ConcurrentModificationException();
                }

                @Override // java.util.Iterator
                public final V next() {
                    if (hasNext()) {
                        ValueEntry<K, V> valueEntry = (ValueEntry) this.nextEntry;
                        Objects.requireNonNull(valueEntry);
                        V v = valueEntry.value;
                        this.toRemove = valueEntry;
                        this.nextEntry = valueEntry.getSuccessorInValueSet();
                        return v;
                    }
                    throw new NoSuchElementException();
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean add(V v) {
            ValueEntry<K, V>[] valueEntryArr;
            int smearedHash = Okio__OkioKt.smearedHash(v);
            int length = (valueEntryArr.length - 1) & smearedHash;
            ValueEntry<K, V> valueEntry = this.hashTable[length];
            ValueEntry<K, V> valueEntry2 = valueEntry;
            while (true) {
                boolean z = true;
                boolean z2 = false;
                if (valueEntry2 != null) {
                    if (valueEntry2.smearedValueHash != smearedHash || !R$dimen.equal(valueEntry2.value, v)) {
                        z = false;
                    }
                    if (z) {
                        return false;
                    }
                    valueEntry2 = valueEntry2.nextInValueBucket;
                } else {
                    ValueEntry<K, V> valueEntry3 = new ValueEntry<>(this.key, v, smearedHash, valueEntry);
                    ValueSetLink<K, V> valueSetLink = this.lastEntry;
                    valueSetLink.setSuccessorInValueSet(valueEntry3);
                    valueEntry3.predecessorInValueSet = valueSetLink;
                    valueEntry3.successorInValueSet = this;
                    this.lastEntry = valueEntry3;
                    ValueEntry<K, V> valueEntry4 = LinkedHashMultimap.this.multimapHeaderEntry;
                    Objects.requireNonNull(valueEntry4);
                    ValueEntry<K, V> valueEntry5 = valueEntry4.predecessorInMultimap;
                    Objects.requireNonNull(valueEntry5);
                    LinkedHashMultimap.succeedsInMultimap(valueEntry5, valueEntry3);
                    LinkedHashMultimap.succeedsInMultimap(valueEntry3, LinkedHashMultimap.this.multimapHeaderEntry);
                    ValueEntry<K, V>[] valueEntryArr2 = this.hashTable;
                    valueEntryArr2[length] = valueEntry3;
                    int i = this.size + 1;
                    this.size = i;
                    this.modCount++;
                    int length2 = valueEntryArr2.length;
                    if (i > length2 * 1.0d && length2 < 1073741824) {
                        z2 = true;
                    }
                    if (z2) {
                        int length3 = valueEntryArr2.length * 2;
                        ValueEntry<K, V>[] valueEntryArr3 = new ValueEntry[length3];
                        this.hashTable = valueEntryArr3;
                        int i2 = length3 - 1;
                        for (ValueSetLink<K, V> valueSetLink2 = this.firstEntry; valueSetLink2 != this; valueSetLink2 = valueSetLink2.getSuccessorInValueSet()) {
                            ValueEntry<K, V> valueEntry6 = (ValueEntry) valueSetLink2;
                            int i3 = valueEntry6.smearedValueHash & i2;
                            valueEntry6.nextInValueBucket = valueEntryArr3[i3];
                            valueEntryArr3[i3] = valueEntry6;
                        }
                    }
                    return true;
                }
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean contains(Object obj) {
            ValueEntry<K, V>[] valueEntryArr;
            int smearedHash = Okio__OkioKt.smearedHash(obj);
            ValueEntry<K, V> valueEntry = this.hashTable[(valueEntryArr.length - 1) & smearedHash];
            while (true) {
                boolean z = false;
                if (valueEntry == null) {
                    return false;
                }
                if (valueEntry.smearedValueHash == smearedHash && R$dimen.equal(valueEntry.value, obj)) {
                    z = true;
                }
                if (z) {
                    return true;
                }
                valueEntry = valueEntry.nextInValueBucket;
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        @CanIgnoreReturnValue
        public final boolean remove(Object obj) {
            ValueEntry<K, V>[] valueEntryArr;
            int smearedHash = Okio__OkioKt.smearedHash(obj);
            int length = (valueEntryArr.length - 1) & smearedHash;
            ValueEntry<K, V> valueEntry = this.hashTable[length];
            ValueEntry<K, V> valueEntry2 = null;
            while (true) {
                boolean z = false;
                if (valueEntry == null) {
                    return false;
                }
                if (valueEntry.smearedValueHash == smearedHash && R$dimen.equal(valueEntry.value, obj)) {
                    z = true;
                }
                if (z) {
                    if (valueEntry2 == null) {
                        this.hashTable[length] = valueEntry.nextInValueBucket;
                    } else {
                        valueEntry2.nextInValueBucket = valueEntry.nextInValueBucket;
                    }
                    ValueSetLink<K, V> predecessorInValueSet = valueEntry.getPredecessorInValueSet();
                    ValueSetLink<K, V> successorInValueSet = valueEntry.getSuccessorInValueSet();
                    predecessorInValueSet.setSuccessorInValueSet(successorInValueSet);
                    successorInValueSet.setPredecessorInValueSet(predecessorInValueSet);
                    ValueEntry<K, V> valueEntry3 = valueEntry.predecessorInMultimap;
                    Objects.requireNonNull(valueEntry3);
                    ValueEntry<K, V> valueEntry4 = valueEntry.successorInMultimap;
                    Objects.requireNonNull(valueEntry4);
                    LinkedHashMultimap.succeedsInMultimap(valueEntry3, valueEntry4);
                    this.size--;
                    this.modCount++;
                    return true;
                }
                valueEntry2 = valueEntry;
                valueEntry = valueEntry.nextInValueBucket;
            }
        }

        @Override // com.google.common.collect.LinkedHashMultimap.ValueSetLink
        public final void setPredecessorInValueSet(ValueSetLink<K, V> valueSetLink) {
            this.lastEntry = valueSetLink;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.ValueSetLink
        public final void setSuccessorInValueSet(ValueSetLink<K, V> valueSetLink) {
            this.firstEntry = valueSetLink;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.ValueSetLink
        public final ValueSetLink<K, V> getSuccessorInValueSet() {
            return this.firstEntry;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final int size() {
            return this.size;
        }
    }

    /* loaded from: classes.dex */
    public interface ValueSetLink<K, V> {
        ValueSetLink<K, V> getSuccessorInValueSet();

        void setPredecessorInValueSet(ValueSetLink<K, V> valueSetLink);

        void setSuccessorInValueSet(ValueSetLink<K, V> valueSetLink);
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap
    public final void clear() {
        for (Collection<V> collection : this.map.values()) {
            collection.clear();
        }
        this.map.clear();
        this.totalSize = 0;
        ValueEntry<K, V> valueEntry = this.multimapHeaderEntry;
        succeedsInMultimap(valueEntry, valueEntry);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        ValueEntry<K, V> valueEntry = new ValueEntry<>(null, null, 0, null);
        this.multimapHeaderEntry = valueEntry;
        succeedsInMultimap(valueEntry, valueEntry);
        this.valueSetCapacity = 2;
        int readInt = objectInputStream.readInt();
        CompactLinkedHashMap compactLinkedHashMap = new CompactLinkedHashMap(12);
        for (int i = 0; i < readInt; i++) {
            Object readObject = objectInputStream.readObject();
            compactLinkedHashMap.put(readObject, new ValueSet(readObject, this.valueSetCapacity));
        }
        int readInt2 = objectInputStream.readInt();
        for (int i2 = 0; i2 < readInt2; i2++) {
            Object readObject2 = objectInputStream.readObject();
            Object readObject3 = objectInputStream.readObject();
            Collection collection = (Collection) compactLinkedHashMap.get(readObject2);
            Objects.requireNonNull(collection);
            collection.add(readObject3);
        }
        this.map = compactLinkedHashMap;
        this.totalSize = 0;
        Iterator<V> it = ((CompactHashMap.ValuesView) compactLinkedHashMap.values()).iterator();
        while (it.hasNext()) {
            Collection collection2 = (Collection) it.next();
            if (!collection2.isEmpty()) {
                this.totalSize = collection2.size() + this.totalSize;
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public static <K, V> void succeedsInMultimap(ValueEntry<K, V> valueEntry, ValueEntry<K, V> valueEntry2) {
        Objects.requireNonNull(valueEntry);
        valueEntry.successorInMultimap = valueEntry2;
        Objects.requireNonNull(valueEntry2);
        valueEntry2.predecessorInMultimap = valueEntry;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        AbstractMapBasedMultimap.KeySet keySet = this.keySet;
        if (keySet == null) {
            keySet = new AbstractMapBasedMultimap.KeySet(this.map);
            this.keySet = keySet;
        }
        objectOutputStream.writeInt(keySet.size());
        AbstractMapBasedMultimap.KeySet keySet2 = this.keySet;
        if (keySet2 == null) {
            keySet2 = new AbstractMapBasedMultimap.KeySet(this.map);
            this.keySet = keySet2;
        }
        Iterator<K> it = keySet2.iterator();
        while (true) {
            AbstractMapBasedMultimap.KeySet.AnonymousClass1 r1 = (AbstractMapBasedMultimap.KeySet.AnonymousClass1) it;
            if (!r1.hasNext()) {
                break;
            }
            objectOutputStream.writeObject(r1.next());
        }
        objectOutputStream.writeInt(this.totalSize);
        AbstractMultimap.Entries entries = this.entries;
        if (entries == null) {
            entries = new AbstractMultimap.EntrySet(this);
            this.entries = entries;
        }
        for (Map.Entry entry : (Set) entries) {
            objectOutputStream.writeObject(entry.getKey());
            objectOutputStream.writeObject(entry.getValue());
        }
    }
}
