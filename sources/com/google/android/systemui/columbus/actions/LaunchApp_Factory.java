package com.google.android.systemui.columbus.actions;

import android.app.IActivityManager;
import android.content.Context;
import android.content.pm.LauncherApps;
import android.os.Handler;
import android.os.UserManager;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.google.android.systemui.columbus.ColumbusSettings;
import com.google.android.systemui.columbus.gates.KeyguardVisibility;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LaunchApp_Factory implements Factory<LaunchApp> {
    public final Provider<IActivityManager> activityManagerServiceProvider;
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<Executor> bgExecutorProvider;
    public final Provider<Handler> bgHandlerProvider;
    public final Provider<ColumbusSettings> columbusSettingsProvider;
    public final Provider<Context> contextProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<KeyguardVisibility> keyguardVisibilityProvider;
    public final Provider<LauncherApps> launcherAppsProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<UserManager> userManagerProvider;
    public final Provider<UserTracker> userTrackerProvider;

    public static LaunchApp_Factory create(Provider<Context> provider, Provider<LauncherApps> provider2, Provider<ActivityStarter> provider3, Provider<StatusBarKeyguardViewManager> provider4, Provider<IActivityManager> provider5, Provider<UserManager> provider6, Provider<ColumbusSettings> provider7, Provider<KeyguardVisibility> provider8, Provider<KeyguardUpdateMonitor> provider9, Provider<Handler> provider10, Provider<Handler> provider11, Provider<Executor> provider12, Provider<UiEventLogger> provider13, Provider<UserTracker> provider14) {
        return new LaunchApp_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new LaunchApp(this.contextProvider.mo144get(), this.launcherAppsProvider.mo144get(), this.activityStarterProvider.mo144get(), this.statusBarKeyguardViewManagerProvider.mo144get(), this.activityManagerServiceProvider.mo144get(), this.userManagerProvider.mo144get(), this.columbusSettingsProvider.mo144get(), this.keyguardVisibilityProvider.mo144get(), this.keyguardUpdateMonitorProvider.mo144get(), this.mainHandlerProvider.mo144get(), this.bgHandlerProvider.mo144get(), this.bgExecutorProvider.mo144get(), this.uiEventLoggerProvider.mo144get(), this.userTrackerProvider.mo144get());
    }

    public LaunchApp_Factory(Provider<Context> provider, Provider<LauncherApps> provider2, Provider<ActivityStarter> provider3, Provider<StatusBarKeyguardViewManager> provider4, Provider<IActivityManager> provider5, Provider<UserManager> provider6, Provider<ColumbusSettings> provider7, Provider<KeyguardVisibility> provider8, Provider<KeyguardUpdateMonitor> provider9, Provider<Handler> provider10, Provider<Handler> provider11, Provider<Executor> provider12, Provider<UiEventLogger> provider13, Provider<UserTracker> provider14) {
        this.contextProvider = provider;
        this.launcherAppsProvider = provider2;
        this.activityStarterProvider = provider3;
        this.statusBarKeyguardViewManagerProvider = provider4;
        this.activityManagerServiceProvider = provider5;
        this.userManagerProvider = provider6;
        this.columbusSettingsProvider = provider7;
        this.keyguardVisibilityProvider = provider8;
        this.keyguardUpdateMonitorProvider = provider9;
        this.mainHandlerProvider = provider10;
        this.bgHandlerProvider = provider11;
        this.bgExecutorProvider = provider12;
        this.uiEventLoggerProvider = provider13;
        this.userTrackerProvider = provider14;
    }
}
