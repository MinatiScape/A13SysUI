package com.android.systemui.plugins;

import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import com.android.systemui.shared.plugins.PluginActionManager;
import com.android.systemui.shared.plugins.PluginEnabler;
import com.android.systemui.shared.plugins.PluginInstance;
import dagger.internal.Factory;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PluginsModule_ProvidePluginInstanceManagerFactoryFactory implements Factory<PluginActionManager.Factory> {
    private final Provider<Context> contextProvider;
    private final Provider<Executor> mainExecutorProvider;
    private final Provider<NotificationManager> notificationManagerProvider;
    private final Provider<PackageManager> packageManagerProvider;
    private final Provider<PluginEnabler> pluginEnablerProvider;
    private final Provider<Executor> pluginExecutorProvider;
    private final Provider<PluginInstance.Factory> pluginInstanceFactoryProvider;
    private final Provider<List<String>> privilegedPluginsProvider;

    public static PluginsModule_ProvidePluginInstanceManagerFactoryFactory create(Provider<Context> provider, Provider<PackageManager> provider2, Provider<Executor> provider3, Provider<Executor> provider4, Provider<NotificationManager> provider5, Provider<PluginEnabler> provider6, Provider<List<String>> provider7, Provider<PluginInstance.Factory> provider8) {
        return new PluginsModule_ProvidePluginInstanceManagerFactoryFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public PluginActionManager.Factory mo144get() {
        return providePluginInstanceManagerFactory(this.contextProvider.mo144get(), this.packageManagerProvider.mo144get(), this.mainExecutorProvider.mo144get(), this.pluginExecutorProvider.mo144get(), this.notificationManagerProvider.mo144get(), this.pluginEnablerProvider.mo144get(), this.privilegedPluginsProvider.mo144get(), this.pluginInstanceFactoryProvider.mo144get());
    }

    public PluginsModule_ProvidePluginInstanceManagerFactoryFactory(Provider<Context> provider, Provider<PackageManager> provider2, Provider<Executor> provider3, Provider<Executor> provider4, Provider<NotificationManager> provider5, Provider<PluginEnabler> provider6, Provider<List<String>> provider7, Provider<PluginInstance.Factory> provider8) {
        this.contextProvider = provider;
        this.packageManagerProvider = provider2;
        this.mainExecutorProvider = provider3;
        this.pluginExecutorProvider = provider4;
        this.notificationManagerProvider = provider5;
        this.pluginEnablerProvider = provider6;
        this.privilegedPluginsProvider = provider7;
        this.pluginInstanceFactoryProvider = provider8;
    }

    public static PluginActionManager.Factory providePluginInstanceManagerFactory(Context context, PackageManager packageManager, Executor executor, Executor executor2, NotificationManager notificationManager, PluginEnabler pluginEnabler, List<String> list, PluginInstance.Factory factory) {
        PluginActionManager.Factory providePluginInstanceManagerFactory = PluginsModule.providePluginInstanceManagerFactory(context, packageManager, executor, executor2, notificationManager, pluginEnabler, list, factory);
        Objects.requireNonNull(providePluginInstanceManagerFactory, "Cannot return null from a non-@Nullable @Provides method");
        return providePluginInstanceManagerFactory;
    }
}
