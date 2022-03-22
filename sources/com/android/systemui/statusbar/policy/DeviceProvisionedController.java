package com.android.systemui.statusbar.policy;
/* loaded from: classes.dex */
public interface DeviceProvisionedController extends CallbackController<DeviceProvisionedListener> {
    boolean isCurrentUserSetup();

    boolean isDeviceProvisioned();

    boolean isUserSetup(int i);

    /* loaded from: classes.dex */
    public interface DeviceProvisionedListener {
        default void onDeviceProvisionedChanged() {
        }

        default void onUserSetupChanged() {
        }

        default void onUserSwitched() {
            onUserSetupChanged();
        }
    }
}
