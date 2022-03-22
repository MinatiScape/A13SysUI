package com.google.common.util.concurrent;

import java.util.concurrent.Executor;
/* JADX WARN: Failed to restore enum class, 'enum' modifier removed */
/* loaded from: classes.dex */
final class DirectExecutor extends Enum<DirectExecutor> implements Executor {
    public static final /* synthetic */ DirectExecutor[] $VALUES;
    public static final DirectExecutor INSTANCE;

    @Override // java.lang.Enum
    public final String toString() {
        return "MoreExecutors.directExecutor()";
    }

    static {
        DirectExecutor directExecutor = new DirectExecutor();
        INSTANCE = directExecutor;
        $VALUES = new DirectExecutor[]{directExecutor};
    }

    public static DirectExecutor valueOf(String str) {
        return (DirectExecutor) Enum.valueOf(DirectExecutor.class, str);
    }

    public static DirectExecutor[] values() {
        return (DirectExecutor[]) $VALUES.clone();
    }

    @Override // java.util.concurrent.Executor
    public final void execute(Runnable runnable) {
        runnable.run();
    }
}
