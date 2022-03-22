package com.android.keyguard;

import android.graphics.Typeface;
import java.util.Objects;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: TextAnimator.kt */
/* loaded from: classes.dex */
public final class TextAnimator$setTextStyle$1 extends Lambda implements Function0<Typeface> {
    public final /* synthetic */ int $weight;
    public final /* synthetic */ TextAnimator this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public TextAnimator$setTextStyle$1(TextAnimator textAnimator, int i) {
        super(0);
        this.this$0 = textAnimator;
        this.$weight = i;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Typeface invoke() {
        TextAnimator textAnimator = this.this$0;
        Objects.requireNonNull(textAnimator);
        TextInterpolator textInterpolator = textAnimator.textInterpolator;
        Objects.requireNonNull(textInterpolator);
        textInterpolator.targetPaint.setFontVariationSettings(Intrinsics.stringPlus("'wght' ", Integer.valueOf(this.$weight)));
        TextAnimator textAnimator2 = this.this$0;
        Objects.requireNonNull(textAnimator2);
        TextInterpolator textInterpolator2 = textAnimator2.textInterpolator;
        Objects.requireNonNull(textInterpolator2);
        return textInterpolator2.targetPaint.getTypeface();
    }
}
