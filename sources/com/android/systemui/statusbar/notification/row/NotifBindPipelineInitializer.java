package com.android.systemui.statusbar.notification.row;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import java.util.Objects;
/* loaded from: classes.dex */
public final class NotifBindPipelineInitializer {
    public NotifBindPipeline mNotifBindPipeline;
    public RowContentBindStage mRowContentBindStage;

    public final void initialize() {
        NotifBindPipeline notifBindPipeline = this.mNotifBindPipeline;
        RowContentBindStage rowContentBindStage = this.mRowContentBindStage;
        Objects.requireNonNull(notifBindPipeline);
        NotifBindPipelineLogger notifBindPipelineLogger = notifBindPipeline.mLogger;
        String name = rowContentBindStage.getClass().getName();
        Objects.requireNonNull(notifBindPipelineLogger);
        LogBuffer logBuffer = notifBindPipelineLogger.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifBindPipelineLogger$logStageSet$2 notifBindPipelineLogger$logStageSet$2 = NotifBindPipelineLogger$logStageSet$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifBindPipeline", logLevel, notifBindPipelineLogger$logStageSet$2);
            obtain.str1 = name;
            logBuffer.push(obtain);
        }
        notifBindPipeline.mStage = rowContentBindStage;
        rowContentBindStage.mBindRequestListener = new NotifBindPipeline$$ExternalSyntheticLambda1(notifBindPipeline);
    }

    public NotifBindPipelineInitializer(NotifBindPipeline notifBindPipeline, RowContentBindStage rowContentBindStage) {
        this.mNotifBindPipeline = notifBindPipeline;
        this.mRowContentBindStage = rowContentBindStage;
    }
}
