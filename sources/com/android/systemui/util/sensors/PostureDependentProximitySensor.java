package com.android.systemui.util.sensors;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.Log;
import com.android.systemui.statusbar.policy.DevicePostureController;
/* loaded from: classes.dex */
public final class PostureDependentProximitySensor extends ProximitySensorImpl {
    public final PostureDependentProximitySensor$$ExternalSyntheticLambda0 mDevicePostureCallback;
    public final DevicePostureController mDevicePostureController;
    public final ThresholdSensor[] mPostureToPrimaryProxSensorMap;
    public final ThresholdSensor[] mPostureToSecondaryProxSensorMap;

    /* JADX WARN: Type inference failed for: r5v1, types: [com.android.systemui.util.sensors.PostureDependentProximitySensor$$ExternalSyntheticLambda0, java.lang.Object] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public PostureDependentProximitySensor(com.android.systemui.util.sensors.ThresholdSensor[] r3, com.android.systemui.util.sensors.ThresholdSensor[] r4, com.android.systemui.util.concurrency.DelayableExecutor r5, com.android.systemui.util.concurrency.Execution r6, com.android.systemui.statusbar.policy.DevicePostureController r7) {
        /*
            r2 = this;
            r0 = 0
            r1 = r3[r0]
            r0 = r4[r0]
            r2.<init>(r1, r0, r5, r6)
            com.android.systemui.util.sensors.PostureDependentProximitySensor$$ExternalSyntheticLambda0 r5 = new com.android.systemui.util.sensors.PostureDependentProximitySensor$$ExternalSyntheticLambda0
            r5.<init>()
            r2.mDevicePostureCallback = r5
            r2.mPostureToPrimaryProxSensorMap = r3
            r2.mPostureToSecondaryProxSensorMap = r4
            r2.mDevicePostureController = r7
            int r3 = r7.getDevicePosture()
            r2.mDevicePosture = r3
            r7.addCallback(r5)
            r2.chooseSensors()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.util.sensors.PostureDependentProximitySensor.<init>(com.android.systemui.util.sensors.ThresholdSensor[], com.android.systemui.util.sensors.ThresholdSensor[], com.android.systemui.util.concurrency.DelayableExecutor, com.android.systemui.util.concurrency.Execution, com.android.systemui.statusbar.policy.DevicePostureController):void");
    }

    @Override // com.android.systemui.util.sensors.ProximitySensorImpl
    public final String toString() {
        return String.format("{posture=%s, proximitySensor=%s}", DevicePostureController.devicePostureToString(this.mDevicePosture), super.toString());
    }

    public final void chooseSensors() {
        int i = this.mDevicePosture;
        ThresholdSensor[] thresholdSensorArr = this.mPostureToPrimaryProxSensorMap;
        if (i < thresholdSensorArr.length) {
            ThresholdSensor[] thresholdSensorArr2 = this.mPostureToSecondaryProxSensorMap;
            if (i < thresholdSensorArr2.length) {
                ThresholdSensor thresholdSensor = thresholdSensorArr[i];
                ThresholdSensor thresholdSensor2 = thresholdSensorArr2[i];
                if (thresholdSensor != this.mPrimaryThresholdSensor || thresholdSensor2 != this.mSecondaryThresholdSensor) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Register new proximity sensors newPosture=");
                    m.append(DevicePostureController.devicePostureToString(this.mDevicePosture));
                    logDebug(m.toString());
                    unregisterInternal();
                    ThresholdSensor thresholdSensor3 = this.mPrimaryThresholdSensor;
                    if (thresholdSensor3 != null) {
                        thresholdSensor3.unregister(this.mPrimaryEventListener);
                    }
                    ThresholdSensor thresholdSensor4 = this.mSecondaryThresholdSensor;
                    if (thresholdSensor4 != null) {
                        thresholdSensor4.unregister(this.mSecondaryEventListener);
                    }
                    this.mPrimaryThresholdSensor = thresholdSensor;
                    this.mSecondaryThresholdSensor = thresholdSensor2;
                    this.mInitializedListeners = false;
                    registerInternal();
                    return;
                }
                return;
            }
        }
        StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("unsupported devicePosture=");
        m2.append(this.mDevicePosture);
        Log.e("PostureDependProxSensor", m2.toString());
    }

    @Override // com.android.systemui.util.sensors.ProximitySensorImpl, com.android.systemui.util.sensors.ProximitySensor
    public final void destroy() {
        pause();
        this.mDevicePostureController.removeCallback(this.mDevicePostureCallback);
    }
}
