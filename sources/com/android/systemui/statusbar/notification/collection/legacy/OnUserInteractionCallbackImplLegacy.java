package com.android.systemui.statusbar.notification.collection.legacy;

import android.os.SystemClock;
import android.service.notification.NotificationListenerService;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.row.OnUserInteractionCallback;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import java.util.Objects;
/* loaded from: classes.dex */
public final class OnUserInteractionCallbackImplLegacy implements OnUserInteractionCallback {
    public final GroupMembershipManager mGroupMembershipManager;
    public final HeadsUpManager mHeadsUpManager;
    public final NotificationEntryManager mNotificationEntryManager;
    public final StatusBarStateController mStatusBarStateController;
    public final NotificationVisibilityProvider mVisibilityProvider;
    public final VisualStabilityManager mVisualStabilityManager;

    @Override // com.android.systemui.statusbar.notification.row.OnUserInteractionCallback
    public final NotificationEntry getGroupSummaryToDismiss(NotificationEntry notificationEntry) {
        if (!this.mGroupMembershipManager.isOnlyChildInGroup(notificationEntry)) {
            return null;
        }
        NotificationEntry logicalGroupSummary = this.mGroupMembershipManager.getLogicalGroupSummary(notificationEntry);
        if (logicalGroupSummary.isDismissable()) {
            return logicalGroupSummary;
        }
        return null;
    }

    @Override // com.android.systemui.statusbar.notification.row.OnUserInteractionCallback
    public final void onDismiss(NotificationEntry notificationEntry, @NotificationListenerService.NotificationCancelReason int i, NotificationEntry notificationEntry2) {
        int i2;
        HeadsUpManager headsUpManager = this.mHeadsUpManager;
        Objects.requireNonNull(notificationEntry);
        if (headsUpManager.isAlerting(notificationEntry.mKey)) {
            i2 = 1;
        } else if (this.mStatusBarStateController.isDozing()) {
            i2 = 2;
        } else {
            i2 = 3;
        }
        if (notificationEntry2 != null) {
            onDismiss(notificationEntry2, i, null);
        }
        this.mNotificationEntryManager.performRemoveNotification(notificationEntry.mSbn, new DismissedByUserStats(i2, this.mVisibilityProvider.obtain(notificationEntry, true)), i);
    }

    @Override // com.android.systemui.statusbar.notification.row.OnUserInteractionCallback
    public final void onImportanceChanged(NotificationEntry notificationEntry) {
        VisualStabilityManager visualStabilityManager = this.mVisualStabilityManager;
        Objects.requireNonNull(visualStabilityManager);
        visualStabilityManager.mHandler.removeCallbacks(visualStabilityManager.mOnTemporaryReorderingExpired);
        visualStabilityManager.mHandler.postDelayed(visualStabilityManager.mOnTemporaryReorderingExpired, 1000L);
        if (!visualStabilityManager.mIsTemporaryReorderingAllowed) {
            visualStabilityManager.mTemporaryReorderingStart = SystemClock.elapsedRealtime();
        }
        visualStabilityManager.mIsTemporaryReorderingAllowed = true;
        visualStabilityManager.updateAllowedStates();
    }

    public OnUserInteractionCallbackImplLegacy(NotificationEntryManager notificationEntryManager, NotificationVisibilityProvider notificationVisibilityProvider, HeadsUpManager headsUpManager, StatusBarStateController statusBarStateController, VisualStabilityManager visualStabilityManager, GroupMembershipManager groupMembershipManager) {
        this.mNotificationEntryManager = notificationEntryManager;
        this.mVisibilityProvider = notificationVisibilityProvider;
        this.mHeadsUpManager = headsUpManager;
        this.mStatusBarStateController = statusBarStateController;
        this.mVisualStabilityManager = visualStabilityManager;
        this.mGroupMembershipManager = groupMembershipManager;
    }
}
