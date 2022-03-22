package kotlinx.coroutines;

import java.util.Objects;
import java.util.concurrent.locks.LockSupport;
import kotlin.coroutines.ContinuationInterceptor;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.jvm.functions.Function2;
/* loaded from: classes.dex */
public final class BuildersKt {
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v5, types: [kotlinx.coroutines.AbstractCoroutine, kotlinx.coroutines.StandaloneCoroutine] */
    public static StandaloneCoroutine launch$default(CoroutineScope coroutineScope, MainCoroutineDispatcher mainCoroutineDispatcher, CoroutineStart coroutineStart, Function2 function2, int i) {
        CoroutineContext coroutineContext;
        boolean z;
        LazyStandaloneCoroutine lazyStandaloneCoroutine;
        EmptyCoroutineContext emptyCoroutineContext = mainCoroutineDispatcher;
        if ((i & 1) != 0) {
            emptyCoroutineContext = EmptyCoroutineContext.INSTANCE;
        }
        if ((i & 2) != 0) {
            coroutineStart = CoroutineStart.DEFAULT;
        }
        boolean z2 = CoroutineContextKt.useCoroutinesScheduler;
        CoroutineContext plus = coroutineScope.getCoroutineContext().plus(emptyCoroutineContext);
        if (DebugKt.DEBUG) {
            coroutineContext = plus.plus(new CoroutineId(DebugKt.COROUTINE_ID.incrementAndGet()));
        } else {
            coroutineContext = plus;
        }
        ExecutorCoroutineDispatcher executorCoroutineDispatcher = Dispatchers.Default;
        if (plus != executorCoroutineDispatcher && plus.get(ContinuationInterceptor.Key.$$INSTANCE) == null) {
            coroutineContext = coroutineContext.plus(executorCoroutineDispatcher);
        }
        if (coroutineStart == CoroutineStart.LAZY) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            lazyStandaloneCoroutine = new LazyStandaloneCoroutine(coroutineContext, function2);
        } else {
            lazyStandaloneCoroutine = new StandaloneCoroutine(coroutineContext, true);
        }
        lazyStandaloneCoroutine.start(coroutineStart, lazyStandaloneCoroutine, function2);
        return lazyStandaloneCoroutine;
    }

    public static Object runBlocking$default(Function2 function2) throws InterruptedException {
        CoroutineContext coroutineContext;
        long j;
        EmptyCoroutineContext emptyCoroutineContext = EmptyCoroutineContext.INSTANCE;
        Thread currentThread = Thread.currentThread();
        ContinuationInterceptor.Key key = ContinuationInterceptor.Key.$$INSTANCE;
        Objects.requireNonNull(emptyCoroutineContext);
        CompletedExceptionally completedExceptionally = null;
        EventLoop eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines = ThreadLocalEventLoop.getEventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        Objects.requireNonNull(emptyCoroutineContext);
        if (DebugKt.DEBUG) {
            coroutineContext = eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.plus(new CoroutineId(DebugKt.COROUTINE_ID.incrementAndGet()));
        } else {
            coroutineContext = eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines;
        }
        ExecutorCoroutineDispatcher executorCoroutineDispatcher = Dispatchers.Default;
        if (eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines != executorCoroutineDispatcher && eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.get(key) == null) {
            coroutineContext = coroutineContext.plus(executorCoroutineDispatcher);
        }
        BlockingCoroutine blockingCoroutine = new BlockingCoroutine(coroutineContext, currentThread, eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines);
        blockingCoroutine.start(CoroutineStart.DEFAULT, blockingCoroutine, function2);
        EventLoop eventLoop = blockingCoroutine.eventLoop;
        if (eventLoop != null) {
            int i = EventLoop.$r8$clinit;
            eventLoop.incrementUseCount(false);
        }
        while (!Thread.interrupted()) {
            EventLoop eventLoop2 = blockingCoroutine.eventLoop;
            if (eventLoop2 == null) {
                j = Long.MAX_VALUE;
            } else {
                j = eventLoop2.processNextEvent();
            }
            if (!(blockingCoroutine.getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines() instanceof Incomplete)) {
                EventLoop eventLoop3 = blockingCoroutine.eventLoop;
                if (eventLoop3 != null) {
                    int i2 = EventLoop.$r8$clinit;
                    eventLoop3.decrementUseCount(false);
                }
                Object unboxState = JobSupportKt.unboxState(blockingCoroutine.getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines());
                if (unboxState instanceof CompletedExceptionally) {
                    completedExceptionally = (CompletedExceptionally) unboxState;
                }
                if (completedExceptionally == null) {
                    return unboxState;
                }
                throw completedExceptionally.cause;
            }
            LockSupport.parkNanos(blockingCoroutine, j);
        }
        InterruptedException interruptedException = new InterruptedException();
        blockingCoroutine.cancelImpl$external__kotlinx_coroutines__android_common__kotlinx_coroutines(interruptedException);
        throw interruptedException;
    }
}
