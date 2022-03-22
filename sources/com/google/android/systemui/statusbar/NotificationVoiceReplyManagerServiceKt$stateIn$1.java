package com.google.android.systemui.statusbar;

import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
import kotlinx.coroutines.flow.MutableStateFlow;
/* compiled from: NotificationVoiceReplyManagerService.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerServiceKt$stateIn$1", f = "NotificationVoiceReplyManagerService.kt", l = {274}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class NotificationVoiceReplyManagerServiceKt$stateIn$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ MutableStateFlow<Object> $stateFlow;
    public final /* synthetic */ Flow<Object> $this_stateIn;
    public int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerServiceKt$stateIn$1(Flow<Object> flow, MutableStateFlow<Object> mutableStateFlow, Continuation<? super NotificationVoiceReplyManagerServiceKt$stateIn$1> continuation) {
        super(2, continuation);
        this.$this_stateIn = flow;
        this.$stateFlow = mutableStateFlow;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new NotificationVoiceReplyManagerServiceKt$stateIn$1(this.$this_stateIn, this.$stateFlow, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyManagerServiceKt$stateIn$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            Flow<Object> flow = this.$this_stateIn;
            final MutableStateFlow<Object> mutableStateFlow = this.$stateFlow;
            FlowCollector<? super Object> notificationVoiceReplyManagerServiceKt$stateIn$1$invokeSuspend$$inlined$collect$1 = new FlowCollector<Object>() { // from class: com.google.android.systemui.statusbar.NotificationVoiceReplyManagerServiceKt$stateIn$1$invokeSuspend$$inlined$collect$1
                @Override // kotlinx.coroutines.flow.FlowCollector
                public final Object emit(Object obj2, Continuation<? super Unit> continuation) {
                    MutableStateFlow.this.setValue(obj2);
                    return Unit.INSTANCE;
                }
            };
            this.label = 1;
            if (flow.collect(notificationVoiceReplyManagerServiceKt$stateIn$1$invokeSuspend$$inlined$collect$1, this) == coroutineSingletons) {
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
