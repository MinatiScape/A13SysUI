package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlinx.coroutines.flow.FlowCollector;
/* compiled from: Collect.kt */
/* loaded from: classes.dex */
public final class NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$2$2 implements FlowCollector<NotificationEntry> {
    public final /* synthetic */ VoiceReplyTarget $candidate$inlined;
    public final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine$inlined;
    public final /* synthetic */ FlowCollector $this_unsafeFlow$inlined;
    public final /* synthetic */ NotificationVoiceReplyController this$0;

    /* compiled from: Collect.kt */
    @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$2$2", f = "NotificationVoiceReplyManager.kt", l = {138}, m = "emit")
    /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$2$2$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass1 extends ContinuationImpl {
        public Object L$0;
        public int label;
        public /* synthetic */ Object result;

        public AnonymousClass1(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$2$2.this.emit(null, this);
        }
    }

    public NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$2$2(FlowCollector flowCollector, NotificationVoiceReplyController notificationVoiceReplyController, NotificationVoiceReplyController.Connection connection, VoiceReplyTarget voiceReplyTarget) {
        this.$this_unsafeFlow$inlined = flowCollector;
        this.this$0 = notificationVoiceReplyController;
        this.$this_stateMachine$inlined = connection;
        this.$candidate$inlined = voiceReplyTarget;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0021  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0030  */
    @Override // kotlinx.coroutines.flow.FlowCollector
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object emit(com.android.systemui.statusbar.notification.collection.NotificationEntry r12, kotlin.coroutines.Continuation r13) {
        /*
            r11 = this;
            boolean r0 = r13 instanceof com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$2$2.AnonymousClass1
            if (r0 == 0) goto L_0x0013
            r0 = r13
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$2$2$1 r0 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$2$2.AnonymousClass1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = r1 & r2
            if (r3 == 0) goto L_0x0013
            int r1 = r1 - r2
            r0.label = r1
            goto L_0x0018
        L_0x0013:
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$2$2$1 r0 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$2$2$1
            r0.<init>(r13)
        L_0x0018:
            java.lang.Object r13 = r0.result
            kotlin.coroutines.intrinsics.CoroutineSingletons r1 = kotlin.coroutines.intrinsics.CoroutineSingletons.COROUTINE_SUSPENDED
            int r2 = r0.label
            r3 = 1
            if (r2 == 0) goto L_0x0030
            if (r2 != r3) goto L_0x0028
            kotlin.ResultKt.throwOnFailure(r13)
            goto L_0x00ae
        L_0x0028:
            java.lang.IllegalStateException r11 = new java.lang.IllegalStateException
            java.lang.String r12 = "call to 'resume' before 'invoke' with coroutine"
            r11.<init>(r12)
            throw r11
        L_0x0030:
            kotlin.ResultKt.throwOnFailure(r13)
            kotlinx.coroutines.flow.FlowCollector r13 = r11.$this_unsafeFlow$inlined
            r6 = r12
            com.android.systemui.statusbar.notification.collection.NotificationEntry r6 = (com.android.systemui.statusbar.notification.collection.NotificationEntry) r6
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r12 = r11.this$0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r2 = r11.$this_stateMachine$inlined
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r11 = r11.$candidate$inlined
            java.util.Objects.requireNonNull(r12)
            java.util.Objects.requireNonNull(r6)
            java.lang.String r4 = r6.mKey
            java.util.Objects.requireNonNull(r11)
            java.lang.String r5 = r11.notifKey
            boolean r4 = kotlin.jvm.internal.Intrinsics.areEqual(r4, r5)
            r10 = 0
            if (r4 == 0) goto L_0x0089
            android.content.Context r4 = r12.context
            android.service.notification.StatusBarNotification r5 = r6.mSbn
            android.app.Notification r5 = r5.getNotification()
            android.app.Notification$Builder r4 = android.app.Notification.Builder.recoverBuilder(r4, r5)
            android.app.Notification$Builder r5 = r11.builder
            boolean r5 = android.app.Notification.areStyledNotificationsVisiblyDifferent(r4, r5)
            if (r5 == 0) goto L_0x006d
            android.service.notification.StatusBarNotification r11 = r6.mSbn
            long r7 = r11.getPostTime()
            goto L_0x006f
        L_0x006d:
            long r7 = r11.postTime
        L_0x006f:
            kotlin.InitializedLazyImpl r9 = new kotlin.InitializedLazyImpl
            r9.<init>(r4)
            r4 = r12
            r5 = r2
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r11 = r4.extractCandidate(r5, r6, r7, r9)
            if (r11 != 0) goto L_0x007d
            goto L_0x0082
        L_0x007d:
            com.google.android.systemui.statusbar.notification.voicereplies.HasCandidate r10 = new com.google.android.systemui.statusbar.notification.voicereplies.HasCandidate
            r10.<init>(r11)
        L_0x0082:
            if (r10 != 0) goto L_0x00a2
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyState r10 = r12.queryInitialState(r2)
            goto L_0x00a2
        L_0x0089:
            android.service.notification.StatusBarNotification r4 = r6.mSbn
            long r4 = r4.getPostTime()
            long r7 = r11.postTime
            int r11 = (r4 > r7 ? 1 : (r4 == r7 ? 0 : -1))
            if (r11 > 0) goto L_0x0096
            goto L_0x00a2
        L_0x0096:
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r11 = com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController.extractCandidate$default(r12, r2, r6)
            if (r11 != 0) goto L_0x009d
            goto L_0x00a2
        L_0x009d:
            com.google.android.systemui.statusbar.notification.voicereplies.HasCandidate r10 = new com.google.android.systemui.statusbar.notification.voicereplies.HasCandidate
            r10.<init>(r11)
        L_0x00a2:
            if (r10 != 0) goto L_0x00a5
            goto L_0x00ae
        L_0x00a5:
            r0.label = r3
            java.lang.Object r11 = r13.emit(r10, r0)
            if (r11 != r1) goto L_0x00ae
            return r1
        L_0x00ae:
            kotlin.Unit r11 = kotlin.Unit.INSTANCE
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$2$2.emit(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
    }
}
