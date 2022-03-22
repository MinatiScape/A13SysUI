package com.android.systemui.controls.management;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
/* compiled from: ControlsModel.kt */
/* loaded from: classes.dex */
public final class DividerWrapper extends ElementWrapper {
    public boolean showDivider;
    public boolean showNone;

    public DividerWrapper() {
        this(0);
    }

    public DividerWrapper(int i) {
        super(0);
        this.showNone = false;
        this.showDivider = false;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DividerWrapper)) {
            return false;
        }
        DividerWrapper dividerWrapper = (DividerWrapper) obj;
        return this.showNone == dividerWrapper.showNone && this.showDivider == dividerWrapper.showDivider;
    }

    public final int hashCode() {
        boolean z = this.showNone;
        int i = 1;
        if (z) {
            z = true;
        }
        int i2 = z ? 1 : 0;
        int i3 = z ? 1 : 0;
        int i4 = i2 * 31;
        boolean z2 = this.showDivider;
        if (!z2) {
            i = z2 ? 1 : 0;
        }
        return i4 + i;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("DividerWrapper(showNone=");
        m.append(this.showNone);
        m.append(", showDivider=");
        return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(m, this.showDivider, ')');
    }
}
