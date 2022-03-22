package com.android.keyguard.mediator;

import com.android.systemui.unfold.SysUIUnfoldComponent;
import java.util.function.Function;
/* compiled from: ScreenOnCoordinator.kt */
/* loaded from: classes.dex */
public /* synthetic */ class ScreenOnCoordinator$unfoldLightRevealAnimation$1 implements Function {
    public static final ScreenOnCoordinator$unfoldLightRevealAnimation$1 INSTANCE = new ScreenOnCoordinator$unfoldLightRevealAnimation$1();

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return ((SysUIUnfoldComponent) obj).getUnfoldLightRevealOverlayAnimation();
    }
}
