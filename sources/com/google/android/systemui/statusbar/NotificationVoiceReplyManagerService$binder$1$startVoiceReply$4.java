package com.google.android.systemui.statusbar;

import com.google.android.systemui.statusbar.notification.voicereplies.Session;
import java.util.Objects;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.flow.FlowCollector;
import kotlinx.coroutines.flow.SharedFlowImpl;
/* compiled from: NotificationVoiceReplyManagerService.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$startVoiceReply$4", f = "NotificationVoiceReplyManagerService.kt", l = {279}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class NotificationVoiceReplyManagerService$binder$1$startVoiceReply$4 extends SuspendLambda implements Function2<Session, Continuation<? super Unit>, Object> {
    public final /* synthetic */ CallbacksHandler $handler;
    public final /* synthetic */ int $token;
    private /* synthetic */ Object L$0;
    public int label;
    public final /* synthetic */ NotificationVoiceReplyManagerService$binder$1 this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerService$binder$1$startVoiceReply$4(CallbacksHandler callbacksHandler, int i, NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1, Continuation<? super NotificationVoiceReplyManagerService$binder$1$startVoiceReply$4> continuation) {
        super(2, continuation);
        this.$handler = callbacksHandler;
        this.$token = i;
        this.this$0 = notificationVoiceReplyManagerService$binder$1;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyManagerService$binder$1$startVoiceReply$4 notificationVoiceReplyManagerService$binder$1$startVoiceReply$4 = new NotificationVoiceReplyManagerService$binder$1$startVoiceReply$4(this.$handler, this.$token, this.this$0, continuation);
        notificationVoiceReplyManagerService$binder$1$startVoiceReply$4.L$0 = obj;
        return notificationVoiceReplyManagerService$binder$1$startVoiceReply$4;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Session session, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyManagerService$binder$1$startVoiceReply$4) create(session, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            final Session session = (Session) this.L$0;
            CallbacksHandler callbacksHandler = this.$handler;
            int i2 = this.$token;
            Objects.requireNonNull(callbacksHandler);
            callbacksHandler.callbacks.onVoiceReplyHandled(i2, 0);
            NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1 = this.this$0;
            Objects.requireNonNull(notificationVoiceReplyManagerService$binder$1);
            SharedFlowImpl sharedFlowImpl = notificationVoiceReplyManagerService$binder$1.onVoiceAuthStateChangedFlow;
            NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$12 = this.this$0;
            int i3 = this.$token;
            FlowCollector<OnVoiceAuthStateChangedData> notificationVoiceReplyManagerService$binder$1$startVoiceReply$4$invokeSuspend$$inlined$collect$1 = new FlowCollector<OnVoiceAuthStateChangedData>() { // from class: com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$startVoiceReply$4$invokeSuspend$$inlined$collect$1
                @Override // kotlinx.coroutines.flow.FlowCollector
                public final Object emit(OnVoiceAuthStateChangedData onVoiceAuthStateChangedData, Continuation<? super Unit> continuation) {
                    OnVoiceAuthStateChangedData onVoiceAuthStateChangedData2 = onVoiceAuthStateChangedData;
                    Objects.requireNonNull(onVoiceAuthStateChangedData2);
                    Session.this.setVoiceAuthState(onVoiceAuthStateChangedData2.newState);
                    return Unit.INSTANCE;
                }
            };
            this.label = 1;
            sharedFlowImpl.collect(new NotificationVoiceReplyManagerService$binder$1$startVoiceReply$4$invokeSuspend$$inlined$filter$1$2(notificationVoiceReplyManagerService$binder$1$startVoiceReply$4$invokeSuspend$$inlined$collect$1, notificationVoiceReplyManagerService$binder$12, i3), this);
            return coroutineSingletons;
        } else if (i == 1) {
            ResultKt.throwOnFailure(obj);
            return Unit.INSTANCE;
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
