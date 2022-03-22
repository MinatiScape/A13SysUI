package com.android.systemui.dagger;

import android.app.IWallpaperManager;
import android.os.ServiceManager;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideIWallPaperManagerFactory implements Factory<IWallpaperManager> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final FrameworkServicesModule_ProvideIWallPaperManagerFactory INSTANCE = new FrameworkServicesModule_ProvideIWallPaperManagerFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return IWallpaperManager.Stub.asInterface(ServiceManager.getService("wallpaper"));
    }
}
