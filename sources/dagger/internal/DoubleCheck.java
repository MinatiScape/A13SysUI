package dagger.internal;

import dagger.Lazy;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DoubleCheck<T> implements Provider<T>, Lazy<T> {
    public static final Object UNINITIALIZED = new Object();
    public volatile Object instance = UNINITIALIZED;
    public volatile Provider<T> provider;

    public static <P extends Provider<T>, T> Lazy<T> lazy(P p) {
        if (p instanceof Lazy) {
            return (Lazy) p;
        }
        Objects.requireNonNull(p);
        return new DoubleCheck(p);
    }

    public static Object reentrantCheck(Object obj, Object obj2) {
        boolean z;
        if (obj != UNINITIALIZED) {
            z = true;
        } else {
            z = false;
        }
        if (!z || obj == obj2) {
            return obj2;
        }
        throw new IllegalStateException("Scoped provider was invoked recursively returning different results: " + obj + " & " + obj2 + ". This is likely due to a circular dependency.");
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final T mo144get() {
        T t = (T) this.instance;
        Object obj = UNINITIALIZED;
        if (t == obj) {
            synchronized (this) {
                t = this.instance;
                if (t == obj) {
                    t = this.provider.mo144get();
                    reentrantCheck(this.instance, t);
                    this.instance = t;
                    this.provider = null;
                }
            }
        }
        return t;
    }

    public DoubleCheck(Provider<T> provider) {
        this.provider = provider;
    }

    public static Provider provider(Factory factory) {
        Objects.requireNonNull(factory);
        if (factory instanceof DoubleCheck) {
            return factory;
        }
        return new DoubleCheck(factory);
    }
}
