package com.airbnb.lottie.model;

import android.graphics.PointF;
/* loaded from: classes.dex */
public final class CubicCurveData {
    public final PointF controlPoint1;
    public final PointF controlPoint2;
    public final PointF vertex;

    public CubicCurveData() {
        this.controlPoint1 = new PointF();
        this.controlPoint2 = new PointF();
        this.vertex = new PointF();
    }

    public CubicCurveData(PointF pointF, PointF pointF2, PointF pointF3) {
        this.controlPoint1 = pointF;
        this.controlPoint2 = pointF2;
        this.vertex = pointF3;
    }
}
