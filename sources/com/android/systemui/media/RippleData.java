package com.android.systemui.media;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: LightSourceDrawable.kt */
/* loaded from: classes.dex */
public final class RippleData {
    public float x = 0.0f;
    public float y = 0.0f;
    public float alpha = 0.0f;
    public float progress = 0.0f;
    public float minSize = 0.0f;
    public float maxSize = 0.0f;
    public float highlight = 0.0f;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RippleData)) {
            return false;
        }
        RippleData rippleData = (RippleData) obj;
        return Intrinsics.areEqual(Float.valueOf(this.x), Float.valueOf(rippleData.x)) && Intrinsics.areEqual(Float.valueOf(this.y), Float.valueOf(rippleData.y)) && Intrinsics.areEqual(Float.valueOf(this.alpha), Float.valueOf(rippleData.alpha)) && Intrinsics.areEqual(Float.valueOf(this.progress), Float.valueOf(rippleData.progress)) && Intrinsics.areEqual(Float.valueOf(this.minSize), Float.valueOf(rippleData.minSize)) && Intrinsics.areEqual(Float.valueOf(this.maxSize), Float.valueOf(rippleData.maxSize)) && Intrinsics.areEqual(Float.valueOf(this.highlight), Float.valueOf(rippleData.highlight));
    }

    public final int hashCode() {
        int hashCode = Float.hashCode(this.y);
        int hashCode2 = Float.hashCode(this.alpha);
        int hashCode3 = Float.hashCode(this.progress);
        int hashCode4 = Float.hashCode(this.minSize);
        int hashCode5 = Float.hashCode(this.maxSize);
        return Float.hashCode(this.highlight) + ((hashCode5 + ((hashCode4 + ((hashCode3 + ((hashCode2 + ((hashCode + (Float.hashCode(this.x) * 31)) * 31)) * 31)) * 31)) * 31)) * 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("RippleData(x=");
        m.append(this.x);
        m.append(", y=");
        m.append(this.y);
        m.append(", alpha=");
        m.append(this.alpha);
        m.append(", progress=");
        m.append(this.progress);
        m.append(", minSize=");
        m.append(this.minSize);
        m.append(", maxSize=");
        m.append(this.maxSize);
        m.append(", highlight=");
        m.append(this.highlight);
        m.append(')');
        return m.toString();
    }
}
