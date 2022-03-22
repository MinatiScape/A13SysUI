package com.google.android.systemui.statusbar;

import com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplySubscription;
import java.util.Objects;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
/* compiled from: NotificationVoiceReplyManagerService.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$2$1", f = "NotificationVoiceReplyManagerService.kt", l = {148}, m = "invokeSuspend")
/* loaded from: classes.dex */
public final class NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$2$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ String $content;
    public final /* synthetic */ CallbacksHandler $handler;
    public final /* synthetic */ VoiceReplySubscription $registration;
    public final /* synthetic */ int $token;
    public int label;
    public final /* synthetic */ NotificationVoiceReplyManagerService$binder$1 this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$2$1(NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1, VoiceReplySubscription voiceReplySubscription, CallbacksHandler callbacksHandler, int i, String str, Continuation<? super NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$2$1> continuation) {
        super(2, continuation);
        this.this$0 = notificationVoiceReplyManagerService$binder$1;
        this.$registration = voiceReplySubscription;
        this.$handler = callbacksHandler;
        this.$token = i;
        this.$content = str;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$2$1(this.this$0, this.$registration, this.$handler, this.$token, this.$content, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$2$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1 = this.this$0;
            VoiceReplySubscription voiceReplySubscription = this.$registration;
            CallbacksHandler callbacksHandler = this.$handler;
            int i2 = this.$token;
            String str = this.$content;
            this.label = 1;
            int i3 = NotificationVoiceReplyManagerService$binder$1.$r8$clinit;
            Objects.requireNonNull(notificationVoiceReplyManagerService$binder$1);
            Object startVoiceReply = voiceReplySubscription.startVoiceReply(i2, str, new NotificationVoiceReplyManagerService$binder$1$startVoiceReply$3(callbacksHandler, i2), new NotificationVoiceReplyManagerService$binder$1$startVoiceReply$4(callbacksHandler, i2, notificationVoiceReplyManagerService$binder$1, null), this);
            if (startVoiceReply != coroutineSingletons) {
                startVoiceReply = Unit.INSTANCE;
            }
            if (startVoiceReply == coroutineSingletons) {
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
