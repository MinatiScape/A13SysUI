package com.android.wm.shell.back;

import android.animation.ValueAnimator;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.MathUtils;
import android.view.Choreographer;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;
import android.view.SyncRtSurfaceTransactionApplier;
import android.view.View;
import android.view.animation.PathInterpolator;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import com.android.wm.shell.animation.Interpolators;
import com.android.wm.shell.startingsurface.SplashScreenExitAnimation;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BackAnimationController$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ BackAnimationController$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        SurfaceControl surfaceControl;
        SplashScreenExitAnimation splashScreenExitAnimation;
        int i;
        switch (this.$r8$classId) {
            case 0:
                BackAnimationController backAnimationController = (BackAnimationController) this.f$0;
                Objects.requireNonNull(backAnimationController);
                if (backAnimationController.mBackNavigationInfo != null) {
                    float animatedFraction = valueAnimator.getAnimatedFraction();
                    float f = backAnimationController.mTouchEventDelta.x;
                    int round = Math.round(f - (f * animatedFraction));
                    float f2 = backAnimationController.mTouchEventDelta.y;
                    int round2 = Math.round(f2 - (animatedFraction * f2));
                    RemoteAnimationTarget departingAnimationTarget = backAnimationController.mBackNavigationInfo.getDepartingAnimationTarget();
                    if (departingAnimationTarget != null) {
                        backAnimationController.mTransaction.setPosition(departingAnimationTarget.leash, round, round2);
                        backAnimationController.mTransaction.apply();
                        return;
                    }
                    return;
                }
                return;
            default:
                SplashScreenExitAnimation splashScreenExitAnimation2 = (SplashScreenExitAnimation) this.f$0;
                PathInterpolator pathInterpolator = SplashScreenExitAnimation.ICON_INTERPOLATOR;
                Objects.requireNonNull(splashScreenExitAnimation2);
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                float interpolation = SplashScreenExitAnimation.ICON_INTERPOLATOR.getInterpolation(MathUtils.constrain(((splashScreenExitAnimation2.mAnimationDuration * floatValue) - ((float) 0)) / splashScreenExitAnimation2.mIconFadeOutDuration, 0.0f, 1.0f));
                View iconView = splashScreenExitAnimation2.mSplashScreenView.getIconView();
                View brandingView = splashScreenExitAnimation2.mSplashScreenView.getBrandingView();
                if (iconView != null) {
                    iconView.setAlpha((1.0f - interpolation) * splashScreenExitAnimation2.mIconStartAlpha);
                }
                if (brandingView != null) {
                    brandingView.setAlpha((1.0f - interpolation) * splashScreenExitAnimation2.mBrandingStartAlpha);
                }
                float constrain = MathUtils.constrain(((floatValue * splashScreenExitAnimation2.mAnimationDuration) - splashScreenExitAnimation2.mAppRevealDelay) / splashScreenExitAnimation2.mAppRevealDuration, 0.0f, 1.0f);
                SplashScreenExitAnimation.RadialVanishAnimation radialVanishAnimation = splashScreenExitAnimation2.mRadialVanishAnimation;
                if (!(radialVanishAnimation == null || radialVanishAnimation.mVanishPaint.getShader() == null)) {
                    float interpolation2 = SplashScreenExitAnimation.MASK_RADIUS_INTERPOLATOR.getInterpolation(constrain);
                    float interpolation3 = Interpolators.ALPHA_OUT.getInterpolation(constrain);
                    float f3 = ((radialVanishAnimation.mFinishRadius - i) * interpolation2) + radialVanishAnimation.mInitRadius;
                    radialVanishAnimation.mVanishMatrix.setScale(f3, f3);
                    Matrix matrix = radialVanishAnimation.mVanishMatrix;
                    Point point = radialVanishAnimation.mCircleCenter;
                    matrix.postTranslate(point.x, point.y);
                    radialVanishAnimation.mVanishPaint.getShader().setLocalMatrix(radialVanishAnimation.mVanishMatrix);
                    radialVanishAnimation.mVanishPaint.setAlpha(Math.round(interpolation3 * 255.0f));
                    radialVanishAnimation.postInvalidate();
                }
                SplashScreenExitAnimation.ShiftUpAnimation shiftUpAnimation = splashScreenExitAnimation2.mShiftUpAnimation;
                if (shiftUpAnimation != null && (surfaceControl = SplashScreenExitAnimation.this.mFirstWindowSurface) != null && surfaceControl.isValid() && SplashScreenExitAnimation.this.mSplashScreenView.isAttachedToWindow()) {
                    float interpolation4 = SplashScreenExitAnimation.SHIFT_UP_INTERPOLATOR.getInterpolation(constrain);
                    float f4 = shiftUpAnimation.mFromYDelta;
                    float m = MotionController$$ExternalSyntheticOutline0.m(shiftUpAnimation.mToYDelta, f4, interpolation4, f4);
                    shiftUpAnimation.mOccludeHoleView.setTranslationY(m);
                    shiftUpAnimation.mTmpTransform.setTranslate(0.0f, m);
                    SurfaceControl.Transaction acquire = SplashScreenExitAnimation.this.mTransactionPool.acquire();
                    acquire.setFrameTimelineVsync(Choreographer.getSfInstance().getVsyncId());
                    Matrix matrix2 = shiftUpAnimation.mTmpTransform;
                    Rect rect = SplashScreenExitAnimation.this.mFirstWindowFrame;
                    matrix2.postTranslate(rect.left, rect.top + splashScreenExitAnimation.mMainWindowShiftLength);
                    shiftUpAnimation.mApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(SplashScreenExitAnimation.this.mFirstWindowSurface).withMatrix(shiftUpAnimation.mTmpTransform).withMergeTransaction(acquire).build()});
                    SplashScreenExitAnimation.this.mTransactionPool.release(acquire);
                    return;
                }
                return;
        }
    }
}
