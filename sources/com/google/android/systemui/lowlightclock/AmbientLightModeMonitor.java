package com.google.android.systemui.lowlightclock;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import com.android.systemui.util.sensors.AsyncSensorManager;
/* compiled from: AmbientLightModeMonitor.kt */
/* loaded from: classes.dex */
public final class AmbientLightModeMonitor {
    public static final boolean DEBUG = Log.isLoggable("AmbientLightModeMonitor", 3);
    public final DebounceAlgorithm algorithm;
    public final Sensor lightSensor;
    public final AmbientLightModeMonitor$mSensorEventListener$1 mSensorEventListener = new SensorEventListener() { // from class: com.google.android.systemui.lowlightclock.AmbientLightModeMonitor$mSensorEventListener$1
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent sensorEvent) {
            boolean z;
            float[] fArr = sensorEvent.values;
            if (fArr.length == 0) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                AmbientLightModeMonitor.this.algorithm.onUpdateLightSensorEvent(fArr[0]);
            } else if (AmbientLightModeMonitor.DEBUG) {
                Log.w("AmbientLightModeMonitor", "SensorEvent doesn't have any value");
            }
        }
    };
    public final AsyncSensorManager sensorManager;

    /* compiled from: AmbientLightModeMonitor.kt */
    /* loaded from: classes.dex */
    public interface Callback {
    }

    /* compiled from: AmbientLightModeMonitor.kt */
    /* loaded from: classes.dex */
    public interface DebounceAlgorithm {
        void onUpdateLightSensorEvent(float f);

        void start(LowLightDockManager$$ExternalSyntheticLambda0 lowLightDockManager$$ExternalSyntheticLambda0);

        void stop();
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [com.google.android.systemui.lowlightclock.AmbientLightModeMonitor$mSensorEventListener$1] */
    public AmbientLightModeMonitor(DebounceAlgorithm debounceAlgorithm, AsyncSensorManager asyncSensorManager) {
        this.algorithm = debounceAlgorithm;
        this.sensorManager = asyncSensorManager;
        this.lightSensor = asyncSensorManager.getDefaultSensor(5);
    }
}
