package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.notification.collection.render.NotifPanelEventSource;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.util.ListenerSet;
import java.util.Iterator;
/* compiled from: NotifPanelEventSource.kt */
/* loaded from: classes.dex */
public final class NotifPanelEventSourceManagerImpl implements NotifPanelEventSourceManager, NotifPanelEventSource.Callbacks {
    public final ListenerSet<NotifPanelEventSource.Callbacks> callbackSet = new ListenerSet<>();
    public NotifPanelEventSource eventSource;

    @Override // com.android.systemui.statusbar.notification.collection.render.NotifPanelEventSource.Callbacks
    public final void onPanelCollapsingChanged(boolean z) {
        Iterator<NotifPanelEventSource.Callbacks> it = this.callbackSet.iterator();
        while (it.hasNext()) {
            it.next().onPanelCollapsingChanged(z);
        }
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NotifPanelEventSource
    public final void registerCallbacks(NotifPanelEventSource.Callbacks callbacks) {
        this.callbackSet.addIfAbsent(callbacks);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NotifPanelEventSourceManager
    public final void setEventSource(NotificationPanelViewController notificationPanelViewController) {
        NotifPanelEventSource notifPanelEventSource = this.eventSource;
        if (notifPanelEventSource != null) {
            notifPanelEventSource.unregisterCallbacks(this);
        }
        if (notificationPanelViewController != null) {
            notificationPanelViewController.registerCallbacks(this);
        }
        this.eventSource = notificationPanelViewController;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NotifPanelEventSource
    public final void unregisterCallbacks(NotifPanelEventSource.Callbacks callbacks) {
        this.callbackSet.remove(callbacks);
    }
}
