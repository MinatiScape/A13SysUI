package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.internal.statusbar.NotificationVisibility;
/* loaded from: classes.dex */
public final class DismissedByUserStats {
    public final int dismissalSentiment = 1;
    public final int dismissalSurface;
    public final NotificationVisibility notificationVisibility;

    public DismissedByUserStats(int i, NotificationVisibility notificationVisibility) {
        this.dismissalSurface = i;
        this.notificationVisibility = notificationVisibility;
    }
}
