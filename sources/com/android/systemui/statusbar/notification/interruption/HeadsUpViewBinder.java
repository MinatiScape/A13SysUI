package com.android.systemui.statusbar.notification.interruption;

import android.util.ArrayMap;
import androidx.core.os.CancellationSignal;
import com.android.internal.util.NotificationMessagingUtil;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import com.android.systemui.statusbar.notification.row.RowContentBindParams;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import com.android.systemui.statusbar.phone.StatusBarNotificationPresenter;
import java.util.Objects;
/* loaded from: classes.dex */
public final class HeadsUpViewBinder {
    public final HeadsUpViewBinderLogger mLogger;
    public final NotificationMessagingUtil mNotificationMessagingUtil;
    public NotificationPresenter mNotificationPresenter;
    public final ArrayMap mOngoingBindCallbacks = new ArrayMap();
    public final RowContentBindStage mStage;

    public final void abortBindCallback(NotificationEntry notificationEntry) {
        CancellationSignal cancellationSignal = (CancellationSignal) this.mOngoingBindCallbacks.remove(notificationEntry);
        if (cancellationSignal != null) {
            HeadsUpViewBinderLogger headsUpViewBinderLogger = this.mLogger;
            Objects.requireNonNull(notificationEntry);
            String str = notificationEntry.mKey;
            Objects.requireNonNull(headsUpViewBinderLogger);
            LogBuffer logBuffer = headsUpViewBinderLogger.buffer;
            LogLevel logLevel = LogLevel.INFO;
            HeadsUpViewBinderLogger$currentOngoingBindingAborted$2 headsUpViewBinderLogger$currentOngoingBindingAborted$2 = HeadsUpViewBinderLogger$currentOngoingBindingAborted$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                LogMessageImpl obtain = logBuffer.obtain("HeadsUpViewBinder", logLevel, headsUpViewBinderLogger$currentOngoingBindingAborted$2);
                obtain.str1 = str;
                logBuffer.push(obtain);
            }
            cancellationSignal.cancel();
        }
    }

    public final void bindHeadsUpView(final NotificationEntry notificationEntry, final NotifBindPipeline.BindCallback bindCallback) {
        boolean z;
        final RowContentBindParams stageParams = this.mStage.getStageParams(notificationEntry);
        NotificationMessagingUtil notificationMessagingUtil = this.mNotificationMessagingUtil;
        Objects.requireNonNull(notificationEntry);
        if (!notificationMessagingUtil.isImportantMessaging(notificationEntry.mSbn, notificationEntry.getImportance()) || ((StatusBarNotificationPresenter) this.mNotificationPresenter).isPresenterFullyCollapsed()) {
            z = false;
        } else {
            z = true;
        }
        if (stageParams.mUseIncreasedHeadsUpHeight != z) {
            stageParams.mDirtyContentViews |= 4;
        }
        stageParams.mUseIncreasedHeadsUpHeight = z;
        stageParams.requireContentViews(4);
        CancellationSignal requestRebind = this.mStage.requestRebind(notificationEntry, new NotifBindPipeline.BindCallback() { // from class: com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinder$$ExternalSyntheticLambda1
            @Override // com.android.systemui.statusbar.notification.row.NotifBindPipeline.BindCallback
            public final void onBindFinished(NotificationEntry notificationEntry2) {
                HeadsUpViewBinder headsUpViewBinder = HeadsUpViewBinder.this;
                NotificationEntry notificationEntry3 = notificationEntry;
                RowContentBindParams rowContentBindParams = stageParams;
                NotifBindPipeline.BindCallback bindCallback2 = bindCallback;
                Objects.requireNonNull(headsUpViewBinder);
                HeadsUpViewBinderLogger headsUpViewBinderLogger = headsUpViewBinder.mLogger;
                Objects.requireNonNull(notificationEntry3);
                String str = notificationEntry3.mKey;
                Objects.requireNonNull(headsUpViewBinderLogger);
                LogBuffer logBuffer = headsUpViewBinderLogger.buffer;
                LogLevel logLevel = LogLevel.INFO;
                HeadsUpViewBinderLogger$entryBoundSuccessfully$2 headsUpViewBinderLogger$entryBoundSuccessfully$2 = HeadsUpViewBinderLogger$entryBoundSuccessfully$2.INSTANCE;
                Objects.requireNonNull(logBuffer);
                if (!logBuffer.frozen) {
                    LogMessageImpl obtain = logBuffer.obtain("HeadsUpViewBinder", logLevel, headsUpViewBinderLogger$entryBoundSuccessfully$2);
                    obtain.str1 = str;
                    logBuffer.push(obtain);
                }
                Objects.requireNonNull(notificationEntry2);
                ExpandableNotificationRow expandableNotificationRow = notificationEntry2.row;
                Objects.requireNonNull(rowContentBindParams);
                boolean z2 = rowContentBindParams.mUseIncreasedHeadsUpHeight;
                Objects.requireNonNull(expandableNotificationRow);
                expandableNotificationRow.mUseIncreasedHeadsUpHeight = z2;
                headsUpViewBinder.mOngoingBindCallbacks.remove(notificationEntry3);
                if (bindCallback2 != null) {
                    bindCallback2.onBindFinished(notificationEntry2);
                }
            }
        });
        abortBindCallback(notificationEntry);
        HeadsUpViewBinderLogger headsUpViewBinderLogger = this.mLogger;
        String str = notificationEntry.mKey;
        Objects.requireNonNull(headsUpViewBinderLogger);
        LogBuffer logBuffer = headsUpViewBinderLogger.buffer;
        LogLevel logLevel = LogLevel.INFO;
        HeadsUpViewBinderLogger$startBindingHun$2 headsUpViewBinderLogger$startBindingHun$2 = HeadsUpViewBinderLogger$startBindingHun$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("HeadsUpViewBinder", logLevel, headsUpViewBinderLogger$startBindingHun$2);
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
        this.mOngoingBindCallbacks.put(notificationEntry, requestRebind);
    }

    public HeadsUpViewBinder(NotificationMessagingUtil notificationMessagingUtil, RowContentBindStage rowContentBindStage, HeadsUpViewBinderLogger headsUpViewBinderLogger) {
        this.mNotificationMessagingUtil = notificationMessagingUtil;
        this.mStage = rowContentBindStage;
        this.mLogger = headsUpViewBinderLogger;
    }

    public final void unbindHeadsUpView(NotificationEntry notificationEntry) {
        abortBindCallback(notificationEntry);
        this.mStage.getStageParams(notificationEntry).markContentViewsFreeable(4);
        HeadsUpViewBinderLogger headsUpViewBinderLogger = this.mLogger;
        String str = notificationEntry.mKey;
        Objects.requireNonNull(headsUpViewBinderLogger);
        LogBuffer logBuffer = headsUpViewBinderLogger.buffer;
        LogLevel logLevel = LogLevel.INFO;
        HeadsUpViewBinderLogger$entryContentViewMarkedFreeable$2 headsUpViewBinderLogger$entryContentViewMarkedFreeable$2 = HeadsUpViewBinderLogger$entryContentViewMarkedFreeable$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("HeadsUpViewBinder", logLevel, headsUpViewBinderLogger$entryContentViewMarkedFreeable$2);
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
        this.mStage.requestRebind(notificationEntry, new NotifBindPipeline.BindCallback() { // from class: com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinder$$ExternalSyntheticLambda0
            @Override // com.android.systemui.statusbar.notification.row.NotifBindPipeline.BindCallback
            public final void onBindFinished(NotificationEntry notificationEntry2) {
                HeadsUpViewBinder headsUpViewBinder = HeadsUpViewBinder.this;
                Objects.requireNonNull(headsUpViewBinder);
                HeadsUpViewBinderLogger headsUpViewBinderLogger2 = headsUpViewBinder.mLogger;
                Objects.requireNonNull(notificationEntry2);
                String str2 = notificationEntry2.mKey;
                Objects.requireNonNull(headsUpViewBinderLogger2);
                LogBuffer logBuffer2 = headsUpViewBinderLogger2.buffer;
                LogLevel logLevel2 = LogLevel.INFO;
                HeadsUpViewBinderLogger$entryUnbound$2 headsUpViewBinderLogger$entryUnbound$2 = HeadsUpViewBinderLogger$entryUnbound$2.INSTANCE;
                Objects.requireNonNull(logBuffer2);
                if (!logBuffer2.frozen) {
                    LogMessageImpl obtain2 = logBuffer2.obtain("HeadsUpViewBinder", logLevel2, headsUpViewBinderLogger$entryUnbound$2);
                    obtain2.str1 = str2;
                    logBuffer2.push(obtain2);
                }
            }
        });
    }
}
