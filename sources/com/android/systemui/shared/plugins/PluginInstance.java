package com.android.systemui.shared.plugins;

import android.content.ComponentName;
import android.content.Context;
import android.util.ArrayMap;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.plugins.Plugin;
import com.android.systemui.shared.plugins.PluginActionManager;
import java.util.List;
/* loaded from: classes.dex */
public final class PluginInstance<T extends Plugin> {
    public static final ArrayMap sClassLoaders = new ArrayMap();
    public final ComponentName mComponentName;
    public final T mPlugin;
    public final Context mPluginContext;
    public final VersionInfo mVersionInfo;

    /* loaded from: classes.dex */
    public static class Factory {
        public final ClassLoader mBaseClassLoader;
        public final InstanceFactory<?> mInstanceFactory;
        public final boolean mIsDebug;
        public final List<String> mPrivilegedPlugins;
        public final VersionChecker mVersionChecker;

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:23:0x00bd  */
        /* JADX WARN: Removed duplicated region for block: B:26:0x00cb  */
        /* JADX WARN: Removed duplicated region for block: B:29:0x00d9  */
        /* JADX WARN: Removed duplicated region for block: B:30:0x00f4  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final <T extends com.android.systemui.plugins.Plugin> com.android.systemui.shared.plugins.PluginInstance<T> create(android.content.Context r10, android.content.pm.ApplicationInfo r11, android.content.ComponentName r12, java.lang.Class<T> r13) throws android.content.pm.PackageManager.NameNotFoundException, java.lang.ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException {
            /*
                Method dump skipped, instructions count: 276
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.shared.plugins.PluginInstance.Factory.create(android.content.Context, android.content.pm.ApplicationInfo, android.content.ComponentName, java.lang.Class):com.android.systemui.shared.plugins.PluginInstance");
        }

        public Factory(ClassLoader classLoader, InstanceFactory<?> instanceFactory, VersionChecker versionChecker, List<String> list, boolean z) {
            this.mPrivilegedPlugins = list;
            this.mBaseClassLoader = classLoader;
            this.mInstanceFactory = instanceFactory;
            this.mVersionChecker = versionChecker;
            this.mIsDebug = z;
        }
    }

    /* loaded from: classes.dex */
    public static class InstanceFactory<T extends Plugin> {
    }

    /* loaded from: classes.dex */
    public static class VersionChecker {
    }

    /* JADX WARN: Multi-variable type inference failed */
    public PluginInstance(ComponentName componentName, Plugin plugin, PluginActionManager.PluginContextWrapper pluginContextWrapper, VersionInfo versionInfo) {
        this.mComponentName = componentName;
        this.mPlugin = plugin;
        this.mPluginContext = pluginContextWrapper;
        this.mVersionInfo = versionInfo;
    }

    @VisibleForTesting
    public Context getPluginContext() {
        return this.mPluginContext;
    }
}
