package com.android.systemui.plugins;

import android.content.Context;
import android.content.pm.PackageManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PluginEnablerImpl_Factory implements Factory<PluginEnablerImpl> {
    private final Provider<Context> contextProvider;
    private final Provider<PackageManager> pmProvider;

    public static PluginEnablerImpl_Factory create(Provider<Context> provider, Provider<PackageManager> provider2) {
        return new PluginEnablerImpl_Factory(provider, provider2);
    }

    public static PluginEnablerImpl newInstance(Context context, PackageManager packageManager) {
        return new PluginEnablerImpl(context, packageManager);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public PluginEnablerImpl mo144get() {
        return newInstance(this.contextProvider.mo144get(), this.pmProvider.mo144get());
    }

    public PluginEnablerImpl_Factory(Provider<Context> provider, Provider<PackageManager> provider2) {
        this.contextProvider = provider;
        this.pmProvider = provider2;
    }
}
