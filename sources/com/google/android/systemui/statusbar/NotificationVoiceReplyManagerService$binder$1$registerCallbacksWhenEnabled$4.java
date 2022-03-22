package com.google.android.systemui.statusbar;

import androidx.mediarouter.R$dimen;
import com.google.android.systemui.statusbar.notification.voicereplies.CtaState;
import java.util.Objects;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
/* compiled from: NotificationVoiceReplyManagerService.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$4", f = "NotificationVoiceReplyManagerService.kt", l = {127}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$4 extends SuspendLambda implements Function2<Pair<? extends Integer, ? extends Integer>, Continuation<? super Unit>, Object> {
    public final /* synthetic */ INotificationVoiceReplyServiceCallbacks $callbacks;
    public /* synthetic */ Object L$0;
    public int label;
    public final /* synthetic */ NotificationVoiceReplyManagerService$binder$1 this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$4(NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1, INotificationVoiceReplyServiceCallbacks iNotificationVoiceReplyServiceCallbacks, Continuation<? super NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$4> continuation) {
        super(2, continuation);
        this.this$0 = notificationVoiceReplyManagerService$binder$1;
        this.$callbacks = iNotificationVoiceReplyServiceCallbacks;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$4 notificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$4 = new NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$4(this.this$0, this.$callbacks, continuation);
        notificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$4.L$0 = obj;
        return notificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$4;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Pair<? extends Integer, ? extends Integer> pair, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$4) create(pair, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        CtaState ctaState;
        Object obj2 = CoroutineSingletons.COROUTINE_SUSPENDED;
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            int intValue = ((Number) ((Pair) this.L$0).component2()).intValue();
            if (intValue != 0) {
                byte[][] bArr = NotificationVoiceReplyManagerServiceKt.AGSA_CERTS;
                if (intValue == 2) {
                    ctaState = CtaState.HOTWORD;
                } else if (intValue != 3) {
                    ctaState = CtaState.NONE;
                } else {
                    ctaState = CtaState.QUICK_PHRASE;
                }
                NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1 = this.this$0;
                INotificationVoiceReplyServiceCallbacks iNotificationVoiceReplyServiceCallbacks = this.$callbacks;
                this.label = 1;
                int i2 = NotificationVoiceReplyManagerService$binder$1.$r8$clinit;
                Objects.requireNonNull(notificationVoiceReplyManagerService$binder$1);
                Object coroutineScope = R$dimen.coroutineScope(new NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2(notificationVoiceReplyManagerService$binder$1, ctaState, iNotificationVoiceReplyServiceCallbacks, notificationVoiceReplyManagerService$binder$1.this$0, null), this);
                if (coroutineScope != obj2) {
                    coroutineScope = Unit.INSTANCE;
                }
                if (coroutineScope == obj2) {
                    return obj2;
                }
            }
        } else if (i == 1) {
            ResultKt.throwOnFailure(obj);
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return Unit.INSTANCE;
    }
}
