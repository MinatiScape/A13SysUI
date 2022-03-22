package com.android.systemui.statusbar;

import com.android.systemui.screenshot.ScreenshotController$$ExternalSyntheticLambda3;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* loaded from: classes.dex */
public interface NotificationLifetimeExtender {

    /* loaded from: classes.dex */
    public interface NotificationSafeToRemoveCallback {
    }

    void setCallback(ScreenshotController$$ExternalSyntheticLambda3 screenshotController$$ExternalSyntheticLambda3);

    void setShouldManageLifetime(NotificationEntry notificationEntry, boolean z);

    boolean shouldExtendLifetime(NotificationEntry notificationEntry);
}
