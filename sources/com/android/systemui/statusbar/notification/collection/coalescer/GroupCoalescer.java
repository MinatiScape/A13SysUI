package com.android.systemui.statusbar.notification.collection.coalescer;

import android.app.NotificationChannel;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.UserHandle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline1;
import com.android.systemui.Dumpable;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionLogger;
import com.android.systemui.tuner.NavBarTuner$$ExternalSyntheticLambda6;
import com.android.systemui.util.Assert;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class GroupCoalescer implements Dumpable {
    public final SystemClock mClock;
    public BatchableNotificationHandler mHandler;
    public final GroupCoalescerLogger mLogger;
    public final DelayableExecutor mMainExecutor;
    public final ArrayMap mCoalescedEvents = new ArrayMap();
    public final ArrayMap mBatches = new ArrayMap();
    public final AnonymousClass1 mListener = new NotificationListener.NotificationHandler() { // from class: com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescer.1
        @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
        public final void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
            ((NotifCollection.AnonymousClass1) GroupCoalescer.this.mHandler).onNotificationChannelModified(str, userHandle, notificationChannel, i);
        }

        @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
        public final void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
            boolean z;
            GroupCoalescer.m92$$Nest$mmaybeEmitBatch(GroupCoalescer.this, statusBarNotification);
            GroupCoalescer.m91$$Nest$mapplyRanking(GroupCoalescer.this, rankingMap);
            GroupCoalescer groupCoalescer = GroupCoalescer.this;
            Objects.requireNonNull(groupCoalescer);
            if (!groupCoalescer.mCoalescedEvents.containsKey(statusBarNotification.getKey())) {
                if (statusBarNotification.isGroup()) {
                    String groupKey = statusBarNotification.getGroupKey();
                    EventBatch eventBatch = (EventBatch) groupCoalescer.mBatches.get(groupKey);
                    if (eventBatch == null) {
                        eventBatch = new EventBatch(groupCoalescer.mClock.uptimeMillis(), groupKey);
                        groupCoalescer.mBatches.put(groupKey, eventBatch);
                    }
                    String key = statusBarNotification.getKey();
                    int size = eventBatch.mMembers.size();
                    String key2 = statusBarNotification.getKey();
                    NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
                    if (rankingMap.getRanking(key2, ranking)) {
                        CoalescedEvent coalescedEvent = new CoalescedEvent(key, size, statusBarNotification, ranking, eventBatch);
                        groupCoalescer.mCoalescedEvents.put(key, coalescedEvent);
                        eventBatch.mMembers.add(coalescedEvent);
                        Runnable runnable = eventBatch.mCancelShortTimeout;
                        if (runnable != null) {
                            runnable.run();
                        }
                        eventBatch.mCancelShortTimeout = groupCoalescer.mMainExecutor.executeDelayed(new NavBarTuner$$ExternalSyntheticLambda6(groupCoalescer, eventBatch, 4), groupCoalescer.mMinGroupLingerDuration);
                        z = true;
                    } else {
                        throw new IllegalArgumentException(SupportMenuInflater$$ExternalSyntheticOutline0.m("Ranking map does not contain key ", key2));
                    }
                } else {
                    z = false;
                }
                if (z) {
                    GroupCoalescerLogger groupCoalescerLogger = GroupCoalescer.this.mLogger;
                    String key3 = statusBarNotification.getKey();
                    Objects.requireNonNull(groupCoalescerLogger);
                    LogBuffer logBuffer = groupCoalescerLogger.buffer;
                    LogLevel logLevel = LogLevel.INFO;
                    GroupCoalescerLogger$logEventCoalesced$2 groupCoalescerLogger$logEventCoalesced$2 = GroupCoalescerLogger$logEventCoalesced$2.INSTANCE;
                    Objects.requireNonNull(logBuffer);
                    if (!logBuffer.frozen) {
                        LogMessageImpl obtain = logBuffer.obtain("GroupCoalescer", logLevel, groupCoalescerLogger$logEventCoalesced$2);
                        obtain.str1 = key3;
                        logBuffer.push(obtain);
                    }
                    ((NotifCollection.AnonymousClass1) GroupCoalescer.this.mHandler).onNotificationRankingUpdate(rankingMap);
                    return;
                }
                ((NotifCollection.AnonymousClass1) GroupCoalescer.this.mHandler).onNotificationPosted(statusBarNotification, rankingMap);
                return;
            }
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Notification has already been coalesced: ");
            m.append(statusBarNotification.getKey());
            throw new IllegalStateException(m.toString());
        }

        @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
        public final void onNotificationRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
            GroupCoalescer.m91$$Nest$mapplyRanking(GroupCoalescer.this, rankingMap);
            ((NotifCollection.AnonymousClass1) GroupCoalescer.this.mHandler).onNotificationRankingUpdate(rankingMap);
        }

        @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
        public final void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i) {
            GroupCoalescer.m92$$Nest$mmaybeEmitBatch(GroupCoalescer.this, statusBarNotification);
            GroupCoalescer.m91$$Nest$mapplyRanking(GroupCoalescer.this, rankingMap);
            ((NotifCollection.AnonymousClass1) GroupCoalescer.this.mHandler).onNotificationRemoved(statusBarNotification, rankingMap, i);
        }

        @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
        public final void onNotificationsInitialized() {
            ((NotifCollection.AnonymousClass1) GroupCoalescer.this.mHandler).onNotificationsInitialized();
        }
    };
    public final GroupCoalescer$$ExternalSyntheticLambda0 mEventComparator = GroupCoalescer$$ExternalSyntheticLambda0.INSTANCE;
    public final long mMinGroupLingerDuration = 200;
    public final long mMaxGroupLingerDuration = 500;

    /* loaded from: classes.dex */
    public interface BatchableNotificationHandler extends NotificationListener.NotificationHandler {
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        long uptimeMillis = this.mClock.uptimeMillis();
        printWriter.println();
        printWriter.println("Coalesced notifications:");
        int i = 0;
        for (EventBatch eventBatch : this.mBatches.values()) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("   Batch ");
            m.append(eventBatch.mGroupKey);
            m.append(":");
            printWriter.println(m.toString());
            printWriter.println("       Created " + (uptimeMillis - eventBatch.mCreatedTimestamp) + "ms ago");
            Iterator it = eventBatch.mMembers.iterator();
            while (it.hasNext()) {
                CoalescedEvent coalescedEvent = (CoalescedEvent) it.next();
                StringBuilder sb = new StringBuilder();
                sb.append("       ");
                Objects.requireNonNull(coalescedEvent);
                sb.append(coalescedEvent.key);
                printWriter.println(sb.toString());
                i++;
            }
        }
        if (i != this.mCoalescedEvents.size()) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("    ERROR: batches contain ");
            m2.append(this.mCoalescedEvents.size());
            m2.append(" events but am tracking ");
            m2.append(this.mCoalescedEvents.size());
            m2.append(" total events");
            printWriter.println(m2.toString());
            printWriter.println("    All tracked events:");
            for (CoalescedEvent coalescedEvent2 : this.mCoalescedEvents.values()) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("        ");
                Objects.requireNonNull(coalescedEvent2);
                sb2.append(coalescedEvent2.key);
                printWriter.println(sb2.toString());
            }
        }
    }

    public final void emitBatch(EventBatch eventBatch) {
        if (eventBatch != this.mBatches.get(eventBatch.mGroupKey)) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Cannot emit out-of-date batch ");
            m.append(eventBatch.mGroupKey);
            throw new IllegalStateException(m.toString());
        } else if (!eventBatch.mMembers.isEmpty()) {
            Runnable runnable = eventBatch.mCancelShortTimeout;
            if (runnable != null) {
                runnable.run();
                eventBatch.mCancelShortTimeout = null;
            }
            this.mBatches.remove(eventBatch.mGroupKey);
            ArrayList arrayList = new ArrayList(eventBatch.mMembers);
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                CoalescedEvent coalescedEvent = (CoalescedEvent) it.next();
                ArrayMap arrayMap = this.mCoalescedEvents;
                Objects.requireNonNull(coalescedEvent);
                arrayMap.remove(coalescedEvent.key);
                coalescedEvent.batch = null;
            }
            arrayList.sort(this.mEventComparator);
            long uptimeMillis = this.mClock.uptimeMillis() - eventBatch.mCreatedTimestamp;
            GroupCoalescerLogger groupCoalescerLogger = this.mLogger;
            String str = eventBatch.mGroupKey;
            int size = eventBatch.mMembers.size();
            Objects.requireNonNull(groupCoalescerLogger);
            LogBuffer logBuffer = groupCoalescerLogger.buffer;
            LogLevel logLevel = LogLevel.DEBUG;
            GroupCoalescerLogger$logEmitBatch$2 groupCoalescerLogger$logEmitBatch$2 = GroupCoalescerLogger$logEmitBatch$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                LogMessageImpl obtain = logBuffer.obtain("GroupCoalescer", logLevel, groupCoalescerLogger$logEmitBatch$2);
                obtain.str1 = str;
                obtain.int1 = size;
                obtain.long1 = uptimeMillis;
                logBuffer.push(obtain);
            }
            NotifCollection.AnonymousClass1 r8 = (NotifCollection.AnonymousClass1) this.mHandler;
            Objects.requireNonNull(r8);
            NotifCollection notifCollection = NotifCollection.this;
            Objects.requireNonNull(notifCollection);
            Assert.isMainThread();
            NotifCollectionLogger notifCollectionLogger = notifCollection.mLogger;
            CoalescedEvent coalescedEvent2 = (CoalescedEvent) arrayList.get(0);
            Objects.requireNonNull(coalescedEvent2);
            notifCollectionLogger.logNotifGroupPosted(coalescedEvent2.sbn.getGroupKey(), arrayList.size());
            Iterator it2 = arrayList.iterator();
            while (it2.hasNext()) {
                CoalescedEvent coalescedEvent3 = (CoalescedEvent) it2.next();
                Objects.requireNonNull(coalescedEvent3);
                notifCollection.postNotification(coalescedEvent3.sbn, coalescedEvent3.ranking);
            }
            notifCollection.dispatchEventsAndRebuildList();
        } else {
            throw new IllegalStateException(MotionController$$ExternalSyntheticOutline1.m(VendorAtomValue$$ExternalSyntheticOutline1.m("Batch "), eventBatch.mGroupKey, " cannot be empty"));
        }
    }

    /* renamed from: -$$Nest$mapplyRanking  reason: not valid java name */
    public static void m91$$Nest$mapplyRanking(GroupCoalescer groupCoalescer, NotificationListenerService.RankingMap rankingMap) {
        Objects.requireNonNull(groupCoalescer);
        for (CoalescedEvent coalescedEvent : groupCoalescer.mCoalescedEvents.values()) {
            NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
            Objects.requireNonNull(coalescedEvent);
            if (rankingMap.getRanking(coalescedEvent.key, ranking)) {
                coalescedEvent.ranking = ranking;
            } else {
                GroupCoalescerLogger groupCoalescerLogger = groupCoalescer.mLogger;
                String str = coalescedEvent.key;
                Objects.requireNonNull(groupCoalescerLogger);
                LogBuffer logBuffer = groupCoalescerLogger.buffer;
                LogLevel logLevel = LogLevel.WARNING;
                GroupCoalescerLogger$logMissingRanking$2 groupCoalescerLogger$logMissingRanking$2 = GroupCoalescerLogger$logMissingRanking$2.INSTANCE;
                Objects.requireNonNull(logBuffer);
                if (!logBuffer.frozen) {
                    LogMessageImpl obtain = logBuffer.obtain("GroupCoalescer", logLevel, groupCoalescerLogger$logMissingRanking$2);
                    obtain.str1 = str;
                    logBuffer.push(obtain);
                }
            }
        }
    }

    /* renamed from: -$$Nest$mmaybeEmitBatch  reason: not valid java name */
    public static void m92$$Nest$mmaybeEmitBatch(GroupCoalescer groupCoalescer, StatusBarNotification statusBarNotification) {
        Objects.requireNonNull(groupCoalescer);
        CoalescedEvent coalescedEvent = (CoalescedEvent) groupCoalescer.mCoalescedEvents.get(statusBarNotification.getKey());
        EventBatch eventBatch = (EventBatch) groupCoalescer.mBatches.get(statusBarNotification.getGroupKey());
        if (coalescedEvent != null) {
            GroupCoalescerLogger groupCoalescerLogger = groupCoalescer.mLogger;
            String key = statusBarNotification.getKey();
            EventBatch eventBatch2 = coalescedEvent.batch;
            Objects.requireNonNull(eventBatch2);
            String str = eventBatch2.mGroupKey;
            Objects.requireNonNull(groupCoalescerLogger);
            LogBuffer logBuffer = groupCoalescerLogger.buffer;
            LogLevel logLevel = LogLevel.DEBUG;
            GroupCoalescerLogger$logEarlyEmit$2 groupCoalescerLogger$logEarlyEmit$2 = GroupCoalescerLogger$logEarlyEmit$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                LogMessageImpl obtain = logBuffer.obtain("GroupCoalescer", logLevel, groupCoalescerLogger$logEarlyEmit$2);
                obtain.str1 = key;
                obtain.str2 = str;
                logBuffer.push(obtain);
            }
            EventBatch eventBatch3 = coalescedEvent.batch;
            Objects.requireNonNull(eventBatch3);
            groupCoalescer.emitBatch(eventBatch3);
        } else if (eventBatch != null && groupCoalescer.mClock.uptimeMillis() - eventBatch.mCreatedTimestamp >= groupCoalescer.mMaxGroupLingerDuration) {
            GroupCoalescerLogger groupCoalescerLogger2 = groupCoalescer.mLogger;
            String key2 = statusBarNotification.getKey();
            String str2 = eventBatch.mGroupKey;
            Objects.requireNonNull(groupCoalescerLogger2);
            LogBuffer logBuffer2 = groupCoalescerLogger2.buffer;
            LogLevel logLevel2 = LogLevel.INFO;
            GroupCoalescerLogger$logMaxBatchTimeout$2 groupCoalescerLogger$logMaxBatchTimeout$2 = GroupCoalescerLogger$logMaxBatchTimeout$2.INSTANCE;
            Objects.requireNonNull(logBuffer2);
            if (!logBuffer2.frozen) {
                LogMessageImpl obtain2 = logBuffer2.obtain("GroupCoalescer", logLevel2, groupCoalescerLogger$logMaxBatchTimeout$2);
                obtain2.str1 = key2;
                obtain2.str2 = str2;
                logBuffer2.push(obtain2);
            }
            groupCoalescer.emitBatch(eventBatch);
        }
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescer$1] */
    public GroupCoalescer(DelayableExecutor delayableExecutor, SystemClock systemClock, GroupCoalescerLogger groupCoalescerLogger) {
        this.mMainExecutor = delayableExecutor;
        this.mClock = systemClock;
        this.mLogger = groupCoalescerLogger;
    }
}
