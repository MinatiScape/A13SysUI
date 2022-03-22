package kotlinx.atomicfu;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlinx.atomicfu.TraceBase;
/* compiled from: AtomicFU.kt */
/* loaded from: classes.dex */
public final class AtomicRef<T> {
    @Deprecated
    public static final AtomicReferenceFieldUpdater<AtomicRef<?>, Object> FU = AtomicReferenceFieldUpdater.newUpdater(AtomicRef.class, Object.class, "value");
    public final TraceBase trace = TraceBase.None.INSTANCE;
    public volatile T value;

    /* JADX WARN: Multi-variable type inference failed */
    public AtomicRef(Object obj) {
        this.value = obj;
    }

    public final boolean compareAndSet(T t, T t2) {
        TraceBase traceBase;
        int i = InterceptorKt.$r8$clinit;
        boolean compareAndSet = FU.compareAndSet(this, t, t2);
        if (compareAndSet && (traceBase = this.trace) != TraceBase.None.INSTANCE) {
            Objects.toString(t);
            Objects.toString(t2);
            Objects.requireNonNull(traceBase);
        }
        return compareAndSet;
    }

    public final T getAndSet(T t) {
        int i = InterceptorKt.$r8$clinit;
        T t2 = (T) FU.getAndSet(this, t);
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            Objects.toString(t);
            Objects.toString(t2);
            Objects.requireNonNull(traceBase);
        }
        return t2;
    }

    public final void lazySet(T t) {
        int i = InterceptorKt.$r8$clinit;
        FU.lazySet(this, t);
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            Objects.toString(t);
            Objects.requireNonNull(traceBase);
        }
    }

    public final void setValue(T t) {
        int i = InterceptorKt.$r8$clinit;
        this.value = t;
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            Objects.toString(t);
            Objects.requireNonNull(traceBase);
        }
    }

    public final String toString() {
        return String.valueOf(this.value);
    }
}
