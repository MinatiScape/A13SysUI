package com.google.android.systemui.columbus.sensors;
/* loaded from: classes.dex */
public final class Sample3C {
    public Point3f mPoint;
    public long mT;

    public Sample3C(float f, float f2, float f3, long j) {
        Point3f point3f = new Point3f(0.0f, 0.0f, 0.0f);
        this.mPoint = point3f;
        point3f.x = f;
        point3f.y = f2;
        point3f.z = f3;
        this.mT = j;
    }
}
