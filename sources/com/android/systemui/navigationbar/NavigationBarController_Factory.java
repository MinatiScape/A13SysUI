package com.android.systemui.navigationbar;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationBar;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.wm.shell.back.BackAnimation;
import com.android.wm.shell.pip.Pip;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NavigationBarController_Factory implements Factory<NavigationBarController> {
    public final Provider<AutoHideController> autoHideControllerProvider;
    public final Provider<Optional<BackAnimation>> backAnimationProvider;
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<LightBarController> lightBarControllerProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<NavBarHelper> navBarHelperProvider;
    public final Provider<NavigationBar.Factory> navigationBarFactoryProvider;
    public final Provider<NavigationModeController> navigationModeControllerProvider;
    public final Provider<OverviewProxyService> overviewProxyServiceProvider;
    public final Provider<Optional<Pip>> pipOptionalProvider;
    public final Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
    public final Provider<SysUiState> sysUiFlagsContainerProvider;
    public final Provider<TaskbarDelegate> taskbarDelegateProvider;

    public NavigationBarController_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, NavigationBar_Factory_Factory navigationBar_Factory_Factory, Provider provider10, Provider provider11, Provider provider12, Provider provider13, Provider provider14, Provider provider15) {
        this.contextProvider = provider;
        this.overviewProxyServiceProvider = provider2;
        this.navigationModeControllerProvider = provider3;
        this.sysUiFlagsContainerProvider = provider4;
        this.commandQueueProvider = provider5;
        this.mainHandlerProvider = provider6;
        this.configurationControllerProvider = provider7;
        this.navBarHelperProvider = provider8;
        this.taskbarDelegateProvider = provider9;
        this.navigationBarFactoryProvider = navigationBar_Factory_Factory;
        this.statusBarKeyguardViewManagerProvider = provider10;
        this.dumpManagerProvider = provider11;
        this.autoHideControllerProvider = provider12;
        this.lightBarControllerProvider = provider13;
        this.pipOptionalProvider = provider14;
        this.backAnimationProvider = provider15;
    }

    public static NavigationBarController_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, NavigationBar_Factory_Factory navigationBar_Factory_Factory, Provider provider10, Provider provider11, Provider provider12, Provider provider13, Provider provider14, Provider provider15) {
        return new NavigationBarController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, navigationBar_Factory_Factory, provider10, provider11, provider12, provider13, provider14, provider15);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NavigationBarController(this.contextProvider.mo144get(), this.overviewProxyServiceProvider.mo144get(), this.navigationModeControllerProvider.mo144get(), this.sysUiFlagsContainerProvider.mo144get(), this.commandQueueProvider.mo144get(), this.mainHandlerProvider.mo144get(), this.configurationControllerProvider.mo144get(), this.navBarHelperProvider.mo144get(), this.taskbarDelegateProvider.mo144get(), this.navigationBarFactoryProvider.mo144get(), this.statusBarKeyguardViewManagerProvider.mo144get(), this.dumpManagerProvider.mo144get(), this.autoHideControllerProvider.mo144get(), this.lightBarControllerProvider.mo144get(), this.pipOptionalProvider.mo144get(), this.backAnimationProvider.mo144get());
    }
}
