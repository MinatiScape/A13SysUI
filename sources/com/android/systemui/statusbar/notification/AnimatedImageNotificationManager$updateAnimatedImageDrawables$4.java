package com.android.systemui.statusbar.notification;

import android.view.View;
import com.android.internal.widget.MessagingGroup;
import com.android.systemui.util.ConvenienceExtensionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import kotlin.sequences.Sequence;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
final class AnimatedImageNotificationManager$updateAnimatedImageDrawables$4 extends Lambda implements Function1<MessagingGroup, Sequence<? extends View>> {
    public static final AnimatedImageNotificationManager$updateAnimatedImageDrawables$4 INSTANCE = new AnimatedImageNotificationManager$updateAnimatedImageDrawables$4();

    public AnimatedImageNotificationManager$updateAnimatedImageDrawables$4() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Sequence<? extends View> invoke(MessagingGroup messagingGroup) {
        return ConvenienceExtensionsKt.getChildren(messagingGroup.getMessageContainer());
    }
}
