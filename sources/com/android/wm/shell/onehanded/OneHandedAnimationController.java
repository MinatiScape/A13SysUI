package com.android.wm.shell.onehanded;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.view.SurfaceControl;
import android.view.animation.BaseInterpolator;
import android.window.WindowContainerToken;
import androidx.core.view.ViewCompat$$ExternalSyntheticLambda0;
import com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda4;
import com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda5;
import com.android.wm.shell.onehanded.OneHandedAnimationController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class OneHandedAnimationController {
    public final HashMap<WindowContainerToken, OneHandedTransitionAnimator> mAnimatorMap = new HashMap<>();
    public final OneHandedInterpolator mInterpolator = new OneHandedInterpolator();
    public final OneHandedSurfaceTransactionHelper mSurfaceTransactionHelper;

    /* loaded from: classes.dex */
    public class OneHandedInterpolator extends BaseInterpolator {
        @Override // android.animation.TimeInterpolator
        public final float getInterpolation(float f) {
            return (float) ((Math.sin((((f - 4.0f) / 4.0f) * 6.283185307179586d) / 4.0d) * Math.pow(2.0d, (-10.0f) * f)) + 1.0d);
        }
    }

    /* loaded from: classes.dex */
    public static abstract class OneHandedTransitionAnimator extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
        public static final /* synthetic */ int $r8$clinit = 0;
        public float mCurrentValue;
        public float mEndValue;
        public final SurfaceControl mLeash;
        public float mStartValue;
        public OneHandedSurfaceTransactionHelper mSurfaceTransactionHelper;
        public final WindowContainerToken mToken;
        public final ArrayList mOneHandedAnimationCallbacks = new ArrayList();
        public ViewCompat$$ExternalSyntheticLambda0 mSurfaceControlTransactionFactory = ViewCompat$$ExternalSyntheticLambda0.INSTANCE;
        public int mTransitionDirection = 0;

        public abstract void applySurfaceControlTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, float f);

        @Override // android.animation.Animator.AnimatorListener
        public final void onAnimationRepeat(Animator animator) {
        }

        public static OneHandedTransitionAnimator ofYOffset(WindowContainerToken windowContainerToken, SurfaceControl surfaceControl, float f, float f2, Rect rect) {
            return new OneHandedTransitionAnimator(windowContainerToken, surfaceControl, f, f2, rect) { // from class: com.android.wm.shell.onehanded.OneHandedAnimationController.OneHandedTransitionAnimator.1
                public final Rect mTmpRect;

                @Override // com.android.wm.shell.onehanded.OneHandedAnimationController.OneHandedTransitionAnimator
                public final void applySurfaceControlTransaction(SurfaceControl surfaceControl2, SurfaceControl.Transaction transaction, float f3) {
                    float f4 = this.mStartValue;
                    float f5 = (this.mEndValue * f3) + ((1.0f - f3) * f4) + 0.5f;
                    Rect rect2 = this.mTmpRect;
                    int i = rect2.left;
                    int round = Math.round(f5) + rect2.top;
                    Rect rect3 = this.mTmpRect;
                    rect2.set(i, round, rect3.right, Math.round(f5) + rect3.bottom);
                    this.mCurrentValue = f5;
                    OneHandedSurfaceTransactionHelper oneHandedSurfaceTransactionHelper = this.mSurfaceTransactionHelper;
                    Rect rect4 = this.mTmpRect;
                    Objects.requireNonNull(oneHandedSurfaceTransactionHelper);
                    transaction.setWindowCrop(surfaceControl2, rect4.width(), rect4.height());
                    if (oneHandedSurfaceTransactionHelper.mEnableCornerRadius) {
                        transaction.setCornerRadius(surfaceControl2, oneHandedSurfaceTransactionHelper.mCornerRadius);
                    }
                    transaction.setPosition(surfaceControl2, 0.0f, f5);
                    transaction.apply();
                }

                {
                    this.mTmpRect = new Rect(rect);
                }
            };
        }

        @Override // android.animation.Animator.AnimatorListener
        public final void onAnimationCancel(Animator animator) {
            this.mCurrentValue = this.mEndValue;
            this.mOneHandedAnimationCallbacks.forEach(new WMShell$$ExternalSyntheticLambda5(this, 3));
            this.mOneHandedAnimationCallbacks.clear();
        }

        @Override // android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            this.mCurrentValue = this.mEndValue;
            Objects.requireNonNull(this.mSurfaceControlTransactionFactory);
            final SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
            this.mOneHandedAnimationCallbacks.forEach(new Consumer() { // from class: com.android.wm.shell.onehanded.OneHandedAnimationController$OneHandedTransitionAnimator$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator = OneHandedAnimationController.OneHandedTransitionAnimator.this;
                    Objects.requireNonNull(oneHandedTransitionAnimator);
                    ((OneHandedAnimationCallback) obj).onOneHandedAnimationEnd(oneHandedTransitionAnimator);
                }
            });
            this.mOneHandedAnimationCallbacks.clear();
        }

        @Override // android.animation.Animator.AnimatorListener
        public final void onAnimationStart(Animator animator) {
            this.mCurrentValue = this.mStartValue;
            this.mOneHandedAnimationCallbacks.forEach(new WMShell$$ExternalSyntheticLambda4(this, 2));
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
            Objects.requireNonNull(this.mSurfaceControlTransactionFactory);
            final SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
            this.mOneHandedAnimationCallbacks.forEach(new Consumer() { // from class: com.android.wm.shell.onehanded.OneHandedAnimationController$OneHandedTransitionAnimator$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator = OneHandedAnimationController.OneHandedTransitionAnimator.this;
                    Objects.requireNonNull(oneHandedTransitionAnimator);
                    ((OneHandedAnimationCallback) obj).onAnimationUpdate(oneHandedTransitionAnimator.mCurrentValue);
                }
            });
            applySurfaceControlTransaction(this.mLeash, transaction, valueAnimator.getAnimatedFraction());
        }

        public OneHandedTransitionAnimator(WindowContainerToken windowContainerToken, SurfaceControl surfaceControl, float f, float f2) {
            this.mLeash = surfaceControl;
            this.mToken = windowContainerToken;
            this.mStartValue = f;
            this.mEndValue = f2;
            addListener(this);
            addUpdateListener(this);
        }
    }

    public final OneHandedTransitionAnimator setupOneHandedTransitionAnimator(OneHandedTransitionAnimator oneHandedTransitionAnimator) {
        OneHandedSurfaceTransactionHelper oneHandedSurfaceTransactionHelper = this.mSurfaceTransactionHelper;
        Objects.requireNonNull(oneHandedTransitionAnimator);
        oneHandedTransitionAnimator.mSurfaceTransactionHelper = oneHandedSurfaceTransactionHelper;
        oneHandedTransitionAnimator.setInterpolator(this.mInterpolator);
        oneHandedTransitionAnimator.setFloatValues(0.0f, 1.0f);
        return oneHandedTransitionAnimator;
    }

    public OneHandedAnimationController(Context context) {
        this.mSurfaceTransactionHelper = new OneHandedSurfaceTransactionHelper(context);
    }
}
