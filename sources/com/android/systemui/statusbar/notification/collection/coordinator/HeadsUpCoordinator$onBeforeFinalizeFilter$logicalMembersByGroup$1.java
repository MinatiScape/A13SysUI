package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: HeadsUpCoordinator.kt */
/* loaded from: classes.dex */
final class HeadsUpCoordinator$onBeforeFinalizeFilter$logicalMembersByGroup$1 extends Lambda implements Function1<NotificationEntry, Boolean> {
    public final /* synthetic */ Map<String, List<HeadsUpCoordinator.PostedEntry>> $postedEntriesByGroup;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public HeadsUpCoordinator$onBeforeFinalizeFilter$logicalMembersByGroup$1(LinkedHashMap linkedHashMap) {
        super(1);
        this.$postedEntriesByGroup = linkedHashMap;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Boolean invoke(NotificationEntry notificationEntry) {
        return Boolean.valueOf(this.$postedEntriesByGroup.containsKey(notificationEntry.mSbn.getGroupKey()));
    }
}
