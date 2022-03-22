package com.android.systemui.statusbar;

import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.util.Assert;
import java.util.LinkedHashMap;
import java.util.Objects;
/* compiled from: NotificationInteractionTracker.kt */
/* loaded from: classes.dex */
public final class NotificationInteractionTracker implements NotifCollectionListener, NotificationInteractionListener {
    public final LinkedHashMap interactions = new LinkedHashMap();

    public final boolean hasUserInteractedWith(String str) {
        Boolean bool = (Boolean) this.interactions.get(str);
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
    public final void onEntryAdded(NotificationEntry notificationEntry) {
        this.interactions.put(notificationEntry.mKey, Boolean.FALSE);
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
    public final void onEntryCleanUp(NotificationEntry notificationEntry) {
        this.interactions.remove(notificationEntry.mKey);
    }

    @Override // com.android.systemui.statusbar.NotificationInteractionListener
    public final void onNotificationInteraction(String str) {
        this.interactions.put(str, Boolean.TRUE);
    }

    public NotificationInteractionTracker(NotificationClickNotifier notificationClickNotifier, NotificationEntryManager notificationEntryManager) {
        Objects.requireNonNull(notificationClickNotifier);
        Assert.isMainThread();
        notificationClickNotifier.listeners.add(this);
        notificationEntryManager.addCollectionListener(this);
    }
}
