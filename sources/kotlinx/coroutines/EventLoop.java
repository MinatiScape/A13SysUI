package kotlinx.coroutines;

import kotlin.collections.ArraysKt___ArraysKt;
import kotlinx.coroutines.internal.ArrayQueue;
/* compiled from: EventLoop.common.kt */
/* loaded from: classes.dex */
public abstract class EventLoop extends CoroutineDispatcher {
    public static final /* synthetic */ int $r8$clinit = 0;
    public boolean shared;
    public ArrayQueue<DispatchedTask<?>> unconfinedQueue;
    public long useCount;

    public void shutdown() {
    }

    public final void decrementUseCount(boolean z) {
        long j;
        long j2 = this.useCount;
        if (z) {
            j = 4294967296L;
        } else {
            j = 1;
        }
        long j3 = j2 - j;
        this.useCount = j3;
        if (j3 <= 0) {
            boolean z2 = DebugKt.DEBUG;
            if (this.shared) {
                shutdown();
            }
        }
    }

    public final void dispatchUnconfined(DispatchedTask<?> dispatchedTask) {
        ArrayQueue<DispatchedTask<?>> arrayQueue = this.unconfinedQueue;
        if (arrayQueue == null) {
            arrayQueue = new ArrayQueue<>();
            this.unconfinedQueue = arrayQueue;
        }
        Object[] objArr = arrayQueue.elements;
        int i = arrayQueue.tail;
        objArr[i] = dispatchedTask;
        int length = (i + 1) & (objArr.length - 1);
        arrayQueue.tail = length;
        int i2 = arrayQueue.head;
        if (length == i2) {
            int length2 = objArr.length;
            Object[] objArr2 = new Object[length2 << 1];
            ArraysKt___ArraysKt.copyInto$default(objArr, objArr2, 0, i2, 0, 10);
            Object[] objArr3 = arrayQueue.elements;
            int length3 = objArr3.length;
            int i3 = arrayQueue.head;
            ArraysKt___ArraysKt.copyInto$default(objArr3, objArr2, length3 - i3, 0, i3, 4);
            arrayQueue.elements = objArr2;
            arrayQueue.head = 0;
            arrayQueue.tail = length2;
        }
    }

    public final void incrementUseCount(boolean z) {
        long j;
        long j2 = this.useCount;
        if (z) {
            j = 4294967296L;
        } else {
            j = 1;
        }
        this.useCount = j + j2;
        if (!z) {
            this.shared = true;
        }
    }

    public final boolean isUnconfinedLoopActive() {
        if (this.useCount >= 4294967296L) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r5v0, types: [java.lang.Object] */
    /* JADX WARN: Unknown variable types count: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean processUnconfinedEvent() {
        /*
            r6 = this;
            kotlinx.coroutines.internal.ArrayQueue<kotlinx.coroutines.DispatchedTask<?>> r6 = r6.unconfinedQueue
            r0 = 0
            if (r6 != 0) goto L_0x0006
            return r0
        L_0x0006:
            int r1 = r6.head
            int r2 = r6.tail
            r3 = 0
            r4 = 1
            if (r1 != r2) goto L_0x000f
            goto L_0x0022
        L_0x000f:
            java.lang.Object[] r2 = r6.elements
            r5 = r2[r1]
            r2[r1] = r3
            int r1 = r1 + r4
            int r2 = r2.length
            int r2 = r2 + (-1)
            r1 = r1 & r2
            r6.head = r1
            java.lang.String r6 = "null cannot be cast to non-null type T of kotlinx.coroutines.internal.ArrayQueue"
            java.util.Objects.requireNonNull(r5, r6)
            r3 = r5
        L_0x0022:
            kotlinx.coroutines.DispatchedTask r3 = (kotlinx.coroutines.DispatchedTask) r3
            if (r3 != 0) goto L_0x0027
            return r0
        L_0x0027:
            r3.run()
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.EventLoop.processUnconfinedEvent():boolean");
    }

    public long processNextEvent() {
        if (!processUnconfinedEvent()) {
            return Long.MAX_VALUE;
        }
        return 0L;
    }
}
