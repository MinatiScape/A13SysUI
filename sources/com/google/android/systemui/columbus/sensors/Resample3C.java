package com.google.android.systemui.columbus.sensors;

import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
/* loaded from: classes.dex */
public final class Resample3C {
    public long mRawLastT;
    public float mRawLastX;
    public float mRawLastY;
    public float mRawLastZ;
    public long mResampledLastT;
    public float mResampledThisY;
    public float mResampledThisZ;
    public float mResampledThisX = 0.0f;
    public long mInterval = 0;

    public final Sample3C getResults() {
        return new Sample3C(this.mResampledThisX, this.mResampledThisY, this.mResampledThisZ, this.mResampledLastT);
    }

    public final boolean update(float f, float f2, float f3, long j) {
        long j2 = this.mRawLastT;
        if (j == j2) {
            return false;
        }
        long j3 = this.mInterval;
        if (j3 <= 0) {
            j3 = j - j2;
        }
        long j4 = this.mResampledLastT + j3;
        if (j < j4) {
            this.mRawLastT = j;
            this.mRawLastX = f;
            this.mRawLastY = f2;
            this.mRawLastZ = f3;
            return false;
        }
        float f4 = ((float) (j4 - j2)) / ((float) (j - j2));
        float f5 = this.mRawLastX;
        this.mResampledThisX = MotionController$$ExternalSyntheticOutline0.m(f, f5, f4, f5);
        float f6 = this.mRawLastY;
        this.mResampledThisY = MotionController$$ExternalSyntheticOutline0.m(f2, f6, f4, f6);
        float f7 = this.mRawLastZ;
        this.mResampledThisZ = MotionController$$ExternalSyntheticOutline0.m(f3, f7, f4, f7);
        this.mResampledLastT = j4;
        if (j2 >= j4) {
            return true;
        }
        this.mRawLastT = j;
        this.mRawLastX = f;
        this.mRawLastY = f2;
        this.mRawLastZ = f3;
        return true;
    }
}
