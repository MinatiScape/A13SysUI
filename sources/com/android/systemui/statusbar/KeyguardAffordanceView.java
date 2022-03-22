package com.android.systemui.statusbar;

import android.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.CanvasProperty;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RecordingCanvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.PathInterpolator;
import android.widget.ImageView;
import com.android.systemui.animation.Interpolators;
import com.android.wm.shell.animation.FlingAnimationUtils;
import java.util.Objects;
/* loaded from: classes.dex */
public class KeyguardAffordanceView extends ImageView {
    public static final /* synthetic */ int $r8$clinit = 0;
    public ValueAnimator mAlphaAnimator;
    public AnonymousClass4 mAlphaEndListener;
    public int mCenterX;
    public int mCenterY;
    public ValueAnimator mCircleAnimator;
    public int mCircleColor;
    public AnonymousClass2 mCircleEndListener;
    public final Paint mCirclePaint;
    public float mCircleRadius;
    public float mCircleStartRadius;
    public float mCircleStartValue;
    public boolean mCircleWillBeHidden;
    public AnonymousClass1 mClipEndListener;
    public final ArgbEvaluator mColorInterpolator;
    public final int mDarkIconColor;
    public boolean mFinishing;
    public final FlingAnimationUtils mFlingAnimationUtils;
    public CanvasProperty<Float> mHwCenterX;
    public CanvasProperty<Float> mHwCenterY;
    public CanvasProperty<Paint> mHwCirclePaint;
    public CanvasProperty<Float> mHwCircleRadius;
    public float mImageScale;
    public boolean mLaunchingAffordance;
    public float mMaxCircleSize;
    public final int mMinBackgroundRadius;
    public final int mNormalColor;
    public Animator mPreviewClipper;
    public View mPreviewView;
    public float mRestingAlpha;
    public ValueAnimator mScaleAnimator;
    public AnonymousClass3 mScaleEndListener;
    public boolean mShouldTint;
    public boolean mSupportHardware;
    public int[] mTempPoint;

    public KeyguardAffordanceView(Context context) {
        this(context, null);
    }

    public final ValueAnimator getAnimatorToRadius(float f) {
        boolean z = false;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.mCircleRadius, f);
        this.mCircleAnimator = ofFloat;
        this.mCircleStartValue = this.mCircleRadius;
        if (f == 0.0f) {
            z = true;
        }
        this.mCircleWillBeHidden = z;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.KeyguardAffordanceView.7
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                KeyguardAffordanceView.this.mCircleRadius = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                KeyguardAffordanceView.this.updateIconColor();
                KeyguardAffordanceView.this.invalidate();
            }
        });
        ofFloat.addListener(this.mCircleEndListener);
        return ofFloat;
    }

    public KeyguardAffordanceView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public static void cancelAnimator(Animator animator) {
        if (animator != null) {
            animator.cancel();
        }
    }

    public final float getMaxCircleSize() {
        getLocationInWindow(this.mTempPoint);
        float f = this.mTempPoint[0] + this.mCenterX;
        return (float) Math.hypot(Math.max(getRootView().getWidth() - f, f), this.mTempPoint[1] + this.mCenterY);
    }

    public final void setCircleRadius(float f, boolean z, boolean z2) {
        boolean z3;
        boolean z4;
        boolean z5;
        PathInterpolator pathInterpolator;
        View view;
        ValueAnimator valueAnimator = this.mCircleAnimator;
        if ((valueAnimator == null || !this.mCircleWillBeHidden) && !(valueAnimator == null && this.mCircleRadius == 0.0f)) {
            z3 = false;
        } else {
            z3 = true;
        }
        int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
        if (i == 0) {
            z4 = true;
        } else {
            z4 = false;
        }
        if (z3 == z4 || z2) {
            z5 = false;
        } else {
            z5 = true;
        }
        if (z5) {
            cancelAnimator(valueAnimator);
            cancelAnimator(this.mPreviewClipper);
            ValueAnimator animatorToRadius = getAnimatorToRadius(f);
            if (i == 0) {
                pathInterpolator = Interpolators.FAST_OUT_LINEAR_IN;
            } else {
                pathInterpolator = Interpolators.LINEAR_OUT_SLOW_IN;
            }
            animatorToRadius.setInterpolator(pathInterpolator);
            long j = 250;
            if (!z) {
                j = Math.min((Math.abs(this.mCircleRadius - f) / this.mMinBackgroundRadius) * 80.0f, 200L);
            }
            animatorToRadius.setDuration(j);
            animatorToRadius.start();
            View view2 = this.mPreviewView;
            if (view2 != null && view2.getVisibility() == 0) {
                this.mPreviewView.setVisibility(0);
                Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(this.mPreviewView, getLeft() + this.mCenterX, getTop() + this.mCenterY, this.mCircleRadius, f);
                this.mPreviewClipper = createCircularReveal;
                createCircularReveal.setInterpolator(pathInterpolator);
                this.mPreviewClipper.setDuration(j);
                this.mPreviewClipper.addListener(this.mClipEndListener);
                this.mPreviewClipper.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.KeyguardAffordanceView.6
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        KeyguardAffordanceView.this.mPreviewView.setVisibility(4);
                    }
                });
                this.mPreviewClipper.start();
            }
        } else if (valueAnimator == null) {
            this.mCircleRadius = f;
            updateIconColor();
            invalidate();
            if (z4 && (view = this.mPreviewView) != null) {
                view.setVisibility(4);
            }
        } else if (!this.mCircleWillBeHidden) {
            valueAnimator.getValues()[0].setFloatValues(this.mCircleStartValue + (f - this.mMinBackgroundRadius), f);
            ValueAnimator valueAnimator2 = this.mCircleAnimator;
            valueAnimator2.setCurrentPlayTime(valueAnimator2.getCurrentPlayTime());
        }
    }

    public final void setImageAlpha(float f, boolean z) {
        int imageAlpha;
        PathInterpolator pathInterpolator;
        cancelAnimator(this.mAlphaAnimator);
        if (this.mLaunchingAffordance) {
            f = 0.0f;
        }
        int i = (int) (f * 255.0f);
        final Drawable background = getBackground();
        if (!z) {
            if (background != null) {
                background.mutate().setAlpha(i);
            }
            setImageAlpha(i);
            return;
        }
        ValueAnimator ofInt = ValueAnimator.ofInt(getImageAlpha(), i);
        this.mAlphaAnimator = ofInt;
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.KeyguardAffordanceView$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                KeyguardAffordanceView keyguardAffordanceView = KeyguardAffordanceView.this;
                Drawable drawable = background;
                int i2 = KeyguardAffordanceView.$r8$clinit;
                Objects.requireNonNull(keyguardAffordanceView);
                int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                if (drawable != null) {
                    drawable.mutate().setAlpha(intValue);
                }
                keyguardAffordanceView.setImageAlpha(intValue);
            }
        });
        ofInt.addListener(this.mAlphaEndListener);
        if (f == 0.0f) {
            pathInterpolator = Interpolators.FAST_OUT_LINEAR_IN;
        } else {
            pathInterpolator = Interpolators.LINEAR_OUT_SLOW_IN;
        }
        ofInt.setInterpolator(pathInterpolator);
        ofInt.setDuration(Math.min(1.0f, Math.abs(imageAlpha - i) / 255.0f) * 200.0f);
        ofInt.start();
    }

    public final void updateIconColor() {
        if (this.mShouldTint) {
            getDrawable().mutate().setColorFilter(((Integer) this.mColorInterpolator.evaluate(Math.min(1.0f, this.mCircleRadius / this.mMinBackgroundRadius), Integer.valueOf(this.mNormalColor), Integer.valueOf(this.mDarkIconColor))).intValue(), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public KeyguardAffordanceView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    @Override // android.widget.ImageView, android.view.View
    public final void onDraw(Canvas canvas) {
        CanvasProperty<Float> canvasProperty;
        boolean isHardwareAccelerated = canvas.isHardwareAccelerated();
        this.mSupportHardware = isHardwareAccelerated;
        float f = this.mCircleRadius;
        if (f > 0.0f || this.mFinishing) {
            if (!this.mFinishing || !isHardwareAccelerated || (canvasProperty = this.mHwCenterX) == null) {
                float f2 = this.mMinBackgroundRadius;
                float max = (Math.max(0.0f, Math.min(1.0f, (f - f2) / (f2 * 0.5f))) * 0.5f) + 0.5f;
                View view = this.mPreviewView;
                if (view != null && view.getVisibility() == 0) {
                    max *= 1.0f - (Math.max(0.0f, this.mCircleRadius - this.mCircleStartRadius) / (this.mMaxCircleSize - this.mCircleStartRadius));
                }
                this.mCirclePaint.setColor(Color.argb((int) (Color.alpha(this.mCircleColor) * max), Color.red(this.mCircleColor), Color.green(this.mCircleColor), Color.blue(this.mCircleColor)));
                canvas.drawCircle(this.mCenterX, this.mCenterY, this.mCircleRadius, this.mCirclePaint);
            } else {
                ((RecordingCanvas) canvas).drawCircle(canvasProperty, this.mHwCenterY, this.mHwCircleRadius, this.mHwCirclePaint);
            }
        }
        canvas.save();
        float f3 = this.mImageScale;
        canvas.scale(f3, f3, getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);
        canvas.restore();
    }

    @Override // android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mCenterX = getWidth() / 2;
        this.mCenterY = getHeight() / 2;
        this.mMaxCircleSize = getMaxCircleSize();
    }

    @Override // android.view.View
    public final boolean performClick() {
        if (isClickable()) {
            return super.performClick();
        }
        return false;
    }

    public final void setImageDrawable(Drawable drawable, boolean z) {
        super.setImageDrawable(drawable);
        this.mShouldTint = z;
        updateIconColor();
    }

    /* JADX WARN: Type inference failed for: r4v1, types: [com.android.systemui.statusbar.KeyguardAffordanceView$1] */
    /* JADX WARN: Type inference failed for: r4v2, types: [com.android.systemui.statusbar.KeyguardAffordanceView$2] */
    /* JADX WARN: Type inference failed for: r4v3, types: [com.android.systemui.statusbar.KeyguardAffordanceView$3] */
    /* JADX WARN: Type inference failed for: r4v4, types: [com.android.systemui.statusbar.KeyguardAffordanceView$4] */
    public KeyguardAffordanceView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mTempPoint = new int[2];
        this.mImageScale = 1.0f;
        this.mRestingAlpha = 1.0f;
        this.mShouldTint = true;
        this.mClipEndListener = new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.KeyguardAffordanceView.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                KeyguardAffordanceView.this.mPreviewClipper = null;
            }
        };
        this.mCircleEndListener = new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.KeyguardAffordanceView.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                KeyguardAffordanceView.this.mCircleAnimator = null;
            }
        };
        this.mScaleEndListener = new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.KeyguardAffordanceView.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                KeyguardAffordanceView.this.mScaleAnimator = null;
            }
        };
        this.mAlphaEndListener = new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.KeyguardAffordanceView.4
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                KeyguardAffordanceView.this.mAlphaAnimator = null;
            }
        };
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ImageView);
        Paint paint = new Paint();
        this.mCirclePaint = paint;
        paint.setAntiAlias(true);
        this.mCircleColor = -1;
        paint.setColor(-1);
        this.mNormalColor = obtainStyledAttributes.getColor(5, -1);
        this.mDarkIconColor = -16777216;
        this.mMinBackgroundRadius = ((ImageView) this).mContext.getResources().getDimensionPixelSize(2131165836);
        this.mColorInterpolator = new ArgbEvaluator();
        this.mFlingAnimationUtils = new FlingAnimationUtils(((ImageView) this).mContext.getResources().getDisplayMetrics(), 0.3f);
        obtainStyledAttributes.recycle();
    }
}
