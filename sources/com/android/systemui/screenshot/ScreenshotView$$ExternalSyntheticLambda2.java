package com.android.systemui.screenshot;

import android.animation.ValueAnimator;
import com.android.systemui.biometrics.UdfpsEnrollProgressBarDrawable;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ScreenshotView$$ExternalSyntheticLambda2 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ ScreenshotView$$ExternalSyntheticLambda2(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        switch (this.$r8$classId) {
            case 0:
                ScreenshotView screenshotView = (ScreenshotView) this.f$0;
                int i = ScreenshotView.$r8$clinit;
                Objects.requireNonNull(screenshotView);
                screenshotView.mScreenshotFlash.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
                return;
            default:
                UdfpsEnrollProgressBarDrawable udfpsEnrollProgressBarDrawable = (UdfpsEnrollProgressBarDrawable) this.f$0;
                int i2 = UdfpsEnrollProgressBarDrawable.$r8$clinit;
                Objects.requireNonNull(udfpsEnrollProgressBarDrawable);
                udfpsEnrollProgressBarDrawable.mCheckmarkScale = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                udfpsEnrollProgressBarDrawable.invalidateSelf();
                return;
        }
    }
}
