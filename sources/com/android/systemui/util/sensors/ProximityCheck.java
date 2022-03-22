package com.android.systemui.util.sensors;

import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.sensors.ThresholdSensor;
import com.android.wm.shell.pip.phone.PipMotionHelper$$ExternalSyntheticLambda1;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes.dex */
public final class ProximityCheck implements Runnable {
    public final DelayableExecutor mDelayableExecutor;
    public final ProximitySensor mSensor;
    public ArrayList mCallbacks = new ArrayList();
    public final AtomicBoolean mRegistered = new AtomicBoolean();
    public final ProximityCheck$$ExternalSyntheticLambda0 mListener = new ThresholdSensor.Listener() { // from class: com.android.systemui.util.sensors.ProximityCheck$$ExternalSyntheticLambda0
        @Override // com.android.systemui.util.sensors.ThresholdSensor.Listener
        public final void onThresholdCrossed(ThresholdSensorEvent thresholdSensorEvent) {
            ProximityCheck.this.onProximityEvent(thresholdSensorEvent);
        }
    };

    public final void onProximityEvent(ThresholdSensorEvent thresholdSensorEvent) {
        this.mCallbacks.forEach(new PipMotionHelper$$ExternalSyntheticLambda1(thresholdSensorEvent, 4));
        this.mCallbacks.clear();
        this.mSensor.unregister(this.mListener);
        this.mRegistered.set(false);
        this.mRegistered.set(false);
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.mSensor.unregister(this.mListener);
        this.mRegistered.set(false);
        onProximityEvent(null);
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.util.sensors.ProximityCheck$$ExternalSyntheticLambda0] */
    public ProximityCheck(ProximitySensor proximitySensor, DelayableExecutor delayableExecutor) {
        this.mSensor = proximitySensor;
        proximitySensor.setTag("prox_check");
        this.mDelayableExecutor = delayableExecutor;
    }
}
