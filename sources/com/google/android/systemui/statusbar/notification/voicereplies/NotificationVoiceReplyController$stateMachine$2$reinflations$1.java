package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.Objects;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$reinflations$1", f = "NotificationVoiceReplyManager.kt", l = {}, m = "invokeSuspend")
/* loaded from: classes.dex */
public final class NotificationVoiceReplyController$stateMachine$2$reinflations$1 extends SuspendLambda implements Function2<Pair<? extends NotificationEntry, ? extends String>, Continuation<? super Unit>, Object> {
    public /* synthetic */ Object L$0;
    public int label;
    public final /* synthetic */ NotificationVoiceReplyController this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$stateMachine$2$reinflations$1(NotificationVoiceReplyController notificationVoiceReplyController, Continuation<? super NotificationVoiceReplyController$stateMachine$2$reinflations$1> continuation) {
        super(2, continuation);
        this.this$0 = notificationVoiceReplyController;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyController$stateMachine$2$reinflations$1 notificationVoiceReplyController$stateMachine$2$reinflations$1 = new NotificationVoiceReplyController$stateMachine$2$reinflations$1(this.this$0, continuation);
        notificationVoiceReplyController$stateMachine$2$reinflations$1.L$0 = obj;
        return notificationVoiceReplyController$stateMachine$2$reinflations$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Pair<? extends NotificationEntry, ? extends String> pair, Continuation<? super Unit> continuation) {
        Unit unit = Unit.INSTANCE;
        ((NotificationVoiceReplyController$stateMachine$2$reinflations$1) create(pair, continuation)).invokeSuspend(unit);
        return unit;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        if (this.label == 0) {
            ResultKt.throwOnFailure(obj);
            Pair pair = (Pair) this.L$0;
            NotificationEntry notificationEntry = (NotificationEntry) pair.component1();
            String str = (String) pair.component2();
            NotificationVoiceReplyLogger notificationVoiceReplyLogger = this.this$0.logger;
            Objects.requireNonNull(notificationEntry);
            String str2 = notificationEntry.mKey;
            Objects.requireNonNull(notificationVoiceReplyLogger);
            LogBuffer logBuffer = notificationVoiceReplyLogger.logBuffer;
            LogLevel logLevel = LogLevel.DEBUG;
            NotificationVoiceReplyLogger$logReinflation$2 notificationVoiceReplyLogger$logReinflation$2 = NotificationVoiceReplyLogger$logReinflation$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logReinflation$2);
                obtain.str1 = str2;
                obtain.str2 = str;
                logBuffer.push(obtain);
            }
            return Unit.INSTANCE;
        }
        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
    }
}
