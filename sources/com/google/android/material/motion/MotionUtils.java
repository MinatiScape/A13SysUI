package com.google.android.material.motion;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.TypedValue;
import android.view.animation.PathInterpolator;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import androidx.core.graphics.PathParser;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
/* loaded from: classes.dex */
public final class MotionUtils {
    public static float getControlPoint(String[] strArr, int i) {
        float parseFloat = Float.parseFloat(strArr[i]);
        if (parseFloat >= 0.0f && parseFloat <= 1.0f) {
            return parseFloat;
        }
        throw new IllegalArgumentException("Motion easing control point value must be between 0 and 1; instead got: " + parseFloat);
    }

    public static boolean isEasingType(String str, String str2) {
        if (!str.startsWith(str2 + "(") || !str.endsWith(")")) {
            return false;
        }
        return true;
    }

    public static TimeInterpolator resolveThemeInterpolator(Context context, FastOutSlowInInterpolator fastOutSlowInInterpolator) {
        TypedValue typedValue = new TypedValue();
        if (!context.getTheme().resolveAttribute(2130969498, typedValue, true)) {
            return fastOutSlowInInterpolator;
        }
        if (typedValue.type == 3) {
            String valueOf = String.valueOf(typedValue.string);
            if (isEasingType(valueOf, "cubic-bezier")) {
                String[] split = valueOf.substring(13, valueOf.length() - 1).split(",");
                if (split.length == 4) {
                    return new PathInterpolator(getControlPoint(split, 0), getControlPoint(split, 1), getControlPoint(split, 2), getControlPoint(split, 3));
                }
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Motion easing theme attribute must have 4 control points if using bezier curve format; instead got: ");
                m.append(split.length);
                throw new IllegalArgumentException(m.toString());
            } else if (isEasingType(valueOf, "path")) {
                return new PathInterpolator(PathParser.createPathFromPathData(valueOf.substring(5, valueOf.length() - 1)));
            } else {
                throw new IllegalArgumentException(SupportMenuInflater$$ExternalSyntheticOutline0.m("Invalid motion easing type: ", valueOf));
            }
        } else {
            throw new IllegalArgumentException("Motion easing theme attribute must be a string");
        }
    }
}
