package com.google.android.systemui.statusbar;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger$logStatic$2;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManager;
import java.util.Objects;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
/* compiled from: NotificationVoiceReplyManagerService.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$hideVisibleQuickPhraseCta$1", f = "NotificationVoiceReplyManagerService.kt", l = {110}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class NotificationVoiceReplyManagerService$binder$1$hideVisibleQuickPhraseCta$1 extends SuspendLambda implements Function1<Continuation<? super Unit>, Object> {
    public int label;
    public final /* synthetic */ NotificationVoiceReplyManagerService this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerService$binder$1$hideVisibleQuickPhraseCta$1(NotificationVoiceReplyManagerService notificationVoiceReplyManagerService, Continuation<? super NotificationVoiceReplyManagerService$binder$1$hideVisibleQuickPhraseCta$1> continuation) {
        super(1, continuation);
        this.this$0 = notificationVoiceReplyManagerService;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Object invoke(Continuation<? super Unit> continuation) {
        return new NotificationVoiceReplyManagerService$binder$1$hideVisibleQuickPhraseCta$1(this.this$0, continuation).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            NotificationVoiceReplyLogger notificationVoiceReplyLogger = this.this$0.logger;
            Objects.requireNonNull(notificationVoiceReplyLogger);
            LogBuffer logBuffer = notificationVoiceReplyLogger.logBuffer;
            LogLevel logLevel = LogLevel.DEBUG;
            NotificationVoiceReplyLogger$logStatic$2 notificationVoiceReplyLogger$logStatic$2 = new NotificationVoiceReplyLogger$logStatic$2("BINDER: hideVisibleQuickPhraseCta");
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                logBuffer.push(logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logStatic$2));
            }
            NotificationVoiceReplyManager notificationVoiceReplyManager = this.this$0.voiceReplyManager;
            if (notificationVoiceReplyManager == null) {
                notificationVoiceReplyManager = null;
            }
            this.label = 1;
            if (notificationVoiceReplyManager.requestHideQuickPhraseCTA(this) == coroutineSingletons) {
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
