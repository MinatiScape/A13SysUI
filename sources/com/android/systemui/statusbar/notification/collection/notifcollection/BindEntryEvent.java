package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotifEvent.kt */
/* loaded from: classes.dex */
public final class BindEntryEvent extends NotifEvent {
    public final NotificationEntry entry;
    public final StatusBarNotification sbn;

    public BindEntryEvent(NotificationEntry notificationEntry, StatusBarNotification statusBarNotification) {
        super(0);
        this.entry = notificationEntry;
        this.sbn = statusBarNotification;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BindEntryEvent)) {
            return false;
        }
        BindEntryEvent bindEntryEvent = (BindEntryEvent) obj;
        return Intrinsics.areEqual(this.entry, bindEntryEvent.entry) && Intrinsics.areEqual(this.sbn, bindEntryEvent.sbn);
    }

    public final int hashCode() {
        return this.sbn.hashCode() + (this.entry.hashCode() * 31);
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifEvent
    public final void dispatchToListener(NotifCollectionListener notifCollectionListener) {
        notifCollectionListener.onEntryBind(this.entry, this.sbn);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("BindEntryEvent(entry=");
        m.append(this.entry);
        m.append(", sbn=");
        m.append(this.sbn);
        m.append(')');
        return m.toString();
    }
}
