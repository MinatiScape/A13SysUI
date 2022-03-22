package com.android.systemui.statusbar.notification.row;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class NotifBindPipeline {
    public final AnonymousClass1 mCollectionListener;
    public final NotifBindPipelineLogger mLogger;
    public final NotifBindPipelineHandler mMainHandler;
    public BindStage mStage;
    public final ArrayMap mBindEntries = new ArrayMap();
    public final ArrayList mScratchCallbacksList = new ArrayList();

    /* loaded from: classes.dex */
    public interface BindCallback {
        void onBindFinished(NotificationEntry notificationEntry);
    }

    /* loaded from: classes.dex */
    public class BindEntry {
        public final ArraySet callbacks = new ArraySet();
        public boolean invalidated;
        public ExpandableNotificationRow row;
    }

    /* loaded from: classes.dex */
    public class NotifBindPipelineHandler extends Handler {
        public NotifBindPipelineHandler(Looper looper) {
            super(looper);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r6v1, types: [com.android.systemui.statusbar.notification.row.RowContentBindStage$1] */
        /* JADX WARN: Unknown variable types count: 1 */
        @Override // android.os.Handler
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void handleMessage(android.os.Message r10) {
            /*
                r9 = this;
                int r0 = r10.what
                r1 = 1
                if (r0 != r1) goto L_0x00af
                java.lang.Object r10 = r10.obj
                r1 = r10
                com.android.systemui.statusbar.notification.collection.NotificationEntry r1 = (com.android.systemui.statusbar.notification.collection.NotificationEntry) r1
                com.android.systemui.statusbar.notification.row.NotifBindPipeline r9 = com.android.systemui.statusbar.notification.row.NotifBindPipeline.this
                java.util.Objects.requireNonNull(r9)
                com.android.systemui.statusbar.notification.row.NotifBindPipelineLogger r10 = r9.mLogger
                java.util.Objects.requireNonNull(r1)
                java.lang.String r0 = r1.mKey
                java.util.Objects.requireNonNull(r10)
                com.android.systemui.log.LogBuffer r10 = r10.buffer
                com.android.systemui.log.LogLevel r2 = com.android.systemui.log.LogLevel.INFO
                com.android.systemui.statusbar.notification.row.NotifBindPipelineLogger$logStartPipeline$2 r3 = com.android.systemui.statusbar.notification.row.NotifBindPipelineLogger$logStartPipeline$2.INSTANCE
                java.util.Objects.requireNonNull(r10)
                boolean r4 = r10.frozen
                if (r4 != 0) goto L_0x0031
                java.lang.String r4 = "NotifBindPipeline"
                com.android.systemui.log.LogMessageImpl r3 = r10.obtain(r4, r2, r3)
                r3.str1 = r0
                r10.push(r3)
            L_0x0031:
                com.android.systemui.statusbar.notification.row.BindStage r10 = r9.mStage
                if (r10 == 0) goto L_0x00a7
                android.util.ArrayMap r10 = r9.mBindEntries
                java.lang.Object r10 = r10.get(r1)
                com.android.systemui.statusbar.notification.row.NotifBindPipeline$BindEntry r10 = (com.android.systemui.statusbar.notification.row.NotifBindPipeline.BindEntry) r10
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r10 = r10.row
                com.android.systemui.statusbar.notification.row.BindStage r0 = r9.mStage
                com.google.android.systemui.assist.uihints.NgaUiController$$ExternalSyntheticLambda3 r3 = new com.google.android.systemui.assist.uihints.NgaUiController$$ExternalSyntheticLambda3
                r3.<init>(r9)
                com.android.systemui.statusbar.notification.row.RowContentBindStage r0 = (com.android.systemui.statusbar.notification.row.RowContentBindStage) r0
                java.util.Objects.requireNonNull(r0)
                java.lang.Object r9 = r0.getStageParams(r1)
                com.android.systemui.statusbar.notification.row.RowContentBindParams r9 = (com.android.systemui.statusbar.notification.row.RowContentBindParams) r9
                com.android.systemui.statusbar.notification.row.RowContentBindStageLogger r4 = r0.mLogger
                java.lang.String r5 = r1.mKey
                java.lang.String r6 = r9.toString()
                java.util.Objects.requireNonNull(r4)
                com.android.systemui.log.LogBuffer r4 = r4.buffer
                com.android.systemui.statusbar.notification.row.RowContentBindStageLogger$logStageParams$2 r7 = com.android.systemui.statusbar.notification.row.RowContentBindStageLogger$logStageParams$2.INSTANCE
                java.util.Objects.requireNonNull(r4)
                boolean r8 = r4.frozen
                if (r8 != 0) goto L_0x0074
                java.lang.String r8 = "RowContentBindStage"
                com.android.systemui.log.LogMessageImpl r2 = r4.obtain(r8, r2, r7)
                r2.str1 = r5
                r2.str2 = r6
                r4.push(r2)
            L_0x0074:
                int r2 = r9.mContentViews
                int r4 = r9.mDirtyContentViews
                r4 = r4 & r2
                r2 = r2 ^ 15
                com.android.systemui.statusbar.notification.row.NotificationRowContentBinder r5 = r0.mBinder
                r5.unbindContent(r1, r10, r2)
                com.android.systemui.statusbar.notification.row.NotificationRowContentBinder$BindParams r5 = new com.android.systemui.statusbar.notification.row.NotificationRowContentBinder$BindParams
                r5.<init>()
                boolean r2 = r9.mUseLowPriority
                r5.isLowPriority = r2
                boolean r2 = r9.mUseIncreasedHeight
                r5.usesIncreasedHeight = r2
                boolean r2 = r9.mUseIncreasedHeadsUpHeight
                r5.usesIncreasedHeadsUpHeight = r2
                boolean r9 = r9.mViewsNeedReinflation
                com.android.systemui.statusbar.notification.row.RowContentBindStage$1 r6 = new com.android.systemui.statusbar.notification.row.RowContentBindStage$1
                r6.<init>(r0, r3)
                com.android.systemui.statusbar.notification.row.NotificationRowContentBinder r2 = r0.mBinder
                r2.cancelBind(r1)
                com.android.systemui.statusbar.notification.row.NotificationRowContentBinder r0 = r0.mBinder
                r2 = r10
                r3 = r4
                r4 = r5
                r5 = r9
                r0.bindContent(r1, r2, r3, r4, r5, r6)
                return
            L_0x00a7:
                java.lang.IllegalStateException r9 = new java.lang.IllegalStateException
                java.lang.String r10 = "No stage was ever set on the pipeline"
                r9.<init>(r10)
                throw r9
            L_0x00af:
                java.lang.IllegalArgumentException r9 = new java.lang.IllegalArgumentException
                java.lang.String r0 = "Unknown message type: "
                java.lang.StringBuilder r0 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1.m(r0)
                int r10 = r10.what
                r0.append(r10)
                java.lang.String r10 = r0.toString()
                r9.<init>(r10)
                throw r9
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.NotifBindPipeline.NotifBindPipelineHandler.handleMessage(android.os.Message):void");
        }
    }

    public final void manageRow(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow) {
        NotifBindPipelineLogger notifBindPipelineLogger = this.mLogger;
        String str = notificationEntry.mKey;
        Objects.requireNonNull(notifBindPipelineLogger);
        LogBuffer logBuffer = notifBindPipelineLogger.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifBindPipelineLogger$logManagedRow$2 notifBindPipelineLogger$logManagedRow$2 = NotifBindPipelineLogger$logManagedRow$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifBindPipeline", logLevel, notifBindPipelineLogger$logManagedRow$2);
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
        BindEntry bindEntry = (BindEntry) this.mBindEntries.get(notificationEntry);
        if (bindEntry != null) {
            bindEntry.row = expandableNotificationRow;
            if (bindEntry.invalidated) {
                requestPipelineRun(notificationEntry);
            }
        }
    }

    public final void requestPipelineRun(NotificationEntry notificationEntry) {
        NotifBindPipelineLogger notifBindPipelineLogger = this.mLogger;
        Objects.requireNonNull(notificationEntry);
        String str = notificationEntry.mKey;
        Objects.requireNonNull(notifBindPipelineLogger);
        LogBuffer logBuffer = notifBindPipelineLogger.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifBindPipelineLogger$logRequestPipelineRun$2 notifBindPipelineLogger$logRequestPipelineRun$2 = NotifBindPipelineLogger$logRequestPipelineRun$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifBindPipeline", logLevel, notifBindPipelineLogger$logRequestPipelineRun$2);
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
        if (((BindEntry) this.mBindEntries.get(notificationEntry)).row == null) {
            NotifBindPipelineLogger notifBindPipelineLogger2 = this.mLogger;
            String str2 = notificationEntry.mKey;
            Objects.requireNonNull(notifBindPipelineLogger2);
            LogBuffer logBuffer2 = notifBindPipelineLogger2.buffer;
            NotifBindPipelineLogger$logRequestPipelineRowNotSet$2 notifBindPipelineLogger$logRequestPipelineRowNotSet$2 = NotifBindPipelineLogger$logRequestPipelineRowNotSet$2.INSTANCE;
            Objects.requireNonNull(logBuffer2);
            if (!logBuffer2.frozen) {
                LogMessageImpl obtain2 = logBuffer2.obtain("NotifBindPipeline", logLevel, notifBindPipelineLogger$logRequestPipelineRowNotSet$2);
                obtain2.str1 = str2;
                logBuffer2.push(obtain2);
                return;
            }
            return;
        }
        RowContentBindStage rowContentBindStage = (RowContentBindStage) this.mStage;
        Objects.requireNonNull(rowContentBindStage);
        rowContentBindStage.mBinder.cancelBind(notificationEntry);
        if (!this.mMainHandler.hasMessages(1, notificationEntry)) {
            this.mMainHandler.sendMessage(Message.obtain(this.mMainHandler, 1, notificationEntry));
        }
    }

    public static void $r8$lambda$QRo7GExQEDf4mZLfcJ0VhAf4hBg(NotifBindPipeline notifBindPipeline, NotificationEntry notificationEntry) {
        Objects.requireNonNull(notifBindPipeline);
        BindEntry bindEntry = (BindEntry) notifBindPipeline.mBindEntries.get(notificationEntry);
        ArraySet arraySet = bindEntry.callbacks;
        NotifBindPipelineLogger notifBindPipelineLogger = notifBindPipeline.mLogger;
        Objects.requireNonNull(notificationEntry);
        String str = notificationEntry.mKey;
        int size = arraySet.size();
        Objects.requireNonNull(notifBindPipelineLogger);
        LogBuffer logBuffer = notifBindPipelineLogger.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifBindPipelineLogger$logFinishedPipeline$2 notifBindPipelineLogger$logFinishedPipeline$2 = NotifBindPipelineLogger$logFinishedPipeline$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifBindPipeline", logLevel, notifBindPipelineLogger$logFinishedPipeline$2);
            obtain.str1 = str;
            obtain.int1 = size;
            logBuffer.push(obtain);
        }
        bindEntry.invalidated = false;
        notifBindPipeline.mScratchCallbacksList.addAll(arraySet);
        arraySet.clear();
        for (int i = 0; i < notifBindPipeline.mScratchCallbacksList.size(); i++) {
            ((BindCallback) notifBindPipeline.mScratchCallbacksList.get(i)).onBindFinished(notificationEntry);
        }
        notifBindPipeline.mScratchCallbacksList.clear();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.systemui.statusbar.notification.row.NotifBindPipeline$1, com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public NotifBindPipeline(com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection r2, com.android.systemui.statusbar.notification.row.NotifBindPipelineLogger r3, android.os.Looper r4) {
        /*
            r1 = this;
            r1.<init>()
            android.util.ArrayMap r0 = new android.util.ArrayMap
            r0.<init>()
            r1.mBindEntries = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r1.mScratchCallbacksList = r0
            com.android.systemui.statusbar.notification.row.NotifBindPipeline$1 r0 = new com.android.systemui.statusbar.notification.row.NotifBindPipeline$1
            r0.<init>()
            r1.mCollectionListener = r0
            r2.addCollectionListener(r0)
            r1.mLogger = r3
            com.android.systemui.statusbar.notification.row.NotifBindPipeline$NotifBindPipelineHandler r2 = new com.android.systemui.statusbar.notification.row.NotifBindPipeline$NotifBindPipelineHandler
            r2.<init>(r4)
            r1.mMainHandler = r2
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.NotifBindPipeline.<init>(com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection, com.android.systemui.statusbar.notification.row.NotifBindPipelineLogger, android.os.Looper):void");
    }
}
