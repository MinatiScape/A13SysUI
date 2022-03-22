package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import java.util.Objects;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$noCandidate$2", f = "NotificationVoiceReplyManager.kt", l = {1174}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class NotificationVoiceReplyController$stateMachine$2$noCandidate$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ Flow<NotificationEntry> $reinflations;
    public final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine;
    private /* synthetic */ Object L$0;
    public int label;
    public final /* synthetic */ NotificationVoiceReplyController this$0;

    /* compiled from: NotificationVoiceReplyManager.kt */
    @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$noCandidate$2$1", f = "NotificationVoiceReplyManager.kt", l = {464}, m = "invokeSuspend")
    /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$noCandidate$2$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        public final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine;
        public Object L$0;
        public int label;
        public final /* synthetic */ NotificationVoiceReplyController this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass1(NotificationVoiceReplyController.Connection connection, NotificationVoiceReplyController notificationVoiceReplyController, Continuation<? super AnonymousClass1> continuation) {
            super(2, continuation);
            this.$this_stateMachine = connection;
            this.this$0 = notificationVoiceReplyController;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass1(this.$this_stateMachine, this.this$0, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:11:0x0032 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:14:0x003b  */
        /* JADX WARN: Removed duplicated region for block: B:18:0x006f  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:10:0x0030 -> B:12:0x0033). Please submit an issue!!! */
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
                r2 = 1
                if (r1 == 0) goto L_0x0019
                if (r1 != r2) goto L_0x0011
                java.lang.Object r1 = r8.L$0
                kotlinx.coroutines.channels.ChannelIterator r1 = (kotlinx.coroutines.channels.ChannelIterator) r1
                kotlin.ResultKt.throwOnFailure(r9)
                goto L_0x0033
            L_0x0011:
                java.lang.IllegalStateException r8 = new java.lang.IllegalStateException
                java.lang.String r9 = "call to 'resume' before 'invoke' with coroutine"
                r8.<init>(r9)
                throw r8
            L_0x0019:
                kotlin.ResultKt.throwOnFailure(r9)
                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r9 = r8.$this_stateMachine
                java.util.Objects.requireNonNull(r9)
                kotlinx.coroutines.channels.Channel<com.google.android.systemui.statusbar.notification.voicereplies.StartSessionRequest> r9 = r9.startSessionRequests
                kotlinx.coroutines.channels.AbstractChannel$Itr r9 = r9.iterator()
                r1 = r9
            L_0x0028:
                r8.L$0 = r1
                r8.label = r2
                java.lang.Object r9 = r1.hasNext(r8)
                if (r9 != r0) goto L_0x0033
                return r0
            L_0x0033:
                java.lang.Boolean r9 = (java.lang.Boolean) r9
                boolean r9 = r9.booleanValue()
                if (r9 == 0) goto L_0x006f
                java.lang.Object r9 = r1.next()
                com.google.android.systemui.statusbar.notification.voicereplies.StartSessionRequest r9 = (com.google.android.systemui.statusbar.notification.voicereplies.StartSessionRequest) r9
                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r3 = r8.this$0
                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger r3 = r3.logger
                java.util.Objects.requireNonNull(r9)
                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler r4 = r9.handler
                int r4 = r4.getUserId()
                java.util.Objects.requireNonNull(r3)
                com.android.systemui.log.LogBuffer r3 = r3.logBuffer
                com.android.systemui.log.LogLevel r5 = com.android.systemui.log.LogLevel.DEBUG
                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger$logStartSessionNoCandidate$2 r6 = com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger$logStartSessionNoCandidate$2.INSTANCE
                java.util.Objects.requireNonNull(r3)
                boolean r7 = r3.frozen
                if (r7 != 0) goto L_0x0069
                java.lang.String r7 = "NotifVoiceReply"
                com.android.systemui.log.LogMessageImpl r5 = r3.obtain(r7, r5, r6)
                r5.int1 = r4
                r3.push(r5)
            L_0x0069:
                kotlin.jvm.functions.Function0<kotlin.Unit> r9 = r9.onFail
                r9.invoke()
                goto L_0x0028
            L_0x006f:
                kotlin.Unit r8 = kotlin.Unit.INSTANCE
                return r8
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$noCandidate$2.AnonymousClass1.invokeSuspend(java.lang.Object):java.lang.Object");
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$stateMachine$2$noCandidate$2(NotificationVoiceReplyController notificationVoiceReplyController, Flow<NotificationEntry> flow, NotificationVoiceReplyController.Connection connection, Continuation<? super NotificationVoiceReplyController$stateMachine$2$noCandidate$2> continuation) {
        super(2, continuation);
        this.this$0 = notificationVoiceReplyController;
        this.$reinflations = flow;
        this.$this_stateMachine = connection;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyController$stateMachine$2$noCandidate$2 notificationVoiceReplyController$stateMachine$2$noCandidate$2 = new NotificationVoiceReplyController$stateMachine$2$noCandidate$2(this.this$0, this.$reinflations, this.$this_stateMachine, continuation);
        notificationVoiceReplyController$stateMachine$2$noCandidate$2.L$0 = obj;
        return notificationVoiceReplyController$stateMachine$2$noCandidate$2;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyController$stateMachine$2$noCandidate$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object obj2 = CoroutineSingletons.COROUTINE_SUSPENDED;
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            CoroutineScope coroutineScope = (CoroutineScope) this.L$0;
            NotificationVoiceReplyLogger notificationVoiceReplyLogger = this.this$0.logger;
            Objects.requireNonNull(notificationVoiceReplyLogger);
            notificationVoiceReplyLogger.eventLogger.log(VoiceReplyEvent.STATE_NO_CANDIDATE);
            LogBuffer logBuffer = notificationVoiceReplyLogger.logBuffer;
            LogLevel logLevel = LogLevel.DEBUG;
            NotificationVoiceReplyLogger$logStateNoCandidate$2 notificationVoiceReplyLogger$logStateNoCandidate$2 = NotificationVoiceReplyLogger$logStateNoCandidate$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logStateNoCandidate$2);
                obtain.str1 = "NoCandidate";
                logBuffer.push(obtain);
            }
            BuildersKt.launch$default(coroutineScope, null, null, new AnonymousClass1(this.$this_stateMachine, this.this$0, null), 3);
            Flow<NotificationEntry> flow = this.$reinflations;
            NotificationVoiceReplyController notificationVoiceReplyController = this.this$0;
            final NotificationVoiceReplyController.Connection connection = this.$this_stateMachine;
            FlowCollector<VoiceReplyTarget> notificationVoiceReplyController$stateMachine$2$noCandidate$2$invokeSuspend$$inlined$collect$1 = new FlowCollector<VoiceReplyTarget>() { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$noCandidate$2$invokeSuspend$$inlined$collect$1
                @Override // kotlinx.coroutines.flow.FlowCollector
                public final Object emit(VoiceReplyTarget voiceReplyTarget, Continuation<? super Unit> continuation) {
                    NotificationVoiceReplyController.Connection connection2 = NotificationVoiceReplyController.Connection.this;
                    Objects.requireNonNull(connection2);
                    connection2.stateFlow.setValue(new HasCandidate(voiceReplyTarget));
                    return Unit.INSTANCE;
                }
            };
            this.label = 1;
            Object collect = flow.collect(new NotificationVoiceReplyController$stateMachine$2$noCandidate$2$invokeSuspend$$inlined$mapNotNull$1$2(notificationVoiceReplyController$stateMachine$2$noCandidate$2$invokeSuspend$$inlined$collect$1, notificationVoiceReplyController, connection), this);
            if (collect != obj2) {
                collect = Unit.INSTANCE;
            }
            if (collect == obj2) {
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
