package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotifEvent.kt */
/* loaded from: classes.dex */
public final class CleanUpEntryEvent extends NotifEvent {
    public final NotificationEntry entry;

    public CleanUpEntryEvent(NotificationEntry notificationEntry) {
        super(0);
        this.entry = notificationEntry;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof CleanUpEntryEvent) && Intrinsics.areEqual(this.entry, ((CleanUpEntryEvent) obj).entry);
    }

    public final int hashCode() {
        return this.entry.hashCode();
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifEvent
    public final void dispatchToListener(NotifCollectionListener notifCollectionListener) {
        notifCollectionListener.onEntryCleanUp(this.entry);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("CleanUpEntryEvent(entry=");
        m.append(this.entry);
        m.append(')');
        return m.toString();
    }
}
