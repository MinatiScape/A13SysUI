package com.android.systemui.util.wakelock;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.animation.Animation;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.util.Assert;
/* loaded from: classes.dex */
public final class KeepAwakeAnimationListener extends AnimatorListenerAdapter implements Animation.AnimationListener {
    @VisibleForTesting
    public static WakeLock sWakeLock;

    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
    public final void onAnimationEnd(Animator animator) {
        Assert.isMainThread();
        sWakeLock.release("KeepAwakeAnimListener");
    }

    @Override // android.view.animation.Animation.AnimationListener
    public final void onAnimationRepeat(Animation animation) {
    }

    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
    public final void onAnimationStart(Animator animator) {
        Assert.isMainThread();
        sWakeLock.acquire("KeepAwakeAnimListener");
    }

    public KeepAwakeAnimationListener(Context context) {
        Assert.isMainThread();
        if (sWakeLock == null) {
            sWakeLock = WakeLock.createPartial(context, "animation");
        }
    }

    @Override // android.view.animation.Animation.AnimationListener
    public final void onAnimationEnd(Animation animation) {
        Assert.isMainThread();
        sWakeLock.release("KeepAwakeAnimListener");
    }

    @Override // android.view.animation.Animation.AnimationListener
    public final void onAnimationStart(Animation animation) {
        Assert.isMainThread();
        sWakeLock.acquire("KeepAwakeAnimListener");
    }
}
