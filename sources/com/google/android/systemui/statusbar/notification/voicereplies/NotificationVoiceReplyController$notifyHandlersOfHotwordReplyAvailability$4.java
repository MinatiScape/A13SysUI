package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$4", f = "NotificationVoiceReplyManager.kt", l = {1174}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class NotificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$4 extends SuspendLambda implements Function2<VoiceReplyTarget, Continuation<? super Unit>, Object> {
    public /* synthetic */ Object L$0;
    public Object L$1;
    public int label;
    public final /* synthetic */ NotificationVoiceReplyController this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$4(NotificationVoiceReplyController notificationVoiceReplyController, Continuation<? super NotificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$4> continuation) {
        super(2, continuation);
        this.this$0 = notificationVoiceReplyController;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$4 notificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$4 = new NotificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$4(this.this$0, continuation);
        notificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$4.L$0 = obj;
        return notificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$4;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(VoiceReplyTarget voiceReplyTarget, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$4) create(voiceReplyTarget, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x0089 A[LOOP:1: B:28:0x0083->B:30:0x0089, LOOP_END] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r6) {
        /*
            r5 = this;
            kotlin.coroutines.intrinsics.CoroutineSingletons r0 = kotlin.coroutines.intrinsics.CoroutineSingletons.COROUTINE_SUSPENDED
            int r1 = r5.label
            r2 = 1
            if (r1 == 0) goto L_0x001f
            if (r1 == r2) goto L_0x0011
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r6 = "call to 'resume' before 'invoke' with coroutine"
            r5.<init>(r6)
            throw r5
        L_0x0011:
            java.lang.Object r0 = r5.L$1
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r0 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController) r0
            java.lang.Object r5 = r5.L$0
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r5 = (com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget) r5
            kotlin.ResultKt.throwOnFailure(r6)     // Catch: all -> 0x001d
            goto L_0x0067
        L_0x001d:
            r6 = move-exception
            goto L_0x0072
        L_0x001f:
            kotlin.ResultKt.throwOnFailure(r6)
            java.lang.Object r6 = r5.L$0
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r6 = (com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget) r6
            if (r6 != 0) goto L_0x002b
            kotlin.Unit r5 = kotlin.Unit.INSTANCE
            return r5
        L_0x002b:
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r1 = r5.this$0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger r1 = r1.logger
            int r3 = r6.userId
            r1.logHotwordAvailabilityChanged(r3, r2)
            java.util.List<com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler> r1 = r6.handlers
            java.util.Iterator r1 = r1.iterator()
        L_0x003a:
            boolean r3 = r1.hasNext()
            if (r3 == 0) goto L_0x004a
            java.lang.Object r3 = r1.next()
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler r3 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler) r3
            r3.onNotifAvailableForReplyChanged(r2)
            goto L_0x003a
        L_0x004a:
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r1 = r5.this$0
            r5.L$0 = r6     // Catch: all -> 0x006d
            r5.L$1 = r1     // Catch: all -> 0x006d
            r5.label = r2     // Catch: all -> 0x006d
            kotlinx.coroutines.CancellableContinuationImpl r3 = new kotlinx.coroutines.CancellableContinuationImpl     // Catch: all -> 0x006d
            kotlin.coroutines.Continuation r5 = androidx.preference.R$color.intercepted(r5)     // Catch: all -> 0x006d
            r3.<init>(r5, r2)     // Catch: all -> 0x006d
            r3.initCancellability()     // Catch: all -> 0x006d
            java.lang.Object r5 = r3.getResult()     // Catch: all -> 0x006d
            if (r5 != r0) goto L_0x0065
            return r0
        L_0x0065:
            r5 = r6
            r0 = r1
        L_0x0067:
            kotlin.KotlinNothingValueException r6 = new kotlin.KotlinNothingValueException     // Catch: all -> 0x001d
            r6.<init>()     // Catch: all -> 0x001d
            throw r6     // Catch: all -> 0x001d
        L_0x006d:
            r5 = move-exception
            r0 = r1
            r4 = r6
            r6 = r5
            r5 = r4
        L_0x0072:
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger r0 = r0.logger
            java.util.Objects.requireNonNull(r5)
            int r1 = r5.userId
            r2 = 0
            r0.logHotwordAvailabilityChanged(r1, r2)
            java.util.List<com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler> r5 = r5.handlers
            java.util.Iterator r5 = r5.iterator()
        L_0x0083:
            boolean r0 = r5.hasNext()
            if (r0 == 0) goto L_0x0093
            java.lang.Object r0 = r5.next()
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler r0 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler) r0
            r0.onNotifAvailableForReplyChanged(r2)
            goto L_0x0083
        L_0x0093:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$4.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
