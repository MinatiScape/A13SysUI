package com.android.systemui.plugins;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.plugins.annotations.ProvidesInterface;
@ProvidesInterface(action = SensorManagerPlugin.ACTION, version = 1)
/* loaded from: classes.dex */
public interface SensorManagerPlugin extends Plugin {
    public static final String ACTION = "com.android.systemui.action.PLUGIN_SENSOR_MANAGER";
    public static final int VERSION = 1;

    /* loaded from: classes.dex */
    public static class SensorEvent {
        public Sensor mSensor;
        public float[] mValues;
        public int mVendorType;

        public SensorEvent(Sensor sensor, int i) {
            this(sensor, i, null);
        }

        public SensorEvent(Sensor sensor, int i, float[] fArr) {
            this.mSensor = sensor;
            this.mVendorType = i;
            this.mValues = fArr;
        }

        public Sensor getSensor() {
            return this.mSensor;
        }

        public float[] getValues() {
            return this.mValues;
        }

        public int getVendorType() {
            return this.mVendorType;
        }
    }

    /* loaded from: classes.dex */
    public interface SensorEventListener {
        void onSensorChanged(SensorEvent sensorEvent);
    }

    void registerListener(Sensor sensor, SensorEventListener sensorEventListener);

    void unregisterListener(Sensor sensor, SensorEventListener sensorEventListener);

    /* loaded from: classes.dex */
    public static class Sensor {
        public static final int TYPE_SKIP_STATUS = 4;
        public static final int TYPE_SWIPE = 3;
        public static final int TYPE_WAKE_DISPLAY = 2;
        public static final int TYPE_WAKE_LOCK_SCREEN = 1;
        private int mType;

        public Sensor(int i) {
            this.mType = i;
        }

        public String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("{PluginSensor type=\"");
            m.append(this.mType);
            m.append("\"}");
            return m.toString();
        }

        public int getType() {
            return this.mType;
        }
    }
}
