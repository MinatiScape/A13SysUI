package com.google.android.systemui.qs.tiles;

import android.content.Context;
import android.content.om.OverlayManager;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.power.EnhancedEstimates;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.settings.UserContentResolverProvider;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator;
import com.android.systemui.statusbar.notification.collection.inflation.OnUserInteractionCallbackImpl;
import com.android.systemui.statusbar.notification.collection.legacy.OnUserInteractionCallbackImplLegacy;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.google.android.systemui.reversecharging.ReverseChargingController;
import com.google.android.systemui.statusbar.policy.BatteryControllerImplGoogle;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class OverlayToggleTile_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider activityStarterProvider;
    public final Provider backgroundLooperProvider;
    public final Provider falsingManagerProvider;
    public final Provider hostProvider;
    public final Provider mainHandlerProvider;
    public final Provider metricsLoggerProvider;
    public final Provider omProvider;
    public final Provider qsLoggerProvider;
    public final Provider statusBarStateControllerProvider;

    public /* synthetic */ OverlayToggleTile_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, int i) {
        this.$r8$classId = i;
        this.hostProvider = provider;
        this.backgroundLooperProvider = provider2;
        this.mainHandlerProvider = provider3;
        this.falsingManagerProvider = provider4;
        this.metricsLoggerProvider = provider5;
        this.statusBarStateControllerProvider = provider6;
        this.activityStarterProvider = provider7;
        this.qsLoggerProvider = provider8;
        this.omProvider = provider9;
    }

    public static OverlayToggleTile_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9) {
        return new OverlayToggleTile_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, 1);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new OverlayToggleTile((QSHost) this.hostProvider.mo144get(), (Looper) this.backgroundLooperProvider.mo144get(), (Handler) this.mainHandlerProvider.mo144get(), (FalsingManager) this.falsingManagerProvider.mo144get(), (MetricsLogger) this.metricsLoggerProvider.mo144get(), (StatusBarStateController) this.statusBarStateControllerProvider.mo144get(), (ActivityStarter) this.activityStarterProvider.mo144get(), (QSLogger) this.qsLoggerProvider.mo144get(), (OverlayManager) this.omProvider.mo144get());
            case 1:
                HeadsUpManager headsUpManager = (HeadsUpManager) this.backgroundLooperProvider.mo144get();
                StatusBarStateController statusBarStateController = (StatusBarStateController) this.mainHandlerProvider.mo144get();
                Lazy lazy = DoubleCheck.lazy(this.falsingManagerProvider);
                Lazy lazy2 = DoubleCheck.lazy(this.metricsLoggerProvider);
                Lazy lazy3 = DoubleCheck.lazy(this.statusBarStateControllerProvider);
                NotificationEntryManager notificationEntryManager = (NotificationEntryManager) this.activityStarterProvider.mo144get();
                VisualStabilityManager visualStabilityManager = (VisualStabilityManager) this.qsLoggerProvider.mo144get();
                Lazy lazy4 = DoubleCheck.lazy(this.omProvider);
                if (((NotifPipelineFlags) this.hostProvider.mo144get()).isNewPipelineEnabled()) {
                    return new OnUserInteractionCallbackImpl((NotificationVisibilityProvider) lazy2.get(), (NotifCollection) lazy.get(), headsUpManager, statusBarStateController, (VisualStabilityCoordinator) lazy3.get(), (GroupMembershipManager) lazy4.get());
                }
                return new OnUserInteractionCallbackImplLegacy(notificationEntryManager, (NotificationVisibilityProvider) lazy2.get(), headsUpManager, statusBarStateController, visualStabilityManager, (GroupMembershipManager) lazy4.get());
            default:
                BatteryControllerImplGoogle batteryControllerImplGoogle = new BatteryControllerImplGoogle((Context) this.hostProvider.mo144get(), (EnhancedEstimates) this.backgroundLooperProvider.mo144get(), (PowerManager) this.mainHandlerProvider.mo144get(), (BroadcastDispatcher) this.falsingManagerProvider.mo144get(), (DemoModeController) this.metricsLoggerProvider.mo144get(), (Handler) this.statusBarStateControllerProvider.mo144get(), (Handler) this.activityStarterProvider.mo144get(), (UserContentResolverProvider) this.qsLoggerProvider.mo144get(), (ReverseChargingController) this.omProvider.mo144get());
                batteryControllerImplGoogle.init();
                return batteryControllerImplGoogle;
        }
    }
}
