package kotlinx.coroutines.internal;

import java.util.Objects;
import kotlinx.atomicfu.AtomicArray;
import kotlinx.atomicfu.AtomicLong;
import kotlinx.atomicfu.AtomicRef;
import kotlinx.atomicfu.InterceptorKt;
import kotlinx.atomicfu.TraceBase;
/* compiled from: LockFreeTaskQueue.kt */
/* loaded from: classes.dex */
public final class LockFreeTaskQueueCore<E> {
    public static final Symbol REMOVE_FROZEN = new Symbol("REMOVE_FROZEN");
    public final AtomicRef<LockFreeTaskQueueCore<E>> _next = new AtomicRef<>(null);
    public final AtomicLong _state = new AtomicLong(0);
    public final AtomicArray<Object> array;
    public final int capacity;
    public final int mask;
    public final boolean singleConsumer;

    /* compiled from: LockFreeTaskQueue.kt */
    /* loaded from: classes.dex */
    public static final class Placeholder {
        public final int index;

        public Placeholder(int i) {
            this.index = i;
        }
    }

    public final int addLast(E e) {
        LockFreeTaskQueueCore<E> lockFreeTaskQueueCore = this;
        AtomicLong atomicLong = lockFreeTaskQueueCore._state;
        while (true) {
            Objects.requireNonNull(atomicLong);
            long j = atomicLong.value;
            if ((3458764513820540928L & j) == 0) {
                int i = (int) ((1073741823 & j) >> 0);
                int i2 = (int) ((1152921503533105152L & j) >> 30);
                int i3 = lockFreeTaskQueueCore.mask;
                if (((i2 + 2) & i3) == (i & i3)) {
                    return 1;
                }
                if (!lockFreeTaskQueueCore.singleConsumer) {
                    AtomicArray<Object> atomicArray = lockFreeTaskQueueCore.array;
                    Objects.requireNonNull(atomicArray);
                    AtomicRef<Object> atomicRef = atomicArray.array[i2 & i3];
                    Objects.requireNonNull(atomicRef);
                    if (atomicRef.value != null) {
                        int i4 = lockFreeTaskQueueCore.capacity;
                        if (i4 < 1024 || ((i2 - i) & 1073741823) > (i4 >> 1)) {
                            break;
                        }
                    }
                }
                if (lockFreeTaskQueueCore._state.compareAndSet(j, (((i2 + 1) & 1073741823) << 30) | ((-1152921503533105153L) & j))) {
                    AtomicArray<Object> atomicArray2 = lockFreeTaskQueueCore.array;
                    Objects.requireNonNull(atomicArray2);
                    atomicArray2.array[i2 & i3].setValue(e);
                    do {
                        AtomicLong atomicLong2 = lockFreeTaskQueueCore._state;
                        Objects.requireNonNull(atomicLong2);
                        if ((atomicLong2.value & 1152921504606846976L) == 0) {
                            return 0;
                        }
                        lockFreeTaskQueueCore = lockFreeTaskQueueCore.next();
                        AtomicArray<Object> atomicArray3 = lockFreeTaskQueueCore.array;
                        Objects.requireNonNull(atomicArray3);
                        AtomicRef<Object> atomicRef2 = atomicArray3.array[lockFreeTaskQueueCore.mask & i2];
                        Objects.requireNonNull(atomicRef2);
                        Object obj = atomicRef2.value;
                        if (!(obj instanceof Placeholder) || ((Placeholder) obj).index != i2) {
                            lockFreeTaskQueueCore = null;
                            continue;
                        } else {
                            AtomicArray<Object> atomicArray4 = lockFreeTaskQueueCore.array;
                            Objects.requireNonNull(atomicArray4);
                            atomicArray4.array[lockFreeTaskQueueCore.mask & i2].setValue(e);
                            continue;
                        }
                    } while (lockFreeTaskQueueCore != null);
                    return 0;
                }
            } else if ((2305843009213693952L & j) != 0) {
                return 2;
            } else {
                return 1;
            }
        }
        return 1;
    }

    public final boolean close() {
        long j;
        AtomicLong atomicLong = this._state;
        do {
            Objects.requireNonNull(atomicLong);
            j = atomicLong.value;
            if ((j & 2305843009213693952L) != 0) {
                return true;
            }
            if ((1152921504606846976L & j) != 0) {
                return false;
            }
        } while (!atomicLong.compareAndSet(j, 2305843009213693952L | j));
        return true;
    }

    public final LockFreeTaskQueueCore<E> next() {
        long j;
        AtomicLong atomicLong = this._state;
        while (true) {
            Objects.requireNonNull(atomicLong);
            j = atomicLong.value;
            if ((j & 1152921504606846976L) == 0) {
                long j2 = 1152921504606846976L | j;
                if (atomicLong.compareAndSet(j, j2)) {
                    j = j2;
                    break;
                }
            } else {
                break;
            }
        }
        AtomicRef<LockFreeTaskQueueCore<E>> atomicRef = this._next;
        while (true) {
            Objects.requireNonNull(atomicRef);
            LockFreeTaskQueueCore<E> lockFreeTaskQueueCore = atomicRef.value;
            if (lockFreeTaskQueueCore != null) {
                return lockFreeTaskQueueCore;
            }
            AtomicRef<LockFreeTaskQueueCore<E>> atomicRef2 = this._next;
            LockFreeTaskQueueCore<E> lockFreeTaskQueueCore2 = new LockFreeTaskQueueCore<>(this.capacity * 2, this.singleConsumer);
            int i = (int) ((1073741823 & j) >> 0);
            int i2 = (int) ((1152921503533105152L & j) >> 30);
            while (true) {
                int i3 = this.mask;
                int i4 = i & i3;
                if (i4 == (i3 & i2)) {
                    break;
                }
                AtomicArray<Object> atomicArray = this.array;
                Objects.requireNonNull(atomicArray);
                AtomicRef<Object> atomicRef3 = atomicArray.array[i4];
                Objects.requireNonNull(atomicRef3);
                Object obj = atomicRef3.value;
                if (obj == null) {
                    obj = new Placeholder(i);
                }
                AtomicArray<Object> atomicArray2 = lockFreeTaskQueueCore2.array;
                Objects.requireNonNull(atomicArray2);
                atomicArray2.array[lockFreeTaskQueueCore2.mask & i].setValue(obj);
                i++;
            }
            AtomicLong atomicLong2 = lockFreeTaskQueueCore2._state;
            Objects.requireNonNull(atomicLong2);
            int i5 = InterceptorKt.$r8$clinit;
            atomicLong2.value = (-1152921504606846977L) & j;
            TraceBase traceBase = atomicLong2.trace;
            if (traceBase != TraceBase.None.INSTANCE) {
                Objects.requireNonNull(traceBase);
            }
            atomicRef2.compareAndSet(null, lockFreeTaskQueueCore2);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0076, code lost:
        r1 = r0._state;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0078, code lost:
        java.util.Objects.requireNonNull(r1);
        r2 = r1.value;
        r4 = (int) ((r2 & r6) >> 0);
        r5 = kotlinx.coroutines.DebugKt.DEBUG;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x008b, code lost:
        if ((r2 & 1152921504606846976L) == 0) goto L_0x0092;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x008d, code lost:
        r0 = r0.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x009c, code lost:
        if (r0._state.compareAndSet(r2, (r2 & (-1073741824)) | r8) == false) goto L_0x00b5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x009e, code lost:
        r1 = r0.array;
        java.util.Objects.requireNonNull(r1);
        r1.array[r0.mask & r4].setValue(null);
        r0 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00ae, code lost:
        if (r0 != null) goto L_0x00b1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00b0, code lost:
        return r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00b1, code lost:
        r6 = 1073741823;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00b5, code lost:
        r6 = 1073741823;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object removeFirstOrNull() {
        /*
            r24 = this;
            r0 = r24
            kotlinx.atomicfu.AtomicLong r1 = r0._state
        L_0x0004:
            java.util.Objects.requireNonNull(r1)
            long r2 = r1.value
            r4 = 1152921504606846976(0x1000000000000000, double:1.2882297539194267E-231)
            long r6 = r2 & r4
            r8 = 0
            int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r6 == 0) goto L_0x0016
            kotlinx.coroutines.internal.Symbol r0 = kotlinx.coroutines.internal.LockFreeTaskQueueCore.REMOVE_FROZEN
            return r0
        L_0x0016:
            r6 = 1073741823(0x3fffffff, double:5.304989472E-315)
            long r10 = r2 & r6
            r12 = 0
            long r10 = r10 >> r12
            int r10 = (int) r10
            r13 = 1152921503533105152(0xfffffffc0000000, double:1.2882296003504729E-231)
            long r13 = r13 & r2
            r11 = 30
            long r13 = r13 >> r11
            int r11 = (int) r13
            int r13 = r0.mask
            r11 = r11 & r13
            r13 = r13 & r10
            r14 = 0
            if (r11 != r13) goto L_0x0030
            return r14
        L_0x0030:
            kotlinx.atomicfu.AtomicArray<java.lang.Object> r11 = r0.array
            java.util.Objects.requireNonNull(r11)
            kotlinx.atomicfu.AtomicRef<T>[] r11 = r11.array
            r11 = r11[r13]
            java.util.Objects.requireNonNull(r11)
            T r11 = r11.value
            if (r11 != 0) goto L_0x0045
            boolean r2 = r0.singleConsumer
            if (r2 == 0) goto L_0x0004
            return r14
        L_0x0045:
            boolean r13 = r11 instanceof kotlinx.coroutines.internal.LockFreeTaskQueueCore.Placeholder
            if (r13 == 0) goto L_0x004a
            return r14
        L_0x004a:
            int r13 = r10 + 1
            r15 = 1073741823(0x3fffffff, float:1.9999999)
            r13 = r13 & r15
            kotlinx.atomicfu.AtomicLong r15 = r0._state
            r16 = -1073741824(0xffffffffc0000000, double:NaN)
            long r18 = r2 & r16
            long r8 = (long) r13
            long r8 = r8 << r12
            long r4 = r18 | r8
            boolean r2 = r15.compareAndSet(r2, r4)
            if (r2 == 0) goto L_0x0071
            kotlinx.atomicfu.AtomicArray<java.lang.Object> r1 = r0.array
            int r0 = r0.mask
            r0 = r0 & r10
            java.util.Objects.requireNonNull(r1)
            kotlinx.atomicfu.AtomicRef<T>[] r1 = r1.array
            r0 = r1[r0]
            r0.setValue(r14)
            return r11
        L_0x0071:
            boolean r2 = r0.singleConsumer
            if (r2 != 0) goto L_0x0076
            goto L_0x0004
        L_0x0076:
            kotlinx.atomicfu.AtomicLong r1 = r0._state
        L_0x0078:
            java.util.Objects.requireNonNull(r1)
            long r2 = r1.value
            long r4 = r2 & r6
            long r4 = r4 >> r12
            int r4 = (int) r4
            boolean r5 = kotlinx.coroutines.DebugKt.DEBUG
            r18 = 1152921504606846976(0x1000000000000000, double:1.2882297539194267E-231)
            long r22 = r2 & r18
            r20 = 0
            int r5 = (r22 > r20 ? 1 : (r22 == r20 ? 0 : -1))
            if (r5 == 0) goto L_0x0092
            kotlinx.coroutines.internal.LockFreeTaskQueueCore r0 = r0.next()
            goto L_0x00ae
        L_0x0092:
            kotlinx.atomicfu.AtomicLong r5 = r0._state
            long r22 = r2 & r16
            long r6 = r22 | r8
            boolean r2 = r5.compareAndSet(r2, r6)
            if (r2 == 0) goto L_0x00b5
            kotlinx.atomicfu.AtomicArray<java.lang.Object> r1 = r0.array
            int r0 = r0.mask
            r0 = r0 & r4
            java.util.Objects.requireNonNull(r1)
            kotlinx.atomicfu.AtomicRef<T>[] r1 = r1.array
            r0 = r1[r0]
            r0.setValue(r14)
            r0 = r14
        L_0x00ae:
            if (r0 != 0) goto L_0x00b1
            return r11
        L_0x00b1:
            r6 = 1073741823(0x3fffffff, double:5.304989472E-315)
            goto L_0x0076
        L_0x00b5:
            r6 = 1073741823(0x3fffffff, double:5.304989472E-315)
            goto L_0x0078
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.LockFreeTaskQueueCore.removeFirstOrNull():java.lang.Object");
    }

    public LockFreeTaskQueueCore(int i, boolean z) {
        boolean z2;
        this.capacity = i;
        this.singleConsumer = z;
        int i2 = i - 1;
        this.mask = i2;
        this.array = new AtomicArray<>(i);
        boolean z3 = false;
        if (i2 <= 1073741823) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (z2) {
            if (!((i & i2) == 0 ? true : z3)) {
                throw new IllegalStateException("Check failed.".toString());
            }
            return;
        }
        throw new IllegalStateException("Check failed.".toString());
    }
}
