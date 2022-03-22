package kotlin.collections;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import kotlin.jvm.functions.Function1;
/* compiled from: MapWithDefault.kt */
/* loaded from: classes.dex */
public final class MapWithDefaultImpl<K, V> implements MapWithDefault<K, V> {

    /* renamed from: default  reason: not valid java name */
    public final Function1<K, V> f9default;
    public final Map<K, V> map;

    @Override // java.util.Map
    public final void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Map
    public final V put(K k, V v) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Map
    public final void putAll(Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Map
    public final V remove(Object obj) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Map
    public final boolean containsKey(Object obj) {
        return this.map.containsKey(obj);
    }

    @Override // java.util.Map
    public final boolean containsValue(Object obj) {
        return this.map.containsValue(obj);
    }

    @Override // java.util.Map
    public final Set<Map.Entry<K, V>> entrySet() {
        return this.map.entrySet();
    }

    @Override // java.util.Map
    public final boolean equals(Object obj) {
        return this.map.equals(obj);
    }

    @Override // java.util.Map
    public final V get(Object obj) {
        return this.map.get(obj);
    }

    @Override // kotlin.collections.MapWithDefault
    public final V getOrImplicitDefault(K k) {
        Map<K, V> map = this.map;
        V v = map.get(k);
        if (v != null || map.containsKey(k)) {
            return v;
        }
        return this.f9default.invoke(k);
    }

    @Override // java.util.Map
    public final int hashCode() {
        return this.map.hashCode();
    }

    @Override // java.util.Map
    public final boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override // java.util.Map
    public final Set<K> keySet() {
        return this.map.keySet();
    }

    @Override // java.util.Map
    public final int size() {
        return this.map.size();
    }

    public final String toString() {
        return this.map.toString();
    }

    @Override // java.util.Map
    public final Collection<V> values() {
        return this.map.values();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public MapWithDefaultImpl(Map<K, ? extends V> map, Function1<? super K, ? extends V> function1) {
        this.map = map;
        this.f9default = function1;
    }

    @Override // kotlin.collections.MapWithDefault
    public final Map<K, V> getMap() {
        return this.map;
    }
}
