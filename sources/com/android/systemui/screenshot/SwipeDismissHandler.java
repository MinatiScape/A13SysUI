package com.android.systemui.screenshot;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.MathUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SwipeDismissHandler implements View.OnTouchListener {
    public final SwipeDismissCallbacks mCallbacks;
    public int mDirectionX;
    public ValueAnimator mDismissAnimation;
    public DisplayMetrics mDisplayMetrics = new DisplayMetrics();
    public final GestureDetector mGestureDetector;
    public float mPreviousX;
    public float mStartX;
    public final View mView;

    /* loaded from: classes.dex */
    public interface SwipeDismissCallbacks {
        void onDismiss();

        void onInteraction();
    }

    /* loaded from: classes.dex */
    public class SwipeDismissGestureListener extends GestureDetector.SimpleOnGestureListener {
        public SwipeDismissGestureListener() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public final boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (SwipeDismissHandler.this.mView.getTranslationX() * f <= 0.0f) {
                return false;
            }
            ValueAnimator valueAnimator = SwipeDismissHandler.this.mDismissAnimation;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                return false;
            }
            SwipeDismissHandler.this.dismiss(f / 1000.0f);
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public final boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            int i;
            SwipeDismissHandler.this.mView.setTranslationX(motionEvent2.getRawX() - SwipeDismissHandler.this.mStartX);
            SwipeDismissHandler swipeDismissHandler = SwipeDismissHandler.this;
            float rawX = motionEvent2.getRawX();
            SwipeDismissHandler swipeDismissHandler2 = SwipeDismissHandler.this;
            if (rawX < swipeDismissHandler2.mPreviousX) {
                i = -1;
            } else {
                i = 1;
            }
            swipeDismissHandler.mDirectionX = i;
            swipeDismissHandler2.mPreviousX = motionEvent2.getRawX();
            return true;
        }
    }

    public final void dismiss(float f) {
        int i;
        float min = Math.min(3.0f, Math.max(1.0f, f));
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        final float translationX = this.mView.getTranslationX();
        int layoutDirection = this.mView.getContext().getResources().getConfiguration().getLayoutDirection();
        int i2 = (translationX > 0.0f ? 1 : (translationX == 0.0f ? 0 : -1));
        if (i2 > 0 || (i2 == 0 && layoutDirection == 1)) {
            i = this.mDisplayMetrics.widthPixels;
        } else {
            i = this.mView.getRight() * (-1);
        }
        final float f2 = i;
        float abs = Math.abs(f2 - translationX);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.screenshot.SwipeDismissHandler$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                SwipeDismissHandler swipeDismissHandler = SwipeDismissHandler.this;
                float f3 = translationX;
                float f4 = f2;
                Objects.requireNonNull(swipeDismissHandler);
                swipeDismissHandler.mView.setTranslationX(MathUtils.lerp(f3, f4, valueAnimator.getAnimatedFraction()));
                swipeDismissHandler.mView.setAlpha(1.0f - valueAnimator.getAnimatedFraction());
            }
        });
        ofFloat.setDuration(abs / Math.abs(min));
        this.mDismissAnimation = ofFloat;
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.screenshot.SwipeDismissHandler.1
            public boolean mCancelled;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationCancel(Animator animator) {
                super.onAnimationCancel(animator);
                this.mCancelled = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                if (!this.mCancelled) {
                    SwipeDismissHandler.this.mCallbacks.onDismiss();
                }
            }
        });
        this.mDismissAnimation.start();
    }

    @Override // android.view.View.OnTouchListener
    public final boolean onTouch(View view, MotionEvent motionEvent) {
        boolean onTouchEvent = this.mGestureDetector.onTouchEvent(motionEvent);
        this.mCallbacks.onInteraction();
        if (motionEvent.getActionMasked() == 0) {
            float rawX = motionEvent.getRawX();
            this.mStartX = rawX;
            this.mPreviousX = rawX;
            return true;
        } else if (motionEvent.getActionMasked() != 1) {
            return onTouchEvent;
        } else {
            ValueAnimator valueAnimator = this.mDismissAnimation;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                return true;
            }
            float translationX = this.mView.getTranslationX();
            boolean z = false;
            if (this.mDirectionX * translationX > 0.0f && Math.abs(translationX) >= FloatingWindowUtil.dpToPx(this.mDisplayMetrics, 20.0f)) {
                z = true;
            }
            if (z) {
                dismiss(1.0f);
            } else {
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                final float translationX2 = this.mView.getTranslationX();
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.screenshot.SwipeDismissHandler$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        SwipeDismissHandler swipeDismissHandler = SwipeDismissHandler.this;
                        float f = translationX2;
                        Objects.requireNonNull(swipeDismissHandler);
                        swipeDismissHandler.mView.setTranslationX(MathUtils.lerp(f, 0.0f, valueAnimator2.getAnimatedFraction()));
                    }
                });
                ofFloat.start();
            }
            return true;
        }
    }

    public SwipeDismissHandler(Context context, View view, SwipeDismissCallbacks swipeDismissCallbacks) {
        this.mView = view;
        this.mCallbacks = swipeDismissCallbacks;
        this.mGestureDetector = new GestureDetector(context, new SwipeDismissGestureListener());
        context.getDisplay().getRealMetrics(this.mDisplayMetrics);
    }
}
