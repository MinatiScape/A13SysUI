package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: HeadsUpCoordinator.kt */
/* loaded from: classes.dex */
final class HeadsUpCoordinator$findBestTransferChild$1 extends Lambda implements Function1<NotificationEntry, Boolean> {
    public static final HeadsUpCoordinator$findBestTransferChild$1 INSTANCE = new HeadsUpCoordinator$findBestTransferChild$1();

    public HeadsUpCoordinator$findBestTransferChild$1() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Boolean invoke(NotificationEntry notificationEntry) {
        return Boolean.valueOf(!notificationEntry.mSbn.getNotification().isGroupSummary());
    }
}
