package com.android.systemui.statusbar.notification.logging;

import android.service.notification.StatusBarNotification;
import android.util.StatsEvent;
import android.util.StatsLog;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.logging.NotificationPanelLogger;
import com.android.systemui.statusbar.notification.logging.nano.Notifications$Notification;
import com.android.systemui.statusbar.notification.logging.nano.Notifications$NotificationList;
import com.google.protobuf.nano.MessageNano;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class NotificationPanelLoggerImpl implements NotificationPanelLogger {
    @Override // com.android.systemui.statusbar.notification.logging.NotificationPanelLogger
    public final void logPanelShown(boolean z, List<NotificationEntry> list) {
        NotificationPanelLogger.NotificationPanelEvent notificationPanelEvent;
        int i;
        Notifications$NotificationList notifications$NotificationList = new Notifications$NotificationList();
        if (list != null) {
            Notifications$Notification[] notifications$NotificationArr = new Notifications$Notification[list.size()];
            int i2 = 0;
            for (NotificationEntry notificationEntry : list) {
                Objects.requireNonNull(notificationEntry);
                StatusBarNotification statusBarNotification = notificationEntry.mSbn;
                if (statusBarNotification != null) {
                    Notifications$Notification notifications$Notification = new Notifications$Notification();
                    notifications$Notification.uid = statusBarNotification.getUid();
                    notifications$Notification.packageName = statusBarNotification.getPackageName();
                    if (statusBarNotification.getInstanceId() != null) {
                        notifications$Notification.instanceId = statusBarNotification.getInstanceId().getId();
                    }
                    if (statusBarNotification.getNotification() != null) {
                        notifications$Notification.isGroupSummary = statusBarNotification.getNotification().isGroupSummary();
                    }
                    switch (notificationEntry.mBucket) {
                        case 1:
                            i = 2;
                            break;
                        case 2:
                            i = 1;
                            break;
                        case 3:
                            i = 6;
                            break;
                        case 4:
                            i = 3;
                            break;
                        case 5:
                            i = 4;
                            break;
                        case FalsingManager.VERSION /* 6 */:
                            i = 5;
                            break;
                        default:
                            i = 0;
                            break;
                    }
                    notifications$Notification.section = i;
                    notifications$NotificationArr[i2] = notifications$Notification;
                }
                i2++;
            }
            notifications$NotificationList.notifications = notifications$NotificationArr;
        }
        if (z) {
            notificationPanelEvent = NotificationPanelLogger.NotificationPanelEvent.NOTIFICATION_PANEL_OPEN_LOCKSCREEN;
        } else {
            notificationPanelEvent = NotificationPanelLogger.NotificationPanelEvent.NOTIFICATION_PANEL_OPEN_STATUS_BAR;
        }
        int id = notificationPanelEvent.getId();
        int length = notifications$NotificationList.notifications.length;
        byte[] byteArray = MessageNano.toByteArray(notifications$NotificationList);
        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
        newBuilder.setAtomId(245);
        newBuilder.writeInt(id);
        newBuilder.writeInt(length);
        newBuilder.writeByteArray(byteArray);
        newBuilder.usePooledBuffer();
        StatsLog.write(newBuilder.build());
    }
}
