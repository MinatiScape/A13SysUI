package com.google.android.systemui.columbus.sensors;

import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
/* loaded from: classes.dex */
public final class Highpass1C {
    public float mPara = 1.0f;
    public float mLastX = 0.0f;
    public float mLastY = 0.0f;

    public final float update(float f) {
        float f2 = this.mPara;
        if (f2 == 1.0f) {
            return f;
        }
        float m = MotionController$$ExternalSyntheticOutline0.m(f, this.mLastX, f2, this.mLastY * f2);
        this.mLastY = m;
        this.mLastX = f;
        return m;
    }
}
