package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Ref$ObjectRef;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$collectLatest$2", f = "NotificationVoiceReplyManager.kt", l = {1169}, m = "invokeSuspend")
/* loaded from: classes.dex */
public final class NotificationVoiceReplyManagerKt$collectLatest$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ Function2<T, Continuation<? super Unit>, Object> $collector;
    public final /* synthetic */ Flow<T> $this_collectLatest;
    private /* synthetic */ Object L$0;
    public int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public NotificationVoiceReplyManagerKt$collectLatest$2(Flow<? extends T> flow, Function2<? super T, ? super Continuation<? super Unit>, ? extends Object> function2, Continuation<? super NotificationVoiceReplyManagerKt$collectLatest$2> continuation) {
        super(2, continuation);
        this.$this_collectLatest = flow;
        this.$collector = function2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyManagerKt$collectLatest$2 notificationVoiceReplyManagerKt$collectLatest$2 = new NotificationVoiceReplyManagerKt$collectLatest$2(this.$this_collectLatest, this.$collector, continuation);
        notificationVoiceReplyManagerKt$collectLatest$2.L$0 = obj;
        return notificationVoiceReplyManagerKt$collectLatest$2;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyManagerKt$collectLatest$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            final CoroutineScope coroutineScope = (CoroutineScope) this.L$0;
            final Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
            Flow<T> flow = this.$this_collectLatest;
            final Function2<T, Continuation<? super Unit>, Object> function2 = this.$collector;
            Object notificationVoiceReplyManagerKt$collectLatest$2$invokeSuspend$$inlined$collect$1 = new FlowCollector<T>() { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$collectLatest$2$invokeSuspend$$inlined$collect$1
                /* JADX WARN: Type inference failed for: r3v3, types: [T, kotlinx.coroutines.StandaloneCoroutine] */
                @Override // kotlinx.coroutines.flow.FlowCollector
                public final Object emit(T t, Continuation<? super Unit> continuation) {
                    Job job = (Job) Ref$ObjectRef.this.element;
                    if (job != null) {
                        job.cancel(null);
                    }
                    Ref$ObjectRef.this.element = BuildersKt.launch$default(coroutineScope, null, null, new NotificationVoiceReplyManagerKt$collectLatest$2$1$1(function2, t, null), 3);
                    return Unit.INSTANCE;
                }
            };
            this.label = 1;
            if (flow.collect(notificationVoiceReplyManagerKt$collectLatest$2$invokeSuspend$$inlined$collect$1, this) == coroutineSingletons) {
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
