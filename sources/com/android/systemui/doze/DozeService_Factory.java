package com.android.systemui.doze;

import com.android.systemui.doze.dagger.DozeComponent;
import com.android.systemui.shared.plugins.PluginManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeService_Factory implements Factory<DozeService> {
    public final Provider<DozeComponent.Builder> dozeComponentBuilderProvider;
    public final Provider<PluginManager> pluginManagerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DozeService(this.dozeComponentBuilderProvider.mo144get(), this.pluginManagerProvider.mo144get());
    }

    public DozeService_Factory(Provider<DozeComponent.Builder> provider, Provider<PluginManager> provider2) {
        this.dozeComponentBuilderProvider = provider;
        this.pluginManagerProvider = provider2;
    }
}
