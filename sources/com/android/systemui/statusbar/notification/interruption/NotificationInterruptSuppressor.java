package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* loaded from: classes.dex */
public interface NotificationInterruptSuppressor {
    default boolean suppressAwakeHeadsUp(NotificationEntry notificationEntry) {
        return false;
    }

    default boolean suppressAwakeInterruptions() {
        return false;
    }

    default boolean suppressInterruptions() {
        return false;
    }

    default String getName() {
        return getClass().getName();
    }
}
