package com.android.systemui.screenshot;

import com.android.systemui.screenshot.ScreenshotController;
import com.android.systemui.screenshot.ScrollCaptureController;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: classes.dex */
public final class LongScreenshotData {
    public final AtomicReference<ScrollCaptureController.LongScreenshot> mLongScreenshot = new AtomicReference<>();
    public final AtomicReference<ScreenshotController.TransitionDestination> mTransitionDestinationCallback = new AtomicReference<>();
}
