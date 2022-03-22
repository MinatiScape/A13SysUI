package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class NotificationListController {
    public final DeviceProvisionedController mDeviceProvisionedController;
    public final NotificationEntryManager mEntryManager;
    public final NotificationListContainer mListContainer;
    public final AnonymousClass1 mEntryListener = new NotificationEntryListener() { // from class: com.android.systemui.statusbar.notification.NotificationListController.1
        @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
        public final void onEntryRemoved(NotificationEntry notificationEntry, boolean z) {
            NotificationListController.this.mListContainer.cleanUpViewStateForEntry(notificationEntry);
        }
    };
    public final AnonymousClass2 mDeviceProvisionedListener = new DeviceProvisionedController.DeviceProvisionedListener() { // from class: com.android.systemui.statusbar.notification.NotificationListController.2
        @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
        public final void onDeviceProvisionedChanged() {
            NotificationListController.this.mEntryManager.updateNotifications("device provisioned changed");
        }
    };

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.statusbar.notification.NotificationListController$1] */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.statusbar.notification.NotificationListController$2] */
    public NotificationListController(NotificationEntryManager notificationEntryManager, NotificationStackScrollLayoutController.NotificationListContainerImpl notificationListContainerImpl, DeviceProvisionedController deviceProvisionedController) {
        Objects.requireNonNull(notificationEntryManager);
        this.mEntryManager = notificationEntryManager;
        Objects.requireNonNull(notificationListContainerImpl);
        this.mListContainer = notificationListContainerImpl;
        Objects.requireNonNull(deviceProvisionedController);
        this.mDeviceProvisionedController = deviceProvisionedController;
    }
}
