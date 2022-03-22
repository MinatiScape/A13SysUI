package com.android.systemui.statusbar.notification.collection.legacy;

import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
/* loaded from: classes.dex */
public final class LowPriorityInflationHelper {
    public final NotificationGroupManagerLegacy mGroupManager;
    public final NotifPipelineFlags mNotifPipelineFlags;
    public final RowContentBindStage mRowContentBindStage;

    public LowPriorityInflationHelper(NotificationGroupManagerLegacy notificationGroupManagerLegacy, RowContentBindStage rowContentBindStage, NotifPipelineFlags notifPipelineFlags) {
        this.mGroupManager = notificationGroupManagerLegacy;
        this.mRowContentBindStage = rowContentBindStage;
        this.mNotifPipelineFlags = notifPipelineFlags;
    }
}
