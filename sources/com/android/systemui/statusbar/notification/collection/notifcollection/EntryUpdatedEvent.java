package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotifEvent.kt */
/* loaded from: classes.dex */
public final class EntryUpdatedEvent extends NotifEvent {
    public final NotificationEntry entry;
    public final boolean fromSystem;

    public EntryUpdatedEvent(NotificationEntry notificationEntry, boolean z) {
        super(0);
        this.entry = notificationEntry;
        this.fromSystem = z;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EntryUpdatedEvent)) {
            return false;
        }
        EntryUpdatedEvent entryUpdatedEvent = (EntryUpdatedEvent) obj;
        return Intrinsics.areEqual(this.entry, entryUpdatedEvent.entry) && this.fromSystem == entryUpdatedEvent.fromSystem;
    }

    public final int hashCode() {
        int hashCode = this.entry.hashCode() * 31;
        boolean z = this.fromSystem;
        if (z) {
            z = true;
        }
        int i = z ? 1 : 0;
        int i2 = z ? 1 : 0;
        return hashCode + i;
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifEvent
    public final void dispatchToListener(NotifCollectionListener notifCollectionListener) {
        notifCollectionListener.onEntryUpdated(this.entry, this.fromSystem);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("EntryUpdatedEvent(entry=");
        m.append(this.entry);
        m.append(", fromSystem=");
        return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(m, this.fromSystem, ')');
    }
}
