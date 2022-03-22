package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class GroupMembershipManagerImpl implements GroupMembershipManager {
    @Override // com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager
    public final List<NotificationEntry> getChildren(ListEntry listEntry) {
        if (listEntry instanceof GroupEntry) {
            GroupEntry groupEntry = (GroupEntry) listEntry;
            Objects.requireNonNull(groupEntry);
            return groupEntry.mUnmodifiableChildren;
        } else if (!isGroupSummary(listEntry.getRepresentativeEntry())) {
            return null;
        } else {
            GroupEntry parent = listEntry.getRepresentativeEntry().getParent();
            Objects.requireNonNull(parent);
            return parent.mUnmodifiableChildren;
        }
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager
    public final NotificationEntry getGroupSummary(NotificationEntry notificationEntry) {
        boolean z;
        if (notificationEntry.getParent() == GroupEntry.ROOT_ENTRY) {
            z = true;
        } else {
            z = false;
        }
        if (z || notificationEntry.getParent() == null) {
            return null;
        }
        GroupEntry parent = notificationEntry.getParent();
        Objects.requireNonNull(parent);
        return parent.mSummary;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager
    public final boolean isChildInGroup(NotificationEntry notificationEntry) {
        boolean z;
        if (notificationEntry.getParent() == GroupEntry.ROOT_ENTRY) {
            z = true;
        } else {
            z = false;
        }
        return !z;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager
    public final boolean isGroupSummary(NotificationEntry notificationEntry) {
        if (getGroupSummary(notificationEntry) == notificationEntry) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager
    public final boolean isOnlyChildInGroup(NotificationEntry notificationEntry) {
        boolean z;
        if (notificationEntry.getParent() == null) {
            return false;
        }
        if (getGroupSummary(notificationEntry) == notificationEntry) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            return false;
        }
        GroupEntry parent = notificationEntry.getParent();
        Objects.requireNonNull(parent);
        if (parent.mUnmodifiableChildren.size() == 1) {
            return true;
        }
        return false;
    }
}
