package kotlinx.coroutines.scheduling;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReferenceArray;
import kotlinx.atomicfu.AtomicInt;
import kotlinx.atomicfu.AtomicRef;
import kotlinx.coroutines.DebugKt;
/* compiled from: WorkQueue.kt */
/* loaded from: classes.dex */
public final class WorkQueue {
    public final AtomicReferenceArray<Task> buffer = new AtomicReferenceArray<>(128);
    public final AtomicRef<Task> lastScheduledTask = new AtomicRef<>(null);
    public final AtomicInt producerIndex = new AtomicInt();
    public final AtomicInt consumerIndex = new AtomicInt();
    public final AtomicInt blockingTasksInBuffer = new AtomicInt();

    public final Task add(Task task, boolean z) {
        if (z) {
            return addLast(task);
        }
        Task andSet = this.lastScheduledTask.getAndSet(task);
        if (andSet == null) {
            return null;
        }
        return addLast(andSet);
    }

    public final Task addLast(Task task) {
        boolean z = true;
        if (task.taskContext.getTaskMode() != 1) {
            z = false;
        }
        if (z) {
            this.blockingTasksInBuffer.incrementAndGet();
        }
        if (getBufferSize$external__kotlinx_coroutines__android_common__kotlinx_coroutines() == 127) {
            return task;
        }
        AtomicInt atomicInt = this.producerIndex;
        Objects.requireNonNull(atomicInt);
        int i = atomicInt.value & 127;
        while (this.buffer.get(i) != null) {
            Thread.yield();
        }
        this.buffer.lazySet(i, task);
        this.producerIndex.incrementAndGet();
        return null;
    }

    public final int getBufferSize$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        AtomicInt atomicInt = this.producerIndex;
        Objects.requireNonNull(atomicInt);
        int i = atomicInt.value;
        AtomicInt atomicInt2 = this.consumerIndex;
        Objects.requireNonNull(atomicInt2);
        return i - atomicInt2.value;
    }

    public final Task pollBuffer() {
        Task andSet;
        while (true) {
            AtomicInt atomicInt = this.consumerIndex;
            Objects.requireNonNull(atomicInt);
            int i = atomicInt.value;
            AtomicInt atomicInt2 = this.producerIndex;
            Objects.requireNonNull(atomicInt2);
            if (i - atomicInt2.value == 0) {
                return null;
            }
            int i2 = i & 127;
            if (this.consumerIndex.compareAndSet(i, i + 1) && (andSet = this.buffer.getAndSet(i2, null)) != null) {
                boolean z = true;
                if (andSet.taskContext.getTaskMode() != 1) {
                    z = false;
                }
                if (z) {
                    this.blockingTasksInBuffer.decrementAndGet();
                    boolean z2 = DebugKt.DEBUG;
                }
                return andSet;
            }
        }
    }

    public final long tryStealLastScheduled(WorkQueue workQueue, boolean z) {
        Task task;
        do {
            AtomicRef<Task> atomicRef = workQueue.lastScheduledTask;
            Objects.requireNonNull(atomicRef);
            task = atomicRef.value;
            if (task == null) {
                return -2L;
            }
            if (z) {
                boolean z2 = true;
                if (task.taskContext.getTaskMode() != 1) {
                    z2 = false;
                }
                if (!z2) {
                    return -2L;
                }
            }
            Objects.requireNonNull(TasksKt.schedulerTimeSource);
            long nanoTime = System.nanoTime() - task.submissionTime;
            long j = TasksKt.WORK_STEALING_TIME_RESOLUTION_NS;
            if (nanoTime < j) {
                return j - nanoTime;
            }
        } while (!workQueue.lastScheduledTask.compareAndSet(task, null));
        add(task, false);
        return -1L;
    }
}
