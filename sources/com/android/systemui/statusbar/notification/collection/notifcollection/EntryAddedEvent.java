package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotifEvent.kt */
/* loaded from: classes.dex */
public final class EntryAddedEvent extends NotifEvent {
    public final NotificationEntry entry;

    public EntryAddedEvent(NotificationEntry notificationEntry) {
        super(0);
        this.entry = notificationEntry;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof EntryAddedEvent) && Intrinsics.areEqual(this.entry, ((EntryAddedEvent) obj).entry);
    }

    public final int hashCode() {
        return this.entry.hashCode();
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifEvent
    public final void dispatchToListener(NotifCollectionListener notifCollectionListener) {
        notifCollectionListener.onEntryAdded(this.entry);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("EntryAddedEvent(entry=");
        m.append(this.entry);
        m.append(')');
        return m.toString();
    }
}
