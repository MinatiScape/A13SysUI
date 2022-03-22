package com.android.systemui.statusbar.notification.collection.coordinator;

import android.content.pm.IPackageManager;
import android.os.RemoteException;
import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DeviceProvisionedCoordinator implements Coordinator {
    public final DeviceProvisionedController mDeviceProvisionedController;
    public final IPackageManager mIPackageManager;
    public final AnonymousClass1 mNotifFilter = new NotifFilter() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.DeviceProvisionedCoordinator.1
        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter
        public final boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            boolean z;
            boolean z2;
            if (!DeviceProvisionedCoordinator.this.mDeviceProvisionedController.isDeviceProvisioned()) {
                DeviceProvisionedCoordinator deviceProvisionedCoordinator = DeviceProvisionedCoordinator.this;
                Objects.requireNonNull(notificationEntry);
                StatusBarNotification statusBarNotification = notificationEntry.mSbn;
                Objects.requireNonNull(deviceProvisionedCoordinator);
                try {
                    if (deviceProvisionedCoordinator.mIPackageManager.checkUidPermission("android.permission.NOTIFICATION_DURING_SETUP", statusBarNotification.getUid()) == 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (!z || !statusBarNotification.getNotification().extras.getBoolean("android.allowDuringSetup")) {
                        z2 = false;
                    } else {
                        z2 = true;
                    }
                    if (!z2) {
                        return true;
                    }
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            return false;
        }
    };
    public final AnonymousClass2 mDeviceProvisionedListener = new DeviceProvisionedController.DeviceProvisionedListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.DeviceProvisionedCoordinator.2
        @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
        public final void onDeviceProvisionedChanged() {
            invalidateList();
        }
    };

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public final void attach(NotifPipeline notifPipeline) {
        this.mDeviceProvisionedController.addCallback(this.mDeviceProvisionedListener);
        notifPipeline.addPreGroupFilter(this.mNotifFilter);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.statusbar.notification.collection.coordinator.DeviceProvisionedCoordinator$1] */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.statusbar.notification.collection.coordinator.DeviceProvisionedCoordinator$2] */
    public DeviceProvisionedCoordinator(DeviceProvisionedController deviceProvisionedController, IPackageManager iPackageManager) {
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mIPackageManager = iPackageManager;
    }
}
