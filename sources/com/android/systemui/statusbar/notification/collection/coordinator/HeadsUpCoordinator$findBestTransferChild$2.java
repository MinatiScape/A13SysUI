package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: HeadsUpCoordinator.kt */
/* loaded from: classes.dex */
final class HeadsUpCoordinator$findBestTransferChild$2 extends Lambda implements Function1<NotificationEntry, Boolean> {
    public final /* synthetic */ Function1<String, GroupLocation> $locationLookupByKey;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public HeadsUpCoordinator$findBestTransferChild$2(Function1<? super String, ? extends GroupLocation> function1) {
        super(1);
        this.$locationLookupByKey = function1;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Boolean invoke(NotificationEntry notificationEntry) {
        boolean z;
        if (this.$locationLookupByKey.invoke(notificationEntry.mKey) != GroupLocation.Detached) {
            z = true;
        } else {
            z = false;
        }
        return Boolean.valueOf(z);
    }
}
