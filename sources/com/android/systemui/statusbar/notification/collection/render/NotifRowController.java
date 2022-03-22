package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.notification.FeedbackIcon;
/* compiled from: NotifRowController.kt */
/* loaded from: classes.dex */
public interface NotifRowController {
    void setFeedbackIcon(FeedbackIcon feedbackIcon);

    void setLastAudiblyAlertedMs(long j);

    void setSystemExpanded(boolean z);
}
