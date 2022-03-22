package com.android.systemui.plugins;

import com.android.systemui.shared.plugins.PluginInstance;
import dagger.internal.Factory;
import java.util.List;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PluginsModule_ProvidesPluginInstanceFactoryFactory implements Factory<PluginInstance.Factory> {
    private final Provider<Boolean> isDebugProvider;
    private final Provider<List<String>> privilegedPluginsProvider;

    public static PluginsModule_ProvidesPluginInstanceFactoryFactory create(Provider<List<String>> provider, Provider<Boolean> provider2) {
        return new PluginsModule_ProvidesPluginInstanceFactoryFactory(provider, provider2);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public PluginInstance.Factory mo144get() {
        return providesPluginInstanceFactory(this.privilegedPluginsProvider.mo144get(), this.isDebugProvider.mo144get().booleanValue());
    }

    public PluginsModule_ProvidesPluginInstanceFactoryFactory(Provider<List<String>> provider, Provider<Boolean> provider2) {
        this.privilegedPluginsProvider = provider;
        this.isDebugProvider = provider2;
    }

    public static PluginInstance.Factory providesPluginInstanceFactory(List<String> list, boolean z) {
        PluginInstance.Factory providesPluginInstanceFactory = PluginsModule.providesPluginInstanceFactory(list, z);
        Objects.requireNonNull(providesPluginInstanceFactory, "Cannot return null from a non-@Nullable @Provides method");
        return providesPluginInstanceFactory;
    }
}
