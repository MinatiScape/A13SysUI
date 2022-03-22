package com.android.systemui.statusbar.phone.fragment.dagger;

import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.PhoneStatusBarViewController;
import com.android.systemui.statusbar.phone.PhoneStatusBarViewController_Factory_Factory;
import com.android.systemui.statusbar.phone.StatusBarMoveFromCenterAnimationController;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarFragmentModule_ProvidePhoneStatusBarViewControllerFactory implements Factory<PhoneStatusBarViewController> {
    public final Provider<NotificationPanelViewController> notificationPanelViewControllerProvider;
    public final Provider<PhoneStatusBarViewController.Factory> phoneStatusBarViewControllerFactoryProvider;
    public final Provider<PhoneStatusBarView> phoneStatusBarViewProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        StatusBarMoveFromCenterAnimationController statusBarMoveFromCenterAnimationController;
        PhoneStatusBarViewController.Factory factory = this.phoneStatusBarViewControllerFactoryProvider.mo144get();
        PhoneStatusBarView phoneStatusBarView = this.phoneStatusBarViewProvider.mo144get();
        NotificationPanelViewController notificationPanelViewController = this.notificationPanelViewControllerProvider.mo144get();
        Objects.requireNonNull(notificationPanelViewController);
        NotificationPanelViewController.AnonymousClass18 r7 = notificationPanelViewController.mStatusBarViewTouchEventHandler;
        Objects.requireNonNull(factory);
        ScopedUnfoldTransitionProgressProvider orElse = factory.progressProvider.orElse(null);
        SysUIUnfoldComponent orElse2 = factory.unfoldComponent.orElse(null);
        if (orElse2 == null) {
            statusBarMoveFromCenterAnimationController = null;
        } else {
            statusBarMoveFromCenterAnimationController = orElse2.getStatusBarMoveFromCenterAnimationController();
        }
        return new PhoneStatusBarViewController(phoneStatusBarView, orElse, statusBarMoveFromCenterAnimationController, factory.userSwitcherController, r7, factory.configurationController);
    }

    public StatusBarFragmentModule_ProvidePhoneStatusBarViewControllerFactory(PhoneStatusBarViewController_Factory_Factory phoneStatusBarViewController_Factory_Factory, Provider provider, Provider provider2) {
        this.phoneStatusBarViewControllerFactoryProvider = phoneStatusBarViewController_Factory_Factory;
        this.phoneStatusBarViewProvider = provider;
        this.notificationPanelViewControllerProvider = provider2;
    }
}
