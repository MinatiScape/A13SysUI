package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import java.util.Objects;
/* loaded from: classes.dex */
public final class HideLocallyDismissedNotifsCoordinator implements Coordinator {
    public final AnonymousClass1 mFilter = new NotifFilter() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.HideLocallyDismissedNotifsCoordinator.1
        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter
        public final boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            Objects.requireNonNull(notificationEntry);
            if (notificationEntry.mDismissState != NotificationEntry.DismissState.NOT_DISMISSED) {
                return true;
            }
            return false;
        }
    };

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public final void attach(NotifPipeline notifPipeline) {
        notifPipeline.addPreGroupFilter(this.mFilter);
    }
}
