package com.android.systemui.statusbar.notification;

import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.animation.LaunchAnimator;
/* compiled from: ExpandAnimationParameters.kt */
/* loaded from: classes.dex */
public final class ExpandAnimationParameters extends LaunchAnimator.State {
    public float linearProgress;
    public int parentStartClipTopAmount;
    public int parentStartRoundedTopClipping;
    public float progress;
    public int startClipTopAmount;
    public float startNotificationTop;
    public int startRoundedTopClipping;
    public float startTranslationZ;

    @VisibleForTesting
    public ExpandAnimationParameters() {
        super(0, 0, 0, 0, 0.0f, 0.0f);
    }

    public ExpandAnimationParameters(int i, int i2, int i3, int i4, float f, float f2) {
        super(i, i2, i3, i4, f, f2);
    }
}
