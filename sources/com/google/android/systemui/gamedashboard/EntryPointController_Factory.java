package com.google.android.systemui.gamedashboard;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.accessibility.AccessibilityManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.CommandQueue;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class EntryPointController_Factory implements Factory<EntryPointController> {
    public final Provider<AccessibilityManager> accessibilityManagerProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<Context> contextProvider;
    public final Provider<GameModeDndController> gameModeDndControllerProvider;
    public final Provider<Optional<LegacySplitScreen>> legacySplitScreenOptionalProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<NavigationModeController> navigationModeControllerProvider;
    public final Provider<OverviewProxyService> overviewProxyServiceProvider;
    public final Provider<PackageManager> packageManagerProvider;
    public final Provider<ShortcutBarController> shortcutBarControllerProvider;
    public final Provider<Optional<TaskSurfaceHelper>> taskSurfaceHelperProvider;
    public final Provider<ToastController> toastProvider;
    public final Provider<GameDashboardUiEventLogger> uiEventLoggerProvider;

    public static EntryPointController_Factory create(Provider<Context> provider, Provider<AccessibilityManager> provider2, Provider<BroadcastDispatcher> provider3, Provider<CommandQueue> provider4, Provider<GameModeDndController> provider5, Provider<Handler> provider6, Provider<NavigationModeController> provider7, Provider<Optional<LegacySplitScreen>> provider8, Provider<OverviewProxyService> provider9, Provider<PackageManager> provider10, Provider<ShortcutBarController> provider11, Provider<ToastController> provider12, Provider<GameDashboardUiEventLogger> provider13, Provider<Optional<TaskSurfaceHelper>> provider14) {
        return new EntryPointController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new EntryPointController(this.contextProvider.mo144get(), this.accessibilityManagerProvider.mo144get(), this.broadcastDispatcherProvider.mo144get(), this.commandQueueProvider.mo144get(), this.gameModeDndControllerProvider.mo144get(), this.mainHandlerProvider.mo144get(), this.navigationModeControllerProvider.mo144get(), this.legacySplitScreenOptionalProvider.mo144get(), this.overviewProxyServiceProvider.mo144get(), this.packageManagerProvider.mo144get(), this.shortcutBarControllerProvider.mo144get(), this.toastProvider.mo144get(), this.uiEventLoggerProvider.mo144get(), this.taskSurfaceHelperProvider.mo144get());
    }

    public EntryPointController_Factory(Provider<Context> provider, Provider<AccessibilityManager> provider2, Provider<BroadcastDispatcher> provider3, Provider<CommandQueue> provider4, Provider<GameModeDndController> provider5, Provider<Handler> provider6, Provider<NavigationModeController> provider7, Provider<Optional<LegacySplitScreen>> provider8, Provider<OverviewProxyService> provider9, Provider<PackageManager> provider10, Provider<ShortcutBarController> provider11, Provider<ToastController> provider12, Provider<GameDashboardUiEventLogger> provider13, Provider<Optional<TaskSurfaceHelper>> provider14) {
        this.contextProvider = provider;
        this.accessibilityManagerProvider = provider2;
        this.broadcastDispatcherProvider = provider3;
        this.commandQueueProvider = provider4;
        this.gameModeDndControllerProvider = provider5;
        this.mainHandlerProvider = provider6;
        this.navigationModeControllerProvider = provider7;
        this.legacySplitScreenOptionalProvider = provider8;
        this.overviewProxyServiceProvider = provider9;
        this.packageManagerProvider = provider10;
        this.shortcutBarControllerProvider = provider11;
        this.toastProvider = provider12;
        this.uiEventLoggerProvider = provider13;
        this.taskSurfaceHelperProvider = provider14;
    }
}
