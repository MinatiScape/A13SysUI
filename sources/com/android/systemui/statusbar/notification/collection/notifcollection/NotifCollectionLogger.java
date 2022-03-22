package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.os.RemoteException;
import android.service.notification.NotificationListenerService;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.Objects;
/* compiled from: NotifCollectionLogger.kt */
/* loaded from: classes.dex */
public final class NotifCollectionLogger {
    public final LogBuffer buffer;

    public final void logChildDismissed(NotificationEntry notificationEntry) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotifCollectionLogger$logChildDismissed$2 notifCollectionLogger$logChildDismissed$2 = NotifCollectionLogger$logChildDismissed$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logChildDismissed$2);
            obtain.str1 = notificationEntry.mKey;
            logBuffer.push(obtain);
        }
    }

    public final void logDismissAll(int i) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logDismissAll$2 notifCollectionLogger$logDismissAll$2 = NotifCollectionLogger$logDismissAll$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logDismissAll$2);
            obtain.int1 = i;
            logBuffer.push(obtain);
        }
    }

    public final void logDismissOnAlreadyCanceledEntry(NotificationEntry notificationEntry) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotifCollectionLogger$logDismissOnAlreadyCanceledEntry$2 notifCollectionLogger$logDismissOnAlreadyCanceledEntry$2 = NotifCollectionLogger$logDismissOnAlreadyCanceledEntry$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logDismissOnAlreadyCanceledEntry$2);
            obtain.str1 = notificationEntry.mKey;
            logBuffer.push(obtain);
        }
    }

    public final void logLifetimeExtended(String str, NotifLifetimeExtender notifLifetimeExtender) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logLifetimeExtended$2 notifCollectionLogger$logLifetimeExtended$2 = NotifCollectionLogger$logLifetimeExtended$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logLifetimeExtended$2);
            obtain.str1 = str;
            obtain.str2 = notifLifetimeExtender.getName();
            logBuffer.push(obtain);
        }
    }

    public final void logLifetimeExtensionEnded(String str, NotifLifetimeExtender notifLifetimeExtender, int i) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logLifetimeExtensionEnded$2 notifCollectionLogger$logLifetimeExtensionEnded$2 = NotifCollectionLogger$logLifetimeExtensionEnded$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logLifetimeExtensionEnded$2);
            obtain.str1 = str;
            obtain.str2 = notifLifetimeExtender.getName();
            obtain.int1 = i;
            logBuffer.push(obtain);
        }
    }

    public final void logNoNotificationToRemoveWithKey(String str, int i) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.ERROR;
        NotifCollectionLogger$logNoNotificationToRemoveWithKey$2 notifCollectionLogger$logNoNotificationToRemoveWithKey$2 = NotifCollectionLogger$logNoNotificationToRemoveWithKey$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNoNotificationToRemoveWithKey$2);
            obtain.str1 = str;
            obtain.int1 = i;
            logBuffer.push(obtain);
        }
    }

    public final void logNonExistentNotifDismissed(String str) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNonExistentNotifDismissed$2 notifCollectionLogger$logNonExistentNotifDismissed$2 = NotifCollectionLogger$logNonExistentNotifDismissed$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNonExistentNotifDismissed$2);
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
    }

    public final void logNotifClearAllDismissalIntercepted(String str) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifClearAllDismissalIntercepted$2 notifCollectionLogger$logNotifClearAllDismissalIntercepted$2 = NotifCollectionLogger$logNotifClearAllDismissalIntercepted$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifClearAllDismissalIntercepted$2);
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
    }

    public final void logNotifDismissed(String str) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifDismissed$2 notifCollectionLogger$logNotifDismissed$2 = NotifCollectionLogger$logNotifDismissed$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifDismissed$2);
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
    }

    public final void logNotifDismissedIntercepted(String str) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifDismissedIntercepted$2 notifCollectionLogger$logNotifDismissedIntercepted$2 = NotifCollectionLogger$logNotifDismissedIntercepted$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifDismissedIntercepted$2);
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
    }

    public final void logNotifGroupPosted(String str, int i) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifGroupPosted$2 notifCollectionLogger$logNotifGroupPosted$2 = NotifCollectionLogger$logNotifGroupPosted$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifGroupPosted$2);
            obtain.str1 = str;
            obtain.int1 = i;
            logBuffer.push(obtain);
        }
    }

    public final void logNotifInternalUpdate(String str, String str2, String str3) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifInternalUpdate$2 notifCollectionLogger$logNotifInternalUpdate$2 = NotifCollectionLogger$logNotifInternalUpdate$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifInternalUpdate$2);
            obtain.str1 = str;
            obtain.str2 = str2;
            obtain.str3 = str3;
            logBuffer.push(obtain);
        }
    }

    public final void logNotifInternalUpdateFailed(String str, String str2, String str3) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifInternalUpdateFailed$2 notifCollectionLogger$logNotifInternalUpdateFailed$2 = NotifCollectionLogger$logNotifInternalUpdateFailed$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifInternalUpdateFailed$2);
            obtain.str1 = str;
            obtain.str2 = str2;
            obtain.str3 = str3;
            logBuffer.push(obtain);
        }
    }

    public final void logNotifPosted(String str) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifPosted$2 notifCollectionLogger$logNotifPosted$2 = NotifCollectionLogger$logNotifPosted$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifPosted$2);
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
    }

    public final void logNotifReleased(String str) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifReleased$2 notifCollectionLogger$logNotifReleased$2 = NotifCollectionLogger$logNotifReleased$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifReleased$2);
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
    }

    public final void logNotifRemoved(String str, int i) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifRemoved$2 notifCollectionLogger$logNotifRemoved$2 = NotifCollectionLogger$logNotifRemoved$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifRemoved$2);
            obtain.str1 = str;
            obtain.int1 = i;
            logBuffer.push(obtain);
        }
    }

    public final void logNotifUpdated(String str) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifUpdated$2 notifCollectionLogger$logNotifUpdated$2 = NotifCollectionLogger$logNotifUpdated$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifUpdated$2);
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
    }

    public final void logRankingMissing(String str, NotificationListenerService.RankingMap rankingMap) {
        LogLevel logLevel = LogLevel.DEBUG;
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel2 = LogLevel.WARNING;
        NotifCollectionLogger$logRankingMissing$2 notifCollectionLogger$logRankingMissing$2 = NotifCollectionLogger$logRankingMissing$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel2, notifCollectionLogger$logRankingMissing$2);
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
        LogBuffer logBuffer2 = this.buffer;
        NotifCollectionLogger$logRankingMissing$4 notifCollectionLogger$logRankingMissing$4 = NotifCollectionLogger$logRankingMissing$4.INSTANCE;
        Objects.requireNonNull(logBuffer2);
        if (!logBuffer2.frozen) {
            logBuffer2.push(logBuffer2.obtain("NotifCollection", logLevel, notifCollectionLogger$logRankingMissing$4));
        }
        String[] orderedKeys = rankingMap.getOrderedKeys();
        int i = 0;
        int length = orderedKeys.length;
        while (i < length) {
            String str2 = orderedKeys[i];
            i++;
            LogBuffer logBuffer3 = this.buffer;
            NotifCollectionLogger$logRankingMissing$6 notifCollectionLogger$logRankingMissing$6 = NotifCollectionLogger$logRankingMissing$6.INSTANCE;
            Objects.requireNonNull(logBuffer3);
            if (!logBuffer3.frozen) {
                LogMessageImpl obtain2 = logBuffer3.obtain("NotifCollection", logLevel, notifCollectionLogger$logRankingMissing$6);
                obtain2.str1 = str2;
                logBuffer3.push(obtain2);
            }
        }
    }

    public final void logRemoteExceptionOnClearAllNotifications(RemoteException remoteException) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.WTF;
        NotifCollectionLogger$logRemoteExceptionOnClearAllNotifications$2 notifCollectionLogger$logRemoteExceptionOnClearAllNotifications$2 = NotifCollectionLogger$logRemoteExceptionOnClearAllNotifications$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logRemoteExceptionOnClearAllNotifications$2);
            obtain.str1 = remoteException.toString();
            logBuffer.push(obtain);
        }
    }

    public final void logRemoteExceptionOnNotificationClear(String str, RemoteException remoteException) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.WTF;
        NotifCollectionLogger$logRemoteExceptionOnNotificationClear$2 notifCollectionLogger$logRemoteExceptionOnNotificationClear$2 = NotifCollectionLogger$logRemoteExceptionOnNotificationClear$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logRemoteExceptionOnNotificationClear$2);
            obtain.str1 = str;
            obtain.str2 = remoteException.toString();
            logBuffer.push(obtain);
        }
    }

    public NotifCollectionLogger(LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }
}
