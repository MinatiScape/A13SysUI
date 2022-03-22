package com.android.systemui.statusbar.notification.collection;

import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.notification.collection.notifcollection.BindEntryEvent;
import com.android.systemui.statusbar.notification.collection.notifcollection.EntryUpdatedEvent;
import com.android.systemui.util.Assert;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NotifCollection$$ExternalSyntheticLambda0 {
    public final /* synthetic */ NotifCollection f$0;
    public final /* synthetic */ String f$1 = "RemoteInputCoordinator";

    public /* synthetic */ NotifCollection$$ExternalSyntheticLambda0(NotifCollection notifCollection) {
        this.f$0 = notifCollection;
    }

    public final void onInternalNotificationUpdate(final StatusBarNotification statusBarNotification, final String str) {
        final NotifCollection notifCollection = this.f$0;
        final String str2 = this.f$1;
        Objects.requireNonNull(notifCollection);
        notifCollection.mMainHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.notification.collection.NotifCollection$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                NotifCollection notifCollection2 = NotifCollection.this;
                StatusBarNotification statusBarNotification2 = statusBarNotification;
                String str3 = str2;
                String str4 = str;
                Objects.requireNonNull(notifCollection2);
                Assert.isMainThread();
                notifCollection2.checkForReentrantCall();
                NotificationEntry notificationEntry = (NotificationEntry) notifCollection2.mNotificationSet.get(statusBarNotification2.getKey());
                if (notificationEntry == null) {
                    notifCollection2.mLogger.logNotifInternalUpdateFailed(statusBarNotification2.getKey(), str3, str4);
                    return;
                }
                notifCollection2.mLogger.logNotifInternalUpdate(statusBarNotification2.getKey(), str3, str4);
                notificationEntry.setSbn(statusBarNotification2);
                notifCollection2.mEventQueue.add(new BindEntryEvent(notificationEntry, statusBarNotification2));
                notifCollection2.mLogger.logNotifUpdated(statusBarNotification2.getKey());
                notifCollection2.mEventQueue.add(new EntryUpdatedEvent(notificationEntry, false));
                notifCollection2.dispatchEventsAndRebuildList();
            }
        });
    }
}
