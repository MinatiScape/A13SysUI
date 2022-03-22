package com.google.android.systemui.columbus.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import com.android.internal.logging.UiEventLogger;
import com.google.android.systemui.columbus.ColumbusEvent;
import java.util.ArrayList;
import java.util.Objects;
/* compiled from: GestureSensorImpl.kt */
/* loaded from: classes.dex */
public final class GestureSensorImpl extends GestureSensor {
    public final Sensor accelerometer;
    public final Sensor gyroscope;
    public final Handler handler;
    public boolean isListening;
    public final SensorManager sensorManager;
    public final TapRT tap;
    public final UiEventLogger uiEventLogger;
    public final GestureSensorEventListener sensorEventListener = new GestureSensorEventListener();
    public final long samplingIntervalNs = 2400000;

    /* compiled from: GestureSensorImpl.kt */
    /* loaded from: classes.dex */
    public final class GestureSensorEventListener implements SensorEventListener {
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int i) {
        }

        public final void setListening(boolean z) {
            GestureSensorImpl gestureSensorImpl;
            Sensor sensor;
            if (!z || (sensor = (gestureSensorImpl = GestureSensorImpl.this).accelerometer) == null || gestureSensorImpl.gyroscope == null) {
                GestureSensorImpl gestureSensorImpl2 = GestureSensorImpl.this;
                gestureSensorImpl2.sensorManager.unregisterListener(gestureSensorImpl2.sensorEventListener);
                GestureSensorImpl gestureSensorImpl3 = GestureSensorImpl.this;
                Objects.requireNonNull(gestureSensorImpl3);
                gestureSensorImpl3.isListening = false;
                return;
            }
            gestureSensorImpl.sensorManager.registerListener(gestureSensorImpl.sensorEventListener, sensor, 0, gestureSensorImpl.handler);
            GestureSensorImpl gestureSensorImpl4 = GestureSensorImpl.this;
            gestureSensorImpl4.sensorManager.registerListener(gestureSensorImpl4.sensorEventListener, gestureSensorImpl4.gyroscope, 0, gestureSensorImpl4.handler);
            GestureSensorImpl gestureSensorImpl5 = GestureSensorImpl.this;
            Objects.requireNonNull(gestureSensorImpl5);
            gestureSensorImpl5.isListening = true;
        }

        public GestureSensorEventListener() {
        }

        /* JADX WARN: Code restructure failed: missing block: B:18:0x0084, code lost:
            if (r2.mGotAcc == false) goto L_0x0165;
         */
        /* JADX WARN: Removed duplicated region for block: B:102:0x04b3  */
        /* JADX WARN: Removed duplicated region for block: B:105:0x04c2  */
        /* JADX WARN: Removed duplicated region for block: B:88:0x045b  */
        /* JADX WARN: Removed duplicated region for block: B:93:0x047a  */
        /* JADX WARN: Removed duplicated region for block: B:94:0x047d  */
        @Override // android.hardware.SensorEventListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onSensorChanged(android.hardware.SensorEvent r20) {
            /*
                Method dump skipped, instructions count: 1238
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.columbus.sensors.GestureSensorImpl.GestureSensorEventListener.onSensorChanged(android.hardware.SensorEvent):void");
        }
    }

    @Override // com.google.android.systemui.columbus.sensors.Sensor
    public final void startListening() {
        this.sensorEventListener.setListening(true);
        TapRT tapRT = this.tap;
        Objects.requireNonNull(tapRT);
        tapRT.mLowpassAcc.setPara(1.0f);
        TapRT tapRT2 = this.tap;
        Objects.requireNonNull(tapRT2);
        tapRT2.mLowpassGyro.setPara(1.0f);
        TapRT tapRT3 = this.tap;
        Objects.requireNonNull(tapRT3);
        tapRT3.mHighpassAcc.setPara();
        TapRT tapRT4 = this.tap;
        Objects.requireNonNull(tapRT4);
        tapRT4.mHighpassGyro.setPara();
        TapRT tapRT5 = this.tap;
        Objects.requireNonNull(tapRT5);
        PeakDetector peakDetector = tapRT5.mPeakDetector;
        Objects.requireNonNull(peakDetector);
        peakDetector.mMinNoiseTolerate = 0.03f;
        TapRT tapRT6 = this.tap;
        Objects.requireNonNull(tapRT6);
        PeakDetector peakDetector2 = tapRT6.mPeakDetector;
        Objects.requireNonNull(peakDetector2);
        peakDetector2.mWindowSize = 64;
        TapRT tapRT7 = this.tap;
        Objects.requireNonNull(tapRT7);
        PeakDetector peakDetector3 = tapRT7.mValleyDetector;
        Objects.requireNonNull(peakDetector3);
        peakDetector3.mMinNoiseTolerate = 0.015f;
        TapRT tapRT8 = this.tap;
        Objects.requireNonNull(tapRT8);
        PeakDetector peakDetector4 = tapRT8.mValleyDetector;
        Objects.requireNonNull(peakDetector4);
        peakDetector4.mWindowSize = 64;
        TapRT tapRT9 = this.tap;
        Objects.requireNonNull(tapRT9);
        tapRT9.mAccXs.clear();
        tapRT9.mAccYs.clear();
        tapRT9.mAccZs.clear();
        tapRT9.mGyroXs.clear();
        tapRT9.mGyroYs.clear();
        tapRT9.mGyroZs.clear();
        tapRT9.mGotAcc = false;
        tapRT9.mGotGyro = false;
        tapRT9.mSyncTime = 0L;
        tapRT9.mFeatureVector = new ArrayList<>(tapRT9.mNumberFeature);
        for (int i = 0; i < tapRT9.mNumberFeature; i++) {
            tapRT9.mFeatureVector.add(Float.valueOf(0.0f));
        }
        this.uiEventLogger.log(ColumbusEvent.COLUMBUS_MODE_HIGH_POWER_ACTIVE);
    }

    @Override // com.google.android.systemui.columbus.sensors.Sensor
    public final void stopListening() {
        this.sensorEventListener.setListening(false);
        this.uiEventLogger.log(ColumbusEvent.COLUMBUS_MODE_INACTIVE);
    }

    public GestureSensorImpl(Context context, UiEventLogger uiEventLogger, Handler handler) {
        this.uiEventLogger = uiEventLogger;
        this.handler = handler;
        Object systemService = context.getSystemService("sensor");
        Objects.requireNonNull(systemService, "null cannot be cast to non-null type android.hardware.SensorManager");
        SensorManager sensorManager = (SensorManager) systemService;
        this.sensorManager = sensorManager;
        this.accelerometer = sensorManager.getDefaultSensor(1);
        this.gyroscope = sensorManager.getDefaultSensor(4);
        String str = Build.MODEL;
        this.tap = new TapRT(context.getAssets());
    }

    @Override // com.google.android.systemui.columbus.sensors.Sensor
    public final boolean isListening() {
        return this.isListening;
    }
}
