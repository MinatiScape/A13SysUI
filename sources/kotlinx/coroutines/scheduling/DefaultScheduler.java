package kotlinx.coroutines.scheduling;

import kotlin.io.CloseableKt;
import kotlinx.coroutines.internal.SystemPropsKt__SystemPropsKt;
/* compiled from: Dispatcher.kt */
/* loaded from: classes.dex */
public final class DefaultScheduler extends ExperimentalCoroutineDispatcher {
    public static final DefaultScheduler INSTANCE;
    public static final LimitingDispatcher IO;

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public final String toString() {
        return "Dispatchers.Default";
    }

    static {
        DefaultScheduler defaultScheduler = new DefaultScheduler();
        INSTANCE = defaultScheduler;
        int i = SystemPropsKt__SystemPropsKt.AVAILABLE_PROCESSORS;
        if (64 >= i) {
            i = 64;
        }
        IO = new LimitingDispatcher(defaultScheduler, CloseableKt.systemProp$default("kotlinx.coroutines.io.parallelism", i, 0, 0, 12));
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public final void close() {
        throw new UnsupportedOperationException("Dispatchers.Default cannot be closed");
    }
}
