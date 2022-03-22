package com.android.settingslib.fuelgauge;
/* compiled from: Estimate.kt */
/* loaded from: classes.dex */
public final class Estimate {
    public final long averageDischargeTime;
    public final long estimateMillis;
    public final boolean isBasedOnUsage;

    public Estimate(long j, boolean z, long j2) {
        this.estimateMillis = j;
        this.isBasedOnUsage = z;
        this.averageDischargeTime = j2;
    }
}
