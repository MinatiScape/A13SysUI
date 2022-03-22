package com.android.systemui.qs.tiles;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.LocationController;
import com.android.wm.shell.WindowManagerShellWrapper;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.tv.TvPipBoundsAlgorithm;
import com.android.wm.shell.pip.tv.TvPipBoundsState;
import com.android.wm.shell.pip.tv.TvPipController;
import com.android.wm.shell.pip.tv.TvPipMenuController;
import com.android.wm.shell.pip.tv.TvPipNotificationController;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class UiModeNightTile_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider activityStarterProvider;
    public final Provider backgroundLooperProvider;
    public final Provider batteryControllerProvider;
    public final Provider configurationControllerProvider;
    public final Provider falsingManagerProvider;
    public final Provider hostProvider;
    public final Provider locationControllerProvider;
    public final Provider mainHandlerProvider;
    public final Provider metricsLoggerProvider;
    public final Provider qsLoggerProvider;
    public final Provider statusBarStateControllerProvider;

    public /* synthetic */ UiModeNightTile_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11, int i) {
        this.$r8$classId = i;
        this.hostProvider = provider;
        this.backgroundLooperProvider = provider2;
        this.mainHandlerProvider = provider3;
        this.falsingManagerProvider = provider4;
        this.metricsLoggerProvider = provider5;
        this.statusBarStateControllerProvider = provider6;
        this.activityStarterProvider = provider7;
        this.qsLoggerProvider = provider8;
        this.configurationControllerProvider = provider9;
        this.batteryControllerProvider = provider10;
        this.locationControllerProvider = provider11;
    }

    public static UiModeNightTile_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11) {
        return new UiModeNightTile_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, 0);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new UiModeNightTile((QSHost) this.hostProvider.mo144get(), (Looper) this.backgroundLooperProvider.mo144get(), (Handler) this.mainHandlerProvider.mo144get(), (FalsingManager) this.falsingManagerProvider.mo144get(), (MetricsLogger) this.metricsLoggerProvider.mo144get(), (StatusBarStateController) this.statusBarStateControllerProvider.mo144get(), (ActivityStarter) this.activityStarterProvider.mo144get(), (QSLogger) this.qsLoggerProvider.mo144get(), (ConfigurationController) this.configurationControllerProvider.mo144get(), (BatteryController) this.batteryControllerProvider.mo144get(), (LocationController) this.locationControllerProvider.mo144get());
            default:
                Optional of = Optional.of(new TvPipController((Context) this.hostProvider.mo144get(), (TvPipBoundsState) this.backgroundLooperProvider.mo144get(), (TvPipBoundsAlgorithm) this.mainHandlerProvider.mo144get(), (PipTaskOrganizer) this.falsingManagerProvider.mo144get(), (PipTransitionController) this.activityStarterProvider.mo144get(), (TvPipMenuController) this.metricsLoggerProvider.mo144get(), (PipMediaController) this.statusBarStateControllerProvider.mo144get(), (TvPipNotificationController) this.qsLoggerProvider.mo144get(), (TaskStackListenerImpl) this.configurationControllerProvider.mo144get(), (WindowManagerShellWrapper) this.batteryControllerProvider.mo144get(), (ShellExecutor) this.locationControllerProvider.mo144get()).mImpl);
                Objects.requireNonNull(of, "Cannot return null from a non-@Nullable @Provides method");
                return of;
        }
    }
}
