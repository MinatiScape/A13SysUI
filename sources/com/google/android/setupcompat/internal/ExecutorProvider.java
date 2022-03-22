package com.google.android.setupcompat.internal;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public final class ExecutorProvider<T extends Executor> {
    public static final ExecutorProvider<ExecutorService> setupCompatServiceInvoker = new ExecutorProvider<>(createSizeBoundedExecutor("SetupCompatServiceInvoker", 50));
    public final T executor;
    public T injectedExecutor;

    public static ExecutorService createSizeBoundedExecutor(final String str, int i) {
        return new ThreadPoolExecutor(1, 1, 0L, TimeUnit.SECONDS, new ArrayBlockingQueue(i), new ThreadFactory() { // from class: com.google.android.setupcompat.internal.ExecutorProvider$$ExternalSyntheticLambda0
            @Override // java.util.concurrent.ThreadFactory
            public final Thread newThread(Runnable runnable) {
                return new Thread(runnable, str);
            }
        });
    }

    public static void resetExecutors() {
        setupCompatServiceInvoker.injectedExecutor = null;
    }

    public ExecutorProvider(ExecutorService executorService) {
        this.executor = executorService;
    }

    public void injectExecutor(T t) {
        this.injectedExecutor = t;
    }
}
