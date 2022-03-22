package com.android.wm.shell.startingsurface;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.util.Log;
import android.window.SplashScreenView;
/* loaded from: classes.dex */
public final class SplashscreenIconDrawableFactory$AnimatableIconAnimateListener extends SplashscreenIconDrawableFactory$AdaptiveForegroundDrawable implements SplashScreenView.IconAnimateListener {
    public Animatable mAnimatableIcon;
    public boolean mAnimationTriggered;
    public final AnonymousClass2 mCallback;
    public ValueAnimator mIconAnimator;
    public AnimatorListenerAdapter mJankMonitoringListener;

    @Override // com.android.wm.shell.startingsurface.SplashscreenIconDrawableFactory$AdaptiveForegroundDrawable, com.android.wm.shell.startingsurface.SplashscreenIconDrawableFactory$MaskBackgroundDrawable, android.graphics.drawable.Drawable
    public final void draw(Canvas canvas) {
        if (!this.mAnimationTriggered) {
            ValueAnimator valueAnimator = this.mIconAnimator;
            if (valueAnimator != null && !valueAnimator.isRunning()) {
                this.mIconAnimator.start();
            }
            this.mAnimationTriggered = true;
        }
        super.draw(canvas);
    }

    public final boolean prepareAnimate(long j, final Runnable runnable) {
        this.mAnimatableIcon = (Animatable) this.mForegroundDrawable;
        ValueAnimator ofInt = ValueAnimator.ofInt(0, 1);
        this.mIconAnimator = ofInt;
        ofInt.setDuration(j);
        this.mIconAnimator.addListener(new Animator.AnimatorListener() { // from class: com.android.wm.shell.startingsurface.SplashscreenIconDrawableFactory$AnimatableIconAnimateListener.1
            @Override // android.animation.Animator.AnimatorListener
            public final void onAnimationCancel(Animator animator) {
                SplashscreenIconDrawableFactory$AnimatableIconAnimateListener.this.mAnimatableIcon.stop();
                AnimatorListenerAdapter animatorListenerAdapter = SplashscreenIconDrawableFactory$AnimatableIconAnimateListener.this.mJankMonitoringListener;
                if (animatorListenerAdapter != null) {
                    animatorListenerAdapter.onAnimationCancel(animator);
                }
            }

            @Override // android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                SplashscreenIconDrawableFactory$AnimatableIconAnimateListener.this.mAnimatableIcon.stop();
                AnimatorListenerAdapter animatorListenerAdapter = SplashscreenIconDrawableFactory$AnimatableIconAnimateListener.this.mJankMonitoringListener;
                if (animatorListenerAdapter != null) {
                    animatorListenerAdapter.onAnimationEnd(animator);
                }
            }

            @Override // android.animation.Animator.AnimatorListener
            public final void onAnimationRepeat(Animator animator) {
                SplashscreenIconDrawableFactory$AnimatableIconAnimateListener.this.mAnimatableIcon.stop();
            }

            @Override // android.animation.Animator.AnimatorListener
            public final void onAnimationStart(Animator animator) {
                Runnable runnable2 = runnable;
                if (runnable2 != null) {
                    runnable2.run();
                }
                try {
                    AnimatorListenerAdapter animatorListenerAdapter = SplashscreenIconDrawableFactory$AnimatableIconAnimateListener.this.mJankMonitoringListener;
                    if (animatorListenerAdapter != null) {
                        animatorListenerAdapter.onAnimationStart(animator);
                    }
                    SplashscreenIconDrawableFactory$AnimatableIconAnimateListener.this.mAnimatableIcon.start();
                } catch (Exception e) {
                    Log.e("ShellStartingWindow", "Error while running the splash screen animated icon", e);
                    animator.cancel();
                }
            }
        });
        return true;
    }

    public final void stopAnimation() {
        ValueAnimator valueAnimator = this.mIconAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mIconAnimator.end();
            this.mJankMonitoringListener = null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [android.graphics.drawable.Drawable$Callback, com.android.wm.shell.startingsurface.SplashscreenIconDrawableFactory$AnimatableIconAnimateListener$2] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public SplashscreenIconDrawableFactory$AnimatableIconAnimateListener(android.graphics.drawable.Drawable r1) {
        /*
            r0 = this;
            r0.<init>(r1)
            com.android.wm.shell.startingsurface.SplashscreenIconDrawableFactory$AnimatableIconAnimateListener$2 r1 = new com.android.wm.shell.startingsurface.SplashscreenIconDrawableFactory$AnimatableIconAnimateListener$2
            r1.<init>()
            r0.mCallback = r1
            android.graphics.drawable.Drawable r0 = r0.mForegroundDrawable
            r0.setCallback(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.startingsurface.SplashscreenIconDrawableFactory$AnimatableIconAnimateListener.<init>(android.graphics.drawable.Drawable):void");
    }

    public final void setAnimationJankMonitoring(AnimatorListenerAdapter animatorListenerAdapter) {
        this.mJankMonitoringListener = animatorListenerAdapter;
    }
}
