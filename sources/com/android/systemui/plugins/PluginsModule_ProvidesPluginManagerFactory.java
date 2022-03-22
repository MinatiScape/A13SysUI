package com.android.systemui.plugins;

import android.content.Context;
import com.android.systemui.shared.plugins.PluginActionManager;
import com.android.systemui.shared.plugins.PluginEnabler;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.shared.plugins.PluginPrefs;
import dagger.internal.Factory;
import java.lang.Thread;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PluginsModule_ProvidesPluginManagerFactory implements Factory<PluginManager> {
    private final Provider<Context> contextProvider;
    private final Provider<Boolean> debugProvider;
    private final Provider<PluginActionManager.Factory> instanceManagerFactoryProvider;
    private final Provider<PluginEnabler> pluginEnablerProvider;
    private final Provider<PluginPrefs> pluginPrefsProvider;
    private final Provider<List<String>> privilegedPluginsProvider;
    private final Provider<Optional<Thread.UncaughtExceptionHandler>> uncaughtExceptionHandlerOptionalProvider;

    public static PluginsModule_ProvidesPluginManagerFactory create(Provider<Context> provider, Provider<PluginActionManager.Factory> provider2, Provider<Boolean> provider3, Provider<Optional<Thread.UncaughtExceptionHandler>> provider4, Provider<PluginEnabler> provider5, Provider<PluginPrefs> provider6, Provider<List<String>> provider7) {
        return new PluginsModule_ProvidesPluginManagerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public PluginManager mo144get() {
        return providesPluginManager(this.contextProvider.mo144get(), this.instanceManagerFactoryProvider.mo144get(), this.debugProvider.mo144get().booleanValue(), this.uncaughtExceptionHandlerOptionalProvider.mo144get(), this.pluginEnablerProvider.mo144get(), this.pluginPrefsProvider.mo144get(), this.privilegedPluginsProvider.mo144get());
    }

    public PluginsModule_ProvidesPluginManagerFactory(Provider<Context> provider, Provider<PluginActionManager.Factory> provider2, Provider<Boolean> provider3, Provider<Optional<Thread.UncaughtExceptionHandler>> provider4, Provider<PluginEnabler> provider5, Provider<PluginPrefs> provider6, Provider<List<String>> provider7) {
        this.contextProvider = provider;
        this.instanceManagerFactoryProvider = provider2;
        this.debugProvider = provider3;
        this.uncaughtExceptionHandlerOptionalProvider = provider4;
        this.pluginEnablerProvider = provider5;
        this.pluginPrefsProvider = provider6;
        this.privilegedPluginsProvider = provider7;
    }

    public static PluginManager providesPluginManager(Context context, PluginActionManager.Factory factory, boolean z, Optional<Thread.UncaughtExceptionHandler> optional, PluginEnabler pluginEnabler, PluginPrefs pluginPrefs, List<String> list) {
        PluginManager providesPluginManager = PluginsModule.providesPluginManager(context, factory, z, optional, pluginEnabler, pluginPrefs, list);
        Objects.requireNonNull(providesPluginManager, "Cannot return null from a non-@Nullable @Provides method");
        return providesPluginManager;
    }
}
