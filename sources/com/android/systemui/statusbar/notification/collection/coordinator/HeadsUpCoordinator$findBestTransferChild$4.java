package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: HeadsUpCoordinator.kt */
/* loaded from: classes.dex */
final class HeadsUpCoordinator$findBestTransferChild$4 extends Lambda implements Function1<NotificationEntry, Comparable<?>> {
    public static final HeadsUpCoordinator$findBestTransferChild$4 INSTANCE = new HeadsUpCoordinator$findBestTransferChild$4();

    public HeadsUpCoordinator$findBestTransferChild$4() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Comparable<?> invoke(NotificationEntry notificationEntry) {
        return Long.valueOf(-notificationEntry.mSbn.getNotification().when);
    }
}
