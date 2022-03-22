package kotlinx.coroutines;
/* compiled from: EventLoop.common.kt */
/* loaded from: classes.dex */
public final class ThreadLocalEventLoop {
    public static final ThreadLocal<EventLoop> ref = new ThreadLocal<>();

    public static EventLoop getEventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        ThreadLocal<EventLoop> threadLocal = ref;
        EventLoop eventLoop = threadLocal.get();
        if (eventLoop != null) {
            return eventLoop;
        }
        BlockingEventLoop blockingEventLoop = new BlockingEventLoop(Thread.currentThread());
        threadLocal.set(blockingEventLoop);
        return blockingEventLoop;
    }
}
