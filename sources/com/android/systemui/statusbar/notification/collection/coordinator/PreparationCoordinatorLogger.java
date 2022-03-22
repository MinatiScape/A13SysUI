package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import java.util.Objects;
/* compiled from: PreparationCoordinatorLogger.kt */
/* loaded from: classes.dex */
public final class PreparationCoordinatorLogger {
    public final LogBuffer buffer;

    public final void logDelayingGroupRelease(String str, String str2) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        PreparationCoordinatorLogger$logDelayingGroupRelease$2 preparationCoordinatorLogger$logDelayingGroupRelease$2 = PreparationCoordinatorLogger$logDelayingGroupRelease$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("PreparationCoordinator", logLevel, preparationCoordinatorLogger$logDelayingGroupRelease$2);
            obtain.str1 = str;
            obtain.str2 = str2;
            logBuffer.push(obtain);
        }
    }

    public PreparationCoordinatorLogger(LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }
}
