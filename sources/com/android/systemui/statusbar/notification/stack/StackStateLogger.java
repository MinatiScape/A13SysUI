package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import java.util.Objects;
/* compiled from: StackStateLogger.kt */
/* loaded from: classes.dex */
public final class StackStateLogger {
    public final LogBuffer buffer;

    public final void disappearAnimationEnded(String str) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        StackStateLogger$disappearAnimationEnded$2 stackStateLogger$disappearAnimationEnded$2 = StackStateLogger$disappearAnimationEnded$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("StackScroll", logLevel, stackStateLogger$disappearAnimationEnded$2);
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
    }

    public StackStateLogger(LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }
}
