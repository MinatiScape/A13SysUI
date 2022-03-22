package com.android.keyguard;

import android.animation.TimeInterpolator;
import android.view.animation.PathInterpolator;
/* compiled from: AnimatableClockView.kt */
/* loaded from: classes.dex */
public final class AnimatableClockView$setTextStyle$1 implements Runnable {
    public final /* synthetic */ Integer $color;
    public final /* synthetic */ long $delay;
    public final /* synthetic */ long $duration;
    public final /* synthetic */ TimeInterpolator $interpolator;
    public final /* synthetic */ Runnable $onAnimationEnd;
    public final /* synthetic */ float $textSize = -1.0f;
    public final /* synthetic */ int $weight;
    public final /* synthetic */ AnimatableClockView this$0;

    public AnimatableClockView$setTextStyle$1(AnimatableClockView animatableClockView, int i, Integer num, long j, PathInterpolator pathInterpolator, long j2, AnimatableClockView$animateCharge$startAnimPhase2$1 animatableClockView$animateCharge$startAnimPhase2$1) {
        this.this$0 = animatableClockView;
        this.$weight = i;
        this.$color = num;
        this.$duration = j;
        this.$interpolator = pathInterpolator;
        this.$delay = j2;
        this.$onAnimationEnd = animatableClockView$animateCharge$startAnimPhase2$1;
    }

    @Override // java.lang.Runnable
    public final void run() {
        TextAnimator textAnimator = this.this$0.textAnimator;
        if (textAnimator != null) {
            textAnimator.setTextStyle(this.$weight, this.$textSize, this.$color, false, this.$duration, this.$interpolator, this.$delay, this.$onAnimationEnd);
        }
    }
}
