package com.android.systemui.statusbar.notification.collection.render;

import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.Coordinator;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeRenderListListener;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController$$ExternalSyntheticLambda1;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final class GroupExpansionManagerImpl implements GroupExpansionManager, Coordinator {
    public final GroupMembershipManager mGroupMembershipManager;
    public final HashSet mOnGroupChangeListeners = new HashSet();
    public final HashSet mExpandedGroups = new HashSet();
    public final GroupExpansionManagerImpl$$ExternalSyntheticLambda0 mNotifTracker = new OnBeforeRenderListListener() { // from class: com.android.systemui.statusbar.notification.collection.render.GroupExpansionManagerImpl$$ExternalSyntheticLambda0
        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeRenderListListener
        public final void onBeforeRenderList(List list) {
            GroupExpansionManagerImpl groupExpansionManagerImpl = GroupExpansionManagerImpl.this;
            Objects.requireNonNull(groupExpansionManagerImpl);
            final HashSet hashSet = new HashSet();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                ListEntry listEntry = (ListEntry) it.next();
                if (listEntry instanceof GroupEntry) {
                    hashSet.add(listEntry.getRepresentativeEntry());
                }
            }
            groupExpansionManagerImpl.mExpandedGroups.removeIf(new Predicate() { // from class: com.android.systemui.statusbar.notification.collection.render.GroupExpansionManagerImpl$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return !hashSet.contains((NotificationEntry) obj);
                }
            });
        }
    };

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public final void attach(NotifPipeline notifPipeline) {
        notifPipeline.addOnBeforeRenderListListener(this.mNotifTracker);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager
    public final void collapseGroups() {
        Iterator it = new ArrayList(this.mExpandedGroups).iterator();
        while (it.hasNext()) {
            setGroupExpanded((NotificationEntry) it.next(), false);
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder m = LockIconView$$ExternalSyntheticOutline0.m(printWriter, "NotificationEntryExpansion state:", "  # expanded groups: ");
        m.append(this.mExpandedGroups.size());
        printWriter.println(m.toString());
        Iterator it = this.mExpandedGroups.iterator();
        while (it.hasNext()) {
            NotificationEntry notificationEntry = (NotificationEntry) it.next();
            StringBuilder sb = new StringBuilder();
            sb.append("    summary key of expanded group: ");
            Objects.requireNonNull(notificationEntry);
            sb.append(notificationEntry.mKey);
            printWriter.println(sb.toString());
        }
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager
    public final boolean isGroupExpanded(NotificationEntry notificationEntry) {
        return this.mExpandedGroups.contains(this.mGroupMembershipManager.getGroupSummary(notificationEntry));
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager
    public final void registerGroupExpansionChangeListener(NotificationStackScrollLayoutController$$ExternalSyntheticLambda1 notificationStackScrollLayoutController$$ExternalSyntheticLambda1) {
        this.mOnGroupChangeListeners.add(notificationStackScrollLayoutController$$ExternalSyntheticLambda1);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager
    public final void setGroupExpanded(NotificationEntry notificationEntry, boolean z) {
        NotificationEntry groupSummary = this.mGroupMembershipManager.getGroupSummary(notificationEntry);
        if (z) {
            this.mExpandedGroups.add(groupSummary);
        } else {
            this.mExpandedGroups.remove(groupSummary);
        }
        Iterator it = this.mOnGroupChangeListeners.iterator();
        while (it.hasNext()) {
            Objects.requireNonNull(notificationEntry);
            ((GroupExpansionManager.OnGroupExpansionChangeListener) it.next()).onGroupExpansionChange(notificationEntry.row, z);
        }
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.systemui.statusbar.notification.collection.render.GroupExpansionManagerImpl$$ExternalSyntheticLambda0] */
    public GroupExpansionManagerImpl(GroupMembershipManager groupMembershipManager) {
        this.mGroupMembershipManager = groupMembershipManager;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager
    public final boolean toggleGroupExpansion(NotificationEntry notificationEntry) {
        setGroupExpanded(notificationEntry, !isGroupExpanded(notificationEntry));
        return isGroupExpanded(notificationEntry);
    }
}
