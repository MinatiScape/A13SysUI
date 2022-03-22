package kotlinx.atomicfu;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import kotlinx.atomicfu.TraceBase;
/* compiled from: AtomicFU.kt */
/* loaded from: classes.dex */
public final class AtomicBoolean {
    @Deprecated
    public static final AtomicIntegerFieldUpdater<AtomicBoolean> FU = AtomicIntegerFieldUpdater.newUpdater(AtomicBoolean.class, "_value");
    public volatile int _value;
    public final TraceBase trace = TraceBase.None.INSTANCE;

    public AtomicBoolean(boolean z) {
        this._value = z ? 1 : 0;
    }

    public final boolean compareAndSet() {
        TraceBase traceBase;
        int i = InterceptorKt.$r8$clinit;
        boolean compareAndSet = FU.compareAndSet(this, 0, 1);
        if (compareAndSet && (traceBase = this.trace) != TraceBase.None.INSTANCE) {
            Objects.requireNonNull(traceBase);
        }
        return compareAndSet;
    }

    public final String toString() {
        boolean z;
        if (this._value != 0) {
            z = true;
        } else {
            z = false;
        }
        return String.valueOf(z);
    }
}
