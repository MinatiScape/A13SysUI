package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: SensitiveContentCoordinator.kt */
/* loaded from: classes.dex */
public final class SensitiveContentCoordinatorImpl$onBeforeRenderList$1 extends Lambda implements Function1<NotificationEntry, Boolean> {
    public static final SensitiveContentCoordinatorImpl$onBeforeRenderList$1 INSTANCE = new SensitiveContentCoordinatorImpl$onBeforeRenderList$1();

    public SensitiveContentCoordinatorImpl$onBeforeRenderList$1() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Boolean invoke(NotificationEntry notificationEntry) {
        return Boolean.valueOf(notificationEntry.rowExists());
    }
}
