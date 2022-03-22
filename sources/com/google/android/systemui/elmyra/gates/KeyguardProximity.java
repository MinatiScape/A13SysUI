package com.google.android.systemui.elmyra.gates;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.Objects;
/* loaded from: classes.dex */
public final class KeyguardProximity extends Gate {
    public final AnonymousClass1 mGateListener;
    public final KeyguardVisibility mKeyguardGate;
    public final float mProximityThreshold;
    public final Sensor mSensor;
    public final SensorManager mSensorManager;
    public final AnonymousClass2 mSensorListener = new SensorEventListener() { // from class: com.google.android.systemui.elmyra.gates.KeyguardProximity.2
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent sensorEvent) {
            boolean z = false;
            float f = sensorEvent.values[0];
            KeyguardProximity keyguardProximity = KeyguardProximity.this;
            if (f < keyguardProximity.mProximityThreshold) {
                z = true;
            }
            if (keyguardProximity.mIsListening && z != keyguardProximity.mProximityBlocked) {
                keyguardProximity.mProximityBlocked = z;
                keyguardProximity.notifyListener();
            }
        }
    };
    public boolean mIsListening = false;
    public boolean mProximityBlocked = false;

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final boolean isBlocked() {
        if (!this.mIsListening || !this.mProximityBlocked) {
            return false;
        }
        return true;
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final void onActivate() {
        if (this.mSensor != null) {
            this.mKeyguardGate.activate();
            updateProximityListener();
        }
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final void onDeactivate() {
        if (this.mSensor != null) {
            this.mKeyguardGate.deactivate();
            updateProximityListener();
        }
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final String toString() {
        return super.toString() + " [mIsListening -> " + this.mIsListening + "]";
    }

    public final void updateProximityListener() {
        if (this.mProximityBlocked) {
            this.mProximityBlocked = false;
            notifyListener();
        }
        if (this.mActive) {
            KeyguardVisibility keyguardVisibility = this.mKeyguardGate;
            Objects.requireNonNull(keyguardVisibility);
            if (keyguardVisibility.mKeyguardStateController.isShowing()) {
                KeyguardVisibility keyguardVisibility2 = this.mKeyguardGate;
                Objects.requireNonNull(keyguardVisibility2);
                if (!keyguardVisibility2.mKeyguardStateController.isOccluded()) {
                    if (!this.mIsListening) {
                        this.mSensorManager.registerListener(this.mSensorListener, this.mSensor, 3);
                        this.mIsListening = true;
                        return;
                    }
                    return;
                }
            }
        }
        this.mSensorManager.unregisterListener(this.mSensorListener);
        this.mIsListening = false;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.google.android.systemui.elmyra.gates.Gate$Listener, com.google.android.systemui.elmyra.gates.KeyguardProximity$1] */
    /* JADX WARN: Type inference failed for: r1v0, types: [com.google.android.systemui.elmyra.gates.KeyguardProximity$2] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public KeyguardProximity(android.content.Context r5) {
        /*
            r4 = this;
            r4.<init>(r5)
            com.google.android.systemui.elmyra.gates.KeyguardProximity$1 r0 = new com.google.android.systemui.elmyra.gates.KeyguardProximity$1
            r0.<init>()
            r4.mGateListener = r0
            com.google.android.systemui.elmyra.gates.KeyguardProximity$2 r1 = new com.google.android.systemui.elmyra.gates.KeyguardProximity$2
            r1.<init>()
            r4.mSensorListener = r1
            r1 = 0
            r4.mIsListening = r1
            r4.mProximityBlocked = r1
            java.lang.Class<com.android.systemui.util.sensors.AsyncSensorManager> r1 = com.android.systemui.util.sensors.AsyncSensorManager.class
            java.lang.Object r1 = com.android.systemui.Dependency.get(r1)
            android.hardware.SensorManager r1 = (android.hardware.SensorManager) r1
            r4.mSensorManager = r1
            r2 = 8
            android.hardware.Sensor r1 = r1.getDefaultSensor(r2)
            r4.mSensor = r1
            if (r1 != 0) goto L_0x0038
            r5 = 0
            r4.mProximityThreshold = r5
            r5 = 0
            r4.mKeyguardGate = r5
            java.lang.String r4 = "Elmyra/KeyguardProximity"
            java.lang.String r5 = "Could not find any Sensor.TYPE_PROXIMITY"
            android.util.Log.e(r4, r5)
            goto L_0x005a
        L_0x0038:
            float r1 = r1.getMaximumRange()
            android.content.res.Resources r2 = r5.getResources()
            r3 = 2131492924(0x7f0c003c, float:1.8609314E38)
            int r2 = r2.getInteger(r3)
            float r2 = (float) r2
            float r1 = java.lang.Math.min(r1, r2)
            r4.mProximityThreshold = r1
            com.google.android.systemui.elmyra.gates.KeyguardVisibility r1 = new com.google.android.systemui.elmyra.gates.KeyguardVisibility
            r1.<init>(r5)
            r4.mKeyguardGate = r1
            r1.mListener = r0
            r4.updateProximityListener()
        L_0x005a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.elmyra.gates.KeyguardProximity.<init>(android.content.Context):void");
    }
}
