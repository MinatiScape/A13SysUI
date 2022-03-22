package com.google.android.systemui.elmyra.sensors;

import java.util.Random;
/* loaded from: classes.dex */
public interface GestureSensor extends Sensor {

    /* loaded from: classes.dex */
    public interface Listener {
        void onGestureDetected(DetectionProperties detectionProperties);

        void onGestureProgress(float f, int i);
    }

    void setGestureListener(Listener listener);

    /* loaded from: classes.dex */
    public static class DetectionProperties {
        public final long mActionId = new Random().nextLong();
        public final boolean mHapticConsumed;
        public final boolean mHostSuspended;

        public DetectionProperties(boolean z, boolean z2) {
            this.mHapticConsumed = z;
            this.mHostSuspended = z2;
        }
    }
}
