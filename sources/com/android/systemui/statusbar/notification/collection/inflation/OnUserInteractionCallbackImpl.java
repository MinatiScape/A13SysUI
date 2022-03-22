package com.android.systemui.statusbar.notification.collection.inflation;

import android.os.SystemClock;
import android.service.notification.NotificationListenerService;
import com.android.keyguard.clock.ClockManager$$ExternalSyntheticLambda1;
import com.android.systemui.people.PeopleTileViewHelper$$ExternalSyntheticLambda3;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.row.OnUserInteractionCallback;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.theme.ThemeOverlayApplier$$ExternalSyntheticLambda6;
import com.android.systemui.util.condition.Monitor$$ExternalSyntheticLambda5;
import java.util.Objects;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final class OnUserInteractionCallbackImpl implements OnUserInteractionCallback {
    public final GroupMembershipManager mGroupMembershipManager;
    public final HeadsUpManager mHeadsUpManager;
    public final NotifCollection mNotifCollection;
    public final StatusBarStateController mStatusBarStateController;
    public final NotificationVisibilityProvider mVisibilityProvider;
    public final VisualStabilityCoordinator mVisualStabilityCoordinator;

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
        this.mNotifCollection.dismissNotification(notificationEntry, new DismissedByUserStats(i2, this.mVisibilityProvider.obtain(notificationEntry, true)));
    }

    @Override // com.android.systemui.statusbar.notification.row.OnUserInteractionCallback
    public final void onImportanceChanged(NotificationEntry notificationEntry) {
        VisualStabilityCoordinator visualStabilityCoordinator = this.mVisualStabilityCoordinator;
        long uptimeMillis = SystemClock.uptimeMillis();
        Objects.requireNonNull(visualStabilityCoordinator);
        Objects.requireNonNull(notificationEntry);
        String str = notificationEntry.mKey;
        boolean isSectionChangeAllowed = visualStabilityCoordinator.mNotifStabilityManager.isSectionChangeAllowed(notificationEntry);
        if (visualStabilityCoordinator.mEntriesThatCanChangeSection.containsKey(str)) {
            ((Runnable) visualStabilityCoordinator.mEntriesThatCanChangeSection.get(str)).run();
        }
        visualStabilityCoordinator.mEntriesThatCanChangeSection.put(str, visualStabilityCoordinator.mDelayableExecutor.m145executeAtTime(new ClockManager$$ExternalSyntheticLambda1(visualStabilityCoordinator, str, 3), uptimeMillis + 500));
        if (!isSectionChangeAllowed) {
            visualStabilityCoordinator.mNotifStabilityManager.invalidateList();
        }
    }

    public OnUserInteractionCallbackImpl(NotificationVisibilityProvider notificationVisibilityProvider, NotifCollection notifCollection, HeadsUpManager headsUpManager, StatusBarStateController statusBarStateController, VisualStabilityCoordinator visualStabilityCoordinator, GroupMembershipManager groupMembershipManager) {
        this.mVisibilityProvider = notificationVisibilityProvider;
        this.mNotifCollection = notifCollection;
        this.mHeadsUpManager = headsUpManager;
        this.mStatusBarStateController = statusBarStateController;
        this.mVisualStabilityCoordinator = visualStabilityCoordinator;
        this.mGroupMembershipManager = groupMembershipManager;
    }

    @Override // com.android.systemui.statusbar.notification.row.OnUserInteractionCallback
    public final NotificationEntry getGroupSummaryToDismiss(NotificationEntry notificationEntry) {
        boolean z;
        Objects.requireNonNull(notificationEntry);
        String group = notificationEntry.mSbn.getGroup();
        NotifCollection notifCollection = this.mNotifCollection;
        Objects.requireNonNull(notifCollection);
        final String group2 = notificationEntry.mSbn.getGroup();
        if (notifCollection.mNotificationSet.get(notificationEntry.mKey) == notificationEntry && notifCollection.mNotificationSet.values().stream().filter(new Predicate() { // from class: com.android.systemui.statusbar.notification.collection.NotifCollection$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                String str = group2;
                NotificationEntry notificationEntry2 = (NotificationEntry) obj;
                Objects.requireNonNull(notificationEntry2);
                return Objects.equals(notificationEntry2.mSbn.getGroup(), str);
            }
        }).filter(Monitor$$ExternalSyntheticLambda5.INSTANCE$1).count() == 1) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            NotifCollection notifCollection2 = this.mNotifCollection;
            Objects.requireNonNull(notifCollection2);
            NotificationEntry notificationEntry2 = (NotificationEntry) notifCollection2.mNotificationSet.values().stream().filter(new ThemeOverlayApplier$$ExternalSyntheticLambda6(group, 1)).filter(PeopleTileViewHelper$$ExternalSyntheticLambda3.INSTANCE$1).findFirst().orElse(null);
            if (notificationEntry2 != null && notificationEntry2.isDismissable()) {
                return notificationEntry2;
            }
        }
        return null;
    }
}
