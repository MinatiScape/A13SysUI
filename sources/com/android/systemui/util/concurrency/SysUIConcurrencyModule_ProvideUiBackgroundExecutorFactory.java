package com.android.systemui.util.concurrency;

import dagger.internal.Factory;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/* loaded from: classes.dex */
public final class SysUIConcurrencyModule_ProvideUiBackgroundExecutorFactory implements Factory<Executor> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final SysUIConcurrencyModule_ProvideUiBackgroundExecutorFactory INSTANCE = new SysUIConcurrencyModule_ProvideUiBackgroundExecutorFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
        Objects.requireNonNull(newSingleThreadExecutor, "Cannot return null from a non-@Nullable @Provides method");
        return newSingleThreadExecutor;
    }
}
