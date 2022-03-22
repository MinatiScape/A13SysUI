package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* loaded from: classes.dex */
public interface NotifDismissInterceptor {

    /* loaded from: classes.dex */
    public interface OnEndDismissInterception {
    }

    void cancelDismissInterception(NotificationEntry notificationEntry);

    void getName();

    boolean shouldInterceptDismissal(NotificationEntry notificationEntry);
}
