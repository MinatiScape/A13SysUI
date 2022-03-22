package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.channels.Channel;
import kotlinx.coroutines.flow.MutableSharedFlow;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$getDozeStateChanges$1$2", f = "NotificationVoiceReplyManager.kt", l = {1176, 1000}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class NotificationVoiceReplyManagerKt$getDozeStateChanges$1$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ Channel<Boolean> $chan;
    public final /* synthetic */ MutableSharedFlow<Boolean> $flow;
    public Object L$0;
    public Object L$1;
    public Object L$2;
    public int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerKt$getDozeStateChanges$1$2(Channel<Boolean> channel, MutableSharedFlow<Boolean> mutableSharedFlow, Continuation<? super NotificationVoiceReplyManagerKt$getDozeStateChanges$1$2> continuation) {
        super(2, continuation);
        this.$chan = channel;
        this.$flow = mutableSharedFlow;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new NotificationVoiceReplyManagerKt$getDozeStateChanges$1$2(this.$chan, this.$flow, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyManagerKt$getDozeStateChanges$1$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:17:0x004e A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x004f  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x005b A[Catch: all -> 0x0083, TryCatch #0 {all -> 0x0083, blocks: (B:7:0x0018, B:12:0x0031, B:14:0x003c, B:15:0x0040, B:19:0x0052, B:21:0x005b, B:25:0x006a), top: B:35:0x0006 }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x007d  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:26:0x007a -> B:8:0x001b). Please submit an issue!!! */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r9) {
        /*
            r8 = this;
            kotlin.coroutines.intrinsics.CoroutineSingletons r0 = kotlin.coroutines.intrinsics.CoroutineSingletons.COROUTINE_SUSPENDED
            int r1 = r8.label
            r2 = 2
            r3 = 1
            if (r1 == 0) goto L_0x0035
            if (r1 == r3) goto L_0x0025
            if (r1 != r2) goto L_0x001d
            java.lang.Object r1 = r8.L$2
            kotlinx.coroutines.channels.ChannelIterator r1 = (kotlinx.coroutines.channels.ChannelIterator) r1
            java.lang.Object r4 = r8.L$1
            kotlinx.coroutines.channels.ReceiveChannel r4 = (kotlinx.coroutines.channels.ReceiveChannel) r4
            java.lang.Object r5 = r8.L$0
            kotlinx.coroutines.flow.MutableSharedFlow r5 = (kotlinx.coroutines.flow.MutableSharedFlow) r5
            kotlin.ResultKt.throwOnFailure(r9)     // Catch: all -> 0x0083
        L_0x001b:
            r9 = r5
            goto L_0x0040
        L_0x001d:
            java.lang.IllegalStateException r8 = new java.lang.IllegalStateException
            java.lang.String r9 = "call to 'resume' before 'invoke' with coroutine"
            r8.<init>(r9)
            throw r8
        L_0x0025:
            java.lang.Object r1 = r8.L$2
            kotlinx.coroutines.channels.ChannelIterator r1 = (kotlinx.coroutines.channels.ChannelIterator) r1
            java.lang.Object r4 = r8.L$1
            kotlinx.coroutines.channels.ReceiveChannel r4 = (kotlinx.coroutines.channels.ReceiveChannel) r4
            java.lang.Object r5 = r8.L$0
            kotlinx.coroutines.flow.MutableSharedFlow r5 = (kotlinx.coroutines.flow.MutableSharedFlow) r5
            kotlin.ResultKt.throwOnFailure(r9)     // Catch: all -> 0x0083
            goto L_0x0052
        L_0x0035:
            kotlin.ResultKt.throwOnFailure(r9)
            kotlinx.coroutines.channels.Channel<java.lang.Boolean> r4 = r8.$chan
            kotlinx.coroutines.flow.MutableSharedFlow<java.lang.Boolean> r9 = r8.$flow
            kotlinx.coroutines.channels.AbstractChannel$Itr r1 = r4.iterator()     // Catch: all -> 0x0083
        L_0x0040:
            r8.L$0 = r9     // Catch: all -> 0x0083
            r8.L$1 = r4     // Catch: all -> 0x0083
            r8.L$2 = r1     // Catch: all -> 0x0083
            r8.label = r3     // Catch: all -> 0x0083
            java.lang.Object r5 = r1.hasNext(r8)     // Catch: all -> 0x0083
            if (r5 != r0) goto L_0x004f
            return r0
        L_0x004f:
            r7 = r5
            r5 = r9
            r9 = r7
        L_0x0052:
            r6 = 0
            java.lang.Boolean r9 = (java.lang.Boolean) r9     // Catch: all -> 0x0083
            boolean r9 = r9.booleanValue()     // Catch: all -> 0x0083
            if (r9 == 0) goto L_0x007d
            java.lang.Object r9 = r1.next()     // Catch: all -> 0x0083
            java.lang.Boolean r9 = (java.lang.Boolean) r9     // Catch: all -> 0x0083
            boolean r9 = r9.booleanValue()     // Catch: all -> 0x0083
            if (r9 == 0) goto L_0x0069
            r9 = r3
            goto L_0x006a
        L_0x0069:
            r9 = 0
        L_0x006a:
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r9)     // Catch: all -> 0x0083
            r8.L$0 = r5     // Catch: all -> 0x0083
            r8.L$1 = r4     // Catch: all -> 0x0083
            r8.L$2 = r1     // Catch: all -> 0x0083
            r8.label = r2     // Catch: all -> 0x0083
            java.lang.Object r9 = r5.emit(r9, r8)     // Catch: all -> 0x0083
            if (r9 != r0) goto L_0x001b
            return r0
        L_0x007d:
            com.android.systemui.R$array.cancelConsumed(r4, r6)
            kotlin.Unit r8 = kotlin.Unit.INSTANCE
            return r8
        L_0x0083:
            r8 = move-exception
            throw r8     // Catch: all -> 0x0085
        L_0x0085:
            r9 = move-exception
            com.android.systemui.R$array.cancelConsumed(r4, r8)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$getDozeStateChanges$1$2.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
