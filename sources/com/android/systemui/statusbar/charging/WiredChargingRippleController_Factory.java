package com.android.systemui.statusbar.charging;

import android.content.Context;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.WindowManager;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import com.google.android.systemui.dreamliner.DockObserver;
import com.google.android.systemui.dreamliner.WirelessCharger;
import com.google.android.systemui.lowlightclock.AmbientLightModeMonitor;
import com.google.android.systemui.lowlightclock.LowLightDockManager;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WiredChargingRippleController_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider batteryControllerProvider;
    public final Provider commandRegistryProvider;
    public final Provider configurationControllerProvider;
    public final Provider contextProvider;
    public final Provider featureFlagsProvider;
    public final Provider systemClockProvider;
    public final Provider uiEventLoggerProvider;
    public final Provider windowManagerProvider;

    public /* synthetic */ WiredChargingRippleController_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, int i) {
        this.$r8$classId = i;
        this.commandRegistryProvider = provider;
        this.batteryControllerProvider = provider2;
        this.configurationControllerProvider = provider3;
        this.featureFlagsProvider = provider4;
        this.contextProvider = provider5;
        this.windowManagerProvider = provider6;
        this.systemClockProvider = provider7;
        this.uiEventLoggerProvider = provider8;
    }

    public static WiredChargingRippleController_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8) {
        return new WiredChargingRippleController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, 0);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new WiredChargingRippleController((CommandRegistry) this.commandRegistryProvider.mo144get(), (BatteryController) this.batteryControllerProvider.mo144get(), (ConfigurationController) this.configurationControllerProvider.mo144get(), (FeatureFlags) this.featureFlagsProvider.mo144get(), (Context) this.contextProvider.mo144get(), (WindowManager) this.windowManagerProvider.mo144get(), (SystemClock) this.systemClockProvider.mo144get(), (UiEventLogger) this.uiEventLoggerProvider.mo144get());
            default:
                Context context = (Context) this.commandRegistryProvider.mo144get();
                Lazy lazy = DoubleCheck.lazy(this.batteryControllerProvider);
                Lazy lazy2 = DoubleCheck.lazy(this.configurationControllerProvider);
                Lazy lazy3 = DoubleCheck.lazy(this.featureFlagsProvider);
                Lazy lazy4 = DoubleCheck.lazy(this.contextProvider);
                Lazy lazy5 = DoubleCheck.lazy(this.windowManagerProvider);
                Lazy lazy6 = DoubleCheck.lazy(this.systemClockProvider);
                Lazy lazy7 = DoubleCheck.lazy(this.uiEventLoggerProvider);
                if (context.getResources().getBoolean(2131034171)) {
                    return new LowLightDockManager(context, (AmbientLightModeMonitor) lazy5.get(), (Executor) lazy6.get(), (PowerManager) lazy7.get());
                }
                WirelessCharger wirelessCharger = null;
                String string = context.getString(2131952136);
                if (!TextUtils.isEmpty(string)) {
                    try {
                        wirelessCharger = (WirelessCharger) context.getClassLoader().loadClass(string).newInstance();
                    } catch (Throwable unused) {
                    }
                }
                return new DockObserver(context, wirelessCharger, (BroadcastDispatcher) lazy.get(), (StatusBarStateController) lazy2.get(), (NotificationInterruptStateProvider) lazy3.get(), (ConfigurationController) lazy4.get(), (DelayableExecutor) lazy6.get());
        }
    }
}
