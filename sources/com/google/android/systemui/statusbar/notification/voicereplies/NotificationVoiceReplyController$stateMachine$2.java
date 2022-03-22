package com.google.android.systemui.statusbar.notification.voicereplies;

import androidx.mediarouter.R$dimen;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.RemoteInputViewController;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import java.util.Objects;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
import kotlinx.coroutines.flow.MutableSharedFlow;
import kotlinx.coroutines.flow.MutableStateFlow;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2", f = "NotificationVoiceReplyManager.kt", l = {626}, m = "invokeSuspend")
/* loaded from: classes.dex */
public final class NotificationVoiceReplyController$stateMachine$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine;
    private /* synthetic */ Object L$0;
    public int label;
    public final /* synthetic */ NotificationVoiceReplyController this$0;

    /* compiled from: NotificationVoiceReplyManager.kt */
    @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$1", f = "NotificationVoiceReplyManager.kt", l = {431}, m = "invokeSuspend")
    /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        public final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine;
        public int label;
        public final /* synthetic */ NotificationVoiceReplyController this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass1(NotificationVoiceReplyController notificationVoiceReplyController, NotificationVoiceReplyController.Connection connection, Continuation<? super AnonymousClass1> continuation) {
            super(2, continuation);
            this.this$0 = notificationVoiceReplyController;
            this.$this_stateMachine = connection;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass1(this.this$0, this.$this_stateMachine, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Object obj2 = CoroutineSingletons.COROUTINE_SUSPENDED;
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                NotificationVoiceReplyController notificationVoiceReplyController = this.this$0;
                NotificationVoiceReplyController.Connection connection = this.$this_stateMachine;
                this.label = 1;
                Objects.requireNonNull(notificationVoiceReplyController);
                Object coroutineScope = R$dimen.coroutineScope(new NotificationVoiceReplyController$refreshCandidateOnNotifChanges$2(notificationVoiceReplyController, connection, null), this);
                if (coroutineScope != obj2) {
                    coroutineScope = Unit.INSTANCE;
                }
                if (coroutineScope == obj2) {
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

    /* compiled from: NotificationVoiceReplyManager.kt */
    @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$2", f = "NotificationVoiceReplyManager.kt", l = {432}, m = "invokeSuspend")
    /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        public final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine;
        public int label;
        public final /* synthetic */ NotificationVoiceReplyController this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass2(NotificationVoiceReplyController notificationVoiceReplyController, NotificationVoiceReplyController.Connection connection, Continuation<? super AnonymousClass2> continuation) {
            super(2, continuation);
            this.this$0 = notificationVoiceReplyController;
            this.$this_stateMachine = connection;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass2(this.this$0, this.$this_stateMachine, continuation);
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
                NotificationVoiceReplyController notificationVoiceReplyController = this.this$0;
                NotificationVoiceReplyController.Connection connection = this.$this_stateMachine;
                this.label = 1;
                Objects.requireNonNull(notificationVoiceReplyController);
                Objects.requireNonNull(connection);
                final MutableStateFlow<VoiceReplyState> mutableStateFlow = connection.stateFlow;
                Object collectLatest = NotificationVoiceReplyManagerKt.collectLatest(NotificationVoiceReplyManagerKt.distinctUntilChanged(new Flow<VoiceReplyTarget>() { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$$inlined$map$1

                    /* compiled from: Collect.kt */
                    /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$$inlined$map$1$2  reason: invalid class name */
                    /* loaded from: classes.dex */
                    public static final class AnonymousClass2 implements FlowCollector<VoiceReplyState> {
                        public final /* synthetic */ FlowCollector $this_unsafeFlow$inlined;

                        /* compiled from: Collect.kt */
                        @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$$inlined$map$1$2", f = "NotificationVoiceReplyManager.kt", l = {137}, m = "emit")
                        /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$$inlined$map$1$2$1  reason: invalid class name */
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
                                return AnonymousClass2.this.emit(null, this);
                            }
                        }

                        public AnonymousClass2(FlowCollector flowCollector) {
                            this.$this_unsafeFlow$inlined = flowCollector;
                        }

                        /* JADX WARN: Removed duplicated region for block: B:10:0x0021  */
                        /* JADX WARN: Removed duplicated region for block: B:14:0x002f  */
                        @Override // kotlinx.coroutines.flow.FlowCollector
                        /*
                            Code decompiled incorrectly, please refer to instructions dump.
                            To view partially-correct add '--show-bad-code' argument
                        */
                        public final java.lang.Object emit(com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyState r5, kotlin.coroutines.Continuation r6) {
                            /*
                                r4 = this;
                                boolean r0 = r6 instanceof com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$$inlined$map$1.AnonymousClass2.AnonymousClass1
                                if (r0 == 0) goto L_0x0013
                                r0 = r6
                                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$$inlined$map$1$2$1 r0 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$$inlined$map$1.AnonymousClass2.AnonymousClass1) r0
                                int r1 = r0.label
                                r2 = -2147483648(0xffffffff80000000, float:-0.0)
                                r3 = r1 & r2
                                if (r3 == 0) goto L_0x0013
                                int r1 = r1 - r2
                                r0.label = r1
                                goto L_0x0018
                            L_0x0013:
                                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$$inlined$map$1$2$1 r0 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$$inlined$map$1$2$1
                                r0.<init>(r6)
                            L_0x0018:
                                java.lang.Object r6 = r0.result
                                kotlin.coroutines.intrinsics.CoroutineSingletons r1 = kotlin.coroutines.intrinsics.CoroutineSingletons.COROUTINE_SUSPENDED
                                int r2 = r0.label
                                r3 = 1
                                if (r2 == 0) goto L_0x002f
                                if (r2 != r3) goto L_0x0027
                                kotlin.ResultKt.throwOnFailure(r6)
                                goto L_0x004d
                            L_0x0027:
                                java.lang.IllegalStateException r4 = new java.lang.IllegalStateException
                                java.lang.String r5 = "call to 'resume' before 'invoke' with coroutine"
                                r4.<init>(r5)
                                throw r4
                            L_0x002f:
                                kotlin.ResultKt.throwOnFailure(r6)
                                kotlinx.coroutines.flow.FlowCollector r4 = r4.$this_unsafeFlow$inlined
                                com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyState r5 = (com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyState) r5
                                boolean r6 = r5 instanceof com.google.android.systemui.statusbar.notification.voicereplies.HasCandidate
                                r2 = 0
                                if (r6 == 0) goto L_0x003e
                                com.google.android.systemui.statusbar.notification.voicereplies.HasCandidate r5 = (com.google.android.systemui.statusbar.notification.voicereplies.HasCandidate) r5
                                goto L_0x003f
                            L_0x003e:
                                r5 = r2
                            L_0x003f:
                                if (r5 != 0) goto L_0x0042
                                goto L_0x0044
                            L_0x0042:
                                com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r2 = r5.candidate
                            L_0x0044:
                                r0.label = r3
                                java.lang.Object r4 = r4.emit(r2, r0)
                                if (r4 != r1) goto L_0x004d
                                return r1
                            L_0x004d:
                                kotlin.Unit r4 = kotlin.Unit.INSTANCE
                                return r4
                            */
                            throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$$inlined$map$1.AnonymousClass2.emit(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
                        }
                    }

                    @Override // kotlinx.coroutines.flow.Flow
                    public final Object collect(FlowCollector<? super VoiceReplyTarget> flowCollector, Continuation continuation) {
                        Object collect = mutableStateFlow.collect(new AnonymousClass2(flowCollector), continuation);
                        if (collect == CoroutineSingletons.COROUTINE_SUSPENDED) {
                            return collect;
                        }
                        return Unit.INSTANCE;
                    }
                }, NotificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$3.INSTANCE), new NotificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$4(notificationVoiceReplyController, null), this);
                if (collectLatest != obj2) {
                    collectLatest = Unit.INSTANCE;
                }
                if (collectLatest == obj2) {
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

    /* compiled from: NotificationVoiceReplyManager.kt */
    @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$3", f = "NotificationVoiceReplyManager.kt", l = {433}, m = "invokeSuspend")
    /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass3 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        public final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine;
        public int label;
        public final /* synthetic */ NotificationVoiceReplyController this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass3(NotificationVoiceReplyController notificationVoiceReplyController, NotificationVoiceReplyController.Connection connection, Continuation<? super AnonymousClass3> continuation) {
            super(2, continuation);
            this.this$0 = notificationVoiceReplyController;
            this.$this_stateMachine = connection;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass3(this.this$0, this.$this_stateMachine, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass3) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                NotificationVoiceReplyController notificationVoiceReplyController = this.this$0;
                NotificationVoiceReplyController.Connection connection = this.$this_stateMachine;
                this.label = 1;
                NotificationVoiceReplyController.access$resetStateOnUserChange(notificationVoiceReplyController, connection, this);
                return coroutineSingletons;
            } else if (i == 1) {
                ResultKt.throwOnFailure(obj);
                return Unit.INSTANCE;
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* compiled from: NotificationVoiceReplyManager.kt */
    @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$4", f = "NotificationVoiceReplyManager.kt", l = {445}, m = "invokeSuspend")
    /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$4  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass4 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        public final /* synthetic */ Flow<Pair<NotificationEntry, Boolean>> $hunStateChanges;
        public final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine;
        public int label;
        public final /* synthetic */ NotificationVoiceReplyController this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass4(NotificationVoiceReplyController notificationVoiceReplyController, NotificationVoiceReplyController.Connection connection, Flow<Pair<NotificationEntry, Boolean>> flow, Continuation<? super AnonymousClass4> continuation) {
            super(2, continuation);
            this.this$0 = notificationVoiceReplyController;
            this.$this_stateMachine = connection;
            this.$hunStateChanges = flow;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass4(this.this$0, this.$this_stateMachine, this.$hunStateChanges, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass4) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Type inference failed for: r1v3, types: [com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$$inlined$map$2] */
        /* JADX WARN: Type inference failed for: r2v1, types: [com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$$inlined$map$1] */
        /* JADX WARN: Unknown variable types count: 2 */
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
                goto L_0x0054
            L_0x000d:
                java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
                java.lang.String r7 = "call to 'resume' before 'invoke' with coroutine"
                r6.<init>(r7)
                throw r6
            L_0x0015:
                kotlin.ResultKt.throwOnFailure(r7)
                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r7 = r6.this$0
                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r1 = r6.$this_stateMachine
                kotlinx.coroutines.flow.Flow<kotlin.Pair<com.android.systemui.statusbar.notification.collection.NotificationEntry, java.lang.Boolean>> r3 = r6.$hunStateChanges
                r6.label = r2
                java.util.Objects.requireNonNull(r7)
                java.util.Objects.requireNonNull(r1)
                kotlinx.coroutines.flow.MutableStateFlow<com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyState> r1 = r1.stateFlow
                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$$inlined$map$1 r2 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$$inlined$map$1
                r2.<init>()
                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$$inlined$map$2 r1 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$$inlined$map$2
                r1.<init>()
                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$4 r3 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$4
                r4 = 0
                r3.<init>(r7, r4)
                kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$unsafeFlow$1 r5 = new kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$unsafeFlow$1
                r5.<init>()
                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$5 r1 = com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$5.INSTANCE
                kotlinx.coroutines.flow.SafeFlow r1 = com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt.distinctUntilChanged(r5, r1)
                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$6 r2 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$6
                r2.<init>(r7, r4)
                java.lang.Object r6 = com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt.collectLatest(r1, r2, r6)
                if (r6 != r0) goto L_0x004f
                goto L_0x0051
            L_0x004f:
                kotlin.Unit r6 = kotlin.Unit.INSTANCE
            L_0x0051:
                if (r6 != r0) goto L_0x0054
                return r0
            L_0x0054:
                kotlin.Unit r6 = kotlin.Unit.INSTANCE
                return r6
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2.AnonymousClass4.invokeSuspend(java.lang.Object):java.lang.Object");
        }
    }

    /* compiled from: NotificationVoiceReplyManager.kt */
    @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$5", f = "NotificationVoiceReplyManager.kt", l = {457}, m = "invokeSuspend")
    /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$5  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass5 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        public final /* synthetic */ MutableSharedFlow<Pair<String, RemoteInputViewController>> $remoteInputViewActivatedForVoiceReply;
        public final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine;
        public int label;
        public final /* synthetic */ NotificationVoiceReplyController this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass5(NotificationVoiceReplyController notificationVoiceReplyController, NotificationVoiceReplyController.Connection connection, MutableSharedFlow<Pair<String, RemoteInputViewController>> mutableSharedFlow, Continuation<? super AnonymousClass5> continuation) {
            super(2, continuation);
            this.this$0 = notificationVoiceReplyController;
            this.$this_stateMachine = connection;
            this.$remoteInputViewActivatedForVoiceReply = mutableSharedFlow;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass5(this.this$0, this.$this_stateMachine, this.$remoteInputViewActivatedForVoiceReply, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass5) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Object obj2 = CoroutineSingletons.COROUTINE_SUSPENDED;
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                NotificationVoiceReplyController notificationVoiceReplyController = this.this$0;
                NotificationVoiceReplyController.Connection connection = this.$this_stateMachine;
                MutableSharedFlow<Pair<String, RemoteInputViewController>> mutableSharedFlow = this.$remoteInputViewActivatedForVoiceReply;
                this.label = 1;
                Objects.requireNonNull(notificationVoiceReplyController);
                Object coroutineScope = R$dimen.coroutineScope(new NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2(mutableSharedFlow, connection, notificationVoiceReplyController, null), this);
                if (coroutineScope != obj2) {
                    coroutineScope = Unit.INSTANCE;
                }
                if (coroutineScope == obj2) {
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

    /* compiled from: NotificationVoiceReplyManager.kt */
    @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$6", f = "NotificationVoiceReplyManager.kt", l = {628, 629, 630}, m = "invokeSuspend")
    /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$6  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass6 extends SuspendLambda implements Function2<VoiceReplyState, Continuation<? super Unit>, Object> {
        public final /* synthetic */ Flow<Pair<NotificationEntry, Boolean>> $hunStateChanges;
        public final /* synthetic */ Flow<NotificationEntry> $reinflations;
        public final /* synthetic */ MutableSharedFlow<Pair<String, RemoteInputViewController>> $remoteInputViewActivatedForVoiceReply;
        public final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine;
        public /* synthetic */ Object L$0;
        public int label;
        public final /* synthetic */ NotificationVoiceReplyController this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass6(NotificationVoiceReplyController notificationVoiceReplyController, Flow<NotificationEntry> flow, NotificationVoiceReplyController.Connection connection, Flow<Pair<NotificationEntry, Boolean>> flow2, MutableSharedFlow<Pair<String, RemoteInputViewController>> mutableSharedFlow, Continuation<? super AnonymousClass6> continuation) {
            super(2, continuation);
            this.this$0 = notificationVoiceReplyController;
            this.$reinflations = flow;
            this.$this_stateMachine = connection;
            this.$hunStateChanges = flow2;
            this.$remoteInputViewActivatedForVoiceReply = mutableSharedFlow;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            AnonymousClass6 r7 = new AnonymousClass6(this.this$0, this.$reinflations, this.$this_stateMachine, this.$hunStateChanges, this.$remoteInputViewActivatedForVoiceReply, continuation);
            r7.L$0 = obj;
            return r7;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(VoiceReplyState voiceReplyState, Continuation<? super Unit> continuation) {
            return ((AnonymousClass6) create(voiceReplyState, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Object obj2 = CoroutineSingletons.COROUTINE_SUSPENDED;
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                VoiceReplyState voiceReplyState = (VoiceReplyState) this.L$0;
                if (voiceReplyState instanceof NoCandidate) {
                    NotificationVoiceReplyController notificationVoiceReplyController = this.this$0;
                    Flow<NotificationEntry> flow = this.$reinflations;
                    NotificationVoiceReplyController.Connection connection = this.$this_stateMachine;
                    this.label = 1;
                    Object coroutineScope = R$dimen.coroutineScope(new NotificationVoiceReplyController$stateMachine$2$noCandidate$2(notificationVoiceReplyController, flow, connection, null), this);
                    if (coroutineScope != obj2) {
                        coroutineScope = Unit.INSTANCE;
                    }
                    if (coroutineScope == obj2) {
                        return obj2;
                    }
                } else if (voiceReplyState instanceof HasCandidate) {
                    NotificationVoiceReplyController.Connection connection2 = this.$this_stateMachine;
                    NotificationVoiceReplyController notificationVoiceReplyController2 = this.this$0;
                    Flow<Pair<NotificationEntry, Boolean>> flow2 = this.$hunStateChanges;
                    Flow<NotificationEntry> flow3 = this.$reinflations;
                    HasCandidate hasCandidate = (HasCandidate) voiceReplyState;
                    Objects.requireNonNull(hasCandidate);
                    VoiceReplyTarget voiceReplyTarget = hasCandidate.candidate;
                    this.label = 2;
                    Object coroutineScope2 = R$dimen.coroutineScope(new NotificationVoiceReplyController$stateMachine$2$hasCandidate$2(connection2, voiceReplyTarget, notificationVoiceReplyController2, flow2, flow3, null), this);
                    if (coroutineScope2 != obj2) {
                        coroutineScope2 = Unit.INSTANCE;
                    }
                    if (coroutineScope2 == obj2) {
                        return obj2;
                    }
                } else if (voiceReplyState instanceof InSession) {
                    NotificationVoiceReplyController notificationVoiceReplyController3 = this.this$0;
                    NotificationVoiceReplyController.Connection connection3 = this.$this_stateMachine;
                    MutableSharedFlow<Pair<String, RemoteInputViewController>> mutableSharedFlow = this.$remoteInputViewActivatedForVoiceReply;
                    InSession inSession = (InSession) voiceReplyState;
                    Objects.requireNonNull(inSession);
                    Function2<Session, Continuation<? super Unit>, Object> function2 = inSession.block;
                    Objects.requireNonNull(inSession);
                    String str = inSession.initialContent;
                    Objects.requireNonNull(inSession);
                    VoiceReplyTarget voiceReplyTarget2 = inSession.target;
                    this.label = 3;
                    Object coroutineScope3 = R$dimen.coroutineScope(new NotificationVoiceReplyController$stateMachine$2$inSession$2(notificationVoiceReplyController3, voiceReplyTarget2, connection3, str, mutableSharedFlow, function2, null), this);
                    if (coroutineScope3 != obj2) {
                        coroutineScope3 = Unit.INSTANCE;
                    }
                    if (coroutineScope3 == obj2) {
                        return obj2;
                    }
                }
            } else if (i == 1 || i == 2 || i == 3) {
                ResultKt.throwOnFailure(obj);
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$stateMachine$2(NotificationVoiceReplyController.Connection connection, NotificationVoiceReplyController notificationVoiceReplyController, Continuation<? super NotificationVoiceReplyController$stateMachine$2> continuation) {
        super(2, continuation);
        this.$this_stateMachine = connection;
        this.this$0 = notificationVoiceReplyController;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyController$stateMachine$2 notificationVoiceReplyController$stateMachine$2 = new NotificationVoiceReplyController$stateMachine$2(this.$this_stateMachine, this.this$0, continuation);
        notificationVoiceReplyController$stateMachine$2.L$0 = obj;
        return notificationVoiceReplyController$stateMachine$2;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyController$stateMachine$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Type inference failed for: r6v3, types: [kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r15) {
        /*
            r14 = this;
            kotlin.coroutines.intrinsics.CoroutineSingletons r0 = kotlin.coroutines.intrinsics.CoroutineSingletons.COROUTINE_SUSPENDED
            int r1 = r14.label
            r2 = 1
            if (r1 == 0) goto L_0x0016
            if (r1 != r2) goto L_0x000e
            kotlin.ResultKt.throwOnFailure(r15)
            goto L_0x00b2
        L_0x000e:
            java.lang.IllegalStateException r14 = new java.lang.IllegalStateException
            java.lang.String r15 = "call to 'resume' before 'invoke' with coroutine"
            r14.<init>(r15)
            throw r14
        L_0x0016:
            kotlin.ResultKt.throwOnFailure(r15)
            java.lang.Object r15 = r14.L$0
            kotlinx.coroutines.CoroutineScope r15 = (kotlinx.coroutines.CoroutineScope) r15
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$1 r1 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$1
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r3 = r14.this$0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r4 = r14.$this_stateMachine
            r5 = 0
            r1.<init>(r3, r4, r5)
            r3 = 3
            kotlinx.coroutines.BuildersKt.launch$default(r15, r5, r5, r1, r3)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$2 r1 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$2
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r4 = r14.this$0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r6 = r14.$this_stateMachine
            r1.<init>(r4, r6, r5)
            kotlinx.coroutines.BuildersKt.launch$default(r15, r5, r5, r1, r3)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$3 r1 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$3
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r4 = r14.this$0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r6 = r14.$this_stateMachine
            r1.<init>(r4, r6, r5)
            kotlinx.coroutines.BuildersKt.launch$default(r15, r5, r5, r1, r3)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r1 = r14.$this_stateMachine
            java.util.Objects.requireNonNull(r1)
            kotlinx.coroutines.flow.MutableSharedFlow<kotlin.Pair<com.android.systemui.statusbar.notification.collection.NotificationEntry, java.lang.String>> r1 = r1.entryReinflations
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$reinflations$1 r4 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$reinflations$1
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r6 = r14.this$0
            r4.<init>(r6, r5)
            kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1 r6 = new kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1
            r6.<init>()
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$invokeSuspend$$inlined$map$1 r9 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$invokeSuspend$$inlined$map$1
            r9.<init>()
            r1 = 0
            r4 = 7
            kotlinx.coroutines.flow.SharedFlowImpl r11 = kotlinx.coroutines.flow.SharedFlowKt.MutableSharedFlow$default(r1, r4)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r6 = r14.this$0
            r7 = 2147483647(0x7fffffff, float:NaN)
            r8 = 6
            kotlinx.coroutines.channels.AbstractChannel r7 = com.android.systemui.R$anim.Channel$default(r7, r8)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hunStateChanges$1$1 r8 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hunStateChanges$1$1
            r8.<init>(r6, r7, r5)
            kotlinx.coroutines.BuildersKt.launch$default(r15, r5, r5, r8, r3)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hunStateChanges$1$2 r6 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hunStateChanges$1$2
            r6.<init>(r7, r11, r5)
            kotlinx.coroutines.BuildersKt.launch$default(r15, r5, r5, r6, r3)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$4 r6 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$4
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r7 = r14.this$0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r8 = r14.$this_stateMachine
            r6.<init>(r7, r8, r11, r5)
            kotlinx.coroutines.BuildersKt.launch$default(r15, r5, r5, r6, r3)
            kotlinx.coroutines.flow.SharedFlowImpl r12 = kotlinx.coroutines.flow.SharedFlowKt.MutableSharedFlow$default(r1, r4)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$5 r1 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$5
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r4 = r14.this$0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r6 = r14.$this_stateMachine
            r1.<init>(r4, r6, r12, r5)
            kotlinx.coroutines.BuildersKt.launch$default(r15, r5, r5, r1, r3)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r15 = r14.$this_stateMachine
            java.util.Objects.requireNonNull(r15)
            kotlinx.coroutines.flow.MutableStateFlow<com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyState> r15 = r15.stateFlow
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$6 r1 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$6
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r8 = r14.this$0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r10 = r14.$this_stateMachine
            r13 = 0
            r7 = r1
            r7.<init>(r8, r9, r10, r11, r12, r13)
            r14.label = r2
            java.lang.Object r14 = com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt.collectLatest(r15, r1, r14)
            if (r14 != r0) goto L_0x00b2
            return r0
        L_0x00b2:
            kotlin.Unit r14 = kotlin.Unit.INSTANCE
            return r14
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
