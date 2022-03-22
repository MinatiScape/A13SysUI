package com.google.android.systemui.statusbar.notification.voicereplies;

import androidx.mediarouter.R$dimen;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.channels.Channel;
import kotlinx.coroutines.flow.Flow;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$newCandidateEvents$1$1", f = "NotificationVoiceReplyManager.kt", l = {557}, m = "invokeSuspend")
/* loaded from: classes.dex */
public final class NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$newCandidateEvents$1$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ Channel<NotificationEntry> $chan;
    public final /* synthetic */ Flow<NotificationEntry> $huns;
    public final /* synthetic */ Flow<NotificationEntry> $reinflations;
    public int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$newCandidateEvents$1$1(Channel<NotificationEntry> channel, Flow<NotificationEntry> flow, Flow<NotificationEntry> flow2, Continuation<? super NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$newCandidateEvents$1$1> continuation) {
        super(2, continuation);
        this.$chan = channel;
        this.$reinflations = flow;
        this.$huns = flow2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$newCandidateEvents$1$1(this.$chan, this.$reinflations, this.$huns, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$newCandidateEvents$1$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object obj2 = CoroutineSingletons.COROUTINE_SUSPENDED;
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            Channel<NotificationEntry> channel = this.$chan;
            Flow[] flowArr = {this.$reinflations, this.$huns};
            this.label = 1;
            Object coroutineScope = R$dimen.coroutineScope(new NotificationVoiceReplyManagerKt$merge$2(flowArr, channel, null), this);
            if (coroutineScope != obj2) {
                coroutineScope = Unit.INSTANCE;
            }
            if (coroutineScope == obj2) {
                return obj2;
            }
        } else if (i == 1) {
            ResultKt.throwOnFailure(obj);
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return Unit.INSTANCE;
    }
}
