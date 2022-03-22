package com.android.systemui.statusbar.notification.row;

import android.util.ArrayMap;
import android.util.SparseArray;
import android.widget.RemoteViews;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* loaded from: classes.dex */
public final class NotifRemoteViewCacheImpl implements NotifRemoteViewCache {
    public final AnonymousClass1 mCollectionListener;
    public final ArrayMap mNotifCachedContentViews = new ArrayMap();

    @Override // com.android.systemui.statusbar.notification.row.NotifRemoteViewCache
    public final void clearCache(NotificationEntry notificationEntry) {
        SparseArray sparseArray = (SparseArray) this.mNotifCachedContentViews.get(notificationEntry);
        if (sparseArray != null) {
            sparseArray.clear();
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.NotifRemoteViewCache
    public final RemoteViews getCachedView(NotificationEntry notificationEntry, int i) {
        SparseArray sparseArray = (SparseArray) this.mNotifCachedContentViews.get(notificationEntry);
        if (sparseArray == null) {
            return null;
        }
        return (RemoteViews) sparseArray.get(i);
    }

    @Override // com.android.systemui.statusbar.notification.row.NotifRemoteViewCache
    public final void putCachedView(NotificationEntry notificationEntry, int i, RemoteViews remoteViews) {
        SparseArray sparseArray = (SparseArray) this.mNotifCachedContentViews.get(notificationEntry);
        if (sparseArray != null) {
            sparseArray.put(i, remoteViews);
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.NotifRemoteViewCache
    public final void removeCachedView(NotificationEntry notificationEntry, int i) {
        SparseArray sparseArray = (SparseArray) this.mNotifCachedContentViews.get(notificationEntry);
        if (sparseArray != null) {
            sparseArray.remove(i);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.statusbar.notification.row.NotifRemoteViewCacheImpl$1, com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public NotifRemoteViewCacheImpl(com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection r2) {
        /*
            r1 = this;
            r1.<init>()
            android.util.ArrayMap r0 = new android.util.ArrayMap
            r0.<init>()
            r1.mNotifCachedContentViews = r0
            com.android.systemui.statusbar.notification.row.NotifRemoteViewCacheImpl$1 r0 = new com.android.systemui.statusbar.notification.row.NotifRemoteViewCacheImpl$1
            r0.<init>()
            r1.mCollectionListener = r0
            r2.addCollectionListener(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.NotifRemoteViewCacheImpl.<init>(com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection):void");
    }

    @Override // com.android.systemui.statusbar.notification.row.NotifRemoteViewCache
    public final boolean hasCachedView(NotificationEntry notificationEntry, int i) {
        if (getCachedView(notificationEntry, i) != null) {
            return true;
        }
        return false;
    }
}
