package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Ref$ObjectRef;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$distinctUntilChanged$2", f = "NotificationVoiceReplyManager.kt", l = {1169}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class NotificationVoiceReplyManagerKt$distinctUntilChanged$2 extends SuspendLambda implements Function2<FlowCollector<Object>, Continuation<? super Unit>, Object> {
    public final /* synthetic */ Function2<Object, Object, Boolean> $areEqual;
    public final /* synthetic */ Flow<Object> $this_distinctUntilChanged;
    private /* synthetic */ Object L$0;
    public int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerKt$distinctUntilChanged$2(Flow<Object> flow, Function2<Object, Object, Boolean> function2, Continuation<? super NotificationVoiceReplyManagerKt$distinctUntilChanged$2> continuation) {
        super(2, continuation);
        this.$this_distinctUntilChanged = flow;
        this.$areEqual = function2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyManagerKt$distinctUntilChanged$2 notificationVoiceReplyManagerKt$distinctUntilChanged$2 = new NotificationVoiceReplyManagerKt$distinctUntilChanged$2(this.$this_distinctUntilChanged, this.$areEqual, continuation);
        notificationVoiceReplyManagerKt$distinctUntilChanged$2.L$0 = obj;
        return notificationVoiceReplyManagerKt$distinctUntilChanged$2;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(FlowCollector<Object> flowCollector, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyManagerKt$distinctUntilChanged$2) create(flowCollector, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [T, com.google.android.systemui.statusbar.notification.voicereplies.NO_VALUE] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            final FlowCollector flowCollector = (FlowCollector) this.L$0;
            final Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
            ref$ObjectRef.element = NO_VALUE.INSTANCE;
            Flow<Object> flow = this.$this_distinctUntilChanged;
            final Function2<Object, Object, Boolean> function2 = this.$areEqual;
            FlowCollector<? super Object> notificationVoiceReplyManagerKt$distinctUntilChanged$2$invokeSuspend$$inlined$collect$1 = new FlowCollector<Object>() { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$distinctUntilChanged$2$invokeSuspend$$inlined$collect$1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // kotlinx.coroutines.flow.FlowCollector
                public final Object emit(Object obj2, Continuation<? super Unit> continuation) {
                    T t = Ref$ObjectRef.this.element;
                    if (t == NO_VALUE.INSTANCE || !((Boolean) function2.invoke(t, obj2)).booleanValue()) {
                        Ref$ObjectRef.this.element = obj2;
                        Object emit = flowCollector.emit(obj2, continuation);
                        if (emit == CoroutineSingletons.COROUTINE_SUSPENDED) {
                            return emit;
                        }
                    }
                    return Unit.INSTANCE;
                }
            };
            this.label = 1;
            if (flow.collect(notificationVoiceReplyManagerKt$distinctUntilChanged$2$invokeSuspend$$inlined$collect$1, this) == coroutineSingletons) {
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
