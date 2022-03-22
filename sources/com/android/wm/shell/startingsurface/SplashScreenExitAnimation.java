package com.android.wm.shell.startingsurface;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Choreographer;
import android.view.SurfaceControl;
import android.view.SyncRtSurfaceTransactionApplier;
import android.view.View;
import android.view.animation.PathInterpolator;
import android.window.SplashScreenView;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda3;
import com.android.wm.shell.common.TransactionPool;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SplashScreenExitAnimation implements Animator.AnimatorListener {
    public static final PathInterpolator ICON_INTERPOLATOR = new PathInterpolator(0.15f, 0.0f, 1.0f, 1.0f);
    public static final PathInterpolator MASK_RADIUS_INTERPOLATOR = new PathInterpolator(0.0f, 0.0f, 0.4f, 1.0f);
    public static final PathInterpolator SHIFT_UP_INTERPOLATOR = new PathInterpolator(0.0f, 0.0f, 0.0f, 1.0f);
    public final int mAnimationDuration;
    public final int mAppRevealDelay;
    public final int mAppRevealDuration;
    public final float mBrandingStartAlpha;
    public Runnable mFinishCallback;
    public final Rect mFirstWindowFrame;
    public final SurfaceControl mFirstWindowSurface;
    public final int mIconFadeOutDuration;
    public final float mIconStartAlpha;
    public final int mMainWindowShiftLength;
    public RadialVanishAnimation mRadialVanishAnimation;
    public ShiftUpAnimation mShiftUpAnimation;
    public final SplashScreenView mSplashScreenView;
    public final TransactionPool mTransactionPool;

    /* loaded from: classes.dex */
    public final class ShiftUpAnimation {
        public final SyncRtSurfaceTransactionApplier mApplier;
        public final View mOccludeHoleView;
        public final float mToYDelta;
        public final Matrix mTmpTransform = new Matrix();
        public final float mFromYDelta = 0.0f;

        public ShiftUpAnimation(float f, View view) {
            this.mToYDelta = f;
            this.mOccludeHoleView = view;
            this.mApplier = new SyncRtSurfaceTransactionApplier(view);
        }
    }

    @Override // android.animation.Animator.AnimatorListener
    public final void onAnimationRepeat(Animator animator) {
    }

    /* loaded from: classes.dex */
    public static class RadialVanishAnimation extends View {
        public int mFinishRadius;
        public int mInitRadius;
        public final Paint mVanishPaint;
        public final SplashScreenView mView;
        public final Point mCircleCenter = new Point();
        public final Matrix mVanishMatrix = new Matrix();

        public RadialVanishAnimation(SplashScreenView splashScreenView) {
            super(splashScreenView.getContext());
            Paint paint = new Paint(1);
            this.mVanishPaint = paint;
            this.mView = splashScreenView;
            splashScreenView.addView(this);
            paint.setAlpha(0);
        }

        @Override // android.view.View
        public final void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawRect(0.0f, 0.0f, this.mView.getWidth(), this.mView.getHeight(), this.mVanishPaint);
        }
    }

    public final void reset() {
        if (this.mSplashScreenView.isAttachedToWindow()) {
            this.mSplashScreenView.setVisibility(8);
            Runnable runnable = this.mFinishCallback;
            if (runnable != null) {
                runnable.run();
                this.mFinishCallback = null;
            }
        }
        ShiftUpAnimation shiftUpAnimation = this.mShiftUpAnimation;
        if (shiftUpAnimation != null) {
            Objects.requireNonNull(shiftUpAnimation);
            SurfaceControl surfaceControl = SplashScreenExitAnimation.this.mFirstWindowSurface;
            if (surfaceControl != null && surfaceControl.isValid()) {
                SurfaceControl.Transaction acquire = SplashScreenExitAnimation.this.mTransactionPool.acquire();
                if (SplashScreenExitAnimation.this.mSplashScreenView.isAttachedToWindow()) {
                    acquire.setFrameTimelineVsync(Choreographer.getSfInstance().getVsyncId());
                    shiftUpAnimation.mApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(SplashScreenExitAnimation.this.mFirstWindowSurface).withWindowCrop((Rect) null).withMergeTransaction(acquire).build()});
                } else {
                    acquire.setWindowCrop(SplashScreenExitAnimation.this.mFirstWindowSurface, null);
                    acquire.apply();
                }
                SplashScreenExitAnimation.this.mTransactionPool.release(acquire);
                Choreographer sfInstance = Choreographer.getSfInstance();
                SurfaceControl surfaceControl2 = SplashScreenExitAnimation.this.mFirstWindowSurface;
                Objects.requireNonNull(surfaceControl2);
                sfInstance.postCallback(4, new TaskView$$ExternalSyntheticLambda3(surfaceControl2, 10), null);
            }
        }
    }

    public SplashScreenExitAnimation(Context context, SplashScreenView splashScreenView, SurfaceControl surfaceControl, Rect rect, int i, TransactionPool transactionPool, Runnable runnable) {
        Rect rect2 = new Rect();
        this.mFirstWindowFrame = rect2;
        this.mSplashScreenView = splashScreenView;
        this.mFirstWindowSurface = surfaceControl;
        if (rect != null) {
            rect2.set(rect);
        }
        View iconView = splashScreenView.getIconView();
        if (iconView == null || iconView.getLayoutParams().width == 0 || iconView.getLayoutParams().height == 0) {
            this.mIconFadeOutDuration = 0;
            this.mIconStartAlpha = 0.0f;
            this.mBrandingStartAlpha = 0.0f;
            this.mAppRevealDelay = 0;
        } else {
            iconView.setLayerType(2, null);
            View brandingView = splashScreenView.getBrandingView();
            if (brandingView != null) {
                this.mBrandingStartAlpha = brandingView.getAlpha();
            } else {
                this.mBrandingStartAlpha = 0.0f;
            }
            this.mIconFadeOutDuration = context.getResources().getInteger(2131493041);
            this.mAppRevealDelay = context.getResources().getInteger(2131493039);
            this.mIconStartAlpha = iconView.getAlpha();
        }
        int integer = context.getResources().getInteger(2131493040);
        this.mAppRevealDuration = integer;
        this.mAnimationDuration = Math.max(this.mIconFadeOutDuration, this.mAppRevealDelay + integer);
        this.mMainWindowShiftLength = i;
        this.mFinishCallback = runnable;
        this.mTransactionPool = transactionPool;
    }

    @Override // android.animation.Animator.AnimatorListener
    public final void onAnimationCancel(Animator animator) {
        reset();
        InteractionJankMonitor.getInstance().cancel(39);
    }

    @Override // android.animation.Animator.AnimatorListener
    public final void onAnimationEnd(Animator animator) {
        reset();
        InteractionJankMonitor.getInstance().end(39);
    }

    @Override // android.animation.Animator.AnimatorListener
    public final void onAnimationStart(Animator animator) {
        InteractionJankMonitor.getInstance().begin(this.mSplashScreenView, 39);
    }
}
