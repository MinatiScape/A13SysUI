package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.RotationLockController;
import com.android.systemui.util.wrapper.RotationPolicyWrapper;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes.dex */
public final class RotationLockControllerImpl implements RotationLockController {
    public final CopyOnWriteArrayList<RotationLockController.RotationLockControllerCallback> mCallbacks;
    public final RotationPolicyWrapper mRotationPolicy;
    public final AnonymousClass1 mRotationPolicyListener;

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(RotationLockController.RotationLockControllerCallback rotationLockControllerCallback) {
        RotationLockController.RotationLockControllerCallback rotationLockControllerCallback2 = rotationLockControllerCallback;
        this.mCallbacks.add(rotationLockControllerCallback2);
        boolean isRotationLocked = this.mRotationPolicy.isRotationLocked();
        this.mRotationPolicy.isRotationLockToggleVisible();
        rotationLockControllerCallback2.onRotationLockStateChanged(isRotationLocked);
    }

    @Override // com.android.systemui.statusbar.policy.RotationLockController
    public final int getRotationLockOrientation() {
        return this.mRotationPolicy.getRotationLockOrientation();
    }

    @Override // com.android.systemui.statusbar.policy.RotationLockController
    public final boolean isCameraRotationEnabled() {
        return this.mRotationPolicy.isCameraRotationEnabled();
    }

    @Override // com.android.systemui.statusbar.policy.RotationLockController
    public final boolean isRotationLocked() {
        return this.mRotationPolicy.isRotationLocked();
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(RotationLockController.RotationLockControllerCallback rotationLockControllerCallback) {
        this.mCallbacks.remove(rotationLockControllerCallback);
    }

    @Override // com.android.systemui.statusbar.policy.RotationLockController
    public final void setRotationLocked(boolean z) {
        this.mRotationPolicy.setRotationLock(z);
    }

    /* JADX WARN: Type inference failed for: r2v4, types: [com.android.systemui.statusbar.policy.DeviceStateRotationLockSettingController$$ExternalSyntheticLambda1, java.lang.Object] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public RotationLockControllerImpl(com.android.systemui.util.wrapper.RotationPolicyWrapper r3, final com.android.systemui.statusbar.policy.DeviceStateRotationLockSettingController r4, java.lang.String[] r5) {
        /*
            r2 = this;
            r2.<init>()
            java.util.concurrent.CopyOnWriteArrayList r0 = new java.util.concurrent.CopyOnWriteArrayList
            r0.<init>()
            r2.mCallbacks = r0
            com.android.systemui.statusbar.policy.RotationLockControllerImpl$1 r1 = new com.android.systemui.statusbar.policy.RotationLockControllerImpl$1
            r1.<init>()
            r2.mRotationPolicyListener = r1
            r2.mRotationPolicy = r3
            int r2 = r5.length
            if (r2 <= 0) goto L_0x0018
            r2 = 1
            goto L_0x0019
        L_0x0018:
            r2 = 0
        L_0x0019:
            if (r2 == 0) goto L_0x001e
            r0.add(r4)
        L_0x001e:
            r3.registerRotationPolicyListener(r1)
            if (r2 == 0) goto L_0x0045
            java.util.Objects.requireNonNull(r4)
            com.android.systemui.statusbar.policy.DeviceStateRotationLockSettingController$$ExternalSyntheticLambda0 r2 = new com.android.systemui.statusbar.policy.DeviceStateRotationLockSettingController$$ExternalSyntheticLambda0
            r2.<init>()
            r4.mDeviceStateCallback = r2
            android.hardware.devicestate.DeviceStateManager r3 = r4.mDeviceStateManager
            java.util.concurrent.Executor r5 = r4.mMainExecutor
            r3.registerCallback(r5, r2)
            com.android.systemui.statusbar.policy.DeviceStateRotationLockSettingController$$ExternalSyntheticLambda1 r2 = new com.android.systemui.statusbar.policy.DeviceStateRotationLockSettingController$$ExternalSyntheticLambda1
            r2.<init>()
            r4.mDeviceStateRotationLockSettingsListener = r2
            com.android.settingslib.devicestate.DeviceStateRotationLockSettingsManager r3 = r4.mDeviceStateRotationLockSettingsManager
            java.util.Objects.requireNonNull(r3)
            java.util.HashSet r3 = r3.mListeners
            r3.add(r2)
        L_0x0045:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.RotationLockControllerImpl.<init>(com.android.systemui.util.wrapper.RotationPolicyWrapper, com.android.systemui.statusbar.policy.DeviceStateRotationLockSettingController, java.lang.String[]):void");
    }
}
