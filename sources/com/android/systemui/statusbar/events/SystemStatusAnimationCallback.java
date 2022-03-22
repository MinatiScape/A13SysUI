package com.android.systemui.statusbar.events;

import android.animation.ValueAnimator;
/* compiled from: SystemStatusAnimationScheduler.kt */
/* loaded from: classes.dex */
public interface SystemStatusAnimationCallback {
    default void onHidePersistentDot() {
    }

    default void onSystemChromeAnimationEnd() {
    }

    default void onSystemChromeAnimationStart() {
    }

    default void onSystemChromeAnimationUpdate(ValueAnimator valueAnimator) {
    }

    default void onSystemStatusAnimationTransitionToPersistentDot(String str) {
    }
}
