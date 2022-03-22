package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.res.Resources;
import android.widget.FrameLayout;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.communal.CommunalStateController;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.user.UserSwitchDialogController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import dagger.internal.Factory;
import dagger.internal.InstanceFactory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardQsUserSwitchController_Factory implements Factory<KeyguardQsUserSwitchController> {
    public final Provider<CommunalStateController> communalStateControllerProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DozeParameters> dozeParametersProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<Resources> resourcesProvider;
    public final Provider<ScreenLifecycle> screenLifecycleProvider;
    public final Provider<ScreenOffAnimationController> screenOffAnimationControllerProvider;
    public final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<UserSwitchDialogController> userSwitchDialogControllerProvider;
    public final Provider<UserSwitcherController> userSwitcherControllerProvider;
    public final Provider<FrameLayout> viewProvider;

    public static KeyguardQsUserSwitchController_Factory create(InstanceFactory instanceFactory, Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11, Provider provider12, Provider provider13) {
        return new KeyguardQsUserSwitchController_Factory(instanceFactory, provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        FrameLayout frameLayout = this.viewProvider.mo144get();
        Context context = this.contextProvider.mo144get();
        Resources resources = this.resourcesProvider.mo144get();
        this.screenLifecycleProvider.mo144get();
        UserSwitcherController userSwitcherController = this.userSwitcherControllerProvider.mo144get();
        CommunalStateController communalStateController = this.communalStateControllerProvider.mo144get();
        KeyguardStateController keyguardStateController = this.keyguardStateControllerProvider.mo144get();
        FalsingManager falsingManager = this.falsingManagerProvider.mo144get();
        ConfigurationController configurationController = this.configurationControllerProvider.mo144get();
        SysuiStatusBarStateController sysuiStatusBarStateController = this.statusBarStateControllerProvider.mo144get();
        this.dozeParametersProvider.mo144get();
        return new KeyguardQsUserSwitchController(frameLayout, context, resources, userSwitcherController, communalStateController, keyguardStateController, falsingManager, configurationController, sysuiStatusBarStateController, this.screenOffAnimationControllerProvider.mo144get(), this.userSwitchDialogControllerProvider.mo144get(), this.uiEventLoggerProvider.mo144get());
    }

    public KeyguardQsUserSwitchController_Factory(InstanceFactory instanceFactory, Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11, Provider provider12, Provider provider13) {
        this.viewProvider = instanceFactory;
        this.contextProvider = provider;
        this.resourcesProvider = provider2;
        this.screenLifecycleProvider = provider3;
        this.userSwitcherControllerProvider = provider4;
        this.communalStateControllerProvider = provider5;
        this.keyguardStateControllerProvider = provider6;
        this.falsingManagerProvider = provider7;
        this.configurationControllerProvider = provider8;
        this.statusBarStateControllerProvider = provider9;
        this.dozeParametersProvider = provider10;
        this.screenOffAnimationControllerProvider = provider11;
        this.userSwitchDialogControllerProvider = provider12;
        this.uiEventLoggerProvider = provider13;
    }
}
