package com.android.systemui.privacy.television;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import androidx.annotation.Keep;
import java.util.Objects;
/* loaded from: classes.dex */
public class PrivacyChipDrawable extends Drawable {
    public int mBgAlpha;
    public final int mBgHeight;
    public final Paint mBgPaint;
    public final int mBgRadius;
    public final int mBgWidth;
    public final Paint mChipPaint;
    public final AnimatorSet mCollapse;
    public int mDotAlpha;
    public final int mDotSize;
    public final AnimatorSet mExpand;
    public final AnimatorSet mFadeIn;
    public final AnimatorSet mFadeOut;
    public float mHeight;
    public final int mIconPadding;
    public final int mIconWidth;
    public boolean mIsExpanded = true;
    public boolean mIsRtl;
    public PrivacyChipDrawableListener mListener;
    public float mMarginEnd;
    public final int mMinWidth;
    public float mRadius;
    public float mTargetWidth;
    public float mWidth;
    public ObjectAnimator mWidthAnimator;

    /* loaded from: classes.dex */
    public interface PrivacyChipDrawableListener {
    }

    @Override // android.graphics.drawable.Drawable
    public final int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public final void setColorFilter(ColorFilter colorFilter) {
    }

    public final void animateToNewTargetWidth(float f) {
        if (f != this.mTargetWidth) {
            this.mTargetWidth = f;
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "width", f);
            ofFloat.start();
            ObjectAnimator objectAnimator = this.mWidthAnimator;
            if (objectAnimator != null) {
                objectAnimator.cancel();
            }
            this.mWidthAnimator = ofFloat;
        }
    }

    @Keep
    public void setBgAlpha(int i) {
        this.mBgAlpha = i;
        this.mBgPaint.setAlpha(i);
    }

    @Keep
    public void setDotAlpha(int i) {
        this.mDotAlpha = i;
        this.mChipPaint.setAlpha(i);
    }

    @Keep
    public void setHeight(float f) {
        this.mHeight = f;
        invalidateSelf();
    }

    @Keep
    public void setMarginEnd(float f) {
        this.mMarginEnd = f;
        invalidateSelf();
    }

    @Keep
    public void setRadius(float f) {
        this.mRadius = f;
        invalidateSelf();
    }

    @Keep
    public void setWidth(float f) {
        this.mWidth = f;
        invalidateSelf();
    }

    public final void updateIcons(int i) {
        if (i == 0) {
            this.mFadeOut.start();
            this.mWidthAnimator.cancel();
            this.mFadeIn.cancel();
            this.mExpand.cancel();
            this.mCollapse.cancel();
            return;
        }
        this.mFadeOut.cancel();
        if (!this.mIsExpanded) {
            this.mIsExpanded = true;
            this.mExpand.start();
            this.mCollapse.cancel();
        }
        animateToNewTargetWidth(((this.mIconWidth + this.mIconPadding) * (i - 1)) + this.mMinWidth);
    }

    public PrivacyChipDrawable(Context context) {
        Paint paint = new Paint();
        this.mChipPaint = paint;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(context.getColor(2131100507));
        paint.setAlpha(this.mDotAlpha);
        paint.setFlags(1);
        Paint paint2 = new Paint();
        this.mBgPaint = paint2;
        paint2.setStyle(Paint.Style.FILL);
        paint2.setColor(context.getColor(2131100506));
        paint2.setAlpha(this.mBgAlpha);
        paint2.setFlags(1);
        this.mBgWidth = context.getResources().getDimensionPixelSize(2131166817);
        this.mBgHeight = context.getResources().getDimensionPixelSize(2131166815);
        this.mBgRadius = context.getResources().getDimensionPixelSize(2131166816);
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(2131166826);
        this.mMinWidth = dimensionPixelSize;
        this.mIconWidth = context.getResources().getDimensionPixelSize(2131166823);
        this.mIconPadding = context.getResources().getDimensionPixelSize(2131166822);
        this.mDotSize = context.getResources().getDimensionPixelSize(2131166820);
        this.mWidth = dimensionPixelSize;
        this.mHeight = context.getResources().getDimensionPixelSize(2131166821);
        this.mRadius = context.getResources().getDimensionPixelSize(2131166828);
        AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, 2130772569);
        this.mExpand = animatorSet;
        animatorSet.setTarget(this);
        AnimatorSet animatorSet2 = (AnimatorSet) AnimatorInflater.loadAnimator(context, 2130772568);
        this.mCollapse = animatorSet2;
        animatorSet2.setTarget(this);
        AnimatorSet animatorSet3 = (AnimatorSet) AnimatorInflater.loadAnimator(context, 2130772570);
        this.mFadeIn = animatorSet3;
        animatorSet3.setTarget(this);
        AnimatorSet animatorSet4 = (AnimatorSet) AnimatorInflater.loadAnimator(context, 2130772571);
        this.mFadeOut = animatorSet4;
        animatorSet4.setTarget(this);
        animatorSet4.addListener(new Animator.AnimatorListener() { // from class: com.android.systemui.privacy.television.PrivacyChipDrawable.1
            public boolean mCancelled;

            @Override // android.animation.Animator.AnimatorListener
            public final void onAnimationCancel(Animator animator) {
                this.mCancelled = true;
            }

            @Override // android.animation.Animator.AnimatorListener
            public final void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public final void onAnimationStart(Animator animator) {
                this.mCancelled = false;
            }

            @Override // android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                PrivacyChipDrawableListener privacyChipDrawableListener;
                if (!this.mCancelled && (privacyChipDrawableListener = PrivacyChipDrawable.this.mListener) != null) {
                    TvOngoingPrivacyChip tvOngoingPrivacyChip = (TvOngoingPrivacyChip) privacyChipDrawableListener;
                    Objects.requireNonNull(tvOngoingPrivacyChip);
                    if (tvOngoingPrivacyChip.mState == 4) {
                        tvOngoingPrivacyChip.removeIndicatorView();
                        tvOngoingPrivacyChip.mState = 0;
                    }
                }
            }
        });
    }

    @Override // android.graphics.drawable.Drawable
    public final void draw(Canvas canvas) {
        int i;
        int i2;
        float f;
        float f2;
        Rect bounds = getBounds();
        int i3 = (bounds.bottom - bounds.top) / 2;
        boolean z = this.mIsRtl;
        if (z) {
            i = bounds.left;
        } else {
            i = bounds.right - this.mBgWidth;
        }
        float f3 = i;
        float f4 = i3;
        int i4 = this.mBgHeight;
        float f5 = f4 - (i4 / 2.0f);
        if (z) {
            i2 = bounds.left + this.mBgWidth;
        } else {
            i2 = bounds.right;
        }
        RectF rectF = new RectF(f3, f5, i2, (i4 / 2.0f) + f4);
        int i5 = this.mBgRadius;
        canvas.drawRoundRect(rectF, i5, i5, this.mBgPaint);
        boolean z2 = this.mIsRtl;
        if (z2) {
            f = bounds.left + this.mMarginEnd;
        } else {
            f = (bounds.right - this.mWidth) - this.mMarginEnd;
        }
        float f6 = this.mHeight;
        float f7 = f4 - (f6 / 2.0f);
        if (z2) {
            f2 = bounds.left + this.mWidth + this.mMarginEnd;
        } else {
            f2 = bounds.right - this.mMarginEnd;
        }
        RectF rectF2 = new RectF(f, f7, f2, (f6 / 2.0f) + f4);
        float f8 = this.mRadius;
        canvas.drawRoundRect(rectF2, f8, f8, this.mChipPaint);
    }

    @Override // android.graphics.drawable.Drawable
    public final void setAlpha(int i) {
        setDotAlpha(i);
        setBgAlpha(i);
    }

    @Override // android.graphics.drawable.Drawable
    public final int getAlpha() {
        return this.mDotAlpha;
    }

    @Keep
    public int getBgAlpha() {
        return this.mBgAlpha;
    }

    @Keep
    public int getDotAlpha() {
        return this.mDotAlpha;
    }

    @Keep
    public float getHeight() {
        return this.mHeight;
    }

    @Keep
    public float getMarginEnd() {
        return this.mMarginEnd;
    }

    @Keep
    public float getRadius() {
        return this.mRadius;
    }

    @Keep
    public float getWidth() {
        return this.mWidth;
    }
}
