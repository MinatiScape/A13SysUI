package com.android.systemui.statusbar.notification.collection;

import android.app.NotificationChannel;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.os.RemoteException;
import android.os.Trace;
import android.os.UserHandle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import android.util.Pair;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline1;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.dump.LogBufferEulogizer;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescer;
import com.android.systemui.statusbar.notification.collection.notifcollection.BindEntryEvent;
import com.android.systemui.statusbar.notification.collection.notifcollection.ChannelChangedEvent;
import com.android.systemui.statusbar.notification.collection.notifcollection.CleanUpEntryEvent;
import com.android.systemui.statusbar.notification.collection.notifcollection.CollectionReadyForBuildListener;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.collection.notifcollection.EntryAddedEvent;
import com.android.systemui.statusbar.notification.collection.notifcollection.EntryRemovedEvent;
import com.android.systemui.statusbar.notification.collection.notifcollection.EntryUpdatedEvent;
import com.android.systemui.statusbar.notification.collection.notifcollection.InitEntryEvent;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionLogger;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifDismissInterceptor;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifEvent;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender;
import com.android.systemui.statusbar.notification.collection.notifcollection.RankingAppliedEvent;
import com.android.systemui.statusbar.notification.collection.notifcollection.RankingUpdatedEvent;
import com.android.systemui.util.Assert;
import com.android.systemui.util.time.SystemClock;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public final class NotifCollection implements Dumpable {
    public static final /* synthetic */ int $r8$clinit = 0;
    public boolean mAmDispatchingToOtherCode;
    public CollectionReadyForBuildListener mBuildListener;
    public final SystemClock mClock;
    public final LogBufferEulogizer mEulogizer;
    public final NotifCollectionLogger mLogger;
    public final Handler mMainHandler;
    public final NotifPipelineFlags mNotifPipelineFlags;
    public final ArrayMap mNotificationSet;
    public final Collection<NotificationEntry> mReadOnlyNotificationSet;
    public final IStatusBarService mStatusBarService;
    public final ArrayList mNotifCollectionListeners = new ArrayList();
    public final ArrayList mLifetimeExtenders = new ArrayList();
    public final ArrayList mDismissInterceptors = new ArrayList();
    public ArrayDeque mEventQueue = new ArrayDeque();
    public boolean mAttached = false;
    public final AnonymousClass1 mNotifHandler = new AnonymousClass1();

    /* renamed from: com.android.systemui.statusbar.notification.collection.NotifCollection$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements GroupCoalescer.BatchableNotificationHandler {
        public AnonymousClass1() {
        }

        @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
        public final void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
            NotifCollection notifCollection = NotifCollection.this;
            Objects.requireNonNull(notifCollection);
            Assert.isMainThread();
            notifCollection.mEventQueue.add(new ChannelChangedEvent(str, userHandle, notificationChannel, i));
            notifCollection.dispatchEventsAndRebuildList();
        }

        @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
        public final void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
            NotifCollection notifCollection = NotifCollection.this;
            Objects.requireNonNull(notifCollection);
            Assert.isMainThread();
            String key = statusBarNotification.getKey();
            NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
            if (rankingMap.getRanking(key, ranking)) {
                notifCollection.postNotification(statusBarNotification, ranking);
                notifCollection.applyRanking(rankingMap);
                notifCollection.dispatchEventsAndRebuildList();
                return;
            }
            throw new IllegalArgumentException(SupportMenuInflater$$ExternalSyntheticOutline0.m("Ranking map doesn't contain key: ", key));
        }

        @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
        public final void onNotificationRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
            NotifCollection notifCollection = NotifCollection.this;
            Objects.requireNonNull(notifCollection);
            Assert.isMainThread();
            notifCollection.mEventQueue.add(new RankingUpdatedEvent(rankingMap));
            notifCollection.applyRanking(rankingMap);
            notifCollection.dispatchEventsAndRebuildList();
        }

        @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
        public final void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i) {
            NotifCollection notifCollection = NotifCollection.this;
            Objects.requireNonNull(notifCollection);
            Assert.isMainThread();
            notifCollection.mLogger.logNotifRemoved(statusBarNotification.getKey(), i);
            NotificationEntry notificationEntry = (NotificationEntry) notifCollection.mNotificationSet.get(statusBarNotification.getKey());
            if (notificationEntry == null) {
                notifCollection.mLogger.logNoNotificationToRemoveWithKey(statusBarNotification.getKey(), i);
                return;
            }
            notificationEntry.mCancellationReason = i;
            notifCollection.tryRemoveNotification(notificationEntry);
            notifCollection.applyRanking(rankingMap);
            notifCollection.dispatchEventsAndRebuildList();
        }

        @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
        public final void onNotificationsInitialized() {
            NotifCollection notifCollection = NotifCollection.this;
            Objects.requireNonNull(notifCollection);
            notifCollection.mClock.uptimeMillis();
        }
    }

    public final void cancelDismissInterception(NotificationEntry notificationEntry) {
        this.mAmDispatchingToOtherCode = true;
        Iterator it = notificationEntry.mDismissInterceptors.iterator();
        while (it.hasNext()) {
            ((NotifDismissInterceptor) it.next()).cancelDismissInterception(notificationEntry);
        }
        this.mAmDispatchingToOtherCode = false;
        notificationEntry.mDismissInterceptors.clear();
    }

    public final void cancelLifetimeExtension(NotificationEntry notificationEntry) {
        this.mAmDispatchingToOtherCode = true;
        Iterator it = notificationEntry.mLifetimeExtenders.iterator();
        while (it.hasNext()) {
            ((NotifLifetimeExtender) it.next()).cancelLifetimeExtension(notificationEntry);
        }
        this.mAmDispatchingToOtherCode = false;
        notificationEntry.mLifetimeExtenders.clear();
    }

    static {
        TimeUnit.SECONDS.toMillis(5L);
    }

    public final void applyRanking(NotificationListenerService.RankingMap rankingMap) {
        boolean z;
        for (NotificationEntry notificationEntry : this.mNotificationSet.values()) {
            if (notificationEntry.mCancellationReason != -1) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
                if (rankingMap.getRanking(notificationEntry.mKey, ranking)) {
                    notificationEntry.setRanking(ranking);
                    if (this.mNotifPipelineFlags.isNewPipelineEnabled()) {
                        String overrideGroupKey = ranking.getOverrideGroupKey();
                        if (!Objects.equals(notificationEntry.mSbn.getOverrideGroupKey(), overrideGroupKey)) {
                            notificationEntry.mSbn.setOverrideGroupKey(overrideGroupKey);
                        }
                    }
                } else {
                    this.mLogger.logRankingMissing(notificationEntry.mKey, rankingMap);
                }
            }
        }
        this.mEventQueue.add(new RankingAppliedEvent());
    }

    public final void checkForReentrantCall() {
        if (this.mAmDispatchingToOtherCode) {
            LogBufferEulogizer logBufferEulogizer = this.mEulogizer;
            IllegalStateException illegalStateException = new IllegalStateException("Reentrant call detected");
            logBufferEulogizer.record(illegalStateException);
            throw illegalStateException;
        }
    }

    public final void dismissNotification(NotificationEntry notificationEntry, DismissedByUserStats dismissedByUserStats) {
        dismissNotifications(List.of(new Pair(notificationEntry, dismissedByUserStats)));
    }

    public final void dispatchEventsAndRebuildList() {
        Trace.beginSection("NotifCollection.dispatchEventsAndRebuildList");
        this.mAmDispatchingToOtherCode = true;
        while (true) {
            if (this.mEventQueue.isEmpty()) {
                break;
            }
            NotifEvent notifEvent = (NotifEvent) this.mEventQueue.remove();
            ArrayList arrayList = this.mNotifCollectionListeners;
            Objects.requireNonNull(notifEvent);
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                notifEvent.dispatchToListener((NotifCollectionListener) arrayList.get(i));
            }
        }
        this.mAmDispatchingToOtherCode = false;
        CollectionReadyForBuildListener collectionReadyForBuildListener = this.mBuildListener;
        if (collectionReadyForBuildListener != null) {
            Collection<NotificationEntry> collection = this.mReadOnlyNotificationSet;
            ShadeListBuilder.AnonymousClass1 r1 = (ShadeListBuilder.AnonymousClass1) collectionReadyForBuildListener;
            Assert.isMainThread();
            ShadeListBuilder.this.mPipelineState.requireIsBefore(1);
            ShadeListBuilder.this.mLogger.logOnBuildList();
            ShadeListBuilder shadeListBuilder = ShadeListBuilder.this;
            shadeListBuilder.mAllEntries = collection;
            shadeListBuilder.mChoreographer.schedule();
        }
        Trace.endSection();
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Assert.isMainThread();
        ArrayList arrayList = new ArrayList(this.mReadOnlyNotificationSet);
        printWriter.println("\tNotifCollection unsorted/unfiltered notifications:");
        if (arrayList.size() == 0) {
            printWriter.println("\t\t None");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arrayList.size(); i++) {
            ListDumper.dumpEntry((ListEntry) arrayList.get(i), Integer.toString(i), "\t\t", sb, false, false);
        }
        printWriter.println(sb.toString());
    }

    public final void locallyDismissNotifications(ArrayList arrayList) {
        boolean z;
        boolean z2;
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            NotificationEntry notificationEntry = (NotificationEntry) arrayList.get(i);
            NotificationEntry.DismissState dismissState = NotificationEntry.DismissState.DISMISSED;
            Objects.requireNonNull(notificationEntry);
            notificationEntry.mDismissState = dismissState;
            this.mLogger.logNotifDismissed(notificationEntry.mKey);
            if (notificationEntry.mCancellationReason != -1) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                arrayList2.add(notificationEntry);
            } else if (notificationEntry.mSbn.getNotification().isGroupSummary()) {
                for (NotificationEntry notificationEntry2 : this.mNotificationSet.values()) {
                    if (shouldAutoDismissChildren(notificationEntry2, notificationEntry.mSbn.getGroupKey())) {
                        NotificationEntry.DismissState dismissState2 = NotificationEntry.DismissState.PARENT_DISMISSED;
                        Objects.requireNonNull(notificationEntry2);
                        notificationEntry2.mDismissState = dismissState2;
                        this.mLogger.logChildDismissed(notificationEntry2);
                        if (notificationEntry2.mCancellationReason != -1) {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                        if (z2) {
                            arrayList2.add(notificationEntry2);
                        }
                    }
                }
            }
        }
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            NotificationEntry notificationEntry3 = (NotificationEntry) it.next();
            this.mLogger.logDismissOnAlreadyCanceledEntry(notificationEntry3);
            tryRemoveNotification(notificationEntry3);
        }
    }

    public final void postNotification(StatusBarNotification statusBarNotification, NotificationListenerService.Ranking ranking) {
        NotificationEntry notificationEntry = (NotificationEntry) this.mNotificationSet.get(statusBarNotification.getKey());
        if (notificationEntry == null) {
            NotificationEntry notificationEntry2 = new NotificationEntry(statusBarNotification, ranking, this.mClock.uptimeMillis());
            this.mEventQueue.add(new InitEntryEvent(notificationEntry2));
            this.mEventQueue.add(new BindEntryEvent(notificationEntry2, statusBarNotification));
            this.mNotificationSet.put(statusBarNotification.getKey(), notificationEntry2);
            this.mLogger.logNotifPosted(statusBarNotification.getKey());
            this.mEventQueue.add(new EntryAddedEvent(notificationEntry2));
            return;
        }
        NotificationEntry.DismissState dismissState = NotificationEntry.DismissState.NOT_DISMISSED;
        if (notificationEntry.mDismissState != dismissState) {
            notificationEntry.mDismissState = dismissState;
            if (notificationEntry.mSbn.getNotification().isGroupSummary()) {
                for (NotificationEntry notificationEntry3 : this.mNotificationSet.values()) {
                    Objects.requireNonNull(notificationEntry3);
                    if (notificationEntry3.mSbn.getGroupKey().equals(notificationEntry.mSbn.getGroupKey()) && notificationEntry3.mDismissState == NotificationEntry.DismissState.PARENT_DISMISSED) {
                        notificationEntry3.mDismissState = dismissState;
                    }
                }
            }
        }
        cancelLifetimeExtension(notificationEntry);
        cancelDismissInterception(notificationEntry);
        notificationEntry.mCancellationReason = -1;
        notificationEntry.setSbn(statusBarNotification);
        this.mEventQueue.add(new BindEntryEvent(notificationEntry, statusBarNotification));
        this.mLogger.logNotifUpdated(statusBarNotification.getKey());
        this.mEventQueue.add(new EntryUpdatedEvent(notificationEntry, true));
    }

    public final boolean tryRemoveNotification(NotificationEntry notificationEntry) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        ArrayMap arrayMap = this.mNotificationSet;
        Objects.requireNonNull(notificationEntry);
        if (arrayMap.get(notificationEntry.mKey) == notificationEntry) {
            int i = notificationEntry.mCancellationReason;
            if (i != -1) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                if (notificationEntry.mDismissState != NotificationEntry.DismissState.NOT_DISMISSED) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (i == 1 || i == 2) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                if (z2 || z3) {
                    z4 = true;
                } else {
                    z4 = false;
                }
                if (z4) {
                    cancelLifetimeExtension(notificationEntry);
                } else {
                    notificationEntry.mLifetimeExtenders.clear();
                    this.mAmDispatchingToOtherCode = true;
                    Iterator it = this.mLifetimeExtenders.iterator();
                    while (it.hasNext()) {
                        NotifLifetimeExtender notifLifetimeExtender = (NotifLifetimeExtender) it.next();
                        if (notifLifetimeExtender.maybeExtendLifetime(notificationEntry, notificationEntry.mCancellationReason)) {
                            this.mLogger.logLifetimeExtended(notificationEntry.mKey, notifLifetimeExtender);
                            notificationEntry.mLifetimeExtenders.add(notifLifetimeExtender);
                        }
                    }
                    this.mAmDispatchingToOtherCode = false;
                }
                if (notificationEntry.mLifetimeExtenders.size() > 0) {
                    z5 = true;
                } else {
                    z5 = false;
                }
                if (z5) {
                    return false;
                }
                this.mLogger.logNotifReleased(notificationEntry.mKey);
                this.mNotificationSet.remove(notificationEntry.mKey);
                cancelDismissInterception(notificationEntry);
                this.mEventQueue.add(new EntryRemovedEvent(notificationEntry, notificationEntry.mCancellationReason));
                this.mEventQueue.add(new CleanUpEntryEvent(notificationEntry));
                return true;
            }
            LogBufferEulogizer logBufferEulogizer = this.mEulogizer;
            IllegalStateException illegalStateException = new IllegalStateException(MotionController$$ExternalSyntheticOutline1.m(VendorAtomValue$$ExternalSyntheticOutline1.m("Cannot remove notification "), notificationEntry.mKey, ": has not been marked for removal"));
            logBufferEulogizer.record(illegalStateException);
            throw illegalStateException;
        }
        LogBufferEulogizer logBufferEulogizer2 = this.mEulogizer;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("No notification to remove with key ");
        m.append(notificationEntry.mKey);
        IllegalStateException illegalStateException2 = new IllegalStateException(m.toString());
        logBufferEulogizer2.record(illegalStateException2);
        throw illegalStateException2;
    }

    public final void updateDismissInterceptors(NotificationEntry notificationEntry) {
        notificationEntry.mDismissInterceptors.clear();
        this.mAmDispatchingToOtherCode = true;
        Iterator it = this.mDismissInterceptors.iterator();
        while (it.hasNext()) {
            NotifDismissInterceptor notifDismissInterceptor = (NotifDismissInterceptor) it.next();
            if (notifDismissInterceptor.shouldInterceptDismissal(notificationEntry)) {
                notificationEntry.mDismissInterceptors.add(notifDismissInterceptor);
            }
        }
        this.mAmDispatchingToOtherCode = false;
    }

    public NotifCollection(IStatusBarService iStatusBarService, SystemClock systemClock, NotifPipelineFlags notifPipelineFlags, NotifCollectionLogger notifCollectionLogger, Handler handler, LogBufferEulogizer logBufferEulogizer, DumpManager dumpManager) {
        ArrayMap arrayMap = new ArrayMap();
        this.mNotificationSet = arrayMap;
        this.mReadOnlyNotificationSet = Collections.unmodifiableCollection(arrayMap.values());
        Assert.isMainThread();
        this.mStatusBarService = iStatusBarService;
        this.mClock = systemClock;
        this.mNotifPipelineFlags = notifPipelineFlags;
        this.mLogger = notifCollectionLogger;
        this.mMainHandler = handler;
        this.mEulogizer = logBufferEulogizer;
        dumpManager.registerDumpable("NotifCollection", this);
    }

    public static boolean hasFlag(NotificationEntry notificationEntry, int i) {
        Objects.requireNonNull(notificationEntry);
        if ((notificationEntry.mSbn.getNotification().flags & i) != 0) {
            return true;
        }
        return false;
    }

    @VisibleForTesting
    public static boolean shouldAutoDismissChildren(NotificationEntry notificationEntry, String str) {
        Objects.requireNonNull(notificationEntry);
        if (!notificationEntry.mSbn.getGroupKey().equals(str) || notificationEntry.mSbn.getNotification().isGroupSummary() || hasFlag(notificationEntry, 2) || hasFlag(notificationEntry, 4096) || hasFlag(notificationEntry, 32) || notificationEntry.mDismissState == NotificationEntry.DismissState.DISMISSED) {
            return false;
        }
        return true;
    }

    public final void dismissNotifications(List<Pair<NotificationEntry, DismissedByUserStats>> list) {
        boolean z;
        Assert.isMainThread();
        checkForReentrantCall();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            NotificationEntry notificationEntry = (NotificationEntry) list.get(i).first;
            DismissedByUserStats dismissedByUserStats = (DismissedByUserStats) list.get(i).second;
            Objects.requireNonNull(dismissedByUserStats);
            ArrayMap arrayMap = this.mNotificationSet;
            Objects.requireNonNull(notificationEntry);
            NotificationEntry notificationEntry2 = (NotificationEntry) arrayMap.get(notificationEntry.mKey);
            if (notificationEntry2 == null) {
                this.mLogger.logNonExistentNotifDismissed(notificationEntry.mKey);
            } else if (notificationEntry != notificationEntry2) {
                LogBufferEulogizer logBufferEulogizer = this.mEulogizer;
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Invalid entry: different stored and dismissed entries for ");
                m.append(notificationEntry.mKey);
                IllegalStateException illegalStateException = new IllegalStateException(m.toString());
                logBufferEulogizer.record(illegalStateException);
                throw illegalStateException;
            } else if (notificationEntry.mDismissState != NotificationEntry.DismissState.DISMISSED) {
                updateDismissInterceptors(notificationEntry);
                boolean z2 = true;
                if (notificationEntry.mDismissInterceptors.size() > 0) {
                    z = true;
                } else {
                    z = false;
                }
                if (z) {
                    this.mLogger.logNotifDismissedIntercepted(notificationEntry.mKey);
                } else {
                    arrayList.add(notificationEntry);
                    if (notificationEntry.mCancellationReason == -1) {
                        z2 = false;
                    }
                    if (!z2) {
                        try {
                            this.mStatusBarService.onNotificationClear(notificationEntry.mSbn.getPackageName(), notificationEntry.mSbn.getUser().getIdentifier(), notificationEntry.mSbn.getKey(), dismissedByUserStats.dismissalSurface, dismissedByUserStats.dismissalSentiment, dismissedByUserStats.notificationVisibility);
                        } catch (RemoteException e) {
                            this.mLogger.logRemoteExceptionOnNotificationClear(notificationEntry.mKey, e);
                        }
                    }
                }
            }
        }
        locallyDismissNotifications(arrayList);
        dispatchEventsAndRebuildList();
    }
}
