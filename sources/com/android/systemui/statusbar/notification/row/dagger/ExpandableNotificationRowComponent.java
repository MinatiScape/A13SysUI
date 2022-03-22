package com.android.systemui.statusbar.notification.row.dagger;

import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
/* loaded from: classes.dex */
public interface ExpandableNotificationRowComponent {

    /* loaded from: classes.dex */
    public interface Builder {
        ExpandableNotificationRowComponent build();

        /* renamed from: expandableNotificationRow */
        Builder mo137expandableNotificationRow(ExpandableNotificationRow expandableNotificationRow);

        /* renamed from: listContainer */
        Builder mo138listContainer(NotificationListContainer notificationListContainer);

        /* renamed from: notificationEntry */
        Builder mo139notificationEntry(NotificationEntry notificationEntry);

        Builder onExpandClickListener(NotificationPresenter notificationPresenter);
    }

    ExpandableNotificationRowController getExpandableNotificationRowController();
}
