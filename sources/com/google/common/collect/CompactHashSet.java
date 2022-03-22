package com.google.common.collect;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import androidx.leanback.R$layout;
import androidx.recyclerview.R$dimen;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import okio.Okio;
import okio.Okio__OkioKt;
/* loaded from: classes.dex */
class CompactHashSet<E> extends AbstractSet<E> implements Serializable {
    public static final double HASH_FLOODING_FPP = 0.001d;
    public transient Object[] elements;
    public transient int[] entries;
    public transient int metadata;
    public transient int size;
    public transient Object table;

    public CompactHashSet() {
        init(3);
    }

    public int adjustAfterRemove(int i, int i2) {
        return i - 1;
    }

    public void init(int i) {
        boolean z;
        if (i >= 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            this.metadata = R$layout.constrainToRange(i, 1);
            return;
        }
        throw new IllegalArgumentException("Expected size must be >= 0");
    }

    public void insertEntry(int i, E e, int i2, int i3) {
        requireEntries()[i] = (i2 & (~i3)) | (i3 & 0);
        requireElements()[i] = e;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public Object[] toArray() {
        if (needsAllocArrays()) {
            return new Object[0];
        }
        Set<E> delegateOrNull = delegateOrNull();
        return delegateOrNull != null ? delegateOrNull.toArray() : Arrays.copyOf(requireElements(), this.size);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    @CanIgnoreReturnValue
    public final boolean add(E e) {
        int min;
        int i;
        int i2;
        if (needsAllocArrays()) {
            allocArrays();
        }
        Set<E> delegateOrNull = delegateOrNull();
        if (delegateOrNull != null) {
            return delegateOrNull.add(e);
        }
        int[] requireEntries = requireEntries();
        Object[] requireElements = requireElements();
        int i3 = this.size;
        int i4 = i3 + 1;
        int smearedHash = Okio__OkioKt.smearedHash(e);
        int i5 = (1 << (this.metadata & 31)) - 1;
        int i6 = smearedHash & i5;
        Object obj = this.table;
        Objects.requireNonNull(obj);
        int tableGet = Okio.tableGet(obj, i6);
        if (tableGet != 0) {
            int i7 = ~i5;
            int i8 = smearedHash & i7;
            boolean z = false;
            int i9 = 0;
            while (true) {
                int i10 = tableGet - 1;
                int i11 = requireEntries[i10];
                int i12 = i11 & i7;
                if (i12 == i8 && R$dimen.equal(e, requireElements[i10])) {
                    return z;
                }
                int i13 = i11 & i5;
                int i14 = i9 + 1;
                if (i13 != 0) {
                    tableGet = i13;
                    i9 = i14;
                    z = false;
                } else if (i14 >= 9) {
                    return convertToHashFloodingResistantImplementation().add(e);
                } else {
                    if (i4 > i5) {
                        if (i5 < 32) {
                            i = 4;
                        } else {
                            i = 2;
                        }
                        i5 = resizeTable(i5, (i5 + 1) * i, smearedHash, i3);
                    } else {
                        requireEntries[i10] = (i4 & i5) | i12;
                    }
                }
            }
        } else if (i4 > i5) {
            if (i5 < 32) {
                i2 = 4;
            } else {
                i2 = 2;
            }
            i5 = resizeTable(i5, (i5 + 1) * i2, smearedHash, i3);
        } else {
            Object obj2 = this.table;
            Objects.requireNonNull(obj2);
            Okio.tableSet(obj2, i6, i4);
        }
        int length = requireEntries().length;
        if (i4 > length && (min = Math.min(1073741823, (Math.max(1, length >>> 1) + length) | 1)) != length) {
            resizeEntries(min);
        }
        insertEntry(i3, e, smearedHash, i5);
        this.size = i4;
        this.metadata += 32;
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @CanIgnoreReturnValue
    public Set<E> convertToHashFloodingResistantImplementation() {
        LinkedHashSet linkedHashSet = new LinkedHashSet(((1 << (this.metadata & 31)) - 1) + 1, 1.0f);
        int firstEntryIndex = firstEntryIndex();
        while (firstEntryIndex >= 0) {
            linkedHashSet.add(requireElements()[firstEntryIndex]);
            firstEntryIndex = getSuccessor(firstEntryIndex);
        }
        this.table = linkedHashSet;
        this.entries = null;
        this.elements = null;
        this.metadata += 32;
        return linkedHashSet;
    }

    public Set<E> delegateOrNull() {
        Object obj = this.table;
        if (obj instanceof Set) {
            return (Set) obj;
        }
        return null;
    }

    public int getSuccessor(int i) {
        int i2 = i + 1;
        if (i2 < this.size) {
            return i2;
        }
        return -1;
    }

    public void moveLastEntry(int i, int i2) {
        Object obj = this.table;
        Objects.requireNonNull(obj);
        int[] requireEntries = requireEntries();
        Object[] requireElements = requireElements();
        int size = size() - 1;
        if (i < size) {
            Object obj2 = requireElements[size];
            requireElements[i] = obj2;
            requireElements[size] = null;
            requireEntries[i] = requireEntries[size];
            requireEntries[size] = 0;
            int smearedHash = Okio__OkioKt.smearedHash(obj2) & i2;
            int tableGet = Okio.tableGet(obj, smearedHash);
            int i3 = size + 1;
            if (tableGet == i3) {
                Okio.tableSet(obj, smearedHash, i + 1);
                return;
            }
            while (true) {
                int i4 = tableGet - 1;
                int i5 = requireEntries[i4];
                int i6 = i5 & i2;
                if (i6 == i3) {
                    requireEntries[i4] = ((~i2) & i5) | ((i + 1) & i2);
                    return;
                }
                tableGet = i6;
            }
        } else {
            requireElements[i] = null;
            requireEntries[i] = 0;
        }
    }

    public boolean needsAllocArrays() {
        if (this.table == null) {
            return true;
        }
        return false;
    }

    public final Object[] requireElements() {
        Object[] objArr = this.elements;
        Objects.requireNonNull(objArr);
        return objArr;
    }

    public final int[] requireEntries() {
        int[] iArr = this.entries;
        Objects.requireNonNull(iArr);
        return iArr;
    }

    public CompactHashSet(int i) {
        init(i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        int readInt = objectInputStream.readInt();
        if (readInt >= 0) {
            init(readInt);
            for (int i = 0; i < readInt; i++) {
                add(objectInputStream.readObject());
            }
            return;
        }
        throw new InvalidObjectException(VendorAtomValue$$ExternalSyntheticOutline0.m("Invalid size: ", readInt));
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(size());
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            objectOutputStream.writeObject(it.next());
        }
    }

    @CanIgnoreReturnValue
    public int allocArrays() {
        Preconditions.checkState(needsAllocArrays(), "Arrays already allocated");
        int i = this.metadata;
        int max = Math.max(i + 1, 2);
        int highestOneBit = Integer.highestOneBit(max);
        if (max > ((int) (highestOneBit * 1.0d))) {
            int i2 = highestOneBit << 1;
            if (i2 <= 0) {
                i2 = 1073741824;
            }
            highestOneBit = i2;
        }
        int max2 = Math.max(4, highestOneBit);
        this.table = Okio.createTable(max2);
        this.metadata = ((32 - Integer.numberOfLeadingZeros(max2 - 1)) & 31) | (this.metadata & (-32));
        this.entries = new int[i];
        this.elements = new Object[i];
        return i;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        if (!needsAllocArrays()) {
            this.metadata += 32;
            Set<E> delegateOrNull = delegateOrNull();
            if (delegateOrNull != null) {
                this.metadata = R$layout.constrainToRange(size(), 3);
                delegateOrNull.clear();
                this.table = null;
                this.size = 0;
                return;
            }
            Arrays.fill(requireElements(), 0, this.size, (Object) null);
            Object obj = this.table;
            Objects.requireNonNull(obj);
            if (obj instanceof byte[]) {
                Arrays.fill((byte[]) obj, (byte) 0);
            } else if (obj instanceof short[]) {
                Arrays.fill((short[]) obj, (short) 0);
            } else {
                Arrays.fill((int[]) obj, 0);
            }
            Arrays.fill(requireEntries(), 0, this.size, 0);
            this.size = 0;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean contains(Object obj) {
        if (needsAllocArrays()) {
            return false;
        }
        Set<E> delegateOrNull = delegateOrNull();
        if (delegateOrNull != null) {
            return delegateOrNull.contains(obj);
        }
        int smearedHash = Okio__OkioKt.smearedHash(obj);
        int i = (1 << (this.metadata & 31)) - 1;
        Object obj2 = this.table;
        Objects.requireNonNull(obj2);
        int tableGet = Okio.tableGet(obj2, smearedHash & i);
        if (tableGet == 0) {
            return false;
        }
        int i2 = ~i;
        int i3 = smearedHash & i2;
        do {
            int i4 = tableGet - 1;
            int i5 = requireEntries()[i4];
            if ((i5 & i2) == i3 && R$dimen.equal(obj, requireElements()[i4])) {
                return true;
            }
            tableGet = i5 & i;
        } while (tableGet != 0);
        return false;
    }

    public int firstEntryIndex() {
        if (isEmpty()) {
            return -1;
        }
        return 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean isEmpty() {
        if (size() == 0) {
            return true;
        }
        return false;
    }

    public boolean isUsingHashFloodingResistance() {
        if (delegateOrNull() != null) {
            return true;
        }
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public final Iterator<E> iterator() {
        Set<E> delegateOrNull = delegateOrNull();
        if (delegateOrNull != null) {
            return delegateOrNull.iterator();
        }
        return new Iterator<E>() { // from class: com.google.common.collect.CompactHashSet.1
            public int currentIndex;
            public int expectedMetadata;
            public int indexToRemove = -1;

            {
                this.expectedMetadata = CompactHashSet.this.metadata;
                this.currentIndex = CompactHashSet.this.firstEntryIndex();
            }

            @Override // java.util.Iterator
            public final boolean hasNext() {
                if (this.currentIndex >= 0) {
                    return true;
                }
                return false;
            }

            @Override // java.util.Iterator
            public final E next() {
                if (CompactHashSet.this.metadata != this.expectedMetadata) {
                    throw new ConcurrentModificationException();
                } else if (hasNext()) {
                    int i = this.currentIndex;
                    this.indexToRemove = i;
                    CompactHashSet compactHashSet = CompactHashSet.this;
                    Objects.requireNonNull(compactHashSet);
                    E e = (E) compactHashSet.requireElements()[i];
                    this.currentIndex = CompactHashSet.this.getSuccessor(this.currentIndex);
                    return e;
                } else {
                    throw new NoSuchElementException();
                }
            }

            @Override // java.util.Iterator
            public final void remove() {
                boolean z;
                if (CompactHashSet.this.metadata == this.expectedMetadata) {
                    if (this.indexToRemove >= 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    Preconditions.checkState(z, "no calls to next() since the last call to remove()");
                    this.expectedMetadata += 32;
                    CompactHashSet compactHashSet = CompactHashSet.this;
                    int i = this.indexToRemove;
                    Objects.requireNonNull(compactHashSet);
                    compactHashSet.remove(compactHashSet.requireElements()[i]);
                    this.currentIndex = CompactHashSet.this.adjustAfterRemove(this.currentIndex, this.indexToRemove);
                    this.indexToRemove = -1;
                    return;
                }
                throw new ConcurrentModificationException();
            }
        };
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    @CanIgnoreReturnValue
    public final boolean remove(Object obj) {
        if (needsAllocArrays()) {
            return false;
        }
        Set<E> delegateOrNull = delegateOrNull();
        if (delegateOrNull != null) {
            return delegateOrNull.remove(obj);
        }
        int i = (1 << (this.metadata & 31)) - 1;
        Object obj2 = this.table;
        Objects.requireNonNull(obj2);
        int remove = Okio.remove(obj, null, i, obj2, requireEntries(), requireElements(), null);
        if (remove == -1) {
            return false;
        }
        moveLastEntry(remove, i);
        this.size--;
        this.metadata += 32;
        return true;
    }

    public void resizeEntries(int i) {
        this.entries = Arrays.copyOf(requireEntries(), i);
        this.elements = Arrays.copyOf(requireElements(), i);
    }

    @CanIgnoreReturnValue
    public final int resizeTable(int i, int i2, int i3, int i4) {
        Object createTable = Okio.createTable(i2);
        int i5 = i2 - 1;
        if (i4 != 0) {
            Okio.tableSet(createTable, i3 & i5, i4 + 1);
        }
        Object obj = this.table;
        Objects.requireNonNull(obj);
        int[] requireEntries = requireEntries();
        for (int i6 = 0; i6 <= i; i6++) {
            int tableGet = Okio.tableGet(obj, i6);
            while (tableGet != 0) {
                int i7 = tableGet - 1;
                int i8 = requireEntries[i7];
                int i9 = ((~i) & i8) | i6;
                int i10 = i9 & i5;
                int tableGet2 = Okio.tableGet(createTable, i10);
                Okio.tableSet(createTable, i10, tableGet);
                requireEntries[i7] = ((~i5) & i9) | (tableGet2 & i5);
                tableGet = i8 & i;
            }
        }
        this.table = createTable;
        this.metadata = ((32 - Integer.numberOfLeadingZeros(i5)) & 31) | (this.metadata & (-32));
        return i5;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final int size() {
        Set<E> delegateOrNull = delegateOrNull();
        if (delegateOrNull != null) {
            return delegateOrNull.size();
        }
        return this.size;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    @CanIgnoreReturnValue
    public <T> T[] toArray(T[] tArr) {
        if (needsAllocArrays()) {
            if (tArr.length > 0) {
                tArr[0] = null;
            }
            return tArr;
        }
        Set<E> delegateOrNull = delegateOrNull();
        if (delegateOrNull != null) {
            return (T[]) delegateOrNull.toArray(tArr);
        }
        Object[] requireElements = requireElements();
        int i = this.size;
        Preconditions.checkPositionIndexes(0, i + 0, requireElements.length);
        if (tArr.length < i) {
            tArr = (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), i));
        } else if (tArr.length > i) {
            tArr[i] = null;
        }
        System.arraycopy(requireElements, 0, tArr, 0, i);
        return tArr;
    }
}
