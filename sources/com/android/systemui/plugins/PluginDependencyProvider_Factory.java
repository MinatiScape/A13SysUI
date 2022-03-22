package com.android.systemui.plugins;

import com.android.systemui.shared.plugins.PluginManager;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PluginDependencyProvider_Factory implements Factory<PluginDependencyProvider> {
    private final Provider<PluginManager> managerLazyProvider;

    public static PluginDependencyProvider_Factory create(Provider<PluginManager> provider) {
        return new PluginDependencyProvider_Factory(provider);
    }

    public static PluginDependencyProvider newInstance(Lazy<PluginManager> lazy) {
        return new PluginDependencyProvider(lazy);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public PluginDependencyProvider mo144get() {
        return newInstance(DoubleCheck.lazy(this.managerLazyProvider));
    }

    public PluginDependencyProvider_Factory(Provider<PluginManager> provider) {
        this.managerLazyProvider = provider;
    }
}
