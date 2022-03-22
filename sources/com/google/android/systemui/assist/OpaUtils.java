package com.google.android.systemui.assist;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.util.ArraySet;
import android.view.RenderNodeAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;
/* loaded from: classes.dex */
public final class OpaUtils {
    public static final PathInterpolator INTERPOLATOR_40_40 = new PathInterpolator(0.4f, 0.0f, 0.6f, 1.0f);
    public static final PathInterpolator INTERPOLATOR_40_OUT = new PathInterpolator(0.4f, 0.0f, 1.0f, 1.0f);

    public static ObjectAnimator getScaleObjectAnimator(View view, float f, int i, PathInterpolator pathInterpolator) {
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat(View.SCALE_X, f), PropertyValuesHolder.ofFloat(View.SCALE_Y, f));
        ofPropertyValuesHolder.setDuration(i);
        ofPropertyValuesHolder.setInterpolator(pathInterpolator);
        return ofPropertyValuesHolder;
    }

    public static ObjectAnimator getAlphaObjectAnimator(View view, int i, LinearInterpolator linearInterpolator) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, View.ALPHA, 1.0f);
        ofFloat.setInterpolator(linearInterpolator);
        ofFloat.setDuration(50);
        ofFloat.setStartDelay(i);
        return ofFloat;
    }

    public static RenderNodeAnimator getTranslationAnimatorX(View view, PathInterpolator pathInterpolator, int i) {
        RenderNodeAnimator renderNodeAnimator = new RenderNodeAnimator(0, 0.0f);
        renderNodeAnimator.setTarget(view);
        renderNodeAnimator.setInterpolator(pathInterpolator);
        renderNodeAnimator.setDuration(i);
        return renderNodeAnimator;
    }

    public static ObjectAnimator getTranslationObjectAnimatorX(View view, PathInterpolator pathInterpolator, float f, float f2, int i) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, View.X, f2, f2 + f);
        ofFloat.setInterpolator(pathInterpolator);
        ofFloat.setDuration(i);
        return ofFloat;
    }

    public static ObjectAnimator getTranslationObjectAnimatorY(View view, PathInterpolator pathInterpolator, float f, float f2) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, View.Y, f2, f2 + f);
        ofFloat.setInterpolator(pathInterpolator);
        ofFloat.setDuration(350);
        return ofFloat;
    }

    public static Animator getLongestAnim(ArraySet<Animator> arraySet) {
        long j = Long.MIN_VALUE;
        Animator animator = null;
        for (int size = arraySet.size() - 1; size >= 0; size--) {
            Animator valueAt = arraySet.valueAt(size);
            if (valueAt.getTotalDuration() > j) {
                j = valueAt.getTotalDuration();
                animator = valueAt;
            }
        }
        return animator;
    }
}
