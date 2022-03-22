package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.core.graphics.Insets$$ExternalSyntheticOutline0;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotifEvent.kt */
/* loaded from: classes.dex */
public final class EntryRemovedEvent extends NotifEvent {
    public final NotificationEntry entry;
    public final int reason;

    public EntryRemovedEvent(NotificationEntry notificationEntry, int i) {
        super(0);
        this.entry = notificationEntry;
        this.reason = i;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EntryRemovedEvent)) {
            return false;
        }
        EntryRemovedEvent entryRemovedEvent = (EntryRemovedEvent) obj;
        return Intrinsics.areEqual(this.entry, entryRemovedEvent.entry) && this.reason == entryRemovedEvent.reason;
    }

    public final int hashCode() {
        return Integer.hashCode(this.reason) + (this.entry.hashCode() * 31);
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifEvent
    public final void dispatchToListener(NotifCollectionListener notifCollectionListener) {
        notifCollectionListener.onEntryRemoved(this.entry, this.reason);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("EntryRemovedEvent(entry=");
        m.append(this.entry);
        m.append(", reason=");
        return Insets$$ExternalSyntheticOutline0.m(m, this.reason, ')');
    }
}
