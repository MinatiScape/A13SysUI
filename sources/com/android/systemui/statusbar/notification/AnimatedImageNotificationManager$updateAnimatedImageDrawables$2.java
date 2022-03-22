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
final class AnimatedImageNotificationManager$updateAnimatedImageDrawables$2 extends Lambda implements Function1<NotificationContentView, Sequence<? extends View>> {
    public static final AnimatedImageNotificationManager$updateAnimatedImageDrawables$2 INSTANCE = new AnimatedImageNotificationManager$updateAnimatedImageDrawables$2();

    public AnimatedImageNotificationManager$updateAnimatedImageDrawables$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Sequence<? extends View> invoke(NotificationContentView notificationContentView) {
        NotificationContentView notificationContentView2 = notificationContentView;
        Objects.requireNonNull(notificationContentView2);
        return ArraysKt___ArraysKt.asSequence(new View[]{notificationContentView2.mContractedChild, notificationContentView2.mHeadsUpChild, notificationContentView2.mExpandedChild, notificationContentView2.mSingleLineView});
    }
}
