package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import java.util.Objects;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: HeadsUpCoordinator.kt */
/* loaded from: classes.dex */
final class HeadsUpCoordinator$findAlertOverride$1 extends Lambda implements Function1<HeadsUpCoordinator.PostedEntry, Boolean> {
    public static final HeadsUpCoordinator$findAlertOverride$1 INSTANCE = new HeadsUpCoordinator$findAlertOverride$1();

    public HeadsUpCoordinator$findAlertOverride$1() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Boolean invoke(HeadsUpCoordinator.PostedEntry postedEntry) {
        NotificationEntry notificationEntry = postedEntry.entry;
        Objects.requireNonNull(notificationEntry);
        return Boolean.valueOf(!notificationEntry.mSbn.getNotification().isGroupSummary());
    }
}
