package com.android.systemui.qs.tiles;

import android.os.Handler;
import android.os.IThermalService;
import android.os.Looper;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.settings.SecureSettings;
import com.google.android.systemui.qs.tiles.ReverseChargingTile;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class OneHandedModeTile_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider activityStarterProvider;
    public final Provider backgroundLooperProvider;
    public final Provider falsingManagerProvider;
    public final Provider hostProvider;
    public final Provider mainHandlerProvider;
    public final Provider metricsLoggerProvider;
    public final Provider qsLoggerProvider;
    public final Provider secureSettingsProvider;
    public final Provider statusBarStateControllerProvider;
    public final Provider userTrackerProvider;

    public /* synthetic */ OneHandedModeTile_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, int i) {
        this.$r8$classId = i;
        this.hostProvider = provider;
        this.backgroundLooperProvider = provider2;
        this.mainHandlerProvider = provider3;
        this.falsingManagerProvider = provider4;
        this.metricsLoggerProvider = provider5;
        this.statusBarStateControllerProvider = provider6;
        this.activityStarterProvider = provider7;
        this.qsLoggerProvider = provider8;
        this.userTrackerProvider = provider9;
        this.secureSettingsProvider = provider10;
    }

    public static OneHandedModeTile_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10) {
        return new OneHandedModeTile_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, 0);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new OneHandedModeTile((QSHost) this.hostProvider.mo144get(), (Looper) this.backgroundLooperProvider.mo144get(), (Handler) this.mainHandlerProvider.mo144get(), (FalsingManager) this.falsingManagerProvider.mo144get(), (MetricsLogger) this.metricsLoggerProvider.mo144get(), (StatusBarStateController) this.statusBarStateControllerProvider.mo144get(), (ActivityStarter) this.activityStarterProvider.mo144get(), (QSLogger) this.qsLoggerProvider.mo144get(), (UserTracker) this.userTrackerProvider.mo144get(), (SecureSettings) this.secureSettingsProvider.mo144get());
            default:
                return new ReverseChargingTile((QSHost) this.hostProvider.mo144get(), (Looper) this.backgroundLooperProvider.mo144get(), (Handler) this.mainHandlerProvider.mo144get(), (FalsingManager) this.falsingManagerProvider.mo144get(), (MetricsLogger) this.metricsLoggerProvider.mo144get(), (StatusBarStateController) this.statusBarStateControllerProvider.mo144get(), (ActivityStarter) this.activityStarterProvider.mo144get(), (QSLogger) this.qsLoggerProvider.mo144get(), (BatteryController) this.userTrackerProvider.mo144get(), (IThermalService) this.secureSettingsProvider.mo144get());
        }
    }
}
