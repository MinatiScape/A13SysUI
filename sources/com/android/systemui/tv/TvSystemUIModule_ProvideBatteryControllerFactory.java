package com.android.systemui.tv;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import com.android.systemui.dagger.DependencyProvider;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.power.EnhancedEstimates;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.BatteryControllerImpl;
import com.android.systemui.util.WallpaperController_Factory;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TvSystemUIModule_ProvideBatteryControllerFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Object bgHandlerProvider;
    public final Provider broadcastDispatcherProvider;
    public final Provider contextProvider;
    public final Provider demoModeControllerProvider;
    public final Provider enhancedEstimatesProvider;
    public final Provider mainHandlerProvider;
    public final Provider powerManagerProvider;

    public /* synthetic */ TvSystemUIModule_ProvideBatteryControllerFactory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.enhancedEstimatesProvider = provider2;
        this.powerManagerProvider = provider3;
        this.broadcastDispatcherProvider = provider4;
        this.demoModeControllerProvider = provider5;
        this.mainHandlerProvider = provider6;
        this.bgHandlerProvider = provider7;
    }

    @Override // javax.inject.Provider
    /* renamed from: get  reason: collision with other method in class */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return mo144get();
            case 1:
                return mo144get();
            default:
                Context context = (Context) this.contextProvider.mo144get();
                Looper looper = (Looper) this.enhancedEstimatesProvider.mo144get();
                Executor executor = (Executor) this.powerManagerProvider.mo144get();
                DumpManager dumpManager = (DumpManager) this.broadcastDispatcherProvider.mo144get();
                Objects.requireNonNull((DependencyProvider) this.bgHandlerProvider);
                BroadcastDispatcher broadcastDispatcher = new BroadcastDispatcher(context, looper, executor, dumpManager, (BroadcastDispatcherLogger) this.demoModeControllerProvider.mo144get(), (UserTracker) this.mainHandlerProvider.mo144get());
                dumpManager.registerDumpable(BroadcastDispatcher.class.getName(), broadcastDispatcher);
                return broadcastDispatcher;
        }
    }

    public TvSystemUIModule_ProvideBatteryControllerFactory(DependencyProvider dependencyProvider, Provider provider, Provider provider2, Provider provider3, Provider provider4, WallpaperController_Factory wallpaperController_Factory, Provider provider5) {
        this.$r8$classId = 2;
        this.bgHandlerProvider = dependencyProvider;
        this.contextProvider = provider;
        this.enhancedEstimatesProvider = provider2;
        this.powerManagerProvider = provider3;
        this.broadcastDispatcherProvider = provider4;
        this.demoModeControllerProvider = wallpaperController_Factory;
        this.mainHandlerProvider = provider5;
    }

    public static TvSystemUIModule_ProvideBatteryControllerFactory create(DependencyProvider dependencyProvider, Provider provider, Provider provider2, Provider provider3, Provider provider4, WallpaperController_Factory wallpaperController_Factory, Provider provider5) {
        return new TvSystemUIModule_ProvideBatteryControllerFactory(dependencyProvider, provider, provider2, provider3, provider4, wallpaperController_Factory, provider5);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final BatteryController mo144get() {
        switch (this.$r8$classId) {
            case 0:
                BatteryControllerImpl batteryControllerImpl = new BatteryControllerImpl((Context) this.contextProvider.mo144get(), (EnhancedEstimates) this.enhancedEstimatesProvider.mo144get(), (PowerManager) this.powerManagerProvider.mo144get(), (BroadcastDispatcher) this.broadcastDispatcherProvider.mo144get(), (DemoModeController) this.demoModeControllerProvider.mo144get(), (Handler) this.mainHandlerProvider.mo144get(), (Handler) ((Provider) this.bgHandlerProvider).mo144get());
                batteryControllerImpl.init();
                return batteryControllerImpl;
            default:
                BatteryControllerImpl batteryControllerImpl2 = new BatteryControllerImpl((Context) this.contextProvider.mo144get(), (EnhancedEstimates) this.enhancedEstimatesProvider.mo144get(), (PowerManager) this.powerManagerProvider.mo144get(), (BroadcastDispatcher) this.broadcastDispatcherProvider.mo144get(), (DemoModeController) this.demoModeControllerProvider.mo144get(), (Handler) this.mainHandlerProvider.mo144get(), (Handler) ((Provider) this.bgHandlerProvider).mo144get());
                batteryControllerImpl2.init();
                return batteryControllerImpl2;
        }
    }
}
