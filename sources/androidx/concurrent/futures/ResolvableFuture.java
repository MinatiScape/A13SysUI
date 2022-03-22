package androidx.concurrent.futures;
/* loaded from: classes.dex */
public final class ResolvableFuture<V> extends AbstractResolvableFuture<V> {
    public final boolean set(V v) {
        if (!AbstractResolvableFuture.ATOMIC_HELPER.casValue(this, null, AbstractResolvableFuture.NULL)) {
            return false;
        }
        AbstractResolvableFuture.complete(this);
        return true;
    }
}
