package com.android.systemui.plugins;

import android.content.Context;
import com.android.systemui.shared.plugins.PluginPrefs;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PluginsModule_ProvidesPluginPrefsFactory implements Factory<PluginPrefs> {
    private final Provider<Context> contextProvider;

    public static PluginsModule_ProvidesPluginPrefsFactory create(Provider<Context> provider) {
        return new PluginsModule_ProvidesPluginPrefsFactory(provider);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public PluginPrefs mo144get() {
        return providesPluginPrefs(this.contextProvider.mo144get());
    }

    public PluginsModule_ProvidesPluginPrefsFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public static PluginPrefs providesPluginPrefs(Context context) {
        PluginPrefs providesPluginPrefs = PluginsModule.providesPluginPrefs(context);
        Objects.requireNonNull(providesPluginPrefs, "Cannot return null from a non-@Nullable @Provides method");
        return providesPluginPrefs;
    }
}
