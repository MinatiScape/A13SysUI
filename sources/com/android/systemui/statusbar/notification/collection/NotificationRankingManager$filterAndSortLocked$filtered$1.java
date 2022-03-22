package com.android.systemui.statusbar.notification.collection;

import java.util.Objects;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
/* compiled from: NotificationRankingManager.kt */
/* loaded from: classes.dex */
public /* synthetic */ class NotificationRankingManager$filterAndSortLocked$filtered$1 extends FunctionReferenceImpl implements Function1<NotificationEntry, Boolean> {
    public NotificationRankingManager$filterAndSortLocked$filtered$1(Object obj) {
        super(1, obj, NotificationRankingManager.class, "filter", "filter(Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;)Z", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Boolean invoke(NotificationEntry notificationEntry) {
        NotificationEntry notificationEntry2 = notificationEntry;
        NotificationRankingManager notificationRankingManager = (NotificationRankingManager) this.receiver;
        Objects.requireNonNull(notificationRankingManager);
        boolean shouldFilterOut = notificationRankingManager.notifFilter.shouldFilterOut(notificationEntry2);
        if (shouldFilterOut) {
            notificationEntry2.initializationTime = -1L;
        }
        return Boolean.valueOf(shouldFilterOut);
    }
}
