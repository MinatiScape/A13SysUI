package com.android.systemui.statusbar;

import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
/* compiled from: LightRevealScrim.kt */
/* loaded from: classes.dex */
public final class CircleReveal implements LightRevealEffect {
    public final float centerX;
    public final float centerY;
    public final float endRadius;

    @Override // com.android.systemui.statusbar.LightRevealEffect
    public final void setRevealAmountOnScrim(float f, LightRevealScrim lightRevealScrim) {
        float f2 = f - 0.5f;
        if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        float m = MotionController$$ExternalSyntheticOutline0.m(this.endRadius, 0.0f, f, 0.0f);
        lightRevealScrim.interpolatedRevealAmount = f;
        lightRevealScrim.setRevealGradientEndColorAlpha(1.0f - (f2 * 2.0f));
        float f3 = this.centerX;
        float f4 = this.centerY;
        lightRevealScrim.setRevealGradientBounds(f3 - m, f4 - m, f3 + m, f4 + m);
    }

    public CircleReveal(float f, float f2, float f3) {
        this.centerX = f;
        this.centerY = f2;
        this.endRadius = f3;
    }
}
