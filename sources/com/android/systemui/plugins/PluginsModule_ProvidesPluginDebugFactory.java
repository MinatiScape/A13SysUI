package com.android.systemui.plugins;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class PluginsModule_ProvidesPluginDebugFactory implements Factory<Boolean> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public Boolean mo144get() {
        return Boolean.valueOf(providesPluginDebug());
    }

    public static PluginsModule_ProvidesPluginDebugFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static boolean providesPluginDebug() {
        return PluginsModule.providesPluginDebug();
    }

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final PluginsModule_ProvidesPluginDebugFactory INSTANCE = new PluginsModule_ProvidesPluginDebugFactory();

        private InstanceHolder() {
        }
    }
}
