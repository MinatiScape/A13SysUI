package com.google.common.collect;

import com.google.common.collect.MapMakerInternalMap;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.ConcurrentMap;
/* loaded from: classes.dex */
public abstract class ForwardingConcurrentMap<K, V> extends ForwardingMap<K, V> implements ConcurrentMap<K, V> {
    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    @CanIgnoreReturnValue
    public final V replace(K k, V v) {
        return ((MapMakerInternalMap.AbstractSerializationProxy) this).delegate.replace(k, v);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    @CanIgnoreReturnValue
    public final V putIfAbsent(K k, V v) {
        return ((MapMakerInternalMap.AbstractSerializationProxy) this).delegate.putIfAbsent(k, v);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    @CanIgnoreReturnValue
    public final boolean remove(Object obj, Object obj2) {
        return ((MapMakerInternalMap.AbstractSerializationProxy) this).delegate.remove(obj, obj2);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    @CanIgnoreReturnValue
    public final boolean replace(K k, V v, V v2) {
        return ((MapMakerInternalMap.AbstractSerializationProxy) this).delegate.replace(k, v, v2);
    }
}
