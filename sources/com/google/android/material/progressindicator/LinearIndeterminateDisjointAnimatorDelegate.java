package com.google.android.material.progressindicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Property;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import androidx.leanback.R$string;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import com.google.android.material.progressindicator.BaseProgressIndicator;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public final class LinearIndeterminateDisjointAnimatorDelegate extends IndeterminateAnimatorDelegate<ObjectAnimator> {
    public float animationFraction;
    public ObjectAnimator animator;
    public final LinearProgressIndicatorSpec baseSpec;
    public ObjectAnimator completeEndAnimator;
    public boolean dirtyColors;
    public final Interpolator[] interpolatorArray;
    public static final int[] DURATION_TO_MOVE_SEGMENT_ENDS = {533, 567, 850, 750};
    public static final int[] DELAY_TO_MOVE_SEGMENT_ENDS = {1267, 1000, 333, 0};
    public static final AnonymousClass3 ANIMATION_FRACTION = new Property<LinearIndeterminateDisjointAnimatorDelegate, Float>() { // from class: com.google.android.material.progressindicator.LinearIndeterminateDisjointAnimatorDelegate.3
        @Override // android.util.Property
        public final Float get(LinearIndeterminateDisjointAnimatorDelegate linearIndeterminateDisjointAnimatorDelegate) {
            LinearIndeterminateDisjointAnimatorDelegate linearIndeterminateDisjointAnimatorDelegate2 = linearIndeterminateDisjointAnimatorDelegate;
            Objects.requireNonNull(linearIndeterminateDisjointAnimatorDelegate2);
            return Float.valueOf(linearIndeterminateDisjointAnimatorDelegate2.animationFraction);
        }

        @Override // android.util.Property
        public final void set(LinearIndeterminateDisjointAnimatorDelegate linearIndeterminateDisjointAnimatorDelegate, Float f) {
            linearIndeterminateDisjointAnimatorDelegate.setAnimationFraction(f.floatValue());
        }
    };
    public int indicatorColorIndex = 0;
    public Animatable2Compat.AnimationCallback animatorCompleteCallback = null;

    public LinearIndeterminateDisjointAnimatorDelegate(Context context, LinearProgressIndicatorSpec linearProgressIndicatorSpec) {
        super(2);
        this.baseSpec = linearProgressIndicatorSpec;
        this.interpolatorArray = new Interpolator[]{AnimationUtils.loadInterpolator(context, 2130837534), AnimationUtils.loadInterpolator(context, 2130837535), AnimationUtils.loadInterpolator(context, 2130837536), AnimationUtils.loadInterpolator(context, 2130837537)};
    }

    public void resetPropertiesForNewStart() {
        this.indicatorColorIndex = 0;
        int i = this.baseSpec.indicatorColors[0];
        IndeterminateDrawable indeterminateDrawable = this.drawable;
        Objects.requireNonNull(indeterminateDrawable);
        int compositeARGBWithAlpha = R$string.compositeARGBWithAlpha(i, indeterminateDrawable.totalAlpha);
        int[] iArr = this.segmentColors;
        iArr[0] = compositeARGBWithAlpha;
        iArr[1] = compositeARGBWithAlpha;
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public final void unregisterAnimatorsCompleteCallback() {
        this.animatorCompleteCallback = null;
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public final void cancelAnimatorImmediately() {
        ObjectAnimator objectAnimator = this.animator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public final void requestCancelAnimatorAfterCurrentCycle() {
        ObjectAnimator objectAnimator = this.completeEndAnimator;
        if (objectAnimator != null && !objectAnimator.isRunning()) {
            cancelAnimatorImmediately();
            if (this.drawable.isVisible()) {
                this.completeEndAnimator.setFloatValues(this.animationFraction, 1.0f);
                this.completeEndAnimator.setDuration((1.0f - this.animationFraction) * 1800.0f);
                this.completeEndAnimator.start();
            }
        }
    }

    public void setAnimationFraction(float f) {
        this.animationFraction = f;
        int i = (int) (f * 1800.0f);
        for (int i2 = 0; i2 < 4; i2++) {
            this.segmentPositions[i2] = Math.max(0.0f, Math.min(1.0f, this.interpolatorArray[i2].getInterpolation((i - DELAY_TO_MOVE_SEGMENT_ENDS[i2]) / DURATION_TO_MOVE_SEGMENT_ENDS[i2])));
        }
        if (this.dirtyColors) {
            int[] iArr = this.segmentColors;
            int i3 = this.baseSpec.indicatorColors[this.indicatorColorIndex];
            IndeterminateDrawable indeterminateDrawable = this.drawable;
            Objects.requireNonNull(indeterminateDrawable);
            Arrays.fill(iArr, R$string.compositeARGBWithAlpha(i3, indeterminateDrawable.totalAlpha));
            this.dirtyColors = false;
        }
        this.drawable.invalidateSelf();
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public final void startAnimator() {
        if (this.animator == null) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, ANIMATION_FRACTION, 0.0f, 1.0f);
            this.animator = ofFloat;
            ofFloat.setDuration(1800L);
            this.animator.setInterpolator(null);
            this.animator.setRepeatCount(-1);
            this.animator.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.material.progressindicator.LinearIndeterminateDisjointAnimatorDelegate.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationRepeat(Animator animator) {
                    super.onAnimationRepeat(animator);
                    LinearIndeterminateDisjointAnimatorDelegate linearIndeterminateDisjointAnimatorDelegate = LinearIndeterminateDisjointAnimatorDelegate.this;
                    linearIndeterminateDisjointAnimatorDelegate.indicatorColorIndex = (linearIndeterminateDisjointAnimatorDelegate.indicatorColorIndex + 1) % linearIndeterminateDisjointAnimatorDelegate.baseSpec.indicatorColors.length;
                    linearIndeterminateDisjointAnimatorDelegate.dirtyColors = true;
                }
            });
        }
        if (this.completeEndAnimator == null) {
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, ANIMATION_FRACTION, 1.0f);
            this.completeEndAnimator = ofFloat2;
            ofFloat2.setDuration(1800L);
            this.completeEndAnimator.setInterpolator(null);
            this.completeEndAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.material.progressindicator.LinearIndeterminateDisjointAnimatorDelegate.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    LinearIndeterminateDisjointAnimatorDelegate.this.cancelAnimatorImmediately();
                    LinearIndeterminateDisjointAnimatorDelegate linearIndeterminateDisjointAnimatorDelegate = LinearIndeterminateDisjointAnimatorDelegate.this;
                    Animatable2Compat.AnimationCallback animationCallback = linearIndeterminateDisjointAnimatorDelegate.animatorCompleteCallback;
                    if (animationCallback != null) {
                        animationCallback.onAnimationEnd(linearIndeterminateDisjointAnimatorDelegate.drawable);
                    }
                }
            });
        }
        resetPropertiesForNewStart();
        this.animator.start();
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public final void registerAnimatorsCompleteCallback(BaseProgressIndicator.AnonymousClass3 r1) {
        this.animatorCompleteCallback = r1;
    }
}
