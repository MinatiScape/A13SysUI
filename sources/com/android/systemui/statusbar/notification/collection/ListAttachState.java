package com.android.systemui.statusbar.notification.collection;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ListAttachState.kt */
/* loaded from: classes.dex */
public final class ListAttachState {
    public SuppressedAttachState suppressedChanges;
    public GroupEntry parent = null;
    public NotifSection section = null;
    public NotifFilter excludingFilter = null;
    public NotifPromoter promoter = null;
    public String groupPruneReason = null;
    public int stableIndex = -1;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ListAttachState)) {
            return false;
        }
        ListAttachState listAttachState = (ListAttachState) obj;
        return Intrinsics.areEqual(this.parent, listAttachState.parent) && Intrinsics.areEqual(this.section, listAttachState.section) && Intrinsics.areEqual(this.excludingFilter, listAttachState.excludingFilter) && Intrinsics.areEqual(this.promoter, listAttachState.promoter) && Intrinsics.areEqual(this.groupPruneReason, listAttachState.groupPruneReason) && Intrinsics.areEqual(this.suppressedChanges, listAttachState.suppressedChanges);
    }

    public final int hashCode() {
        GroupEntry groupEntry = this.parent;
        int i = 0;
        int hashCode = (groupEntry == null ? 0 : groupEntry.hashCode()) * 31;
        NotifSection notifSection = this.section;
        int hashCode2 = (hashCode + (notifSection == null ? 0 : notifSection.hashCode())) * 31;
        NotifFilter notifFilter = this.excludingFilter;
        int hashCode3 = (hashCode2 + (notifFilter == null ? 0 : notifFilter.hashCode())) * 31;
        NotifPromoter notifPromoter = this.promoter;
        int hashCode4 = (hashCode3 + (notifPromoter == null ? 0 : notifPromoter.hashCode())) * 31;
        String str = this.groupPruneReason;
        if (str != null) {
            i = str.hashCode();
        }
        return this.suppressedChanges.hashCode() + ((hashCode4 + i) * 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ListAttachState(parent=");
        m.append(this.parent);
        m.append(", section=");
        m.append(this.section);
        m.append(", excludingFilter=");
        m.append(this.excludingFilter);
        m.append(", promoter=");
        m.append(this.promoter);
        m.append(", groupPruneReason=");
        m.append((Object) this.groupPruneReason);
        m.append(", suppressedChanges=");
        m.append(this.suppressedChanges);
        m.append(')');
        return m.toString();
    }

    public ListAttachState(SuppressedAttachState suppressedAttachState) {
        this.suppressedChanges = suppressedAttachState;
    }
}
