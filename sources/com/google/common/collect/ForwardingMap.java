package com.google.common.collect;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public abstract class ForwardingMap<K, V> extends ForwardingObject implements Map<K, V> {
    @Override // com.google.common.collect.ForwardingObject
    /* renamed from: delegate */
    public abstract Map<K, V> mo179delegate();

    @Override // java.util.Map
    public final boolean equals(Object obj) {
        if (obj == this || mo179delegate().equals(obj)) {
            return true;
        }
        return false;
    }

    @Override // java.util.Map
    public final void clear() {
        mo179delegate().clear();
    }

    @Override // java.util.Map
    public final boolean containsKey(Object obj) {
        return mo179delegate().containsKey(obj);
    }

    @Override // java.util.Map
    public final boolean containsValue(Object obj) {
        return mo179delegate().containsValue(obj);
    }

    @Override // java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        return mo179delegate().entrySet();
    }

    @Override // java.util.Map
    public final V get(Object obj) {
        return mo179delegate().get(obj);
    }

    @Override // java.util.Map
    public final int hashCode() {
        return mo179delegate().hashCode();
    }

    @Override // java.util.Map
    public final boolean isEmpty() {
        return mo179delegate().isEmpty();
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        return mo179delegate().keySet();
    }

    @Override // java.util.Map
    @CanIgnoreReturnValue
    public final V put(K k, V v) {
        return mo179delegate().put(k, v);
    }

    @Override // java.util.Map
    public final void putAll(Map<? extends K, ? extends V> map) {
        mo179delegate().putAll(map);
    }

    @Override // java.util.Map
    @CanIgnoreReturnValue
    public final V remove(Object obj) {
        return mo179delegate().remove(obj);
    }

    @Override // java.util.Map
    public final int size() {
        return mo179delegate().size();
    }

    @Override // java.util.Map
    public Collection<V> values() {
        return mo179delegate().values();
    }
}
