package com.android.systemui.dreams.touch.dagger;

import com.android.wm.shell.animation.FlingAnimationUtils;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class BouncerSwipeModule_ProvidesSwipeToBouncerFlingAnimationUtilsOpeningFactory implements Factory<FlingAnimationUtils> {
    public static FlingAnimationUtils providesSwipeToBouncerFlingAnimationUtilsOpening(Provider<FlingAnimationUtils.Builder> provider) {
        FlingAnimationUtils.Builder builder = provider.mo144get();
        builder.reset();
        builder.mMaxLengthSeconds = 0.6f;
        builder.mSpeedUpFactor = 0.6f;
        return builder.build();
    }
}
