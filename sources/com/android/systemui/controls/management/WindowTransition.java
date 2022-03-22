package com.android.systemui.controls.management;

import android.animation.Animator;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsAnimations.kt */
/* loaded from: classes.dex */
public final class WindowTransition extends Transition {
    public final Function1<View, Animator> animator;

    @Override // android.transition.Transition
    public final void captureEndValues(TransitionValues transitionValues) {
        transitionValues.values.put("item", Float.valueOf(1.0f));
    }

    @Override // android.transition.Transition
    public final void captureStartValues(TransitionValues transitionValues) {
        transitionValues.values.put("item", Float.valueOf(0.0f));
    }

    @Override // android.transition.Transition
    public final Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        Function1<View, Animator> function1 = this.animator;
        Intrinsics.checkNotNull(transitionValues);
        return function1.invoke(transitionValues.view);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public WindowTransition(Function1<? super View, ? extends Animator> function1) {
        this.animator = function1;
    }
}
