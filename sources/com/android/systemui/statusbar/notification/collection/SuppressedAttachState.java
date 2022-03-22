package com.android.systemui.statusbar.notification.collection;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SuppressedAttachState.kt */
/* loaded from: classes.dex */
public final class SuppressedAttachState {
    public NotifSection section = null;
    public GroupEntry parent = null;
    public boolean wasPruneSuppressed = false;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SuppressedAttachState)) {
            return false;
        }
        SuppressedAttachState suppressedAttachState = (SuppressedAttachState) obj;
        return Intrinsics.areEqual(this.section, suppressedAttachState.section) && Intrinsics.areEqual(this.parent, suppressedAttachState.parent) && this.wasPruneSuppressed == suppressedAttachState.wasPruneSuppressed;
    }

    public final int hashCode() {
        NotifSection notifSection = this.section;
        int i = 0;
        int hashCode = (notifSection == null ? 0 : notifSection.hashCode()) * 31;
        GroupEntry groupEntry = this.parent;
        if (groupEntry != null) {
            i = groupEntry.hashCode();
        }
        int i2 = (hashCode + i) * 31;
        boolean z = this.wasPruneSuppressed;
        if (z) {
            z = true;
        }
        int i3 = z ? 1 : 0;
        int i4 = z ? 1 : 0;
        return i2 + i3;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("SuppressedAttachState(section=");
        m.append(this.section);
        m.append(", parent=");
        m.append(this.parent);
        m.append(", wasPruneSuppressed=");
        return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(m, this.wasPruneSuppressed, ')');
    }
}
