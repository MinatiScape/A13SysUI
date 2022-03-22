package androidx.core.util;
/* loaded from: classes.dex */
public final class Pools$SynchronizedPool<T> extends Pools$SimplePool<T> {
    public final Object mLock = new Object();

    @Override // androidx.core.util.Pools$SimplePool
    public final T acquire() {
        T t;
        synchronized (this.mLock) {
            t = (T) super.acquire();
        }
        return t;
    }

    @Override // androidx.core.util.Pools$SimplePool
    public final boolean release(T t) {
        boolean release;
        synchronized (this.mLock) {
            release = super.release(t);
        }
        return release;
    }

    public Pools$SynchronizedPool(int i) {
        super(i);
    }
}
