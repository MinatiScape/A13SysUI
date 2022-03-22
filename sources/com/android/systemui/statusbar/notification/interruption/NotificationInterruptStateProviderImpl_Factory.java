package com.android.systemui.statusbar.notification.interruption;

import android.content.ContentResolver;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.Handler;
import android.os.PowerManager;
import android.service.dreams.IDreamManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationFilter;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationInterruptStateProviderImpl_Factory implements Factory<NotificationInterruptStateProviderImpl> {
    public final Provider<AmbientDisplayConfiguration> ambientDisplayConfigurationProvider;
    public final Provider<BatteryController> batteryControllerProvider;
    public final Provider<ContentResolver> contentResolverProvider;
    public final Provider<IDreamManager> dreamManagerProvider;
    public final Provider<HeadsUpManager> headsUpManagerProvider;
    public final Provider<NotificationInterruptLogger> loggerProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<NotificationFilter> notificationFilterProvider;
    public final Provider<PowerManager> powerManagerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public static NotificationInterruptStateProviderImpl_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory wMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory, Provider provider9) {
        return new NotificationInterruptStateProviderImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, wMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory, provider9);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NotificationInterruptStateProviderImpl(this.contentResolverProvider.mo144get(), this.powerManagerProvider.mo144get(), this.dreamManagerProvider.mo144get(), this.ambientDisplayConfigurationProvider.mo144get(), this.notificationFilterProvider.mo144get(), this.batteryControllerProvider.mo144get(), this.statusBarStateControllerProvider.mo144get(), this.headsUpManagerProvider.mo144get(), this.loggerProvider.mo144get(), this.mainHandlerProvider.mo144get());
    }

    public NotificationInterruptStateProviderImpl_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory wMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory, Provider provider9) {
        this.contentResolverProvider = provider;
        this.powerManagerProvider = provider2;
        this.dreamManagerProvider = provider3;
        this.ambientDisplayConfigurationProvider = provider4;
        this.notificationFilterProvider = provider5;
        this.batteryControllerProvider = provider6;
        this.statusBarStateControllerProvider = provider7;
        this.headsUpManagerProvider = provider8;
        this.loggerProvider = wMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory;
        this.mainHandlerProvider = provider9;
    }
}
