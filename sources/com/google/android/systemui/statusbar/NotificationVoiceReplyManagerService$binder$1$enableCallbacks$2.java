package com.google.android.systemui.statusbar;

import com.google.android.systemui.statusbar.notification.voicereplies.CtaState;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
/* compiled from: NotificationVoiceReplyManagerService.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2", f = "NotificationVoiceReplyManagerService.kt", l = {289}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ INotificationVoiceReplyServiceCallbacks $callbacks;
    public final /* synthetic */ CtaState $ctaState;
    private /* synthetic */ Object L$0;
    public int label;
    public final /* synthetic */ NotificationVoiceReplyManagerService$binder$1 this$0;
    public final /* synthetic */ NotificationVoiceReplyManagerService this$1;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2(NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1, CtaState ctaState, INotificationVoiceReplyServiceCallbacks iNotificationVoiceReplyServiceCallbacks, NotificationVoiceReplyManagerService notificationVoiceReplyManagerService, Continuation<? super NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2> continuation) {
        super(2, continuation);
        this.this$0 = notificationVoiceReplyManagerService$binder$1;
        this.$ctaState = ctaState;
        this.$callbacks = iNotificationVoiceReplyServiceCallbacks;
        this.this$1 = notificationVoiceReplyManagerService;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2 notificationVoiceReplyManagerService$binder$1$enableCallbacks$2 = new NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2(this.this$0, this.$ctaState, this.$callbacks, this.this$1, continuation);
        notificationVoiceReplyManagerService$binder$1$enableCallbacks$2.L$0 = obj;
        return notificationVoiceReplyManagerService$binder$1$enableCallbacks$2;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Type inference failed for: r4v0, types: [com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$invokeSuspend$$inlined$filter$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r8) {
        /*
            r7 = this;
            kotlin.coroutines.intrinsics.CoroutineSingletons r0 = kotlin.coroutines.intrinsics.CoroutineSingletons.COROUTINE_SUSPENDED
            int r1 = r7.label
            r2 = 1
            if (r1 == 0) goto L_0x0030
            if (r1 != r2) goto L_0x0028
            java.lang.Object r0 = r7.L$0
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplySubscription r0 = (com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplySubscription) r0
            kotlin.ResultKt.throwOnFailure(r8)     // Catch: all -> 0x0026
            com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService r8 = r7.this$1
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger r8 = r8.logger
            com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1 r7 = r7.this$0
            java.util.Objects.requireNonNull(r7)
            int r7 = com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1.getUserId()
            r8.logUnregisterCallbacks(r7)
            r0.unsubscribe()
            kotlin.Unit r7 = kotlin.Unit.INSTANCE
            return r7
        L_0x0026:
            r8 = move-exception
            goto L_0x009a
        L_0x0028:
            java.lang.IllegalStateException r7 = new java.lang.IllegalStateException
            java.lang.String r8 = "call to 'resume' before 'invoke' with coroutine"
            r7.<init>(r8)
            throw r7
        L_0x0030:
            kotlin.ResultKt.throwOnFailure(r8)
            java.lang.Object r8 = r7.L$0
            kotlinx.coroutines.CoroutineScope r8 = (kotlinx.coroutines.CoroutineScope) r8
            com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1 r1 = r7.this$0
            java.util.Objects.requireNonNull(r1)
            kotlinx.coroutines.flow.SharedFlowImpl r1 = r1.setFeatureEnabledFlow
            com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1 r3 = r7.this$0
            com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$invokeSuspend$$inlined$filter$1 r4 = new com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$invokeSuspend$$inlined$filter$1
            r4.<init>()
            com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$invokeSuspend$$inlined$map$1 r1 = new com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$invokeSuspend$$inlined$map$1
            r1.<init>()
            com.google.android.systemui.statusbar.notification.voicereplies.CtaState r3 = r7.$ctaState
            byte[][] r4 = com.google.android.systemui.statusbar.NotificationVoiceReplyManagerServiceKt.AGSA_CERTS
            kotlinx.coroutines.flow.StateFlowImpl r4 = new kotlinx.coroutines.flow.StateFlowImpl
            if (r3 != 0) goto L_0x0054
            kotlinx.coroutines.internal.Symbol r3 = androidx.slice.compat.SliceProviderCompat.AnonymousClass2.NULL
        L_0x0054:
            r4.<init>(r3)
            com.google.android.systemui.statusbar.NotificationVoiceReplyManagerServiceKt$stateIn$1 r3 = new com.google.android.systemui.statusbar.NotificationVoiceReplyManagerServiceKt$stateIn$1
            r5 = 0
            r3.<init>(r1, r4, r5)
            r1 = 3
            kotlinx.coroutines.BuildersKt.launch$default(r8, r5, r5, r3, r1)
            com.google.android.systemui.statusbar.CallbacksHandler r1 = new com.google.android.systemui.statusbar.CallbacksHandler
            com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1 r3 = r7.this$0
            java.util.Objects.requireNonNull(r3)
            int r3 = com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1.getUserId()
            com.google.android.systemui.statusbar.INotificationVoiceReplyServiceCallbacks r6 = r7.$callbacks
            r1.<init>(r3, r6, r4)
            com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService r3 = r7.this$1
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManager r3 = r3.voiceReplyManager
            if (r3 != 0) goto L_0x0078
            goto L_0x0079
        L_0x0078:
            r5 = r3
        L_0x0079:
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$connect$1$registerHandler$1 r3 = r5.registerHandler(r1)
            com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1 r4 = r7.this$0     // Catch: all -> 0x0098
            java.util.Objects.requireNonNull(r4)     // Catch: all -> 0x0098
            kotlinx.coroutines.flow.SharedFlowImpl r4 = r4.startVoiceReplyFlow     // Catch: all -> 0x0098
            com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1 r5 = r7.this$0     // Catch: all -> 0x0098
            com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$invokeSuspend$$inlined$collect$1 r6 = new com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$invokeSuspend$$inlined$collect$1     // Catch: all -> 0x0098
            r6.<init>()     // Catch: all -> 0x0098
            r7.L$0 = r3     // Catch: all -> 0x0098
            r7.label = r2     // Catch: all -> 0x0098
            com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$invokeSuspend$$inlined$filter$2$2 r8 = new com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$invokeSuspend$$inlined$filter$2$2     // Catch: all -> 0x0098
            r8.<init>(r6, r5)     // Catch: all -> 0x0098
            r4.collect(r8, r7)     // Catch: all -> 0x0098
            return r0
        L_0x0098:
            r8 = move-exception
            r0 = r3
        L_0x009a:
            com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService r1 = r7.this$1
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger r1 = r1.logger
            com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1 r7 = r7.this$0
            java.util.Objects.requireNonNull(r7)
            int r7 = com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1.getUserId()
            r1.logUnregisterCallbacks(r7)
            r0.unsubscribe()
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
