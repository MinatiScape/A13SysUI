package com.google.android.systemui.columbus.sensors;

import java.util.Objects;
/* loaded from: classes.dex */
public final class Slope3C {
    public Slope1C mSlopeX = new Slope1C();
    public Slope1C mSlopeY = new Slope1C();
    public Slope1C mSlopeZ = new Slope1C();

    public final Point3f update(Point3f point3f, float f) {
        Slope1C slope1C = this.mSlopeX;
        float f2 = point3f.x;
        Objects.requireNonNull(slope1C);
        float f3 = f2 * f;
        float f4 = f3 - slope1C.mRawLastX;
        slope1C.mRawLastX = f3;
        Slope1C slope1C2 = this.mSlopeY;
        float f5 = point3f.y;
        Objects.requireNonNull(slope1C2);
        float f6 = f5 * f;
        slope1C2.mRawLastX = f6;
        Slope1C slope1C3 = this.mSlopeZ;
        float f7 = point3f.z;
        Objects.requireNonNull(slope1C3);
        float f8 = f7 * f;
        slope1C3.mRawLastX = f8;
        return new Point3f(f4, f6 - slope1C2.mRawLastX, f8 - slope1C3.mRawLastX);
    }
}
