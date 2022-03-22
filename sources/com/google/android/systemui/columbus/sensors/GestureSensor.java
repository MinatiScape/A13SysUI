package com.google.android.systemui.columbus.sensors;

import java.util.Random;
/* compiled from: GestureSensor.kt */
/* loaded from: classes.dex */
public abstract class GestureSensor implements Sensor {
    public Listener listener;

    /* compiled from: GestureSensor.kt */
    /* loaded from: classes.dex */
    public interface Listener {
        void onGestureDetected(GestureSensor gestureSensor, int i, DetectionProperties detectionProperties);
    }

    /* compiled from: GestureSensor.kt */
    /* loaded from: classes.dex */
    public static final class DetectionProperties {
        public final long actionId = new Random().nextLong();
        public final boolean isHapticConsumed;

        public DetectionProperties(boolean z) {
            this.isHapticConsumed = z;
        }
    }
}
