package com.google.android.material.shape;

import androidx.leanback.R$fraction;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MarkerEdgeTreatment extends R$fraction {
    public final float radius;

    @Override // androidx.leanback.R$fraction
    public final void getEdgePath(float f, float f2, float f3, ShapePath shapePath) {
        float sqrt = (float) ((Math.sqrt(2.0d) * this.radius) / 2.0d);
        float sqrt2 = (float) Math.sqrt(Math.pow(this.radius, 2.0d) - Math.pow(sqrt, 2.0d));
        double sqrt3 = Math.sqrt(2.0d);
        Objects.requireNonNull(shapePath);
        shapePath.reset(f2 - sqrt, ((float) (-((sqrt3 * this.radius) - this.radius))) + sqrt2, 270.0f, 0.0f);
        shapePath.lineTo(f2, (float) (-((Math.sqrt(2.0d) * this.radius) - this.radius)));
        shapePath.lineTo(f2 + sqrt, ((float) (-((Math.sqrt(2.0d) * this.radius) - this.radius))) + sqrt2);
    }

    public MarkerEdgeTreatment(float f) {
        this.radius = f - 0.001f;
    }
}
