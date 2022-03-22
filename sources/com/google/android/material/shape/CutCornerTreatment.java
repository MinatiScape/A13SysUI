package com.google.android.material.shape;

import androidx.leanback.R$drawable;
/* loaded from: classes.dex */
public final class CutCornerTreatment extends R$drawable {
    @Override // androidx.leanback.R$drawable
    public final void getCornerPath(ShapePath shapePath, float f, float f2) {
        shapePath.reset(0.0f, f2 * f, 180.0f, 90.0f);
        double d = f2;
        double d2 = f;
        shapePath.lineTo((float) (Math.sin(Math.toRadians(90.0f)) * d * d2), (float) (Math.sin(Math.toRadians(0.0f)) * d * d2));
    }
}
