package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
final class ConversationNotificationManager$updateNotificationRanking$activeConversationEntries$1 extends Lambda implements Function1<String, NotificationEntry> {
    public final /* synthetic */ ConversationNotificationManager this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ConversationNotificationManager$updateNotificationRanking$activeConversationEntries$1(ConversationNotificationManager conversationNotificationManager) {
        super(1);
        this.this$0 = conversationNotificationManager;
    }

    @Override // kotlin.jvm.functions.Function1
    public final NotificationEntry invoke(String str) {
        return this.this$0.notifCollection.getEntry(str);
    }
}
