package com.google.android.material.animation;

import android.animation.TypeEvaluator;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
/* loaded from: classes.dex */
public final class ArgbEvaluatorCompat implements TypeEvaluator<Integer> {
    public static final ArgbEvaluatorCompat instance = new ArgbEvaluatorCompat();

    @Override // android.animation.TypeEvaluator
    public final /* bridge */ /* synthetic */ Integer evaluate(float f, Integer num, Integer num2) {
        return evaluate2(f, num, num2);
    }

    /* renamed from: evaluate  reason: avoid collision after fix types in other method */
    public static Integer evaluate2(float f, Integer num, Integer num2) {
        int intValue = num.intValue();
        float f2 = ((intValue >> 24) & 255) / 255.0f;
        int intValue2 = num2.intValue();
        float pow = (float) Math.pow(((intValue >> 16) & 255) / 255.0f, 2.2d);
        float pow2 = (float) Math.pow(((intValue >> 8) & 255) / 255.0f, 2.2d);
        float pow3 = (float) Math.pow((intValue & 255) / 255.0f, 2.2d);
        float pow4 = (float) Math.pow(((intValue2 >> 16) & 255) / 255.0f, 2.2d);
        float m = MotionController$$ExternalSyntheticOutline0.m(((intValue2 >> 24) & 255) / 255.0f, f2, f, f2);
        float m2 = MotionController$$ExternalSyntheticOutline0.m(pow4, pow, f, pow);
        float m3 = MotionController$$ExternalSyntheticOutline0.m((float) Math.pow(((intValue2 >> 8) & 255) / 255.0f, 2.2d), pow2, f, pow2);
        float m4 = MotionController$$ExternalSyntheticOutline0.m((float) Math.pow((intValue2 & 255) / 255.0f, 2.2d), pow3, f, pow3);
        int round = Math.round(((float) Math.pow(m2, 0.45454545454545453d)) * 255.0f) << 16;
        return Integer.valueOf(Math.round(((float) Math.pow(m4, 0.45454545454545453d)) * 255.0f) | round | (Math.round(m * 255.0f) << 24) | (Math.round(((float) Math.pow(m3, 0.45454545454545453d)) * 255.0f) << 8));
    }
}
