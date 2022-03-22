package androidx.leanback;

import android.graphics.Color;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import com.google.android.material.resources.MaterialAttributes;
/* loaded from: classes.dex */
public final class R$string {
    public static final int[] RestrictedPreference = {2130970088, 2130970093};
    public static final int[] RestrictedSwitchPreference = {2130969659, 2130970087};

    public static int compositeARGBWithAlpha(int i, int i2) {
        return ColorUtils.setAlphaComponent(i, (Color.alpha(i) * i2) / 255);
    }

    public static int getColor(View view, int i) {
        return MaterialAttributes.resolveOrThrow(view.getContext(), i, view.getClass().getCanonicalName());
    }

    public static int layer(int i, int i2, float f) {
        return ColorUtils.compositeColors(ColorUtils.setAlphaComponent(i2, Math.round(Color.alpha(i2) * f)), i);
    }
}
