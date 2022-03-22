package com.google.android.material.progressindicator;

import android.animation.Animator;
import com.google.android.material.progressindicator.BaseProgressIndicator;
/* loaded from: classes.dex */
public abstract class IndeterminateAnimatorDelegate<T extends Animator> {
    public IndeterminateDrawable drawable;
    public final int[] segmentColors;
    public final float[] segmentPositions;

    public abstract void cancelAnimatorImmediately();

    public abstract void registerAnimatorsCompleteCallback(BaseProgressIndicator.AnonymousClass3 r1);

    public abstract void requestCancelAnimatorAfterCurrentCycle();

    public abstract void startAnimator();

    public abstract void unregisterAnimatorsCompleteCallback();

    public IndeterminateAnimatorDelegate(int i) {
        this.segmentPositions = new float[i * 2];
        this.segmentColors = new int[i];
    }
}
