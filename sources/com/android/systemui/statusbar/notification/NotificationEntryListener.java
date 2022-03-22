package com.android.systemui.statusbar.notification;

import android.app.NotificationChannel;
import android.os.UserHandle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* loaded from: classes.dex */
public interface NotificationEntryListener {
    default void onEntryInflated(NotificationEntry notificationEntry) {
    }

    default void onEntryReinflated(NotificationEntry notificationEntry) {
    }

    default void onEntryRemoved(NotificationEntry notificationEntry, boolean z) {
    }

    default void onInflationError(StatusBarNotification statusBarNotification, Exception exc) {
    }

    default void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
    }

    default void onNotificationRankingUpdated(NotificationListenerService.RankingMap rankingMap) {
    }

    default void onPendingEntryAdded(NotificationEntry notificationEntry) {
    }

    default void onPostEntryUpdated(NotificationEntry notificationEntry) {
    }

    default void onPreEntryUpdated(NotificationEntry notificationEntry) {
    }
}
