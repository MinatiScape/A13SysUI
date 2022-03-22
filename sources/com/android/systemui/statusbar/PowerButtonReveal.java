package com.android.systemui.statusbar;

import com.android.systemui.animation.Interpolators;
/* compiled from: LightRevealScrim.kt */
/* loaded from: classes.dex */
public final class PowerButtonReveal implements LightRevealEffect {
    public final float powerButtonY;

    @Override // com.android.systemui.statusbar.LightRevealEffect
    public final void setRevealAmountOnScrim(float f, LightRevealScrim lightRevealScrim) {
        float interpolation = Interpolators.FAST_OUT_SLOW_IN_REVERSE.getInterpolation(f);
        float f2 = interpolation - 0.5f;
        if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        lightRevealScrim.setRevealGradientEndColorAlpha(1.0f - (2.0f * f2));
        lightRevealScrim.interpolatedRevealAmount = interpolation;
        lightRevealScrim.setRevealGradientBounds((lightRevealScrim.getWidth() * 1.05f) - ((lightRevealScrim.getWidth() * 1.25f) * interpolation), this.powerButtonY - (lightRevealScrim.getHeight() * interpolation), (lightRevealScrim.getWidth() * 1.25f * interpolation) + (lightRevealScrim.getWidth() * 1.05f), (lightRevealScrim.getHeight() * interpolation) + this.powerButtonY);
    }

    public PowerButtonReveal(float f) {
        this.powerButtonY = f;
    }
}
