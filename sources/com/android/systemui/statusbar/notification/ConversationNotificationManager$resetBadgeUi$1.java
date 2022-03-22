package com.android.systemui.statusbar.notification;

import android.view.View;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import java.util.Objects;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import kotlin.sequences.Sequence;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
public final class ConversationNotificationManager$resetBadgeUi$1 extends Lambda implements Function1<NotificationContentView, Sequence<? extends View>> {
    public static final ConversationNotificationManager$resetBadgeUi$1 INSTANCE = new ConversationNotificationManager$resetBadgeUi$1();

    public ConversationNotificationManager$resetBadgeUi$1() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Sequence<? extends View> invoke(NotificationContentView notificationContentView) {
        NotificationContentView notificationContentView2 = notificationContentView;
        Objects.requireNonNull(notificationContentView2);
        return ArraysKt___ArraysKt.asSequence(new View[]{notificationContentView2.mContractedChild, notificationContentView2.mHeadsUpChild, notificationContentView2.mExpandedChild, notificationContentView2.mSingleLineView});
    }
}
