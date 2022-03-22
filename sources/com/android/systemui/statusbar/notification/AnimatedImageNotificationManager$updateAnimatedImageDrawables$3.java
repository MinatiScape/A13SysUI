package com.android.systemui.statusbar.notification;

import android.view.View;
import com.android.internal.widget.ConversationLayout;
import com.android.internal.widget.MessagingGroup;
import com.android.internal.widget.MessagingLayout;
import java.util.ArrayList;
import kotlin.collections.CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import kotlin.sequences.EmptySequence;
import kotlin.sequences.Sequence;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
final class AnimatedImageNotificationManager$updateAnimatedImageDrawables$3 extends Lambda implements Function1<View, Sequence<? extends MessagingGroup>> {
    public static final AnimatedImageNotificationManager$updateAnimatedImageDrawables$3 INSTANCE = new AnimatedImageNotificationManager$updateAnimatedImageDrawables$3();

    public AnimatedImageNotificationManager$updateAnimatedImageDrawables$3() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Sequence<? extends MessagingGroup> invoke(View view) {
        ConversationLayout conversationLayout;
        CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1 collectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1;
        MessagingLayout messagingLayout;
        ArrayList messagingGroups;
        ArrayList messagingGroups2;
        View view2 = view;
        CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1 collectionsKt___CollectionsKt$asSequence$$inlined$Sequence$12 = null;
        if (view2 instanceof ConversationLayout) {
            conversationLayout = (ConversationLayout) view2;
        } else {
            conversationLayout = null;
        }
        if (conversationLayout == null || (messagingGroups2 = conversationLayout.getMessagingGroups()) == null) {
            collectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1 = null;
        } else {
            collectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1 = new CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1(messagingGroups2);
        }
        if (collectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1 != null) {
            return collectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1;
        }
        if (view2 instanceof MessagingLayout) {
            messagingLayout = (MessagingLayout) view2;
        } else {
            messagingLayout = null;
        }
        if (!(messagingLayout == null || (messagingGroups = messagingLayout.getMessagingGroups()) == null)) {
            collectionsKt___CollectionsKt$asSequence$$inlined$Sequence$12 = new CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1(messagingGroups);
        }
        if (collectionsKt___CollectionsKt$asSequence$$inlined$Sequence$12 == null) {
            return EmptySequence.INSTANCE;
        }
        return collectionsKt___CollectionsKt$asSequence$$inlined$Sequence$12;
    }
}
