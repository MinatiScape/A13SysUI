package com.google.android.systemui.smartspace;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
/* loaded from: classes.dex */
public final class BcSmartspaceCardLoggingInfo {
    public final int mCardinality;
    public final int mDisplaySurface;
    public final int mFeatureType;
    public final int mInstanceId;
    public final int mRank;
    public final int mReceivedLatency;

    /* loaded from: classes.dex */
    public static class Builder {
        public int mCardinality;
        public int mDisplaySurface = 1;
        public int mFeatureType;
        public int mInstanceId;
        public int mRank;
        public int mReceivedLatency;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("instance_id = ");
        m.append(this.mInstanceId);
        m.append(", feature type = ");
        m.append(this.mFeatureType);
        m.append(", display surface = ");
        m.append(this.mDisplaySurface);
        m.append(", rank = ");
        m.append(this.mRank);
        m.append(", cardinality = ");
        m.append(this.mCardinality);
        m.append(", receivedLatencyMillis = ");
        m.append(this.mReceivedLatency);
        return m.toString();
    }

    public BcSmartspaceCardLoggingInfo(Builder builder) {
        this.mInstanceId = builder.mInstanceId;
        this.mDisplaySurface = builder.mDisplaySurface;
        this.mRank = builder.mRank;
        this.mCardinality = builder.mCardinality;
        this.mFeatureType = builder.mFeatureType;
        this.mReceivedLatency = builder.mReceivedLatency;
    }
}
