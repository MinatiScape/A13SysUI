package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import java.util.Objects;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes.dex */
public final class NotificationVoiceReplyController$queryInitialState$1 extends Lambda implements Function1<NotificationEntry, VoiceReplyTarget> {
    public final /* synthetic */ NotificationVoiceReplyController.Connection $this_queryInitialState;
    public final /* synthetic */ NotificationVoiceReplyController this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$queryInitialState$1(NotificationVoiceReplyController notificationVoiceReplyController, NotificationVoiceReplyController.Connection connection) {
        super(1);
        this.this$0 = notificationVoiceReplyController;
        this.$this_queryInitialState = connection;
    }

    @Override // kotlin.jvm.functions.Function1
    public final VoiceReplyTarget invoke(NotificationEntry notificationEntry) {
        NotificationVoiceReplyController notificationVoiceReplyController = this.this$0;
        NotificationVoiceReplyController.Connection connection = this.$this_queryInitialState;
        return Objects.requireNonNull(notificationEntry);
    }
}
