package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.statusbar.notification.collection.NotifCollection$$ExternalSyntheticLambda2;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* loaded from: classes.dex */
public interface NotifLifetimeExtender {

    /* loaded from: classes.dex */
    public interface OnEndLifetimeExtensionCallback {
    }

    void cancelLifetimeExtension(NotificationEntry notificationEntry);

    String getName();

    boolean maybeExtendLifetime(NotificationEntry notificationEntry, int i);

    void setCallback(NotifCollection$$ExternalSyntheticLambda2 notifCollection$$ExternalSyntheticLambda2);
}
