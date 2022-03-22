package com.android.wm.shell.dagger;

import android.content.Context;
import android.os.Handler;
import com.android.wm.shell.common.HandlerExecutor;
import com.android.wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellConcurrencyModule_ProvideShellMainExecutorFactory implements Factory<ShellExecutor> {
    public final Provider<Context> contextProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<ShellExecutor> sysuiMainExecutorProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        Handler handler = this.mainHandlerProvider.mo144get();
        ShellExecutor shellExecutor = this.sysuiMainExecutorProvider.mo144get();
        if (this.contextProvider.mo144get().getResources().getBoolean(2131034135)) {
            shellExecutor = new HandlerExecutor(handler);
        }
        Objects.requireNonNull(shellExecutor, "Cannot return null from a non-@Nullable @Provides method");
        return shellExecutor;
    }

    public WMShellConcurrencyModule_ProvideShellMainExecutorFactory(Provider<Context> provider, Provider<Handler> provider2, Provider<ShellExecutor> provider3) {
        this.contextProvider = provider;
        this.mainHandlerProvider = provider2;
        this.sysuiMainExecutorProvider = provider3;
    }
}
