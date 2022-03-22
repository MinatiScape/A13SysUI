package com.android.settingslib.animation;

import android.content.Context;
import android.view.animation.Interpolator;
import com.android.settingslib.animation.AppearAnimationUtils;
/* loaded from: classes.dex */
public final class DisappearAnimationUtils extends AppearAnimationUtils {
    public static final AnonymousClass1 ROW_TRANSLATION_SCALER = new AppearAnimationUtils.RowTranslationScaler() { // from class: com.android.settingslib.animation.DisappearAnimationUtils.1
    };

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DisappearAnimationUtils(Context context, long j, float f, float f2, Interpolator interpolator) {
        super(context, j, f, f2, interpolator);
        AnonymousClass1 r0 = ROW_TRANSLATION_SCALER;
        this.mRowTranslationScaler = r0;
        this.mAppearing = false;
    }

    @Override // com.android.settingslib.animation.AppearAnimationUtils
    public final long calculateDelay(int i, int i2) {
        return (long) ((((Math.pow(i, 0.4d) + 0.4d) * i2 * 10.0d) + (i * 60)) * this.mDelayScale);
    }
}
