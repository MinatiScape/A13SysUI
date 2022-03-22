package com.android.systemui.statusbar;

import android.app.PendingIntent;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import java.util.Objects;
/* compiled from: ActionClickLogger.kt */
/* loaded from: classes.dex */
public final class ActionClickLogger {
    public final LogBuffer buffer;

    public final void logKeyguardGone(PendingIntent pendingIntent) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        ActionClickLogger$logKeyguardGone$2 actionClickLogger$logKeyguardGone$2 = ActionClickLogger$logKeyguardGone$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("ActionClickLogger", logLevel, actionClickLogger$logKeyguardGone$2);
            obtain.str1 = pendingIntent.getIntent().toString();
            logBuffer.push(obtain);
        }
    }

    public final void logWaitingToCloseKeyguard(PendingIntent pendingIntent) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        ActionClickLogger$logWaitingToCloseKeyguard$2 actionClickLogger$logWaitingToCloseKeyguard$2 = ActionClickLogger$logWaitingToCloseKeyguard$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("ActionClickLogger", logLevel, actionClickLogger$logWaitingToCloseKeyguard$2);
            obtain.str1 = pendingIntent.getIntent().toString();
            logBuffer.push(obtain);
        }
    }

    public ActionClickLogger(LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }
}
