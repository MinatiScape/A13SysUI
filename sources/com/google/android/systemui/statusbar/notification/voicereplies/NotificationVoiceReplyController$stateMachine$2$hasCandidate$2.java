package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.R$anim;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.channels.AbstractChannel;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
import kotlinx.coroutines.flow.MutableSharedFlow;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.SharedFlowImpl;
import kotlinx.coroutines.flow.SharedFlowKt;
import kotlinx.coroutines.flow.StateFlowImpl;
import kotlinx.coroutines.flow.internal.CombineKt;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2", f = "NotificationVoiceReplyManager.kt", l = {574}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class NotificationVoiceReplyController$stateMachine$2$hasCandidate$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ VoiceReplyTarget $candidate;
    public final /* synthetic */ Flow<Pair<NotificationEntry, Boolean>> $hunStateChanges;
    public final /* synthetic */ Flow<NotificationEntry> $reinflations;
    public final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine;
    private /* synthetic */ Object L$0;
    public int label;
    public final /* synthetic */ NotificationVoiceReplyController this$0;

    /* compiled from: NotificationVoiceReplyManager.kt */
    @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$1", f = "NotificationVoiceReplyManager.kt", l = {504}, m = "invokeSuspend")
    /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        public final /* synthetic */ VoiceReplyTarget $candidate;
        public final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine;
        private /* synthetic */ Object L$0;
        public int label;
        public final /* synthetic */ NotificationVoiceReplyController this$0;

        /* compiled from: NotificationVoiceReplyManager.kt */
        @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$1", f = "NotificationVoiceReplyManager.kt", l = {1180}, m = "invokeSuspend")
        /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$1  reason: invalid class name and collision with other inner class name */
        /* loaded from: classes.dex */
        public static final class C00121 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
            public final /* synthetic */ VoiceReplyTarget $candidate;
            public final /* synthetic */ MutableStateFlow<CtaState> $ctaStateFlow;
            public int label;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public C00121(VoiceReplyTarget voiceReplyTarget, MutableStateFlow<CtaState> mutableStateFlow, Continuation<? super C00121> continuation) {
                super(2, continuation);
                this.$candidate = voiceReplyTarget;
                this.$ctaStateFlow = mutableStateFlow;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
                return new C00121(this.$candidate, this.$ctaStateFlow, continuation);
            }

            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
                return ((C00121) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object obj) {
                Object obj2 = CoroutineSingletons.COROUTINE_SUSPENDED;
                int i = this.label;
                if (i == 0) {
                    ResultKt.throwOnFailure(obj);
                    VoiceReplyTarget voiceReplyTarget = this.$candidate;
                    Objects.requireNonNull(voiceReplyTarget);
                    List<NotificationVoiceReplyHandler> list = voiceReplyTarget.handlers;
                    ArrayList arrayList = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(list, 10));
                    for (NotificationVoiceReplyHandler notificationVoiceReplyHandler : list) {
                        arrayList.add(notificationVoiceReplyHandler.getShowCta());
                    }
                    Object[] array = CollectionsKt___CollectionsKt.toList(arrayList).toArray(new Flow[0]);
                    Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
                    Flow[] flowArr = (Flow[]) array;
                    final MutableStateFlow<CtaState> mutableStateFlow = this.$ctaStateFlow;
                    FlowCollector<CtaState> notificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$1$invokeSuspend$$inlined$collect$1 = new FlowCollector<CtaState>() { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$1$invokeSuspend$$inlined$collect$1
                        @Override // kotlinx.coroutines.flow.FlowCollector
                        public final Object emit(CtaState ctaState, Continuation<? super Unit> continuation) {
                            MutableStateFlow.this.setValue(ctaState);
                            return Unit.INSTANCE;
                        }
                    };
                    this.label = 1;
                    Object combineInternal = CombineKt.combineInternal(notificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$1$invokeSuspend$$inlined$collect$1, flowArr, new NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$1$invokeSuspend$$inlined$combine$1$2(flowArr), new NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$1$invokeSuspend$$inlined$combine$1$3(null), this);
                    if (combineInternal != obj2) {
                        combineInternal = Unit.INSTANCE;
                    }
                    if (combineInternal == obj2) {
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
        @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$2", f = "NotificationVoiceReplyManager.kt", l = {1169}, m = "invokeSuspend")
        /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$2  reason: invalid class name */
        /* loaded from: classes.dex */
        public static final class AnonymousClass2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
            public final /* synthetic */ MutableStateFlow<CtaState> $ctaStateFlow;
            public final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine;
            public int label;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public AnonymousClass2(NotificationVoiceReplyController.Connection connection, MutableStateFlow<CtaState> mutableStateFlow, Continuation<? super AnonymousClass2> continuation) {
                super(2, continuation);
                this.$this_stateMachine = connection;
                this.$ctaStateFlow = mutableStateFlow;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
                return new AnonymousClass2(this.$this_stateMachine, this.$ctaStateFlow, continuation);
            }

            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
                return ((AnonymousClass2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object obj) {
                CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
                int i = this.label;
                if (i == 0) {
                    ResultKt.throwOnFailure(obj);
                    NotificationVoiceReplyController.Connection connection = this.$this_stateMachine;
                    Objects.requireNonNull(connection);
                    MutableSharedFlow<Unit> mutableSharedFlow = connection.hideVisibleQuickPhraseCtaRequests;
                    final MutableStateFlow<CtaState> mutableStateFlow = this.$ctaStateFlow;
                    FlowCollector<Unit> notificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$2$invokeSuspend$$inlined$collect$1 = new FlowCollector<Unit>() { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$2$invokeSuspend$$inlined$collect$1
                        @Override // kotlinx.coroutines.flow.FlowCollector
                        public final Object emit(Unit unit, Continuation<? super Unit> continuation) {
                            if (MutableStateFlow.this.getValue() == CtaState.QUICK_PHRASE) {
                                MutableStateFlow.this.setValue(CtaState.HOTWORD);
                            }
                            return Unit.INSTANCE;
                        }
                    };
                    this.label = 1;
                    if (mutableSharedFlow.collect(notificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$2$invokeSuspend$$inlined$collect$1, this) == coroutineSingletons) {
                        return coroutineSingletons;
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
        @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$3", f = "NotificationVoiceReplyManager.kt", l = {1172, 1195}, m = "invokeSuspend")
        /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$3  reason: invalid class name */
        /* loaded from: classes.dex */
        public static final class AnonymousClass3 extends SuspendLambda implements Function2<CtaState, Continuation<? super Unit>, Object> {
            public final /* synthetic */ CoroutineScope $$this$launch;
            public final /* synthetic */ VoiceReplyTarget $candidate;
            public /* synthetic */ Object L$0;
            public Object L$1;
            public int label;
            public final /* synthetic */ NotificationVoiceReplyController this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public AnonymousClass3(NotificationVoiceReplyController notificationVoiceReplyController, VoiceReplyTarget voiceReplyTarget, CoroutineScope coroutineScope, Continuation<? super AnonymousClass3> continuation) {
                super(2, continuation);
                this.this$0 = notificationVoiceReplyController;
                this.$candidate = voiceReplyTarget;
                this.$$this$launch = coroutineScope;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
                AnonymousClass3 r0 = new AnonymousClass3(this.this$0, this.$candidate, this.$$this$launch, continuation);
                r0.L$0 = obj;
                return r0;
            }

            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(CtaState ctaState, Continuation<? super Unit> continuation) {
                return ((AnonymousClass3) create(ctaState, continuation)).invokeSuspend(Unit.INSTANCE);
            }

            /* JADX WARN: Removed duplicated region for block: B:42:0x0107  */
            /* JADX WARN: Removed duplicated region for block: B:68:0x01c3  */
            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public final java.lang.Object invokeSuspend(java.lang.Object r13) {
                /*
                    Method dump skipped, instructions count: 498
                    To view this dump add '--comments-level debug' option
                */
                throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2.AnonymousClass1.AnonymousClass3.invokeSuspend(java.lang.Object):java.lang.Object");
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass1(VoiceReplyTarget voiceReplyTarget, NotificationVoiceReplyController.Connection connection, NotificationVoiceReplyController notificationVoiceReplyController, Continuation<? super AnonymousClass1> continuation) {
            super(2, continuation);
            this.$candidate = voiceReplyTarget;
            this.$this_stateMachine = connection;
            this.this$0 = notificationVoiceReplyController;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            AnonymousClass1 r0 = new AnonymousClass1(this.$candidate, this.$this_stateMachine, this.this$0, continuation);
            r0.L$0 = obj;
            return r0;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Comparable comparable;
            CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                CoroutineScope coroutineScope = (CoroutineScope) this.L$0;
                VoiceReplyTarget voiceReplyTarget = this.$candidate;
                Objects.requireNonNull(voiceReplyTarget);
                List<NotificationVoiceReplyHandler> list = voiceReplyTarget.handlers;
                ArrayList arrayList = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(list, 10));
                for (NotificationVoiceReplyHandler notificationVoiceReplyHandler : list) {
                    arrayList.add(notificationVoiceReplyHandler.getShowCta().getValue());
                }
                Iterator it = arrayList.iterator();
                if (!it.hasNext()) {
                    comparable = null;
                } else {
                    comparable = (Comparable) it.next();
                    while (it.hasNext()) {
                        Comparable comparable2 = (Comparable) it.next();
                        if (comparable.compareTo(comparable2) < 0) {
                            comparable = comparable2;
                        }
                    }
                }
                CtaState ctaState = (CtaState) comparable;
                if (ctaState == null) {
                    ctaState = CtaState.NONE;
                }
                StateFlowImpl stateFlowImpl = new StateFlowImpl(ctaState);
                BuildersKt.launch$default(coroutineScope, null, null, new C00121(this.$candidate, stateFlowImpl, null), 3);
                BuildersKt.launch$default(coroutineScope, null, null, new AnonymousClass2(this.$this_stateMachine, stateFlowImpl, null), 3);
                AnonymousClass3 r3 = new AnonymousClass3(this.this$0, this.$candidate, coroutineScope, null);
                this.label = 1;
                if (NotificationVoiceReplyManagerKt.collectLatest(stateFlowImpl, r3, this) == coroutineSingletons) {
                    return coroutineSingletons;
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
    @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2", f = "NotificationVoiceReplyManager.kt", l = {1179}, m = "invokeSuspend")
    /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        public final /* synthetic */ VoiceReplyTarget $candidate;
        public final /* synthetic */ Flow<Pair<NotificationEntry, Boolean>> $hunStateChanges;
        public final /* synthetic */ Flow<NotificationEntry> $reinflations;
        public final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine;
        private /* synthetic */ Object L$0;
        public int label;
        public final /* synthetic */ NotificationVoiceReplyController this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass2(Flow<Pair<NotificationEntry, Boolean>> flow, VoiceReplyTarget voiceReplyTarget, Flow<NotificationEntry> flow2, NotificationVoiceReplyController notificationVoiceReplyController, NotificationVoiceReplyController.Connection connection, Continuation<? super AnonymousClass2> continuation) {
            super(2, continuation);
            this.$hunStateChanges = flow;
            this.$candidate = voiceReplyTarget;
            this.$reinflations = flow2;
            this.this$0 = notificationVoiceReplyController;
            this.$this_stateMachine = connection;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            AnonymousClass2 r7 = new AnonymousClass2(this.$hunStateChanges, this.$candidate, this.$reinflations, this.this$0, this.$this_stateMachine, continuation);
            r7.L$0 = obj;
            return r7;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                CoroutineScope coroutineScope = (CoroutineScope) this.L$0;
                final Flow<Pair<NotificationEntry, Boolean>> flow = this.$hunStateChanges;
                final VoiceReplyTarget voiceReplyTarget = this.$candidate;
                Flow<NotificationEntry> notificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$1 = new Flow<NotificationEntry>() { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$1

                    /* compiled from: Collect.kt */
                    /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$1$2  reason: invalid class name */
                    /* loaded from: classes.dex */
                    public static final class AnonymousClass2 implements FlowCollector<Pair<? extends NotificationEntry, ? extends Boolean>> {
                        public final /* synthetic */ VoiceReplyTarget $candidate$inlined;
                        public final /* synthetic */ FlowCollector $this_unsafeFlow$inlined;

                        /* compiled from: Collect.kt */
                        @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$1$2", f = "NotificationVoiceReplyManager.kt", l = {140}, m = "emit")
                        /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$1$2$1  reason: invalid class name */
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

                        public AnonymousClass2(FlowCollector flowCollector, VoiceReplyTarget voiceReplyTarget) {
                            this.$this_unsafeFlow$inlined = flowCollector;
                            this.$candidate$inlined = voiceReplyTarget;
                        }

                        /* JADX WARN: Removed duplicated region for block: B:10:0x0021  */
                        /* JADX WARN: Removed duplicated region for block: B:14:0x002f  */
                        @Override // kotlinx.coroutines.flow.FlowCollector
                        /*
                            Code decompiled incorrectly, please refer to instructions dump.
                            To view partially-correct add '--show-bad-code' argument
                        */
                        public final java.lang.Object emit(kotlin.Pair<? extends com.android.systemui.statusbar.notification.collection.NotificationEntry, ? extends java.lang.Boolean> r5, kotlin.coroutines.Continuation r6) {
                            /*
                                r4 = this;
                                boolean r0 = r6 instanceof com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$1.AnonymousClass2.AnonymousClass1
                                if (r0 == 0) goto L_0x0013
                                r0 = r6
                                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$1$2$1 r0 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$1.AnonymousClass2.AnonymousClass1) r0
                                int r1 = r0.label
                                r2 = -2147483648(0xffffffff80000000, float:-0.0)
                                r3 = r1 & r2
                                if (r3 == 0) goto L_0x0013
                                int r1 = r1 - r2
                                r0.label = r1
                                goto L_0x0018
                            L_0x0013:
                                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$1$2$1 r0 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$1$2$1
                                r0.<init>(r6)
                            L_0x0018:
                                java.lang.Object r6 = r0.result
                                kotlin.coroutines.intrinsics.CoroutineSingletons r1 = kotlin.coroutines.intrinsics.CoroutineSingletons.COROUTINE_SUSPENDED
                                int r2 = r0.label
                                r3 = 1
                                if (r2 == 0) goto L_0x002f
                                if (r2 != r3) goto L_0x0027
                                kotlin.ResultKt.throwOnFailure(r6)
                                goto L_0x005c
                            L_0x0027:
                                java.lang.IllegalStateException r4 = new java.lang.IllegalStateException
                                java.lang.String r5 = "call to 'resume' before 'invoke' with coroutine"
                                r4.<init>(r5)
                                throw r4
                            L_0x002f:
                                kotlin.ResultKt.throwOnFailure(r6)
                                kotlinx.coroutines.flow.FlowCollector r6 = r4.$this_unsafeFlow$inlined
                                kotlin.Pair r5 = (kotlin.Pair) r5
                                java.lang.Object r5 = r5.component1()
                                com.android.systemui.statusbar.notification.collection.NotificationEntry r5 = (com.android.systemui.statusbar.notification.collection.NotificationEntry) r5
                                java.util.Objects.requireNonNull(r5)
                                java.lang.String r2 = r5.mKey
                                com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r4 = r4.$candidate$inlined
                                java.util.Objects.requireNonNull(r4)
                                java.lang.String r4 = r4.notifKey
                                boolean r4 = kotlin.jvm.internal.Intrinsics.areEqual(r2, r4)
                                if (r4 == 0) goto L_0x004f
                                goto L_0x0050
                            L_0x004f:
                                r5 = 0
                            L_0x0050:
                                if (r5 != 0) goto L_0x0053
                                goto L_0x005c
                            L_0x0053:
                                r0.label = r3
                                java.lang.Object r4 = r6.emit(r5, r0)
                                if (r4 != r1) goto L_0x005c
                                return r1
                            L_0x005c:
                                kotlin.Unit r4 = kotlin.Unit.INSTANCE
                                return r4
                            */
                            throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$1.AnonymousClass2.emit(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
                        }
                    }

                    @Override // kotlinx.coroutines.flow.Flow
                    public final Object collect(FlowCollector<? super NotificationEntry> flowCollector, Continuation continuation) {
                        Object collect = Flow.this.collect(new AnonymousClass2(flowCollector, voiceReplyTarget), continuation);
                        if (collect == CoroutineSingletons.COROUTINE_SUSPENDED) {
                            return collect;
                        }
                        return Unit.INSTANCE;
                    }
                };
                SharedFlowImpl MutableSharedFlow$default = SharedFlowKt.MutableSharedFlow$default(0, 7);
                Flow<NotificationEntry> flow2 = this.$reinflations;
                AbstractChannel Channel$default = R$anim.Channel$default(0, 7);
                BuildersKt.launch$default(coroutineScope, null, null, new NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$newCandidateEvents$1$1(Channel$default, flow2, notificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$1, null), 3);
                BuildersKt.launch$default(coroutineScope, null, null, new NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$newCandidateEvents$1$2(Channel$default, MutableSharedFlow$default, null), 3);
                NotificationVoiceReplyController notificationVoiceReplyController = this.this$0;
                final NotificationVoiceReplyController.Connection connection = this.$this_stateMachine;
                VoiceReplyTarget voiceReplyTarget2 = this.$candidate;
                FlowCollector<VoiceReplyState> notificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$collect$1 = new FlowCollector<VoiceReplyState>() { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$collect$1
                    @Override // kotlinx.coroutines.flow.FlowCollector
                    public final Object emit(VoiceReplyState voiceReplyState, Continuation<? super Unit> continuation) {
                        NotificationVoiceReplyController.Connection connection2 = NotificationVoiceReplyController.Connection.this;
                        Objects.requireNonNull(connection2);
                        connection2.stateFlow.setValue(voiceReplyState);
                        return Unit.INSTANCE;
                    }
                };
                this.label = 1;
                MutableSharedFlow$default.collect(new NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$2$2(notificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$collect$1, notificationVoiceReplyController, connection, voiceReplyTarget2), this);
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
    @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$3", f = "NotificationVoiceReplyManager.kt", l = {1174}, m = "invokeSuspend")
    /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass3 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        public final /* synthetic */ VoiceReplyTarget $candidate;
        public final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine;
        public int label;
        public final /* synthetic */ NotificationVoiceReplyController this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass3(NotificationVoiceReplyController.Connection connection, VoiceReplyTarget voiceReplyTarget, NotificationVoiceReplyController notificationVoiceReplyController, Continuation<? super AnonymousClass3> continuation) {
            super(2, continuation);
            this.$this_stateMachine = connection;
            this.$candidate = voiceReplyTarget;
            this.this$0 = notificationVoiceReplyController;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass3(this.$this_stateMachine, this.$candidate, this.this$0, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass3) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
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
                VoiceReplyTarget voiceReplyTarget = this.$candidate;
                final NotificationVoiceReplyController notificationVoiceReplyController = this.this$0;
                final NotificationVoiceReplyController.Connection connection2 = this.$this_stateMachine;
                FlowCollector<String> notificationVoiceReplyController$stateMachine$2$hasCandidate$2$3$invokeSuspend$$inlined$collect$1 = new FlowCollector<String>() { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$3$invokeSuspend$$inlined$collect$1
                    @Override // kotlinx.coroutines.flow.FlowCollector
                    public final Object emit(String str, Continuation<? super Unit> continuation) {
                        NotificationVoiceReplyLogger notificationVoiceReplyLogger = NotificationVoiceReplyController.this.logger;
                        Objects.requireNonNull(notificationVoiceReplyLogger);
                        LogBuffer logBuffer = notificationVoiceReplyLogger.logBuffer;
                        LogLevel logLevel = LogLevel.DEBUG;
                        NotificationVoiceReplyLogger$logStatic$2 notificationVoiceReplyLogger$logStatic$2 = new NotificationVoiceReplyLogger$logStatic$2("Candidate notification was removed");
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
                Object collect = mutableSharedFlow.collect(new NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$3$invokeSuspend$$inlined$filter$1$2(notificationVoiceReplyController$stateMachine$2$hasCandidate$2$3$invokeSuspend$$inlined$collect$1, voiceReplyTarget), this);
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
    public NotificationVoiceReplyController$stateMachine$2$hasCandidate$2(NotificationVoiceReplyController.Connection connection, VoiceReplyTarget voiceReplyTarget, NotificationVoiceReplyController notificationVoiceReplyController, Flow<Pair<NotificationEntry, Boolean>> flow, Flow<NotificationEntry> flow2, Continuation<? super NotificationVoiceReplyController$stateMachine$2$hasCandidate$2> continuation) {
        super(2, continuation);
        this.$this_stateMachine = connection;
        this.$candidate = voiceReplyTarget;
        this.this$0 = notificationVoiceReplyController;
        this.$hunStateChanges = flow;
        this.$reinflations = flow2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyController$stateMachine$2$hasCandidate$2 notificationVoiceReplyController$stateMachine$2$hasCandidate$2 = new NotificationVoiceReplyController$stateMachine$2$hasCandidate$2(this.$this_stateMachine, this.$candidate, this.this$0, this.$hunStateChanges, this.$reinflations, continuation);
        notificationVoiceReplyController$stateMachine$2$hasCandidate$2.L$0 = obj;
        return notificationVoiceReplyController$stateMachine$2$hasCandidate$2;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyController$stateMachine$2$hasCandidate$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0068 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0071  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00d3  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:10:0x0066 -> B:12:0x0069). Please submit an issue!!! */
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
            if (r1 == 0) goto L_0x0019
            if (r1 != r2) goto L_0x0011
            java.lang.Object r1 = r14.L$0
            kotlinx.coroutines.channels.ChannelIterator r1 = (kotlinx.coroutines.channels.ChannelIterator) r1
            kotlin.ResultKt.throwOnFailure(r15)
            goto L_0x0069
        L_0x0011:
            java.lang.IllegalStateException r14 = new java.lang.IllegalStateException
            java.lang.String r15 = "call to 'resume' before 'invoke' with coroutine"
            r14.<init>(r15)
            throw r14
        L_0x0019:
            kotlin.ResultKt.throwOnFailure(r15)
            java.lang.Object r15 = r14.L$0
            kotlinx.coroutines.CoroutineScope r15 = (kotlinx.coroutines.CoroutineScope) r15
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$1 r1 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$1
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r3 = r14.$candidate
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r4 = r14.$this_stateMachine
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r5 = r14.this$0
            r6 = 0
            r1.<init>(r3, r4, r5, r6)
            r3 = 3
            kotlinx.coroutines.BuildersKt.launch$default(r15, r6, r6, r1, r3)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2 r1 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2
            kotlinx.coroutines.flow.Flow<kotlin.Pair<com.android.systemui.statusbar.notification.collection.NotificationEntry, java.lang.Boolean>> r8 = r14.$hunStateChanges
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r9 = r14.$candidate
            kotlinx.coroutines.flow.Flow<com.android.systemui.statusbar.notification.collection.NotificationEntry> r10 = r14.$reinflations
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r11 = r14.this$0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r12 = r14.$this_stateMachine
            r13 = 0
            r7 = r1
            r7.<init>(r8, r9, r10, r11, r12, r13)
            kotlinx.coroutines.BuildersKt.launch$default(r15, r6, r6, r1, r3)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$3 r1 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$3
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r4 = r14.$this_stateMachine
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r5 = r14.$candidate
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r7 = r14.this$0
            r1.<init>(r4, r5, r7, r6)
            kotlinx.coroutines.BuildersKt.launch$default(r15, r6, r6, r1, r3)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r15 = r14.$this_stateMachine
            java.util.Objects.requireNonNull(r15)
            kotlinx.coroutines.channels.Channel<com.google.android.systemui.statusbar.notification.voicereplies.StartSessionRequest> r15 = r15.startSessionRequests
            kotlinx.coroutines.channels.AbstractChannel$Itr r15 = r15.iterator()
            r1 = r15
        L_0x005e:
            r14.L$0 = r1
            r14.label = r2
            java.lang.Object r15 = r1.hasNext(r14)
            if (r15 != r0) goto L_0x0069
            return r0
        L_0x0069:
            java.lang.Boolean r15 = (java.lang.Boolean) r15
            boolean r15 = r15.booleanValue()
            if (r15 == 0) goto L_0x00d3
            java.lang.Object r15 = r1.next()
            com.google.android.systemui.statusbar.notification.voicereplies.StartSessionRequest r15 = (com.google.android.systemui.statusbar.notification.voicereplies.StartSessionRequest) r15
            java.util.Objects.requireNonNull(r15)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler r3 = r15.handler
            int r3 = r3.getUserId()
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r4 = r14.$candidate
            java.util.Objects.requireNonNull(r4)
            int r4 = r4.userId
            if (r3 == r4) goto L_0x00bd
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r3 = r14.this$0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger r3 = r3.logger
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler r4 = r15.handler
            int r4 = r4.getUserId()
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r5 = r14.$candidate
            java.util.Objects.requireNonNull(r5)
            int r5 = r5.userId
            java.util.Objects.requireNonNull(r3)
            com.android.systemui.log.LogBuffer r3 = r3.logBuffer
            com.android.systemui.log.LogLevel r6 = com.android.systemui.log.LogLevel.DEBUG
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger$logUserIdMismatch$2 r7 = com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger$logUserIdMismatch$2.INSTANCE
            java.util.Objects.requireNonNull(r3)
            boolean r8 = r3.frozen
            if (r8 != 0) goto L_0x00b7
            java.lang.String r8 = "NotifVoiceReply"
            com.android.systemui.log.LogMessageImpl r6 = r3.obtain(r8, r6, r7)
            r6.int1 = r4
            r6.int2 = r5
            r3.push(r6)
        L_0x00b7:
            kotlin.jvm.functions.Function0<kotlin.Unit> r15 = r15.onFail
            r15.invoke()
            goto L_0x005e
        L_0x00bd:
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r3 = r14.$this_stateMachine
            java.util.Objects.requireNonNull(r3)
            kotlinx.coroutines.flow.MutableStateFlow<com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyState> r3 = r3.stateFlow
            com.google.android.systemui.statusbar.notification.voicereplies.InSession r4 = new com.google.android.systemui.statusbar.notification.voicereplies.InSession
            java.lang.String r5 = r15.initialContent
            kotlin.jvm.functions.Function2<com.google.android.systemui.statusbar.notification.voicereplies.Session, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> r15 = r15.block
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r6 = r14.$candidate
            r4.<init>(r5, r15, r6)
            r3.setValue(r4)
            goto L_0x005e
        L_0x00d3:
            kotlin.Unit r14 = kotlin.Unit.INSTANCE
            return r14
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
