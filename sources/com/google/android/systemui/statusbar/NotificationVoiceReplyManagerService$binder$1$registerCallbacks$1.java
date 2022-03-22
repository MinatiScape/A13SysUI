package com.google.android.systemui.statusbar;

import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger;
import java.util.Objects;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Lambda;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.StandaloneCoroutine;
/* compiled from: NotificationVoiceReplyManagerService.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1", f = "NotificationVoiceReplyManagerService.kt", l = {}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1 extends SuspendLambda implements Function1<Continuation<? super Unit>, Object> {
    public final /* synthetic */ INotificationVoiceReplyServiceCallbacks $callbacks;
    public int label;
    public final /* synthetic */ NotificationVoiceReplyManagerService this$0;
    public final /* synthetic */ NotificationVoiceReplyManagerService$binder$1 this$1;

    /* compiled from: NotificationVoiceReplyManagerService.kt */
    @DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1", f = "NotificationVoiceReplyManagerService.kt", l = {}, m = "invokeSuspend")
    /* renamed from: com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        public final /* synthetic */ INotificationVoiceReplyServiceCallbacks $callbacks;
        private /* synthetic */ Object L$0;
        public int label;
        public final /* synthetic */ NotificationVoiceReplyManagerService$binder$1 this$0;

        /* compiled from: NotificationVoiceReplyManagerService.kt */
        /* renamed from: com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1$1  reason: invalid class name and collision with other inner class name */
        /* loaded from: classes.dex */
        public static final class C00111 extends Lambda implements Function0<Unit> {
            public final /* synthetic */ Job $cbJob;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public C00111(StandaloneCoroutine standaloneCoroutine) {
                super(0);
                this.$cbJob = standaloneCoroutine;
            }

            @Override // kotlin.jvm.functions.Function0
            public final Unit invoke() {
                this.$cbJob.cancel(null);
                return Unit.INSTANCE;
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass1(INotificationVoiceReplyServiceCallbacks iNotificationVoiceReplyServiceCallbacks, NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1, Continuation<? super AnonymousClass1> continuation) {
            super(2, continuation);
            this.$callbacks = iNotificationVoiceReplyServiceCallbacks;
            this.this$0 = notificationVoiceReplyManagerService$binder$1;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            AnonymousClass1 r0 = new AnonymousClass1(this.$callbacks, this.this$0, continuation);
            r0.L$0 = obj;
            return r0;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            Unit unit = Unit.INSTANCE;
            ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(unit);
            return unit;
        }

        /* JADX WARN: Type inference failed for: r2v2, types: [android.os.IBinder$DeathRecipient, com.google.android.systemui.statusbar.NotificationVoiceReplyManagerServiceKt$onDeath$recipient$1] */
        /* JADX WARN: Type inference failed for: r6v5, types: [T, com.google.android.systemui.statusbar.notification.voicereplies.SafeSubscription] */
        /* JADX WARN: Unknown variable types count: 1 */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final java.lang.Object invokeSuspend(java.lang.Object r7) {
            /*
                r6 = this;
                int r0 = r6.label
                if (r0 != 0) goto L_0x004d
                kotlin.ResultKt.throwOnFailure(r7)
                java.lang.Object r7 = r6.L$0
                kotlinx.coroutines.CoroutineScope r7 = (kotlinx.coroutines.CoroutineScope) r7
                kotlin.jvm.internal.Ref$ObjectRef r0 = new kotlin.jvm.internal.Ref$ObjectRef
                r0.<init>()
                kotlinx.coroutines.CoroutineStart r1 = kotlinx.coroutines.CoroutineStart.LAZY
                com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1$cbJob$1 r2 = new com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1$cbJob$1
                com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1 r3 = r6.this$0
                com.google.android.systemui.statusbar.INotificationVoiceReplyServiceCallbacks r4 = r6.$callbacks
                r5 = 0
                r2.<init>(r3, r4, r0, r5)
                r3 = 1
                kotlinx.coroutines.StandaloneCoroutine r7 = kotlinx.coroutines.BuildersKt.launch$default(r7, r5, r1, r2, r3)
                com.google.android.systemui.statusbar.INotificationVoiceReplyServiceCallbacks r6 = r6.$callbacks     // Catch: RemoteException -> 0x0047
                android.os.IBinder r6 = r6.asBinder()     // Catch: RemoteException -> 0x0047
                com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1$1 r1 = new com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1$1     // Catch: RemoteException -> 0x0047
                r1.<init>(r7)     // Catch: RemoteException -> 0x0047
                byte[][] r2 = com.google.android.systemui.statusbar.NotificationVoiceReplyManagerServiceKt.AGSA_CERTS     // Catch: RemoteException -> 0x0047
                com.google.android.systemui.statusbar.NotificationVoiceReplyManagerServiceKt$onDeath$recipient$1 r2 = new com.google.android.systemui.statusbar.NotificationVoiceReplyManagerServiceKt$onDeath$recipient$1     // Catch: RemoteException -> 0x0047
                r2.<init>()     // Catch: RemoteException -> 0x0047
                r1 = 0
                r6.linkToDeath(r2, r1)     // Catch: RemoteException -> 0x0047
                com.google.android.systemui.statusbar.NotificationVoiceReplyManagerServiceKt$onDeath$1 r1 = new com.google.android.systemui.statusbar.NotificationVoiceReplyManagerServiceKt$onDeath$1     // Catch: RemoteException -> 0x0047
                r1.<init>(r6, r2)     // Catch: RemoteException -> 0x0047
                com.google.android.systemui.statusbar.notification.voicereplies.SafeSubscription r6 = new com.google.android.systemui.statusbar.notification.voicereplies.SafeSubscription     // Catch: RemoteException -> 0x0047
                r6.<init>(r1)     // Catch: RemoteException -> 0x0047
                r0.element = r6     // Catch: RemoteException -> 0x0047
                r7.start()     // Catch: RemoteException -> 0x0047
                goto L_0x004a
            L_0x0047:
                r7.cancel(r5)
            L_0x004a:
                kotlin.Unit r6 = kotlin.Unit.INSTANCE
                return r6
            L_0x004d:
                java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
                java.lang.String r7 = "call to 'resume' before 'invoke' with coroutine"
                r6.<init>(r7)
                throw r6
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1.AnonymousClass1.invokeSuspend(java.lang.Object):java.lang.Object");
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1(NotificationVoiceReplyManagerService notificationVoiceReplyManagerService, NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1, INotificationVoiceReplyServiceCallbacks iNotificationVoiceReplyServiceCallbacks, Continuation<? super NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1> continuation) {
        super(1, continuation);
        this.this$0 = notificationVoiceReplyManagerService;
        this.this$1 = notificationVoiceReplyManagerService$binder$1;
        this.$callbacks = iNotificationVoiceReplyServiceCallbacks;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Object invoke(Continuation<? super Unit> continuation) {
        NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1 notificationVoiceReplyManagerService$binder$1$registerCallbacks$1 = new NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1(this.this$0, this.this$1, this.$callbacks, continuation);
        Unit unit = Unit.INSTANCE;
        notificationVoiceReplyManagerService$binder$1$registerCallbacks$1.invokeSuspend(unit);
        return unit;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        if (this.label == 0) {
            ResultKt.throwOnFailure(obj);
            NotificationVoiceReplyLogger notificationVoiceReplyLogger = this.this$0.logger;
            Objects.requireNonNull(this.this$1);
            notificationVoiceReplyLogger.logRegisterCallbacks(NotificationVoiceReplyManagerService$binder$1.getUserId());
            BuildersKt.launch$default(this.this$0.scope, null, null, new AnonymousClass1(this.$callbacks, this.this$1, null), 3);
            return Unit.INSTANCE;
        }
        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
    }
}
