package kotlinx.coroutines.scheduling;

import java.util.Objects;
import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicLong;
import kotlinx.atomicfu.AtomicRef;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;
/* compiled from: Tasks.kt */
/* loaded from: classes.dex */
public final class GlobalQueue {
    public final AtomicRef<LockFreeTaskQueueCore<Task>> _cur = AtomicFU.atomic(new LockFreeTaskQueueCore(8, false));

    public final boolean addLast(Task task) {
        AtomicRef<LockFreeTaskQueueCore<Task>> atomicRef = this._cur;
        while (true) {
            Objects.requireNonNull(atomicRef);
            LockFreeTaskQueueCore<Task> lockFreeTaskQueueCore = atomicRef.value;
            int addLast = lockFreeTaskQueueCore.addLast(task);
            if (addLast == 0) {
                return true;
            }
            if (addLast == 1) {
                this._cur.compareAndSet(lockFreeTaskQueueCore, lockFreeTaskQueueCore.next());
            } else if (addLast == 2) {
                return false;
            }
        }
    }

    public final int getSize() {
        AtomicRef<LockFreeTaskQueueCore<Task>> atomicRef = this._cur;
        Objects.requireNonNull(atomicRef);
        LockFreeTaskQueueCore<Task> lockFreeTaskQueueCore = atomicRef.value;
        Objects.requireNonNull(lockFreeTaskQueueCore);
        AtomicLong atomicLong = lockFreeTaskQueueCore._state;
        Objects.requireNonNull(atomicLong);
        long j = atomicLong.value;
        return 1073741823 & (((int) ((j & 1152921503533105152L) >> 30)) - ((int) ((1073741823 & j) >> 0)));
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [kotlinx.coroutines.scheduling.Task, java.lang.Object] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final kotlinx.coroutines.scheduling.Task removeFirstOrNull() {
        /*
            r4 = this;
            kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.internal.LockFreeTaskQueueCore<kotlinx.coroutines.scheduling.Task>> r0 = r4._cur
        L_0x0002:
            java.util.Objects.requireNonNull(r0)
            T r1 = r0.value
            kotlinx.coroutines.internal.LockFreeTaskQueueCore r1 = (kotlinx.coroutines.internal.LockFreeTaskQueueCore) r1
            java.lang.Object r2 = r1.removeFirstOrNull()
            kotlinx.coroutines.internal.Symbol r3 = kotlinx.coroutines.internal.LockFreeTaskQueueCore.REMOVE_FROZEN
            if (r2 == r3) goto L_0x0012
            return r2
        L_0x0012:
            kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.internal.LockFreeTaskQueueCore<kotlinx.coroutines.scheduling.Task>> r2 = r4._cur
            kotlinx.coroutines.internal.LockFreeTaskQueueCore r3 = r1.next()
            r2.compareAndSet(r1, r3)
            goto L_0x0002
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.scheduling.GlobalQueue.removeFirstOrNull():java.lang.Object");
    }
}
