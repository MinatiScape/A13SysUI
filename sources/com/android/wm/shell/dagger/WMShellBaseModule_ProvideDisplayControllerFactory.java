package com.android.wm.shell.dagger;

import android.content.Context;
import android.view.IWindowManager;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellBaseModule_ProvideDisplayControllerFactory implements Factory<DisplayController> {
    public final Provider<Context> contextProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<IWindowManager> wmServiceProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DisplayController(this.contextProvider.mo144get(), this.wmServiceProvider.mo144get(), this.mainExecutorProvider.mo144get());
    }

    public WMShellBaseModule_ProvideDisplayControllerFactory(Provider<Context> provider, Provider<IWindowManager> provider2, Provider<ShellExecutor> provider3) {
        this.contextProvider = provider;
        this.wmServiceProvider = provider2;
        this.mainExecutorProvider = provider3;
    }
}
