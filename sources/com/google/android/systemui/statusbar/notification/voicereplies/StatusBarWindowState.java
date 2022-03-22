package com.google.android.systemui.statusbar.notification.voicereplies;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes.dex */
public final class StatusBarWindowState {
    public final boolean bouncerShowing;
    public final boolean keyguardOccluded;
    public final boolean keyguardShowing;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StatusBarWindowState)) {
            return false;
        }
        StatusBarWindowState statusBarWindowState = (StatusBarWindowState) obj;
        return this.keyguardShowing == statusBarWindowState.keyguardShowing && this.keyguardOccluded == statusBarWindowState.keyguardOccluded && this.bouncerShowing == statusBarWindowState.bouncerShowing;
    }

    public final int hashCode() {
        boolean z = this.keyguardShowing;
        int i = 1;
        if (z) {
            z = true;
        }
        int i2 = z ? 1 : 0;
        int i3 = z ? 1 : 0;
        int i4 = i2 * 31;
        boolean z2 = this.keyguardOccluded;
        if (z2) {
            z2 = true;
        }
        int i5 = z2 ? 1 : 0;
        int i6 = z2 ? 1 : 0;
        int i7 = (i4 + i5) * 31;
        boolean z3 = this.bouncerShowing;
        if (!z3) {
            i = z3 ? 1 : 0;
        }
        return i7 + i;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("StatusBarWindowState(keyguardShowing=");
        m.append(this.keyguardShowing);
        m.append(", keyguardOccluded=");
        m.append(this.keyguardOccluded);
        m.append(", bouncerShowing=");
        return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(m, this.bouncerShowing, ')');
    }

    public StatusBarWindowState(boolean z, boolean z2, boolean z3) {
        this.keyguardShowing = z;
        this.keyguardOccluded = z2;
        this.bouncerShowing = z3;
    }
}
