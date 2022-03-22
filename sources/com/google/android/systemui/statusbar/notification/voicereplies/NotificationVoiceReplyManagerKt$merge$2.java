package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.channels.SendChannel;
import kotlinx.coroutines.flow.Flow;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$merge$2", f = "NotificationVoiceReplyManager.kt", l = {}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class NotificationVoiceReplyManagerKt$merge$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ SendChannel<Object> $chan;
    public final /* synthetic */ Flow<Object>[] $flows;
    private /* synthetic */ Object L$0;
    public int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerKt$merge$2(Flow<Object>[] flowArr, SendChannel<Object> sendChannel, Continuation<? super NotificationVoiceReplyManagerKt$merge$2> continuation) {
        super(2, continuation);
        this.$flows = flowArr;
        this.$chan = sendChannel;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyManagerKt$merge$2 notificationVoiceReplyManagerKt$merge$2 = new NotificationVoiceReplyManagerKt$merge$2(this.$flows, this.$chan, continuation);
        notificationVoiceReplyManagerKt$merge$2.L$0 = obj;
        return notificationVoiceReplyManagerKt$merge$2;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        Unit unit = Unit.INSTANCE;
        ((NotificationVoiceReplyManagerKt$merge$2) create(coroutineScope, continuation)).invokeSuspend(unit);
        return unit;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        if (this.label == 0) {
            ResultKt.throwOnFailure(obj);
            CoroutineScope coroutineScope = (CoroutineScope) this.L$0;
            Flow<Object>[] flowArr = this.$flows;
            SendChannel<Object> sendChannel = this.$chan;
            int i = 0;
            int length = flowArr.length;
            while (i < length) {
                Flow<Object> flow = flowArr[i];
                i++;
                BuildersKt.launch$default(coroutineScope, null, null, new NotificationVoiceReplyManagerKt$merge$2$1$1(flow, sendChannel, null), 3);
            }
            return Unit.INSTANCE;
        }
        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
    }
}
