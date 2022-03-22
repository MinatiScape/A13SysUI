package com.google.android.systemui.statusbar;

import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger;
import java.util.Objects;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.flow.SharedFlowImpl;
/* compiled from: NotificationVoiceReplyManagerService.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$setFeatureEnabled$1", f = "NotificationVoiceReplyManagerService.kt", l = {84}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class NotificationVoiceReplyManagerService$binder$1$setFeatureEnabled$1 extends SuspendLambda implements Function1<Continuation<? super Unit>, Object> {
    public final /* synthetic */ int $enabledSetting;
    public int label;
    public final /* synthetic */ NotificationVoiceReplyManagerService this$0;
    public final /* synthetic */ NotificationVoiceReplyManagerService$binder$1 this$1;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerService$binder$1$setFeatureEnabled$1(NotificationVoiceReplyManagerService notificationVoiceReplyManagerService, NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1, int i, Continuation<? super NotificationVoiceReplyManagerService$binder$1$setFeatureEnabled$1> continuation) {
        super(1, continuation);
        this.this$0 = notificationVoiceReplyManagerService;
        this.this$1 = notificationVoiceReplyManagerService$binder$1;
        this.$enabledSetting = i;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Object invoke(Continuation<? super Unit> continuation) {
        return new NotificationVoiceReplyManagerService$binder$1$setFeatureEnabled$1(this.this$0, this.this$1, this.$enabledSetting, continuation).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            NotificationVoiceReplyLogger notificationVoiceReplyLogger = this.this$0.logger;
            Objects.requireNonNull(this.this$1);
            notificationVoiceReplyLogger.logSetFeatureEnabled(NotificationVoiceReplyManagerService$binder$1.getUserId(), this.$enabledSetting);
            NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1 = this.this$1;
            Objects.requireNonNull(notificationVoiceReplyManagerService$binder$1);
            SharedFlowImpl sharedFlowImpl = notificationVoiceReplyManagerService$binder$1.setFeatureEnabledFlow;
            Objects.requireNonNull(this.this$1);
            Pair pair = new Pair(new Integer(NotificationVoiceReplyManagerService$binder$1.getUserId()), new Integer(this.$enabledSetting));
            this.label = 1;
            if (sharedFlowImpl.emit(pair, this) == coroutineSingletons) {
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
