package com.android.systemui.volume;

import android.animation.ValueAnimator;
import com.android.systemui.screenshot.ScreenshotView;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class VolumeDialogImpl$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ VolumeDialogImpl$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        switch (this.$r8$classId) {
            case 0:
                VolumeDialogImpl volumeDialogImpl = (VolumeDialogImpl) this.f$0;
                Objects.requireNonNull(volumeDialogImpl);
                volumeDialogImpl.mRingerDrawerClosedAmount = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                volumeDialogImpl.updateBackgroundForDrawerClosedAmount();
                return;
            default:
                ScreenshotView screenshotView = (ScreenshotView) this.f$0;
                int i = ScreenshotView.$r8$clinit;
                Objects.requireNonNull(screenshotView);
                screenshotView.mScreenshotFlash.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
                return;
        }
    }
}
