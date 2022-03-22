package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$6", f = "NotificationVoiceReplyManager.kt", l = {330}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$6 extends SuspendLambda implements Function2<VoiceReplyTarget, Continuation<? super Unit>, Object> {
    public /* synthetic */ Object L$0;
    public int label;
    public final /* synthetic */ NotificationVoiceReplyController this$0;

    /* compiled from: NotificationVoiceReplyManager.kt */
    @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$6$4", f = "NotificationVoiceReplyManager.kt", l = {1174}, m = "invokeSuspend")
    /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$6$4  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass4 extends SuspendLambda implements Function2<Boolean, Continuation<? super Unit>, Object> {
        public final /* synthetic */ VoiceReplyTarget $candidate;
        public Object L$0;
        public Object L$1;
        public /* synthetic */ boolean Z$0;
        public int label;
        public final /* synthetic */ NotificationVoiceReplyController this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass4(NotificationVoiceReplyController notificationVoiceReplyController, VoiceReplyTarget voiceReplyTarget, Continuation<? super AnonymousClass4> continuation) {
            super(2, continuation);
            this.this$0 = notificationVoiceReplyController;
            this.$candidate = voiceReplyTarget;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            AnonymousClass4 r0 = new AnonymousClass4(this.this$0, this.$candidate, continuation);
            r0.Z$0 = ((Boolean) obj).booleanValue();
            return r0;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(Boolean bool, Continuation<? super Unit> continuation) {
            return ((AnonymousClass4) create(Boolean.valueOf(bool.booleanValue()), continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:28:0x0090 A[LOOP:1: B:26:0x008a->B:28:0x0090, LOOP_END] */
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
                com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r0 = (com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget) r0
                java.lang.Object r5 = r5.L$0
                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r5 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController) r5
                kotlin.ResultKt.throwOnFailure(r6)     // Catch: all -> 0x001d
                goto L_0x006e
            L_0x001d:
                r6 = move-exception
                goto L_0x0079
            L_0x001f:
                kotlin.ResultKt.throwOnFailure(r6)
                boolean r6 = r5.Z$0
                if (r6 == 0) goto L_0x009b
                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r6 = r5.this$0
                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger r6 = r6.logger
                com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r1 = r5.$candidate
                java.util.Objects.requireNonNull(r1)
                int r1 = r1.userId
                r6.logQuickPhraseAvailabilityChanged(r1, r2)
                com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r6 = r5.$candidate
                java.util.Objects.requireNonNull(r6)
                java.util.List<com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler> r6 = r6.handlers
                java.util.Iterator r6 = r6.iterator()
            L_0x003f:
                boolean r1 = r6.hasNext()
                if (r1 == 0) goto L_0x004f
                java.lang.Object r1 = r6.next()
                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler r1 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler) r1
                r1.onNotifAvailableForQuickPhraseReplyChanged(r2)
                goto L_0x003f
            L_0x004f:
                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r6 = r5.this$0
                com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r1 = r5.$candidate
                r5.L$0 = r6     // Catch: all -> 0x0074
                r5.L$1 = r1     // Catch: all -> 0x0074
                r5.label = r2     // Catch: all -> 0x0074
                kotlinx.coroutines.CancellableContinuationImpl r3 = new kotlinx.coroutines.CancellableContinuationImpl     // Catch: all -> 0x0074
                kotlin.coroutines.Continuation r5 = androidx.preference.R$color.intercepted(r5)     // Catch: all -> 0x0074
                r3.<init>(r5, r2)     // Catch: all -> 0x0074
                r3.initCancellability()     // Catch: all -> 0x0074
                java.lang.Object r5 = r3.getResult()     // Catch: all -> 0x0074
                if (r5 != r0) goto L_0x006c
                return r0
            L_0x006c:
                r5 = r6
                r0 = r1
            L_0x006e:
                kotlin.KotlinNothingValueException r6 = new kotlin.KotlinNothingValueException     // Catch: all -> 0x001d
                r6.<init>()     // Catch: all -> 0x001d
                throw r6     // Catch: all -> 0x001d
            L_0x0074:
                r5 = move-exception
                r0 = r1
                r4 = r6
                r6 = r5
                r5 = r4
            L_0x0079:
                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger r5 = r5.logger
                java.util.Objects.requireNonNull(r0)
                int r1 = r0.userId
                r2 = 0
                r5.logQuickPhraseAvailabilityChanged(r1, r2)
                java.util.List<com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler> r5 = r0.handlers
                java.util.Iterator r5 = r5.iterator()
            L_0x008a:
                boolean r0 = r5.hasNext()
                if (r0 == 0) goto L_0x009a
                java.lang.Object r0 = r5.next()
                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler r0 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler) r0
                r0.onNotifAvailableForQuickPhraseReplyChanged(r2)
                goto L_0x008a
            L_0x009a:
                throw r6
            L_0x009b:
                kotlin.Unit r5 = kotlin.Unit.INSTANCE
                return r5
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$6.AnonymousClass4.invokeSuspend(java.lang.Object):java.lang.Object");
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$6(NotificationVoiceReplyController notificationVoiceReplyController, Continuation<? super NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$6> continuation) {
        super(2, continuation);
        this.this$0 = notificationVoiceReplyController;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$6 notificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$6 = new NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$6(this.this$0, continuation);
        notificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$6.L$0 = obj;
        return notificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$6;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(VoiceReplyTarget voiceReplyTarget, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$6) create(voiceReplyTarget, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Type inference failed for: r3v4, types: [com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$6$invokeSuspend$$inlined$combine$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r7) {
        /*
            r6 = this;
            kotlin.coroutines.intrinsics.CoroutineSingletons r0 = kotlin.coroutines.intrinsics.CoroutineSingletons.COROUTINE_SUSPENDED
            int r1 = r6.label
            r2 = 1
            if (r1 == 0) goto L_0x0015
            if (r1 != r2) goto L_0x000d
            kotlin.ResultKt.throwOnFailure(r7)
            goto L_0x0079
        L_0x000d:
            java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
            java.lang.String r7 = "call to 'resume' before 'invoke' with coroutine"
            r6.<init>(r7)
            throw r6
        L_0x0015:
            kotlin.ResultKt.throwOnFailure(r7)
            java.lang.Object r7 = r6.L$0
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r7 = (com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget) r7
            if (r7 != 0) goto L_0x0021
            kotlin.Unit r6 = kotlin.Unit.INSTANCE
            return r6
        L_0x0021:
            java.util.List<com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler> r1 = r7.handlers
            java.util.ArrayList r3 = new java.util.ArrayList
            r4 = 10
            int r4 = kotlin.collections.CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(r1, r4)
            r3.<init>(r4)
            java.util.Iterator r1 = r1.iterator()
        L_0x0032:
            boolean r4 = r1.hasNext()
            if (r4 == 0) goto L_0x0046
            java.lang.Object r4 = r1.next()
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler r4 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler) r4
            kotlinx.coroutines.flow.StateFlow r4 = r4.getShowCta()
            r3.add(r4)
            goto L_0x0032
        L_0x0046:
            java.util.List r1 = kotlin.collections.CollectionsKt___CollectionsKt.toList(r3)
            r3 = 0
            kotlinx.coroutines.flow.Flow[] r3 = new kotlinx.coroutines.flow.Flow[r3]
            java.lang.Object[] r1 = r1.toArray(r3)
            java.lang.String r3 = "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>"
            java.util.Objects.requireNonNull(r1, r3)
            kotlinx.coroutines.flow.Flow[] r1 = (kotlinx.coroutines.flow.Flow[]) r1
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$6$invokeSuspend$$inlined$combine$1 r3 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$6$invokeSuspend$$inlined$combine$1
            r3.<init>()
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$6$invokeSuspend$$inlined$map$1 r1 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$6$invokeSuspend$$inlined$map$1
            r1.<init>()
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$distinctUntilChanged$1 r3 = com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$distinctUntilChanged$1.INSTANCE
            kotlinx.coroutines.flow.SafeFlow r1 = com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt.distinctUntilChanged(r1, r3)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$6$4 r3 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$6$4
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r4 = r6.this$0
            r5 = 0
            r3.<init>(r4, r7, r5)
            r6.label = r2
            java.lang.Object r6 = com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt.collectLatest(r1, r3, r6)
            if (r6 != r0) goto L_0x0079
            return r0
        L_0x0079:
            kotlin.Unit r6 = kotlin.Unit.INSTANCE
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$6.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
