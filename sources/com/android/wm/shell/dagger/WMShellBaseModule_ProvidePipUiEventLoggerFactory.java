package com.android.wm.shell.dagger;

import android.content.pm.PackageManager;
import com.android.internal.logging.UiEventLogger;
import com.android.wm.shell.pip.PipUiEventLogger;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellBaseModule_ProvidePipUiEventLoggerFactory implements Factory<PipUiEventLogger> {
    public final Provider<PackageManager> packageManagerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new PipUiEventLogger(this.uiEventLoggerProvider.mo144get(), this.packageManagerProvider.mo144get());
    }

    public WMShellBaseModule_ProvidePipUiEventLoggerFactory(Provider<UiEventLogger> provider, Provider<PackageManager> provider2) {
        this.uiEventLoggerProvider = provider;
        this.packageManagerProvider = provider2;
    }
}
