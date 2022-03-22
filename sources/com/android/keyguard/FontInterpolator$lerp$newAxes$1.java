package com.android.keyguard;

import android.util.MathUtils;
import java.util.Objects;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: FontInterpolator.kt */
/* loaded from: classes.dex */
public final class FontInterpolator$lerp$newAxes$1 extends Lambda implements Function3<String, Float, Float, Float> {
    public final /* synthetic */ float $progress;
    public final /* synthetic */ FontInterpolator this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FontInterpolator$lerp$newAxes$1(FontInterpolator fontInterpolator, float f) {
        super(3);
        this.this$0 = fontInterpolator;
        this.$progress = f;
    }

    @Override // kotlin.jvm.functions.Function3
    public final Float invoke(String str, Float f, Float f2) {
        float f3;
        boolean z;
        float f4;
        float f5;
        String str2 = str;
        Float f6 = f;
        Float f7 = f2;
        if (Intrinsics.areEqual(str2, "wght")) {
            FontInterpolator fontInterpolator = this.this$0;
            float f8 = 400.0f;
            if (f6 == null) {
                f5 = 400.0f;
            } else {
                f5 = f6.floatValue();
            }
            if (f7 != null) {
                f8 = f7.floatValue();
            }
            float lerp = MathUtils.lerp(f5, f8, this.$progress);
            Objects.requireNonNull(fontInterpolator);
            f3 = FontInterpolator.coerceInWithStep(lerp, 1000.0f, 10.0f);
        } else if (Intrinsics.areEqual(str2, "ital")) {
            FontInterpolator fontInterpolator2 = this.this$0;
            float f9 = 0.0f;
            if (f6 == null) {
                f4 = 0.0f;
            } else {
                f4 = f6.floatValue();
            }
            if (f7 != null) {
                f9 = f7.floatValue();
            }
            float lerp2 = MathUtils.lerp(f4, f9, this.$progress);
            Objects.requireNonNull(fontInterpolator2);
            f3 = FontInterpolator.coerceInWithStep(lerp2, 1.0f, 0.1f);
        } else {
            if (f6 == null || f7 == null) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                f3 = MathUtils.lerp(f6.floatValue(), f7.floatValue(), this.$progress);
            } else {
                throw new IllegalArgumentException(Intrinsics.stringPlus("Unable to interpolate due to unknown default axes value : ", str2).toString());
            }
        }
        return Float.valueOf(f3);
    }
}
