package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
final class RegularImmutableMap<K, V> extends ImmutableMap<K, V> {
    public static final ImmutableMap<Object, Object> EMPTY = new RegularImmutableMap(null, new Object[0], 0);
    private static final long serialVersionUID = 0;
    public final transient Object[] alternatingKeysAndValues;
    public final transient Object hashTable;
    public final transient int size;

    /* loaded from: classes.dex */
    public static class EntrySet<K, V> extends ImmutableSet<Map.Entry<K, V>> {
        public final transient Object[] alternatingKeysAndValues;
        public final transient int keyOffset = 0;
        public final transient ImmutableMap<K, V> map;
        public final transient int size;

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection
        public final boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (value == null || !value.equals(this.map.get(key))) {
                return false;
            }
            return true;
        }

        @Override // com.google.common.collect.ImmutableSet
        public final ImmutableList<Map.Entry<K, V>> createAsList() {
            return new ImmutableList<Map.Entry<K, V>>() { // from class: com.google.common.collect.RegularImmutableMap.EntrySet.1
                @Override // java.util.List
                public final Object get(int i) {
                    Preconditions.checkElementIndex(i, EntrySet.this.size);
                    EntrySet entrySet = EntrySet.this;
                    int i2 = i * 2;
                    Object obj = entrySet.alternatingKeysAndValues[entrySet.keyOffset + i2];
                    Objects.requireNonNull(obj);
                    EntrySet entrySet2 = EntrySet.this;
                    Object obj2 = entrySet2.alternatingKeysAndValues[i2 + (entrySet2.keyOffset ^ 1)];
                    Objects.requireNonNull(obj2);
                    return new AbstractMap.SimpleImmutableEntry(obj, obj2);
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                public final int size() {
                    return EntrySet.this.size;
                }
            };
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        /* renamed from: iterator */
        public final UnmodifiableIterator<Map.Entry<K, V>> mo174iterator() {
            ImmutableList<Map.Entry<K, V>> asList = asList();
            Objects.requireNonNull(asList);
            return asList.listIterator(0);
        }

        public EntrySet(ImmutableMap immutableMap, Object[] objArr, int i) {
            this.map = immutableMap;
            this.alternatingKeysAndValues = objArr;
            this.size = i;
        }

        @Override // com.google.common.collect.ImmutableCollection
        public final int copyIntoArray(Object[] objArr) {
            return asList().copyIntoArray(objArr);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final int size() {
            return this.size;
        }
    }

    /* loaded from: classes.dex */
    public static final class KeySet<K> extends ImmutableSet<K> {
        public final transient ImmutableList<K> list;
        public final transient ImmutableMap<K, ?> map;

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection
        public final boolean contains(Object obj) {
            if (this.map.get(obj) != null) {
                return true;
            }
            return false;
        }

        @Override // com.google.common.collect.ImmutableCollection
        public final int copyIntoArray(Object[] objArr) {
            return this.list.copyIntoArray(objArr);
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        /* renamed from: iterator */
        public final UnmodifiableIterator<K> mo174iterator() {
            ImmutableList<K> immutableList = this.list;
            Objects.requireNonNull(immutableList);
            return immutableList.listIterator(0);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final int size() {
            return this.map.size();
        }

        public KeySet(ImmutableMap<K, ?> immutableMap, ImmutableList<K> immutableList) {
            this.map = immutableMap;
            this.list = immutableList;
        }

        @Override // com.google.common.collect.ImmutableSet
        public final ImmutableList<K> asList() {
            return this.list;
        }
    }

    /* loaded from: classes.dex */
    public static final class KeysOrValuesAsList extends ImmutableList<Object> {
        public final transient Object[] alternatingKeysAndValues;
        public final transient int offset;
        public final transient int size;

        @Override // java.util.List
        public final Object get(int i) {
            Preconditions.checkElementIndex(i, this.size);
            Object obj = this.alternatingKeysAndValues[(i * 2) + this.offset];
            Objects.requireNonNull(obj);
            return obj;
        }

        public KeysOrValuesAsList(Object[] objArr, int i, int i2) {
            this.alternatingKeysAndValues = objArr;
            this.offset = i;
            this.size = i2;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public final int size() {
            return this.size;
        }
    }

    public static IllegalArgumentException duplicateKeyException(Object obj, Object obj2, Object[] objArr, int i) {
        return new IllegalArgumentException("Multiple entries with same key: " + obj + "=" + obj2 + " and " + objArr[i] + "=" + objArr[i ^ 1]);
    }

    @Override // com.google.common.collect.ImmutableMap
    public final ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return new EntrySet(this, this.alternatingKeysAndValues, this.size);
    }

    @Override // com.google.common.collect.ImmutableMap
    public final ImmutableSet<K> createKeySet() {
        return new KeySet(this, new KeysOrValuesAsList(this.alternatingKeysAndValues, 0, this.size));
    }

    @Override // com.google.common.collect.ImmutableMap
    public final ImmutableCollection<V> createValues() {
        return new KeysOrValuesAsList(this.alternatingKeysAndValues, 1, this.size);
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x009e A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x009f A[RETURN] */
    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final V get(java.lang.Object r9) {
        /*
            r8 = this;
            java.lang.Object r0 = r8.hashTable
            java.lang.Object[] r1 = r8.alternatingKeysAndValues
            int r8 = r8.size
            r2 = 0
            if (r9 != 0) goto L_0x000a
            goto L_0x0020
        L_0x000a:
            r3 = 1
            if (r8 != r3) goto L_0x0023
            r8 = 0
            r8 = r1[r8]
            java.util.Objects.requireNonNull(r8)
            boolean r8 = r8.equals(r9)
            if (r8 == 0) goto L_0x0020
            r8 = r1[r3]
            java.util.Objects.requireNonNull(r8)
            goto L_0x009c
        L_0x0020:
            r8 = r2
            goto L_0x009c
        L_0x0023:
            if (r0 != 0) goto L_0x0026
            goto L_0x0020
        L_0x0026:
            boolean r8 = r0 instanceof byte[]
            r4 = -1
            if (r8 == 0) goto L_0x0052
            r8 = r0
            byte[] r8 = (byte[]) r8
            int r0 = r8.length
            int r5 = r0 + (-1)
            int r0 = r9.hashCode()
            int r0 = okio.Okio__OkioKt.smear(r0)
        L_0x0039:
            r0 = r0 & r5
            r4 = r8[r0]
            r6 = 255(0xff, float:3.57E-43)
            r4 = r4 & r6
            if (r4 != r6) goto L_0x0042
            goto L_0x0020
        L_0x0042:
            r6 = r1[r4]
            boolean r6 = r9.equals(r6)
            if (r6 == 0) goto L_0x004f
            r8 = r4 ^ 1
            r8 = r1[r8]
            goto L_0x009c
        L_0x004f:
            int r0 = r0 + 1
            goto L_0x0039
        L_0x0052:
            boolean r8 = r0 instanceof short[]
            if (r8 == 0) goto L_0x007e
            r8 = r0
            short[] r8 = (short[]) r8
            int r0 = r8.length
            int r5 = r0 + (-1)
            int r0 = r9.hashCode()
            int r0 = okio.Okio__OkioKt.smear(r0)
        L_0x0064:
            r0 = r0 & r5
            short r4 = r8[r0]
            r6 = 65535(0xffff, float:9.1834E-41)
            r4 = r4 & r6
            if (r4 != r6) goto L_0x006e
            goto L_0x0020
        L_0x006e:
            r6 = r1[r4]
            boolean r6 = r9.equals(r6)
            if (r6 == 0) goto L_0x007b
            r8 = r4 ^ 1
            r8 = r1[r8]
            goto L_0x009c
        L_0x007b:
            int r0 = r0 + 1
            goto L_0x0064
        L_0x007e:
            int[] r0 = (int[]) r0
            int r8 = r0.length
            int r8 = r8 - r3
            int r5 = r9.hashCode()
            int r5 = okio.Okio__OkioKt.smear(r5)
        L_0x008a:
            r5 = r5 & r8
            r6 = r0[r5]
            if (r6 != r4) goto L_0x0090
            goto L_0x0020
        L_0x0090:
            r7 = r1[r6]
            boolean r7 = r9.equals(r7)
            if (r7 == 0) goto L_0x00a0
            r8 = r6 ^ 1
            r8 = r1[r8]
        L_0x009c:
            if (r8 != 0) goto L_0x009f
            return r2
        L_0x009f:
            return r8
        L_0x00a0:
            int r5 = r5 + 1
            goto L_0x008a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.RegularImmutableMap.get(java.lang.Object):java.lang.Object");
    }

    public RegularImmutableMap(Object obj, Object[] objArr, int i) {
        this.hashTable = obj;
        this.alternatingKeysAndValues = objArr;
        this.size = i;
    }

    @Override // java.util.Map
    public final int size() {
        return this.size;
    }
}
