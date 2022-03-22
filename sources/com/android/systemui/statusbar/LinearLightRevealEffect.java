package com.android.systemui.statusbar;

import android.util.MathUtils;
import android.view.animation.PathInterpolator;
import com.android.systemui.animation.Interpolators;
/* compiled from: LightRevealScrim.kt */
/* loaded from: classes.dex */
public final class LinearLightRevealEffect implements LightRevealEffect {
    public final PathInterpolator INTERPOLATOR = Interpolators.FAST_OUT_SLOW_IN_REVERSE;
    public final boolean isVertical;

    @Override // com.android.systemui.statusbar.LightRevealEffect
    public final void setRevealAmountOnScrim(float f, LightRevealScrim lightRevealScrim) {
        float interpolation = this.INTERPOLATOR.getInterpolation(f);
        lightRevealScrim.interpolatedRevealAmount = interpolation;
        boolean z = true;
        float f2 = (1 - interpolation) - 0.7f;
        if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        float f3 = 3.3333333f * f2;
        if (lightRevealScrim.startColorAlpha != f3) {
            z = false;
        }
        if (!z) {
            lightRevealScrim.startColorAlpha = f3;
            lightRevealScrim.invalidate();
        }
        float f4 = interpolation - 0.6f;
        if (f4 < 0.0f) {
            f4 = 0.0f;
        }
        lightRevealScrim.setRevealGradientEndColorAlpha(1.0f - (2.5000002f * f4));
        float lerp = MathUtils.lerp(0.3f, 1.0f, interpolation);
        if (this.isVertical) {
            lightRevealScrim.setRevealGradientBounds((lightRevealScrim.getWidth() / 2) - ((lightRevealScrim.getWidth() / 2) * lerp), 0.0f, ((lightRevealScrim.getWidth() / 2) * lerp) + (lightRevealScrim.getWidth() / 2), lightRevealScrim.getHeight());
            return;
        }
        lightRevealScrim.setRevealGradientBounds(0.0f, (lightRevealScrim.getHeight() / 2) - ((lightRevealScrim.getHeight() / 2) * lerp), lightRevealScrim.getWidth(), ((lightRevealScrim.getHeight() / 2) * lerp) + (lightRevealScrim.getHeight() / 2));
    }

    public LinearLightRevealEffect(boolean z) {
        this.isVertical = z;
    }
}
