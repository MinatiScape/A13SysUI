package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function3;
import kotlinx.coroutines.flow.FlowCollector;
/* compiled from: Zip.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$1$invokeSuspend$$inlined$combine$1$3", f = "NotificationVoiceReplyManager.kt", l = {333}, m = "invokeSuspend")
/* loaded from: classes.dex */
public final class NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$1$invokeSuspend$$inlined$combine$1$3 extends SuspendLambda implements Function3<FlowCollector<? super CtaState>, CtaState[], Continuation<? super Unit>, Object> {
    private /* synthetic */ Object L$0;
    public /* synthetic */ Object L$1;
    public int label;

    public NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$1$invokeSuspend$$inlined$combine$1$3(Continuation continuation) {
        super(3, continuation);
    }

    @Override // kotlin.jvm.functions.Function3
    public final Object invoke(FlowCollector<? super CtaState> flowCollector, CtaState[] ctaStateArr, Continuation<? super Unit> continuation) {
        NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$1$invokeSuspend$$inlined$combine$1$3 notificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$1$invokeSuspend$$inlined$combine$1$3 = new NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$1$invokeSuspend$$inlined$combine$1$3(continuation);
        notificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$1$invokeSuspend$$inlined$combine$1$3.L$0 = flowCollector;
        notificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$1$invokeSuspend$$inlined$combine$1$3.L$1 = ctaStateArr;
        return notificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$1$invokeSuspend$$inlined$combine$1$3.invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            FlowCollector flowCollector = (FlowCollector) this.L$0;
            CtaState ctaState = (CtaState) ArraysKt___ArraysKt.maxOrNull((CtaState[]) ((Object[]) this.L$1));
            if (ctaState == null) {
                ctaState = CtaState.NONE;
            }
            this.label = 1;
            if (flowCollector.emit(ctaState, this) == coroutineSingletons) {
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
