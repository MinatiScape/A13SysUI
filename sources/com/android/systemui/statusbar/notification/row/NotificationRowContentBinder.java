package com.android.systemui.statusbar.notification.row;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
/* loaded from: classes.dex */
public interface NotificationRowContentBinder {

    /* loaded from: classes.dex */
    public static class BindParams {
        public boolean isLowPriority;
        public boolean usesIncreasedHeadsUpHeight;
        public boolean usesIncreasedHeight;
    }

    /* loaded from: classes.dex */
    public interface InflationCallback {
        void handleInflationException(NotificationEntry notificationEntry, Exception exc);

        void onAsyncInflationFinished(NotificationEntry notificationEntry);
    }

    void bindContent(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, int i, BindParams bindParams, boolean z, RowContentBindStage.AnonymousClass1 r6);

    void cancelBind(NotificationEntry notificationEntry);

    void unbindContent(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, int i);
}
