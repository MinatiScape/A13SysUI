package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class HeadsUpController_Factory implements Factory<HeadsUpController> {
    public final Provider<HeadsUpManager> headsUpManagerProvider;
    public final Provider<HeadsUpViewBinder> headsUpViewBinderProvider;
    public final Provider<NotificationInterruptStateProvider> notificationInterruptStateProvider;
    public final Provider<NotificationListener> notificationListenerProvider;
    public final Provider<NotificationRemoteInputManager> remoteInputManagerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<VisualStabilityManager> visualStabilityManagerProvider;

    public static HeadsUpController_Factory create(Provider<HeadsUpViewBinder> provider, Provider<NotificationInterruptStateProvider> provider2, Provider<HeadsUpManager> provider3, Provider<NotificationRemoteInputManager> provider4, Provider<StatusBarStateController> provider5, Provider<VisualStabilityManager> provider6, Provider<NotificationListener> provider7) {
        return new HeadsUpController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new HeadsUpController(this.headsUpViewBinderProvider.mo144get(), this.notificationInterruptStateProvider.mo144get(), this.headsUpManagerProvider.mo144get(), this.remoteInputManagerProvider.mo144get(), this.statusBarStateControllerProvider.mo144get(), this.visualStabilityManagerProvider.mo144get(), this.notificationListenerProvider.mo144get());
    }

    public HeadsUpController_Factory(Provider<HeadsUpViewBinder> provider, Provider<NotificationInterruptStateProvider> provider2, Provider<HeadsUpManager> provider3, Provider<NotificationRemoteInputManager> provider4, Provider<StatusBarStateController> provider5, Provider<VisualStabilityManager> provider6, Provider<NotificationListener> provider7) {
        this.headsUpViewBinderProvider = provider;
        this.notificationInterruptStateProvider = provider2;
        this.headsUpManagerProvider = provider3;
        this.remoteInputManagerProvider = provider4;
        this.statusBarStateControllerProvider = provider5;
        this.visualStabilityManagerProvider = provider6;
        this.notificationListenerProvider = provider7;
    }
}
