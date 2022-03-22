package com.android.systemui.statusbar.notification.collection.render;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
/* compiled from: NotifStackController.kt */
/* loaded from: classes.dex */
public final class NotifStats {
    public static final NotifStats empty = new NotifStats(0, false, false, false, false);
    public final boolean hasClearableAlertingNotifs;
    public final boolean hasClearableSilentNotifs;
    public final boolean hasNonClearableAlertingNotifs;
    public final boolean hasNonClearableSilentNotifs;
    public final int numActiveNotifs;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NotifStats)) {
            return false;
        }
        NotifStats notifStats = (NotifStats) obj;
        return this.numActiveNotifs == notifStats.numActiveNotifs && this.hasNonClearableAlertingNotifs == notifStats.hasNonClearableAlertingNotifs && this.hasClearableAlertingNotifs == notifStats.hasClearableAlertingNotifs && this.hasNonClearableSilentNotifs == notifStats.hasNonClearableSilentNotifs && this.hasClearableSilentNotifs == notifStats.hasClearableSilentNotifs;
    }

    public final int hashCode() {
        int hashCode = Integer.hashCode(this.numActiveNotifs) * 31;
        boolean z = this.hasNonClearableAlertingNotifs;
        int i = 1;
        if (z) {
            z = true;
        }
        int i2 = z ? 1 : 0;
        int i3 = z ? 1 : 0;
        int i4 = (hashCode + i2) * 31;
        boolean z2 = this.hasClearableAlertingNotifs;
        if (z2) {
            z2 = true;
        }
        int i5 = z2 ? 1 : 0;
        int i6 = z2 ? 1 : 0;
        int i7 = (i4 + i5) * 31;
        boolean z3 = this.hasNonClearableSilentNotifs;
        if (z3) {
            z3 = true;
        }
        int i8 = z3 ? 1 : 0;
        int i9 = z3 ? 1 : 0;
        int i10 = (i7 + i8) * 31;
        boolean z4 = this.hasClearableSilentNotifs;
        if (!z4) {
            i = z4 ? 1 : 0;
        }
        return i10 + i;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("NotifStats(numActiveNotifs=");
        m.append(this.numActiveNotifs);
        m.append(", hasNonClearableAlertingNotifs=");
        m.append(this.hasNonClearableAlertingNotifs);
        m.append(", hasClearableAlertingNotifs=");
        m.append(this.hasClearableAlertingNotifs);
        m.append(", hasNonClearableSilentNotifs=");
        m.append(this.hasNonClearableSilentNotifs);
        m.append(", hasClearableSilentNotifs=");
        return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(m, this.hasClearableSilentNotifs, ')');
    }

    public NotifStats(int i, boolean z, boolean z2, boolean z3, boolean z4) {
        this.numActiveNotifs = i;
        this.hasNonClearableAlertingNotifs = z;
        this.hasClearableAlertingNotifs = z2;
        this.hasNonClearableSilentNotifs = z3;
        this.hasClearableSilentNotifs = z4;
    }
}
