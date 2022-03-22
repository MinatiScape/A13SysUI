package com.google.android.material.progressindicator;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.provider.Settings;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.leanback.R$string;
import com.google.android.material.progressindicator.BaseProgressIndicatorSpec;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DeterminateDrawable<S extends BaseProgressIndicatorSpec> extends DrawableWithAnimatedVisibilityChange {
    public static final AnonymousClass1 INDICATOR_LENGTH_IN_LEVEL = new FloatPropertyCompat<DeterminateDrawable>() { // from class: com.google.android.material.progressindicator.DeterminateDrawable.1
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final float getValue(DeterminateDrawable determinateDrawable) {
            DeterminateDrawable determinateDrawable2 = determinateDrawable;
            Objects.requireNonNull(determinateDrawable2);
            return determinateDrawable2.indicatorFraction * 10000.0f;
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final void setValue(DeterminateDrawable determinateDrawable, float f) {
            DeterminateDrawable determinateDrawable2 = determinateDrawable;
            Objects.requireNonNull(determinateDrawable2);
            determinateDrawable2.indicatorFraction = f / 10000.0f;
            determinateDrawable2.invalidateSelf();
        }
    };
    public DrawingDelegate<S> drawingDelegate;
    public float indicatorFraction;
    public boolean skipAnimationOnLevelChange = false;
    public final SpringAnimation springAnimation;
    public final SpringForce springForce;

    @Override // android.graphics.drawable.Drawable
    public final void draw(Canvas canvas) {
        Rect rect = new Rect();
        if (!getBounds().isEmpty() && isVisible() && canvas.getClipBounds(rect)) {
            canvas.save();
            DrawingDelegate<S> drawingDelegate = this.drawingDelegate;
            float growFraction = getGrowFraction();
            Objects.requireNonNull(drawingDelegate);
            drawingDelegate.spec.validateSpec();
            drawingDelegate.adjustCanvas(canvas, growFraction);
            this.drawingDelegate.fillTrack(canvas, this.paint);
            this.drawingDelegate.fillIndicator(canvas, this.paint, 0.0f, this.indicatorFraction, R$string.compositeARGBWithAlpha(this.baseSpec.indicatorColors[0], this.totalAlpha));
            canvas.restore();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public final int getIntrinsicHeight() {
        return this.drawingDelegate.getPreferredHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public final int getIntrinsicWidth() {
        return this.drawingDelegate.getPreferredWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public final void jumpToCurrentState() {
        this.springAnimation.skipToEnd();
        this.indicatorFraction = getLevel() / 10000.0f;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public final boolean onLevelChange(int i) {
        if (this.skipAnimationOnLevelChange) {
            this.springAnimation.skipToEnd();
            this.indicatorFraction = i / 10000.0f;
            invalidateSelf();
        } else {
            SpringAnimation springAnimation = this.springAnimation;
            Objects.requireNonNull(springAnimation);
            springAnimation.mValue = this.indicatorFraction * 10000.0f;
            springAnimation.mStartValueIsSet = true;
            this.springAnimation.animateToFinalPosition(i);
        }
        return true;
    }

    public DeterminateDrawable(Context context, BaseProgressIndicatorSpec baseProgressIndicatorSpec, DrawingDelegate<S> drawingDelegate) {
        super(context, baseProgressIndicatorSpec);
        this.drawingDelegate = drawingDelegate;
        drawingDelegate.drawable = this;
        SpringForce springForce = new SpringForce();
        this.springForce = springForce;
        springForce.setDampingRatio(1.0f);
        springForce.setStiffness(50.0f);
        SpringAnimation springAnimation = new SpringAnimation(this, INDICATOR_LENGTH_IN_LEVEL);
        this.springAnimation = springAnimation;
        springAnimation.mSpring = springForce;
        if (this.growFraction != 1.0f) {
            this.growFraction = 1.0f;
            invalidateSelf();
        }
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange
    public final boolean setVisibleInternal(boolean z, boolean z2, boolean z3) {
        boolean visibleInternal = super.setVisibleInternal(z, z2, z3);
        AnimatorDurationScaleProvider animatorDurationScaleProvider = this.animatorDurationScaleProvider;
        ContentResolver contentResolver = this.context.getContentResolver();
        Objects.requireNonNull(animatorDurationScaleProvider);
        float f = Settings.Global.getFloat(contentResolver, "animator_duration_scale", 1.0f);
        if (f == 0.0f) {
            this.skipAnimationOnLevelChange = true;
        } else {
            this.skipAnimationOnLevelChange = false;
            this.springForce.setStiffness(50.0f / f);
        }
        return visibleInternal;
    }
}
