package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.ConversationNotificationManager;
import java.util.function.BiFunction;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
public final class ConversationNotificationManager$resetCount$1<T, U, R> implements BiFunction {
    public static final ConversationNotificationManager$resetCount$1<T, U, R> INSTANCE = new ConversationNotificationManager$resetCount$1<>();

    @Override // java.util.function.BiFunction
    public final Object apply(Object obj, Object obj2) {
        String str = (String) obj;
        ConversationNotificationManager.ConversationState conversationState = (ConversationNotificationManager.ConversationState) obj2;
        if (conversationState == null) {
            return null;
        }
        return new ConversationNotificationManager.ConversationState(0, conversationState.notification);
    }
}
