package com.android.systemui.statusbar.notification.people;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: PeopleNotificationIdentifier.kt */
/* loaded from: classes.dex */
public final class PeopleNotificationIdentifierImpl$getPeopleTypeOfSummary$childTypes$1 extends Lambda implements Function1<NotificationEntry, Integer> {
    public final /* synthetic */ PeopleNotificationIdentifierImpl this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public PeopleNotificationIdentifierImpl$getPeopleTypeOfSummary$childTypes$1(PeopleNotificationIdentifierImpl peopleNotificationIdentifierImpl) {
        super(1);
        this.this$0 = peopleNotificationIdentifierImpl;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Integer invoke(NotificationEntry notificationEntry) {
        return Integer.valueOf(this.this$0.getPeopleNotificationType(notificationEntry));
    }
}
