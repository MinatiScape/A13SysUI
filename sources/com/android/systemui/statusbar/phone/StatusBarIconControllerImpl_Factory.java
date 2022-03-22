package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.tuner.TunerService;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarIconControllerImpl_Factory implements Factory<StatusBarIconControllerImpl> {
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DemoModeController> demoModeControllerProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<TunerService> tunerServiceProvider;

    public static StatusBarIconControllerImpl_Factory create(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<DemoModeController> provider3, Provider<ConfigurationController> provider4, Provider<TunerService> provider5, Provider<DumpManager> provider6) {
        return new StatusBarIconControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new StatusBarIconControllerImpl(this.contextProvider.mo144get(), this.commandQueueProvider.mo144get(), this.demoModeControllerProvider.mo144get(), this.configurationControllerProvider.mo144get(), this.tunerServiceProvider.mo144get(), this.dumpManagerProvider.mo144get());
    }

    public StatusBarIconControllerImpl_Factory(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<DemoModeController> provider3, Provider<ConfigurationController> provider4, Provider<TunerService> provider5, Provider<DumpManager> provider6) {
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
        this.demoModeControllerProvider = provider3;
        this.configurationControllerProvider = provider4;
        this.tunerServiceProvider = provider5;
        this.dumpManagerProvider = provider6;
    }
}
