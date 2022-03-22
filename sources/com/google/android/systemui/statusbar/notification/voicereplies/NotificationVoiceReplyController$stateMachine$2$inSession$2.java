package com.google.android.systemui.statusbar.notification.voicereplies;

import androidx.mediarouter.R$dimen;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.policy.RemoteInputViewController;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.StandaloneCoroutine;
import kotlinx.coroutines.flow.FlowCollector;
import kotlinx.coroutines.flow.MutableSharedFlow;
import kotlinx.coroutines.flow.MutableStateFlow;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$inSession$2", f = "NotificationVoiceReplyManager.kt", l = {619}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class NotificationVoiceReplyController$stateMachine$2$inSession$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ Function2<Session, Continuation<? super Unit>, Object> $block;
    public final /* synthetic */ String $initialContent;
    public final /* synthetic */ MutableSharedFlow<Pair<String, RemoteInputViewController>> $remoteInputViewActivatedForVoiceReply;
    public final /* synthetic */ VoiceReplyTarget $target;
    public final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine;
    private /* synthetic */ Object L$0;
    public int label;
    public final /* synthetic */ NotificationVoiceReplyController this$0;

    /* compiled from: NotificationVoiceReplyManager.kt */
    @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$inSession$2$1", f = "NotificationVoiceReplyManager.kt", l = {602}, m = "invokeSuspend")
    /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$inSession$2$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        public final /* synthetic */ Function2<Session, Continuation<? super Unit>, Object> $block;
        public final /* synthetic */ String $initialContent;
        public final /* synthetic */ MutableSharedFlow<Pair<String, RemoteInputViewController>> $remoteInputViewActivatedForVoiceReply;
        public final /* synthetic */ VoiceReplyTarget $target;
        public final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine;
        private /* synthetic */ Object L$0;
        public int label;
        public final /* synthetic */ NotificationVoiceReplyController this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        public AnonymousClass1(VoiceReplyTarget voiceReplyTarget, String str, MutableSharedFlow<Pair<String, RemoteInputViewController>> mutableSharedFlow, NotificationVoiceReplyController.Connection connection, NotificationVoiceReplyController notificationVoiceReplyController, Function2<? super Session, ? super Continuation<? super Unit>, ? extends Object> function2, Continuation<? super AnonymousClass1> continuation) {
            super(2, continuation);
            this.$target = voiceReplyTarget;
            this.$initialContent = str;
            this.$remoteInputViewActivatedForVoiceReply = mutableSharedFlow;
            this.$this_stateMachine = connection;
            this.this$0 = notificationVoiceReplyController;
            this.$block = function2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            AnonymousClass1 r8 = new AnonymousClass1(this.$target, this.$initialContent, this.$remoteInputViewActivatedForVoiceReply, this.$this_stateMachine, this.this$0, this.$block, continuation);
            r8.L$0 = obj;
            return r8;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Type inference failed for: r1v4, types: [com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$inSession$2$1$session$1] */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Job job;
            Object obj2 = CoroutineSingletons.COROUTINE_SUSPENDED;
            int i = this.label;
            VoiceReplyState voiceReplyState = null;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                final AuthStateRef authStateRef = new AuthStateRef();
                StandaloneCoroutine launch$default = BuildersKt.launch$default((CoroutineScope) this.L$0, null, null, new NotificationVoiceReplyController$stateMachine$2$inSession$2$1$sessionJob$1(this.this$0, this.$block, new Session() { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$inSession$2$1$session$1
                    @Override // com.google.android.systemui.statusbar.notification.voicereplies.Session
                    public final void setVoiceAuthState(int i2) {
                        AuthStateRef authStateRef2 = AuthStateRef.this;
                        Objects.requireNonNull(authStateRef2);
                        authStateRef2.value = i2;
                    }
                }, null), 3);
                VoiceReplyTarget voiceReplyTarget = this.$target;
                String str = this.$initialContent;
                MutableSharedFlow<Pair<String, RemoteInputViewController>> mutableSharedFlow = this.$remoteInputViewActivatedForVoiceReply;
                this.L$0 = launch$default;
                this.label = 1;
                Objects.requireNonNull(voiceReplyTarget);
                Object coroutineScope = R$dimen.coroutineScope(new VoiceReplyTarget$focus$2(voiceReplyTarget, str, mutableSharedFlow, authStateRef, null), this);
                if (coroutineScope != obj2) {
                    coroutineScope = Unit.INSTANCE;
                }
                if (coroutineScope == obj2) {
                    return obj2;
                }
                job = launch$default;
            } else if (i == 1) {
                job = (Job) this.L$0;
                ResultKt.throwOnFailure(obj);
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            CancellationException cancellationException = new CancellationException("Voice reply has completed, session scope no longer valid");
            cancellationException.initCause(null);
            job.cancel(cancellationException);
            NotificationVoiceReplyController.Connection connection = this.$this_stateMachine;
            Objects.requireNonNull(connection);
            MutableStateFlow<VoiceReplyState> mutableStateFlow = connection.stateFlow;
            CommonNotifCollection commonNotifCollection = this.this$0.notifCollection;
            VoiceReplyTarget voiceReplyTarget2 = this.$target;
            Objects.requireNonNull(voiceReplyTarget2);
            if (commonNotifCollection.getEntry(voiceReplyTarget2.notifKey) != null) {
                voiceReplyState = new HasCandidate(this.$target);
            }
            if (voiceReplyState == null) {
                voiceReplyState = this.this$0.queryInitialState(this.$this_stateMachine);
            }
            mutableStateFlow.setValue(voiceReplyState);
            return Unit.INSTANCE;
        }
    }

    /* compiled from: NotificationVoiceReplyManager.kt */
    @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$inSession$2$2", f = "NotificationVoiceReplyManager.kt", l = {1174}, m = "invokeSuspend")
    /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$inSession$2$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        public final /* synthetic */ VoiceReplyTarget $target;
        public final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine;
        public int label;
        public final /* synthetic */ NotificationVoiceReplyController this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass2(NotificationVoiceReplyController.Connection connection, VoiceReplyTarget voiceReplyTarget, NotificationVoiceReplyController notificationVoiceReplyController, Continuation<? super AnonymousClass2> continuation) {
            super(2, continuation);
            this.$this_stateMachine = connection;
            this.$target = voiceReplyTarget;
            this.this$0 = notificationVoiceReplyController;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass2(this.$this_stateMachine, this.$target, this.this$0, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Object obj2 = CoroutineSingletons.COROUTINE_SUSPENDED;
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                NotificationVoiceReplyController.Connection connection = this.$this_stateMachine;
                Objects.requireNonNull(connection);
                MutableSharedFlow<String> mutableSharedFlow = connection.entryRemovals;
                VoiceReplyTarget voiceReplyTarget = this.$target;
                final NotificationVoiceReplyController notificationVoiceReplyController = this.this$0;
                final NotificationVoiceReplyController.Connection connection2 = this.$this_stateMachine;
                FlowCollector<String> notificationVoiceReplyController$stateMachine$2$inSession$2$2$invokeSuspend$$inlined$collect$1 = new FlowCollector<String>() { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$inSession$2$2$invokeSuspend$$inlined$collect$1
                    @Override // kotlinx.coroutines.flow.FlowCollector
                    public final Object emit(String str, Continuation<? super Unit> continuation) {
                        NotificationVoiceReplyLogger notificationVoiceReplyLogger = NotificationVoiceReplyController.this.logger;
                        Objects.requireNonNull(notificationVoiceReplyLogger);
                        LogBuffer logBuffer = notificationVoiceReplyLogger.logBuffer;
                        LogLevel logLevel = LogLevel.DEBUG;
                        NotificationVoiceReplyLogger$logStatic$2 notificationVoiceReplyLogger$logStatic$2 = new NotificationVoiceReplyLogger$logStatic$2("Target notification was removed, ending session");
                        Objects.requireNonNull(logBuffer);
                        if (!logBuffer.frozen) {
                            logBuffer.push(logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logStatic$2));
                        }
                        NotificationVoiceReplyController.Connection connection3 = connection2;
                        Objects.requireNonNull(connection3);
                        connection3.stateFlow.setValue(NotificationVoiceReplyController.this.queryInitialState(connection2));
                        return Unit.INSTANCE;
                    }
                };
                this.label = 1;
                Object collect = mutableSharedFlow.collect(new NotificationVoiceReplyController$stateMachine$2$inSession$2$2$invokeSuspend$$inlined$filter$1$2(notificationVoiceReplyController$stateMachine$2$inSession$2$2$invokeSuspend$$inlined$collect$1, voiceReplyTarget), this);
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

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public NotificationVoiceReplyController$stateMachine$2$inSession$2(NotificationVoiceReplyController notificationVoiceReplyController, VoiceReplyTarget voiceReplyTarget, NotificationVoiceReplyController.Connection connection, String str, MutableSharedFlow<Pair<String, RemoteInputViewController>> mutableSharedFlow, Function2<? super Session, ? super Continuation<? super Unit>, ? extends Object> function2, Continuation<? super NotificationVoiceReplyController$stateMachine$2$inSession$2> continuation) {
        super(2, continuation);
        this.this$0 = notificationVoiceReplyController;
        this.$target = voiceReplyTarget;
        this.$this_stateMachine = connection;
        this.$initialContent = str;
        this.$remoteInputViewActivatedForVoiceReply = mutableSharedFlow;
        this.$block = function2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyController$stateMachine$2$inSession$2 notificationVoiceReplyController$stateMachine$2$inSession$2 = new NotificationVoiceReplyController$stateMachine$2$inSession$2(this.this$0, this.$target, this.$this_stateMachine, this.$initialContent, this.$remoteInputViewActivatedForVoiceReply, this.$block, continuation);
        notificationVoiceReplyController$stateMachine$2$inSession$2.L$0 = obj;
        return notificationVoiceReplyController$stateMachine$2$inSession$2;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyController$stateMachine$2$inSession$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0086 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x008f  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00bf  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:13:0x0084 -> B:15:0x0087). Please submit an issue!!! */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r18) {
        /*
            r17 = this;
            r0 = r17
            com.android.systemui.log.LogLevel r1 = com.android.systemui.log.LogLevel.DEBUG
            kotlin.coroutines.intrinsics.CoroutineSingletons r2 = kotlin.coroutines.intrinsics.CoroutineSingletons.COROUTINE_SUSPENDED
            int r3 = r0.label
            r4 = 1
            java.lang.String r5 = "NotifVoiceReply"
            if (r3 == 0) goto L_0x0021
            if (r3 != r4) goto L_0x0019
            java.lang.Object r3 = r0.L$0
            kotlinx.coroutines.channels.ChannelIterator r3 = (kotlinx.coroutines.channels.ChannelIterator) r3
            kotlin.ResultKt.throwOnFailure(r18)
            r6 = r18
            goto L_0x0087
        L_0x0019:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "call to 'resume' before 'invoke' with coroutine"
            r0.<init>(r1)
            throw r0
        L_0x0021:
            kotlin.ResultKt.throwOnFailure(r18)
            java.lang.Object r3 = r0.L$0
            kotlinx.coroutines.CoroutineScope r3 = (kotlinx.coroutines.CoroutineScope) r3
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r6 = r0.this$0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger r6 = r6.logger
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r7 = r0.$target
            java.util.Objects.requireNonNull(r7)
            java.lang.String r7 = r7.notifKey
            java.util.Objects.requireNonNull(r6)
            com.android.systemui.log.LogBuffer r6 = r6.logBuffer
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger$logStateInSession$2 r8 = com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger$logStateInSession$2.INSTANCE
            java.util.Objects.requireNonNull(r6)
            boolean r9 = r6.frozen
            if (r9 != 0) goto L_0x004a
            com.android.systemui.log.LogMessageImpl r8 = r6.obtain(r5, r1, r8)
            r8.str1 = r7
            r6.push(r8)
        L_0x004a:
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$inSession$2$1 r6 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$inSession$2$1
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r10 = r0.$target
            java.lang.String r11 = r0.$initialContent
            kotlinx.coroutines.flow.MutableSharedFlow<kotlin.Pair<java.lang.String, com.android.systemui.statusbar.policy.RemoteInputViewController>> r12 = r0.$remoteInputViewActivatedForVoiceReply
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r13 = r0.$this_stateMachine
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r14 = r0.this$0
            kotlin.jvm.functions.Function2<com.google.android.systemui.statusbar.notification.voicereplies.Session, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> r15 = r0.$block
            r16 = 0
            r9 = r6
            r9.<init>(r10, r11, r12, r13, r14, r15, r16)
            r7 = 0
            r8 = 3
            kotlinx.coroutines.BuildersKt.launch$default(r3, r7, r7, r6, r8)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$inSession$2$2 r6 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$inSession$2$2
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r9 = r0.$this_stateMachine
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r10 = r0.$target
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r11 = r0.this$0
            r6.<init>(r9, r10, r11, r7)
            kotlinx.coroutines.BuildersKt.launch$default(r3, r7, r7, r6, r8)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r3 = r0.$this_stateMachine
            java.util.Objects.requireNonNull(r3)
            kotlinx.coroutines.channels.Channel<com.google.android.systemui.statusbar.notification.voicereplies.StartSessionRequest> r3 = r3.startSessionRequests
            kotlinx.coroutines.channels.AbstractChannel$Itr r3 = r3.iterator()
        L_0x007c:
            r0.L$0 = r3
            r0.label = r4
            java.lang.Object r6 = r3.hasNext(r0)
            if (r6 != r2) goto L_0x0087
            return r2
        L_0x0087:
            java.lang.Boolean r6 = (java.lang.Boolean) r6
            boolean r6 = r6.booleanValue()
            if (r6 == 0) goto L_0x00bf
            java.lang.Object r6 = r3.next()
            com.google.android.systemui.statusbar.notification.voicereplies.StartSessionRequest r6 = (com.google.android.systemui.statusbar.notification.voicereplies.StartSessionRequest) r6
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r7 = r0.this$0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger r7 = r7.logger
            java.util.Objects.requireNonNull(r6)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler r8 = r6.handler
            int r8 = r8.getUserId()
            java.util.Objects.requireNonNull(r7)
            com.android.systemui.log.LogBuffer r7 = r7.logBuffer
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger$logSessionAlreadyInProgress$2 r9 = com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger$logSessionAlreadyInProgress$2.INSTANCE
            java.util.Objects.requireNonNull(r7)
            boolean r10 = r7.frozen
            if (r10 != 0) goto L_0x00b9
            com.android.systemui.log.LogMessageImpl r9 = r7.obtain(r5, r1, r9)
            r9.int1 = r8
            r7.push(r9)
        L_0x00b9:
            kotlin.jvm.functions.Function0<kotlin.Unit> r6 = r6.onFail
            r6.invoke()
            goto L_0x007c
        L_0x00bf:
            kotlin.Unit r0 = kotlin.Unit.INSTANCE
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$inSession$2.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
