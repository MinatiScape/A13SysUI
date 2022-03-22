package com.android.systemui.statusbar.notification.collection.render;

import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* compiled from: NotificationVisibilityProvider.kt */
/* loaded from: classes.dex */
public interface NotificationVisibilityProvider {
    NotificationVisibility.NotificationLocation getLocation(String str);

    NotificationVisibility obtain(NotificationEntry notificationEntry, boolean z);

    NotificationVisibility obtain(String str);
}
