package androidx.leanback.transition;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import androidx.leanback.widget.Parallax;
import androidx.leanback.widget.ParallaxEffect;
import androidx.leanback.widget.ParallaxTarget;
import java.util.Objects;
/* loaded from: classes.dex */
public class ParallaxTransition extends Visibility {
    public static LinearInterpolator sInterpolator = new LinearInterpolator();

    public ParallaxTransition() {
    }

    public ParallaxTransition(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.transition.Visibility
    public final Animator onAppear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        if (transitionValues2 == null) {
            return null;
        }
        return createAnimator(view);
    }

    @Override // android.transition.Visibility
    public final Animator onDisappear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        if (transitionValues == null) {
            return null;
        }
        return createAnimator(view);
    }

    public static ValueAnimator createAnimator(View view) {
        final Parallax parallax = (Parallax) view.getTag(2131428228);
        if (parallax == null) {
            return null;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setInterpolator(sInterpolator);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: androidx.leanback.transition.ParallaxTransition.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                Parallax parallax2 = Parallax.this;
                Objects.requireNonNull(parallax2);
                for (int i = 0; i < parallax2.mEffects.size(); i++) {
                    ParallaxEffect parallaxEffect = (ParallaxEffect) parallax2.mEffects.get(i);
                    Objects.requireNonNull(parallaxEffect);
                    if (parallaxEffect.mMarkerValues.size() >= 2) {
                        if (parallax2.mProperties.size() >= 2) {
                            float f = parallax2.mFloatValues[0];
                            int i2 = 1;
                            while (i2 < parallax2.mProperties.size()) {
                                float f2 = parallax2.mFloatValues[i2];
                                if (f2 < f) {
                                    int i3 = i2 - 1;
                                    throw new IllegalStateException(String.format("Parallax Property[%d]\"%s\" is smaller than Property[%d]\"%s\"", Integer.valueOf(i2), ((Property) parallax2.mProperties.get(i2)).getName(), Integer.valueOf(i3), ((Property) parallax2.mProperties.get(i3)).getName()));
                                } else if (f == -3.4028235E38f && f2 == Float.MAX_VALUE) {
                                    int i4 = i2 - 1;
                                    throw new IllegalStateException(String.format("Parallax Property[%d]\"%s\" is UNKNOWN_BEFORE and Property[%d]\"%s\" is UNKNOWN_AFTER", Integer.valueOf(i4), ((Property) parallax2.mProperties.get(i4)).getName(), Integer.valueOf(i2), ((Property) parallax2.mProperties.get(i2)).getName()));
                                } else {
                                    i2++;
                                    f = f2;
                                }
                            }
                        }
                        boolean z = false;
                        for (int i5 = 0; i5 < parallaxEffect.mTargets.size(); i5++) {
                            Objects.requireNonNull((ParallaxTarget) parallaxEffect.mTargets.get(i5));
                            if (!z) {
                                parallaxEffect.calculateFraction();
                                z = true;
                            }
                        }
                    }
                }
            }
        });
        return ofFloat;
    }
}
