package com.android.systemui.keyguard.dagger;

import android.app.trust.TrustManager;
import android.content.Context;
import android.os.PowerManager;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardDisplayManager;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardViewController;
import com.android.keyguard.mediator.ScreenOnCoordinator;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.DismissCallbackRegistry;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.DeviceConfigProxy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardModule_NewKeyguardViewMediatorFactory implements Factory<KeyguardViewMediator> {
    public final Provider<ActivityLaunchAnimator> activityLaunchAnimatorProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DeviceConfigProxy> deviceConfigProvider;
    public final Provider<DismissCallbackRegistry> dismissCallbackRegistryProvider;
    public final Provider<DozeParameters> dozeParametersProvider;
    public final Provider<DreamOverlayStateController> dreamOverlayStateControllerProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<FalsingCollector> falsingCollectorProvider;
    public final Provider<InteractionJankMonitor> interactionJankMonitorProvider;
    public final Provider<KeyguardDisplayManager> keyguardDisplayManagerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<KeyguardUnlockAnimationController> keyguardUnlockAnimationControllerProvider;
    public final Provider<LockPatternUtils> lockPatternUtilsProvider;
    public final Provider<NavigationModeController> navigationModeControllerProvider;
    public final Provider<NotificationShadeDepthController> notificationShadeDepthControllerProvider;
    public final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    public final Provider<PowerManager> powerManagerProvider;
    public final Provider<ScreenOffAnimationController> screenOffAnimationControllerProvider;
    public final Provider<ScreenOnCoordinator> screenOnCoordinatorProvider;
    public final Provider<KeyguardViewController> statusBarKeyguardViewManagerLazyProvider;
    public final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;
    public final Provider<TrustManager> trustManagerProvider;
    public final Provider<Executor> uiBgExecutorProvider;
    public final Provider<KeyguardUpdateMonitor> updateMonitorProvider;
    public final Provider<UserSwitcherController> userSwitcherControllerProvider;

    public KeyguardModule_NewKeyguardViewMediatorFactory(Provider<Context> provider, Provider<FalsingCollector> provider2, Provider<LockPatternUtils> provider3, Provider<BroadcastDispatcher> provider4, Provider<KeyguardViewController> provider5, Provider<DismissCallbackRegistry> provider6, Provider<KeyguardUpdateMonitor> provider7, Provider<DumpManager> provider8, Provider<PowerManager> provider9, Provider<TrustManager> provider10, Provider<UserSwitcherController> provider11, Provider<Executor> provider12, Provider<DeviceConfigProxy> provider13, Provider<NavigationModeController> provider14, Provider<KeyguardDisplayManager> provider15, Provider<DozeParameters> provider16, Provider<SysuiStatusBarStateController> provider17, Provider<KeyguardStateController> provider18, Provider<KeyguardUnlockAnimationController> provider19, Provider<ScreenOffAnimationController> provider20, Provider<NotificationShadeDepthController> provider21, Provider<ScreenOnCoordinator> provider22, Provider<InteractionJankMonitor> provider23, Provider<DreamOverlayStateController> provider24, Provider<NotificationShadeWindowController> provider25, Provider<ActivityLaunchAnimator> provider26) {
        this.contextProvider = provider;
        this.falsingCollectorProvider = provider2;
        this.lockPatternUtilsProvider = provider3;
        this.broadcastDispatcherProvider = provider4;
        this.statusBarKeyguardViewManagerLazyProvider = provider5;
        this.dismissCallbackRegistryProvider = provider6;
        this.updateMonitorProvider = provider7;
        this.dumpManagerProvider = provider8;
        this.powerManagerProvider = provider9;
        this.trustManagerProvider = provider10;
        this.userSwitcherControllerProvider = provider11;
        this.uiBgExecutorProvider = provider12;
        this.deviceConfigProvider = provider13;
        this.navigationModeControllerProvider = provider14;
        this.keyguardDisplayManagerProvider = provider15;
        this.dozeParametersProvider = provider16;
        this.statusBarStateControllerProvider = provider17;
        this.keyguardStateControllerProvider = provider18;
        this.keyguardUnlockAnimationControllerProvider = provider19;
        this.screenOffAnimationControllerProvider = provider20;
        this.notificationShadeDepthControllerProvider = provider21;
        this.screenOnCoordinatorProvider = provider22;
        this.interactionJankMonitorProvider = provider23;
        this.dreamOverlayStateControllerProvider = provider24;
        this.notificationShadeWindowControllerProvider = provider25;
        this.activityLaunchAnimatorProvider = provider26;
    }

    public static KeyguardModule_NewKeyguardViewMediatorFactory create(Provider<Context> provider, Provider<FalsingCollector> provider2, Provider<LockPatternUtils> provider3, Provider<BroadcastDispatcher> provider4, Provider<KeyguardViewController> provider5, Provider<DismissCallbackRegistry> provider6, Provider<KeyguardUpdateMonitor> provider7, Provider<DumpManager> provider8, Provider<PowerManager> provider9, Provider<TrustManager> provider10, Provider<UserSwitcherController> provider11, Provider<Executor> provider12, Provider<DeviceConfigProxy> provider13, Provider<NavigationModeController> provider14, Provider<KeyguardDisplayManager> provider15, Provider<DozeParameters> provider16, Provider<SysuiStatusBarStateController> provider17, Provider<KeyguardStateController> provider18, Provider<KeyguardUnlockAnimationController> provider19, Provider<ScreenOffAnimationController> provider20, Provider<NotificationShadeDepthController> provider21, Provider<ScreenOnCoordinator> provider22, Provider<InteractionJankMonitor> provider23, Provider<DreamOverlayStateController> provider24, Provider<NotificationShadeWindowController> provider25, Provider<ActivityLaunchAnimator> provider26) {
        return new KeyguardModule_NewKeyguardViewMediatorFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new KeyguardViewMediator(this.contextProvider.mo144get(), this.falsingCollectorProvider.mo144get(), this.lockPatternUtilsProvider.mo144get(), this.broadcastDispatcherProvider.mo144get(), DoubleCheck.lazy(this.statusBarKeyguardViewManagerLazyProvider), this.dismissCallbackRegistryProvider.mo144get(), this.updateMonitorProvider.mo144get(), this.dumpManagerProvider.mo144get(), this.uiBgExecutorProvider.mo144get(), this.powerManagerProvider.mo144get(), this.trustManagerProvider.mo144get(), this.userSwitcherControllerProvider.mo144get(), this.deviceConfigProvider.mo144get(), this.navigationModeControllerProvider.mo144get(), this.keyguardDisplayManagerProvider.mo144get(), this.dozeParametersProvider.mo144get(), this.statusBarStateControllerProvider.mo144get(), this.keyguardStateControllerProvider.mo144get(), DoubleCheck.lazy(this.keyguardUnlockAnimationControllerProvider), this.screenOffAnimationControllerProvider.mo144get(), DoubleCheck.lazy(this.notificationShadeDepthControllerProvider), this.screenOnCoordinatorProvider.mo144get(), this.interactionJankMonitorProvider.mo144get(), this.dreamOverlayStateControllerProvider.mo144get(), DoubleCheck.lazy(this.notificationShadeWindowControllerProvider), DoubleCheck.lazy(this.activityLaunchAnimatorProvider));
    }
}
