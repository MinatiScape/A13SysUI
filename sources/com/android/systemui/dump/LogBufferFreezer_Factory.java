package com.android.systemui.dump;

import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LogBufferFreezer_Factory implements Factory<LogBufferFreezer> {
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<DelayableExecutor> executorProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new LogBufferFreezer(this.dumpManagerProvider.mo144get(), this.executorProvider.mo144get());
    }

    public LogBufferFreezer_Factory(Provider<DumpManager> provider, Provider<DelayableExecutor> provider2) {
        this.dumpManagerProvider = provider;
        this.executorProvider = provider2;
    }
}
