package com.android.wm.shell.onehanded;

import com.android.wm.shell.onehanded.OneHandedAnimationController;
/* loaded from: classes.dex */
public interface OneHandedAnimationCallback {
    default void onAnimationUpdate(float f) {
    }

    default void onOneHandedAnimationCancel(OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator) {
    }

    default void onOneHandedAnimationEnd(OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator) {
    }

    default void onOneHandedAnimationStart(OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator) {
    }
}
