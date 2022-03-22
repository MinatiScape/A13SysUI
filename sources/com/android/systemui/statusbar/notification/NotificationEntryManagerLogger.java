package com.android.systemui.statusbar.notification;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import java.util.Objects;
/* compiled from: NotificationEntryManagerLogger.kt */
/* loaded from: classes.dex */
public final class NotificationEntryManagerLogger {
    public final LogBuffer buffer;

    public final void logFilterAndSort(String str) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotificationEntryManagerLogger$logFilterAndSort$2 notificationEntryManagerLogger$logFilterAndSort$2 = NotificationEntryManagerLogger$logFilterAndSort$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logFilterAndSort$2);
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
    }

    public final void logInflationAborted(String str, String str2, String str3) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationEntryManagerLogger$logInflationAborted$2 notificationEntryManagerLogger$logInflationAborted$2 = NotificationEntryManagerLogger$logInflationAborted$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logInflationAborted$2);
            obtain.str1 = str;
            obtain.str2 = str2;
            obtain.str3 = str3;
            logBuffer.push(obtain);
        }
    }

    public final void logLifetimeExtended(String str, String str2, String str3) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotificationEntryManagerLogger$logLifetimeExtended$2 notificationEntryManagerLogger$logLifetimeExtended$2 = NotificationEntryManagerLogger$logLifetimeExtended$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logLifetimeExtended$2);
            obtain.str1 = str;
            obtain.str2 = str2;
            obtain.str3 = str3;
            logBuffer.push(obtain);
        }
    }

    public final void logUseWhileNewPipelineActive(String str, String str2) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.WARNING;
        NotificationEntryManagerLogger$logUseWhileNewPipelineActive$2 notificationEntryManagerLogger$logUseWhileNewPipelineActive$2 = NotificationEntryManagerLogger$logUseWhileNewPipelineActive$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logUseWhileNewPipelineActive$2);
            obtain.str1 = str;
            obtain.str2 = str2;
            logBuffer.push(obtain);
        }
    }

    public NotificationEntryManagerLogger(LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }
}
