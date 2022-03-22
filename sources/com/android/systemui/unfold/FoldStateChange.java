package com.android.systemui.unfold;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.keyguard.FontInterpolator$VarFontKey$$ExternalSyntheticOutline0;
/* compiled from: FoldStateLoggingProvider.kt */
/* loaded from: classes.dex */
public final class FoldStateChange {
    public final int current;
    public final long dtMillis;
    public final int previous;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FoldStateChange)) {
            return false;
        }
        FoldStateChange foldStateChange = (FoldStateChange) obj;
        return this.previous == foldStateChange.previous && this.current == foldStateChange.current && this.dtMillis == foldStateChange.dtMillis;
    }

    public final int hashCode() {
        return Long.hashCode(this.dtMillis) + FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.current, Integer.hashCode(this.previous) * 31, 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("FoldStateChange(previous=");
        m.append(this.previous);
        m.append(", current=");
        m.append(this.current);
        m.append(", dtMillis=");
        m.append(this.dtMillis);
        m.append(')');
        return m.toString();
    }

    public FoldStateChange(int i, int i2, long j) {
        this.previous = i;
        this.current = i2;
        this.dtMillis = j;
    }
}
