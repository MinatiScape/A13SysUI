package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.notification.people.Subscription;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import java.util.Objects;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes.dex */
public final class NotificationVoiceReplyController$connect$1$registerHandler$1 implements VoiceReplySubscription, Subscription {
    public final /* synthetic */ Subscription $$delegate_0;
    public final /* synthetic */ NotificationVoiceReplyController.Connection $connection;
    public final /* synthetic */ NotificationVoiceReplyHandler $handler;
    public final /* synthetic */ Subscription $sub;
    public final /* synthetic */ NotificationVoiceReplyController$connect$1 this$0;
    public final /* synthetic */ NotificationVoiceReplyController this$1;

    @Override // com.android.systemui.statusbar.notification.people.Subscription
    public final void unsubscribe() {
        this.$$delegate_0.unsubscribe();
    }

    public NotificationVoiceReplyController$connect$1$registerHandler$1(SafeSubscription safeSubscription, NotificationVoiceReplyController$connect$1 notificationVoiceReplyController$connect$1, NotificationVoiceReplyController notificationVoiceReplyController, NotificationVoiceReplyController.Connection connection, NotificationVoiceReplyHandler notificationVoiceReplyHandler) {
        this.$sub = safeSubscription;
        this.this$0 = notificationVoiceReplyController$connect$1;
        this.this$1 = notificationVoiceReplyController;
        this.$connection = connection;
        this.$handler = notificationVoiceReplyHandler;
        this.$$delegate_0 = safeSubscription;
    }

    @Override // com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplySubscription
    public final Object startVoiceReply(int i, String str, Function0<Unit> function0, Function2<? super Session, ? super Continuation<? super Unit>, ? extends Object> function2, Continuation<? super Unit> continuation) {
        this.this$0.ensureConnected();
        NotificationVoiceReplyController notificationVoiceReplyController = this.this$1;
        NotificationVoiceReplyController.Connection connection = this.$connection;
        NotificationVoiceReplyHandler notificationVoiceReplyHandler = this.$handler;
        Objects.requireNonNull(notificationVoiceReplyController);
        Objects.requireNonNull(connection);
        Object send = connection.startSessionRequests.send(new StartSessionRequest(notificationVoiceReplyHandler, i, str, function0, function2), continuation);
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        if (send != coroutineSingletons) {
            send = Unit.INSTANCE;
        }
        if (send == coroutineSingletons) {
            return send;
        }
        return Unit.INSTANCE;
    }
}
