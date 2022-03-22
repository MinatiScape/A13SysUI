package com.android.systemui.statusbar.notification.collection.legacy;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.service.notification.StatusBarNotification;
import android.util.ArraySet;
import android.util.Log;
import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.R$array;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager$$ExternalSyntheticLambda3;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController$$ExternalSyntheticLambda1;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import com.android.wm.shell.bubbles.Bubbles;
import dagger.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.Function;
/* loaded from: classes.dex */
public final class NotificationGroupManagerLegacy implements OnHeadsUpChangedListener, StatusBarStateController.StateListener, GroupMembershipManager, GroupExpansionManager, Dumpable {
    public final Optional<Bubbles> mBubblesOptional;
    public final GroupEventDispatcher mEventDispatcher;
    public final HashMap<String, NotificationGroup> mGroupMap;
    public HeadsUpManager mHeadsUpManager;
    public boolean mIsUpdatingUnchangedGroup;
    public final Lazy<PeopleNotificationIdentifier> mPeopleNotificationIdentifier;
    public final ArraySet<GroupExpansionManager.OnGroupExpansionChangeListener> mExpansionChangeListeners = new ArraySet<>();
    public HashMap<String, StatusBarNotification> mIsolatedEntries = new HashMap<>();

    /* loaded from: classes.dex */
    public static class GroupEventDispatcher {
        public final Function<String, NotificationGroup> mGroupMapGetter;
        public final ArraySet<OnGroupChangeListener> mGroupChangeListeners = new ArraySet<>();
        public final HashMap<String, NotificationEntry> mOldAlertOverrideByGroup = new HashMap<>();
        public final HashMap<String, Boolean> mOldSuppressedByGroup = new HashMap<>();
        public int mBufferScopeDepth = 0;
        public boolean mDidGroupsChange = false;

        public final void closeBufferScope() {
            boolean z;
            NotificationEntry value;
            int i = this.mBufferScopeDepth - 1;
            this.mBufferScopeDepth = i;
            if (i > 0) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                for (Map.Entry<String, Boolean> entry : this.mOldSuppressedByGroup.entrySet()) {
                    NotificationGroup apply = this.mGroupMapGetter.apply(entry.getKey());
                    if (apply != null) {
                        if (apply.suppressed != entry.getValue().booleanValue()) {
                            notifySuppressedChanged(apply);
                        }
                    }
                }
                this.mOldSuppressedByGroup.clear();
                for (Map.Entry<String, NotificationEntry> entry2 : this.mOldAlertOverrideByGroup.entrySet()) {
                    NotificationGroup apply2 = this.mGroupMapGetter.apply(entry2.getKey());
                    if (!(apply2 == null || apply2.alertOverride == (value = entry2.getValue()))) {
                        notifyAlertOverrideChanged(apply2, value);
                    }
                }
                this.mOldAlertOverrideByGroup.clear();
                if (this.mDidGroupsChange) {
                    notifyGroupsChanged();
                    this.mDidGroupsChange = false;
                }
            }
        }

        public final void notifyAlertOverrideChanged(NotificationGroup notificationGroup, NotificationEntry notificationEntry) {
            boolean z;
            if (this.mBufferScopeDepth > 0) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                Iterator<OnGroupChangeListener> it = this.mGroupChangeListeners.iterator();
                while (it.hasNext()) {
                    NotificationEntry notificationEntry2 = notificationGroup.alertOverride;
                    it.next().onGroupAlertOverrideChanged(notificationGroup, notificationEntry);
                }
            } else if (!this.mOldAlertOverrideByGroup.containsKey(notificationGroup.groupKey)) {
                this.mOldAlertOverrideByGroup.put(notificationGroup.groupKey, notificationEntry);
            }
        }

        public final void notifyGroupsChanged() {
            boolean z;
            if (this.mBufferScopeDepth > 0) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                this.mDidGroupsChange = true;
                return;
            }
            Iterator<OnGroupChangeListener> it = this.mGroupChangeListeners.iterator();
            while (it.hasNext()) {
                it.next().onGroupsChanged();
            }
        }

        public final void notifySuppressedChanged(NotificationGroup notificationGroup) {
            boolean z;
            if (this.mBufferScopeDepth > 0) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                this.mOldSuppressedByGroup.putIfAbsent(notificationGroup.groupKey, Boolean.valueOf(!notificationGroup.suppressed));
                return;
            }
            Iterator<OnGroupChangeListener> it = this.mGroupChangeListeners.iterator();
            while (it.hasNext()) {
                it.next().onGroupSuppressionChanged(notificationGroup, notificationGroup.suppressed);
            }
        }

        public GroupEventDispatcher(PeopleSpaceWidgetManager$$ExternalSyntheticLambda3 peopleSpaceWidgetManager$$ExternalSyntheticLambda3) {
            this.mGroupMapGetter = peopleSpaceWidgetManager$$ExternalSyntheticLambda3;
        }
    }

    /* loaded from: classes.dex */
    public static class NotificationGroup {
        public NotificationEntry alertOverride;
        public boolean expanded;
        public final String groupKey;
        public NotificationEntry summary;
        public boolean suppressed;
        public final HashMap<String, NotificationEntry> children = new HashMap<>();
        public final TreeSet<PostRecord> postBatchHistory = new TreeSet<>();

        public static void appendEntry(StringBuilder sb, NotificationEntry notificationEntry) {
            Object obj;
            Throwable th;
            sb.append("\n      ");
            if (notificationEntry != null) {
                obj = notificationEntry.mSbn;
            } else {
                obj = "null";
            }
            sb.append(obj);
            if (notificationEntry != null && (th = notificationEntry.mDebugThrowable) != null) {
                sb.append(Log.getStackTraceString(th));
            }
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("    groupKey: ");
            m.append(this.groupKey);
            m.append("\n    summary:");
            appendEntry(m, this.summary);
            m.append("\n    children size: ");
            m.append(this.children.size());
            for (NotificationEntry notificationEntry : this.children.values()) {
                appendEntry(m, notificationEntry);
            }
            m.append("\n    alertOverride:");
            appendEntry(m, this.alertOverride);
            m.append("\n    summary suppressed: ");
            m.append(this.suppressed);
            return m.toString();
        }

        public NotificationGroup(String str) {
            this.groupKey = str;
        }
    }

    /* loaded from: classes.dex */
    public interface OnGroupChangeListener {
        default void onGroupAlertOverrideChanged(NotificationGroup notificationGroup, NotificationEntry notificationEntry) {
        }

        default void onGroupCreated(NotificationGroup notificationGroup, String str) {
        }

        default void onGroupRemoved(String str) {
        }

        default void onGroupSuppressionChanged(NotificationGroup notificationGroup, boolean z) {
        }

        default void onGroupsChanged() {
        }
    }

    /* loaded from: classes.dex */
    public static class PostRecord implements Comparable<PostRecord> {
        public final String key;
        public final long postTime;

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || PostRecord.class != obj.getClass()) {
                return false;
            }
            PostRecord postRecord = (PostRecord) obj;
            return this.postTime == postRecord.postTime && this.key.equals(postRecord.key);
        }

        public final int hashCode() {
            return Objects.hash(Long.valueOf(this.postTime), this.key);
        }

        @Override // java.lang.Comparable
        public final int compareTo(PostRecord postRecord) {
            PostRecord postRecord2 = postRecord;
            int compare = Long.compare(this.postTime, postRecord2.postTime);
            if (compare == 0) {
                return String.CASE_INSENSITIVE_ORDER.compare(this.key, postRecord2.key);
            }
            return compare;
        }

        public PostRecord(NotificationEntry notificationEntry) {
            Objects.requireNonNull(notificationEntry);
            this.postTime = notificationEntry.mSbn.getPostTime();
            this.key = notificationEntry.mKey;
        }
    }

    public final void onEntryRemovedInternal(NotificationEntry notificationEntry, StatusBarNotification statusBarNotification) {
        onEntryRemovedInternal(notificationEntry, statusBarNotification.getGroupKey(), statusBarNotification.isGroup(), statusBarNotification.getNotification().isGroupSummary());
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public final void onStateChanged(int i) {
        if (i == 1) {
            collapseGroups();
        }
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager
    public final void setGroupExpanded(NotificationEntry notificationEntry, boolean z) {
        HashMap<String, NotificationGroup> hashMap = this.mGroupMap;
        Objects.requireNonNull(notificationEntry);
        NotificationGroup notificationGroup = hashMap.get(getGroupKey(notificationEntry.mSbn));
        if (notificationGroup != null) {
            setGroupExpanded(notificationGroup, z);
        }
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager
    public final void collapseGroups() {
        ArrayList arrayList = new ArrayList(this.mGroupMap.values());
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            NotificationGroup notificationGroup = (NotificationGroup) arrayList.get(i);
            if (notificationGroup.expanded) {
                setGroupExpanded(notificationGroup, false);
            }
            updateSuppression(notificationGroup);
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder m = LockIconView$$ExternalSyntheticOutline0.m(printWriter, "GroupManagerLegacy state:", "  number of groups: ");
        m.append(this.mGroupMap.size());
        printWriter.println(m.toString());
        for (Map.Entry<String, NotificationGroup> entry : this.mGroupMap.entrySet()) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("\n    key: ");
            m2.append(R$array.logKey(entry.getKey()));
            printWriter.println(m2.toString());
            printWriter.println(entry.getValue());
        }
        StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("\n    isolated entries: ");
        m3.append(this.mIsolatedEntries.size());
        printWriter.println(m3.toString());
        for (Map.Entry<String, StatusBarNotification> entry2 : this.mIsolatedEntries.entrySet()) {
            printWriter.print("      ");
            printWriter.print(R$array.logKey(entry2.getKey()));
            printWriter.print(", ");
            printWriter.println(entry2.getValue());
        }
    }

    public final ArrayList<NotificationEntry> getLogicalChildren(StatusBarNotification statusBarNotification) {
        NotificationGroup notificationGroup = this.mGroupMap.get(statusBarNotification.getGroupKey());
        if (notificationGroup == null) {
            return null;
        }
        ArrayList<NotificationEntry> arrayList = new ArrayList<>(notificationGroup.children.values());
        for (StatusBarNotification statusBarNotification2 : this.mIsolatedEntries.values()) {
            if (statusBarNotification2.getGroupKey().equals(statusBarNotification.getGroupKey())) {
                arrayList.add(this.mGroupMap.get(statusBarNotification2.getKey()).summary);
            }
        }
        return arrayList;
    }

    public final int getNumberOfIsolatedChildren(String str) {
        int i = 0;
        for (StatusBarNotification statusBarNotification : this.mIsolatedEntries.values()) {
            if (statusBarNotification.getGroupKey().equals(str) && isIsolated(statusBarNotification.getKey())) {
                i++;
            }
        }
        return i;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager
    public final boolean isGroupExpanded(NotificationEntry notificationEntry) {
        HashMap<String, NotificationGroup> hashMap = this.mGroupMap;
        Objects.requireNonNull(notificationEntry);
        NotificationGroup notificationGroup = hashMap.get(getGroupKey(notificationEntry.mSbn));
        if (notificationGroup == null) {
            return false;
        }
        return notificationGroup.expanded;
    }

    public final boolean isIsolated(String str) {
        return this.mIsolatedEntries.containsKey(str);
    }

    public final void onEntryRemoved(NotificationEntry notificationEntry) {
        GroupEventDispatcher groupEventDispatcher = this.mEventDispatcher;
        Objects.requireNonNull(groupEventDispatcher);
        groupEventDispatcher.mBufferScopeDepth++;
        Objects.requireNonNull(notificationEntry);
        onEntryRemovedInternal(notificationEntry, notificationEntry.mSbn);
        StatusBarNotification remove = this.mIsolatedEntries.remove(notificationEntry.mKey);
        if (remove != null) {
            updateSuppression(this.mGroupMap.get(remove.getGroupKey()));
        }
        this.mEventDispatcher.closeBufferScope();
    }

    public final void onEntryUpdated(NotificationEntry notificationEntry, String str, boolean z, boolean z2) {
        boolean z3;
        String groupKey = notificationEntry.mSbn.getGroupKey();
        boolean z4 = true;
        boolean z5 = !str.equals(groupKey);
        if (!isIsolated(notificationEntry.mKey) && z && !z2) {
            z3 = true;
        } else {
            z3 = false;
        }
        boolean isGroupChild = isGroupChild(notificationEntry.mSbn);
        GroupEventDispatcher groupEventDispatcher = this.mEventDispatcher;
        Objects.requireNonNull(groupEventDispatcher);
        groupEventDispatcher.mBufferScopeDepth++;
        if (z5 || z3 != isGroupChild) {
            z4 = false;
        }
        this.mIsUpdatingUnchangedGroup = z4;
        HashMap<String, NotificationGroup> hashMap = this.mGroupMap;
        String str2 = notificationEntry.mKey;
        if (!isIsolated(str2)) {
            str2 = str;
        }
        if (hashMap.get(str2) != null) {
            onEntryRemovedInternal(notificationEntry, str, z, z2);
        }
        onEntryAddedInternal(notificationEntry);
        this.mIsUpdatingUnchangedGroup = false;
        if (isIsolated(notificationEntry.mSbn.getKey())) {
            this.mIsolatedEntries.put(notificationEntry.mKey, notificationEntry.mSbn);
            if (z5) {
                updateSuppression(this.mGroupMap.get(str));
            }
            updateSuppression(this.mGroupMap.get(groupKey));
        } else if (!z3 && isGroupChild) {
            updateIsolation(notificationEntry);
        }
        this.mEventDispatcher.closeBufferScope();
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager
    public final void registerGroupExpansionChangeListener(NotificationStackScrollLayoutController$$ExternalSyntheticLambda1 notificationStackScrollLayoutController$$ExternalSyntheticLambda1) {
        this.mExpansionChangeListeners.add(notificationStackScrollLayoutController$$ExternalSyntheticLambda1);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager
    public final boolean toggleGroupExpansion(NotificationEntry notificationEntry) {
        HashMap<String, NotificationGroup> hashMap = this.mGroupMap;
        Objects.requireNonNull(notificationEntry);
        NotificationGroup notificationGroup = hashMap.get(getGroupKey(notificationEntry.mSbn));
        if (notificationGroup == null) {
            return false;
        }
        setGroupExpanded(notificationGroup, !notificationGroup.expanded);
        return notificationGroup.expanded;
    }

    /* JADX WARN: Code restructure failed: missing block: B:36:0x008e, code lost:
        if (r1 == false) goto L_0x0091;
     */
    /* JADX WARN: Removed duplicated region for block: B:34:0x008b  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0094  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00be  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateIsolation(com.android.systemui.statusbar.notification.collection.NotificationEntry r7) {
        /*
            Method dump skipped, instructions count: 218
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy.updateIsolation(com.android.systemui.statusbar.notification.collection.NotificationEntry):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:74:0x01b2, code lost:
        if (r7 == false) goto L_0x01b6;
     */
    /* JADX WARN: Removed duplicated region for block: B:120:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x01bd  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x01bf  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x01c2  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x01c9  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x01d0  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x01d9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateSuppression(com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy.NotificationGroup r18) {
        /*
            Method dump skipped, instructions count: 479
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy.updateSuppression(com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy$NotificationGroup):void");
    }

    public NotificationGroupManagerLegacy(StatusBarStateController statusBarStateController, Lazy<PeopleNotificationIdentifier> lazy, Optional<Bubbles> optional, DumpManager dumpManager) {
        HashMap<String, NotificationGroup> hashMap = new HashMap<>();
        this.mGroupMap = hashMap;
        this.mEventDispatcher = new GroupEventDispatcher(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda3(hashMap, 1));
        statusBarStateController.addCallback(this);
        this.mPeopleNotificationIdentifier = lazy;
        this.mBubblesOptional = optional;
        dumpManager.registerDumpable(this);
    }

    public static void trimPostBatchHistory(TreeSet treeSet) {
        if (treeSet.size() > 1) {
            long j = ((PostRecord) treeSet.last()).postTime - 5000;
            while (!treeSet.isEmpty() && ((PostRecord) treeSet.first()).postTime < j) {
                treeSet.pollFirst();
            }
        }
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager
    public final List<NotificationEntry> getChildren(ListEntry listEntry) {
        NotificationEntry representativeEntry = listEntry.getRepresentativeEntry();
        HashMap<String, NotificationGroup> hashMap = this.mGroupMap;
        Objects.requireNonNull(representativeEntry);
        NotificationGroup notificationGroup = hashMap.get(representativeEntry.mSbn.getGroupKey());
        if (notificationGroup == null) {
            return null;
        }
        return new ArrayList(notificationGroup.children.values());
    }

    public final String getGroupKey(StatusBarNotification statusBarNotification) {
        String key = statusBarNotification.getKey();
        String groupKey = statusBarNotification.getGroupKey();
        if (isIsolated(key)) {
            return key;
        }
        return groupKey;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager
    public final NotificationEntry getGroupSummary(NotificationEntry notificationEntry) {
        Objects.requireNonNull(notificationEntry);
        NotificationGroup notificationGroup = this.mGroupMap.get(getGroupKey(notificationEntry.mSbn));
        if (notificationGroup == null) {
            return null;
        }
        return notificationGroup.summary;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager
    public final NotificationEntry getLogicalGroupSummary(NotificationEntry notificationEntry) {
        Objects.requireNonNull(notificationEntry);
        NotificationGroup notificationGroup = this.mGroupMap.get(notificationEntry.mSbn.getGroupKey());
        if (notificationGroup == null) {
            return null;
        }
        return notificationGroup.summary;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager
    public final boolean isChildInGroup(NotificationEntry notificationEntry) {
        NotificationGroup notificationGroup;
        Objects.requireNonNull(notificationEntry);
        StatusBarNotification statusBarNotification = notificationEntry.mSbn;
        if (isGroupChild(statusBarNotification) && (notificationGroup = this.mGroupMap.get(getGroupKey(statusBarNotification))) != null && notificationGroup.summary != null && !notificationGroup.suppressed && !notificationGroup.children.isEmpty()) {
            return true;
        }
        return false;
    }

    public final boolean isGroupChild(StatusBarNotification statusBarNotification) {
        String key = statusBarNotification.getKey();
        boolean isGroup = statusBarNotification.isGroup();
        boolean isGroupSummary = statusBarNotification.getNotification().isGroupSummary();
        if (!isIsolated(key) && isGroup && !isGroupSummary) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager
    public final boolean isGroupSummary(NotificationEntry notificationEntry) {
        boolean z;
        NotificationGroup notificationGroup;
        Objects.requireNonNull(notificationEntry);
        StatusBarNotification statusBarNotification = notificationEntry.mSbn;
        if (isIsolated(statusBarNotification.getKey())) {
            z = true;
        } else {
            z = statusBarNotification.getNotification().isGroupSummary();
        }
        if (!z || (notificationGroup = this.mGroupMap.get(getGroupKey(statusBarNotification))) == null || notificationGroup.summary == null) {
            return false;
        }
        if (!notificationGroup.children.isEmpty()) {
            NotificationEntry notificationEntry2 = notificationGroup.summary;
            Objects.requireNonNull(notificationEntry2);
            if (Objects.equals(notificationEntry2.mSbn, statusBarNotification)) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0037 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0038  */
    @Override // com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean isOnlyChildInGroup(com.android.systemui.statusbar.notification.collection.NotificationEntry r7) {
        /*
            r6 = this;
            java.util.Objects.requireNonNull(r7)
            android.service.notification.StatusBarNotification r0 = r7.mSbn
            android.app.Notification r1 = r0.getNotification()
            boolean r1 = r1.isGroupSummary()
            r2 = 1
            r3 = 0
            if (r1 != 0) goto L_0x0034
            java.lang.String r1 = r0.getGroupKey()
            int r1 = r6.getNumberOfIsolatedChildren(r1)
            java.util.HashMap<java.lang.String, com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy$NotificationGroup> r4 = r6.mGroupMap
            java.lang.String r5 = r0.getGroupKey()
            java.lang.Object r4 = r4.get(r5)
            com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy$NotificationGroup r4 = (com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy.NotificationGroup) r4
            if (r4 == 0) goto L_0x002e
            java.util.HashMap<java.lang.String, com.android.systemui.statusbar.notification.collection.NotificationEntry> r4 = r4.children
            int r4 = r4.size()
            goto L_0x002f
        L_0x002e:
            r4 = r3
        L_0x002f:
            int r1 = r1 + r4
            if (r1 != r2) goto L_0x0034
            r1 = r2
            goto L_0x0035
        L_0x0034:
            r1 = r3
        L_0x0035:
            if (r1 != 0) goto L_0x0038
            return r3
        L_0x0038:
            com.android.systemui.statusbar.notification.collection.NotificationEntry r6 = r6.getLogicalGroupSummary(r7)
            if (r6 == 0) goto L_0x0047
            android.service.notification.StatusBarNotification r6 = r6.mSbn
            boolean r6 = r6.equals(r0)
            if (r6 != 0) goto L_0x0047
            goto L_0x0048
        L_0x0047:
            r2 = r3
        L_0x0048:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy.isOnlyChildInGroup(com.android.systemui.statusbar.notification.collection.NotificationEntry):boolean");
    }

    public final boolean isSummaryOfSuppressedGroup(StatusBarNotification statusBarNotification) {
        boolean z;
        if (statusBarNotification.getNotification().isGroupSummary()) {
            NotificationGroup notificationGroup = this.mGroupMap.get(getGroupKey(statusBarNotification));
            if (notificationGroup == null || !notificationGroup.suppressed) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                return true;
            }
        }
        return false;
    }

    public final void onEntryAddedInternal(NotificationEntry notificationEntry) {
        boolean z;
        String str;
        if (notificationEntry.isRowRemoved()) {
            notificationEntry.mDebugThrowable = new Throwable();
        }
        StatusBarNotification statusBarNotification = notificationEntry.mSbn;
        boolean isGroupChild = isGroupChild(statusBarNotification);
        String groupKey = getGroupKey(statusBarNotification);
        NotificationGroup notificationGroup = this.mGroupMap.get(groupKey);
        if (notificationGroup == null) {
            notificationGroup = new NotificationGroup(groupKey);
            this.mGroupMap.put(groupKey, notificationGroup);
            GroupEventDispatcher groupEventDispatcher = this.mEventDispatcher;
            Objects.requireNonNull(groupEventDispatcher);
            String str2 = notificationGroup.groupKey;
            Iterator<OnGroupChangeListener> it = groupEventDispatcher.mGroupChangeListeners.iterator();
            while (it.hasNext()) {
                it.next().onGroupCreated(notificationGroup, str2);
            }
        }
        if (isGroupChild) {
            NotificationEntry notificationEntry2 = notificationGroup.children.get(notificationEntry.mKey);
            if (!(notificationEntry2 == null || notificationEntry2 == notificationEntry)) {
                Throwable th = notificationEntry2.mDebugThrowable;
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Inconsistent entries found with the same key ");
                m.append(R$array.logKey(notificationEntry.mKey));
                m.append("existing removed: ");
                m.append(notificationEntry2.isRowRemoved());
                if (th != null) {
                    str = Log.getStackTraceString(th) + "\n";
                } else {
                    str = "";
                }
                m.append(str);
                m.append(" added removed");
                m.append(notificationEntry.isRowRemoved());
                Log.wtf("NotifGroupManager", m.toString(), new Throwable());
            }
            notificationGroup.children.put(notificationEntry.mKey, notificationEntry);
            if (notificationGroup.postBatchHistory.add(new PostRecord(notificationEntry))) {
                trimPostBatchHistory(notificationGroup.postBatchHistory);
            }
            updateSuppression(notificationGroup);
            return;
        }
        notificationGroup.summary = notificationEntry;
        if (notificationGroup.postBatchHistory.add(new PostRecord(notificationEntry))) {
            trimPostBatchHistory(notificationGroup.postBatchHistory);
        }
        ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
        if (expandableNotificationRow == null || !expandableNotificationRow.mChildrenExpanded) {
            z = false;
        } else {
            z = true;
        }
        notificationGroup.expanded = z;
        updateSuppression(notificationGroup);
        if (!notificationGroup.children.isEmpty()) {
            Iterator it2 = new ArrayList(notificationGroup.children.values()).iterator();
            while (it2.hasNext()) {
                updateIsolation((NotificationEntry) it2.next());
            }
            this.mEventDispatcher.notifyGroupsChanged();
        }
    }

    public final void onEntryRemovedInternal(NotificationEntry notificationEntry, String str, boolean z, boolean z2) {
        Objects.requireNonNull(notificationEntry);
        String str2 = notificationEntry.mKey;
        if (isIsolated(str2)) {
            str = str2;
        }
        NotificationGroup notificationGroup = this.mGroupMap.get(str);
        if (notificationGroup != null) {
            boolean z3 = false;
            if (!isIsolated(notificationEntry.mKey) && z && !z2) {
                z3 = true;
            }
            if (z3) {
                notificationGroup.children.remove(notificationEntry.mKey);
            } else {
                notificationGroup.summary = null;
            }
            updateSuppression(notificationGroup);
            if (notificationGroup.children.isEmpty() && notificationGroup.summary == null) {
                this.mGroupMap.remove(str);
                GroupEventDispatcher groupEventDispatcher = this.mEventDispatcher;
                Objects.requireNonNull(groupEventDispatcher);
                String str3 = notificationGroup.groupKey;
                Iterator<OnGroupChangeListener> it = groupEventDispatcher.mGroupChangeListeners.iterator();
                while (it.hasNext()) {
                    it.next().onGroupRemoved(str3);
                }
            }
        }
    }

    public final void setGroupExpanded(NotificationGroup notificationGroup, boolean z) {
        notificationGroup.expanded = z;
        if (notificationGroup.summary != null) {
            Iterator<GroupExpansionManager.OnGroupExpansionChangeListener> it = this.mExpansionChangeListeners.iterator();
            while (it.hasNext()) {
                NotificationEntry notificationEntry = notificationGroup.summary;
                Objects.requireNonNull(notificationEntry);
                it.next().onGroupExpansionChange(notificationEntry.row, z);
            }
        }
    }

    @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
    public final void onHeadsUpStateChanged(NotificationEntry notificationEntry, boolean z) {
        updateIsolation(notificationEntry);
    }
}
