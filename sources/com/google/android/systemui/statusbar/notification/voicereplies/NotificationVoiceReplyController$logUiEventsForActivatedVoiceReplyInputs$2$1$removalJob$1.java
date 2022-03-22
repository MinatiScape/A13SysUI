package com.google.android.systemui.statusbar.notification.voicereplies;

import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import java.util.Objects;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CompletableDeferred;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.flow.FlowKt__ReduceKt;
import kotlinx.coroutines.flow.MutableSharedFlow;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$removalJob$1", f = "NotificationVoiceReplyManager.kt", l = {409}, m = "invokeSuspend")
/* loaded from: classes.dex */
public final class NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$removalJob$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ CompletableDeferred<Unit> $completion;
    public final /* synthetic */ String $key;
    public final /* synthetic */ NotificationVoiceReplyController.Connection $this_logUiEventsForActivatedVoiceReplyInputs;
    public int label;

    /* compiled from: NotificationVoiceReplyManager.kt */
    @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$removalJob$1$1", f = "NotificationVoiceReplyManager.kt", l = {}, m = "invokeSuspend")
    /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$removalJob$1$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass1 extends SuspendLambda implements Function2<String, Continuation<? super Boolean>, Object> {
        public final /* synthetic */ String $key;
        public /* synthetic */ Object L$0;
        public int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass1(String str, Continuation<? super AnonymousClass1> continuation) {
            super(2, continuation);
            this.$key = str;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            AnonymousClass1 r0 = new AnonymousClass1(this.$key, continuation);
            r0.L$0 = obj;
            return r0;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(String str, Continuation<? super Boolean> continuation) {
            return ((AnonymousClass1) create(str, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            if (this.label == 0) {
                ResultKt.throwOnFailure(obj);
                return Boolean.valueOf(Intrinsics.areEqual((String) this.L$0, this.$key));
            }
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$removalJob$1(NotificationVoiceReplyController.Connection connection, CompletableDeferred<Unit> completableDeferred, String str, Continuation<? super NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$removalJob$1> continuation) {
        super(2, continuation);
        this.$this_logUiEventsForActivatedVoiceReplyInputs = connection;
        this.$completion = completableDeferred;
        this.$key = str;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$removalJob$1(this.$this_logUiEventsForActivatedVoiceReplyInputs, this.$completion, this.$key, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$removalJob$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            NotificationVoiceReplyController.Connection connection = this.$this_logUiEventsForActivatedVoiceReplyInputs;
            Objects.requireNonNull(connection);
            MutableSharedFlow<String> mutableSharedFlow = connection.entryRemovals;
            AnonymousClass1 r1 = new AnonymousClass1(this.$key, null);
            this.label = 1;
            if (FlowKt__ReduceKt.first(mutableSharedFlow, r1, this) == coroutineSingletons) {
                return coroutineSingletons;
            }
        } else if (i == 1) {
            ResultKt.throwOnFailure(obj);
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        CompletableDeferred<Unit> completableDeferred = this.$completion;
        Unit unit = Unit.INSTANCE;
        completableDeferred.complete();
        return unit;
    }
}
