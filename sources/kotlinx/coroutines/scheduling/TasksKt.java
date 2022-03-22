package kotlinx.coroutines.scheduling;

import java.util.concurrent.TimeUnit;
import kotlin.io.CloseableKt;
import kotlin.ranges.RangesKt___RangesKt;
import kotlinx.coroutines.internal.SystemPropsKt__SystemPropsKt;
/* compiled from: Tasks.kt */
/* loaded from: classes.dex */
public final class TasksKt {
    public static final int CORE_POOL_SIZE;
    public static final long IDLE_WORKER_KEEP_ALIVE_NS;
    public static final int MAX_POOL_SIZE;
    public static final long WORK_STEALING_TIME_RESOLUTION_NS = CloseableKt.systemProp("kotlinx.coroutines.scheduler.resolution.ns", 100000, 1, Long.MAX_VALUE);
    public static NanoTimeSource schedulerTimeSource;

    static {
        CloseableKt.systemProp$default("kotlinx.coroutines.scheduler.blocking.parallelism", 16, 0, 0, 12);
        int i = SystemPropsKt__SystemPropsKt.AVAILABLE_PROCESSORS;
        int i2 = 2;
        if (i >= 2) {
            i2 = i;
        }
        int systemProp$default = CloseableKt.systemProp$default("kotlinx.coroutines.scheduler.core.pool.size", i2, 1, 0, 8);
        CORE_POOL_SIZE = systemProp$default;
        MAX_POOL_SIZE = CloseableKt.systemProp$default("kotlinx.coroutines.scheduler.max.pool.size", RangesKt___RangesKt.coerceIn(i * 128, systemProp$default, 2097150), 0, 2097150, 4);
        IDLE_WORKER_KEEP_ALIVE_NS = TimeUnit.SECONDS.toNanos(CloseableKt.systemProp("kotlinx.coroutines.scheduler.keep.alive.sec", 60L, 1L, Long.MAX_VALUE));
        schedulerTimeSource = NanoTimeSource.INSTANCE;
    }
}
