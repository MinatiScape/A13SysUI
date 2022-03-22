package com.google.common.collect;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
class CompactLinkedHashMap<K, V> extends CompactHashMap<K, V> {
    private final boolean accessOrder;
    public transient int firstEntry;
    public transient int lastEntry;
    public transient long[] links;

    public CompactLinkedHashMap() {
        this(3);
    }

    public CompactLinkedHashMap(int i) {
        super(i);
        this.accessOrder = false;
    }

    @Override // com.google.common.collect.CompactHashMap
    public final void accessEntry(int i) {
        if (this.accessOrder) {
            long[] jArr = this.links;
            Objects.requireNonNull(jArr);
            setSucceeds(((int) (jArr[i] >>> 32)) - 1, getSuccessor(i));
            setSucceeds(this.lastEntry, i);
            setSucceeds(i, -2);
            incrementModCount();
        }
    }

    @Override // com.google.common.collect.CompactHashMap
    public final LinkedHashMap createHashFloodingResistantDelegate(int i) {
        return new LinkedHashMap(i, 1.0f, this.accessOrder);
    }

    @Override // com.google.common.collect.CompactHashMap
    public final int getSuccessor(int i) {
        long[] jArr = this.links;
        Objects.requireNonNull(jArr);
        return ((int) jArr[i]) - 1;
    }

    @Override // com.google.common.collect.CompactHashMap
    public final int adjustAfterRemove(int i, int i2) {
        if (i >= size()) {
            return i2;
        }
        return i;
    }

    @Override // com.google.common.collect.CompactHashMap
    public final int allocArrays() {
        int allocArrays = super.allocArrays();
        this.links = new long[allocArrays];
        return allocArrays;
    }

    @Override // com.google.common.collect.CompactHashMap, java.util.AbstractMap, java.util.Map
    public final void clear() {
        if (!needsAllocArrays()) {
            this.firstEntry = -2;
            this.lastEntry = -2;
            long[] jArr = this.links;
            if (jArr != null) {
                Arrays.fill(jArr, 0, size(), 0L);
            }
            super.clear();
        }
    }

    @Override // com.google.common.collect.CompactHashMap
    @CanIgnoreReturnValue
    public final Map<K, V> convertToHashFloodingResistantImplementation() {
        Map<K, V> convertToHashFloodingResistantImplementation = super.convertToHashFloodingResistantImplementation();
        this.links = null;
        return convertToHashFloodingResistantImplementation;
    }

    @Override // com.google.common.collect.CompactHashMap
    public final void init(int i) {
        super.init(i);
        this.firstEntry = -2;
        this.lastEntry = -2;
    }

    @Override // com.google.common.collect.CompactHashMap
    public final void insertEntry(int i, K k, V v, int i2, int i3) {
        super.insertEntry(i, k, v, i2, i3);
        setSucceeds(this.lastEntry, i);
        setSucceeds(i, -2);
    }

    @Override // com.google.common.collect.CompactHashMap
    public final void moveLastEntry(int i, int i2) {
        long[] jArr;
        long[] jArr2;
        int size = size() - 1;
        super.moveLastEntry(i, i2);
        Objects.requireNonNull(this.links);
        setSucceeds(((int) (jArr[i] >>> 32)) - 1, getSuccessor(i));
        if (i < size) {
            Objects.requireNonNull(this.links);
            setSucceeds(((int) (jArr2[size] >>> 32)) - 1, i);
            setSucceeds(i, getSuccessor(size));
        }
        long[] jArr3 = this.links;
        Objects.requireNonNull(jArr3);
        jArr3[size] = 0;
    }

    @Override // com.google.common.collect.CompactHashMap
    public final void resizeEntries(int i) {
        super.resizeEntries(i);
        long[] jArr = this.links;
        Objects.requireNonNull(jArr);
        this.links = Arrays.copyOf(jArr, i);
    }

    public final void setSucceeds(int i, int i2) {
        if (i == -2) {
            this.firstEntry = i2;
        } else {
            long[] jArr = this.links;
            Objects.requireNonNull(jArr);
            long j = (jArr[i] & (-4294967296L)) | ((i2 + 1) & 4294967295L);
            long[] jArr2 = this.links;
            Objects.requireNonNull(jArr2);
            jArr2[i] = j;
        }
        if (i2 == -2) {
            this.lastEntry = i;
            return;
        }
        long[] jArr3 = this.links;
        Objects.requireNonNull(jArr3);
        long[] jArr4 = this.links;
        Objects.requireNonNull(jArr4);
        jArr4[i2] = (4294967295L & jArr3[i2]) | ((i + 1) << 32);
    }

    @Override // com.google.common.collect.CompactHashMap
    public final int firstEntryIndex() {
        return this.firstEntry;
    }
}
