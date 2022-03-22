package com.android.systemui.statusbar.notification.collection.inflation;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController;
import com.google.android.systemui.lowlightclock.LowLightDockManager$$ExternalSyntheticLambda0;
/* compiled from: NotifInflater.kt */
/* loaded from: classes.dex */
public interface NotifInflater {

    /* compiled from: NotifInflater.kt */
    /* loaded from: classes.dex */
    public interface InflationCallback {
        void onInflationFinished(NotificationEntry notificationEntry, ExpandableNotificationRowController expandableNotificationRowController);
    }

    void abortInflation(NotificationEntry notificationEntry);

    void inflateViews(NotificationEntry notificationEntry, Params params, InflationCallback inflationCallback);

    void rebindViews(NotificationEntry notificationEntry, Params params, LowLightDockManager$$ExternalSyntheticLambda0 lowLightDockManager$$ExternalSyntheticLambda0);

    /* compiled from: NotifInflater.kt */
    /* loaded from: classes.dex */
    public static final class Params {
        public final boolean isLowPriority;

        public Params(boolean z) {
            this.isLowPriority = z;
        }
    }
}
