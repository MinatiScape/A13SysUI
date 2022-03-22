package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.provider.DebugModeFilterProvider;
import java.util.Objects;
/* compiled from: DebugModeCoordinator.kt */
/* loaded from: classes.dex */
public final class DebugModeCoordinator implements Coordinator {
    public final DebugModeFilterProvider debugModeFilterProvider;
    public final DebugModeCoordinator$preGroupFilter$1 preGroupFilter = new NotifFilter() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.DebugModeCoordinator$preGroupFilter$1
        {
            super("DebugModeCoordinator");
        }

        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter
        public final boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            DebugModeFilterProvider debugModeFilterProvider = DebugModeCoordinator.this.debugModeFilterProvider;
            Objects.requireNonNull(debugModeFilterProvider);
            if (debugModeFilterProvider.allowedPackages.isEmpty()) {
                return false;
            }
            return !debugModeFilterProvider.allowedPackages.contains(notificationEntry.mSbn.getPackageName());
        }
    };

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public final void attach(NotifPipeline notifPipeline) {
        notifPipeline.addPreGroupFilter(this.preGroupFilter);
        DebugModeFilterProvider debugModeFilterProvider = this.debugModeFilterProvider;
        final DebugModeCoordinator$preGroupFilter$1 debugModeCoordinator$preGroupFilter$1 = this.preGroupFilter;
        debugModeFilterProvider.registerInvalidationListener(new Runnable() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.DebugModeCoordinator$attach$1
            @Override // java.lang.Runnable
            public final void run() {
                invalidateList();
            }
        });
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.statusbar.notification.collection.coordinator.DebugModeCoordinator$preGroupFilter$1] */
    public DebugModeCoordinator(DebugModeFilterProvider debugModeFilterProvider) {
        this.debugModeFilterProvider = debugModeFilterProvider;
    }
}
