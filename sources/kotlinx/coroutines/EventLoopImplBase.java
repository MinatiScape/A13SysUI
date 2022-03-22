package kotlinx.coroutines;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import java.util.Objects;
import java.util.concurrent.locks.LockSupport;
import kotlin.coroutines.CoroutineContext;
import kotlinx.atomicfu.AtomicBoolean;
import kotlinx.atomicfu.AtomicLong;
import kotlinx.atomicfu.AtomicRef;
import kotlinx.atomicfu.InterceptorKt;
import kotlinx.atomicfu.TraceBase;
import kotlinx.coroutines.internal.ArrayQueue;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;
import kotlinx.coroutines.internal.Symbol;
import kotlinx.coroutines.internal.ThreadSafeHeap;
import kotlinx.coroutines.internal.ThreadSafeHeapNode;
/* compiled from: EventLoop.common.kt */
/* loaded from: classes.dex */
public abstract class EventLoopImplBase extends EventLoopImplPlatform {
    public final AtomicRef<Object> _queue = new AtomicRef<>(null);
    public final AtomicRef<DelayedTaskQueue> _delayed = new AtomicRef<>(null);
    public final AtomicBoolean _isCompleted = new AtomicBoolean(false);

    /* compiled from: EventLoop.common.kt */
    /* loaded from: classes.dex */
    public static abstract class DelayedTask implements Runnable, Comparable<DelayedTask>, DisposableHandle, ThreadSafeHeapNode {
        public Object _heap;
        public int index;
        public long nanoTime;

        @Override // kotlinx.coroutines.DisposableHandle
        public final synchronized void dispose() {
            DelayedTaskQueue delayedTaskQueue;
            Object obj = this._heap;
            Symbol symbol = EventLoop_commonKt.DISPOSED_TASK;
            if (obj != symbol) {
                ThreadSafeHeap threadSafeHeap = null;
                if (obj instanceof DelayedTaskQueue) {
                    delayedTaskQueue = (DelayedTaskQueue) obj;
                } else {
                    delayedTaskQueue = null;
                }
                if (delayedTaskQueue != null) {
                    synchronized (delayedTaskQueue) {
                        Object obj2 = this._heap;
                        if (obj2 instanceof ThreadSafeHeap) {
                            threadSafeHeap = (ThreadSafeHeap) obj2;
                        }
                        if (threadSafeHeap != null) {
                            int i = this.index;
                            boolean z = DebugKt.DEBUG;
                            delayedTaskQueue.removeAtImpl(i);
                        }
                    }
                }
                this._heap = symbol;
            }
        }

        @Override // java.lang.Comparable
        public final int compareTo(DelayedTask delayedTask) {
            int i = ((this.nanoTime - delayedTask.nanoTime) > 0L ? 1 : ((this.nanoTime - delayedTask.nanoTime) == 0L ? 0 : -1));
            if (i > 0) {
                return 1;
            }
            if (i < 0) {
                return -1;
            }
            return 0;
        }

        @Override // kotlinx.coroutines.internal.ThreadSafeHeapNode
        public final void setHeap(DelayedTaskQueue delayedTaskQueue) {
            boolean z;
            if (this._heap != EventLoop_commonKt.DISPOSED_TASK) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                this._heap = delayedTaskQueue;
                return;
            }
            throw new IllegalArgumentException("Failed requirement.".toString());
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Delayed[nanos=");
            m.append(this.nanoTime);
            m.append(']');
            return m.toString();
        }

        @Override // kotlinx.coroutines.internal.ThreadSafeHeapNode
        public final void setIndex(int i) {
            this.index = i;
        }
    }

    /* compiled from: EventLoop.common.kt */
    /* loaded from: classes.dex */
    public static final class DelayedTaskQueue extends ThreadSafeHeap<DelayedTask> {
        public long timeNow;

        public DelayedTaskQueue(long j) {
            this.timeNow = j;
        }
    }

    public final boolean enqueueImpl(Runnable runnable) {
        boolean z;
        AtomicRef<Object> atomicRef = this._queue;
        while (true) {
            Objects.requireNonNull(atomicRef);
            Object obj = atomicRef.value;
            AtomicBoolean atomicBoolean = this._isCompleted;
            Objects.requireNonNull(atomicBoolean);
            if (atomicBoolean._value != 0) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                return false;
            }
            if (obj == null) {
                if (this._queue.compareAndSet(null, runnable)) {
                    return true;
                }
            } else if (obj instanceof LockFreeTaskQueueCore) {
                LockFreeTaskQueueCore lockFreeTaskQueueCore = (LockFreeTaskQueueCore) obj;
                int addLast = lockFreeTaskQueueCore.addLast(runnable);
                if (addLast == 0) {
                    return true;
                }
                if (addLast == 1) {
                    this._queue.compareAndSet(obj, lockFreeTaskQueueCore.next());
                } else if (addLast == 2) {
                    return false;
                }
            } else if (obj == EventLoop_commonKt.CLOSED_EMPTY) {
                return false;
            } else {
                LockFreeTaskQueueCore lockFreeTaskQueueCore2 = new LockFreeTaskQueueCore(8, true);
                lockFreeTaskQueueCore2.addLast((Runnable) obj);
                lockFreeTaskQueueCore2.addLast(runnable);
                if (this._queue.compareAndSet(obj, lockFreeTaskQueueCore2)) {
                    return true;
                }
            }
        }
    }

    public final boolean isEmpty() {
        boolean z;
        boolean z2;
        ArrayQueue<DispatchedTask<?>> arrayQueue = this.unconfinedQueue;
        if (arrayQueue == null || arrayQueue.head == arrayQueue.tail) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            return false;
        }
        AtomicRef<DelayedTaskQueue> atomicRef = this._delayed;
        Objects.requireNonNull(atomicRef);
        DelayedTaskQueue delayedTaskQueue = atomicRef.value;
        if (delayedTaskQueue != null) {
            if (delayedTaskQueue.getSize() == 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (!z2) {
                return false;
            }
        }
        AtomicRef<Object> atomicRef2 = this._queue;
        Objects.requireNonNull(atomicRef2);
        Object obj = atomicRef2.value;
        if (obj != null) {
            if (obj instanceof LockFreeTaskQueueCore) {
                AtomicLong atomicLong = ((LockFreeTaskQueueCore) obj)._state;
                Objects.requireNonNull(atomicLong);
                long j = atomicLong.value;
                if (((int) ((1073741823 & j) >> 0)) != ((int) ((j & 1152921503533105152L) >> 30))) {
                    return false;
                }
            } else if (obj != EventLoop_commonKt.CLOSED_EMPTY) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x0083  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0099  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void schedule(long r13, kotlinx.coroutines.EventLoopImplBase.DelayedTask r15) {
        /*
            Method dump skipped, instructions count: 205
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.EventLoopImplBase.schedule(long, kotlinx.coroutines.EventLoopImplBase$DelayedTask):void");
    }

    @Override // kotlinx.coroutines.EventLoop
    public final void shutdown() {
        DelayedTask delayedTask;
        DelayedTask delayedTask2;
        ThreadLocalEventLoop.ref.set(null);
        AtomicBoolean atomicBoolean = this._isCompleted;
        Objects.requireNonNull(atomicBoolean);
        int i = InterceptorKt.$r8$clinit;
        atomicBoolean._value = 1;
        TraceBase traceBase = atomicBoolean.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            Objects.requireNonNull(traceBase);
        }
        boolean z = DebugKt.DEBUG;
        AtomicRef<Object> atomicRef = this._queue;
        while (true) {
            Objects.requireNonNull(atomicRef);
            Object obj = atomicRef.value;
            if (obj == null) {
                if (this._queue.compareAndSet(null, EventLoop_commonKt.CLOSED_EMPTY)) {
                    break;
                }
            } else if (obj instanceof LockFreeTaskQueueCore) {
                ((LockFreeTaskQueueCore) obj).close();
                break;
            } else if (obj == EventLoop_commonKt.CLOSED_EMPTY) {
                break;
            } else {
                LockFreeTaskQueueCore lockFreeTaskQueueCore = new LockFreeTaskQueueCore(8, true);
                lockFreeTaskQueueCore.addLast((Runnable) obj);
                if (this._queue.compareAndSet(obj, lockFreeTaskQueueCore)) {
                    break;
                }
            }
        }
        do {
        } while (processNextEvent() <= 0);
        long nanoTime = System.nanoTime();
        while (true) {
            AtomicRef<DelayedTaskQueue> atomicRef2 = this._delayed;
            Objects.requireNonNull(atomicRef2);
            DelayedTaskQueue delayedTaskQueue = atomicRef2.value;
            if (delayedTaskQueue == null) {
                delayedTask = null;
            } else {
                synchronized (delayedTaskQueue) {
                    if (delayedTaskQueue.getSize() > 0) {
                        delayedTask2 = delayedTaskQueue.removeAtImpl(0);
                    } else {
                        delayedTask2 = null;
                    }
                }
                delayedTask = delayedTask2;
            }
            if (delayedTask != null) {
                EventLoopImplPlatform.reschedule(nanoTime, delayedTask);
            } else {
                return;
            }
        }
    }

    public static final boolean access$isCompleted(DefaultExecutor defaultExecutor) {
        Objects.requireNonNull(defaultExecutor);
        AtomicBoolean atomicBoolean = defaultExecutor._isCompleted;
        Objects.requireNonNull(atomicBoolean);
        if (atomicBoolean._value != 0) {
            return true;
        }
        return false;
    }

    public final void enqueue(Runnable runnable) {
        if (enqueueImpl(runnable)) {
            Thread thread = getThread();
            if (Thread.currentThread() != thread) {
                LockSupport.unpark(thread);
                return;
            }
            return;
        }
        DefaultExecutor.INSTANCE.enqueue(runnable);
    }

    /* JADX WARN: Code restructure failed: missing block: B:47:0x0084, code lost:
        r8 = null;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:108:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x00b5  */
    /* JADX WARN: Type inference failed for: r0v10, types: [T extends kotlinx.coroutines.internal.ThreadSafeHeapNode & java.lang.Comparable<? super T>[]] */
    /* JADX WARN: Type inference failed for: r12v0, types: [kotlinx.coroutines.EventLoopImplBase, kotlinx.coroutines.EventLoop] */
    /* JADX WARN: Type inference failed for: r3v6 */
    /* JADX WARN: Type inference failed for: r8v16, types: [T extends kotlinx.coroutines.internal.ThreadSafeHeapNode & java.lang.Comparable<? super T>[]] */
    /* JADX WARN: Type inference failed for: r8v28 */
    /* JADX WARN: Unknown variable types count: 2 */
    @Override // kotlinx.coroutines.EventLoop
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final long processNextEvent() {
        /*
            Method dump skipped, instructions count: 276
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.EventLoopImplBase.processNextEvent():long");
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public final void dispatch(CoroutineContext coroutineContext, Runnable runnable) {
        enqueue(runnable);
    }
}
