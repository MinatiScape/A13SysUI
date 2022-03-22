package com.google.android.systemui.statusbar;

import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
/* compiled from: NotificationVoiceReplyManagerService.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerServiceKt$startingWith$1$1", f = "NotificationVoiceReplyManagerService.kt", l = {210, 274}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class NotificationVoiceReplyManagerServiceKt$startingWith$1$1 extends SuspendLambda implements Function2<FlowCollector<Object>, Continuation<? super Unit>, Object> {
    public final /* synthetic */ Flow<Object> $this_run;
    public final /* synthetic */ Object $value;
    private /* synthetic */ Object L$0;
    public int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerServiceKt$startingWith$1$1(Object obj, Flow<Object> flow, Continuation<? super NotificationVoiceReplyManagerServiceKt$startingWith$1$1> continuation) {
        super(2, continuation);
        this.$value = obj;
        this.$this_run = flow;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyManagerServiceKt$startingWith$1$1 notificationVoiceReplyManagerServiceKt$startingWith$1$1 = new NotificationVoiceReplyManagerServiceKt$startingWith$1$1(this.$value, this.$this_run, continuation);
        notificationVoiceReplyManagerServiceKt$startingWith$1$1.L$0 = obj;
        return notificationVoiceReplyManagerServiceKt$startingWith$1$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(FlowCollector<Object> flowCollector, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyManagerServiceKt$startingWith$1$1) create(flowCollector, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        final FlowCollector flowCollector;
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            flowCollector = (FlowCollector) this.L$0;
            Object obj2 = this.$value;
            this.L$0 = flowCollector;
            this.label = 1;
            if (flowCollector.emit(obj2, this) == coroutineSingletons) {
                return coroutineSingletons;
            }
        } else if (i == 1) {
            flowCollector = (FlowCollector) this.L$0;
            ResultKt.throwOnFailure(obj);
        } else if (i == 2) {
            ResultKt.throwOnFailure(obj);
            return Unit.INSTANCE;
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        Flow<Object> flow = this.$this_run;
        FlowCollector<? super Object> notificationVoiceReplyManagerServiceKt$startingWith$1$1$invokeSuspend$$inlined$collect$1 = new FlowCollector<Object>() { // from class: com.google.android.systemui.statusbar.NotificationVoiceReplyManagerServiceKt$startingWith$1$1$invokeSuspend$$inlined$collect$1
            @Override // kotlinx.coroutines.flow.FlowCollector
            public final Object emit(Object obj3, Continuation<? super Unit> continuation) {
                Object emit = FlowCollector.this.emit(obj3, continuation);
                if (emit == CoroutineSingletons.COROUTINE_SUSPENDED) {
                    return emit;
                }
                return Unit.INSTANCE;
            }
        };
        this.L$0 = null;
        this.label = 2;
        if (flow.collect(notificationVoiceReplyManagerServiceKt$startingWith$1$1$invokeSuspend$$inlined$collect$1, this) == coroutineSingletons) {
            return coroutineSingletons;
        }
        return Unit.INSTANCE;
    }
}
