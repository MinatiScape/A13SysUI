package com.android.systemui.biometrics;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.animation.OvershootInterpolator;
import com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda1;
import com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda2;
import java.util.Objects;
/* loaded from: classes.dex */
public final class UdfpsEnrollProgressBarDrawable extends Drawable {
    public static final /* synthetic */ int $r8$clinit = 0;
    public boolean mAfterFirstTouch;
    public final Paint mBackgroundPaint;
    public ValueAnimator mCheckmarkAnimator;
    public final Drawable mCheckmarkDrawable;
    public ValueAnimator mFillColorAnimator;
    public final Paint mFillPaint;
    public final int mHelpColor;
    public ValueAnimator mProgressAnimator;
    public final int mProgressColor;
    public final float mStrokeWidthPx;
    public int mRemainingSteps = 0;
    public int mTotalSteps = 0;
    public float mProgress = 0.0f;
    public boolean mShowingHelp = false;
    public boolean mComplete = false;
    public float mCheckmarkScale = 0.0f;
    public final OvershootInterpolator mCheckmarkInterpolator = new OvershootInterpolator();
    public final ScreenshotView$$ExternalSyntheticLambda1 mProgressUpdateListener = new ScreenshotView$$ExternalSyntheticLambda1(this, 1);
    public final UdfpsEnrollProgressBarDrawable$$ExternalSyntheticLambda0 mFillColorUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.UdfpsEnrollProgressBarDrawable$$ExternalSyntheticLambda0
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
            UdfpsEnrollProgressBarDrawable udfpsEnrollProgressBarDrawable = UdfpsEnrollProgressBarDrawable.this;
            Objects.requireNonNull(udfpsEnrollProgressBarDrawable);
            udfpsEnrollProgressBarDrawable.mFillPaint.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
            udfpsEnrollProgressBarDrawable.invalidateSelf();
        }
    };
    public final ScreenshotView$$ExternalSyntheticLambda2 mCheckmarkUpdateListener = new ScreenshotView$$ExternalSyntheticLambda2(this, 1);

    @Override // android.graphics.drawable.Drawable
    public final int getOpacity() {
        return 0;
    }

    @Override // android.graphics.drawable.Drawable
    public final void setAlpha(int i) {
    }

    @Override // android.graphics.drawable.Drawable
    public final void setColorFilter(ColorFilter colorFilter) {
    }

    public final void updateState(int i, int i2, boolean z) {
        int i3;
        int i4;
        float f;
        if (!(this.mRemainingSteps == i && this.mTotalSteps == i2)) {
            this.mRemainingSteps = i;
            this.mTotalSteps = i2;
            int max = Math.max(0, i2 - i);
            boolean z2 = this.mAfterFirstTouch;
            if (z2) {
                max++;
            }
            if (z2) {
                i4 = this.mTotalSteps + 1;
            } else {
                i4 = this.mTotalSteps;
            }
            float min = Math.min(1.0f, max / i4);
            ValueAnimator valueAnimator = this.mProgressAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.mProgressAnimator.cancel();
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.mProgress, min);
            this.mProgressAnimator = ofFloat;
            ofFloat.setDuration(400L);
            this.mProgressAnimator.addUpdateListener(this.mProgressUpdateListener);
            this.mProgressAnimator.start();
            if (i == 0) {
                if (!this.mComplete) {
                    this.mComplete = true;
                    ValueAnimator valueAnimator2 = this.mCheckmarkAnimator;
                    if (valueAnimator2 != null && valueAnimator2.isRunning()) {
                        this.mCheckmarkAnimator.cancel();
                    }
                    ValueAnimator ofFloat2 = ValueAnimator.ofFloat(this.mCheckmarkScale, 1.0f);
                    this.mCheckmarkAnimator = ofFloat2;
                    ofFloat2.setStartDelay(200L);
                    this.mCheckmarkAnimator.setDuration(300L);
                    this.mCheckmarkAnimator.setInterpolator(this.mCheckmarkInterpolator);
                    this.mCheckmarkAnimator.addUpdateListener(this.mCheckmarkUpdateListener);
                    this.mCheckmarkAnimator.start();
                }
            } else if (i > 0 && this.mComplete) {
                this.mComplete = false;
                ValueAnimator valueAnimator3 = this.mCheckmarkAnimator;
                if (valueAnimator3 != null) {
                    f = valueAnimator3.getAnimatedFraction();
                } else {
                    f = 0.0f;
                }
                long round = Math.round(f * 200.0f);
                ValueAnimator valueAnimator4 = this.mCheckmarkAnimator;
                if (valueAnimator4 != null && valueAnimator4.isRunning()) {
                    this.mCheckmarkAnimator.cancel();
                }
                ValueAnimator ofFloat3 = ValueAnimator.ofFloat(this.mCheckmarkScale, 0.0f);
                this.mCheckmarkAnimator = ofFloat3;
                ofFloat3.setDuration(round);
                this.mCheckmarkAnimator.addUpdateListener(this.mCheckmarkUpdateListener);
                this.mCheckmarkAnimator.start();
            }
        }
        if (this.mShowingHelp != z) {
            this.mShowingHelp = z;
            ValueAnimator valueAnimator5 = this.mFillColorAnimator;
            if (valueAnimator5 != null && valueAnimator5.isRunning()) {
                this.mFillColorAnimator.cancel();
            }
            if (z) {
                i3 = this.mHelpColor;
            } else {
                i3 = this.mProgressColor;
            }
            ValueAnimator ofArgb = ValueAnimator.ofArgb(this.mFillPaint.getColor(), i3);
            this.mFillColorAnimator = ofArgb;
            ofArgb.setDuration(200L);
            this.mFillColorAnimator.addUpdateListener(this.mFillColorUpdateListener);
            this.mFillColorAnimator.start();
        }
    }

    /* JADX WARN: Type inference failed for: r0v10, types: [com.android.systemui.biometrics.UdfpsEnrollProgressBarDrawable$$ExternalSyntheticLambda0] */
    public UdfpsEnrollProgressBarDrawable(Context context) {
        float f = (context.getResources().getDisplayMetrics().densityDpi / 160.0f) * 12.0f;
        this.mStrokeWidthPx = f;
        int color = context.getColor(2131100776);
        this.mProgressColor = color;
        this.mHelpColor = context.getColor(2131100777);
        Drawable drawable = context.getDrawable(2131232746);
        this.mCheckmarkDrawable = drawable;
        drawable.mutate();
        Paint paint = new Paint();
        this.mBackgroundPaint = paint;
        paint.setStrokeWidth(f);
        paint.setColor(context.getColor(2131100778));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        Paint paint2 = new Paint();
        this.mFillPaint = paint2;
        paint2.setStrokeWidth(f);
        paint2.setColor(color);
        paint2.setAntiAlias(true);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override // android.graphics.drawable.Drawable
    public final void draw(Canvas canvas) {
        canvas.save();
        canvas.rotate(-90.0f, getBounds().centerX(), getBounds().centerY());
        float f = this.mStrokeWidthPx / 2.0f;
        if (this.mProgress < 1.0f) {
            canvas.drawArc(f, f, getBounds().right - f, getBounds().bottom - f, 0.0f, 360.0f, false, this.mBackgroundPaint);
        }
        if (this.mProgress > 0.0f) {
            canvas.drawArc(f, f, getBounds().right - f, getBounds().bottom - f, 0.0f, this.mProgress * 360.0f, false, this.mFillPaint);
        }
        canvas.restore();
        if (this.mCheckmarkScale > 0.0f) {
            float sqrt = ((float) Math.sqrt(2.0d)) / 2.0f;
            float width = ((getBounds().width() - this.mStrokeWidthPx) / 2.0f) * sqrt;
            float height = ((getBounds().height() - this.mStrokeWidthPx) / 2.0f) * sqrt;
            float centerX = getBounds().centerX() + width;
            float centerY = getBounds().centerY() + height;
            float intrinsicWidth = (this.mCheckmarkDrawable.getIntrinsicWidth() / 2.0f) * this.mCheckmarkScale;
            float intrinsicHeight = (this.mCheckmarkDrawable.getIntrinsicHeight() / 2.0f) * this.mCheckmarkScale;
            this.mCheckmarkDrawable.setBounds(Math.round(centerX - intrinsicWidth), Math.round(centerY - intrinsicHeight), Math.round(centerX + intrinsicWidth), Math.round(centerY + intrinsicHeight));
            this.mCheckmarkDrawable.draw(canvas);
        }
    }
}
