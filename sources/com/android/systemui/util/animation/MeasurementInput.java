package com.android.systemui.util.animation;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.core.graphics.Insets$$ExternalSyntheticOutline0;
/* compiled from: MeasurementInput.kt */
/* loaded from: classes.dex */
public final class MeasurementInput {
    public int heightMeasureSpec;
    public int widthMeasureSpec;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MeasurementInput)) {
            return false;
        }
        MeasurementInput measurementInput = (MeasurementInput) obj;
        return this.widthMeasureSpec == measurementInput.widthMeasureSpec && this.heightMeasureSpec == measurementInput.heightMeasureSpec;
    }

    public final int hashCode() {
        return Integer.hashCode(this.heightMeasureSpec) + (Integer.hashCode(this.widthMeasureSpec) * 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("MeasurementInput(widthMeasureSpec=");
        m.append(this.widthMeasureSpec);
        m.append(", heightMeasureSpec=");
        return Insets$$ExternalSyntheticOutline0.m(m, this.heightMeasureSpec, ')');
    }

    public MeasurementInput(int i, int i2) {
        this.widthMeasureSpec = i;
        this.heightMeasureSpec = i2;
    }
}
