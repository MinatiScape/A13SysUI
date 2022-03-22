package com.android.systemui.statusbar.notification.collection.provider;

import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
/* loaded from: classes.dex */
public final class HighPriorityProvider {
    public final GroupMembershipManager mGroupMembershipManager;
    public final PeopleNotificationIdentifier mPeopleNotificationIdentifier;

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0053, code lost:
        if (r1.mSbn.getNotification().isStyle(android.app.Notification.MessagingStyle.class) == false) goto L_0x0057;
     */
    /* JADX WARN: Removed duplicated region for block: B:29:0x005a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean isHighPriority(com.android.systemui.statusbar.notification.collection.ListEntry r6) {
        /*
            r5 = this;
            r0 = 0
            if (r6 != 0) goto L_0x0004
            return r0
        L_0x0004:
            com.android.systemui.statusbar.notification.collection.NotificationEntry r1 = r6.getRepresentativeEntry()
            if (r1 != 0) goto L_0x000b
            return r0
        L_0x000b:
            android.service.notification.NotificationListenerService$Ranking r2 = r1.mRanking
            int r2 = r2.getImportance()
            r3 = 3
            r4 = 1
            if (r2 >= r3) goto L_0x008f
            android.service.notification.NotificationListenerService$Ranking r2 = r1.mRanking
            android.app.NotificationChannel r2 = r2.getChannel()
            if (r2 == 0) goto L_0x002b
            android.service.notification.NotificationListenerService$Ranking r2 = r1.mRanking
            android.app.NotificationChannel r2 = r2.getChannel()
            boolean r2 = r2.hasUserSetImportance()
            if (r2 == 0) goto L_0x002b
            r2 = r4
            goto L_0x002c
        L_0x002b:
            r2 = r0
        L_0x002c:
            if (r2 != 0) goto L_0x0057
            android.service.notification.StatusBarNotification r2 = r1.mSbn
            android.app.Notification r2 = r2.getNotification()
            boolean r2 = r2.isMediaNotification()
            if (r2 != 0) goto L_0x0055
            com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier r2 = r5.mPeopleNotificationIdentifier
            int r2 = r2.getPeopleNotificationType(r1)
            if (r2 == 0) goto L_0x0044
            r2 = r4
            goto L_0x0045
        L_0x0044:
            r2 = r0
        L_0x0045:
            if (r2 != 0) goto L_0x0055
            android.service.notification.StatusBarNotification r1 = r1.mSbn
            android.app.Notification r1 = r1.getNotification()
            java.lang.Class<android.app.Notification$MessagingStyle> r2 = android.app.Notification.MessagingStyle.class
            boolean r1 = r1.isStyle(r2)
            if (r1 == 0) goto L_0x0057
        L_0x0055:
            r1 = r4
            goto L_0x0058
        L_0x0057:
            r1 = r0
        L_0x0058:
            if (r1 != 0) goto L_0x008f
            boolean r1 = r6 instanceof com.android.systemui.statusbar.notification.collection.NotificationEntry
            if (r1 == 0) goto L_0x006a
            com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager r1 = r5.mGroupMembershipManager
            r2 = r6
            com.android.systemui.statusbar.notification.collection.NotificationEntry r2 = (com.android.systemui.statusbar.notification.collection.NotificationEntry) r2
            boolean r1 = r1.isGroupSummary(r2)
            if (r1 != 0) goto L_0x006a
            goto L_0x008c
        L_0x006a:
            com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager r1 = r5.mGroupMembershipManager
            java.util.List r1 = r1.getChildren(r6)
            if (r1 == 0) goto L_0x008c
            java.util.Iterator r1 = r1.iterator()
        L_0x0076:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x008c
            java.lang.Object r2 = r1.next()
            com.android.systemui.statusbar.notification.collection.NotificationEntry r2 = (com.android.systemui.statusbar.notification.collection.NotificationEntry) r2
            if (r2 == r6) goto L_0x0076
            boolean r2 = r5.isHighPriority(r2)
            if (r2 == 0) goto L_0x0076
            r5 = r4
            goto L_0x008d
        L_0x008c:
            r5 = r0
        L_0x008d:
            if (r5 == 0) goto L_0x0090
        L_0x008f:
            r0 = r4
        L_0x0090:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider.isHighPriority(com.android.systemui.statusbar.notification.collection.ListEntry):boolean");
    }

    public HighPriorityProvider(PeopleNotificationIdentifier peopleNotificationIdentifier, GroupMembershipManager groupMembershipManager) {
        this.mPeopleNotificationIdentifier = peopleNotificationIdentifier;
        this.mGroupMembershipManager = groupMembershipManager;
    }
}
