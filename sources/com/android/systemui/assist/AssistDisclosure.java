package com.android.systemui.assist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.PathInterpolator;
import com.android.systemui.animation.Interpolators;
import java.util.Objects;
/* loaded from: classes.dex */
public final class AssistDisclosure {
    public final Context mContext;
    public final Handler mHandler;
    public AnonymousClass1 mShowRunnable = new Runnable() { // from class: com.android.systemui.assist.AssistDisclosure.1
        @Override // java.lang.Runnable
        public final void run() {
            AssistDisclosure assistDisclosure = AssistDisclosure.this;
            Objects.requireNonNull(assistDisclosure);
            if (assistDisclosure.mView == null) {
                assistDisclosure.mView = new AssistDisclosureView(assistDisclosure.mContext);
            }
            if (!assistDisclosure.mViewAdded) {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(2015, 525576, -3);
                layoutParams.setTitle("AssistDisclosure");
                layoutParams.setFitInsetsTypes(0);
                assistDisclosure.mWm.addView(assistDisclosure.mView, layoutParams);
                assistDisclosure.mViewAdded = true;
            }
        }
    };
    public AssistDisclosureView mView;
    public boolean mViewAdded;
    public final WindowManager mWm;

    /* loaded from: classes.dex */
    public class AssistDisclosureView extends View implements ValueAnimator.AnimatorUpdateListener {
        public final ValueAnimator mAlphaInAnimator;
        public final ValueAnimator mAlphaOutAnimator;
        public final AnimatorSet mAnimator;
        public final Paint mPaint;
        public final Paint mShadowPaint;
        public int mAlpha = 0;
        public float mThickness = getResources().getDimension(2131165342);
        public float mShadowThickness = getResources().getDimension(2131165341);

        public AssistDisclosureView(Context context) {
            super(context);
            Paint paint = new Paint();
            this.mPaint = paint;
            Paint paint2 = new Paint();
            this.mShadowPaint = paint2;
            ValueAnimator duration = ValueAnimator.ofInt(0, 222).setDuration(400L);
            this.mAlphaInAnimator = duration;
            duration.addUpdateListener(this);
            PathInterpolator pathInterpolator = Interpolators.CUSTOM_40_40;
            duration.setInterpolator(pathInterpolator);
            ValueAnimator duration2 = ValueAnimator.ofInt(222, 0).setDuration(300L);
            this.mAlphaOutAnimator = duration2;
            duration2.addUpdateListener(this);
            duration2.setInterpolator(pathInterpolator);
            AnimatorSet animatorSet = new AnimatorSet();
            this.mAnimator = animatorSet;
            animatorSet.play(duration).before(duration2);
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.assist.AssistDisclosure.AssistDisclosureView.1
                public boolean mCancelled;

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationCancel(Animator animator) {
                    this.mCancelled = true;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationStart(Animator animator) {
                    this.mCancelled = false;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    if (!this.mCancelled) {
                        AssistDisclosure assistDisclosure = AssistDisclosure.this;
                        Objects.requireNonNull(assistDisclosure);
                        if (assistDisclosure.mViewAdded) {
                            assistDisclosure.mWm.removeView(assistDisclosure.mView);
                            assistDisclosure.mViewAdded = false;
                        }
                    }
                }
            });
            PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC);
            paint.setColor(-1);
            paint.setXfermode(porterDuffXfermode);
            paint2.setColor(-12303292);
            paint2.setXfermode(porterDuffXfermode);
        }

        public static void drawBeam(Canvas canvas, float f, float f2, float f3, float f4, Paint paint, float f5) {
            canvas.drawRect(f - f5, f2 - f5, f3 + f5, f4 + f5, paint);
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
            ValueAnimator valueAnimator2 = this.mAlphaOutAnimator;
            if (valueAnimator == valueAnimator2) {
                this.mAlpha = ((Integer) valueAnimator2.getAnimatedValue()).intValue();
            } else {
                ValueAnimator valueAnimator3 = this.mAlphaInAnimator;
                if (valueAnimator == valueAnimator3) {
                    this.mAlpha = ((Integer) valueAnimator3.getAnimatedValue()).intValue();
                }
            }
            invalidate();
        }

        @Override // android.view.View
        public final void onDraw(Canvas canvas) {
            this.mPaint.setAlpha(this.mAlpha);
            this.mShadowPaint.setAlpha(this.mAlpha / 4);
            drawGeometry(canvas, this.mShadowPaint, this.mShadowThickness);
            drawGeometry(canvas, this.mPaint, 0.0f);
        }

        public final void drawGeometry(Canvas canvas, Paint paint, float f) {
            int width = getWidth();
            int height = getHeight();
            float f2 = this.mThickness;
            float f3 = height;
            float f4 = f3 - f2;
            float f5 = width;
            drawBeam(canvas, 0.0f, f4, f5, f3, paint, f);
            drawBeam(canvas, 0.0f, 0.0f, f2, f4, paint, f);
            float f6 = f5 - f2;
            drawBeam(canvas, f6, 0.0f, f5, f4, paint, f);
            drawBeam(canvas, f2, 0.0f, f6, f2, paint, f);
        }

        @Override // android.view.View
        public final void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.mAnimator.cancel();
            this.mAnimator.start();
            sendAccessibilityEvent(16777216);
        }

        @Override // android.view.View
        public final void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.mAnimator.cancel();
            this.mAlpha = 0;
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.assist.AssistDisclosure$1] */
    public AssistDisclosure(Context context, Handler handler) {
        this.mContext = context;
        this.mHandler = handler;
        this.mWm = (WindowManager) context.getSystemService(WindowManager.class);
    }
}
