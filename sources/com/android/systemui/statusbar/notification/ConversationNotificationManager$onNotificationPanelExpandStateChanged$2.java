package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
public final class ConversationNotificationManager$onNotificationPanelExpandStateChanged$2 extends Lambda implements Function1<NotificationEntry, ExpandableNotificationRow> {
    public static final ConversationNotificationManager$onNotificationPanelExpandStateChanged$2 INSTANCE = new ConversationNotificationManager$onNotificationPanelExpandStateChanged$2();

    public ConversationNotificationManager$onNotificationPanelExpandStateChanged$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final ExpandableNotificationRow invoke(NotificationEntry notificationEntry) {
        return notificationEntry.row;
    }
}
