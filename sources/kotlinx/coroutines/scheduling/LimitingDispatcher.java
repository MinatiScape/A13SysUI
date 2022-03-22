package kotlinx.coroutines.scheduling;

import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import kotlin.coroutines.CoroutineContext;
import kotlinx.atomicfu.AtomicInt;
import kotlinx.coroutines.DefaultExecutor;
import kotlinx.coroutines.ExecutorCoroutineDispatcher;
/* compiled from: Dispatcher.kt */
/* loaded from: classes.dex */
public final class LimitingDispatcher extends ExecutorCoroutineDispatcher implements TaskContext, Executor {
    public final ExperimentalCoroutineDispatcher dispatcher;
    public final int parallelism;
    public final String name = "Dispatchers.IO";
    public final int taskMode = 1;
    public final ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();
    public final AtomicInt inFlightTasks = new AtomicInt();

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public final void dispatch(CoroutineContext coroutineContext, Runnable runnable) {
        dispatch(runnable, false);
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public final void dispatchYield(CoroutineContext coroutineContext, Runnable runnable) {
        dispatch(runnable, true);
    }

    @Override // java.util.concurrent.Executor
    public final void execute(Runnable runnable) {
        dispatch(runnable, false);
    }

    @Override // kotlinx.coroutines.scheduling.TaskContext
    public final void afterTask() {
        Task task;
        Runnable poll = this.queue.poll();
        if (poll != null) {
            ExperimentalCoroutineDispatcher experimentalCoroutineDispatcher = this.dispatcher;
            Objects.requireNonNull(experimentalCoroutineDispatcher);
            try {
                experimentalCoroutineDispatcher.coroutineScheduler.dispatch(poll, this, true);
            } catch (RejectedExecutionException unused) {
                DefaultExecutor defaultExecutor = DefaultExecutor.INSTANCE;
                Objects.requireNonNull(experimentalCoroutineDispatcher.coroutineScheduler);
                Objects.requireNonNull(TasksKt.schedulerTimeSource);
                long nanoTime = System.nanoTime();
                if (poll instanceof Task) {
                    task = (Task) poll;
                    task.submissionTime = nanoTime;
                    task.taskContext = this;
                } else {
                    task = new TaskImpl(poll, nanoTime, this);
                }
                defaultExecutor.enqueue(task);
            }
        } else {
            this.inFlightTasks.decrementAndGet();
            Runnable poll2 = this.queue.poll();
            if (poll2 != null) {
                dispatch(poll2, true);
            }
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public final void close() {
        throw new IllegalStateException("Close cannot be invoked on LimitingBlockingDispatcher".toString());
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x003a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void dispatch(java.lang.Runnable r4, boolean r5) {
        /*
            r3 = this;
        L_0x0000:
            kotlinx.atomicfu.AtomicInt r0 = r3.inFlightTasks
            int r0 = r0.incrementAndGet()
            int r1 = r3.parallelism
            if (r0 > r1) goto L_0x003a
            kotlinx.coroutines.scheduling.ExperimentalCoroutineDispatcher r0 = r3.dispatcher
            java.util.Objects.requireNonNull(r0)
            kotlinx.coroutines.scheduling.CoroutineScheduler r1 = r0.coroutineScheduler     // Catch: RejectedExecutionException -> 0x0015
            r1.dispatch(r4, r3, r5)     // Catch: RejectedExecutionException -> 0x0015
            goto L_0x0039
        L_0x0015:
            kotlinx.coroutines.DefaultExecutor r5 = kotlinx.coroutines.DefaultExecutor.INSTANCE
            kotlinx.coroutines.scheduling.CoroutineScheduler r0 = r0.coroutineScheduler
            java.util.Objects.requireNonNull(r0)
            kotlinx.coroutines.scheduling.NanoTimeSource r0 = kotlinx.coroutines.scheduling.TasksKt.schedulerTimeSource
            java.util.Objects.requireNonNull(r0)
            long r0 = java.lang.System.nanoTime()
            boolean r2 = r4 instanceof kotlinx.coroutines.scheduling.Task
            if (r2 == 0) goto L_0x0030
            kotlinx.coroutines.scheduling.Task r4 = (kotlinx.coroutines.scheduling.Task) r4
            r4.submissionTime = r0
            r4.taskContext = r3
            goto L_0x0036
        L_0x0030:
            kotlinx.coroutines.scheduling.TaskImpl r2 = new kotlinx.coroutines.scheduling.TaskImpl
            r2.<init>(r4, r0, r3)
            r4 = r2
        L_0x0036:
            r5.enqueue(r4)
        L_0x0039:
            return
        L_0x003a:
            java.util.concurrent.ConcurrentLinkedQueue<java.lang.Runnable> r0 = r3.queue
            r0.add(r4)
            kotlinx.atomicfu.AtomicInt r4 = r3.inFlightTasks
            int r4 = r4.decrementAndGet()
            int r0 = r3.parallelism
            if (r4 < r0) goto L_0x004a
            return
        L_0x004a:
            java.util.concurrent.ConcurrentLinkedQueue<java.lang.Runnable> r4 = r3.queue
            java.lang.Object r4 = r4.poll()
            java.lang.Runnable r4 = (java.lang.Runnable) r4
            if (r4 != 0) goto L_0x0000
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.scheduling.LimitingDispatcher.dispatch(java.lang.Runnable, boolean):void");
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public final String toString() {
        String str = this.name;
        if (str != null) {
            return str;
        }
        return super.toString() + "[dispatcher = " + this.dispatcher + ']';
    }

    public LimitingDispatcher(DefaultScheduler defaultScheduler, int i) {
        this.dispatcher = defaultScheduler;
        this.parallelism = i;
    }

    @Override // kotlinx.coroutines.scheduling.TaskContext
    public final int getTaskMode() {
        return this.taskMode;
    }
}
