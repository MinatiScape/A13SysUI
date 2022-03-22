package com.android.systemui.util.animation;

import android.graphics.PointF;
/* compiled from: TransitionLayoutController.kt */
/* loaded from: classes.dex */
public final class DisappearParameters {
    public float disappearStart;
    public PointF gonePivot = new PointF(0.0f, 1.0f);
    public PointF disappearSize = new PointF(1.0f, 0.0f);
    public PointF contentTranslationFraction = new PointF(0.0f, 0.8f);
    public float disappearEnd = 1.0f;
    public float fadeStartPosition = 0.9f;

    public final boolean equals(Object obj) {
        boolean z;
        boolean z2;
        boolean z3;
        if (!(obj instanceof DisappearParameters)) {
            return false;
        }
        DisappearParameters disappearParameters = (DisappearParameters) obj;
        if (!this.disappearSize.equals(disappearParameters.disappearSize) || !this.gonePivot.equals(disappearParameters.gonePivot) || !this.contentTranslationFraction.equals(disappearParameters.contentTranslationFraction)) {
            return false;
        }
        if (this.disappearStart == disappearParameters.disappearStart) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            return false;
        }
        if (this.disappearEnd == disappearParameters.disappearEnd) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (!z2) {
            return false;
        }
        if (this.fadeStartPosition == disappearParameters.fadeStartPosition) {
            z3 = true;
        } else {
            z3 = false;
        }
        if (!z3) {
            return false;
        }
        return true;
    }

    public final int hashCode() {
        int hashCode = this.gonePivot.hashCode();
        int hashCode2 = this.contentTranslationFraction.hashCode();
        int hashCode3 = Float.hashCode(this.disappearStart);
        int hashCode4 = Float.hashCode(this.disappearEnd);
        return Float.hashCode(this.fadeStartPosition) + ((hashCode4 + ((hashCode3 + ((hashCode2 + ((hashCode + (this.disappearSize.hashCode() * 31)) * 31)) * 31)) * 31)) * 31);
    }
}
