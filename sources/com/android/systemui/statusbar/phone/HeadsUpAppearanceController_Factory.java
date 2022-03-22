package com.android.systemui.statusbar.phone;

import android.view.View;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.HeadsUpStatusBarView;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.policy.Clock;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class HeadsUpAppearanceController_Factory implements Factory<HeadsUpAppearanceController> {
    public final Provider<KeyguardBypassController> bypassControllerProvider;
    public final Provider<Clock> clockViewProvider;
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<DarkIconDispatcher> darkIconDispatcherProvider;
    public final Provider<HeadsUpManagerPhone> headsUpManagerProvider;
    public final Provider<HeadsUpStatusBarView> headsUpStatusBarViewProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<NotificationIconAreaController> notificationIconAreaControllerProvider;
    public final Provider<NotificationPanelViewController> notificationPanelViewControllerProvider;
    public final Provider<Optional<View>> operatorNameViewOptionalProvider;
    public final Provider<NotificationStackScrollLayoutController> stackScrollerControllerProvider;
    public final Provider<StatusBarStateController> stateControllerProvider;
    public final Provider<NotificationWakeUpCoordinator> wakeUpCoordinatorProvider;

    public static HeadsUpAppearanceController_Factory create(Provider<NotificationIconAreaController> provider, Provider<HeadsUpManagerPhone> provider2, Provider<StatusBarStateController> provider3, Provider<KeyguardBypassController> provider4, Provider<NotificationWakeUpCoordinator> provider5, Provider<DarkIconDispatcher> provider6, Provider<KeyguardStateController> provider7, Provider<CommandQueue> provider8, Provider<NotificationStackScrollLayoutController> provider9, Provider<NotificationPanelViewController> provider10, Provider<HeadsUpStatusBarView> provider11, Provider<Clock> provider12, Provider<Optional<View>> provider13) {
        return new HeadsUpAppearanceController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new HeadsUpAppearanceController(this.notificationIconAreaControllerProvider.mo144get(), this.headsUpManagerProvider.mo144get(), this.stateControllerProvider.mo144get(), this.bypassControllerProvider.mo144get(), this.wakeUpCoordinatorProvider.mo144get(), this.darkIconDispatcherProvider.mo144get(), this.keyguardStateControllerProvider.mo144get(), this.commandQueueProvider.mo144get(), this.stackScrollerControllerProvider.mo144get(), this.notificationPanelViewControllerProvider.mo144get(), this.headsUpStatusBarViewProvider.mo144get(), this.clockViewProvider.mo144get(), this.operatorNameViewOptionalProvider.mo144get());
    }

    public HeadsUpAppearanceController_Factory(Provider<NotificationIconAreaController> provider, Provider<HeadsUpManagerPhone> provider2, Provider<StatusBarStateController> provider3, Provider<KeyguardBypassController> provider4, Provider<NotificationWakeUpCoordinator> provider5, Provider<DarkIconDispatcher> provider6, Provider<KeyguardStateController> provider7, Provider<CommandQueue> provider8, Provider<NotificationStackScrollLayoutController> provider9, Provider<NotificationPanelViewController> provider10, Provider<HeadsUpStatusBarView> provider11, Provider<Clock> provider12, Provider<Optional<View>> provider13) {
        this.notificationIconAreaControllerProvider = provider;
        this.headsUpManagerProvider = provider2;
        this.stateControllerProvider = provider3;
        this.bypassControllerProvider = provider4;
        this.wakeUpCoordinatorProvider = provider5;
        this.darkIconDispatcherProvider = provider6;
        this.keyguardStateControllerProvider = provider7;
        this.commandQueueProvider = provider8;
        this.stackScrollerControllerProvider = provider9;
        this.notificationPanelViewControllerProvider = provider10;
        this.headsUpStatusBarViewProvider = provider11;
        this.clockViewProvider = provider12;
        this.operatorNameViewOptionalProvider = provider13;
    }
}
