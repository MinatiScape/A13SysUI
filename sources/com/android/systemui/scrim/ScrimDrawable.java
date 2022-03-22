package com.android.systemui.scrim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.view.animation.DecelerateInterpolator;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.graphics.ColorUtils;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ScrimDrawable extends Drawable {
    public int mAlpha = 255;
    public int mBottomEdgePosition;
    public ValueAnimator mColorAnimation;
    public ConcaveInfo mConcaveInfo;
    public float mCornerRadius;
    public boolean mCornerRadiusEnabled;
    public int mMainColor;
    public int mMainColorTo;
    public final Paint mPaint;

    /* loaded from: classes.dex */
    public static class ConcaveInfo {
        public float mPathOverlap;
        public final Path mPath = new Path();
        public final float[] mCornerRadii = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    }

    @Override // android.graphics.drawable.Drawable
    public final int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public final void draw(Canvas canvas) {
        this.mPaint.setColor(this.mMainColor);
        this.mPaint.setAlpha(this.mAlpha);
        ConcaveInfo concaveInfo = this.mConcaveInfo;
        if (concaveInfo != null) {
            canvas.clipOutPath(concaveInfo.mPath);
            canvas.drawRect(getBounds().left, getBounds().top, getBounds().right, this.mBottomEdgePosition + this.mConcaveInfo.mPathOverlap, this.mPaint);
        } else if (!this.mCornerRadiusEnabled || this.mCornerRadius <= 0.0f) {
            canvas.drawRect(getBounds().left, getBounds().top, getBounds().right, getBounds().bottom, this.mPaint);
        } else {
            float f = this.mCornerRadius;
            canvas.drawRoundRect(getBounds().left, getBounds().top, getBounds().right, getBounds().bottom + f, f, f, this.mPaint);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public final ColorFilter getColorFilter() {
        return this.mPaint.getColorFilter();
    }

    @Override // android.graphics.drawable.Drawable
    public final void setAlpha(int i) {
        if (i != this.mAlpha) {
            this.mAlpha = i;
            invalidateSelf();
        }
    }

    public final void setColor(final int i, boolean z) {
        if (i != this.mMainColorTo) {
            ValueAnimator valueAnimator = this.mColorAnimation;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.mColorAnimation.cancel();
            }
            this.mMainColorTo = i;
            if (z) {
                final int i2 = this.mMainColor;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                ofFloat.setDuration(2000L);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.scrim.ScrimDrawable$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        ScrimDrawable scrimDrawable = ScrimDrawable.this;
                        int i3 = i2;
                        int i4 = i;
                        Objects.requireNonNull(scrimDrawable);
                        scrimDrawable.mMainColor = ColorUtils.blendARGB(i3, i4, ((Float) valueAnimator2.getAnimatedValue()).floatValue());
                        scrimDrawable.invalidateSelf();
                    }
                });
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.scrim.ScrimDrawable.1
                    @Override // android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator, boolean z2) {
                        ScrimDrawable scrimDrawable = ScrimDrawable.this;
                        if (scrimDrawable.mColorAnimation == animator) {
                            scrimDrawable.mColorAnimation = null;
                        }
                    }
                });
                ofFloat.setInterpolator(new DecelerateInterpolator());
                ofFloat.start();
                this.mColorAnimation = ofFloat;
                return;
            }
            this.mMainColor = i;
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public final void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    public final void setXfermode(Xfermode xfermode) {
        this.mPaint.setXfermode(xfermode);
        invalidateSelf();
    }

    public final void updatePath() {
        ConcaveInfo concaveInfo = this.mConcaveInfo;
        if (concaveInfo != null) {
            concaveInfo.mPath.reset();
            int i = this.mBottomEdgePosition;
            ConcaveInfo concaveInfo2 = this.mConcaveInfo;
            concaveInfo2.mPath.addRoundRect(getBounds().left, i, getBounds().right, i + concaveInfo2.mPathOverlap, this.mConcaveInfo.mCornerRadii, Path.Direction.CW);
        }
    }

    public ScrimDrawable() {
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setStyle(Paint.Style.FILL);
    }

    @Override // android.graphics.drawable.Drawable
    public final void onBoundsChange(Rect rect) {
        updatePath();
    }

    @Override // android.graphics.drawable.Drawable
    public final int getAlpha() {
        return this.mAlpha;
    }

    @VisibleForTesting
    public int getMainColor() {
        return this.mMainColor;
    }
}
