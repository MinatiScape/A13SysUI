package kotlinx.coroutines.flow;

import androidx.preference.R$color;
import androidx.slice.SliceSpecs;
import java.util.Arrays;
import java.util.Objects;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.DisposableHandle;
import kotlinx.coroutines.DisposeOnCancel;
import kotlinx.coroutines.channels.BufferOverflow;
import kotlinx.coroutines.flow.internal.AbstractSharedFlow;
import kotlinx.coroutines.flow.internal.AbstractSharedFlowSlot;
import kotlinx.coroutines.internal.Symbol;
/* compiled from: SharedFlow.kt */
/* loaded from: classes.dex */
public final class SharedFlowImpl<T> extends AbstractSharedFlow<SharedFlowSlot> implements MutableSharedFlow<T>, Flow {
    public Object[] buffer;
    public final int bufferCapacity;
    public int bufferSize;
    public long minCollectorIndex;
    public final BufferOverflow onBufferOverflow;
    public int queueSize;
    public final int replay;
    public long replayIndex;

    /* compiled from: SharedFlow.kt */
    /* loaded from: classes.dex */
    public static final class Emitter implements DisposableHandle {
        public final Continuation<Unit> cont;
        public final SharedFlowImpl<?> flow;
        public long index;
        public final Object value;

        @Override // kotlinx.coroutines.DisposableHandle
        public final void dispose() {
            SharedFlowImpl<?> sharedFlowImpl = this.flow;
            Objects.requireNonNull(sharedFlowImpl);
            synchronized (sharedFlowImpl) {
                if (this.index >= sharedFlowImpl.getHead()) {
                    Object[] objArr = sharedFlowImpl.buffer;
                    Intrinsics.checkNotNull(objArr);
                    int i = (int) this.index;
                    if (objArr[(objArr.length - 1) & i] == this) {
                        objArr[i & (objArr.length - 1)] = SharedFlowKt.NO_VALUE;
                        sharedFlowImpl.cleanupTailLocked();
                    }
                }
            }
        }

        public Emitter(SharedFlowImpl sharedFlowImpl, long j, Object obj, CancellableContinuationImpl cancellableContinuationImpl) {
            this.flow = sharedFlowImpl;
            this.index = j;
            this.value = obj;
            this.cont = cancellableContinuationImpl;
        }
    }

    @Override // kotlinx.coroutines.flow.internal.AbstractSharedFlow
    public final AbstractSharedFlowSlot[] createSlotArray() {
        return new SharedFlowSlot[2];
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final Continuation<Unit>[] findSlotsToResumeLocked(Continuation<Unit>[] continuationArr) {
        Object[] objArr;
        SharedFlowSlot sharedFlowSlot;
        CancellableContinuationImpl cancellableContinuationImpl;
        int length = continuationArr.length;
        if (!(this.nCollectors == 0 || (objArr = this.slots) == null)) {
            int i = 0;
            int length2 = objArr.length;
            while (i < length2) {
                Object obj = objArr[i];
                i++;
                if (!(obj == null || (cancellableContinuationImpl = (sharedFlowSlot = (SharedFlowSlot) obj).cont) == null || tryPeekLocked(sharedFlowSlot) < 0)) {
                    int length3 = continuationArr.length;
                    continuationArr = continuationArr;
                    if (length >= length3) {
                        continuationArr = Arrays.copyOf(continuationArr, Math.max(2, continuationArr.length * 2));
                    }
                    continuationArr[length] = cancellableContinuationImpl;
                    sharedFlowSlot.cont = null;
                    length++;
                }
            }
        }
        return continuationArr;
    }

    public final Object[] growBuffer(Object[] objArr, int i, int i2) {
        boolean z;
        int i3 = 0;
        if (i2 > 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            Object[] objArr2 = new Object[i2];
            this.buffer = objArr2;
            if (objArr == null) {
                return objArr2;
            }
            long head = getHead();
            while (i3 < i) {
                int i4 = i3 + 1;
                int i5 = (int) (i3 + head);
                objArr2[i5 & (i2 - 1)] = objArr[(objArr.length - 1) & i5];
                i3 = i4;
            }
            return objArr2;
        }
        throw new IllegalStateException("Buffer size overflow".toString());
    }

    public final void updateBufferLocked(long j, long j2, long j3, long j4) {
        long min = Math.min(j2, j);
        boolean z = DebugKt.DEBUG;
        for (long head = getHead(); head < min; head = 1 + head) {
            Object[] objArr = this.buffer;
            Intrinsics.checkNotNull(objArr);
            objArr[((int) head) & (objArr.length - 1)] = null;
        }
        this.replayIndex = j;
        this.minCollectorIndex = j2;
        this.bufferSize = (int) (j3 - min);
        this.queueSize = (int) (j4 - j3);
        boolean z2 = DebugKt.DEBUG;
    }

    public final Object awaitValue(SharedFlowSlot sharedFlowSlot, Continuation<? super Unit> continuation) {
        Unit unit;
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(R$color.intercepted(continuation), 1);
        cancellableContinuationImpl.initCancellability();
        synchronized (this) {
            if (tryPeekLocked(sharedFlowSlot) < 0) {
                sharedFlowSlot.cont = cancellableContinuationImpl;
            } else {
                cancellableContinuationImpl.resumeWith(Unit.INSTANCE);
            }
            unit = Unit.INSTANCE;
        }
        Object result = cancellableContinuationImpl.getResult();
        if (result == CoroutineSingletons.COROUTINE_SUSPENDED) {
            return result;
        }
        return unit;
    }

    public final void cleanupTailLocked() {
        if (this.bufferCapacity != 0 || this.queueSize > 1) {
            Object[] objArr = this.buffer;
            Intrinsics.checkNotNull(objArr);
            while (this.queueSize > 0) {
                long head = getHead();
                int i = this.bufferSize;
                int i2 = this.queueSize;
                if (objArr[((int) ((head + (i + i2)) - 1)) & (objArr.length - 1)] == SharedFlowKt.NO_VALUE) {
                    this.queueSize = i2 - 1;
                    objArr[((int) (getHead() + this.bufferSize + this.queueSize)) & (objArr.length - 1)] = null;
                } else {
                    return;
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:43:0x00bc, code lost:
        throw r8.getCancellationException();
     */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0023  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0065  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x00bd A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x009e A[SYNTHETIC] */
    @Override // kotlinx.coroutines.flow.Flow
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector<? super T> r9, kotlin.coroutines.Continuation<? super kotlin.Unit> r10) {
        /*
            Method dump skipped, instructions count: 216
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.SharedFlowImpl.collect(kotlinx.coroutines.flow.FlowCollector, kotlin.coroutines.Continuation):java.lang.Object");
    }

    @Override // kotlinx.coroutines.flow.internal.AbstractSharedFlow
    public final SharedFlowSlot createSlot() {
        return new SharedFlowSlot();
    }

    public final void dropOldestLocked() {
        Object[] objArr;
        Object[] objArr2 = this.buffer;
        Intrinsics.checkNotNull(objArr2);
        objArr2[((int) getHead()) & (objArr2.length - 1)] = null;
        this.bufferSize--;
        long head = getHead() + 1;
        if (this.replayIndex < head) {
            this.replayIndex = head;
        }
        if (this.minCollectorIndex < head) {
            if (!(this.nCollectors == 0 || (objArr = this.slots) == null)) {
                int i = 0;
                int length = objArr.length;
                while (i < length) {
                    Object obj = objArr[i];
                    i++;
                    if (obj != null) {
                        SharedFlowSlot sharedFlowSlot = (SharedFlowSlot) obj;
                        long j = sharedFlowSlot.index;
                        if (j >= 0 && j < head) {
                            sharedFlowSlot.index = head;
                        }
                    }
                }
            }
            this.minCollectorIndex = head;
        }
        boolean z = DebugKt.DEBUG;
    }

    public final void enqueueLocked(Object obj) {
        int i = this.bufferSize + this.queueSize;
        Object[] objArr = this.buffer;
        if (objArr == null) {
            objArr = growBuffer(null, 0, 2);
        } else if (i >= objArr.length) {
            objArr = growBuffer(objArr, i, objArr.length * 2);
        }
        objArr[((int) (getHead() + i)) & (objArr.length - 1)] = obj;
    }

    public final long getHead() {
        return Math.min(this.minCollectorIndex, this.replayIndex);
    }

    @Override // kotlinx.coroutines.flow.MutableSharedFlow
    public final boolean tryEmit(T t) {
        int i;
        boolean z;
        Continuation<Unit>[] continuationArr = SliceSpecs.EMPTY_RESUMES;
        synchronized (this) {
            i = 0;
            if (tryEmitLocked(t)) {
                continuationArr = findSlotsToResumeLocked(continuationArr);
                z = true;
            } else {
                z = false;
            }
        }
        int length = continuationArr.length;
        while (i < length) {
            Continuation<Unit> continuation = continuationArr[i];
            i++;
            if (continuation != null) {
                continuation.resumeWith(Unit.INSTANCE);
            }
        }
        return z;
    }

    public final boolean tryEmitLocked(T t) {
        if (this.nCollectors == 0) {
            boolean z = DebugKt.DEBUG;
            if (this.replay != 0) {
                enqueueLocked(t);
                int i = this.bufferSize + 1;
                this.bufferSize = i;
                if (i > this.replay) {
                    dropOldestLocked();
                }
                this.minCollectorIndex = getHead() + this.bufferSize;
            }
            return true;
        }
        if (this.bufferSize >= this.bufferCapacity && this.minCollectorIndex <= this.replayIndex) {
            int ordinal = this.onBufferOverflow.ordinal();
            if (ordinal == 0) {
                return false;
            }
            if (ordinal == 2) {
                return true;
            }
        }
        enqueueLocked(t);
        int i2 = this.bufferSize + 1;
        this.bufferSize = i2;
        if (i2 > this.bufferCapacity) {
            dropOldestLocked();
        }
        long head = getHead() + this.bufferSize;
        long j = this.replayIndex;
        if (((int) (head - j)) > this.replay) {
            updateBufferLocked(1 + j, this.minCollectorIndex, getHead() + this.bufferSize, getHead() + this.bufferSize + this.queueSize);
        }
        return true;
    }

    public final long tryPeekLocked(SharedFlowSlot sharedFlowSlot) {
        long j = sharedFlowSlot.index;
        if (j < getHead() + this.bufferSize) {
            return j;
        }
        if (this.bufferCapacity <= 0 && j <= getHead() && this.queueSize != 0) {
            return j;
        }
        return -1L;
    }

    public final Object tryTakeValue(SharedFlowSlot sharedFlowSlot) {
        Object obj;
        Continuation<Unit>[] continuationArr = SliceSpecs.EMPTY_RESUMES;
        synchronized (this) {
            long tryPeekLocked = tryPeekLocked(sharedFlowSlot);
            if (tryPeekLocked < 0) {
                obj = SharedFlowKt.NO_VALUE;
            } else {
                long j = sharedFlowSlot.index;
                Object[] objArr = this.buffer;
                Intrinsics.checkNotNull(objArr);
                Object obj2 = objArr[((int) tryPeekLocked) & (objArr.length - 1)];
                if (obj2 instanceof Emitter) {
                    obj2 = ((Emitter) obj2).value;
                }
                sharedFlowSlot.index = tryPeekLocked + 1;
                continuationArr = updateCollectorIndexLocked$external__kotlinx_coroutines__android_common__kotlinx_coroutines(j);
                obj = obj2;
            }
        }
        int i = 0;
        int length = continuationArr.length;
        while (i < length) {
            Continuation<Unit> continuation = continuationArr[i];
            i++;
            if (continuation != null) {
                continuation.resumeWith(Unit.INSTANCE);
            }
        }
        return obj;
    }

    public final Continuation<Unit>[] updateCollectorIndexLocked$external__kotlinx_coroutines__android_common__kotlinx_coroutines(long j) {
        int i;
        long j2;
        boolean z;
        Object[] objArr;
        boolean z2 = DebugKt.DEBUG;
        if (j > this.minCollectorIndex) {
            return SliceSpecs.EMPTY_RESUMES;
        }
        long head = getHead();
        long j3 = this.bufferSize + head;
        long j4 = 1;
        if (this.bufferCapacity == 0 && this.queueSize > 0) {
            j3++;
        }
        if (!(this.nCollectors == 0 || (objArr = this.slots) == null)) {
            int length = objArr.length;
            int i2 = 0;
            while (i2 < length) {
                Object obj = objArr[i2];
                i2++;
                if (obj != null) {
                    long j5 = ((SharedFlowSlot) obj).index;
                    if (j5 >= 0 && j5 < j3) {
                        j3 = j5;
                    }
                }
            }
        }
        boolean z3 = DebugKt.DEBUG;
        if (j3 <= this.minCollectorIndex) {
            return SliceSpecs.EMPTY_RESUMES;
        }
        long head2 = getHead() + this.bufferSize;
        if (this.nCollectors > 0) {
            i = Math.min(this.queueSize, this.bufferCapacity - ((int) (head2 - j3)));
        } else {
            i = this.queueSize;
        }
        Continuation<Unit>[] continuationArr = SliceSpecs.EMPTY_RESUMES;
        long j6 = this.queueSize + head2;
        if (i > 0) {
            continuationArr = new Continuation[i];
            Object[] objArr2 = this.buffer;
            Intrinsics.checkNotNull(objArr2);
            long j7 = head2;
            int i3 = 0;
            while (true) {
                if (head2 >= j6) {
                    head2 = j7;
                    break;
                }
                long j8 = head2 + j4;
                int i4 = (int) head2;
                Object obj2 = objArr2[(objArr2.length - 1) & i4];
                Symbol symbol = SharedFlowKt.NO_VALUE;
                if (obj2 != symbol) {
                    Objects.requireNonNull(obj2, "null cannot be cast to non-null type kotlinx.coroutines.flow.SharedFlowImpl.Emitter");
                    Emitter emitter = (Emitter) obj2;
                    int i5 = i3 + 1;
                    continuationArr[i3] = emitter.cont;
                    objArr2[(objArr2.length - 1) & i4] = symbol;
                    objArr2[((int) j7) & (objArr2.length - 1)] = emitter.value;
                    long j9 = j7 + 1;
                    if (i5 >= i) {
                        head2 = j9;
                        break;
                    }
                    i3 = i5;
                    j7 = j9;
                    head2 = j8;
                    j4 = 1;
                } else {
                    head2 = j8;
                }
            }
        }
        int i6 = (int) (head2 - head);
        if (this.nCollectors == 0) {
            j2 = head2;
        } else {
            j2 = j3;
        }
        long max = Math.max(this.replayIndex, head2 - Math.min(this.replay, i6));
        if (this.bufferCapacity == 0 && max < j6) {
            Object[] objArr3 = this.buffer;
            Intrinsics.checkNotNull(objArr3);
            if (Intrinsics.areEqual(objArr3[((int) max) & (objArr3.length - 1)], SharedFlowKt.NO_VALUE)) {
                head2++;
                max++;
            }
        }
        updateBufferLocked(max, j2, head2, j6);
        cleanupTailLocked();
        if (continuationArr.length == 0) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            return findSlotsToResumeLocked(continuationArr);
        }
        return continuationArr;
    }

    public SharedFlowImpl(int i, int i2, BufferOverflow bufferOverflow) {
        this.replay = i;
        this.bufferCapacity = i2;
        this.onBufferOverflow = bufferOverflow;
    }

    @Override // kotlinx.coroutines.flow.MutableSharedFlow, kotlinx.coroutines.flow.FlowCollector
    public final Object emit(T t, Continuation<? super Unit> continuation) {
        Emitter emitter;
        Continuation<Unit>[] continuationArr;
        if (tryEmit(t)) {
            return Unit.INSTANCE;
        }
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(R$color.intercepted(continuation), 1);
        cancellableContinuationImpl.initCancellability();
        Continuation<Unit>[] continuationArr2 = SliceSpecs.EMPTY_RESUMES;
        synchronized (this) {
            if (tryEmitLocked(t)) {
                cancellableContinuationImpl.resumeWith(Unit.INSTANCE);
                continuationArr = findSlotsToResumeLocked(continuationArr2);
                emitter = null;
            } else {
                Emitter emitter2 = new Emitter(this, this.bufferSize + this.queueSize + getHead(), t, cancellableContinuationImpl);
                enqueueLocked(emitter2);
                this.queueSize++;
                if (this.bufferCapacity == 0) {
                    continuationArr2 = findSlotsToResumeLocked(continuationArr2);
                }
                continuationArr = continuationArr2;
                emitter = emitter2;
            }
        }
        if (emitter != null) {
            cancellableContinuationImpl.invokeOnCancellation(new DisposeOnCancel(emitter));
        }
        int i = 0;
        int length = continuationArr.length;
        while (i < length) {
            Continuation<Unit> continuation2 = continuationArr[i];
            i++;
            if (continuation2 != null) {
                continuation2.resumeWith(Unit.INSTANCE);
            }
        }
        Object result = cancellableContinuationImpl.getResult();
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        if (result != coroutineSingletons) {
            result = Unit.INSTANCE;
        }
        if (result == coroutineSingletons) {
            return result;
        }
        return Unit.INSTANCE;
    }
}
