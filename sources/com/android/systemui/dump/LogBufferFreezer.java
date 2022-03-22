package com.android.systemui.dump;

import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.concurrent.TimeUnit;
/* compiled from: LogBufferFreezer.kt */
/* loaded from: classes.dex */
public final class LogBufferFreezer {
    public final DumpManager dumpManager;
    public final DelayableExecutor executor;
    public final long freezeDuration;
    public Runnable pendingToken;

    public LogBufferFreezer(DumpManager dumpManager, DelayableExecutor delayableExecutor) {
        long millis = TimeUnit.MINUTES.toMillis(5L);
        this.dumpManager = dumpManager;
        this.executor = delayableExecutor;
        this.freezeDuration = millis;
    }
}
