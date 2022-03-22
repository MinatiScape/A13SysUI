package com.android.systemui.statusbar.notification;

import android.view.View;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequencesKt;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
final /* synthetic */ class ConversationNotificationManager$updateNotificationRanking$1 extends FunctionReferenceImpl implements Function1<NotificationContentView, Sequence<? extends View>> {
    public static final ConversationNotificationManager$updateNotificationRanking$1 INSTANCE = new ConversationNotificationManager$updateNotificationRanking$1();

    public ConversationNotificationManager$updateNotificationRanking$1() {
        super(1, Intrinsics.Kotlin.class, "getLayouts", "updateNotificationRanking$getLayouts(Lcom/android/systemui/statusbar/notification/row/NotificationContentView;)Lkotlin/sequences/Sequence;", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Sequence<? extends View> invoke(NotificationContentView notificationContentView) {
        NotificationContentView notificationContentView2 = notificationContentView;
        return SequencesKt__SequencesKt.sequenceOf(notificationContentView2.mContractedChild, notificationContentView2.mExpandedChild, notificationContentView2.mHeadsUpChild);
    }
}
