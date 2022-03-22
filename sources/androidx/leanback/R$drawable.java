package androidx.leanback;

import com.google.android.material.shape.ShapePath;
/* loaded from: classes.dex */
public class R$drawable {
    public void getCornerPath(ShapePath shapePath, float f, float f2) {
        throw null;
    }

    public static final int roundToInt(float f) {
        if (!Float.isNaN(f)) {
            return Math.round(f);
        }
        throw new IllegalArgumentException("Cannot round NaN value.");
    }
}
