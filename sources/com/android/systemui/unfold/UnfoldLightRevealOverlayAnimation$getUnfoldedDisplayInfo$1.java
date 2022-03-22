package com.android.systemui.unfold;

import android.view.Display;
import android.view.DisplayInfo;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: UnfoldLightRevealOverlayAnimation.kt */
/* loaded from: classes.dex */
final class UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$1 extends Lambda implements Function1<Display, DisplayInfo> {
    public static final UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$1 INSTANCE = new UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$1();

    public UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$1() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final DisplayInfo invoke(Display display) {
        DisplayInfo displayInfo = new DisplayInfo();
        display.getDisplayInfo(displayInfo);
        return displayInfo;
    }
}
