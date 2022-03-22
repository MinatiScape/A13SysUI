package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SecurityControllerImpl_Factory implements Factory<SecurityControllerImpl> {
    public final Provider<Executor> bgExecutorProvider;
    public final Provider<Handler> bgHandlerProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new SecurityControllerImpl(this.contextProvider.mo144get(), this.bgHandlerProvider.mo144get(), this.broadcastDispatcherProvider.mo144get(), this.bgExecutorProvider.mo144get(), this.dumpManagerProvider.mo144get());
    }

    public SecurityControllerImpl_Factory(Provider<Context> provider, Provider<Handler> provider2, Provider<BroadcastDispatcher> provider3, Provider<Executor> provider4, Provider<DumpManager> provider5) {
        this.contextProvider = provider;
        this.bgHandlerProvider = provider2;
        this.broadcastDispatcherProvider = provider3;
        this.bgExecutorProvider = provider4;
        this.dumpManagerProvider = provider5;
    }
}
