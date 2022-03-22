package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.ConversationNotificationManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import java.util.Map;
import kotlin.Pair;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
public final class ConversationNotificationManager$onNotificationPanelExpandStateChanged$expanded$1 extends Lambda implements Function1<Map.Entry<? extends String, ? extends ConversationNotificationManager.ConversationState>, Pair<? extends String, ? extends NotificationEntry>> {
    public final /* synthetic */ ConversationNotificationManager this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ConversationNotificationManager$onNotificationPanelExpandStateChanged$expanded$1(ConversationNotificationManager conversationNotificationManager) {
        super(1);
        this.this$0 = conversationNotificationManager;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Pair<? extends String, ? extends NotificationEntry> invoke(Map.Entry<? extends String, ? extends ConversationNotificationManager.ConversationState> entry) {
        String str = (String) entry.getKey();
        NotificationEntry entry2 = this.this$0.notifCollection.getEntry(str);
        if (entry2 == null) {
            return null;
        }
        ExpandableNotificationRow expandableNotificationRow = entry2.row;
        boolean z = true;
        if (expandableNotificationRow == null || !expandableNotificationRow.isExpanded(false)) {
            z = false;
        }
        if (z) {
            return new Pair<>(str, entry2);
        }
        return null;
    }
}
