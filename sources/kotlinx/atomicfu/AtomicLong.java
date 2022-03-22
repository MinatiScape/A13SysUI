package kotlinx.atomicfu;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import kotlinx.atomicfu.TraceBase;
/* compiled from: AtomicFU.kt */
/* loaded from: classes.dex */
public final class AtomicLong {
    @Deprecated
    public static final AtomicLongFieldUpdater<AtomicLong> FU = AtomicLongFieldUpdater.newUpdater(AtomicLong.class, "value");
    public final TraceBase trace = TraceBase.None.INSTANCE;
    public volatile long value;

    public AtomicLong(long j) {
        this.value = j;
    }

    public final long addAndGet(long j) {
        int i = InterceptorKt.$r8$clinit;
        long addAndGet = FU.addAndGet(this, j);
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            Objects.requireNonNull(traceBase);
        }
        return addAndGet;
    }

    public final boolean compareAndSet(long j, long j2) {
        TraceBase traceBase;
        int i = InterceptorKt.$r8$clinit;
        boolean compareAndSet = FU.compareAndSet(this, j, j2);
        if (compareAndSet && (traceBase = this.trace) != TraceBase.None.INSTANCE) {
            Objects.requireNonNull(traceBase);
        }
        return compareAndSet;
    }

    public final String toString() {
        return String.valueOf(this.value);
    }
}
