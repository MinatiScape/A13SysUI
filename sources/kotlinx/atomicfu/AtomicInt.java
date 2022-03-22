package kotlinx.atomicfu;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.atomicfu.TraceBase;
/* compiled from: AtomicFU.kt */
/* loaded from: classes.dex */
public final class AtomicInt {
    @Deprecated
    public static final AtomicIntegerFieldUpdater<AtomicInt> FU = AtomicIntegerFieldUpdater.newUpdater(AtomicInt.class, "value");
    public final TraceBase trace = TraceBase.None.INSTANCE;
    public volatile int value = 0;

    public final boolean compareAndSet(int i, int i2) {
        TraceBase traceBase;
        int i3 = InterceptorKt.$r8$clinit;
        boolean compareAndSet = FU.compareAndSet(this, i, i2);
        if (compareAndSet && (traceBase = this.trace) != TraceBase.None.INSTANCE) {
            Objects.requireNonNull(traceBase);
        }
        return compareAndSet;
    }

    public final int decrementAndGet() {
        int i = InterceptorKt.$r8$clinit;
        int decrementAndGet = FU.decrementAndGet(this);
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            Intrinsics.stringPlus("decAndGet():", Integer.valueOf(decrementAndGet));
            Objects.requireNonNull(traceBase);
        }
        return decrementAndGet;
    }

    public final int incrementAndGet() {
        int i = InterceptorKt.$r8$clinit;
        int incrementAndGet = FU.incrementAndGet(this);
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            Intrinsics.stringPlus("incAndGet():", Integer.valueOf(incrementAndGet));
            Objects.requireNonNull(traceBase);
        }
        return incrementAndGet;
    }

    public final void setValue(int i) {
        int i2 = InterceptorKt.$r8$clinit;
        this.value = i;
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            Objects.requireNonNull(traceBase);
        }
    }

    public final String toString() {
        return String.valueOf(this.value);
    }
}
