package kotlinx.coroutines;

import java.util.Objects;
import kotlinx.coroutines.scheduling.DefaultScheduler;
import kotlinx.coroutines.scheduling.LimitingDispatcher;
/* compiled from: Dispatchers.kt */
/* loaded from: classes.dex */
public final class Dispatchers {
    public static final ExecutorCoroutineDispatcher Default;
    public static final LimitingDispatcher IO;

    static {
        ExecutorCoroutineDispatcher executorCoroutineDispatcher;
        if (CoroutineContextKt.useCoroutinesScheduler) {
            executorCoroutineDispatcher = DefaultScheduler.INSTANCE;
        } else {
            executorCoroutineDispatcher = CommonPool.INSTANCE;
        }
        Default = executorCoroutineDispatcher;
        int i = Unconfined.$r8$clinit;
        Objects.requireNonNull(DefaultScheduler.INSTANCE);
        IO = DefaultScheduler.IO;
    }
}
