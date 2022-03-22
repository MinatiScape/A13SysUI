package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.phone.PhoneStatusBarViewController;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PhoneStatusBarViewController_Factory_Factory implements Factory<PhoneStatusBarViewController.Factory> {
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Optional<ScopedUnfoldTransitionProgressProvider>> progressProvider;
    public final Provider<Optional<SysUIUnfoldComponent>> unfoldComponentProvider;
    public final Provider<StatusBarUserSwitcherController> userSwitcherControllerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new PhoneStatusBarViewController.Factory(this.unfoldComponentProvider.mo144get(), this.progressProvider.mo144get(), this.userSwitcherControllerProvider.mo144get(), this.configurationControllerProvider.mo144get());
    }

    public PhoneStatusBarViewController_Factory_Factory(Provider<Optional<SysUIUnfoldComponent>> provider, Provider<Optional<ScopedUnfoldTransitionProgressProvider>> provider2, Provider<StatusBarUserSwitcherController> provider3, Provider<ConfigurationController> provider4) {
        this.unfoldComponentProvider = provider;
        this.progressProvider = provider2;
        this.userSwitcherControllerProvider = provider3;
        this.configurationControllerProvider = provider4;
    }
}
