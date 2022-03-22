package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.hardware.devicestate.DeviceStateManager;
import android.util.SparseIntArray;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.tuner.LockscreenFragment$$ExternalSyntheticLambda1;
import com.android.systemui.util.Assert;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class DevicePostureControllerImpl implements DevicePostureController {
    public final ArrayList mListeners = new ArrayList();
    public int mCurrentDevicePosture = 0;
    public final SparseIntArray mDeviceStateToPostureMap = new SparseIntArray();

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(DevicePostureController.Callback callback) {
        Assert.isMainThread();
        this.mListeners.add(callback);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(DevicePostureController.Callback callback) {
        Assert.isMainThread();
        this.mListeners.remove(callback);
    }

    public DevicePostureControllerImpl(Context context, DeviceStateManager deviceStateManager, Executor executor) {
        for (String str : context.getResources().getStringArray(17236026)) {
            String[] split = str.split(":");
            if (split.length == 2) {
                try {
                    this.mDeviceStateToPostureMap.put(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                } catch (NumberFormatException unused) {
                }
            }
        }
        deviceStateManager.registerCallback(executor, new DeviceStateManager.DeviceStateCallback() { // from class: com.android.systemui.statusbar.policy.DevicePostureControllerImpl$$ExternalSyntheticLambda0
            public final void onStateChanged(int i) {
                DevicePostureControllerImpl devicePostureControllerImpl = DevicePostureControllerImpl.this;
                Objects.requireNonNull(devicePostureControllerImpl);
                Assert.isMainThread();
                devicePostureControllerImpl.mCurrentDevicePosture = devicePostureControllerImpl.mDeviceStateToPostureMap.get(i, 0);
                devicePostureControllerImpl.mListeners.forEach(new LockscreenFragment$$ExternalSyntheticLambda1(devicePostureControllerImpl, 1));
            }
        });
    }

    @Override // com.android.systemui.statusbar.policy.DevicePostureController
    public final int getDevicePosture() {
        return this.mCurrentDevicePosture;
    }
}
