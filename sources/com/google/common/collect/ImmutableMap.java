package com.google.common.collect;

import com.google.common.collect.ImmutableCollection;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.DoNotCall;
import com.google.errorprone.annotations.DoNotMock;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
@DoNotMock("Use ImmutableMap.of or another implementation")
/* loaded from: classes.dex */
public abstract class ImmutableMap<K, V> implements Map<K, V>, Serializable {
    @LazyInit
    public transient ImmutableSet<Map.Entry<K, V>> entrySet;
    @LazyInit
    public transient ImmutableSet<K> keySet;
    @LazyInit
    public transient ImmutableCollection<V> values;

    @DoNotMock
    /* loaded from: classes.dex */
    public static class Builder<K, V> {
        public Object[] alternatingKeysAndValues;
        public int size = 0;

        /* JADX WARN: Code restructure failed: missing block: B:18:0x006a, code lost:
            r3[r8] = (byte) r5;
            r4 = r4 + 1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:32:0x00b0, code lost:
            r3[r8] = (short) r5;
            r4 = r4 + 1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:43:0x00ea, code lost:
            r3[r9] = r6;
            r4 = r4 + 1;
         */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r10v0, types: [int] */
        /* JADX WARN: Type inference failed for: r3v3, types: [int[]] */
        /* JADX WARN: Type inference failed for: r3v5 */
        /* JADX WARN: Type inference failed for: r9v7 */
        /* JADX WARN: Unknown variable types count: 1 */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public com.google.common.collect.ImmutableMap<K, V> buildOrThrow() {
            /*
                Method dump skipped, instructions count: 262
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.ImmutableMap.Builder.buildOrThrow():com.google.common.collect.ImmutableMap");
        }

        @CanIgnoreReturnValue
        public Builder<K, V> put(K k, V v) {
            int i = (this.size + 1) * 2;
            Object[] objArr = this.alternatingKeysAndValues;
            if (i > objArr.length) {
                this.alternatingKeysAndValues = Arrays.copyOf(objArr, ImmutableCollection.Builder.expandedCapacity(objArr.length, i));
            }
            CollectPreconditions.checkEntryNotNull(k, v);
            Object[] objArr2 = this.alternatingKeysAndValues;
            int i2 = this.size;
            int i3 = i2 * 2;
            objArr2[i3] = k;
            objArr2[i3 + 1] = v;
            this.size = i2 + 1;
            return this;
        }

        public Builder(int i) {
            this.alternatingKeysAndValues = new Object[i * 2];
        }

        public ImmutableMap<K, V> build() {
            return buildOrThrow();
        }
    }

    /* loaded from: classes.dex */
    public static class SerializedForm<K, V> implements Serializable {
        private static final long serialVersionUID = 0;
        private final Object keys;
        private final Object values;

        public Builder<K, V> makeBuilder(int i) {
            return new Builder<>(i);
        }

        public final Object readResolve() {
            Object obj = this.keys;
            if (!(obj instanceof ImmutableSet)) {
                Object[] objArr = (Object[]) obj;
                Object[] objArr2 = (Object[]) this.values;
                Builder<K, V> makeBuilder = makeBuilder(objArr.length);
                for (int i = 0; i < objArr.length; i++) {
                    makeBuilder.put((K) objArr[i], (V) objArr2[i]);
                }
                return makeBuilder.build();
            }
            ImmutableSet immutableSet = (ImmutableSet) obj;
            Builder<K, V> makeBuilder2 = makeBuilder(immutableSet.size());
            Iterator it = immutableSet.iterator();
            UnmodifiableIterator it2 = ((ImmutableCollection) this.values).iterator();
            while (it.hasNext()) {
                makeBuilder2.put((K) it.next(), (V) it2.next());
            }
            return makeBuilder2.build();
        }

        public SerializedForm(ImmutableMap<K, V> immutableMap) {
            Object[] objArr = new Object[immutableMap.size()];
            Object[] objArr2 = new Object[immutableMap.size()];
            UnmodifiableIterator<Map.Entry<K, V>> it = immutableMap.entrySet().iterator();
            int i = 0;
            while (it.hasNext()) {
                Map.Entry<K, V> next = it.next();
                objArr[i] = next.getKey();
                objArr2[i] = next.getValue();
                i++;
            }
            this.keys = objArr;
            this.values = objArr2;
        }
    }

    public abstract ImmutableSet<Map.Entry<K, V>> createEntrySet();

    public abstract ImmutableSet<K> createKeySet();

    public abstract ImmutableCollection<V> createValues();

    @Override // java.util.Map
    public abstract V get(Object obj);

    @Override // java.util.Map
    @DoNotCall("Always throws UnsupportedOperationException")
    @Deprecated
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    public ImmutableSet<Map.Entry<K, V>> entrySet() {
        ImmutableSet<Map.Entry<K, V>> immutableSet = this.entrySet;
        if (immutableSet != null) {
            return immutableSet;
        }
        ImmutableSet<Map.Entry<K, V>> createEntrySet = createEntrySet();
        this.entrySet = createEntrySet;
        return createEntrySet;
    }

    @Override // java.util.Map
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Map) {
            return entrySet().equals(((Map) obj).entrySet());
        }
        return false;
    }

    @Override // java.util.Map
    public ImmutableSet<K> keySet() {
        ImmutableSet<K> immutableSet = this.keySet;
        if (immutableSet != null) {
            return immutableSet;
        }
        ImmutableSet<K> createKeySet = createKeySet();
        this.keySet = createKeySet;
        return createKeySet;
    }

    @Override // java.util.Map
    @CanIgnoreReturnValue
    @DoNotCall("Always throws UnsupportedOperationException")
    @Deprecated
    public final V put(K k, V v) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    @DoNotCall("Always throws UnsupportedOperationException")
    @Deprecated
    public final void putAll(Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    @CanIgnoreReturnValue
    @Deprecated
    public final V remove(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    public ImmutableCollection<V> values() {
        ImmutableCollection<V> immutableCollection = this.values;
        if (immutableCollection != null) {
            return immutableCollection;
        }
        ImmutableCollection<V> createValues = createValues();
        this.values = createValues;
        return createValues;
    }

    Object writeReplace() {
        return new SerializedForm(this);
    }

    @Override // java.util.Map
    public final boolean containsKey(Object obj) {
        if (get(obj) != null) {
            return true;
        }
        return false;
    }

    @Override // java.util.Map
    public final boolean containsValue(Object obj) {
        return values().contains(obj);
    }

    @Override // java.util.Map
    public final V getOrDefault(Object obj, V v) {
        V v2 = get(obj);
        if (v2 != null) {
            return v2;
        }
        return v;
    }

    @Override // java.util.Map
    public final int hashCode() {
        return Sets.hashCodeImpl(entrySet());
    }

    @Override // java.util.Map
    public final boolean isEmpty() {
        if (size() == 0) {
            return true;
        }
        return false;
    }

    public final String toString() {
        return Maps.toStringImpl(this);
    }
}
