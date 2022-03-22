package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DynamicChildBindController {
    public final int mChildBindCutoff = 9;
    public final RowContentBindStage mStage;

    public DynamicChildBindController(RowContentBindStage rowContentBindStage) {
        this.mStage = rowContentBindStage;
    }

    public static boolean hasContent(NotificationEntry notificationEntry) {
        Objects.requireNonNull(notificationEntry);
        ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
        Objects.requireNonNull(expandableNotificationRow);
        NotificationContentView notificationContentView = expandableNotificationRow.mPrivateLayout;
        Objects.requireNonNull(notificationContentView);
        if (notificationContentView.mContractedChild == null) {
            NotificationContentView notificationContentView2 = expandableNotificationRow.mPrivateLayout;
            Objects.requireNonNull(notificationContentView2);
            if (notificationContentView2.mExpandedChild == null) {
                return false;
            }
        }
        return true;
    }
}
