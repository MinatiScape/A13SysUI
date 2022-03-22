package com.android.systemui.statusbar;

import android.view.animation.PathInterpolator;
import com.android.systemui.animation.Interpolators;
/* compiled from: LightRevealScrim.kt */
/* loaded from: classes.dex */
public final class LiftReveal implements LightRevealEffect {
    public static final LiftReveal INSTANCE = new LiftReveal();
    public static final PathInterpolator INTERPOLATOR = Interpolators.FAST_OUT_SLOW_IN_REVERSE;

    @Override // com.android.systemui.statusbar.LightRevealEffect
    public final void setRevealAmountOnScrim(float f, LightRevealScrim lightRevealScrim) {
        float interpolation = INTERPOLATOR.getInterpolation(f);
        float f2 = interpolation - 0.35f;
        float f3 = 0.0f;
        if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        float f4 = 1.5384616f * f2;
        float f5 = f - 0.85f;
        if (f5 >= 0.0f) {
            f3 = f5;
        }
        lightRevealScrim.setRevealGradientEndColorAlpha(1.0f - (6.666668f * f3));
        lightRevealScrim.setRevealGradientBounds(((-lightRevealScrim.getWidth()) * f4) + (lightRevealScrim.getWidth() * 0.25f), (lightRevealScrim.getHeight() * 1.1f) - (lightRevealScrim.getHeight() * interpolation), (lightRevealScrim.getWidth() * f4) + (lightRevealScrim.getWidth() * 0.75f), (lightRevealScrim.getHeight() * interpolation) + (lightRevealScrim.getHeight() * 1.2f));
    }
}
