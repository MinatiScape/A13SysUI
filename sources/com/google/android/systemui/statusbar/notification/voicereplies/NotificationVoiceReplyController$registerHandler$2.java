package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import java.util.List;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes.dex */
public final class NotificationVoiceReplyController$registerHandler$2 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ NotificationVoiceReplyHandler $handler;
    public final /* synthetic */ NotificationVoiceReplyController.Connection $this_registerHandler;
    public final /* synthetic */ NotificationVoiceReplyController this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$registerHandler$2(NotificationVoiceReplyController.Connection connection, NotificationVoiceReplyHandler notificationVoiceReplyHandler, NotificationVoiceReplyController notificationVoiceReplyController) {
        super(0);
        this.$this_registerHandler = connection;
        this.$handler = notificationVoiceReplyHandler;
        this.this$0 = notificationVoiceReplyController;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        HasCandidate hasCandidate;
        NotificationVoiceReplyController.Connection connection = this.$this_registerHandler;
        Objects.requireNonNull(connection);
        List<NotificationVoiceReplyHandler> list = connection.activeHandlersByUser.get(Integer.valueOf(this.$handler.getUserId()));
        if (list != null) {
            NotificationVoiceReplyHandler notificationVoiceReplyHandler = this.$handler;
            NotificationVoiceReplyController.Connection connection2 = this.$this_registerHandler;
            NotificationVoiceReplyController notificationVoiceReplyController = this.this$0;
            list.remove(notificationVoiceReplyHandler);
            if (list.isEmpty()) {
                Objects.requireNonNull(connection2);
                connection2.activeHandlersByUser.remove(Integer.valueOf(notificationVoiceReplyHandler.getUserId()));
                VoiceReplyState value = connection2.stateFlow.getValue();
                VoiceReplyTarget voiceReplyTarget = null;
                if (value instanceof HasCandidate) {
                    hasCandidate = (HasCandidate) value;
                } else {
                    hasCandidate = null;
                }
                if (hasCandidate != null) {
                    voiceReplyTarget = hasCandidate.candidate;
                }
                boolean z = false;
                if (voiceReplyTarget != null && voiceReplyTarget.userId == notificationVoiceReplyHandler.getUserId()) {
                    z = true;
                }
                if (z) {
                    NotificationVoiceReplyLogger notificationVoiceReplyLogger = notificationVoiceReplyController.logger;
                    Objects.requireNonNull(notificationVoiceReplyLogger);
                    LogBuffer logBuffer = notificationVoiceReplyLogger.logBuffer;
                    LogLevel logLevel = LogLevel.DEBUG;
                    NotificationVoiceReplyLogger$logStatic$2 notificationVoiceReplyLogger$logStatic$2 = new NotificationVoiceReplyLogger$logStatic$2("No more registered handlers for current candidate");
                    Objects.requireNonNull(logBuffer);
                    if (!logBuffer.frozen) {
                        logBuffer.push(logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logStatic$2));
                    }
                    connection2.stateFlow.setValue(notificationVoiceReplyController.queryInitialState(connection2));
                }
            }
        }
        return Unit.INSTANCE;
    }
}
