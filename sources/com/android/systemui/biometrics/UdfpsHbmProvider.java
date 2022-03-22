package com.android.systemui.biometrics;

import android.view.Surface;
/* loaded from: classes.dex */
public interface UdfpsHbmProvider {
    void disableHbm();

    void enableHbm(int i, Surface surface, UdfpsView$doIlluminate$1 udfpsView$doIlluminate$1);
}
