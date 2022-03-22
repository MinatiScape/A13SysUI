package com.android.wm.shell.common.split;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.animation.PathInterpolator;
import com.android.wm.shell.animation.Interpolators;
/* loaded from: classes.dex */
public class DividerHandleView extends View {
    public AnimatorSet mAnimator;
    public int mCurrentHeight;
    public int mCurrentWidth;
    public final int mHeight;
    public final Paint mPaint;
    public boolean mTouching;
    public final int mTouchingHeight;
    public final int mTouchingWidth;
    public final int mWidth;
    public static final AnonymousClass1 WIDTH_PROPERTY = new Property<DividerHandleView, Integer>() { // from class: com.android.wm.shell.common.split.DividerHandleView.1
        @Override // android.util.Property
        public final Integer get(DividerHandleView dividerHandleView) {
            return Integer.valueOf(dividerHandleView.mCurrentWidth);
        }

        @Override // android.util.Property
        public final void set(DividerHandleView dividerHandleView, Integer num) {
            DividerHandleView dividerHandleView2 = dividerHandleView;
            dividerHandleView2.mCurrentWidth = num.intValue();
            dividerHandleView2.invalidate();
        }
    };
    public static final AnonymousClass2 HEIGHT_PROPERTY = new Property<DividerHandleView, Integer>() { // from class: com.android.wm.shell.common.split.DividerHandleView.2
        @Override // android.util.Property
        public final Integer get(DividerHandleView dividerHandleView) {
            return Integer.valueOf(dividerHandleView.mCurrentHeight);
        }

        @Override // android.util.Property
        public final void set(DividerHandleView dividerHandleView, Integer num) {
            DividerHandleView dividerHandleView2 = dividerHandleView;
            dividerHandleView2.mCurrentHeight = num.intValue();
            dividerHandleView2.invalidate();
        }
    };

    @Override // android.view.View
    public final boolean hasOverlappingRendering() {
        return false;
    }

    public final void setTouching(boolean z, boolean z2) {
        int i;
        int i2;
        long j;
        PathInterpolator pathInterpolator;
        if (z != this.mTouching) {
            AnimatorSet animatorSet = this.mAnimator;
            if (animatorSet != null) {
                animatorSet.cancel();
                this.mAnimator = null;
            }
            if (!z2) {
                if (z) {
                    this.mCurrentWidth = this.mTouchingWidth;
                    this.mCurrentHeight = this.mTouchingHeight;
                } else {
                    this.mCurrentWidth = this.mWidth;
                    this.mCurrentHeight = this.mHeight;
                }
                invalidate();
            } else {
                if (z) {
                    i = this.mTouchingWidth;
                } else {
                    i = this.mWidth;
                }
                if (z) {
                    i2 = this.mTouchingHeight;
                } else {
                    i2 = this.mHeight;
                }
                ObjectAnimator ofInt = ObjectAnimator.ofInt(this, WIDTH_PROPERTY, this.mCurrentWidth, i);
                ObjectAnimator ofInt2 = ObjectAnimator.ofInt(this, HEIGHT_PROPERTY, this.mCurrentHeight, i2);
                AnimatorSet animatorSet2 = new AnimatorSet();
                this.mAnimator = animatorSet2;
                animatorSet2.playTogether(ofInt, ofInt2);
                AnimatorSet animatorSet3 = this.mAnimator;
                if (z) {
                    j = 150;
                } else {
                    j = 200;
                }
                animatorSet3.setDuration(j);
                AnimatorSet animatorSet4 = this.mAnimator;
                if (z) {
                    pathInterpolator = Interpolators.TOUCH_RESPONSE;
                } else {
                    pathInterpolator = Interpolators.FAST_OUT_SLOW_IN;
                }
                animatorSet4.setInterpolator(pathInterpolator);
                this.mAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.wm.shell.common.split.DividerHandleView.3
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        DividerHandleView.this.mAnimator = null;
                    }
                });
                this.mAnimator.start();
            }
            this.mTouching = z;
        }
    }

    public DividerHandleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        int i;
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setColor(getResources().getColor(2131099851, null));
        paint.setAntiAlias(true);
        int dimensionPixelSize = getResources().getDimensionPixelSize(2131167040);
        this.mWidth = dimensionPixelSize;
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(2131167039);
        this.mHeight = dimensionPixelSize2;
        this.mCurrentWidth = dimensionPixelSize;
        this.mCurrentHeight = dimensionPixelSize2;
        if (dimensionPixelSize > dimensionPixelSize2) {
            i = dimensionPixelSize / 2;
        } else {
            i = dimensionPixelSize;
        }
        this.mTouchingWidth = i;
        this.mTouchingHeight = dimensionPixelSize2 > dimensionPixelSize ? dimensionPixelSize2 / 2 : dimensionPixelSize2;
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        int width = (getWidth() / 2) - (this.mCurrentWidth / 2);
        int i = this.mCurrentHeight;
        int height = (getHeight() / 2) - (i / 2);
        float min = Math.min(this.mCurrentWidth, i) / 2;
        canvas.drawRoundRect(width, height, width + this.mCurrentWidth, height + this.mCurrentHeight, min, min, this.mPaint);
    }
}
