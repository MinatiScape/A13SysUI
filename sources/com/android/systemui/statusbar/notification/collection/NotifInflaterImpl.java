package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.statusbar.notification.InflationException;
import com.android.systemui.statusbar.notification.collection.inflation.NotifInflater;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.row.NotifInflationErrorManager;
import com.android.systemui.statusbar.notification.row.NotificationRowContentBinder;
import com.google.android.systemui.lowlightclock.LowLightDockManager$$ExternalSyntheticLambda0;
import java.util.Objects;
/* loaded from: classes.dex */
public final class NotifInflaterImpl implements NotifInflater {
    public final NotifInflationErrorManager mNotifErrorManager;
    public NotificationRowBinderImpl mNotificationRowBinder;

    @Override // com.android.systemui.statusbar.notification.collection.inflation.NotifInflater
    public final void inflateViews(NotificationEntry notificationEntry, NotifInflater.Params params, final NotifInflater.InflationCallback inflationCallback) {
        try {
            NotificationRowBinderImpl notificationRowBinderImpl = this.mNotificationRowBinder;
            if (notificationRowBinderImpl != null) {
                notificationRowBinderImpl.inflateViews(notificationEntry, params, new NotificationRowContentBinder.InflationCallback() { // from class: com.android.systemui.statusbar.notification.collection.NotifInflaterImpl.1
                    @Override // com.android.systemui.statusbar.notification.row.NotificationRowContentBinder.InflationCallback
                    public final void handleInflationException(NotificationEntry notificationEntry2, Exception exc) {
                        NotifInflaterImpl.this.mNotifErrorManager.setInflationError(notificationEntry2, exc);
                    }

                    @Override // com.android.systemui.statusbar.notification.row.NotificationRowContentBinder.InflationCallback
                    public final void onAsyncInflationFinished(NotificationEntry notificationEntry2) {
                        NotifInflaterImpl.this.mNotifErrorManager.clearInflationError(notificationEntry2);
                        NotifInflater.InflationCallback inflationCallback2 = inflationCallback;
                        if (inflationCallback2 != null) {
                            Objects.requireNonNull(notificationEntry2);
                            inflationCallback2.onInflationFinished(notificationEntry2, notificationEntry2.mRowController);
                        }
                    }
                });
                return;
            }
            throw new RuntimeException("NotificationRowBinder must be attached before using NotifInflaterImpl.");
        } catch (InflationException e) {
            this.mNotifErrorManager.setInflationError(notificationEntry, e);
        }
    }

    public NotifInflaterImpl(NotifInflationErrorManager notifInflationErrorManager) {
        this.mNotifErrorManager = notifInflationErrorManager;
    }

    @Override // com.android.systemui.statusbar.notification.collection.inflation.NotifInflater
    public final void abortInflation(NotificationEntry notificationEntry) {
        notificationEntry.abortTask();
    }

    @Override // com.android.systemui.statusbar.notification.collection.inflation.NotifInflater
    public final void rebindViews(NotificationEntry notificationEntry, NotifInflater.Params params, LowLightDockManager$$ExternalSyntheticLambda0 lowLightDockManager$$ExternalSyntheticLambda0) {
        inflateViews(notificationEntry, params, lowLightDockManager$$ExternalSyntheticLambda0);
    }
}
