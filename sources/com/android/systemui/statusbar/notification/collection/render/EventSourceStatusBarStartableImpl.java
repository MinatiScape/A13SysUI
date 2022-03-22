package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.dagger.StatusBarComponent;
/* compiled from: NotifPanelEventSource.kt */
/* loaded from: classes.dex */
public final class EventSourceStatusBarStartableImpl implements StatusBarComponent.Startable {
    public final NotifPanelEventSourceManager manager;
    public final NotificationPanelViewController notifPanelController;

    @Override // com.android.systemui.statusbar.phone.dagger.StatusBarComponent.Startable
    public final void start() {
        this.manager.setEventSource(this.notifPanelController);
    }

    @Override // com.android.systemui.statusbar.phone.dagger.StatusBarComponent.Startable
    public final void stop() {
        this.manager.setEventSource(null);
    }

    public EventSourceStatusBarStartableImpl(NotifPanelEventSourceManager notifPanelEventSourceManager, NotificationPanelViewController notificationPanelViewController) {
        this.manager = notifPanelEventSourceManager;
        this.notifPanelController = notificationPanelViewController;
    }
}
