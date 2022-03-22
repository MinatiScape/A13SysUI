package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class ListEntry {
    public final long mCreationTime;
    public final String mKey;
    public final ListAttachState mPreviousAttachState = new ListAttachState(new SuppressedAttachState());
    public final ListAttachState mAttachState = new ListAttachState(new SuppressedAttachState());

    public abstract NotificationEntry getRepresentativeEntry();

    public final void beginNewAttachState() {
        ListAttachState listAttachState = this.mPreviousAttachState;
        ListAttachState listAttachState2 = this.mAttachState;
        Objects.requireNonNull(listAttachState);
        listAttachState.parent = listAttachState2.parent;
        listAttachState.section = listAttachState2.section;
        listAttachState.excludingFilter = listAttachState2.excludingFilter;
        listAttachState.promoter = listAttachState2.promoter;
        listAttachState.groupPruneReason = listAttachState2.groupPruneReason;
        SuppressedAttachState suppressedAttachState = listAttachState.suppressedChanges;
        SuppressedAttachState suppressedAttachState2 = listAttachState2.suppressedChanges;
        Objects.requireNonNull(suppressedAttachState);
        suppressedAttachState.parent = suppressedAttachState2.parent;
        suppressedAttachState.section = suppressedAttachState2.section;
        suppressedAttachState.wasPruneSuppressed = suppressedAttachState2.wasPruneSuppressed;
        listAttachState.stableIndex = listAttachState2.stableIndex;
        ListAttachState listAttachState3 = this.mAttachState;
        Objects.requireNonNull(listAttachState3);
        listAttachState3.parent = null;
        listAttachState3.section = null;
        listAttachState3.excludingFilter = null;
        listAttachState3.promoter = null;
        listAttachState3.groupPruneReason = null;
        SuppressedAttachState suppressedAttachState3 = listAttachState3.suppressedChanges;
        Objects.requireNonNull(suppressedAttachState3);
        suppressedAttachState3.parent = null;
        suppressedAttachState3.section = null;
        suppressedAttachState3.wasPruneSuppressed = false;
        listAttachState3.stableIndex = -1;
    }

    public final GroupEntry getParent() {
        ListAttachState listAttachState = this.mAttachState;
        Objects.requireNonNull(listAttachState);
        return listAttachState.parent;
    }

    public final NotifSection getSection() {
        ListAttachState listAttachState = this.mAttachState;
        Objects.requireNonNull(listAttachState);
        return listAttachState.section;
    }

    public final void setParent(GroupEntry groupEntry) {
        ListAttachState listAttachState = this.mAttachState;
        Objects.requireNonNull(listAttachState);
        listAttachState.parent = groupEntry;
    }

    public final boolean wasAttachedInPreviousPass() {
        ListAttachState listAttachState = this.mPreviousAttachState;
        Objects.requireNonNull(listAttachState);
        if (listAttachState.parent != null) {
            return true;
        }
        return false;
    }

    public ListEntry(String str, long j) {
        this.mKey = str;
        this.mCreationTime = j;
    }

    public String getKey() {
        return this.mKey;
    }
}
