package com.android.systemui.unfold.updates.hinge;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import androidx.core.util.Consumer;
import java.util.ArrayList;
import java.util.Iterator;
/* compiled from: HingeSensorAngleProvider.kt */
/* loaded from: classes.dex */
public final class HingeSensorAngleProvider implements HingeAngleProvider {
    public final SensorManager sensorManager;
    public final HingeAngleSensorListener sensorListener = new HingeAngleSensorListener();
    public final ArrayList listeners = new ArrayList();

    /* compiled from: HingeSensorAngleProvider.kt */
    /* loaded from: classes.dex */
    public final class HingeAngleSensorListener implements SensorEventListener {
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int i) {
        }

        public HingeAngleSensorListener() {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent sensorEvent) {
            Iterator it = HingeSensorAngleProvider.this.listeners.iterator();
            while (it.hasNext()) {
                ((Consumer) it.next()).accept(Float.valueOf(sensorEvent.values[0]));
            }
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(Consumer<Float> consumer) {
        this.listeners.add(consumer);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(Consumer<Float> consumer) {
        this.listeners.remove(consumer);
    }

    @Override // com.android.systemui.unfold.updates.hinge.HingeAngleProvider
    public final void start() {
        this.sensorManager.registerListener(this.sensorListener, this.sensorManager.getDefaultSensor(36), 0);
    }

    @Override // com.android.systemui.unfold.updates.hinge.HingeAngleProvider
    public final void stop() {
        this.sensorManager.unregisterListener(this.sensorListener);
    }

    public HingeSensorAngleProvider(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
    }
}
