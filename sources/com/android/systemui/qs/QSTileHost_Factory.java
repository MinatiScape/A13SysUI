package com.android.systemui.qs;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.qs.QSFactory;
import com.android.systemui.qs.external.CustomTileStatePersister;
import com.android.systemui.qs.external.TileLifecycleManager;
import com.android.systemui.qs.external.TileServiceRequestController;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import dagger.internal.InstanceFactory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSTileHost_Factory implements Factory<QSTileHost> {
    public final Provider<AutoTileManager> autoTilesProvider;
    public final Provider<Looper> bgLooperProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;
    public final Provider<CustomTileStatePersister> customTileStatePersisterProvider;
    public final Provider<QSFactory> defaultFactoryProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<StatusBarIconController> iconControllerProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<PluginManager> pluginManagerProvider;
    public final Provider<QSLogger> qsLoggerProvider;
    public final Provider<SecureSettings> secureSettingsProvider;
    public final Provider<Optional<StatusBar>> statusBarOptionalProvider;
    public final Provider<TileLifecycleManager.Factory> tileLifecycleManagerFactoryProvider;
    public final Provider<TileServiceRequestController.Builder> tileServiceRequestControllerBuilderProvider;
    public final Provider<TunerService> tunerServiceProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<UserTracker> userTrackerProvider;

    public QSTileHost_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11, Provider provider12, Provider provider13, Provider provider14, Provider provider15, Provider provider16, Provider provider17, InstanceFactory instanceFactory) {
        this.contextProvider = provider;
        this.iconControllerProvider = provider2;
        this.defaultFactoryProvider = provider3;
        this.mainHandlerProvider = provider4;
        this.bgLooperProvider = provider5;
        this.pluginManagerProvider = provider6;
        this.tunerServiceProvider = provider7;
        this.autoTilesProvider = provider8;
        this.dumpManagerProvider = provider9;
        this.broadcastDispatcherProvider = provider10;
        this.statusBarOptionalProvider = provider11;
        this.qsLoggerProvider = provider12;
        this.uiEventLoggerProvider = provider13;
        this.userTrackerProvider = provider14;
        this.secureSettingsProvider = provider15;
        this.customTileStatePersisterProvider = provider16;
        this.tileServiceRequestControllerBuilderProvider = provider17;
        this.tileLifecycleManagerFactoryProvider = instanceFactory;
    }

    public static QSTileHost_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11, Provider provider12, Provider provider13, Provider provider14, Provider provider15, Provider provider16, Provider provider17, InstanceFactory instanceFactory) {
        return new QSTileHost_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, instanceFactory);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        Context context = this.contextProvider.mo144get();
        StatusBarIconController statusBarIconController = this.iconControllerProvider.mo144get();
        QSFactory qSFactory = this.defaultFactoryProvider.mo144get();
        Handler handler = this.mainHandlerProvider.mo144get();
        this.bgLooperProvider.mo144get();
        PluginManager pluginManager = this.pluginManagerProvider.mo144get();
        TunerService tunerService = this.tunerServiceProvider.mo144get();
        Provider<AutoTileManager> provider = this.autoTilesProvider;
        DumpManager dumpManager = this.dumpManagerProvider.mo144get();
        this.broadcastDispatcherProvider.mo144get();
        return new QSTileHost(context, statusBarIconController, qSFactory, handler, pluginManager, tunerService, provider, dumpManager, this.statusBarOptionalProvider.mo144get(), this.qsLoggerProvider.mo144get(), this.uiEventLoggerProvider.mo144get(), this.userTrackerProvider.mo144get(), this.secureSettingsProvider.mo144get(), this.customTileStatePersisterProvider.mo144get(), this.tileServiceRequestControllerBuilderProvider.mo144get(), this.tileLifecycleManagerFactoryProvider.mo144get());
    }
}
