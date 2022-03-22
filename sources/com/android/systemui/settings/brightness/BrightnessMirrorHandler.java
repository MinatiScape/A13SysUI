package com.android.systemui.settings.brightness;

import com.android.systemui.screenshot.ScreenshotController$$ExternalSyntheticLambda4;
import com.android.systemui.statusbar.policy.BrightnessMirrorController;
import java.util.Objects;
/* compiled from: BrightnessMirrorHandler.kt */
/* loaded from: classes.dex */
public final class BrightnessMirrorHandler {
    public final MirroredBrightnessController brightnessController;
    public final BrightnessMirrorHandler$brightnessMirrorListener$1 brightnessMirrorListener = new BrightnessMirrorController.BrightnessMirrorListener() { // from class: com.android.systemui.settings.brightness.BrightnessMirrorHandler$brightnessMirrorListener$1
        @Override // com.android.systemui.statusbar.policy.BrightnessMirrorController.BrightnessMirrorListener
        public final void onBrightnessMirrorReinflated() {
            BrightnessMirrorHandler.this.updateBrightnessMirror();
        }
    };
    public BrightnessMirrorController mirrorController;

    public final void updateBrightnessMirror() {
        BrightnessMirrorController brightnessMirrorController = this.mirrorController;
        if (brightnessMirrorController != null) {
            BrightnessController brightnessController = (BrightnessController) this.brightnessController;
            Objects.requireNonNull(brightnessController);
            BrightnessSliderController brightnessSliderController = (BrightnessSliderController) brightnessController.mControl;
            Objects.requireNonNull(brightnessSliderController);
            brightnessSliderController.mMirrorController = brightnessMirrorController;
            BrightnessSliderController brightnessSliderController2 = brightnessMirrorController.mToggleSliderController;
            brightnessSliderController.mMirror = brightnessSliderController2;
            if (brightnessSliderController2 != null) {
                BrightnessSliderView brightnessSliderView = (BrightnessSliderView) brightnessSliderController.mView;
                Objects.requireNonNull(brightnessSliderView);
                brightnessSliderController2.setMax(brightnessSliderView.mSlider.getMax());
                ToggleSlider toggleSlider = brightnessSliderController.mMirror;
                BrightnessSliderView brightnessSliderView2 = (BrightnessSliderView) brightnessSliderController.mView;
                Objects.requireNonNull(brightnessSliderView2);
                ((BrightnessSliderController) toggleSlider).setValue(brightnessSliderView2.mSlider.getProgress());
                BrightnessSliderView brightnessSliderView3 = (BrightnessSliderView) brightnessSliderController.mView;
                ScreenshotController$$ExternalSyntheticLambda4 screenshotController$$ExternalSyntheticLambda4 = new ScreenshotController$$ExternalSyntheticLambda4(brightnessSliderController);
                Objects.requireNonNull(brightnessSliderView3);
                brightnessSliderView3.mListener = screenshotController$$ExternalSyntheticLambda4;
                return;
            }
            BrightnessSliderView brightnessSliderView4 = (BrightnessSliderView) brightnessSliderController.mView;
            Objects.requireNonNull(brightnessSliderView4);
            brightnessSliderView4.mListener = null;
        }
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.settings.brightness.BrightnessMirrorHandler$brightnessMirrorListener$1] */
    public BrightnessMirrorHandler(BrightnessController brightnessController) {
        this.brightnessController = brightnessController;
    }
}
