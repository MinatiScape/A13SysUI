package com.android.systemui.shared.rotation;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.core.graphics.Insets$$ExternalSyntheticOutline0;
import com.android.keyguard.FontInterpolator$VarFontKey$$ExternalSyntheticOutline0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: FloatingRotationButtonPositionCalculator.kt */
/* loaded from: classes.dex */
public final class FloatingRotationButtonPositionCalculator {
    public final int defaultMargin;
    public final int taskbarMarginBottom;
    public final int taskbarMarginLeft;

    /* compiled from: FloatingRotationButtonPositionCalculator.kt */
    /* loaded from: classes.dex */
    public static final class Position {
        public final int gravity;
        public final int translationX;
        public final int translationY;

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Position)) {
                return false;
            }
            Position position = (Position) obj;
            return this.gravity == position.gravity && this.translationX == position.translationX && this.translationY == position.translationY;
        }

        public final int hashCode() {
            return Integer.hashCode(this.translationY) + FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.translationX, Integer.hashCode(this.gravity) * 31, 31);
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Position(gravity=");
            m.append(this.gravity);
            m.append(", translationX=");
            m.append(this.translationX);
            m.append(", translationY=");
            return Insets$$ExternalSyntheticOutline0.m(m, this.translationY, ')');
        }

        public Position(int i, int i2, int i3) {
            this.gravity = i;
            this.translationX = i2;
            this.translationY = i3;
        }
    }

    public final Position calculatePosition(int i, boolean z, boolean z2) {
        boolean z3;
        int i2;
        int i3;
        int i4;
        boolean z4 = false;
        if (i == 0 || i == 1) {
            z3 = true;
        } else {
            z3 = false;
        }
        if (z3 && z && !z2) {
            z4 = true;
        }
        if (i == 0) {
            i2 = 83;
        } else if (i == 1) {
            i2 = 85;
        } else if (i == 2) {
            i2 = 53;
        } else if (i == 3) {
            i2 = 51;
        } else {
            throw new IllegalArgumentException(Intrinsics.stringPlus("Invalid rotation ", Integer.valueOf(i)));
        }
        if (z4) {
            i3 = this.taskbarMarginLeft;
        } else {
            i3 = this.defaultMargin;
        }
        if (z4) {
            i4 = this.taskbarMarginBottom;
        } else {
            i4 = this.defaultMargin;
        }
        if ((i2 & 5) == 5) {
            i3 = -i3;
        }
        if ((i2 & 80) == 80) {
            i4 = -i4;
        }
        return new Position(i2, i3, i4);
    }

    public FloatingRotationButtonPositionCalculator(int i, int i2, int i3) {
        this.defaultMargin = i;
        this.taskbarMarginLeft = i2;
        this.taskbarMarginBottom = i3;
    }
}
