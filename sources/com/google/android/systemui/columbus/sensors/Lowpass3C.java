package com.google.android.systemui.columbus.sensors;

import java.util.Objects;
/* loaded from: classes.dex */
public final class Lowpass3C extends Lowpass1C {
    public Lowpass1C mLowpassX = new Lowpass1C();
    public Lowpass1C mLowpassY = new Lowpass1C();
    public Lowpass1C mLowpassZ = new Lowpass1C();

    public final void setPara(float f) {
        Lowpass1C lowpass1C = this.mLowpassX;
        Objects.requireNonNull(lowpass1C);
        lowpass1C.mPara = 1.0f;
        Lowpass1C lowpass1C2 = this.mLowpassY;
        Objects.requireNonNull(lowpass1C2);
        lowpass1C2.mPara = 1.0f;
        Lowpass1C lowpass1C3 = this.mLowpassZ;
        Objects.requireNonNull(lowpass1C3);
        lowpass1C3.mPara = 1.0f;
    }

    public final Point3f update(Point3f point3f) {
        return new Point3f(this.mLowpassX.update(point3f.x), this.mLowpassY.update(point3f.y), this.mLowpassZ.update(point3f.z));
    }
}
