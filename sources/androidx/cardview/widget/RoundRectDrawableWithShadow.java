package androidx.cardview.widget;

import android.graphics.drawable.Drawable;
/* loaded from: classes.dex */
public final class RoundRectDrawableWithShadow extends Drawable {
    public static final /* synthetic */ int $r8$clinit = 0;
    public static final double COS_45 = Math.cos(Math.toRadians(45.0d));

    public static float calculateVerticalPadding(float f, float f2, boolean z) {
        if (!z) {
            return f * 1.5f;
        }
        return (float) (((1.0d - COS_45) * f2) + (f * 1.5f));
    }
}
