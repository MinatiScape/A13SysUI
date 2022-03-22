package com.android.systemui.shared.system;

import android.view.InsetsController;
import android.view.animation.Interpolator;
/* loaded from: classes.dex */
public final class WindowManagerWrapper {
    public static final WindowManagerWrapper sInstance = new WindowManagerWrapper();

    static {
        Interpolator interpolator = InsetsController.RESIZE_INTERPOLATOR;
    }
}
