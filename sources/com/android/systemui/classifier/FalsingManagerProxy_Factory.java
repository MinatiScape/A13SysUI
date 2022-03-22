package com.android.systemui.classifier;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.util.DeviceConfigProxy;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FalsingManagerProxy_Factory implements Factory<FalsingManagerProxy> {
    public final Provider<BrightLineFalsingManager> brightLineFalsingManagerProvider;
    public final Provider<DeviceConfigProxy> deviceConfigProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<Executor> executorProvider;
    public final Provider<PluginManager> pluginManagerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new FalsingManagerProxy(this.pluginManagerProvider.mo144get(), this.executorProvider.mo144get(), this.deviceConfigProvider.mo144get(), this.dumpManagerProvider.mo144get(), this.brightLineFalsingManagerProvider);
    }

    public FalsingManagerProxy_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, BrightLineFalsingManager_Factory brightLineFalsingManager_Factory) {
        this.pluginManagerProvider = provider;
        this.executorProvider = provider2;
        this.deviceConfigProvider = provider3;
        this.dumpManagerProvider = provider4;
        this.brightLineFalsingManagerProvider = brightLineFalsingManager_Factory;
    }
}
