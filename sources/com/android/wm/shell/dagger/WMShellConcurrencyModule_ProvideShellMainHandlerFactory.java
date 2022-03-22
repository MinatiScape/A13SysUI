package com.android.wm.shell.dagger;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideMainHandlerFactory;
import dagger.internal.Factory;
import dagger.internal.InstanceFactory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellConcurrencyModule_ProvideShellMainHandlerFactory implements Factory<Handler> {
    public final Provider<Context> contextProvider;
    public final Provider<HandlerThread> mainThreadProvider;
    public final Provider<Handler> sysuiMainHandlerProvider;

    public WMShellConcurrencyModule_ProvideShellMainHandlerFactory(Provider provider, InstanceFactory instanceFactory) {
        WMShellConcurrencyModule_ProvideMainHandlerFactory wMShellConcurrencyModule_ProvideMainHandlerFactory = WMShellConcurrencyModule_ProvideMainHandlerFactory.InstanceHolder.INSTANCE;
        this.contextProvider = provider;
        this.mainThreadProvider = instanceFactory;
        this.sysuiMainHandlerProvider = wMShellConcurrencyModule_ProvideMainHandlerFactory;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        HandlerThread handlerThread = this.mainThreadProvider.mo144get();
        Handler handler = this.sysuiMainHandlerProvider.mo144get();
        if (this.contextProvider.mo144get().getResources().getBoolean(2131034135)) {
            if (handlerThread == null) {
                handlerThread = new HandlerThread("wmshell.main", -4);
                handlerThread.start();
            }
            if (Build.IS_DEBUGGABLE) {
                handlerThread.getLooper().setTraceTag(32L);
                handlerThread.getLooper().setSlowLogThresholdMs(30L, 30L);
            }
            handler = Handler.createAsync(handlerThread.getLooper());
        }
        Objects.requireNonNull(handler, "Cannot return null from a non-@Nullable @Provides method");
        return handler;
    }
}
