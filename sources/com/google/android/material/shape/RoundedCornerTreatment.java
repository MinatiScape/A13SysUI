package com.google.android.material.shape;

import androidx.leanback.R$drawable;
/* loaded from: classes.dex */
public final class RoundedCornerTreatment extends R$drawable {
    @Override // androidx.leanback.R$drawable
    public final void getCornerPath(ShapePath shapePath, float f, float f2) {
        shapePath.reset(0.0f, f2 * f, 180.0f, 90.0f);
        float f3 = f2 * 2.0f * f;
        shapePath.addArc(0.0f, 0.0f, f3, f3, 180.0f, 90.0f);
    }
}
