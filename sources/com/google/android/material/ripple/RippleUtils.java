package com.google.android.material.ripple;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.StateSet;
import androidx.core.graphics.ColorUtils;
/* loaded from: classes.dex */
public final class RippleUtils {
    public static final String TRANSPARENT_DEFAULT_COLOR_WARNING = "Use a non-transparent color for the default color as it will be used to finish ripple animations.";
    public static final int[] PRESSED_STATE_SET = {16842919};
    public static final int[] SELECTED_PRESSED_STATE_SET = {16842913, 16842919};
    public static final int[] SELECTED_STATE_SET = {16842913};
    public static final String LOG_TAG = "RippleUtils";

    public static ColorStateList convertToRippleDrawableColor(ColorStateList colorStateList) {
        return new ColorStateList(new int[][]{SELECTED_STATE_SET, StateSet.NOTHING}, new int[]{getColorForState(colorStateList, SELECTED_PRESSED_STATE_SET), getColorForState(colorStateList, PRESSED_STATE_SET)});
    }

    public static boolean shouldDrawRippleCompat(int[] iArr) {
        boolean z = false;
        boolean z2 = false;
        for (int i : iArr) {
            if (i == 16842910) {
                z = true;
            } else if (i == 16842908 || i == 16842919 || i == 16843623) {
                z2 = true;
            }
        }
        if (!z || !z2) {
            return false;
        }
        return true;
    }

    public static int getColorForState(ColorStateList colorStateList, int[] iArr) {
        int i;
        if (colorStateList != null) {
            i = colorStateList.getColorForState(iArr, colorStateList.getDefaultColor());
        } else {
            i = 0;
        }
        return ColorUtils.setAlphaComponent(i, Math.min(Color.alpha(i) * 2, 255));
    }

    public static ColorStateList sanitizeRippleDrawableColor(ColorStateList colorStateList) {
        if (colorStateList != null) {
            return colorStateList;
        }
        return ColorStateList.valueOf(0);
    }
}
