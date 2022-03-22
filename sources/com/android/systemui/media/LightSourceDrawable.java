package com.android.systemui.media;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.view.animation.PathInterpolator;
import androidx.annotation.Keep;
import androidx.recyclerview.R$styleable;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.animation.Interpolators;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParser;
/* compiled from: LightSourceDrawable.kt */
@Keep
/* loaded from: classes.dex */
public final class LightSourceDrawable extends Drawable {
    private boolean active;
    private boolean pressed;
    private Animator rippleAnimation;
    private int[] themeAttrs;
    private final RippleData rippleData = new RippleData();
    private Paint paint = new Paint();
    private int highlightColor = -1;

    private final void updateStateFromTypedArray(TypedArray typedArray) {
        if (typedArray.hasValue(3)) {
            RippleData rippleData = this.rippleData;
            float dimension = typedArray.getDimension(3, 0.0f);
            Objects.requireNonNull(rippleData);
            rippleData.minSize = dimension;
        }
        if (typedArray.hasValue(2)) {
            RippleData rippleData2 = this.rippleData;
            float dimension2 = typedArray.getDimension(2, 0.0f);
            Objects.requireNonNull(rippleData2);
            rippleData2.maxSize = dimension2;
        }
        if (typedArray.hasValue(1)) {
            RippleData rippleData3 = this.rippleData;
            Objects.requireNonNull(rippleData3);
            rippleData3.highlight = typedArray.getInteger(1, 0) / 100.0f;
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    @Override // android.graphics.drawable.Drawable
    public void getOutline(Outline outline) {
    }

    public boolean hasFocusStateSpecified() {
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isProjected() {
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        return true;
    }

    private final void illuminate() {
        RippleData rippleData = this.rippleData;
        Objects.requireNonNull(rippleData);
        rippleData.alpha = 1.0f;
        invalidateSelf();
        Animator animator = this.rippleAnimation;
        if (animator != null) {
            animator.cancel();
        }
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
        ofFloat.setStartDelay(133L);
        ofFloat.setDuration(800 - ofFloat.getStartDelay());
        PathInterpolator pathInterpolator = Interpolators.LINEAR_OUT_SLOW_IN;
        ofFloat.setInterpolator(pathInterpolator);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.media.LightSourceDrawable$illuminate$1$1$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                RippleData rippleData2;
                rippleData2 = LightSourceDrawable.this.rippleData;
                Object animatedValue = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                float floatValue = ((Float) animatedValue).floatValue();
                Objects.requireNonNull(rippleData2);
                rippleData2.alpha = floatValue;
                LightSourceDrawable.this.invalidateSelf();
            }
        });
        RippleData rippleData2 = this.rippleData;
        Objects.requireNonNull(rippleData2);
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(rippleData2.progress, 1.0f);
        ofFloat2.setDuration(800L);
        ofFloat2.setInterpolator(pathInterpolator);
        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.media.LightSourceDrawable$illuminate$1$2$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                RippleData rippleData3;
                rippleData3 = LightSourceDrawable.this.rippleData;
                Object animatedValue = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                float floatValue = ((Float) animatedValue).floatValue();
                Objects.requireNonNull(rippleData3);
                rippleData3.progress = floatValue;
                LightSourceDrawable.this.invalidateSelf();
            }
        });
        animatorSet.playTogether(ofFloat, ofFloat2);
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.media.LightSourceDrawable$illuminate$1$3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator2) {
                RippleData rippleData3;
                rippleData3 = LightSourceDrawable.this.rippleData;
                Objects.requireNonNull(rippleData3);
                rippleData3.progress = 0.0f;
                LightSourceDrawable.this.rippleAnimation = null;
                LightSourceDrawable.this.invalidateSelf();
            }
        });
        animatorSet.start();
        this.rippleAnimation = animatorSet;
    }

    private final void setActive(boolean z) {
        if (z != this.active) {
            this.active = z;
            if (z) {
                Animator animator = this.rippleAnimation;
                if (animator != null) {
                    animator.cancel();
                }
                RippleData rippleData = this.rippleData;
                Objects.requireNonNull(rippleData);
                rippleData.alpha = 1.0f;
                RippleData rippleData2 = this.rippleData;
                Objects.requireNonNull(rippleData2);
                rippleData2.progress = 0.05f;
            } else {
                Animator animator2 = this.rippleAnimation;
                if (animator2 != null) {
                    animator2.cancel();
                }
                RippleData rippleData3 = this.rippleData;
                Objects.requireNonNull(rippleData3);
                ValueAnimator ofFloat = ValueAnimator.ofFloat(rippleData3.alpha, 0.0f);
                ofFloat.setDuration(200L);
                ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.media.LightSourceDrawable$active$1$1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        RippleData rippleData4;
                        rippleData4 = LightSourceDrawable.this.rippleData;
                        Object animatedValue = valueAnimator.getAnimatedValue();
                        Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                        float floatValue = ((Float) animatedValue).floatValue();
                        Objects.requireNonNull(rippleData4);
                        rippleData4.alpha = floatValue;
                        LightSourceDrawable.this.invalidateSelf();
                    }
                });
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.media.LightSourceDrawable$active$1$2
                    public boolean cancelled;

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationCancel(Animator animator3) {
                        this.cancelled = true;
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator3) {
                        RippleData rippleData4;
                        RippleData rippleData5;
                        if (!this.cancelled) {
                            rippleData4 = LightSourceDrawable.this.rippleData;
                            Objects.requireNonNull(rippleData4);
                            rippleData4.progress = 0.0f;
                            rippleData5 = LightSourceDrawable.this.rippleData;
                            Objects.requireNonNull(rippleData5);
                            rippleData5.alpha = 0.0f;
                            LightSourceDrawable.this.rippleAnimation = null;
                            LightSourceDrawable.this.invalidateSelf();
                        }
                    }
                });
                ofFloat.start();
                this.rippleAnimation = ofFloat;
            }
            invalidateSelf();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:5:0x0008, code lost:
        if (r0.length <= 0) goto L_0x000a;
     */
    @Override // android.graphics.drawable.Drawable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean canApplyTheme() {
        /*
            r1 = this;
            int[] r0 = r1.themeAttrs
            if (r0 == 0) goto L_0x000a
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            int r0 = r0.length
            if (r0 > 0) goto L_0x0010
        L_0x000a:
            boolean r1 = super.canApplyTheme()
            if (r1 == 0) goto L_0x0012
        L_0x0010:
            r1 = 1
            goto L_0x0013
        L_0x0012:
            r1 = 0
        L_0x0013:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.LightSourceDrawable.canApplyTheme():boolean");
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        RippleData rippleData = this.rippleData;
        Objects.requireNonNull(rippleData);
        float f = rippleData.minSize;
        RippleData rippleData2 = this.rippleData;
        Objects.requireNonNull(rippleData2);
        float f2 = rippleData2.maxSize;
        RippleData rippleData3 = this.rippleData;
        Objects.requireNonNull(rippleData3);
        float lerp = MathUtils.lerp(f, f2, rippleData3.progress);
        int i = this.highlightColor;
        RippleData rippleData4 = this.rippleData;
        Objects.requireNonNull(rippleData4);
        int alphaComponent = ColorUtils.setAlphaComponent(i, (int) (rippleData4.alpha * 255));
        Paint paint = this.paint;
        RippleData rippleData5 = this.rippleData;
        Objects.requireNonNull(rippleData5);
        float f3 = rippleData5.x;
        RippleData rippleData6 = this.rippleData;
        Objects.requireNonNull(rippleData6);
        paint.setShader(new RadialGradient(f3, rippleData6.y, lerp, new int[]{alphaComponent, 0}, R$styleable.GRADIENT_STOPS, Shader.TileMode.CLAMP));
        RippleData rippleData7 = this.rippleData;
        Objects.requireNonNull(rippleData7);
        float f4 = rippleData7.x;
        RippleData rippleData8 = this.rippleData;
        Objects.requireNonNull(rippleData8);
        canvas.drawCircle(f4, rippleData8.y, lerp, this.paint);
    }

    @Override // android.graphics.drawable.Drawable
    public Rect getDirtyBounds() {
        RippleData rippleData = this.rippleData;
        Objects.requireNonNull(rippleData);
        float f = rippleData.minSize;
        RippleData rippleData2 = this.rippleData;
        Objects.requireNonNull(rippleData2);
        float f2 = rippleData2.maxSize;
        RippleData rippleData3 = this.rippleData;
        Objects.requireNonNull(rippleData3);
        float lerp = MathUtils.lerp(f, f2, rippleData3.progress);
        RippleData rippleData4 = this.rippleData;
        Objects.requireNonNull(rippleData4);
        RippleData rippleData5 = this.rippleData;
        Objects.requireNonNull(rippleData5);
        RippleData rippleData6 = this.rippleData;
        Objects.requireNonNull(rippleData6);
        RippleData rippleData7 = this.rippleData;
        Objects.requireNonNull(rippleData7);
        Rect rect = new Rect((int) (rippleData4.x - lerp), (int) (rippleData5.y - lerp), (int) (rippleData6.x + lerp), (int) (rippleData7.y + lerp));
        rect.union(super.getDirtyBounds());
        return rect;
    }

    @Override // android.graphics.drawable.Drawable
    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
        TypedArray obtainAttributes = Drawable.obtainAttributes(resources, theme, attributeSet, com.android.systemui.R$styleable.IlluminationDrawable);
        this.themeAttrs = obtainAttributes.extractThemeAttrs();
        updateStateFromTypedArray(obtainAttributes);
        obtainAttributes.recycle();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        if (i != this.paint.getAlpha()) {
            this.paint.setAlpha(i);
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        throw new UnsupportedOperationException("Color filters are not supported");
    }

    public final void setHighlightColor(int i) {
        if (this.highlightColor != i) {
            this.highlightColor = i;
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setHotspot(float f, float f2) {
        RippleData rippleData = this.rippleData;
        Objects.requireNonNull(rippleData);
        rippleData.x = f;
        RippleData rippleData2 = this.rippleData;
        Objects.requireNonNull(rippleData2);
        rippleData2.y = f2;
        if (this.active) {
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void applyTheme(Resources.Theme theme) {
        super.applyTheme(theme);
        int[] iArr = this.themeAttrs;
        if (iArr != null) {
            TypedArray resolveAttributes = theme.resolveAttributes(iArr, com.android.systemui.R$styleable.IlluminationDrawable);
            updateStateFromTypedArray(resolveAttributes);
            resolveAttributes.recycle();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean onStateChange(int[] iArr) {
        boolean onStateChange = super.onStateChange(iArr);
        if (iArr == null) {
            return onStateChange;
        }
        boolean z = this.pressed;
        boolean z2 = false;
        this.pressed = false;
        int length = iArr.length;
        int i = 0;
        boolean z3 = false;
        boolean z4 = false;
        boolean z5 = false;
        while (i < length) {
            int i2 = iArr[i];
            i++;
            switch (i2) {
                case 16842908:
                    z4 = true;
                    break;
                case 16842910:
                    z3 = true;
                    break;
                case 16842919:
                    this.pressed = true;
                    break;
                case 16843623:
                    z5 = true;
                    break;
            }
        }
        if (z3 && (this.pressed || z4 || z5)) {
            z2 = true;
        }
        setActive(z2);
        if (z && !this.pressed) {
            illuminate();
        }
        return onStateChange;
    }

    public final int getHighlightColor() {
        return this.highlightColor;
    }
}
