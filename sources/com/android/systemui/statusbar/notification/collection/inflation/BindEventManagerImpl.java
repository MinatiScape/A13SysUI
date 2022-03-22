package com.android.systemui.statusbar.notification.collection.inflation;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManager;
/* compiled from: BindEventManagerImpl.kt */
/* loaded from: classes.dex */
public final class BindEventManagerImpl extends BindEventManager {
    public final void notifyViewBound(NotificationEntry notificationEntry) {
        for (BindEventManager.Listener listener : this.listeners) {
            listener.onViewBound(notificationEntry);
        }
    }
}
