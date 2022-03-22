package com.android.systemui.navigationbar;

import android.os.Handler;
import android.telecom.TelecomManager;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationBar;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.recents.Recents;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.wm.shell.back.BackAnimation;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.pip.Pip;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NavigationBar_Factory_Factory implements Factory<NavigationBar.Factory> {
    public final Provider<AccessibilityButtonModeObserver> accessibilityButtonModeObserverProvider;
    public final Provider<AccessibilityManager> accessibilityManagerProvider;
    public final Provider<AssistManager> assistManagerLazyProvider;
    public final Provider<AutoHideController.Factory> autoHideControllerFactoryProvider;
    public final Provider<Optional<BackAnimation>> backAnimationProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    public final Provider<InputMethodManager> inputMethodManagerProvider;
    public final Provider<LightBarController.Factory> lightBarControllerFactoryProvider;
    public final Provider<AutoHideController> mainAutoHideControllerProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<LightBarController> mainLightBarControllerProvider;
    public final Provider<MetricsLogger> metricsLoggerProvider;
    public final Provider<NavBarHelper> navBarHelperProvider;
    public final Provider<NavigationBarOverlayController> navbarOverlayControllerProvider;
    public final Provider<NavigationModeController> navigationModeControllerProvider;
    public final Provider<NotificationRemoteInputManager> notificationRemoteInputManagerProvider;
    public final Provider<NotificationShadeDepthController> notificationShadeDepthControllerProvider;
    public final Provider<OverviewProxyService> overviewProxyServiceProvider;
    public final Provider<Optional<Pip>> pipOptionalProvider;
    public final Provider<Optional<Recents>> recentsOptionalProvider;
    public final Provider<ShadeController> shadeControllerProvider;
    public final Provider<Optional<LegacySplitScreen>> splitScreenOptionalProvider;
    public final Provider<Optional<StatusBar>> statusBarOptionalLazyProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<SysUiState> sysUiFlagsContainerProvider;
    public final Provider<Optional<TelecomManager>> telecomManagerOptionalProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;

    public NavigationBar_Factory_Factory(Provider<AssistManager> provider, Provider<AccessibilityManager> provider2, Provider<DeviceProvisionedController> provider3, Provider<MetricsLogger> provider4, Provider<OverviewProxyService> provider5, Provider<NavigationModeController> provider6, Provider<AccessibilityButtonModeObserver> provider7, Provider<StatusBarStateController> provider8, Provider<SysUiState> provider9, Provider<BroadcastDispatcher> provider10, Provider<CommandQueue> provider11, Provider<Optional<Pip>> provider12, Provider<Optional<LegacySplitScreen>> provider13, Provider<Optional<Recents>> provider14, Provider<Optional<StatusBar>> provider15, Provider<ShadeController> provider16, Provider<NotificationRemoteInputManager> provider17, Provider<NotificationShadeDepthController> provider18, Provider<Handler> provider19, Provider<NavigationBarOverlayController> provider20, Provider<UiEventLogger> provider21, Provider<NavBarHelper> provider22, Provider<LightBarController> provider23, Provider<LightBarController.Factory> provider24, Provider<AutoHideController> provider25, Provider<AutoHideController.Factory> provider26, Provider<Optional<TelecomManager>> provider27, Provider<InputMethodManager> provider28, Provider<Optional<BackAnimation>> provider29) {
        this.assistManagerLazyProvider = provider;
        this.accessibilityManagerProvider = provider2;
        this.deviceProvisionedControllerProvider = provider3;
        this.metricsLoggerProvider = provider4;
        this.overviewProxyServiceProvider = provider5;
        this.navigationModeControllerProvider = provider6;
        this.accessibilityButtonModeObserverProvider = provider7;
        this.statusBarStateControllerProvider = provider8;
        this.sysUiFlagsContainerProvider = provider9;
        this.broadcastDispatcherProvider = provider10;
        this.commandQueueProvider = provider11;
        this.pipOptionalProvider = provider12;
        this.splitScreenOptionalProvider = provider13;
        this.recentsOptionalProvider = provider14;
        this.statusBarOptionalLazyProvider = provider15;
        this.shadeControllerProvider = provider16;
        this.notificationRemoteInputManagerProvider = provider17;
        this.notificationShadeDepthControllerProvider = provider18;
        this.mainHandlerProvider = provider19;
        this.navbarOverlayControllerProvider = provider20;
        this.uiEventLoggerProvider = provider21;
        this.navBarHelperProvider = provider22;
        this.mainLightBarControllerProvider = provider23;
        this.lightBarControllerFactoryProvider = provider24;
        this.mainAutoHideControllerProvider = provider25;
        this.autoHideControllerFactoryProvider = provider26;
        this.telecomManagerOptionalProvider = provider27;
        this.inputMethodManagerProvider = provider28;
        this.backAnimationProvider = provider29;
    }

    public static NavigationBar_Factory_Factory create(Provider<AssistManager> provider, Provider<AccessibilityManager> provider2, Provider<DeviceProvisionedController> provider3, Provider<MetricsLogger> provider4, Provider<OverviewProxyService> provider5, Provider<NavigationModeController> provider6, Provider<AccessibilityButtonModeObserver> provider7, Provider<StatusBarStateController> provider8, Provider<SysUiState> provider9, Provider<BroadcastDispatcher> provider10, Provider<CommandQueue> provider11, Provider<Optional<Pip>> provider12, Provider<Optional<LegacySplitScreen>> provider13, Provider<Optional<Recents>> provider14, Provider<Optional<StatusBar>> provider15, Provider<ShadeController> provider16, Provider<NotificationRemoteInputManager> provider17, Provider<NotificationShadeDepthController> provider18, Provider<Handler> provider19, Provider<NavigationBarOverlayController> provider20, Provider<UiEventLogger> provider21, Provider<NavBarHelper> provider22, Provider<LightBarController> provider23, Provider<LightBarController.Factory> provider24, Provider<AutoHideController> provider25, Provider<AutoHideController.Factory> provider26, Provider<Optional<TelecomManager>> provider27, Provider<InputMethodManager> provider28, Provider<Optional<BackAnimation>> provider29) {
        return new NavigationBar_Factory_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27, provider28, provider29);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NavigationBar.Factory(DoubleCheck.lazy(this.assistManagerLazyProvider), this.accessibilityManagerProvider.mo144get(), this.deviceProvisionedControllerProvider.mo144get(), this.metricsLoggerProvider.mo144get(), this.overviewProxyServiceProvider.mo144get(), this.navigationModeControllerProvider.mo144get(), this.accessibilityButtonModeObserverProvider.mo144get(), this.statusBarStateControllerProvider.mo144get(), this.sysUiFlagsContainerProvider.mo144get(), this.broadcastDispatcherProvider.mo144get(), this.commandQueueProvider.mo144get(), this.pipOptionalProvider.mo144get(), this.splitScreenOptionalProvider.mo144get(), this.recentsOptionalProvider.mo144get(), DoubleCheck.lazy(this.statusBarOptionalLazyProvider), this.shadeControllerProvider.mo144get(), this.notificationRemoteInputManagerProvider.mo144get(), this.notificationShadeDepthControllerProvider.mo144get(), this.mainHandlerProvider.mo144get(), this.navbarOverlayControllerProvider.mo144get(), this.uiEventLoggerProvider.mo144get(), this.navBarHelperProvider.mo144get(), this.mainLightBarControllerProvider.mo144get(), this.lightBarControllerFactoryProvider.mo144get(), this.mainAutoHideControllerProvider.mo144get(), this.autoHideControllerFactoryProvider.mo144get(), this.telecomManagerOptionalProvider.mo144get(), this.inputMethodManagerProvider.mo144get(), this.backAnimationProvider.mo144get());
    }
}
