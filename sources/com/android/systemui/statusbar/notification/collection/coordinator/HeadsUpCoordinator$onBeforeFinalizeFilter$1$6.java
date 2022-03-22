package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import java.util.Objects;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: HeadsUpCoordinator.kt */
/* loaded from: classes.dex */
final class HeadsUpCoordinator$onBeforeFinalizeFilter$1$6 extends Lambda implements Function1<HeadsUpCoordinator.PostedEntry, Boolean> {
    public final /* synthetic */ NotificationEntry $logicalSummary;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public HeadsUpCoordinator$onBeforeFinalizeFilter$1$6(NotificationEntry notificationEntry) {
        super(1);
        this.$logicalSummary = notificationEntry;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Boolean invoke(HeadsUpCoordinator.PostedEntry postedEntry) {
        String str = postedEntry.key;
        NotificationEntry notificationEntry = this.$logicalSummary;
        Objects.requireNonNull(notificationEntry);
        return Boolean.valueOf(!Intrinsics.areEqual(str, notificationEntry.mKey));
    }
}
