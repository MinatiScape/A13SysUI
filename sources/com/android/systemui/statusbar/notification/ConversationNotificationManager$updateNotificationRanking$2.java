package com.android.systemui.statusbar.notification;

import android.view.View;
import com.android.internal.widget.ConversationLayout;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
final class ConversationNotificationManager$updateNotificationRanking$2 extends Lambda implements Function1<View, ConversationLayout> {
    public static final ConversationNotificationManager$updateNotificationRanking$2 INSTANCE = new ConversationNotificationManager$updateNotificationRanking$2();

    public ConversationNotificationManager$updateNotificationRanking$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final ConversationLayout invoke(View view) {
        View view2 = view;
        if (view2 instanceof ConversationLayout) {
            return (ConversationLayout) view2;
        }
        return null;
    }
}
