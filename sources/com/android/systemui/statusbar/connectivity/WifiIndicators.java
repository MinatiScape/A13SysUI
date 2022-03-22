package com.android.systemui.statusbar.connectivity;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: SignalCallback.kt */
/* loaded from: classes.dex */
public final class WifiIndicators {
    public final boolean activityIn;
    public final boolean activityOut;
    public final String description;
    public final boolean enabled;
    public final boolean isTransient;
    public final IconState qsIcon;
    public final IconState statusIcon;
    public final String statusLabel;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WifiIndicators)) {
            return false;
        }
        WifiIndicators wifiIndicators = (WifiIndicators) obj;
        return this.enabled == wifiIndicators.enabled && Intrinsics.areEqual(this.statusIcon, wifiIndicators.statusIcon) && Intrinsics.areEqual(this.qsIcon, wifiIndicators.qsIcon) && this.activityIn == wifiIndicators.activityIn && this.activityOut == wifiIndicators.activityOut && Intrinsics.areEqual(this.description, wifiIndicators.description) && this.isTransient == wifiIndicators.isTransient && Intrinsics.areEqual(this.statusLabel, wifiIndicators.statusLabel);
    }

    public final int hashCode() {
        boolean z = this.enabled;
        int i = 1;
        if (z) {
            z = true;
        }
        int i2 = z ? 1 : 0;
        int i3 = z ? 1 : 0;
        int i4 = i2 * 31;
        IconState iconState = this.statusIcon;
        int i5 = 0;
        int hashCode = (i4 + (iconState == null ? 0 : iconState.hashCode())) * 31;
        IconState iconState2 = this.qsIcon;
        int hashCode2 = (hashCode + (iconState2 == null ? 0 : iconState2.hashCode())) * 31;
        boolean z2 = this.activityIn;
        if (z2) {
            z2 = true;
        }
        int i6 = z2 ? 1 : 0;
        int i7 = z2 ? 1 : 0;
        int i8 = (hashCode2 + i6) * 31;
        boolean z3 = this.activityOut;
        if (z3) {
            z3 = true;
        }
        int i9 = z3 ? 1 : 0;
        int i10 = z3 ? 1 : 0;
        int i11 = (i8 + i9) * 31;
        String str = this.description;
        int hashCode3 = (i11 + (str == null ? 0 : str.hashCode())) * 31;
        boolean z4 = this.isTransient;
        if (!z4) {
            i = z4 ? 1 : 0;
        }
        int i12 = (hashCode3 + i) * 31;
        String str2 = this.statusLabel;
        if (str2 != null) {
            i5 = str2.hashCode();
        }
        return i12 + i5;
    }

    public final String toString() {
        String str;
        StringBuilder sb = new StringBuilder("WifiIndicators[");
        sb.append("enabled=");
        sb.append(this.enabled);
        sb.append(",statusIcon=");
        IconState iconState = this.statusIcon;
        String str2 = "";
        if (iconState == null) {
            str = str2;
        } else {
            str = iconState.toString();
        }
        sb.append(str);
        sb.append(",qsIcon=");
        IconState iconState2 = this.qsIcon;
        if (iconState2 != null) {
            str2 = iconState2.toString();
        }
        sb.append(str2);
        sb.append(",activityIn=");
        sb.append(this.activityIn);
        sb.append(",activityOut=");
        sb.append(this.activityOut);
        sb.append(",qsDescription=");
        sb.append(this.description);
        sb.append(",isTransient=");
        sb.append(this.isTransient);
        sb.append(",statusLabel=");
        sb.append(this.statusLabel);
        sb.append(']');
        return sb.toString();
    }

    public WifiIndicators(boolean z, IconState iconState, IconState iconState2, boolean z2, boolean z3, String str, boolean z4, String str2) {
        this.enabled = z;
        this.statusIcon = iconState;
        this.qsIcon = iconState2;
        this.activityIn = z2;
        this.activityOut = z3;
        this.description = str;
        this.isTransient = z4;
        this.statusLabel = str2;
    }
}
