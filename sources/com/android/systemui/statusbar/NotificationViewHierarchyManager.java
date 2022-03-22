package com.android.systemui.statusbar;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.os.Trace;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.ScreenDecorations$$ExternalSyntheticLambda2;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.DynamicChildBindController;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.legacy.LowPriorityInflationHelper;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.render.NotifStackController;
import com.android.systemui.statusbar.notification.collection.render.NotifStats;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import com.android.systemui.statusbar.notification.row.RowContentBindParams;
import com.android.systemui.statusbar.notification.stack.ForegroundServiceSectionController;
import com.android.systemui.statusbar.notification.stack.NotificationChildrenContainer;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.StatusBarNotificationPresenter;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.Assert;
import com.android.wm.shell.bubbles.Bubbles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Stack;
/* loaded from: classes.dex */
public final class NotificationViewHierarchyManager implements DynamicPrivacyController.Listener {
    public final boolean mAlwaysExpandNonGroupedNotification;
    public AssistantFeedbackController mAssistantFeedbackController;
    public final Optional<Bubbles> mBubblesOptional;
    public final DynamicChildBindController mDynamicChildBindController;
    public final DynamicPrivacyController mDynamicPrivacyController;
    public final NotificationEntryManager mEntryManager;
    public final FeatureFlags mFeatureFlags;
    public final ForegroundServiceSectionController mFgsSectionController;
    public final NotificationGroupManagerLegacy mGroupManager;
    public final Handler mHandler;
    public boolean mIsHandleDynamicPrivacyChangeScheduled;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public NotificationListContainer mListContainer;
    public final NotificationLockscreenUserManager mLockscreenUserManager;
    public final LowPriorityInflationHelper mLowPriorityInflationHelper;
    public final NotifPipelineFlags mNotifPipelineFlags;
    public boolean mPerformingUpdate;
    public NotificationPresenter mPresenter;
    public NotifStackController mStackController;
    public final SysuiStatusBarStateController mStatusBarStateController;
    public final HashMap<NotificationEntry, List<NotificationEntry>> mTmpChildOrderMap = new HashMap<>();
    public final VisualStabilityManager mVisualStabilityManager;

    public NotificationViewHierarchyManager(Context context, Handler handler, FeatureFlags featureFlags, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationGroupManagerLegacy notificationGroupManagerLegacy, VisualStabilityManager visualStabilityManager, StatusBarStateController statusBarStateController, NotificationEntryManager notificationEntryManager, Optional optional, DynamicPrivacyController dynamicPrivacyController, ForegroundServiceSectionController foregroundServiceSectionController, DynamicChildBindController dynamicChildBindController, LowPriorityInflationHelper lowPriorityInflationHelper, AssistantFeedbackController assistantFeedbackController, NotifPipelineFlags notifPipelineFlags, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardStateController keyguardStateController) {
        this.mHandler = handler;
        this.mFeatureFlags = featureFlags;
        this.mLockscreenUserManager = notificationLockscreenUserManager;
        this.mGroupManager = notificationGroupManagerLegacy;
        this.mVisualStabilityManager = visualStabilityManager;
        this.mStatusBarStateController = (SysuiStatusBarStateController) statusBarStateController;
        this.mEntryManager = notificationEntryManager;
        this.mFgsSectionController = foregroundServiceSectionController;
        this.mNotifPipelineFlags = notifPipelineFlags;
        this.mAlwaysExpandNonGroupedNotification = context.getResources().getBoolean(2131034117);
        this.mBubblesOptional = optional;
        this.mDynamicPrivacyController = dynamicPrivacyController;
        this.mDynamicChildBindController = dynamicChildBindController;
        this.mLowPriorityInflationHelper = lowPriorityInflationHelper;
        this.mAssistantFeedbackController = assistantFeedbackController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mKeyguardStateController = keyguardStateController;
    }

    @Override // com.android.systemui.statusbar.notification.DynamicPrivacyController.Listener
    public final void onDynamicPrivacyChanged() {
        NotifPipelineFlags notifPipelineFlags = this.mNotifPipelineFlags;
        Objects.requireNonNull(notifPipelineFlags);
        if (!notifPipelineFlags.isNewPipelineEnabled()) {
            if (this.mPerformingUpdate) {
                Log.w("NotificationViewHierarchyManager", "onDynamicPrivacyChanged made a re-entrant call");
            }
            if (!this.mIsHandleDynamicPrivacyChangeScheduled) {
                this.mIsHandleDynamicPrivacyChangeScheduled = true;
                this.mHandler.post(new ScreenDecorations$$ExternalSyntheticLambda2(this, 5));
                return;
            }
            return;
        }
        throw new IllegalStateException("Old pipeline code running w/ new pipeline enabled".toString());
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0045 A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean shouldSuppressActiveNotification(com.android.systemui.statusbar.notification.collection.NotificationEntry r6) {
        /*
            r5 = this;
            java.util.Optional<com.android.wm.shell.bubbles.Bubbles> r0 = r5.mBubblesOptional
            boolean r0 = r0.isPresent()
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L_0x0025
            java.util.Optional<com.android.wm.shell.bubbles.Bubbles> r0 = r5.mBubblesOptional
            java.lang.Object r0 = r0.get()
            com.android.wm.shell.bubbles.Bubbles r0 = (com.android.wm.shell.bubbles.Bubbles) r0
            java.util.Objects.requireNonNull(r6)
            java.lang.String r3 = r6.mKey
            android.service.notification.StatusBarNotification r4 = r6.mSbn
            java.lang.String r4 = r4.getGroupKey()
            boolean r0 = r0.isBubbleNotificationSuppressedFromShade(r3, r4)
            if (r0 == 0) goto L_0x0025
            r0 = r1
            goto L_0x0026
        L_0x0025:
            r0 = r2
        L_0x0026:
            boolean r3 = r6.isRowDismissed()
            if (r3 != 0) goto L_0x0046
            boolean r3 = r6.isRowRemoved()
            if (r3 != 0) goto L_0x0046
            if (r0 != 0) goto L_0x0046
            com.android.systemui.statusbar.notification.stack.ForegroundServiceSectionController r5 = r5.mFgsSectionController
            java.util.Objects.requireNonNull(r5)
            com.android.systemui.util.Assert.isMainThread()
            java.util.LinkedHashSet r5 = r5.entries
            boolean r5 = r5.contains(r6)
            if (r5 == 0) goto L_0x0045
            goto L_0x0046
        L_0x0045:
            return r2
        L_0x0046:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.NotificationViewHierarchyManager.shouldSuppressActiveNotification(com.android.systemui.statusbar.notification.collection.NotificationEntry):boolean");
    }

    public final void updateRowStatesInternal() {
        boolean z;
        boolean z2;
        boolean z3;
        NotificationEntry logicalGroupSummary;
        boolean z4;
        Trace.beginSection("NotificationViewHierarchyManager.updateRowStates");
        int containerChildCount = this.mListContainer.getContainerChildCount();
        if (this.mStatusBarStateController.getCurrentOrUpcomingState() == 1) {
            z = true;
        } else {
            z = false;
        }
        Stack stack = new Stack();
        for (int i = containerChildCount - 1; i >= 0; i--) {
            View containerChildAt = this.mListContainer.getContainerChildAt(i);
            if (containerChildAt instanceof ExpandableNotificationRow) {
                stack.push((ExpandableNotificationRow) containerChildAt);
            }
        }
        int i2 = 0;
        while (!stack.isEmpty()) {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) stack.pop();
            Objects.requireNonNull(expandableNotificationRow);
            NotificationEntry notificationEntry = expandableNotificationRow.mEntry;
            boolean isChildInGroup = this.mGroupManager.isChildInGroup(notificationEntry);
            if (!z) {
                if (this.mAlwaysExpandNonGroupedNotification || (i2 == 0 && !isChildInGroup && !expandableNotificationRow.mIsLowPriority)) {
                    z4 = true;
                } else {
                    z4 = false;
                }
                expandableNotificationRow.setSystemExpanded(z4);
            }
            int userId = notificationEntry.mSbn.getUserId();
            if (!this.mGroupManager.isSummaryOfSuppressedGroup(notificationEntry.mSbn) || notificationEntry.isRowRemoved()) {
                z2 = false;
            } else {
                z2 = true;
            }
            boolean shouldShowOnKeyguard = this.mLockscreenUserManager.shouldShowOnKeyguard(notificationEntry);
            if (!shouldShowOnKeyguard && this.mGroupManager.isChildInGroup(notificationEntry) && (logicalGroupSummary = this.mGroupManager.getLogicalGroupSummary(notificationEntry)) != null && this.mLockscreenUserManager.shouldShowOnKeyguard(logicalGroupSummary)) {
                shouldShowOnKeyguard = true;
            }
            if (z2 || this.mLockscreenUserManager.shouldHideNotifications(userId) || (z && !shouldShowOnKeyguard)) {
                notificationEntry.row.setVisibility(8);
            } else {
                if (notificationEntry.row.getVisibility() == 8) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                if (z3) {
                    notificationEntry.row.setVisibility(0);
                }
                if (!isChildInGroup) {
                    ExpandableNotificationRow expandableNotificationRow2 = notificationEntry.row;
                    Objects.requireNonNull(expandableNotificationRow2);
                    if (!expandableNotificationRow2.mRemoved) {
                        if (z3) {
                            this.mListContainer.generateAddAnimation(notificationEntry.row, !shouldShowOnKeyguard);
                        }
                        i2++;
                    }
                }
            }
            if (expandableNotificationRow.mIsSummaryWithChildren) {
                ArrayList attachedChildren = expandableNotificationRow.getAttachedChildren();
                for (int size = attachedChildren.size() - 1; size >= 0; size--) {
                    stack.push((ExpandableNotificationRow) attachedChildren.get(size));
                }
            }
            AssistantFeedbackController assistantFeedbackController = this.mAssistantFeedbackController;
            Objects.requireNonNull(assistantFeedbackController);
            expandableNotificationRow.setFeedbackIcon(assistantFeedbackController.mIcons.get(assistantFeedbackController.getFeedbackStatus(notificationEntry)));
            expandableNotificationRow.setLastAudiblyAlertedMs(notificationEntry.mRanking.getLastAudiblyAlertedMillis());
        }
        Trace.beginSection("NotificationPresenter#onUpdateRowStates");
        StatusBarNotificationPresenter statusBarNotificationPresenter = (StatusBarNotificationPresenter) this.mPresenter;
        Objects.requireNonNull(statusBarNotificationPresenter);
        NotificationPanelViewController notificationPanelViewController = statusBarNotificationPresenter.mNotificationPanel;
        Objects.requireNonNull(notificationPanelViewController);
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = notificationPanelViewController.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController);
        notificationStackScrollLayoutController.mView.onUpdateRowStates();
        Trace.endSection();
        Trace.endSection();
    }

    public final void updateNotificationViews() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        final boolean z7;
        boolean z8;
        boolean z9;
        boolean z10;
        Assert.isMainThread();
        if (this.mNotifPipelineFlags.checkLegacyPipelineEnabled()) {
            Trace.beginSection("NotificationViewHierarchyManager.updateNotificationViews");
            if (this.mPerformingUpdate) {
                Log.wtf("NotificationViewHierarchyManager", "Re-entrant code during update", new Exception());
            }
            this.mPerformingUpdate = true;
            if (!this.mDynamicPrivacyController.isDynamicallyUnlocked() || ((this.mStatusBarStateController.getState() == 1 && this.mKeyguardUpdateMonitor.getUserUnlockedWithBiometricAndIsBypassing(KeyguardUpdateMonitor.getCurrentUser())) || this.mKeyguardStateController.isKeyguardGoingAway())) {
                z = false;
            } else {
                z = true;
            }
            NotificationEntryManager notificationEntryManager = this.mEntryManager;
            Objects.requireNonNull(notificationEntryManager);
            notificationEntryManager.mNotifPipelineFlags.checkLegacyPipelineEnabled();
            List<NotificationEntry> list = notificationEntryManager.mReadOnlyNotifications;
            ArrayList arrayList = new ArrayList(list.size());
            int size = list.size();
            for (int i = 0; i < size; i++) {
                NotificationEntry notificationEntry = list.get(i);
                if (!shouldSuppressActiveNotification(notificationEntry)) {
                    int userId = notificationEntry.mSbn.getUserId();
                    int currentUserId = this.mLockscreenUserManager.getCurrentUserId();
                    boolean isLockscreenPublicMode = this.mLockscreenUserManager.isLockscreenPublicMode(currentUserId);
                    if (isLockscreenPublicMode || this.mLockscreenUserManager.isLockscreenPublicMode(userId)) {
                        z4 = true;
                    } else {
                        z4 = false;
                    }
                    if (z4 && z && (userId == currentUserId || userId == -1 || !this.mLockscreenUserManager.needsSeparateWorkChallenge(userId))) {
                        z4 = false;
                    }
                    boolean needsRedaction = this.mLockscreenUserManager.needsRedaction(notificationEntry);
                    if (!z4 || !needsRedaction) {
                        z5 = false;
                    } else {
                        z5 = true;
                    }
                    if (!isLockscreenPublicMode || this.mLockscreenUserManager.userAllowsPrivateNotificationsInPublic(currentUserId)) {
                        z6 = false;
                    } else {
                        z6 = true;
                    }
                    notificationEntry.setSensitive(z5, z6);
                    notificationEntry.row.setNeedsRedaction(needsRedaction);
                    LowPriorityInflationHelper lowPriorityInflationHelper = this.mLowPriorityInflationHelper;
                    final ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
                    Objects.requireNonNull(lowPriorityInflationHelper);
                    lowPriorityInflationHelper.mNotifPipelineFlags.checkLegacyPipelineEnabled();
                    RowContentBindParams stageParams = lowPriorityInflationHelper.mRowContentBindStage.getStageParams(notificationEntry);
                    lowPriorityInflationHelper.mNotifPipelineFlags.checkLegacyPipelineEnabled();
                    if (!notificationEntry.isAmbient() || lowPriorityInflationHelper.mGroupManager.isChildInGroup(notificationEntry)) {
                        z7 = false;
                    } else {
                        z7 = true;
                    }
                    Objects.requireNonNull(expandableNotificationRow);
                    if (!expandableNotificationRow.mRemoved && expandableNotificationRow.mIsLowPriority != z7) {
                        if (stageParams.mUseLowPriority != z7) {
                            stageParams.mDirtyContentViews |= 3;
                        }
                        stageParams.mUseLowPriority = z7;
                        lowPriorityInflationHelper.mRowContentBindStage.requestRebind(notificationEntry, new NotifBindPipeline.BindCallback() { // from class: com.android.systemui.statusbar.notification.collection.legacy.LowPriorityInflationHelper$$ExternalSyntheticLambda0
                            @Override // com.android.systemui.statusbar.notification.row.NotifBindPipeline.BindCallback
                            public final void onBindFinished(NotificationEntry notificationEntry2) {
                                ExpandableNotificationRow expandableNotificationRow2 = ExpandableNotificationRow.this;
                                boolean z11 = z7;
                                Objects.requireNonNull(expandableNotificationRow2);
                                expandableNotificationRow2.mIsLowPriority = z11;
                                Objects.requireNonNull(expandableNotificationRow2.mPrivateLayout);
                                NotificationChildrenContainer notificationChildrenContainer = expandableNotificationRow2.mChildrenContainer;
                                if (notificationChildrenContainer != null) {
                                    notificationChildrenContainer.mIsLowPriority = z11;
                                    if (notificationChildrenContainer.mContainingNotification != null) {
                                        notificationChildrenContainer.recreateLowPriorityHeader(null);
                                        notificationChildrenContainer.updateHeaderVisibility(false);
                                    }
                                    boolean z12 = notificationChildrenContainer.mUserLocked;
                                    if (z12) {
                                        notificationChildrenContainer.setUserLocked(z12);
                                    }
                                }
                            }
                        });
                    }
                    boolean isChildInGroup = this.mGroupManager.isChildInGroup(notificationEntry);
                    VisualStabilityManager visualStabilityManager = this.mVisualStabilityManager;
                    Objects.requireNonNull(visualStabilityManager);
                    if (visualStabilityManager.mGroupChangedAllowed || !notificationEntry.hasFinishedInitialization()) {
                        z8 = true;
                    } else {
                        z8 = false;
                    }
                    NotificationEntry groupSummary = this.mGroupManager.getGroupSummary(notificationEntry);
                    if (!z8) {
                        ExpandableNotificationRow expandableNotificationRow2 = notificationEntry.row;
                        if (expandableNotificationRow2 == null || !expandableNotificationRow2.isChildInGroup()) {
                            z9 = false;
                        } else {
                            z9 = true;
                        }
                        if (isChildInGroup && !z9) {
                            VisualStabilityManager visualStabilityManager2 = this.mVisualStabilityManager;
                            NotificationEntryManager notificationEntryManager2 = this.mEntryManager;
                            Objects.requireNonNull(visualStabilityManager2);
                            if (!visualStabilityManager2.mGroupChangesAllowedCallbacks.contains(notificationEntryManager2)) {
                                visualStabilityManager2.mGroupChangesAllowedCallbacks.add(notificationEntryManager2);
                            }
                        } else if (!isChildInGroup && z9) {
                            NotificationGroupManagerLegacy notificationGroupManagerLegacy = this.mGroupManager;
                            StatusBarNotification statusBarNotification = notificationEntry.mSbn;
                            Objects.requireNonNull(notificationGroupManagerLegacy);
                            NotificationGroupManagerLegacy.NotificationGroup notificationGroup = notificationGroupManagerLegacy.mGroupMap.get(statusBarNotification.getGroupKey());
                            if (notificationGroup == null) {
                                z10 = false;
                            } else {
                                z10 = notificationGroup.expanded;
                            }
                            if (z10) {
                                ExpandableNotificationRow expandableNotificationRow3 = notificationEntry.row;
                                Objects.requireNonNull(expandableNotificationRow3);
                                ExpandableNotificationRow expandableNotificationRow4 = expandableNotificationRow3.mNotificationParent;
                                Objects.requireNonNull(expandableNotificationRow4);
                                NotificationEntry notificationEntry2 = expandableNotificationRow4.mEntry;
                                VisualStabilityManager visualStabilityManager3 = this.mVisualStabilityManager;
                                NotificationEntryManager notificationEntryManager3 = this.mEntryManager;
                                Objects.requireNonNull(visualStabilityManager3);
                                if (!visualStabilityManager3.mGroupChangesAllowedCallbacks.contains(notificationEntryManager3)) {
                                    visualStabilityManager3.mGroupChangesAllowedCallbacks.add(notificationEntryManager3);
                                }
                                groupSummary = notificationEntry2;
                            }
                        }
                        isChildInGroup = z9;
                    }
                    if (isChildInGroup) {
                        List<NotificationEntry> list2 = this.mTmpChildOrderMap.get(groupSummary);
                        if (list2 == null) {
                            list2 = new ArrayList<>();
                            this.mTmpChildOrderMap.put(groupSummary, list2);
                        }
                        list2.add(notificationEntry);
                    } else {
                        if (!this.mTmpChildOrderMap.containsKey(notificationEntry)) {
                            this.mTmpChildOrderMap.put(notificationEntry, null);
                        }
                        arrayList.add(notificationEntry.row);
                    }
                }
            }
            ArrayList arrayList2 = new ArrayList();
            for (int i2 = 0; i2 < this.mListContainer.getContainerChildCount(); i2++) {
                View containerChildAt = this.mListContainer.getContainerChildAt(i2);
                if (!arrayList.contains(containerChildAt) && (containerChildAt instanceof ExpandableNotificationRow)) {
                    ExpandableNotificationRow expandableNotificationRow5 = (ExpandableNotificationRow) containerChildAt;
                    Objects.requireNonNull(expandableNotificationRow5);
                    arrayList2.add(expandableNotificationRow5);
                }
            }
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                ExpandableNotificationRow expandableNotificationRow6 = (ExpandableNotificationRow) it.next();
                Objects.requireNonNull(expandableNotificationRow6);
                NotificationEntry notificationEntry3 = expandableNotificationRow6.mEntry;
                NotificationEntryManager notificationEntryManager4 = this.mEntryManager;
                Objects.requireNonNull(notificationEntry3);
                if (notificationEntryManager4.getPendingOrActiveNotif(notificationEntry3.mKey) != null && !shouldSuppressActiveNotification(notificationEntry3)) {
                    this.mListContainer.setChildTransferInProgress(true);
                }
                if (expandableNotificationRow6.mIsSummaryWithChildren) {
                    NotificationChildrenContainer notificationChildrenContainer = expandableNotificationRow6.mChildrenContainer;
                    Objects.requireNonNull(notificationChildrenContainer);
                    ArrayList arrayList3 = new ArrayList(notificationChildrenContainer.mAttachedChildren);
                    for (int i3 = 0; i3 < arrayList3.size(); i3++) {
                        ExpandableNotificationRow expandableNotificationRow7 = (ExpandableNotificationRow) arrayList3.get(i3);
                        Objects.requireNonNull(expandableNotificationRow7);
                        if (!expandableNotificationRow7.mKeepInParent) {
                            expandableNotificationRow6.mChildrenContainer.removeNotification(expandableNotificationRow7);
                            expandableNotificationRow7.setIsChildInGroup(false, null);
                        }
                    }
                    expandableNotificationRow6.onAttachedChildrenCountChanged();
                }
                this.mListContainer.removeContainerView(expandableNotificationRow6);
                this.mListContainer.setChildTransferInProgress(false);
            }
            ArrayList arrayList4 = new ArrayList();
            for (int i4 = 0; i4 < this.mListContainer.getContainerChildCount(); i4++) {
                View containerChildAt2 = this.mListContainer.getContainerChildAt(i4);
                if (containerChildAt2 instanceof ExpandableNotificationRow) {
                    ExpandableNotificationRow expandableNotificationRow8 = (ExpandableNotificationRow) containerChildAt2;
                    ArrayList<ExpandableNotificationRow> attachedChildren = expandableNotificationRow8.getAttachedChildren();
                    List<NotificationEntry> list3 = this.mTmpChildOrderMap.get(expandableNotificationRow8.mEntry);
                    if (attachedChildren != null) {
                        arrayList4.clear();
                        for (ExpandableNotificationRow expandableNotificationRow9 : attachedChildren) {
                            if (list3 != null) {
                                Objects.requireNonNull(expandableNotificationRow9);
                                if (!list3.contains(expandableNotificationRow9.mEntry)) {
                                }
                            }
                            Objects.requireNonNull(expandableNotificationRow9);
                            if (!expandableNotificationRow9.mKeepInParent) {
                                arrayList4.add(expandableNotificationRow9);
                            }
                        }
                        Iterator it2 = arrayList4.iterator();
                        while (it2.hasNext()) {
                            ExpandableNotificationRow expandableNotificationRow10 = (ExpandableNotificationRow) it2.next();
                            expandableNotificationRow8.removeChildNotification(expandableNotificationRow10);
                            NotificationEntryManager notificationEntryManager5 = this.mEntryManager;
                            NotificationEntry notificationEntry4 = expandableNotificationRow10.mEntry;
                            Objects.requireNonNull(notificationEntry4);
                            if (notificationEntryManager5.getActiveNotificationUnfiltered(notificationEntry4.mSbn.getKey()) == null) {
                                this.mListContainer.notifyGroupChildRemoved(expandableNotificationRow10, expandableNotificationRow8.mChildrenContainer);
                            }
                        }
                    }
                }
            }
            int i5 = 0;
            while (i5 < arrayList.size()) {
                View view = (View) arrayList.get(i5);
                if (view.getParent() == null) {
                    VisualStabilityManager visualStabilityManager4 = this.mVisualStabilityManager;
                    Objects.requireNonNull(visualStabilityManager4);
                    visualStabilityManager4.mAddedChildren.add(view);
                    this.mListContainer.addContainerView(view);
                } else if (!this.mListContainer.containsView(view)) {
                    arrayList.remove(view);
                    i5--;
                }
                i5++;
            }
            ArrayList arrayList5 = new ArrayList();
            boolean z11 = false;
            for (int i6 = 0; i6 < this.mListContainer.getContainerChildCount(); i6++) {
                View containerChildAt3 = this.mListContainer.getContainerChildAt(i6);
                if (containerChildAt3 instanceof ExpandableNotificationRow) {
                    ExpandableNotificationRow expandableNotificationRow11 = (ExpandableNotificationRow) containerChildAt3;
                    ArrayList attachedChildren2 = expandableNotificationRow11.getAttachedChildren();
                    List<NotificationEntry> list4 = this.mTmpChildOrderMap.get(expandableNotificationRow11.mEntry);
                    if (list4 != null) {
                        int size2 = list4.size();
                        if (expandableNotificationRow11.mChildrenContainer == null) {
                            expandableNotificationRow11.mChildrenContainerStub.inflate();
                        }
                        NotificationChildrenContainer notificationChildrenContainer2 = expandableNotificationRow11.mChildrenContainer;
                        Objects.requireNonNull(notificationChildrenContainer2);
                        notificationChildrenContainer2.mUntruncatedChildCount = size2;
                        notificationChildrenContainer2.updateGroupOverflow();
                        for (int i7 = 0; i7 < list4.size(); i7++) {
                            NotificationEntry notificationEntry5 = list4.get(i7);
                            Objects.requireNonNull(notificationEntry5);
                            ExpandableNotificationRow expandableNotificationRow12 = notificationEntry5.row;
                            if (attachedChildren2 == null || !attachedChildren2.contains(expandableNotificationRow12)) {
                                if (expandableNotificationRow12.getParent() != null) {
                                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("trying to add a notification child that already has a parent. class:");
                                    m.append(expandableNotificationRow12.getParent().getClass());
                                    m.append("\n child: ");
                                    m.append(expandableNotificationRow12);
                                    Log.wtf("NotificationViewHierarchyManager", m.toString());
                                    ((ViewGroup) expandableNotificationRow12.getParent()).removeView(expandableNotificationRow12);
                                }
                                VisualStabilityManager visualStabilityManager5 = this.mVisualStabilityManager;
                                Objects.requireNonNull(visualStabilityManager5);
                                visualStabilityManager5.mAddedChildren.add(expandableNotificationRow12);
                                expandableNotificationRow11.addChildNotification(expandableNotificationRow12, i7);
                                this.mListContainer.notifyGroupChildAdded(expandableNotificationRow12);
                            }
                            arrayList5.add(expandableNotificationRow12);
                        }
                        VisualStabilityManager visualStabilityManager6 = this.mVisualStabilityManager;
                        NotificationEntryManager notificationEntryManager6 = this.mEntryManager;
                        NotificationChildrenContainer notificationChildrenContainer3 = expandableNotificationRow11.mChildrenContainer;
                        if (notificationChildrenContainer3 != null) {
                            boolean z12 = false;
                            for (int i8 = 0; i8 < notificationChildrenContainer3.mAttachedChildren.size() && i8 < arrayList5.size(); i8++) {
                                ExpandableNotificationRow expandableNotificationRow13 = (ExpandableNotificationRow) notificationChildrenContainer3.mAttachedChildren.get(i8);
                                ExpandableNotificationRow expandableNotificationRow14 = (ExpandableNotificationRow) arrayList5.get(i8);
                                if (expandableNotificationRow13 != expandableNotificationRow14) {
                                    if (visualStabilityManager6.canReorderNotification(expandableNotificationRow14)) {
                                        notificationChildrenContainer3.mAttachedChildren.remove(expandableNotificationRow14);
                                        notificationChildrenContainer3.mAttachedChildren.add(i8, expandableNotificationRow14);
                                        z12 = true;
                                    } else if (!visualStabilityManager6.mReorderingAllowedCallbacks.contains(notificationEntryManager6)) {
                                        visualStabilityManager6.mReorderingAllowedCallbacks.add(notificationEntryManager6);
                                    }
                                }
                            }
                            notificationChildrenContainer3.updateExpansionStates();
                            if (z12) {
                                z3 = true;
                                z11 |= z3;
                                arrayList5.clear();
                            }
                        }
                        z3 = false;
                        z11 |= z3;
                        arrayList5.clear();
                    }
                }
            }
            if (z11) {
                this.mListContainer.generateChildOrderChangedEvent();
            }
            int i9 = 0;
            for (int i10 = 0; i10 < this.mListContainer.getContainerChildCount(); i10++) {
                View containerChildAt4 = this.mListContainer.getContainerChildAt(i10);
                if (containerChildAt4 instanceof ExpandableNotificationRow) {
                    Objects.requireNonNull((ExpandableNotificationRow) containerChildAt4);
                    ExpandableNotificationRow expandableNotificationRow15 = (ExpandableNotificationRow) arrayList.get(i9);
                    if (containerChildAt4 != expandableNotificationRow15) {
                        if (this.mVisualStabilityManager.canReorderNotification(expandableNotificationRow15)) {
                            this.mListContainer.changeViewPosition(expandableNotificationRow15, i10);
                        } else {
                            VisualStabilityManager visualStabilityManager7 = this.mVisualStabilityManager;
                            NotificationEntryManager notificationEntryManager7 = this.mEntryManager;
                            Objects.requireNonNull(visualStabilityManager7);
                            if (!visualStabilityManager7.mReorderingAllowedCallbacks.contains(notificationEntryManager7)) {
                                visualStabilityManager7.mReorderingAllowedCallbacks.add(notificationEntryManager7);
                            }
                        }
                    }
                    i9++;
                }
            }
            DynamicChildBindController dynamicChildBindController = this.mDynamicChildBindController;
            HashMap<NotificationEntry, List<NotificationEntry>> hashMap = this.mTmpChildOrderMap;
            Objects.requireNonNull(dynamicChildBindController);
            for (NotificationEntry notificationEntry6 : hashMap.keySet()) {
                List<NotificationEntry> list5 = hashMap.get(notificationEntry6);
                if (list5 != null) {
                    for (int i11 = 0; i11 < list5.size(); i11++) {
                        NotificationEntry notificationEntry7 = list5.get(i11);
                        if (i11 >= dynamicChildBindController.mChildBindCutoff) {
                            if (DynamicChildBindController.hasContent(notificationEntry7)) {
                                RowContentBindParams stageParams2 = dynamicChildBindController.mStage.getStageParams(notificationEntry7);
                                stageParams2.markContentViewsFreeable(1);
                                stageParams2.markContentViewsFreeable(2);
                                dynamicChildBindController.mStage.requestRebind(notificationEntry7, null);
                            }
                        } else if (!DynamicChildBindController.hasContent(notificationEntry7)) {
                            RowContentBindParams stageParams3 = dynamicChildBindController.mStage.getStageParams(notificationEntry7);
                            stageParams3.requireContentViews(1);
                            stageParams3.requireContentViews(2);
                            dynamicChildBindController.mStage.requestRebind(notificationEntry7, null);
                        }
                    }
                } else if (!DynamicChildBindController.hasContent(notificationEntry6)) {
                    RowContentBindParams stageParams4 = dynamicChildBindController.mStage.getStageParams(notificationEntry6);
                    stageParams4.requireContentViews(1);
                    stageParams4.requireContentViews(2);
                    dynamicChildBindController.mStage.requestRebind(notificationEntry6, null);
                }
            }
            VisualStabilityManager visualStabilityManager8 = this.mVisualStabilityManager;
            Objects.requireNonNull(visualStabilityManager8);
            visualStabilityManager8.mAllowedReorderViews.clear();
            visualStabilityManager8.mAddedChildren.clear();
            visualStabilityManager8.mLowPriorityReorderingViews.clear();
            this.mTmpChildOrderMap.clear();
            updateRowStatesInternal();
            Trace.beginSection("NotificationViewHierarchyManager.updateNotifStats");
            int containerChildCount = this.mListContainer.getContainerChildCount();
            int i12 = 0;
            boolean z13 = false;
            boolean z14 = false;
            boolean z15 = false;
            boolean z16 = false;
            for (int i13 = 0; i13 < containerChildCount; i13++) {
                View containerChildAt5 = this.mListContainer.getContainerChildAt(i13);
                if (!(containerChildAt5 == null || containerChildAt5.getVisibility() == 8 || !(containerChildAt5 instanceof ExpandableNotificationRow))) {
                    ExpandableNotificationRow expandableNotificationRow16 = (ExpandableNotificationRow) containerChildAt5;
                    NotificationEntry notificationEntry8 = expandableNotificationRow16.mEntry;
                    Objects.requireNonNull(notificationEntry8);
                    if (notificationEntry8.mBucket == 6) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    boolean isClearable = expandableNotificationRow16.mEntry.isClearable();
                    i12++;
                    if (z2) {
                        if (isClearable) {
                            z16 = true;
                        } else {
                            z15 = true;
                        }
                    } else if (isClearable) {
                        z14 = true;
                    } else {
                        z13 = true;
                    }
                }
            }
            NotifStackController notifStackController = this.mStackController;
            NotifStats notifStats = new NotifStats(i12, z13, z14, z15, z16);
            NotificationStackScrollLayoutController.NotifStackControllerImpl notifStackControllerImpl = (NotificationStackScrollLayoutController.NotifStackControllerImpl) notifStackController;
            Objects.requireNonNull(notifStackControllerImpl);
            NotificationStackScrollLayoutController notificationStackScrollLayoutController = NotificationStackScrollLayoutController.this;
            notificationStackScrollLayoutController.mNotifStats = notifStats;
            notificationStackScrollLayoutController.updateFooter();
            NotificationStackScrollLayoutController.this.updateShowEmptyShadeView();
            Trace.endSection();
            Objects.requireNonNull(this.mListContainer);
            if (!this.mPerformingUpdate) {
                Log.wtf("NotificationViewHierarchyManager", "Manager state has become desynced", new Exception());
            }
            this.mPerformingUpdate = false;
            Trace.endSection();
        }
    }
}
