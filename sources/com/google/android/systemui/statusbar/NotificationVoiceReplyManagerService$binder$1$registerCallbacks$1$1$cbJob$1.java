package com.google.android.systemui.statusbar;

import com.android.systemui.statusbar.notification.people.Subscription;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt;
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
import kotlin.jvm.internal.Ref$ObjectRef;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
import kotlinx.coroutines.flow.SafeFlow;
import kotlinx.coroutines.flow.SharedFlowImpl;
/* compiled from: NotificationVoiceReplyManagerService.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1$cbJob$1", f = "NotificationVoiceReplyManagerService.kt", l = {66}, m = "invokeSuspend")
/* loaded from: classes.dex */
public final class NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1$cbJob$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ INotificationVoiceReplyServiceCallbacks $callbacks;
    public final /* synthetic */ Ref$ObjectRef<Subscription> $onDeathSub;
    public int label;
    public final /* synthetic */ NotificationVoiceReplyManagerService$binder$1 this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1$cbJob$1(NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1, INotificationVoiceReplyServiceCallbacks iNotificationVoiceReplyServiceCallbacks, Ref$ObjectRef<Subscription> ref$ObjectRef, Continuation<? super NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1$cbJob$1> continuation) {
        super(2, continuation);
        this.this$0 = notificationVoiceReplyManagerService$binder$1;
        this.$callbacks = iNotificationVoiceReplyServiceCallbacks;
        this.$onDeathSub = ref$ObjectRef;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1$cbJob$1(this.this$0, this.$callbacks, this.$onDeathSub, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1$cbJob$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object obj2 = CoroutineSingletons.COROUTINE_SUSPENDED;
        int i = this.label;
        Subscription subscription = null;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            final NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1 = this.this$0;
            INotificationVoiceReplyServiceCallbacks iNotificationVoiceReplyServiceCallbacks = this.$callbacks;
            this.label = 1;
            int i2 = NotificationVoiceReplyManagerService$binder$1.$r8$clinit;
            Objects.requireNonNull(notificationVoiceReplyManagerService$binder$1);
            final SharedFlowImpl sharedFlowImpl = notificationVoiceReplyManagerService$binder$1.setFeatureEnabledFlow;
            Flow<Pair<? extends Integer, ? extends Integer>> notificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$$inlined$filter$1 = new Flow<Pair<? extends Integer, ? extends Integer>>() { // from class: com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$$inlined$filter$1

                /* compiled from: Collect.kt */
                /* renamed from: com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$$inlined$filter$1$2  reason: invalid class name */
                /* loaded from: classes.dex */
                public static final class AnonymousClass2 implements FlowCollector<Pair<? extends Integer, ? extends Integer>> {
                    public final /* synthetic */ FlowCollector $this_unsafeFlow$inlined;
                    public final /* synthetic */ NotificationVoiceReplyManagerService$binder$1 this$0;

                    /* compiled from: Collect.kt */
                    @DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$$inlined$filter$1$2", f = "NotificationVoiceReplyManagerService.kt", l = {137}, m = "emit")
                    /* renamed from: com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$$inlined$filter$1$2$1  reason: invalid class name */
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
                            return AnonymousClass2.this.emit(null, this);
                        }
                    }

                    public AnonymousClass2(FlowCollector flowCollector, NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1) {
                        this.$this_unsafeFlow$inlined = flowCollector;
                        this.this$0 = notificationVoiceReplyManagerService$binder$1;
                    }

                    /* JADX WARN: Removed duplicated region for block: B:10:0x0021  */
                    /* JADX WARN: Removed duplicated region for block: B:14:0x002f  */
                    @Override // kotlinx.coroutines.flow.FlowCollector
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                        To view partially-correct add '--show-bad-code' argument
                    */
                    public final java.lang.Object emit(kotlin.Pair<? extends java.lang.Integer, ? extends java.lang.Integer> r5, kotlin.coroutines.Continuation r6) {
                        /*
                            r4 = this;
                            boolean r0 = r6 instanceof com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$$inlined$filter$1.AnonymousClass2.AnonymousClass1
                            if (r0 == 0) goto L_0x0013
                            r0 = r6
                            com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$$inlined$filter$1$2$1 r0 = (com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$$inlined$filter$1.AnonymousClass2.AnonymousClass1) r0
                            int r1 = r0.label
                            r2 = -2147483648(0xffffffff80000000, float:-0.0)
                            r3 = r1 & r2
                            if (r3 == 0) goto L_0x0013
                            int r1 = r1 - r2
                            r0.label = r1
                            goto L_0x0018
                        L_0x0013:
                            com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$$inlined$filter$1$2$1 r0 = new com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$$inlined$filter$1$2$1
                            r0.<init>(r6)
                        L_0x0018:
                            java.lang.Object r6 = r0.result
                            kotlin.coroutines.intrinsics.CoroutineSingletons r1 = kotlin.coroutines.intrinsics.CoroutineSingletons.COROUTINE_SUSPENDED
                            int r2 = r0.label
                            r3 = 1
                            if (r2 == 0) goto L_0x002f
                            if (r2 != r3) goto L_0x0027
                            kotlin.ResultKt.throwOnFailure(r6)
                            goto L_0x005a
                        L_0x0027:
                            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException
                            java.lang.String r5 = "call to 'resume' before 'invoke' with coroutine"
                            r4.<init>(r5)
                            throw r4
                        L_0x002f:
                            kotlin.ResultKt.throwOnFailure(r6)
                            kotlinx.coroutines.flow.FlowCollector r6 = r4.$this_unsafeFlow$inlined
                            r2 = r5
                            kotlin.Pair r2 = (kotlin.Pair) r2
                            java.lang.Object r2 = r2.getFirst()
                            java.lang.Number r2 = (java.lang.Number) r2
                            int r2 = r2.intValue()
                            com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1 r4 = r4.this$0
                            java.util.Objects.requireNonNull(r4)
                            int r4 = com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1.getUserId()
                            if (r2 != r4) goto L_0x004e
                            r4 = r3
                            goto L_0x004f
                        L_0x004e:
                            r4 = 0
                        L_0x004f:
                            if (r4 == 0) goto L_0x005a
                            r0.label = r3
                            java.lang.Object r4 = r6.emit(r5, r0)
                            if (r4 != r1) goto L_0x005a
                            return r1
                        L_0x005a:
                            kotlin.Unit r4 = kotlin.Unit.INSTANCE
                            return r4
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$$inlined$filter$1.AnonymousClass2.emit(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
                    }
                }

                @Override // kotlinx.coroutines.flow.Flow
                public final Object collect(FlowCollector<? super Pair<? extends Integer, ? extends Integer>> flowCollector, Continuation continuation) {
                    Object collect = sharedFlowImpl.collect(new AnonymousClass2(flowCollector, notificationVoiceReplyManagerService$binder$1), continuation);
                    if (collect == CoroutineSingletons.COROUTINE_SUSPENDED) {
                        return collect;
                    }
                    return Unit.INSTANCE;
                }
            };
            Pair pair = new Pair(new Integer(NotificationVoiceReplyManagerService$binder$1.getUserId()), new Integer(2));
            byte[][] bArr = NotificationVoiceReplyManagerServiceKt.AGSA_CERTS;
            Object collectLatest = NotificationVoiceReplyManagerKt.collectLatest(NotificationVoiceReplyManagerKt.distinctUntilChanged(new SafeFlow(new NotificationVoiceReplyManagerServiceKt$startingWith$1$1(pair, notificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$$inlined$filter$1, null)), NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$3.INSTANCE), new NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$4(notificationVoiceReplyManagerService$binder$1, iNotificationVoiceReplyServiceCallbacks, null), this);
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
        Subscription subscription2 = this.$onDeathSub.element;
        if (subscription2 != null) {
            subscription = subscription2;
        }
        subscription.unsubscribe();
        return Unit.INSTANCE;
    }
}
