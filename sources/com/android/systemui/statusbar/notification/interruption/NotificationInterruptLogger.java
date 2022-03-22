package com.android.systemui.statusbar.notification.interruption;

import android.service.notification.StatusBarNotification;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import java.util.Objects;
/* compiled from: NotificationInterruptLogger.kt */
/* loaded from: classes.dex */
public final class NotificationInterruptLogger {
    public final LogBuffer hunBuffer;
    public final LogBuffer notifBuffer;

    public final void logNoAlertingSuppressedBy(StatusBarNotification statusBarNotification, NotificationInterruptSuppressor notificationInterruptSuppressor, boolean z) {
        LogBuffer logBuffer = this.hunBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationInterruptLogger$logNoAlertingSuppressedBy$2 notificationInterruptLogger$logNoAlertingSuppressedBy$2 = NotificationInterruptLogger$logNoAlertingSuppressedBy$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", logLevel, notificationInterruptLogger$logNoAlertingSuppressedBy$2);
            obtain.str1 = statusBarNotification.getKey();
            obtain.str2 = notificationInterruptSuppressor.getName();
            obtain.bool1 = z;
            logBuffer.push(obtain);
        }
    }

    public NotificationInterruptLogger(LogBuffer logBuffer, LogBuffer logBuffer2) {
        this.notifBuffer = logBuffer;
        this.hunBuffer = logBuffer2;
    }
}
