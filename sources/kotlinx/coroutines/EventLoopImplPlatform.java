package kotlinx.coroutines;

import kotlinx.coroutines.EventLoopImplBase;
/* compiled from: EventLoop.kt */
/* loaded from: classes.dex */
public abstract class EventLoopImplPlatform extends EventLoop {
    public abstract Thread getThread();

    public static void reschedule(long j, EventLoopImplBase.DelayedTask delayedTask) {
        boolean z = DebugKt.DEBUG;
        DefaultExecutor.INSTANCE.schedule(j, delayedTask);
    }
}
