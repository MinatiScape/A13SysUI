package com.android.systemui.statusbar.notification.collection.provider;

import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStore;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
/* compiled from: NotificationVisibilityProviderImpl.kt */
/* loaded from: classes.dex */
public final class NotificationVisibilityProviderImpl implements NotificationVisibilityProvider {
    public final CommonNotifCollection notifCollection;
    public final NotifLiveDataStore notifDataStore;

    @Override // com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider
    public final NotificationVisibility obtain(String str) {
        NotificationEntry entry = this.notifCollection.getEntry(str);
        if (entry == null) {
            return NotificationVisibility.obtain(str, -1, ((Number) this.notifDataStore.getActiveNotifCount().getValue()).intValue(), false);
        }
        return obtain(entry, true);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider
    public final NotificationVisibility.NotificationLocation getLocation(String str) {
        return NotificationLogger.getNotificationLocation(this.notifCollection.getEntry(str));
    }

    public NotificationVisibilityProviderImpl(NotifLiveDataStore notifLiveDataStore, CommonNotifCollection commonNotifCollection) {
        this.notifDataStore = notifLiveDataStore;
        this.notifCollection = commonNotifCollection;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider
    public final NotificationVisibility obtain(NotificationEntry notificationEntry, boolean z) {
        int intValue = ((Number) this.notifDataStore.getActiveNotifCount().getValue()).intValue();
        int rank = notificationEntry.mRanking.getRank();
        boolean z2 = true;
        boolean z3 = notificationEntry.row != null;
        NotificationVisibility.NotificationLocation notificationLocation = NotificationLogger.getNotificationLocation(notificationEntry);
        String str = notificationEntry.mKey;
        if (!z || !z3) {
            z2 = false;
        }
        return NotificationVisibility.obtain(str, rank, intValue, z2, notificationLocation);
    }
}
