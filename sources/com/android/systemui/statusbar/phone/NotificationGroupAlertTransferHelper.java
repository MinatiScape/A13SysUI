package com.android.systemui.statusbar.phone;

import android.os.SystemClock;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import com.android.systemui.statusbar.notification.row.RowContentBindParams;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class NotificationGroupAlertTransferHelper implements OnHeadsUpChangedListener, StatusBarStateController.StateListener {
    public NotificationEntryManager mEntryManager;
    public final NotificationGroupManagerLegacy mGroupManager;
    public HeadsUpManager mHeadsUpManager;
    public boolean mIsDozing;
    public final RowContentBindStage mRowContentBindStage;
    public final ArrayMap<String, GroupAlertEntry> mGroupAlertEntries = new ArrayMap<>();
    public final ArrayMap<String, PendingAlertInfo> mPendingAlerts = new ArrayMap<>();
    public final AnonymousClass1 mOnGroupChangeListener = new NotificationGroupManagerLegacy.OnGroupChangeListener() { // from class: com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper.1
        @Override // com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy.OnGroupChangeListener
        public final void onGroupAlertOverrideChanged(NotificationGroupManagerLegacy.NotificationGroup notificationGroup, NotificationEntry notificationEntry) {
            NotificationGroupAlertTransferHelper.m99$$Nest$monGroupChanged(NotificationGroupAlertTransferHelper.this, notificationGroup, notificationEntry);
        }

        @Override // com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy.OnGroupChangeListener
        public final void onGroupCreated(NotificationGroupManagerLegacy.NotificationGroup notificationGroup, String str) {
            NotificationGroupAlertTransferHelper.this.mGroupAlertEntries.put(str, new GroupAlertEntry(notificationGroup));
        }

        @Override // com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy.OnGroupChangeListener
        public final void onGroupRemoved(String str) {
            NotificationGroupAlertTransferHelper.this.mGroupAlertEntries.remove(str);
        }

        @Override // com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy.OnGroupChangeListener
        public final void onGroupSuppressionChanged(NotificationGroupManagerLegacy.NotificationGroup notificationGroup, boolean z) {
            NotificationGroupAlertTransferHelper.m99$$Nest$monGroupChanged(NotificationGroupAlertTransferHelper.this, notificationGroup, notificationGroup.alertOverride);
        }
    };
    public final AnonymousClass2 mNotificationEntryListener = new NotificationEntryListener() { // from class: com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper.2
        @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
        public final void onEntryRemoved(NotificationEntry notificationEntry, boolean z) {
            NotificationGroupAlertTransferHelper.this.mPendingAlerts.remove(notificationEntry.mKey);
        }

        @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
        public final void onPendingEntryAdded(NotificationEntry notificationEntry) {
            GroupAlertEntry groupAlertEntry = NotificationGroupAlertTransferHelper.this.mGroupAlertEntries.get(NotificationGroupAlertTransferHelper.this.mGroupManager.getGroupKey(notificationEntry.mSbn));
            if (groupAlertEntry != null && groupAlertEntry.mGroup.alertOverride == null) {
                NotificationGroupAlertTransferHelper.this.checkShouldTransferBack(groupAlertEntry);
            }
        }
    };

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public final void onStateChanged(int i) {
    }

    public final boolean releaseChildAlerts(ArrayList arrayList) {
        boolean z = false;
        for (int i = 0; i < arrayList.size(); i++) {
            NotificationEntry notificationEntry = (NotificationEntry) arrayList.get(i);
            if (onlySummaryAlerts(notificationEntry) && this.mHeadsUpManager.isAlerting(notificationEntry.mKey)) {
                this.mHeadsUpManager.removeNotification(notificationEntry.mKey, true);
                z = true;
            }
            if (this.mPendingAlerts.containsKey(notificationEntry.mKey)) {
                this.mPendingAlerts.get(notificationEntry.mKey).mAbortOnInflation = true;
                z = true;
            }
        }
        return z;
    }

    /* loaded from: classes.dex */
    public static class GroupAlertEntry {
        public boolean mAlertSummaryOnNextAddition;
        public final NotificationGroupManagerLegacy.NotificationGroup mGroup;
        public long mLastAlertTransferTime;

        public GroupAlertEntry(NotificationGroupManagerLegacy.NotificationGroup notificationGroup) {
            this.mGroup = notificationGroup;
        }
    }

    /* loaded from: classes.dex */
    public class PendingAlertInfo {
        public boolean mAbortOnInflation;
        public final NotificationEntry mEntry;
        public final StatusBarNotification mOriginalNotification;

        public PendingAlertInfo(NotificationEntry notificationEntry) {
            Objects.requireNonNull(notificationEntry);
            this.mOriginalNotification = notificationEntry.mSbn;
            this.mEntry = notificationEntry;
        }
    }

    public final void alertNotificationWhenPossible(final NotificationEntry notificationEntry) {
        Objects.requireNonNull(this.mHeadsUpManager);
        RowContentBindParams stageParams = this.mRowContentBindStage.getStageParams(notificationEntry);
        if ((stageParams.mContentViews & 4) == 0) {
            ArrayMap<String, PendingAlertInfo> arrayMap = this.mPendingAlerts;
            Objects.requireNonNull(notificationEntry);
            arrayMap.put(notificationEntry.mKey, new PendingAlertInfo(notificationEntry));
            stageParams.requireContentViews(4);
            this.mRowContentBindStage.requestRebind(notificationEntry, new NotifBindPipeline.BindCallback() { // from class: com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper$$ExternalSyntheticLambda0
                @Override // com.android.systemui.statusbar.notification.row.NotifBindPipeline.BindCallback
                public final void onBindFinished(NotificationEntry notificationEntry2) {
                    NotificationGroupAlertTransferHelper notificationGroupAlertTransferHelper = NotificationGroupAlertTransferHelper.this;
                    NotificationEntry notificationEntry3 = notificationEntry;
                    Objects.requireNonNull(notificationGroupAlertTransferHelper);
                    ArrayMap<String, NotificationGroupAlertTransferHelper.PendingAlertInfo> arrayMap2 = notificationGroupAlertTransferHelper.mPendingAlerts;
                    Objects.requireNonNull(notificationEntry3);
                    NotificationGroupAlertTransferHelper.PendingAlertInfo remove = arrayMap2.remove(notificationEntry3.mKey);
                    if (remove != null) {
                        boolean z = false;
                        if (!remove.mAbortOnInflation) {
                            NotificationEntry notificationEntry4 = remove.mEntry;
                            Objects.requireNonNull(notificationEntry4);
                            if (notificationEntry4.mSbn.getGroupKey().equals(remove.mOriginalNotification.getGroupKey())) {
                                NotificationEntry notificationEntry5 = remove.mEntry;
                                Objects.requireNonNull(notificationEntry5);
                                if (notificationEntry5.mSbn.getNotification().isGroupSummary() == remove.mOriginalNotification.getNotification().isGroupSummary()) {
                                    z = true;
                                }
                            }
                        }
                        if (z) {
                            notificationGroupAlertTransferHelper.alertNotificationWhenPossible(notificationEntry3);
                            return;
                        }
                        notificationGroupAlertTransferHelper.mRowContentBindStage.getStageParams(notificationEntry3).markContentViewsFreeable(4);
                        notificationGroupAlertTransferHelper.mRowContentBindStage.requestRebind(notificationEntry3, null);
                    }
                }
            });
            return;
        }
        HeadsUpManager headsUpManager = this.mHeadsUpManager;
        Objects.requireNonNull(notificationEntry);
        if (headsUpManager.isAlerting(notificationEntry.mKey)) {
            this.mHeadsUpManager.updateNotification(notificationEntry.mKey, true);
        } else {
            this.mHeadsUpManager.showNotification(notificationEntry);
        }
    }

    public final void checkForForwardAlertTransfer(NotificationEntry notificationEntry, NotificationEntry notificationEntry2) {
        NotificationGroupManagerLegacy.NotificationGroup notificationGroup;
        boolean z;
        NotificationGroupManagerLegacy.NotificationGroup notificationGroup2;
        ArrayList<NotificationEntry> logicalChildren;
        NotificationGroupManagerLegacy notificationGroupManagerLegacy = this.mGroupManager;
        Objects.requireNonNull(notificationEntry);
        StatusBarNotification statusBarNotification = notificationEntry.mSbn;
        Objects.requireNonNull(notificationGroupManagerLegacy);
        if (statusBarNotification.getNotification().isGroupSummary()) {
            notificationGroup = notificationGroupManagerLegacy.mGroupMap.get(notificationGroupManagerLegacy.getGroupKey(statusBarNotification));
        } else {
            notificationGroup = null;
        }
        if (notificationGroup != null && notificationGroup.alertOverride != null) {
            GroupAlertEntry groupAlertEntry = this.mGroupAlertEntries.get(this.mGroupManager.getGroupKey(notificationEntry.mSbn));
            NotificationGroupManagerLegacy notificationGroupManagerLegacy2 = this.mGroupManager;
            StatusBarNotification statusBarNotification2 = notificationEntry.mSbn;
            Objects.requireNonNull(notificationGroupManagerLegacy2);
            if (statusBarNotification2.getNotification().isGroupSummary()) {
                notificationGroup2 = notificationGroupManagerLegacy2.mGroupMap.get(notificationGroupManagerLegacy2.getGroupKey(statusBarNotification2));
            } else {
                notificationGroup2 = null;
            }
            if (notificationGroup2 != null && notificationGroup2.alertOverride != null && groupAlertEntry != null) {
                if (this.mHeadsUpManager.isAlerting(notificationEntry.mKey)) {
                    tryTransferAlertState(notificationEntry, notificationEntry, notificationGroup2.alertOverride, groupAlertEntry);
                } else if (canStillTransferBack(groupAlertEntry) && (logicalChildren = this.mGroupManager.getLogicalChildren(notificationEntry.mSbn)) != null) {
                    logicalChildren.remove(notificationGroup2.alertOverride);
                    if (releaseChildAlerts(logicalChildren)) {
                        tryTransferAlertState(notificationEntry, null, notificationGroup2.alertOverride, groupAlertEntry);
                    }
                }
            }
        } else if (this.mGroupManager.isSummaryOfSuppressedGroup(notificationEntry.mSbn)) {
            GroupAlertEntry groupAlertEntry2 = this.mGroupAlertEntries.get(this.mGroupManager.getGroupKey(notificationEntry.mSbn));
            if (this.mGroupManager.isSummaryOfSuppressedGroup(notificationEntry.mSbn) && groupAlertEntry2 != null) {
                boolean isAlerting = this.mHeadsUpManager.isAlerting(notificationEntry.mKey);
                boolean z2 = true;
                if (notificationEntry2 == null || !this.mHeadsUpManager.isAlerting(notificationEntry2.mKey)) {
                    z = false;
                } else {
                    z = true;
                }
                if (isAlerting || z) {
                    NotificationGroupManagerLegacy.NotificationGroup notificationGroup3 = groupAlertEntry2.mGroup;
                    NotificationEntryManager notificationEntryManager = this.mEntryManager;
                    if (notificationEntryManager != null) {
                        notificationEntryManager.mNotifPipelineFlags.checkLegacyPipelineEnabled();
                        for (NotificationEntry notificationEntry3 : notificationEntryManager.mPendingNotifications.values()) {
                            if (isPendingNotificationInGroup(notificationEntry3, notificationGroup3)) {
                                break;
                            }
                        }
                    }
                    z2 = false;
                    if (!z2) {
                        NotificationEntry next = this.mGroupManager.getLogicalChildren(notificationEntry.mSbn).iterator().next();
                        if (isAlerting) {
                            tryTransferAlertState(notificationEntry, notificationEntry, next, groupAlertEntry2);
                        } else if (canStillTransferBack(groupAlertEntry2)) {
                            tryTransferAlertState(notificationEntry, notificationEntry2, next, groupAlertEntry2);
                        }
                    }
                }
            }
        }
    }

    public final boolean isPendingNotificationInGroup(NotificationEntry notificationEntry, NotificationGroupManagerLegacy.NotificationGroup notificationGroup) {
        NotificationGroupManagerLegacy notificationGroupManagerLegacy = this.mGroupManager;
        NotificationEntry notificationEntry2 = notificationGroup.summary;
        Objects.requireNonNull(notificationEntry2);
        String groupKey = notificationGroupManagerLegacy.getGroupKey(notificationEntry2.mSbn);
        NotificationGroupManagerLegacy notificationGroupManagerLegacy2 = this.mGroupManager;
        Objects.requireNonNull(notificationEntry);
        if (!notificationGroupManagerLegacy2.isGroupChild(notificationEntry.mSbn) || !Objects.equals(this.mGroupManager.getGroupKey(notificationEntry.mSbn), groupKey) || notificationGroup.children.containsKey(notificationEntry.mKey)) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public final void onDozingChanged(boolean z) {
        if (this.mIsDozing != z) {
            for (GroupAlertEntry groupAlertEntry : this.mGroupAlertEntries.values()) {
                groupAlertEntry.mLastAlertTransferTime = 0L;
                groupAlertEntry.mAlertSummaryOnNextAddition = false;
            }
        }
        this.mIsDozing = z;
    }

    @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
    public final void onHeadsUpStateChanged(NotificationEntry notificationEntry, boolean z) {
        if (z && notificationEntry.mSbn.getNotification().isGroupSummary()) {
            checkForForwardAlertTransfer(notificationEntry, null);
        }
    }

    public final void tryTransferAlertState(NotificationEntry notificationEntry, NotificationEntry notificationEntry2, NotificationEntry notificationEntry3, GroupAlertEntry groupAlertEntry) {
        if (notificationEntry3 != null) {
            ExpandableNotificationRow expandableNotificationRow = notificationEntry3.row;
            Objects.requireNonNull(expandableNotificationRow);
            if (!expandableNotificationRow.mKeepInParent && !notificationEntry3.isRowRemoved() && !notificationEntry3.isRowDismissed()) {
                if (!this.mHeadsUpManager.isAlerting(notificationEntry3.mKey) && onlySummaryAlerts(notificationEntry)) {
                    groupAlertEntry.mLastAlertTransferTime = SystemClock.elapsedRealtime();
                }
                if (notificationEntry2 != null) {
                    this.mHeadsUpManager.removeNotification(notificationEntry2.mKey, true);
                }
                alertNotificationWhenPossible(notificationEntry3);
            }
        }
    }

    /* renamed from: -$$Nest$monGroupChanged  reason: not valid java name */
    public static void m99$$Nest$monGroupChanged(NotificationGroupAlertTransferHelper notificationGroupAlertTransferHelper, NotificationGroupManagerLegacy.NotificationGroup notificationGroup, NotificationEntry notificationEntry) {
        Objects.requireNonNull(notificationGroupAlertTransferHelper);
        NotificationEntry notificationEntry2 = notificationGroup.summary;
        if (notificationEntry2 != null) {
            if (notificationGroup.suppressed || notificationGroup.alertOverride != null) {
                notificationGroupAlertTransferHelper.checkForForwardAlertTransfer(notificationEntry2, notificationEntry);
                return;
            }
            GroupAlertEntry groupAlertEntry = notificationGroupAlertTransferHelper.mGroupAlertEntries.get(notificationGroupAlertTransferHelper.mGroupManager.getGroupKey(notificationEntry2.mSbn));
            if (groupAlertEntry.mAlertSummaryOnNextAddition) {
                HeadsUpManager headsUpManager = notificationGroupAlertTransferHelper.mHeadsUpManager;
                NotificationEntry notificationEntry3 = notificationGroup.summary;
                Objects.requireNonNull(notificationEntry3);
                if (!headsUpManager.isAlerting(notificationEntry3.mKey)) {
                    notificationGroupAlertTransferHelper.alertNotificationWhenPossible(notificationGroup.summary);
                }
                groupAlertEntry.mAlertSummaryOnNextAddition = false;
                return;
            }
            notificationGroupAlertTransferHelper.checkShouldTransferBack(groupAlertEntry);
        }
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper$1] */
    /* JADX WARN: Type inference failed for: r0v3, types: [com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper$2] */
    public NotificationGroupAlertTransferHelper(RowContentBindStage rowContentBindStage, StatusBarStateController statusBarStateController, NotificationGroupManagerLegacy notificationGroupManagerLegacy) {
        this.mRowContentBindStage = rowContentBindStage;
        this.mGroupManager = notificationGroupManagerLegacy;
        statusBarStateController.addCallback(this);
    }

    public static boolean canStillTransferBack(GroupAlertEntry groupAlertEntry) {
        if (SystemClock.elapsedRealtime() - groupAlertEntry.mLastAlertTransferTime < 300) {
            return true;
        }
        return false;
    }

    public static boolean onlySummaryAlerts(NotificationEntry notificationEntry) {
        Objects.requireNonNull(notificationEntry);
        if (notificationEntry.mSbn.getNotification().getGroupAlertBehavior() == 1) {
            return true;
        }
        return false;
    }

    public final void checkShouldTransferBack(GroupAlertEntry groupAlertEntry) {
        int i;
        if (canStillTransferBack(groupAlertEntry)) {
            NotificationEntry notificationEntry = groupAlertEntry.mGroup.summary;
            if (onlySummaryAlerts(notificationEntry)) {
                ArrayList<NotificationEntry> logicalChildren = this.mGroupManager.getLogicalChildren(notificationEntry.mSbn);
                int size = logicalChildren.size();
                NotificationGroupManagerLegacy.NotificationGroup notificationGroup = groupAlertEntry.mGroup;
                NotificationEntryManager notificationEntryManager = this.mEntryManager;
                boolean z = false;
                if (notificationEntryManager == null) {
                    i = 0;
                } else {
                    notificationEntryManager.mNotifPipelineFlags.checkLegacyPipelineEnabled();
                    i = 0;
                    for (NotificationEntry notificationEntry2 : notificationEntryManager.mPendingNotifications.values()) {
                        if (isPendingNotificationInGroup(notificationEntry2, notificationGroup) && onlySummaryAlerts(notificationEntry2)) {
                            i++;
                        }
                    }
                }
                if (i + size > 1 && releaseChildAlerts(logicalChildren) && !this.mHeadsUpManager.isAlerting(notificationEntry.mKey)) {
                    if (size > 1) {
                        z = true;
                    }
                    if (z) {
                        alertNotificationWhenPossible(notificationEntry);
                    } else {
                        groupAlertEntry.mAlertSummaryOnNextAddition = true;
                    }
                    groupAlertEntry.mLastAlertTransferTime = 0L;
                }
            }
        }
    }
}
