package com.android.systemui.qs.external;

import android.os.Handler;
import android.os.Looper;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.qs.logging.QSLogger;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class CustomTile_Builder_Factory implements Factory<CustomTile.Builder> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<Looper> backgroundLooperProvider;
    public final Provider<CustomTileStatePersister> customTileStatePersisterProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<QSHost> hostLazyProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<MetricsLogger> metricsLoggerProvider;
    public final Provider<QSLogger> qsLoggerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<TileServices> tileServicesProvider;

    public static CustomTile_Builder_Factory create(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<FalsingManager> provider4, Provider<MetricsLogger> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<CustomTileStatePersister> provider9, Provider<TileServices> provider10) {
        return new CustomTile_Builder_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new CustomTile.Builder(DoubleCheck.lazy(this.hostLazyProvider), this.backgroundLooperProvider.mo144get(), this.mainHandlerProvider.mo144get(), this.falsingManagerProvider.mo144get(), this.metricsLoggerProvider.mo144get(), this.statusBarStateControllerProvider.mo144get(), this.activityStarterProvider.mo144get(), this.qsLoggerProvider.mo144get(), this.customTileStatePersisterProvider.mo144get(), this.tileServicesProvider.mo144get());
    }

    public CustomTile_Builder_Factory(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<FalsingManager> provider4, Provider<MetricsLogger> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<CustomTileStatePersister> provider9, Provider<TileServices> provider10) {
        this.hostLazyProvider = provider;
        this.backgroundLooperProvider = provider2;
        this.mainHandlerProvider = provider3;
        this.falsingManagerProvider = provider4;
        this.metricsLoggerProvider = provider5;
        this.statusBarStateControllerProvider = provider6;
        this.activityStarterProvider = provider7;
        this.qsLoggerProvider = provider8;
        this.customTileStatePersisterProvider = provider9;
        this.tileServicesProvider = provider10;
    }
}
