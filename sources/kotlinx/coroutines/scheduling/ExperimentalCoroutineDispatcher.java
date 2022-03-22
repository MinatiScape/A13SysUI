package kotlinx.coroutines.scheduling;

import androidx.coordinatorlayout.R$styleable;
import java.util.Objects;
import java.util.concurrent.RejectedExecutionException;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.DefaultExecutor;
import kotlinx.coroutines.ExecutorCoroutineDispatcher;
/* compiled from: Dispatcher.kt */
/* loaded from: classes.dex */
public class ExperimentalCoroutineDispatcher extends ExecutorCoroutineDispatcher {
    public CoroutineScheduler coroutineScheduler = new CoroutineScheduler(TasksKt.CORE_POOL_SIZE, TasksKt.MAX_POOL_SIZE, TasksKt.IDLE_WORKER_KEEP_ALIVE_NS, "DefaultDispatcher");

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public final void dispatch(CoroutineContext coroutineContext, Runnable runnable) {
        try {
            this.coroutineScheduler.dispatch(runnable, R$styleable.INSTANCE, false);
        } catch (RejectedExecutionException unused) {
            DefaultExecutor defaultExecutor = DefaultExecutor.INSTANCE;
            Objects.requireNonNull(defaultExecutor);
            defaultExecutor.enqueue(runnable);
        }
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public final void dispatchYield(CoroutineContext coroutineContext, Runnable runnable) {
        try {
            this.coroutineScheduler.dispatch(runnable, R$styleable.INSTANCE, true);
        } catch (RejectedExecutionException unused) {
            DefaultExecutor defaultExecutor = DefaultExecutor.INSTANCE;
            Objects.requireNonNull(defaultExecutor);
            defaultExecutor.enqueue(runnable);
        }
    }
}
