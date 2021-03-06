package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.app.NotificationChannel;
import android.os.UserHandle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* loaded from: classes.dex */
public interface NotifCollectionListener {
    default void onEntryAdded(NotificationEntry notificationEntry) {
    }

    default void onEntryBind(NotificationEntry notificationEntry, StatusBarNotification statusBarNotification) {
    }

    default void onEntryCleanUp(NotificationEntry notificationEntry) {
    }

    default void onEntryInit(NotificationEntry notificationEntry) {
    }

    default void onEntryRemoved(NotificationEntry notificationEntry, int i) {
    }

    default void onEntryUpdated(NotificationEntry notificationEntry) {
    }

    default void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
    }

    default void onRankingApplied() {
    }

    default void onRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
    }

    default void onEntryUpdated(NotificationEntry notificationEntry, boolean z) {
        onEntryUpdated(notificationEntry);
    }
}
