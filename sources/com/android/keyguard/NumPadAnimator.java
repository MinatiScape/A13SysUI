package com.android.keyguard;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.view.ContextThemeWrapper;
import com.android.systemui.animation.Interpolators;
/* loaded from: classes.dex */
public final class NumPadAnimator {
    public AnimatorSet mAnimator = new AnimatorSet();
    public GradientDrawable mBackground;
    public ValueAnimator mContractAnimator;
    public ValueAnimator mExpandAnimator;
    public int mHighlightColor;
    public int mNormalColor;
    public RippleDrawable mRipple;
    public int mStyle;

    public final void onLayout(int i) {
        float f = i;
        float f2 = f / 2.0f;
        float f3 = f / 4.0f;
        this.mBackground.setCornerRadius(f2);
        this.mExpandAnimator.setFloatValues(f2, f3);
        this.mContractAnimator.setFloatValues(f3, f2);
    }

    public final void reloadColors(Context context) {
        int i;
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, this.mStyle);
        TypedArray obtainStyledAttributes = contextThemeWrapper.obtainStyledAttributes(new int[]{16843817, 16843820});
        if (obtainStyledAttributes.hasValue(0)) {
            i = obtainStyledAttributes.getColor(0, 0);
        } else {
            TypedArray obtainStyledAttributes2 = contextThemeWrapper.obtainStyledAttributes(new int[]{17956909});
            int color = obtainStyledAttributes2.getColor(0, 0);
            obtainStyledAttributes2.recycle();
            i = color;
        }
        this.mNormalColor = i;
        this.mHighlightColor = obtainStyledAttributes.getColor(1, 0);
        obtainStyledAttributes.recycle();
        this.mBackground.setColor(this.mNormalColor);
        this.mRipple.setColor(ColorStateList.valueOf(this.mHighlightColor));
    }

    public NumPadAnimator(Context context, RippleDrawable rippleDrawable, int i) {
        this.mStyle = i;
        RippleDrawable rippleDrawable2 = (RippleDrawable) rippleDrawable.mutate();
        this.mRipple = rippleDrawable2;
        this.mBackground = (GradientDrawable) rippleDrawable2.findDrawableByLayerId(2131427539);
        reloadColors(context);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mExpandAnimator = ofFloat;
        ofFloat.setDuration(50L);
        this.mExpandAnimator.setInterpolator(Interpolators.LINEAR);
        this.mExpandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.keyguard.NumPadAnimator.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                NumPadAnimator.this.mBackground.setCornerRadius(((Float) valueAnimator.getAnimatedValue()).floatValue());
                NumPadAnimator.this.mRipple.invalidateSelf();
            }
        });
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(1.0f, 0.0f);
        this.mContractAnimator = ofFloat2;
        ofFloat2.setStartDelay(33L);
        this.mContractAnimator.setDuration(417L);
        this.mContractAnimator.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        this.mContractAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.keyguard.NumPadAnimator.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                NumPadAnimator.this.mBackground.setCornerRadius(((Float) valueAnimator.getAnimatedValue()).floatValue());
                NumPadAnimator.this.mRipple.invalidateSelf();
            }
        });
        this.mAnimator.playSequentially(this.mExpandAnimator, this.mContractAnimator);
    }
}
