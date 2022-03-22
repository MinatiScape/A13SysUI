package com.android.systemui.qs.tiles;

import android.hardware.display.ColorDisplayManager;
import android.os.Handler;
import android.os.Looper;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.dagger.NightDisplayListenerModule;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.statusbar.policy.LocationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NightDisplayTile_Factory implements Factory<NightDisplayTile> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<Looper> backgroundLooperProvider;
    public final Provider<ColorDisplayManager> colorDisplayManagerProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<QSHost> hostProvider;
    public final Provider<LocationController> locationControllerProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<MetricsLogger> metricsLoggerProvider;
    public final Provider<NightDisplayListenerModule.Builder> nightDisplayListenerBuilderProvider;
    public final Provider<QSLogger> qsLoggerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public static NightDisplayTile_Factory create(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<FalsingManager> provider4, Provider<MetricsLogger> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<LocationController> provider9, Provider<ColorDisplayManager> provider10, Provider<NightDisplayListenerModule.Builder> provider11) {
        return new NightDisplayTile_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NightDisplayTile(this.hostProvider.mo144get(), this.backgroundLooperProvider.mo144get(), this.mainHandlerProvider.mo144get(), this.falsingManagerProvider.mo144get(), this.metricsLoggerProvider.mo144get(), this.statusBarStateControllerProvider.mo144get(), this.activityStarterProvider.mo144get(), this.qsLoggerProvider.mo144get(), this.locationControllerProvider.mo144get(), this.colorDisplayManagerProvider.mo144get(), this.nightDisplayListenerBuilderProvider.mo144get());
    }

    public NightDisplayTile_Factory(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<FalsingManager> provider4, Provider<MetricsLogger> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<LocationController> provider9, Provider<ColorDisplayManager> provider10, Provider<NightDisplayListenerModule.Builder> provider11) {
        this.hostProvider = provider;
        this.backgroundLooperProvider = provider2;
        this.mainHandlerProvider = provider3;
        this.falsingManagerProvider = provider4;
        this.metricsLoggerProvider = provider5;
        this.statusBarStateControllerProvider = provider6;
        this.activityStarterProvider = provider7;
        this.qsLoggerProvider = provider8;
        this.locationControllerProvider = provider9;
        this.colorDisplayManagerProvider = provider10;
        this.nightDisplayListenerBuilderProvider = provider11;
    }
}
