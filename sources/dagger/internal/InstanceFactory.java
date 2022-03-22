package dagger.internal;

import dagger.Lazy;
import java.util.Objects;
/* loaded from: classes.dex */
public final class InstanceFactory<T> implements Factory<T>, Lazy<T> {
    public static final InstanceFactory<Object> NULL_INSTANCE_FACTORY = new InstanceFactory<>(null);
    public final T instance;

    public static InstanceFactory create(Object obj) {
        Objects.requireNonNull(obj, "instance cannot be null");
        return new InstanceFactory(obj);
    }

    public InstanceFactory(T t) {
        this.instance = t;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final T mo144get() {
        return this.instance;
    }
}
