package com.android.systemui.log;
/* compiled from: LogcatEchoTrackerProd.kt */
/* loaded from: classes.dex */
public final class LogcatEchoTrackerProd implements LogcatEchoTracker {
    @Override // com.android.systemui.log.LogcatEchoTracker
    public final boolean isBufferLoggable(String str, LogLevel logLevel) {
        if (logLevel.compareTo(LogLevel.WARNING) >= 0) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.log.LogcatEchoTracker
    public final boolean isTagLoggable(String str, LogLevel logLevel) {
        if (logLevel.compareTo(LogLevel.WARNING) >= 0) {
            return true;
        }
        return false;
    }
}
