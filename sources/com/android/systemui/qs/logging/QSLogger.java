package com.android.systemui.qs.logging;

import androidx.preference.R$id;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.plugins.qs.QSTile;
import java.util.Objects;
/* compiled from: QSLogger.kt */
/* loaded from: classes.dex */
public final class QSLogger {
    public final LogBuffer buffer;

    public static String toStateString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? "wrong state" : "active" : "inactive" : "unavailable";
    }

    public final void logAllTilesChangeListening(boolean z, String str, String str2) {
        LogLevel logLevel = LogLevel.DEBUG;
        QSLogger$logAllTilesChangeListening$2 qSLogger$logAllTilesChangeListening$2 = QSLogger$logAllTilesChangeListening$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("QSLog", logLevel, qSLogger$logAllTilesChangeListening$2);
            obtain.bool1 = z;
            obtain.str1 = str;
            obtain.str2 = str2;
            logBuffer.push(obtain);
        }
    }

    public final void logPanelExpanded(boolean z, String str) {
        LogLevel logLevel = LogLevel.DEBUG;
        QSLogger$logPanelExpanded$2 qSLogger$logPanelExpanded$2 = QSLogger$logPanelExpanded$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("QSLog", logLevel, qSLogger$logPanelExpanded$2);
            obtain.str1 = str;
            obtain.bool1 = z;
            logBuffer.push(obtain);
        }
    }

    public final void logTileAdded(String str) {
        LogLevel logLevel = LogLevel.DEBUG;
        QSLogger$logTileAdded$2 qSLogger$logTileAdded$2 = QSLogger$logTileAdded$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("QSLog", logLevel, qSLogger$logTileAdded$2);
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
    }

    public final void logTileChangeListening(String str, boolean z) {
        LogLevel logLevel = LogLevel.VERBOSE;
        QSLogger$logTileChangeListening$2 qSLogger$logTileChangeListening$2 = QSLogger$logTileChangeListening$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("QSLog", logLevel, qSLogger$logTileChangeListening$2);
            obtain.bool1 = z;
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
    }

    public final void logTileClick(String str, int i, int i2) {
        LogLevel logLevel = LogLevel.DEBUG;
        QSLogger$logTileClick$2 qSLogger$logTileClick$2 = QSLogger$logTileClick$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("QSLog", logLevel, qSLogger$logTileClick$2);
            obtain.str1 = str;
            obtain.int1 = i;
            obtain.str2 = R$id.toShortString(i);
            obtain.str3 = toStateString(i2);
            logBuffer.push(obtain);
        }
    }

    public final void logTileDestroyed(String str, String str2) {
        LogLevel logLevel = LogLevel.DEBUG;
        QSLogger$logTileDestroyed$2 qSLogger$logTileDestroyed$2 = QSLogger$logTileDestroyed$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("QSLog", logLevel, qSLogger$logTileDestroyed$2);
            obtain.str1 = str;
            obtain.str2 = str2;
            logBuffer.push(obtain);
        }
    }

    public final void logTileLongClick(String str, int i, int i2) {
        LogLevel logLevel = LogLevel.DEBUG;
        QSLogger$logTileLongClick$2 qSLogger$logTileLongClick$2 = QSLogger$logTileLongClick$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("QSLog", logLevel, qSLogger$logTileLongClick$2);
            obtain.str1 = str;
            obtain.int1 = i;
            obtain.str2 = R$id.toShortString(i);
            obtain.str3 = toStateString(i2);
            logBuffer.push(obtain);
        }
    }

    public final void logTileSecondaryClick(String str, int i, int i2) {
        LogLevel logLevel = LogLevel.DEBUG;
        QSLogger$logTileSecondaryClick$2 qSLogger$logTileSecondaryClick$2 = QSLogger$logTileSecondaryClick$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("QSLog", logLevel, qSLogger$logTileSecondaryClick$2);
            obtain.str1 = str;
            obtain.int1 = i;
            obtain.str2 = R$id.toShortString(i);
            obtain.str3 = toStateString(i2);
            logBuffer.push(obtain);
        }
    }

    public final void logTileUpdated(String str, QSTile.State state) {
        String str2;
        LogLevel logLevel = LogLevel.VERBOSE;
        QSLogger$logTileUpdated$2 qSLogger$logTileUpdated$2 = QSLogger$logTileUpdated$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("QSLog", logLevel, qSLogger$logTileUpdated$2);
            obtain.str1 = str;
            CharSequence charSequence = state.label;
            String str3 = null;
            if (charSequence == null) {
                str2 = null;
            } else {
                str2 = charSequence.toString();
            }
            obtain.str2 = str2;
            QSTile.Icon icon = state.icon;
            if (icon != null) {
                str3 = icon.toString();
            }
            obtain.str3 = str3;
            obtain.int1 = state.state;
            if (state instanceof QSTile.SignalState) {
                obtain.bool1 = true;
                QSTile.SignalState signalState = (QSTile.SignalState) state;
                obtain.bool2 = signalState.activityIn;
                obtain.bool3 = signalState.activityOut;
            }
            logBuffer.push(obtain);
        }
    }

    public QSLogger(LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }
}
