package com.android.systemui.statusbar.notification;

import android.app.NotificationChannel;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Trace;
import android.os.UserHandle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.NotificationLifetimeExtender;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationRemoveInterceptor;
import com.android.systemui.statusbar.NotificationUiAdjustment;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStoreImpl;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinder;
import com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRanker;
import com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRankerStub;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.icon.IconPack;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationRowContentBinder;
import com.android.systemui.statusbar.phone.StatusBarNotificationPresenter;
import com.android.systemui.util.Assert;
import com.android.systemui.util.leak.LeakDetector;
import com.android.systemui.util.leak.TrackedObjects;
import com.android.systemui.util.leak.WeakIdentityHashMap;
import dagger.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes.dex */
public final class NotificationEntryManager implements CommonNotifCollection, Dumpable, VisualStabilityManager.Callback {
    public final ArraySet mAllNotifications;
    public final DumpManager mDumpManager;
    public final NotificationGroupManagerLegacy mGroupManager;
    public NotificationListenerService.RankingMap mLatestRankingMap;
    public final LeakDetector mLeakDetector;
    public final NotificationEntryManagerLogger mLogger;
    public final NotifLiveDataStoreImpl mNotifLiveDataStore;
    public final NotifPipelineFlags mNotifPipelineFlags;
    public final Lazy<NotificationRowBinder> mNotificationRowBinderLazy;
    public NotificationPresenter mPresenter;
    public final Set<NotificationEntry> mReadOnlyAllNotifications;
    public final List<NotificationEntry> mReadOnlyNotifications;
    public final Lazy<NotificationRemoteInputManager> mRemoteInputManagerLazy;
    @VisibleForTesting
    public final ArrayList<NotificationEntry> mSortedAndFiltered;
    public final IStatusBarService mStatusBarService;
    @VisibleForTesting
    public final HashMap<String, NotificationEntry> mPendingNotifications = new HashMap<>();
    public final ArrayMap<String, NotificationEntry> mActiveNotifications = new ArrayMap<>();
    public final ArrayMap mRetainedNotifications = new ArrayMap();
    public final ArrayList mNotifCollectionListeners = new ArrayList();
    public LegacyNotificationRanker mRanker = new LegacyNotificationRankerStub();
    @VisibleForTesting
    public final ArrayList<NotificationLifetimeExtender> mNotificationLifetimeExtenders = new ArrayList<>();
    public final ArrayList mNotificationEntryListeners = new ArrayList();
    public final ArrayList mRemoveInterceptors = new ArrayList();
    public final AnonymousClass1 mInflationCallback = new NotificationRowContentBinder.InflationCallback() { // from class: com.android.systemui.statusbar.notification.NotificationEntryManager.1
        @Override // com.android.systemui.statusbar.notification.row.NotificationRowContentBinder.InflationCallback
        public final void handleInflationException(NotificationEntry notificationEntry, Exception exc) {
            Trace.beginSection("NotificationEntryManager.handleInflationException");
            NotificationEntryManager notificationEntryManager = NotificationEntryManager.this;
            Objects.requireNonNull(notificationEntry);
            notificationEntryManager.handleInflationException(notificationEntry.mSbn, exc);
            Trace.endSection();
        }

        @Override // com.android.systemui.statusbar.notification.row.NotificationRowContentBinder.InflationCallback
        public final void onAsyncInflationFinished(NotificationEntry notificationEntry) {
            boolean z;
            Trace.beginSection("NotificationEntryManager.onAsyncInflationFinished");
            HashMap<String, NotificationEntry> hashMap = NotificationEntryManager.this.mPendingNotifications;
            Objects.requireNonNull(notificationEntry);
            hashMap.remove(notificationEntry.mKey);
            if (!notificationEntry.isRowRemoved()) {
                if (NotificationEntryManager.this.getActiveNotificationUnfiltered(notificationEntry.mKey) == null) {
                    z = true;
                } else {
                    z = false;
                }
                NotificationEntryManagerLogger notificationEntryManagerLogger = NotificationEntryManager.this.mLogger;
                String str = notificationEntry.mKey;
                Objects.requireNonNull(notificationEntryManagerLogger);
                LogBuffer logBuffer = notificationEntryManagerLogger.buffer;
                LogLevel logLevel = LogLevel.DEBUG;
                NotificationEntryManagerLogger$logNotifInflated$2 notificationEntryManagerLogger$logNotifInflated$2 = NotificationEntryManagerLogger$logNotifInflated$2.INSTANCE;
                Objects.requireNonNull(logBuffer);
                if (!logBuffer.frozen) {
                    LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logNotifInflated$2);
                    obtain.str1 = str;
                    obtain.bool1 = z;
                    logBuffer.push(obtain);
                }
                if (z) {
                    Iterator it = NotificationEntryManager.this.mNotificationEntryListeners.iterator();
                    while (it.hasNext()) {
                        ((NotificationEntryListener) it.next()).onEntryInflated(notificationEntry);
                    }
                    NotificationEntryManager notificationEntryManager = NotificationEntryManager.this;
                    Objects.requireNonNull(notificationEntryManager);
                    Assert.isMainThread();
                    notificationEntryManager.mActiveNotifications.put(notificationEntry.mKey, notificationEntry);
                    NotificationGroupManagerLegacy notificationGroupManagerLegacy = notificationEntryManager.mGroupManager;
                    Objects.requireNonNull(notificationGroupManagerLegacy);
                    NotificationGroupManagerLegacy.GroupEventDispatcher groupEventDispatcher = notificationGroupManagerLegacy.mEventDispatcher;
                    Objects.requireNonNull(groupEventDispatcher);
                    groupEventDispatcher.mBufferScopeDepth++;
                    notificationGroupManagerLegacy.updateIsolation(notificationEntry);
                    notificationGroupManagerLegacy.onEntryAddedInternal(notificationEntry);
                    notificationGroupManagerLegacy.mEventDispatcher.closeBufferScope();
                    notificationEntryManager.updateRankingAndSort(notificationEntryManager.mRanker.getRankingMap(), "addEntryInternalInternal");
                    NotificationEntryManager.this.updateNotifications("onAsyncInflationFinished");
                    Iterator it2 = NotificationEntryManager.this.mNotificationEntryListeners.iterator();
                    while (it2.hasNext()) {
                        Objects.requireNonNull((NotificationEntryListener) it2.next());
                    }
                } else {
                    Iterator it3 = NotificationEntryManager.this.mNotificationEntryListeners.iterator();
                    while (it3.hasNext()) {
                        ((NotificationEntryListener) it3.next()).onEntryReinflated(notificationEntry);
                    }
                }
            }
            Trace.endSection();
        }
    };
    public final AnonymousClass2 mNotifListener = new NotificationListener.NotificationHandler() { // from class: com.android.systemui.statusbar.notification.NotificationEntryManager.2
        @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
        public final void onNotificationsInitialized() {
        }

        @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
        public final void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
            NotificationEntryManager notificationEntryManager = NotificationEntryManager.this;
            Objects.requireNonNull(notificationEntryManager);
            Iterator it = notificationEntryManager.mNotifCollectionListeners.iterator();
            while (it.hasNext()) {
                ((NotifCollectionListener) it.next()).onNotificationChannelModified(str, userHandle, notificationChannel, i);
            }
            Iterator it2 = notificationEntryManager.mNotificationEntryListeners.iterator();
            while (it2.hasNext()) {
                ((NotificationEntryListener) it2.next()).onNotificationChannelModified(str, userHandle, notificationChannel, i);
            }
        }

        @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
        public final void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
            if (NotificationEntryManager.this.mActiveNotifications.containsKey(statusBarNotification.getKey())) {
                NotificationEntryManager notificationEntryManager = NotificationEntryManager.this;
                Objects.requireNonNull(notificationEntryManager);
                try {
                    notificationEntryManager.updateNotificationInternal(statusBarNotification, rankingMap);
                } catch (InflationException e) {
                    notificationEntryManager.handleInflationException(statusBarNotification, e);
                }
            } else {
                NotificationEntryManager notificationEntryManager2 = NotificationEntryManager.this;
                Objects.requireNonNull(notificationEntryManager2);
                try {
                    notificationEntryManager2.addNotificationInternal(statusBarNotification, rankingMap);
                } catch (InflationException e2) {
                    notificationEntryManager2.handleInflationException(statusBarNotification, e2);
                }
            }
        }

        @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
        public final void onNotificationRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
            NotificationEntryManager notificationEntryManager = NotificationEntryManager.this;
            Objects.requireNonNull(notificationEntryManager);
            Trace.beginSection("NotificationEntryManager.updateNotificationRanking");
            ArrayList arrayList = new ArrayList();
            notificationEntryManager.mNotifPipelineFlags.checkLegacyPipelineEnabled();
            arrayList.addAll(notificationEntryManager.mReadOnlyNotifications);
            arrayList.addAll(notificationEntryManager.mPendingNotifications.values());
            ArrayMap arrayMap = new ArrayMap();
            ArrayMap arrayMap2 = new ArrayMap();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                NotificationEntry notificationEntry = (NotificationEntry) it.next();
                Objects.requireNonNull(notificationEntry);
                arrayMap.put(notificationEntry.mKey, new NotificationUiAdjustment(notificationEntry.mKey, notificationEntry.mRanking.getSmartActions(), notificationEntry.mRanking.getSmartReplies(), notificationEntry.mRanking.isConversation()));
                arrayMap2.put(notificationEntry.mKey, Integer.valueOf(notificationEntry.getImportance()));
            }
            notificationEntryManager.updateRankingAndSort(rankingMap, "updateNotificationRanking");
            if (rankingMap != null) {
                for (NotificationEntry notificationEntry2 : notificationEntryManager.mPendingNotifications.values()) {
                    NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
                    Objects.requireNonNull(notificationEntry2);
                    if (rankingMap.getRanking(notificationEntry2.mKey, ranking)) {
                        notificationEntry2.setRanking(ranking);
                    }
                }
            }
            Iterator it2 = arrayList.iterator();
            while (it2.hasNext()) {
                NotificationEntry notificationEntry3 = (NotificationEntry) it2.next();
                Objects.requireNonNull(notificationEntry3);
                notificationEntryManager.mNotificationRowBinderLazy.get().onNotificationRankingUpdated(notificationEntry3, (Integer) arrayMap2.get(notificationEntry3.mKey), (NotificationUiAdjustment) arrayMap.get(notificationEntry3.mKey), new NotificationUiAdjustment(notificationEntry3.mKey, notificationEntry3.mRanking.getSmartActions(), notificationEntry3.mRanking.getSmartReplies(), notificationEntry3.mRanking.isConversation()), notificationEntryManager.mInflationCallback);
            }
            notificationEntryManager.updateNotifications("updateNotificationRanking");
            Iterator it3 = notificationEntryManager.mNotificationEntryListeners.iterator();
            while (it3.hasNext()) {
                ((NotificationEntryListener) it3.next()).onNotificationRankingUpdated(rankingMap);
            }
            Iterator it4 = notificationEntryManager.mNotifCollectionListeners.iterator();
            while (it4.hasNext()) {
                ((NotifCollectionListener) it4.next()).onRankingUpdate(rankingMap);
            }
            Iterator it5 = notificationEntryManager.mNotifCollectionListeners.iterator();
            while (it5.hasNext()) {
                ((NotifCollectionListener) it5.next()).onRankingApplied();
            }
            Trace.endSection();
        }

        @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
        public final void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i) {
            NotificationEntryManager.this.removeNotification(statusBarNotification.getKey(), rankingMap, i);
        }
    };

    /* loaded from: classes.dex */
    public interface KeyguardEnvironment {
        boolean isDeviceProvisioned();

        boolean isNotificationForCurrentProfiles(StatusBarNotification statusBarNotification);
    }

    public static void dumpEntry(PrintWriter printWriter, int i, NotificationEntry notificationEntry) {
        printWriter.print("  ");
        StringBuilder sb = new StringBuilder();
        sb.append("  [");
        sb.append(i);
        sb.append("] key=");
        Objects.requireNonNull(notificationEntry);
        sb.append(notificationEntry.mKey);
        sb.append(" icon=");
        IconPack iconPack = notificationEntry.mIcons;
        Objects.requireNonNull(iconPack);
        sb.append(iconPack.mStatusBarIcon);
        printWriter.println(sb.toString());
        StatusBarNotification statusBarNotification = notificationEntry.mSbn;
        printWriter.print("  ");
        printWriter.println("      pkg=" + statusBarNotification.getPackageName() + " id=" + statusBarNotification.getId() + " importance=" + notificationEntry.mRanking.getImportance());
        printWriter.print("  ");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("      notification=");
        sb2.append(statusBarNotification.getNotification());
        printWriter.println(sb2.toString());
    }

    public final void abortExistingInflation(String str, String str2) {
        if (this.mPendingNotifications.containsKey(str)) {
            this.mPendingNotifications.get(str).abortTask();
            this.mPendingNotifications.remove(str);
            this.mLogger.logInflationAborted(str, "pending", str2);
        }
        NotificationEntry activeNotificationUnfiltered = getActiveNotificationUnfiltered(str);
        if (activeNotificationUnfiltered != null) {
            activeNotificationUnfiltered.abortTask();
            this.mLogger.logInflationAborted(str, "active", str2);
        }
    }

    @VisibleForTesting
    public void addActiveNotificationForTest(NotificationEntry notificationEntry) {
        ArrayMap<String, NotificationEntry> arrayMap = this.mActiveNotifications;
        Objects.requireNonNull(notificationEntry);
        arrayMap.put(notificationEntry.mKey, notificationEntry);
        NotificationGroupManagerLegacy notificationGroupManagerLegacy = this.mGroupManager;
        Objects.requireNonNull(notificationGroupManagerLegacy);
        NotificationGroupManagerLegacy.GroupEventDispatcher groupEventDispatcher = notificationGroupManagerLegacy.mEventDispatcher;
        Objects.requireNonNull(groupEventDispatcher);
        groupEventDispatcher.mBufferScopeDepth++;
        notificationGroupManagerLegacy.updateIsolation(notificationEntry);
        notificationGroupManagerLegacy.onEntryAddedInternal(notificationEntry);
        notificationGroupManagerLegacy.mEventDispatcher.closeBufferScope();
        reapplyFilterAndSort("addVisibleNotification");
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection
    public final void addCollectionListener(NotifCollectionListener notifCollectionListener) {
        this.mNotifCollectionListeners.add(notifCollectionListener);
    }

    public final void addNotificationEntryListener(NotificationEntryListener notificationEntryListener) {
        this.mNotificationEntryListeners.add(notificationEntryListener);
    }

    public final void addNotificationInternal(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) throws InflationException {
        Trace.beginSection("NotificationEntryManager.addNotificationInternal");
        String key = statusBarNotification.getKey();
        updateRankingAndSort(rankingMap, "addNotificationInternal");
        NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
        rankingMap.getRanking(key, ranking);
        NotificationEntry notificationEntry = this.mPendingNotifications.get(key);
        if (notificationEntry != null) {
            notificationEntry.setSbn(statusBarNotification);
            notificationEntry.setRanking(ranking);
        } else {
            notificationEntry = new NotificationEntry(statusBarNotification, ranking, SystemClock.uptimeMillis());
            this.mAllNotifications.add(notificationEntry);
            LeakDetector leakDetector = this.mLeakDetector;
            Objects.requireNonNull(leakDetector);
            TrackedObjects trackedObjects = leakDetector.mTrackedObjects;
            if (trackedObjects != null) {
                synchronized (trackedObjects) {
                    TrackedObjects.TrackedClass<?> trackedClass = trackedObjects.mTrackedClasses.get(NotificationEntry.class);
                    if (trackedClass == null) {
                        trackedClass = new TrackedObjects.TrackedClass<>();
                        trackedObjects.mTrackedClasses.put(NotificationEntry.class, trackedClass);
                    }
                    WeakIdentityHashMap<?, Void> weakIdentityHashMap = trackedClass.instances;
                    Objects.requireNonNull(weakIdentityHashMap);
                    weakIdentityHashMap.cleanUp();
                    weakIdentityHashMap.mMap.put(new WeakIdentityHashMap.CmpWeakReference(notificationEntry, weakIdentityHashMap.mRefQueue), null);
                    trackedObjects.mTrackedCollections.track(trackedClass, NotificationEntry.class.getName());
                }
            }
            Iterator it = this.mNotifCollectionListeners.iterator();
            while (it.hasNext()) {
                ((NotifCollectionListener) it.next()).onEntryInit(notificationEntry);
            }
        }
        Iterator it2 = this.mNotifCollectionListeners.iterator();
        while (it2.hasNext()) {
            ((NotifCollectionListener) it2.next()).onEntryBind(notificationEntry, statusBarNotification);
        }
        if (!this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            this.mNotificationRowBinderLazy.get().inflateViews(notificationEntry, null, this.mInflationCallback);
        }
        this.mPendingNotifications.put(key, notificationEntry);
        NotificationEntryManagerLogger notificationEntryManagerLogger = this.mLogger;
        String str = notificationEntry.mKey;
        Objects.requireNonNull(notificationEntryManagerLogger);
        LogBuffer logBuffer = notificationEntryManagerLogger.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotificationEntryManagerLogger$logNotifAdded$2 notificationEntryManagerLogger$logNotifAdded$2 = NotificationEntryManagerLogger$logNotifAdded$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logNotifAdded$2);
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
        Iterator it3 = this.mNotificationEntryListeners.iterator();
        while (it3.hasNext()) {
            ((NotificationEntryListener) it3.next()).onPendingEntryAdded(notificationEntry);
        }
        Iterator it4 = this.mNotifCollectionListeners.iterator();
        while (it4.hasNext()) {
            ((NotifCollectionListener) it4.next()).onEntryAdded(notificationEntry);
        }
        Iterator it5 = this.mNotifCollectionListeners.iterator();
        while (it5.hasNext()) {
            ((NotifCollectionListener) it5.next()).onRankingApplied();
        }
        Trace.endSection();
    }

    public final void cancelLifetimeExtension(NotificationEntry notificationEntry) {
        NotificationLifetimeExtender notificationLifetimeExtender = (NotificationLifetimeExtender) this.mRetainedNotifications.remove(notificationEntry);
        if (notificationLifetimeExtender != null) {
            notificationLifetimeExtender.setShouldManageLifetime(notificationEntry, false);
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("NotificationEntryManager state:");
        printWriter.println("  mAllNotifications=");
        if (this.mAllNotifications.size() == 0) {
            printWriter.println("null");
        } else {
            int i = 0;
            Iterator it = this.mAllNotifications.iterator();
            while (it.hasNext()) {
                dumpEntry(printWriter, i, (NotificationEntry) it.next());
                i++;
            }
        }
        printWriter.print("  mPendingNotifications=");
        if (this.mPendingNotifications.size() == 0) {
            printWriter.println("null");
        } else {
            for (NotificationEntry notificationEntry : this.mPendingNotifications.values()) {
                Objects.requireNonNull(notificationEntry);
                printWriter.println(notificationEntry.mSbn);
            }
        }
        printWriter.println("  Remove interceptors registered:");
        Iterator it2 = this.mRemoveInterceptors.iterator();
        while (it2.hasNext()) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("    ");
            m.append(((NotificationRemoveInterceptor) it2.next()).getClass().getSimpleName());
            printWriter.println(m.toString());
        }
        printWriter.println("  Lifetime extenders registered:");
        Iterator<NotificationLifetimeExtender> it3 = this.mNotificationLifetimeExtenders.iterator();
        while (it3.hasNext()) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("    ");
            m2.append(it3.next().getClass().getSimpleName());
            printWriter.println(m2.toString());
        }
        printWriter.println("  Lifetime-extended notifications:");
        if (this.mRetainedNotifications.isEmpty()) {
            printWriter.println("    None");
            return;
        }
        for (Map.Entry entry : this.mRetainedNotifications.entrySet()) {
            StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("    ");
            NotificationEntry notificationEntry2 = (NotificationEntry) entry.getKey();
            Objects.requireNonNull(notificationEntry2);
            m3.append(notificationEntry2.mSbn);
            m3.append(" retained by ");
            m3.append(((NotificationLifetimeExtender) entry.getValue()).getClass().getName());
            printWriter.println(m3.toString());
        }
    }

    public final void extendLifetime(NotificationEntry notificationEntry, NotificationLifetimeExtender notificationLifetimeExtender) {
        NotificationLifetimeExtender notificationLifetimeExtender2 = (NotificationLifetimeExtender) this.mRetainedNotifications.get(notificationEntry);
        if (!(notificationLifetimeExtender2 == null || notificationLifetimeExtender2 == notificationLifetimeExtender)) {
            notificationLifetimeExtender2.setShouldManageLifetime(notificationEntry, false);
        }
        this.mRetainedNotifications.put(notificationEntry, notificationLifetimeExtender);
        notificationLifetimeExtender.setShouldManageLifetime(notificationEntry, true);
    }

    public final NotificationEntry getActiveNotificationUnfiltered(String str) {
        this.mNotifPipelineFlags.checkLegacyPipelineEnabled();
        return this.mActiveNotifications.get(str);
    }

    public final ArrayList getActiveNotificationsForCurrentUser() {
        Trace.beginSection("NotificationEntryManager.getActiveNotificationsForCurrentUser");
        Assert.isMainThread();
        ArrayList arrayList = new ArrayList();
        int size = this.mActiveNotifications.size();
        for (int i = 0; i < size; i++) {
            NotificationEntry valueAt = this.mActiveNotifications.valueAt(i);
            if (this.mRanker.isNotificationForCurrentProfiles(valueAt)) {
                arrayList.add(valueAt);
            }
        }
        Trace.endSection();
        return arrayList;
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection
    public final Collection<NotificationEntry> getAllNotifs() {
        this.mNotifPipelineFlags.checkLegacyPipelineEnabled();
        return this.mReadOnlyAllNotifications;
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection
    public final NotificationEntry getEntry(String str) {
        this.mNotifPipelineFlags.checkLegacyPipelineEnabled();
        return getPendingOrActiveNotif(str);
    }

    public final NotificationEntry getPendingOrActiveNotif(String str) {
        this.mNotifPipelineFlags.checkLegacyPipelineEnabled();
        NotificationEntry notificationEntry = this.mPendingNotifications.get(str);
        if (notificationEntry != null) {
            return notificationEntry;
        }
        return this.mActiveNotifications.get(str);
    }

    public final void reapplyFilterAndSort(String str) {
        if (this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            this.mLogger.logUseWhileNewPipelineActive("reapplyFilterAndSort", str);
            return;
        }
        Trace.beginSection("NotificationEntryManager.reapplyFilterAndSort");
        updateRankingAndSort(this.mRanker.getRankingMap(), str);
        Trace.endSection();
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection
    public final void removeCollectionListener(NotifCollectionListener notifCollectionListener) {
        this.mNotifCollectionListeners.remove(notifCollectionListener);
    }

    @VisibleForTesting
    public void removeNotification(String str, NotificationListenerService.RankingMap rankingMap, int i) {
        int i2;
        NotificationEntry notificationEntry = this.mActiveNotifications.get(str);
        if (notificationEntry != null) {
            i2 = notificationEntry.mRanking.getRank();
        } else {
            i2 = 0;
        }
        removeNotificationInternal(str, rankingMap, NotificationVisibility.obtain(str, i2, this.mActiveNotifications.size(), true, NotificationLogger.getNotificationLocation(getActiveNotificationUnfiltered(str))), false, null, i);
    }

    public final void removeNotificationInternal(String str, NotificationListenerService.RankingMap rankingMap, NotificationVisibility notificationVisibility, boolean z, DismissedByUserStats dismissedByUserStats, int i) {
        boolean z2;
        boolean z3;
        ArrayList attachedNotifChildren;
        boolean z4;
        boolean z5;
        LogLevel logLevel = LogLevel.INFO;
        Trace.beginSection("NotificationEntryManager.removeNotificationInternal");
        NotificationEntry activeNotificationUnfiltered = getActiveNotificationUnfiltered(str);
        Iterator it = this.mRemoveInterceptors.iterator();
        while (it.hasNext()) {
            if (((NotificationRemoveInterceptor) it.next()).onNotificationRemoveRequested(activeNotificationUnfiltered, i)) {
                NotificationEntryManagerLogger notificationEntryManagerLogger = this.mLogger;
                Objects.requireNonNull(notificationEntryManagerLogger);
                LogBuffer logBuffer = notificationEntryManagerLogger.buffer;
                NotificationEntryManagerLogger$logRemovalIntercepted$2 notificationEntryManagerLogger$logRemovalIntercepted$2 = NotificationEntryManagerLogger$logRemovalIntercepted$2.INSTANCE;
                Objects.requireNonNull(logBuffer);
                if (!logBuffer.frozen) {
                    LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logRemovalIntercepted$2);
                    obtain.str1 = str;
                    logBuffer.push(obtain);
                }
                Trace.endSection();
                return;
            }
        }
        if (activeNotificationUnfiltered == null) {
            NotificationEntry notificationEntry = this.mPendingNotifications.get(str);
            if (notificationEntry != null) {
                Iterator<NotificationLifetimeExtender> it2 = this.mNotificationLifetimeExtenders.iterator();
                while (it2.hasNext()) {
                    Objects.requireNonNull(it2.next());
                }
                abortExistingInflation(str, "removeNotification");
                Iterator it3 = this.mNotifCollectionListeners.iterator();
                while (it3.hasNext()) {
                    ((NotifCollectionListener) it3.next()).onEntryRemoved(notificationEntry, 0);
                }
                Iterator it4 = this.mNotifCollectionListeners.iterator();
                while (it4.hasNext()) {
                    ((NotifCollectionListener) it4.next()).onEntryCleanUp(notificationEntry);
                }
                this.mAllNotifications.remove(notificationEntry);
                this.mLeakDetector.trackGarbage(notificationEntry);
            }
        } else {
            boolean isRowDismissed = activeNotificationUnfiltered.isRowDismissed();
            boolean z6 = true;
            if (!z && !isRowDismissed) {
                Iterator<NotificationLifetimeExtender> it5 = this.mNotificationLifetimeExtenders.iterator();
                while (it5.hasNext()) {
                    NotificationLifetimeExtender next = it5.next();
                    if (next.shouldExtendLifetime(activeNotificationUnfiltered)) {
                        this.mLatestRankingMap = rankingMap;
                        extendLifetime(activeNotificationUnfiltered, next);
                        this.mLogger.logLifetimeExtended(str, next.getClass().getName(), "active");
                        z2 = true;
                        break;
                    }
                }
            }
            z2 = false;
            if (!z2) {
                abortExistingInflation(str, "removeNotification");
                this.mAllNotifications.remove(activeNotificationUnfiltered);
                cancelLifetimeExtension(activeNotificationUnfiltered);
                if (activeNotificationUnfiltered.rowExists()) {
                    activeNotificationUnfiltered.removeRow();
                }
                NotificationEntry activeNotificationUnfiltered2 = getActiveNotificationUnfiltered(str);
                if (activeNotificationUnfiltered2 != null && activeNotificationUnfiltered2.rowExists()) {
                    ExpandableNotificationRow expandableNotificationRow = activeNotificationUnfiltered2.row;
                    if (expandableNotificationRow == null || !expandableNotificationRow.mIsSummaryWithChildren) {
                        z3 = false;
                    } else {
                        z3 = true;
                    }
                    if (z3 && ((activeNotificationUnfiltered2.mSbn.getOverrideGroupKey() == null || activeNotificationUnfiltered2.isRowDismissed()) && (attachedNotifChildren = activeNotificationUnfiltered2.getAttachedNotifChildren()) != null)) {
                        for (int i2 = 0; i2 < attachedNotifChildren.size(); i2++) {
                            NotificationEntry notificationEntry2 = (NotificationEntry) attachedNotifChildren.get(i2);
                            if ((activeNotificationUnfiltered2.mSbn.getNotification().flags & 64) != 0) {
                                z4 = true;
                            } else {
                                z4 = false;
                            }
                            if (this.mRemoteInputManagerLazy.get().shouldKeepForRemoteInputHistory(notificationEntry2) || this.mRemoteInputManagerLazy.get().shouldKeepForSmartReplyHistory(notificationEntry2)) {
                                z5 = true;
                            } else {
                                z5 = false;
                            }
                            if (!z4 && !z5) {
                                Objects.requireNonNull(notificationEntry2);
                                ExpandableNotificationRow expandableNotificationRow2 = notificationEntry2.row;
                                if (expandableNotificationRow2 != null) {
                                    expandableNotificationRow2.mKeepInParent = true;
                                }
                                notificationEntry2.removeRow();
                            }
                        }
                    }
                }
                Assert.isMainThread();
                NotificationEntry remove = this.mActiveNotifications.remove(str);
                if (remove != null) {
                    this.mGroupManager.onEntryRemoved(remove);
                }
                updateNotifications("removeNotificationInternal");
                if (dismissedByUserStats == null) {
                    z6 = false;
                }
                NotificationEntryManagerLogger notificationEntryManagerLogger2 = this.mLogger;
                String str2 = activeNotificationUnfiltered.mKey;
                Objects.requireNonNull(notificationEntryManagerLogger2);
                LogBuffer logBuffer2 = notificationEntryManagerLogger2.buffer;
                NotificationEntryManagerLogger$logNotifRemoved$2 notificationEntryManagerLogger$logNotifRemoved$2 = NotificationEntryManagerLogger$logNotifRemoved$2.INSTANCE;
                Objects.requireNonNull(logBuffer2);
                if (!logBuffer2.frozen) {
                    LogMessageImpl obtain2 = logBuffer2.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logNotifRemoved$2);
                    obtain2.str1 = str2;
                    obtain2.bool1 = z6;
                    logBuffer2.push(obtain2);
                }
                if (z6 && notificationVisibility != null) {
                    StatusBarNotification statusBarNotification = activeNotificationUnfiltered.mSbn;
                    try {
                        this.mStatusBarService.onNotificationClear(statusBarNotification.getPackageName(), statusBarNotification.getUser().getIdentifier(), statusBarNotification.getKey(), dismissedByUserStats.dismissalSurface, dismissedByUserStats.dismissalSentiment, dismissedByUserStats.notificationVisibility);
                    } catch (RemoteException unused) {
                    }
                }
                Iterator it6 = this.mNotificationEntryListeners.iterator();
                while (it6.hasNext()) {
                    ((NotificationEntryListener) it6.next()).onEntryRemoved(activeNotificationUnfiltered, z6);
                }
                Iterator it7 = this.mNotifCollectionListeners.iterator();
                while (it7.hasNext()) {
                    ((NotifCollectionListener) it7.next()).onEntryRemoved(activeNotificationUnfiltered, 0);
                }
                Iterator it8 = this.mNotifCollectionListeners.iterator();
                while (it8.hasNext()) {
                    ((NotifCollectionListener) it8.next()).onEntryCleanUp(activeNotificationUnfiltered);
                }
                this.mLeakDetector.trackGarbage(activeNotificationUnfiltered);
            }
        }
        Trace.endSection();
    }

    public final void updateNotificationInternal(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) throws InflationException {
        boolean z;
        Trace.beginSection("NotificationEntryManager.updateNotificationInternal");
        String key = statusBarNotification.getKey();
        abortExistingInflation(key, "updateNotification");
        NotificationEntry activeNotificationUnfiltered = getActiveNotificationUnfiltered(key);
        if (activeNotificationUnfiltered == null) {
            Trace.endSection();
            return;
        }
        cancelLifetimeExtension(activeNotificationUnfiltered);
        updateRankingAndSort(rankingMap, "updateNotificationInternal");
        StatusBarNotification statusBarNotification2 = activeNotificationUnfiltered.mSbn;
        activeNotificationUnfiltered.setSbn(statusBarNotification);
        Iterator it = this.mNotifCollectionListeners.iterator();
        while (it.hasNext()) {
            ((NotifCollectionListener) it.next()).onEntryBind(activeNotificationUnfiltered, statusBarNotification);
        }
        NotificationGroupManagerLegacy notificationGroupManagerLegacy = this.mGroupManager;
        Objects.requireNonNull(notificationGroupManagerLegacy);
        notificationGroupManagerLegacy.onEntryUpdated(activeNotificationUnfiltered, statusBarNotification2.getGroupKey(), statusBarNotification2.isGroup(), statusBarNotification2.getNotification().isGroupSummary());
        NotificationEntryManagerLogger notificationEntryManagerLogger = this.mLogger;
        String str = activeNotificationUnfiltered.mKey;
        Objects.requireNonNull(notificationEntryManagerLogger);
        LogBuffer logBuffer = notificationEntryManagerLogger.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotificationEntryManagerLogger$logNotifUpdated$2 notificationEntryManagerLogger$logNotifUpdated$2 = NotificationEntryManagerLogger$logNotifUpdated$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logNotifUpdated$2);
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
        Iterator it2 = this.mNotificationEntryListeners.iterator();
        while (it2.hasNext()) {
            ((NotificationEntryListener) it2.next()).onPreEntryUpdated(activeNotificationUnfiltered);
        }
        if (rankingMap != null) {
            z = true;
        } else {
            z = false;
        }
        Iterator it3 = this.mNotifCollectionListeners.iterator();
        while (it3.hasNext()) {
            ((NotifCollectionListener) it3.next()).onEntryUpdated(activeNotificationUnfiltered, z);
        }
        if (!this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            this.mNotificationRowBinderLazy.get().inflateViews(activeNotificationUnfiltered, null, this.mInflationCallback);
        }
        updateNotifications("updateNotificationInternal");
        Iterator it4 = this.mNotificationEntryListeners.iterator();
        while (it4.hasNext()) {
            ((NotificationEntryListener) it4.next()).onPostEntryUpdated(activeNotificationUnfiltered);
        }
        Iterator it5 = this.mNotifCollectionListeners.iterator();
        while (it5.hasNext()) {
            ((NotifCollectionListener) it5.next()).onRankingApplied();
        }
        Trace.endSection();
    }

    public final void updateNotifications(String str) {
        if (this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            this.mLogger.logUseWhileNewPipelineActive("updateNotifications", str);
            return;
        }
        Trace.beginSection("NotificationEntryManager.updateNotifications");
        reapplyFilterAndSort(str);
        NotificationPresenter notificationPresenter = this.mPresenter;
        if (notificationPresenter != null) {
            ((StatusBarNotificationPresenter) notificationPresenter).updateNotificationViews(str);
        }
        NotifLiveDataStoreImpl notifLiveDataStoreImpl = this.mNotifLiveDataStore;
        this.mNotifPipelineFlags.checkLegacyPipelineEnabled();
        notifLiveDataStoreImpl.setActiveNotifList(this.mReadOnlyNotifications);
        Trace.endSection();
    }

    public final void updateRankingAndSort(NotificationListenerService.RankingMap rankingMap, String str) {
        if (this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            this.mLogger.logUseWhileNewPipelineActive("updateRankingAndSort", str);
            return;
        }
        Trace.beginSection("NotificationEntryManager.updateRankingAndSort");
        this.mSortedAndFiltered.clear();
        this.mSortedAndFiltered.addAll(this.mRanker.updateRanking(rankingMap, this.mActiveNotifications.values(), str));
        Trace.endSection();
    }

    /* JADX WARN: Type inference failed for: r0v12, types: [com.android.systemui.statusbar.notification.NotificationEntryManager$1] */
    /* JADX WARN: Type inference failed for: r0v13, types: [com.android.systemui.statusbar.notification.NotificationEntryManager$2] */
    public NotificationEntryManager(NotificationEntryManagerLogger notificationEntryManagerLogger, NotificationGroupManagerLegacy notificationGroupManagerLegacy, NotifPipelineFlags notifPipelineFlags, Lazy lazy, Lazy lazy2, LeakDetector leakDetector, IStatusBarService iStatusBarService, NotifLiveDataStoreImpl notifLiveDataStoreImpl, DumpManager dumpManager) {
        ArraySet arraySet = new ArraySet();
        this.mAllNotifications = arraySet;
        this.mReadOnlyAllNotifications = Collections.unmodifiableSet(arraySet);
        ArrayList<NotificationEntry> arrayList = new ArrayList<>();
        this.mSortedAndFiltered = arrayList;
        this.mReadOnlyNotifications = Collections.unmodifiableList(arrayList);
        this.mLogger = notificationEntryManagerLogger;
        this.mGroupManager = notificationGroupManagerLegacy;
        this.mNotifPipelineFlags = notifPipelineFlags;
        this.mNotificationRowBinderLazy = lazy;
        this.mRemoteInputManagerLazy = lazy2;
        this.mLeakDetector = leakDetector;
        this.mStatusBarService = iStatusBarService;
        this.mNotifLiveDataStore = notifLiveDataStoreImpl;
        this.mDumpManager = dumpManager;
    }

    public final void handleInflationException(StatusBarNotification statusBarNotification, Exception exc) {
        removeNotificationInternal(statusBarNotification.getKey(), null, null, true, null, 4);
        Iterator it = this.mNotificationEntryListeners.iterator();
        while (it.hasNext()) {
            ((NotificationEntryListener) it.next()).onInflationError(statusBarNotification, exc);
        }
    }

    @Override // com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager.Callback
    public final void onChangeAllowed() {
        updateNotifications("reordering is now allowed");
    }

    public final void performRemoveNotification(StatusBarNotification statusBarNotification, DismissedByUserStats dismissedByUserStats, int i) {
        removeNotificationInternal(statusBarNotification.getKey(), null, dismissedByUserStats.notificationVisibility, false, dismissedByUserStats, i);
    }
}
