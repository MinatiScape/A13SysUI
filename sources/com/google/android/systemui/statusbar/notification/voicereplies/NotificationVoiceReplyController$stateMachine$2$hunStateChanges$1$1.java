package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.channels.Channel;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hunStateChanges$1$1", f = "NotificationVoiceReplyManager.kt", l = {441}, m = "invokeSuspend")
/* loaded from: classes.dex */
public final class NotificationVoiceReplyController$stateMachine$2$hunStateChanges$1$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ Channel<Pair<NotificationEntry, Boolean>> $chan;
    public int label;
    public final /* synthetic */ NotificationVoiceReplyController this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$stateMachine$2$hunStateChanges$1$1(NotificationVoiceReplyController notificationVoiceReplyController, Channel<Pair<NotificationEntry, Boolean>> channel, Continuation<? super NotificationVoiceReplyController$stateMachine$2$hunStateChanges$1$1> continuation) {
        super(2, continuation);
        this.this$0 = notificationVoiceReplyController;
        this.$chan = channel;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new NotificationVoiceReplyController$stateMachine$2$hunStateChanges$1$1(this.this$0, this.$chan, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyController$stateMachine$2$hunStateChanges$1$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            HeadsUpManager headsUpManager = this.this$0.headsUpManager;
            Channel<Pair<NotificationEntry, Boolean>> channel = this.$chan;
            this.label = 1;
            NotificationVoiceReplyManagerKt.access$sendHunStateChanges(headsUpManager, channel, this);
            return coroutineSingletons;
        } else if (i == 1) {
            ResultKt.throwOnFailure(obj);
            return Unit.INSTANCE;
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
