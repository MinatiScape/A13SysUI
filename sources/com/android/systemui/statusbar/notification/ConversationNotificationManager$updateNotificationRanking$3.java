package com.android.systemui.statusbar.notification;

import com.android.internal.widget.ConversationLayout;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
final class ConversationNotificationManager$updateNotificationRanking$3 extends Lambda implements Function1<ConversationLayout, Boolean> {
    public final /* synthetic */ boolean $important;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ConversationNotificationManager$updateNotificationRanking$3(boolean z) {
        super(1);
        this.$important = z;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Boolean invoke(ConversationLayout conversationLayout) {
        boolean z;
        if (conversationLayout.isImportantConversation() == this.$important) {
            z = true;
        } else {
            z = false;
        }
        return Boolean.valueOf(z);
    }
}
