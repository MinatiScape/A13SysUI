package com.android.systemui.statusbar.notification;

import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.android.internal.widget.MessagingImageMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
final class AnimatedImageNotificationManager$updateAnimatedImageDrawables$5 extends Lambda implements Function1<View, AnimatedImageDrawable> {
    public static final AnimatedImageNotificationManager$updateAnimatedImageDrawables$5 INSTANCE = new AnimatedImageNotificationManager$updateAnimatedImageDrawables$5();

    public AnimatedImageNotificationManager$updateAnimatedImageDrawables$5() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final AnimatedImageDrawable invoke(View view) {
        MessagingImageMessage messagingImageMessage;
        View view2 = view;
        if (view2 instanceof MessagingImageMessage) {
            messagingImageMessage = (MessagingImageMessage) view2;
        } else {
            messagingImageMessage = null;
        }
        if (messagingImageMessage == null) {
            return null;
        }
        Drawable drawable = messagingImageMessage.getDrawable();
        if (drawable instanceof AnimatedImageDrawable) {
            return (AnimatedImageDrawable) drawable;
        }
        return null;
    }
}
