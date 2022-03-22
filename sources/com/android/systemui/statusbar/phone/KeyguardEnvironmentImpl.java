package com.android.systemui.statusbar.phone;

import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
/* loaded from: classes.dex */
public final class KeyguardEnvironmentImpl implements NotificationEntryManager.KeyguardEnvironment {
    public final DeviceProvisionedController mDeviceProvisionedController;
    public final NotificationLockscreenUserManager mLockscreenUserManager;

    @Override // com.android.systemui.statusbar.notification.NotificationEntryManager.KeyguardEnvironment
    public final boolean isDeviceProvisioned() {
        return this.mDeviceProvisionedController.isDeviceProvisioned();
    }

    public KeyguardEnvironmentImpl(NotificationLockscreenUserManager notificationLockscreenUserManager, DeviceProvisionedController deviceProvisionedController) {
        this.mLockscreenUserManager = notificationLockscreenUserManager;
        this.mDeviceProvisionedController = deviceProvisionedController;
    }

    @Override // com.android.systemui.statusbar.notification.NotificationEntryManager.KeyguardEnvironment
    public final boolean isNotificationForCurrentProfiles(StatusBarNotification statusBarNotification) {
        return this.mLockscreenUserManager.isCurrentProfile(statusBarNotification.getUserId());
    }
}
