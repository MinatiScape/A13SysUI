package com.google.android.systemui.statusbar;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlinx.coroutines.flow.FlowCollector;
/* compiled from: Collect.kt */
/* loaded from: classes.dex */
public final class NotificationVoiceReplyManagerService$binder$1$startVoiceReply$4$invokeSuspend$$inlined$filter$1$2 implements FlowCollector<OnVoiceAuthStateChangedData> {
    public final /* synthetic */ FlowCollector $this_unsafeFlow$inlined;
    public final /* synthetic */ int $token$inlined;
    public final /* synthetic */ NotificationVoiceReplyManagerService$binder$1 this$0;

    /* compiled from: Collect.kt */
    @DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$startVoiceReply$4$invokeSuspend$$inlined$filter$1$2", f = "NotificationVoiceReplyManagerService.kt", l = {137}, m = "emit")
    /* renamed from: com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$startVoiceReply$4$invokeSuspend$$inlined$filter$1$2$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass1 extends ContinuationImpl {
        public Object L$0;
        public Object L$1;
        public int label;
        public /* synthetic */ Object result;

        public AnonymousClass1(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return NotificationVoiceReplyManagerService$binder$1$startVoiceReply$4$invokeSuspend$$inlined$filter$1$2.this.emit(null, this);
        }
    }

    public NotificationVoiceReplyManagerService$binder$1$startVoiceReply$4$invokeSuspend$$inlined$filter$1$2(FlowCollector flowCollector, NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1, int i) {
        this.$this_unsafeFlow$inlined = flowCollector;
        this.this$0 = notificationVoiceReplyManagerService$binder$1;
        this.$token$inlined = i;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0021  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x002f  */
    @Override // kotlinx.coroutines.flow.FlowCollector
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object emit(com.google.android.systemui.statusbar.OnVoiceAuthStateChangedData r7, kotlin.coroutines.Continuation r8) {
        /*
            r6 = this;
            boolean r0 = r8 instanceof com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$startVoiceReply$4$invokeSuspend$$inlined$filter$1$2.AnonymousClass1
            if (r0 == 0) goto L_0x0013
            r0 = r8
            com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$startVoiceReply$4$invokeSuspend$$inlined$filter$1$2$1 r0 = (com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$startVoiceReply$4$invokeSuspend$$inlined$filter$1$2.AnonymousClass1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = r1 & r2
            if (r3 == 0) goto L_0x0013
            int r1 = r1 - r2
            r0.label = r1
            goto L_0x0018
        L_0x0013:
            com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$startVoiceReply$4$invokeSuspend$$inlined$filter$1$2$1 r0 = new com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$startVoiceReply$4$invokeSuspend$$inlined$filter$1$2$1
            r0.<init>(r8)
        L_0x0018:
            java.lang.Object r8 = r0.result
            kotlin.coroutines.intrinsics.CoroutineSingletons r1 = kotlin.coroutines.intrinsics.CoroutineSingletons.COROUTINE_SUSPENDED
            int r2 = r0.label
            r3 = 1
            if (r2 == 0) goto L_0x002f
            if (r2 != r3) goto L_0x0027
            kotlin.ResultKt.throwOnFailure(r8)
            goto L_0x005b
        L_0x0027:
            java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
            java.lang.String r7 = "call to 'resume' before 'invoke' with coroutine"
            r6.<init>(r7)
            throw r6
        L_0x002f:
            kotlin.ResultKt.throwOnFailure(r8)
            kotlinx.coroutines.flow.FlowCollector r8 = r6.$this_unsafeFlow$inlined
            r2 = r7
            com.google.android.systemui.statusbar.OnVoiceAuthStateChangedData r2 = (com.google.android.systemui.statusbar.OnVoiceAuthStateChangedData) r2
            java.util.Objects.requireNonNull(r2)
            int r4 = r2.userId
            com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1 r5 = r6.this$0
            java.util.Objects.requireNonNull(r5)
            int r5 = com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1.getUserId()
            if (r4 != r5) goto L_0x004f
            int r2 = r2.sessionToken
            int r6 = r6.$token$inlined
            if (r2 != r6) goto L_0x004f
            r6 = r3
            goto L_0x0050
        L_0x004f:
            r6 = 0
        L_0x0050:
            if (r6 == 0) goto L_0x005b
            r0.label = r3
            java.lang.Object r6 = r8.emit(r7, r0)
            if (r6 != r1) goto L_0x005b
            return r1
        L_0x005b:
            kotlin.Unit r6 = kotlin.Unit.INSTANCE
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$startVoiceReply$4$invokeSuspend$$inlined$filter$1$2.emit(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
    }
}
