package com.google.android.systemui.columbus.sensors;

import java.util.Objects;
/* loaded from: classes.dex */
public final class Highpass3C {
    public Highpass1C mHighpassX = new Highpass1C();
    public Highpass1C mHighpassY = new Highpass1C();
    public Highpass1C mHighpassZ = new Highpass1C();

    public final void setPara() {
        Highpass1C highpass1C = this.mHighpassX;
        Objects.requireNonNull(highpass1C);
        highpass1C.mPara = 0.05f;
        Highpass1C highpass1C2 = this.mHighpassY;
        Objects.requireNonNull(highpass1C2);
        highpass1C2.mPara = 0.05f;
        Highpass1C highpass1C3 = this.mHighpassZ;
        Objects.requireNonNull(highpass1C3);
        highpass1C3.mPara = 0.05f;
    }

    public final Point3f update(Point3f point3f) {
        return new Point3f(this.mHighpassX.update(point3f.x), this.mHighpassY.update(point3f.y), this.mHighpassZ.update(point3f.z));
    }
}
