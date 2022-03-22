package com.android.systemui.controls.management;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsModel.kt */
/* loaded from: classes.dex */
public final class ZoneNameWrapper extends ElementWrapper {
    public final CharSequence zoneName;

    public ZoneNameWrapper(CharSequence charSequence) {
        super(0);
        this.zoneName = charSequence;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof ZoneNameWrapper) && Intrinsics.areEqual(this.zoneName, ((ZoneNameWrapper) obj).zoneName);
    }

    public final int hashCode() {
        return this.zoneName.hashCode();
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ZoneNameWrapper(zoneName=");
        m.append((Object) this.zoneName);
        m.append(')');
        return m.toString();
    }
}
