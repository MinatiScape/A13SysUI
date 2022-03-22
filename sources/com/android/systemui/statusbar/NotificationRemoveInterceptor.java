package com.android.systemui.statusbar;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* loaded from: classes.dex */
public interface NotificationRemoveInterceptor {
    boolean onNotificationRemoveRequested(NotificationEntry notificationEntry, int i);
}
