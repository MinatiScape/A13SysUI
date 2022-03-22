package com.android.systemui.statusbar.policy;

import android.hardware.SensorPrivacyManager;
import com.android.systemui.statusbar.policy.SensorPrivacyController;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public final class SensorPrivacyControllerImpl implements SensorPrivacyController, SensorPrivacyManager.OnAllSensorPrivacyChangedListener {
    public final ArrayList mListeners = new ArrayList(1);
    public Object mLock = new Object();
    public boolean mSensorPrivacyEnabled;
    public SensorPrivacyManager mSensorPrivacyManager;

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(SensorPrivacyController.OnSensorPrivacyChangedListener onSensorPrivacyChangedListener) {
        SensorPrivacyController.OnSensorPrivacyChangedListener onSensorPrivacyChangedListener2 = onSensorPrivacyChangedListener;
        synchronized (this.mLock) {
            this.mListeners.add(onSensorPrivacyChangedListener2);
            onSensorPrivacyChangedListener2.onSensorPrivacyChanged(this.mSensorPrivacyEnabled);
        }
    }

    @Override // com.android.systemui.statusbar.policy.SensorPrivacyController
    public final boolean isSensorPrivacyEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mSensorPrivacyEnabled;
        }
        return z;
    }

    public final void onAllSensorPrivacyChanged(boolean z) {
        synchronized (this.mLock) {
            this.mSensorPrivacyEnabled = z;
            Iterator it = this.mListeners.iterator();
            while (it.hasNext()) {
                ((SensorPrivacyController.OnSensorPrivacyChangedListener) it.next()).onSensorPrivacyChanged(this.mSensorPrivacyEnabled);
            }
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(SensorPrivacyController.OnSensorPrivacyChangedListener onSensorPrivacyChangedListener) {
        SensorPrivacyController.OnSensorPrivacyChangedListener onSensorPrivacyChangedListener2 = onSensorPrivacyChangedListener;
        synchronized (this.mLock) {
            this.mListeners.remove(onSensorPrivacyChangedListener2);
        }
    }

    public SensorPrivacyControllerImpl(SensorPrivacyManager sensorPrivacyManager) {
        this.mSensorPrivacyManager = sensorPrivacyManager;
    }
}
