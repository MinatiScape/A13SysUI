package com.android.systemui.statusbar.policy;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import java.util.Objects;
/* compiled from: HeadsUpManagerLogger.kt */
/* loaded from: classes.dex */
public final class HeadsUpManagerLogger {
    public final LogBuffer buffer;

    public final void logReleaseAllImmediately() {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        HeadsUpManagerLogger$logReleaseAllImmediately$2 headsUpManagerLogger$logReleaseAllImmediately$2 = HeadsUpManagerLogger$logReleaseAllImmediately$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            logBuffer.push(logBuffer.obtain("HeadsUpManager", logLevel, headsUpManagerLogger$logReleaseAllImmediately$2));
        }
    }

    public final void logRemoveNotification(String str, boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        HeadsUpManagerLogger$logRemoveNotification$2 headsUpManagerLogger$logRemoveNotification$2 = HeadsUpManagerLogger$logRemoveNotification$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("HeadsUpManager", logLevel, headsUpManagerLogger$logRemoveNotification$2);
            obtain.str1 = str;
            obtain.bool1 = z;
            logBuffer.push(obtain);
        }
    }

    public final void logShowNotification(String str) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        HeadsUpManagerLogger$logShowNotification$2 headsUpManagerLogger$logShowNotification$2 = HeadsUpManagerLogger$logShowNotification$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("HeadsUpManager", logLevel, headsUpManagerLogger$logShowNotification$2);
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
    }

    public final void logUpdateEntry(String str, boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        HeadsUpManagerLogger$logUpdateEntry$2 headsUpManagerLogger$logUpdateEntry$2 = new HeadsUpManagerLogger$logUpdateEntry$2(str);
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("HeadsUpManager", logLevel, headsUpManagerLogger$logUpdateEntry$2);
            obtain.str1 = str;
            obtain.bool1 = z;
            logBuffer.push(obtain);
        }
    }

    public final void logUpdateNotification(String str, boolean z, boolean z2) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        HeadsUpManagerLogger$logUpdateNotification$2 headsUpManagerLogger$logUpdateNotification$2 = HeadsUpManagerLogger$logUpdateNotification$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("HeadsUpManager", logLevel, headsUpManagerLogger$logUpdateNotification$2);
            obtain.str1 = str;
            obtain.bool1 = z;
            obtain.bool2 = z2;
            logBuffer.push(obtain);
        }
    }

    public HeadsUpManagerLogger(LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }
}
