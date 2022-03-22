package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.policy.RemoteInputViewController;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.flow.Flow;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2", f = "NotificationVoiceReplyManager.kt", l = {1169}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ Flow<Pair<String, RemoteInputViewController>> $remoteInputViewActivatedForVoiceReply;
    public final /* synthetic */ NotificationVoiceReplyController.Connection $this_logUiEventsForActivatedVoiceReplyInputs;
    private /* synthetic */ Object L$0;
    public int label;
    public final /* synthetic */ NotificationVoiceReplyController this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2(Flow<? extends Pair<String, ? extends RemoteInputViewController>> flow, NotificationVoiceReplyController.Connection connection, NotificationVoiceReplyController notificationVoiceReplyController, Continuation<? super NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2> continuation) {
        super(2, continuation);
        this.$remoteInputViewActivatedForVoiceReply = flow;
        this.$this_logUiEventsForActivatedVoiceReplyInputs = connection;
        this.this$0 = notificationVoiceReplyController;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2 notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2 = new NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2(this.$remoteInputViewActivatedForVoiceReply, this.$this_logUiEventsForActivatedVoiceReplyInputs, this.this$0, continuation);
        notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2.L$0 = obj;
        return notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            Flow<Pair<String, RemoteInputViewController>> flow = this.$remoteInputViewActivatedForVoiceReply;
            NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$invokeSuspend$$inlined$collect$1 notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$invokeSuspend$$inlined$collect$1 = new NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$invokeSuspend$$inlined$collect$1((CoroutineScope) this.L$0, this.$this_logUiEventsForActivatedVoiceReplyInputs, this.this$0, flow);
            this.label = 1;
            if (flow.collect(notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$invokeSuspend$$inlined$collect$1, this) == coroutineSingletons) {
                return coroutineSingletons;
            }
        } else if (i == 1) {
            ResultKt.throwOnFailure(obj);
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return Unit.INSTANCE;
    }
}
