package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.notification.collection.render.NotifPanelEventSourceModule_ProvideManagerFactory;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.dagger.StatusBarComponent;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarNotifPanelEventSourceModule_BindStartableFactory implements Factory<StatusBarComponent.Startable> {
    public final Provider<NotifPanelEventSourceManager> managerProvider = NotifPanelEventSourceModule_ProvideManagerFactory.InstanceHolder.INSTANCE;
    public final Provider<NotificationPanelViewController> notifPanelControllerProvider;

    public StatusBarNotifPanelEventSourceModule_BindStartableFactory(Provider provider) {
        this.notifPanelControllerProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new EventSourceStatusBarStartableImpl(this.managerProvider.mo144get(), this.notifPanelControllerProvider.mo144get());
    }
}
