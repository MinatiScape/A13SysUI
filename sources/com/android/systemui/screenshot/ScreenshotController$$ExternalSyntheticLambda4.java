package com.android.systemui.screenshot;

import android.view.MotionEvent;
import com.android.systemui.screenshot.ScreenshotController;
import com.android.systemui.settings.brightness.BrightnessSliderController;
import com.android.systemui.settings.brightness.BrightnessSliderView;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ScreenshotController$$ExternalSyntheticLambda4 implements ScreenshotController.QuickShareActionReadyListener, BrightnessSliderView.DispatchTouchEventListener {
    public final /* synthetic */ Object f$0;

    public final boolean onDispatchTouchEvent(MotionEvent motionEvent) {
        return ((BrightnessSliderController) this.f$0).mirrorTouchEvent(motionEvent);
    }

    public /* synthetic */ ScreenshotController$$ExternalSyntheticLambda4(Object obj) {
        this.f$0 = obj;
    }
}
