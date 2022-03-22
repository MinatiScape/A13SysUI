package com.android.systemui.statusbar.phone;

import android.app.IWallpaperManager;
import android.app.WallpaperManager;
import android.os.Handler;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIWallPaperManagerFactory;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.NotificationMediaManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LockscreenWallpaper_Factory implements Factory<LockscreenWallpaper> {
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<IWallpaperManager> iWallpaperManagerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<NotificationMediaManager> mediaManagerProvider;
    public final Provider<WallpaperManager> wallpaperManagerProvider;

    public LockscreenWallpaper_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5) {
        FrameworkServicesModule_ProvideIWallPaperManagerFactory frameworkServicesModule_ProvideIWallPaperManagerFactory = FrameworkServicesModule_ProvideIWallPaperManagerFactory.InstanceHolder.INSTANCE;
        this.wallpaperManagerProvider = provider;
        this.iWallpaperManagerProvider = frameworkServicesModule_ProvideIWallPaperManagerFactory;
        this.keyguardUpdateMonitorProvider = provider2;
        this.dumpManagerProvider = provider3;
        this.mediaManagerProvider = provider4;
        this.mainHandlerProvider = provider5;
    }

    public static LockscreenWallpaper_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5) {
        return new LockscreenWallpaper_Factory(provider, provider2, provider3, provider4, provider5);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        WallpaperManager wallpaperManager = this.wallpaperManagerProvider.mo144get();
        IWallpaperManager iWallpaperManager = this.iWallpaperManagerProvider.mo144get();
        this.keyguardUpdateMonitorProvider.mo144get();
        return new LockscreenWallpaper(wallpaperManager, iWallpaperManager, this.dumpManagerProvider.mo144get(), this.mediaManagerProvider.mo144get(), this.mainHandlerProvider.mo144get());
    }
}
