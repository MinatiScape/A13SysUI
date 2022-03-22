package com.android.systemui.statusbar.notification.collection.listbuilder.pluggable;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* compiled from: NotifStabilityManager.kt */
/* loaded from: classes.dex */
public final class DefaultNotifStabilityManager extends NotifStabilityManager {
    public static final DefaultNotifStabilityManager INSTANCE = new DefaultNotifStabilityManager();

    @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifStabilityManager
    public final boolean isEntryReorderingAllowed() {
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifStabilityManager
    public final boolean isEveryChangeAllowed() {
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifStabilityManager
    public final boolean isGroupChangeAllowed(NotificationEntry notificationEntry) {
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifStabilityManager
    public final boolean isPipelineRunAllowed() {
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifStabilityManager
    public final boolean isSectionChangeAllowed(NotificationEntry notificationEntry) {
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifStabilityManager
    public final void onBeginRun() {
    }

    @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifStabilityManager
    public final void onEntryReorderSuppressed() {
    }

    public DefaultNotifStabilityManager() {
        super("DefaultNotifStabilityManager");
    }
}
