package com.android.systemui.statusbar.policy;

import android.hardware.SensorPrivacyManager;
import android.util.ArraySet;
import android.util.SparseBooleanArray;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class IndividualSensorPrivacyControllerImpl implements IndividualSensorPrivacyController {
    public static final int[] SENSORS = {2, 1};
    public final SensorPrivacyManager mSensorPrivacyManager;
    public final SparseBooleanArray mState = new SparseBooleanArray();
    public final ArraySet mCallbacks = new ArraySet();

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(IndividualSensorPrivacyController.Callback callback) {
        this.mCallbacks.add(callback);
    }

    public final void init() {
        int[] iArr = SENSORS;
        for (int i = 0; i < 2; i++) {
            final int i2 = iArr[i];
            this.mSensorPrivacyManager.addSensorPrivacyListener(i2, new SensorPrivacyManager.OnSensorPrivacyChangedListener() { // from class: com.android.systemui.statusbar.policy.IndividualSensorPrivacyControllerImpl$$ExternalSyntheticLambda0
                public final void onSensorPrivacyChanged(int i3, boolean z) {
                    IndividualSensorPrivacyControllerImpl individualSensorPrivacyControllerImpl = IndividualSensorPrivacyControllerImpl.this;
                    int i4 = i2;
                    Objects.requireNonNull(individualSensorPrivacyControllerImpl);
                    individualSensorPrivacyControllerImpl.mState.put(i4, z);
                    Iterator it = individualSensorPrivacyControllerImpl.mCallbacks.iterator();
                    while (it.hasNext()) {
                        ((IndividualSensorPrivacyController.Callback) it.next()).onSensorBlockedChanged(i4, z);
                    }
                }
            });
            this.mState.put(i2, this.mSensorPrivacyManager.isSensorPrivacyEnabled(i2));
        }
    }

    @Override // com.android.systemui.statusbar.policy.IndividualSensorPrivacyController
    public final boolean isSensorBlocked(int i) {
        return this.mState.get(i, false);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(IndividualSensorPrivacyController.Callback callback) {
        this.mCallbacks.remove(callback);
    }

    @Override // com.android.systemui.statusbar.policy.IndividualSensorPrivacyController
    public final void setSensorBlocked(int i, int i2, boolean z) {
        this.mSensorPrivacyManager.setSensorPrivacyForProfileGroup(i, i2, z);
    }

    @Override // com.android.systemui.statusbar.policy.IndividualSensorPrivacyController
    public final boolean supportsSensorToggle(int i) {
        return this.mSensorPrivacyManager.supportsSensorToggle(i);
    }

    @Override // com.android.systemui.statusbar.policy.IndividualSensorPrivacyController
    public final void suppressSensorPrivacyReminders(int i, boolean z) {
        this.mSensorPrivacyManager.suppressSensorPrivacyReminders(i, z);
    }

    public IndividualSensorPrivacyControllerImpl(SensorPrivacyManager sensorPrivacyManager) {
        this.mSensorPrivacyManager = sensorPrivacyManager;
    }
}
