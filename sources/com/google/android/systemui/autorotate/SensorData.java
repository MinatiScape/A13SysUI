package com.google.android.systemui.autorotate;
/* loaded from: classes.dex */
public final class SensorData {
    public int mSensorIdentifier;
    public long mTimestampMillis;
    public float mValueX;
    public float mValueY;
    public float mValueZ;

    public SensorData(float f, float f2, float f3, int i, long j) {
        this.mValueX = f;
        this.mValueY = f2;
        this.mValueZ = f3;
        this.mSensorIdentifier = i;
        this.mTimestampMillis = j;
    }
}
