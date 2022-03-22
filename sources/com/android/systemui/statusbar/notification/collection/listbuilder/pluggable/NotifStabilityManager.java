package com.android.systemui.statusbar.notification.collection.listbuilder.pluggable;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* compiled from: NotifStabilityManager.kt */
/* loaded from: classes.dex */
public abstract class NotifStabilityManager extends Pluggable<NotifStabilityManager> {
    public abstract boolean isEntryReorderingAllowed();

    public abstract boolean isEveryChangeAllowed();

    public abstract boolean isGroupChangeAllowed(NotificationEntry notificationEntry);

    public abstract boolean isPipelineRunAllowed();

    public abstract boolean isSectionChangeAllowed(NotificationEntry notificationEntry);

    public abstract void onBeginRun();

    public abstract void onEntryReorderSuppressed();

    public NotifStabilityManager(String str) {
        super(str);
    }
}
