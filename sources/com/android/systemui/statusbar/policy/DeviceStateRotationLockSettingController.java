package com.android.systemui.statusbar.policy;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.hardware.devicestate.DeviceStateManager;
import android.util.Log;
import android.util.SparseIntArray;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline1;
import com.android.settingslib.devicestate.DeviceStateRotationLockSettingsManager;
import com.android.systemui.statusbar.policy.RotationLockController;
import com.android.systemui.util.wrapper.RotationPolicyWrapper;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class DeviceStateRotationLockSettingController implements RotationLockController.RotationLockControllerCallback {
    public int mDeviceState = -1;
    public DeviceStateRotationLockSettingController$$ExternalSyntheticLambda0 mDeviceStateCallback;
    public final DeviceStateManager mDeviceStateManager;
    public DeviceStateRotationLockSettingController$$ExternalSyntheticLambda1 mDeviceStateRotationLockSettingsListener;
    public final DeviceStateRotationLockSettingsManager mDeviceStateRotationLockSettingsManager;
    public final Executor mMainExecutor;
    public final RotationPolicyWrapper mRotationPolicyWrapper;

    @Override // com.android.systemui.statusbar.policy.RotationLockController.RotationLockControllerCallback
    public final void onRotationLockStateChanged(boolean z) {
        boolean z2;
        int i = this.mDeviceState;
        if (i == -1) {
            Log.wtf("DSRotateLockSettingCon", "Device state was not initialized.");
            return;
        }
        DeviceStateRotationLockSettingsManager deviceStateRotationLockSettingsManager = this.mDeviceStateRotationLockSettingsManager;
        Objects.requireNonNull(deviceStateRotationLockSettingsManager);
        int rotationLockSetting = deviceStateRotationLockSettingsManager.getRotationLockSetting(i);
        int i2 = 1;
        if (rotationLockSetting == 1) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (z == z2) {
            Log.v("DSRotateLockSettingCon", "Rotation lock same as the current setting, no need to update.");
            return;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("saveNewRotationLockSetting [state=");
        m.append(this.mDeviceState);
        m.append("] [isRotationLocked=");
        m.append(z);
        m.append("]");
        Log.v("DSRotateLockSettingCon", m.toString());
        DeviceStateRotationLockSettingsManager deviceStateRotationLockSettingsManager2 = this.mDeviceStateRotationLockSettingsManager;
        int i3 = this.mDeviceState;
        Objects.requireNonNull(deviceStateRotationLockSettingsManager2);
        if (deviceStateRotationLockSettingsManager2.mDeviceStateRotationLockFallbackSettings.indexOfKey(i3) >= 0) {
            i3 = deviceStateRotationLockSettingsManager2.mDeviceStateRotationLockFallbackSettings.get(i3);
        }
        SparseIntArray sparseIntArray = deviceStateRotationLockSettingsManager2.mDeviceStateRotationLockSettings;
        if (!z) {
            i2 = 2;
        }
        sparseIntArray.put(i3, i2);
        deviceStateRotationLockSettingsManager2.persistSettings();
    }

    public final void readPersistedSetting(int i) {
        int rotationLockSetting = this.mDeviceStateRotationLockSettingsManager.getRotationLockSetting(i);
        if (rotationLockSetting == 0) {
            GridLayoutManager$$ExternalSyntheticOutline1.m("Missing fallback. Ignoring new device state: ", i, "DSRotateLockSettingCon");
            return;
        }
        this.mDeviceState = i;
        boolean z = true;
        if (rotationLockSetting != 1) {
            z = false;
        }
        if (z != this.mRotationPolicyWrapper.isRotationLocked()) {
            this.mRotationPolicyWrapper.setRotationLock(z);
        }
    }

    public DeviceStateRotationLockSettingController(RotationPolicyWrapper rotationPolicyWrapper, DeviceStateManager deviceStateManager, Executor executor, DeviceStateRotationLockSettingsManager deviceStateRotationLockSettingsManager) {
        this.mRotationPolicyWrapper = rotationPolicyWrapper;
        this.mDeviceStateManager = deviceStateManager;
        this.mMainExecutor = executor;
        this.mDeviceStateRotationLockSettingsManager = deviceStateRotationLockSettingsManager;
    }
}
