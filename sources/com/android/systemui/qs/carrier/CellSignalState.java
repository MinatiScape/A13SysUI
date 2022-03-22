package com.android.systemui.qs.carrier;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
import com.android.keyguard.FontInterpolator$VarFontKey$$ExternalSyntheticOutline0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: CellSignalState.kt */
/* loaded from: classes.dex */
public final class CellSignalState {
    public final String contentDescription;
    public final int mobileSignalIconId;
    public final boolean providerModelBehavior;
    public final boolean roaming;
    public final String typeContentDescription;
    public final boolean visible;

    public CellSignalState() {
        this(false, 0, null, null, false, false);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CellSignalState)) {
            return false;
        }
        CellSignalState cellSignalState = (CellSignalState) obj;
        return this.visible == cellSignalState.visible && this.mobileSignalIconId == cellSignalState.mobileSignalIconId && Intrinsics.areEqual(this.contentDescription, cellSignalState.contentDescription) && Intrinsics.areEqual(this.typeContentDescription, cellSignalState.typeContentDescription) && this.roaming == cellSignalState.roaming && this.providerModelBehavior == cellSignalState.providerModelBehavior;
    }

    public CellSignalState(boolean z, int i, String str, String str2, boolean z2, boolean z3) {
        this.visible = z;
        this.mobileSignalIconId = i;
        this.contentDescription = str;
        this.typeContentDescription = str2;
        this.roaming = z2;
        this.providerModelBehavior = z3;
    }

    public final CellSignalState changeVisibility(boolean z) {
        if (this.visible == z) {
            return this;
        }
        return new CellSignalState(z, this.mobileSignalIconId, this.contentDescription, this.typeContentDescription, this.roaming, this.providerModelBehavior);
    }

    public final int hashCode() {
        int i;
        boolean z = this.visible;
        int i2 = 1;
        if (z) {
            z = true;
        }
        int i3 = z ? 1 : 0;
        int i4 = z ? 1 : 0;
        int m = FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.mobileSignalIconId, i3 * 31, 31);
        String str = this.contentDescription;
        int i5 = 0;
        if (str == null) {
            i = 0;
        } else {
            i = str.hashCode();
        }
        int i6 = (m + i) * 31;
        String str2 = this.typeContentDescription;
        if (str2 != null) {
            i5 = str2.hashCode();
        }
        int i7 = (i6 + i5) * 31;
        boolean z2 = this.roaming;
        if (z2) {
            z2 = true;
        }
        int i8 = z2 ? 1 : 0;
        int i9 = z2 ? 1 : 0;
        int i10 = (i7 + i8) * 31;
        boolean z3 = this.providerModelBehavior;
        if (!z3) {
            i2 = z3 ? 1 : 0;
        }
        return i10 + i2;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("CellSignalState(visible=");
        m.append(this.visible);
        m.append(", mobileSignalIconId=");
        m.append(this.mobileSignalIconId);
        m.append(", contentDescription=");
        m.append((Object) this.contentDescription);
        m.append(", typeContentDescription=");
        m.append((Object) this.typeContentDescription);
        m.append(", roaming=");
        m.append(this.roaming);
        m.append(", providerModelBehavior=");
        return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(m, this.providerModelBehavior, ')');
    }
}
