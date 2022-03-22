package com.android.systemui.statusbar.notification.collection.coordinator;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.ArrayMap;
import android.util.ArraySet;
import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.AlertingNotificationManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifCollection$$ExternalSyntheticLambda2;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeFinalizeFilterListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeTransformGroupsListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifComparator;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinder;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import com.android.systemui.util.Assert;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.function.BiFunction;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: HeadsUpCoordinator.kt */
/* loaded from: classes.dex */
public final class HeadsUpCoordinator implements Coordinator {
    public NotifLifetimeExtender.OnEndLifetimeExtensionCallback mEndLifetimeExtension;
    public final DelayableExecutor mExecutor;
    public final HeadsUpManager mHeadsUpManager;
    public final HeadsUpViewBinder mHeadsUpViewBinder;
    public final NodeController mIncomingHeaderController;
    public final HeadsUpCoordinatorLogger mLogger;
    public NotifPipeline mNotifPipeline;
    public final NotificationInterruptStateProvider mNotificationInterruptStateProvider;
    public final NotificationRemoteInputManager mRemoteInputManager;
    public final SystemClock mSystemClock;
    public final ArrayMap<String, Long> mEntriesBindingUntil = new ArrayMap<>();
    public long mNow = -1;
    public final ArraySet<NotificationEntry> mNotifsExtendingLifetime = new ArraySet<>();
    public final LinkedHashMap<String, PostedEntry> mPostedEntries = new LinkedHashMap<>();
    public final HeadsUpCoordinator$mNotifCollectionListener$1 mNotifCollectionListener = new NotifCollectionListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$mNotifCollectionListener$1
        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onEntryAdded(NotificationEntry notificationEntry) {
            HeadsUpCoordinator.this.mPostedEntries.put(notificationEntry.mKey, new HeadsUpCoordinator.PostedEntry(notificationEntry, true, false, HeadsUpCoordinator.this.mNotificationInterruptStateProvider.shouldHeadsUp(notificationEntry), true, false, false));
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onEntryCleanUp(NotificationEntry notificationEntry) {
            HeadsUpCoordinator.this.mHeadsUpViewBinder.abortBindCallback(notificationEntry);
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onEntryRemoved(NotificationEntry notificationEntry, int i) {
            boolean z;
            HeadsUpCoordinator.this.mPostedEntries.remove(notificationEntry.mKey);
            HeadsUpCoordinator headsUpCoordinator = HeadsUpCoordinator.this;
            Objects.requireNonNull(headsUpCoordinator);
            headsUpCoordinator.mEntriesBindingUntil.remove(notificationEntry.mKey);
            headsUpCoordinator.mHeadsUpViewBinder.abortBindCallback(notificationEntry);
            String str = notificationEntry.mKey;
            if (HeadsUpCoordinator.this.mHeadsUpManager.isAlerting(str)) {
                if (!HeadsUpCoordinator.this.mRemoteInputManager.isSpinning(str) || NotificationRemoteInputManager.FORCE_REMOTE_INPUT_HISTORY) {
                    z = false;
                } else {
                    z = true;
                }
                HeadsUpCoordinator.this.mHeadsUpManager.removeNotification(notificationEntry.mKey, z);
            }
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onEntryUpdated(final NotificationEntry notificationEntry) {
            final boolean z;
            final boolean shouldHeadsUp = HeadsUpCoordinator.this.mNotificationInterruptStateProvider.shouldHeadsUp(notificationEntry);
            Objects.requireNonNull(HeadsUpCoordinator.this);
            if (!notificationEntry.interruption || (notificationEntry.mSbn.getNotification().flags & 8) == 0) {
                z = true;
            } else {
                z = false;
            }
            final boolean isAlerting = HeadsUpCoordinator.this.mHeadsUpManager.isAlerting(notificationEntry.mKey);
            final boolean isEntryBinding = HeadsUpCoordinator.this.isEntryBinding(notificationEntry);
            HeadsUpCoordinator.this.mPostedEntries.compute(notificationEntry.mKey, new BiFunction() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$mNotifCollectionListener$1$onEntryUpdated$1
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    boolean z2;
                    String str = (String) obj;
                    HeadsUpCoordinator.PostedEntry postedEntry = (HeadsUpCoordinator.PostedEntry) obj2;
                    if (postedEntry == null) {
                        postedEntry = null;
                    } else {
                        boolean z3 = shouldHeadsUp;
                        boolean z4 = z;
                        boolean z5 = isAlerting;
                        boolean z6 = isEntryBinding;
                        boolean z7 = true;
                        postedEntry.wasUpdated = true;
                        if (postedEntry.shouldHeadsUpEver || z3) {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                        postedEntry.shouldHeadsUpEver = z2;
                        if (!postedEntry.shouldHeadsUpAgain && !z4) {
                            z7 = false;
                        }
                        postedEntry.shouldHeadsUpAgain = z7;
                        postedEntry.isAlerting = z5;
                        postedEntry.isBinding = z6;
                    }
                    if (postedEntry == null) {
                        return new HeadsUpCoordinator.PostedEntry(NotificationEntry.this, false, true, shouldHeadsUp, z, isAlerting, isEntryBinding);
                    }
                    return postedEntry;
                }
            });
        }
    };
    public final HeadsUpCoordinator$mLifetimeExtender$1 mLifetimeExtender = new NotifLifetimeExtender() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$mLifetimeExtender$1
        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender
        public final String getName() {
            return "HeadsUpCoordinator";
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender
        public final void cancelLifetimeExtension(NotificationEntry notificationEntry) {
            HeadsUpCoordinator.this.mNotifsExtendingLifetime.remove(notificationEntry);
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender
        public final boolean maybeExtendLifetime(final NotificationEntry notificationEntry, int i) {
            boolean z = false;
            if (HeadsUpCoordinator.this.mHeadsUpManager.canRemoveImmediately(notificationEntry.mKey)) {
                return false;
            }
            HeadsUpCoordinator headsUpCoordinator = HeadsUpCoordinator.this;
            Objects.requireNonNull(headsUpCoordinator);
            HeadsUpManager headsUpManager = headsUpCoordinator.mHeadsUpManager;
            String str = notificationEntry.mKey;
            Objects.requireNonNull(headsUpManager);
            AlertingNotificationManager.AlertEntry alertEntry = headsUpManager.mAlertEntries.get(str);
            if (alertEntry != null) {
                z = alertEntry.isSticky();
            }
            if (z) {
                HeadsUpManager headsUpManager2 = HeadsUpCoordinator.this.mHeadsUpManager;
                String str2 = notificationEntry.mKey;
                Objects.requireNonNull(headsUpManager2);
                AlertingNotificationManager.AlertEntry alertEntry2 = headsUpManager2.mAlertEntries.get(str2);
                long j = 0;
                if (alertEntry2 != null) {
                    long j2 = alertEntry2.mEarliestRemovaltime;
                    Objects.requireNonNull(headsUpManager2.mClock);
                    j = Math.max(0L, j2 - android.os.SystemClock.elapsedRealtime());
                }
                final HeadsUpCoordinator headsUpCoordinator2 = HeadsUpCoordinator.this;
                headsUpCoordinator2.mExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$mLifetimeExtender$1$maybeExtendLifetime$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        HeadsUpManager headsUpManager3 = HeadsUpCoordinator.this.mHeadsUpManager;
                        NotificationEntry notificationEntry2 = notificationEntry;
                        Objects.requireNonNull(notificationEntry2);
                        boolean canRemoveImmediately = headsUpManager3.canRemoveImmediately(notificationEntry2.mKey);
                        if (HeadsUpCoordinator.this.mNotifsExtendingLifetime.contains(notificationEntry) && canRemoveImmediately) {
                            HeadsUpManager headsUpManager4 = HeadsUpCoordinator.this.mHeadsUpManager;
                            NotificationEntry notificationEntry3 = notificationEntry;
                            Objects.requireNonNull(notificationEntry3);
                            headsUpManager4.removeNotification(notificationEntry3.mKey, true);
                        }
                    }
                }, j);
            } else {
                final HeadsUpCoordinator headsUpCoordinator3 = HeadsUpCoordinator.this;
                headsUpCoordinator3.mExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$mLifetimeExtender$1$maybeExtendLifetime$2
                    @Override // java.lang.Runnable
                    public final void run() {
                        HeadsUpManager headsUpManager3 = HeadsUpCoordinator.this.mHeadsUpManager;
                        NotificationEntry notificationEntry2 = notificationEntry;
                        Objects.requireNonNull(notificationEntry2);
                        headsUpManager3.removeNotification(notificationEntry2.mKey, false);
                    }
                });
            }
            HeadsUpCoordinator.this.mNotifsExtendingLifetime.add(notificationEntry);
            return true;
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender
        public final void setCallback(NotifCollection$$ExternalSyntheticLambda2 notifCollection$$ExternalSyntheticLambda2) {
            HeadsUpCoordinator.this.mEndLifetimeExtension = notifCollection$$ExternalSyntheticLambda2;
        }
    };
    public final HeadsUpCoordinator$mNotifPromoter$1 mNotifPromoter = new NotifPromoter() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$mNotifPromoter$1
        {
            super("HeadsUpCoordinator");
        }

        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter
        public final boolean shouldPromoteToTopLevel(NotificationEntry notificationEntry) {
            return HeadsUpCoordinator.access$isGoingToShowHunNoRetract(HeadsUpCoordinator.this, notificationEntry);
        }
    };
    public final HeadsUpCoordinator$sectioner$1 sectioner = new NotifSectioner() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$sectioner$1
        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner
        public final NodeController getHeaderNodeController() {
            return null;
        }

        {
            super("HeadsUp", 2);
        }

        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner
        public final NotifComparator getComparator() {
            final HeadsUpCoordinator headsUpCoordinator = HeadsUpCoordinator.this;
            return new NotifComparator() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$sectioner$1$getComparator$1
                {
                    super("HeadsUp");
                }

                @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifComparator
                public final int compare(ListEntry listEntry, ListEntry listEntry2) {
                    return HeadsUpCoordinator.this.mHeadsUpManager.compare(listEntry.getRepresentativeEntry(), listEntry2.getRepresentativeEntry());
                }
            };
        }

        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner
        public final boolean isInSection(ListEntry listEntry) {
            return HeadsUpCoordinator.access$isGoingToShowHunNoRetract(HeadsUpCoordinator.this, listEntry);
        }
    };
    public final HeadsUpCoordinator$mOnHeadsUpChangedListener$1 mOnHeadsUpChangedListener = new OnHeadsUpChangedListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$mOnHeadsUpChangedListener$1
        @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
        public final void onHeadsUpStateChanged(NotificationEntry notificationEntry, boolean z) {
            NotifLifetimeExtender.OnEndLifetimeExtensionCallback onEndLifetimeExtensionCallback;
            if (!z) {
                HeadsUpCoordinator.this.mHeadsUpViewBinder.unbindHeadsUpView(notificationEntry);
                HeadsUpCoordinator headsUpCoordinator = HeadsUpCoordinator.this;
                Objects.requireNonNull(headsUpCoordinator);
                if (headsUpCoordinator.mNotifsExtendingLifetime.remove(notificationEntry) && (onEndLifetimeExtensionCallback = headsUpCoordinator.mEndLifetimeExtension) != null) {
                    ((NotifCollection$$ExternalSyntheticLambda2) onEndLifetimeExtensionCallback).onEndLifetimeExtension(headsUpCoordinator.mLifetimeExtender, notificationEntry);
                }
            }
        }
    };

    /* compiled from: HeadsUpCoordinator.kt */
    /* loaded from: classes.dex */
    public static final class PostedEntry {
        public final NotificationEntry entry;
        public boolean isAlerting;
        public boolean isBinding;
        public final String key;
        public boolean shouldHeadsUpAgain;
        public boolean shouldHeadsUpEver;
        public final boolean wasAdded;
        public boolean wasUpdated;

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof PostedEntry)) {
                return false;
            }
            PostedEntry postedEntry = (PostedEntry) obj;
            return Intrinsics.areEqual(this.entry, postedEntry.entry) && this.wasAdded == postedEntry.wasAdded && this.wasUpdated == postedEntry.wasUpdated && this.shouldHeadsUpEver == postedEntry.shouldHeadsUpEver && this.shouldHeadsUpAgain == postedEntry.shouldHeadsUpAgain && this.isAlerting == postedEntry.isAlerting && this.isBinding == postedEntry.isBinding;
        }

        public final int hashCode() {
            int hashCode = this.entry.hashCode() * 31;
            boolean z = this.wasAdded;
            int i = 1;
            if (z) {
                z = true;
            }
            int i2 = z ? 1 : 0;
            int i3 = z ? 1 : 0;
            int i4 = (hashCode + i2) * 31;
            boolean z2 = this.wasUpdated;
            if (z2) {
                z2 = true;
            }
            int i5 = z2 ? 1 : 0;
            int i6 = z2 ? 1 : 0;
            int i7 = (i4 + i5) * 31;
            boolean z3 = this.shouldHeadsUpEver;
            if (z3) {
                z3 = true;
            }
            int i8 = z3 ? 1 : 0;
            int i9 = z3 ? 1 : 0;
            int i10 = (i7 + i8) * 31;
            boolean z4 = this.shouldHeadsUpAgain;
            if (z4) {
                z4 = true;
            }
            int i11 = z4 ? 1 : 0;
            int i12 = z4 ? 1 : 0;
            int i13 = (i10 + i11) * 31;
            boolean z5 = this.isAlerting;
            if (z5) {
                z5 = true;
            }
            int i14 = z5 ? 1 : 0;
            int i15 = z5 ? 1 : 0;
            int i16 = (i13 + i14) * 31;
            boolean z6 = this.isBinding;
            if (!z6) {
                i = z6 ? 1 : 0;
            }
            return i16 + i;
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("PostedEntry(entry=");
            m.append(this.entry);
            m.append(", wasAdded=");
            m.append(this.wasAdded);
            m.append(", wasUpdated=");
            m.append(this.wasUpdated);
            m.append(", shouldHeadsUpEver=");
            m.append(this.shouldHeadsUpEver);
            m.append(", shouldHeadsUpAgain=");
            m.append(this.shouldHeadsUpAgain);
            m.append(", isAlerting=");
            m.append(this.isAlerting);
            m.append(", isBinding=");
            return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(m, this.isBinding, ')');
        }

        public PostedEntry(NotificationEntry notificationEntry, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6) {
            this.entry = notificationEntry;
            this.wasAdded = z;
            this.wasUpdated = z2;
            this.shouldHeadsUpEver = z3;
            this.shouldHeadsUpAgain = z4;
            this.isAlerting = z5;
            this.isBinding = z6;
            Objects.requireNonNull(notificationEntry);
            this.key = notificationEntry.mKey;
        }
    }

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public final void attach(NotifPipeline notifPipeline) {
        this.mNotifPipeline = notifPipeline;
        this.mHeadsUpManager.addListener(this.mOnHeadsUpChangedListener);
        notifPipeline.addCollectionListener(this.mNotifCollectionListener);
        OnBeforeTransformGroupsListener headsUpCoordinator$attach$1 = new OnBeforeTransformGroupsListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$attach$1
            @Override // com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeTransformGroupsListener
            public final void onBeforeTransformGroups() {
                HeadsUpCoordinator headsUpCoordinator = HeadsUpCoordinator.this;
                Objects.requireNonNull(headsUpCoordinator);
                headsUpCoordinator.mNow = headsUpCoordinator.mSystemClock.currentTimeMillis();
                if (!headsUpCoordinator.mPostedEntries.isEmpty()) {
                    for (HeadsUpCoordinator.PostedEntry postedEntry : CollectionsKt___CollectionsKt.toList(headsUpCoordinator.mPostedEntries.values())) {
                        Objects.requireNonNull(postedEntry);
                        NotificationEntry notificationEntry = postedEntry.entry;
                        Objects.requireNonNull(notificationEntry);
                        if (!notificationEntry.mSbn.isGroup()) {
                            headsUpCoordinator.handlePostedEntry(postedEntry, "non-group");
                            headsUpCoordinator.mPostedEntries.remove(postedEntry.key);
                        }
                    }
                }
            }
        };
        ShadeListBuilder shadeListBuilder = notifPipeline.mShadeListBuilder;
        Objects.requireNonNull(shadeListBuilder);
        Assert.isMainThread();
        shadeListBuilder.mPipelineState.requireState();
        shadeListBuilder.mOnBeforeTransformGroupsListeners.add(headsUpCoordinator$attach$1);
        notifPipeline.addOnBeforeFinalizeFilterListener(new OnBeforeFinalizeFilterListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$attach$2
            /* JADX WARN: Code restructure failed: missing block: B:63:0x0184, code lost:
                if (r7 != false) goto L_0x0186;
             */
            /* JADX WARN: Code restructure failed: missing block: B:92:0x0217, code lost:
                if (r7 != false) goto L_0x021b;
             */
            @Override // com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeFinalizeFilterListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public final void onBeforeFinalizeFilter(java.util.List<? extends com.android.systemui.statusbar.notification.collection.ListEntry> r25) {
                /*
                    Method dump skipped, instructions count: 891
                    To view this dump add '--comments-level debug' option
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$attach$2.onBeforeFinalizeFilter(java.util.List):void");
            }
        });
        notifPipeline.addPromoter(this.mNotifPromoter);
        notifPipeline.addNotificationLifetimeExtender(this.mLifetimeExtender);
    }

    public final void bindForAsyncHeadsUp(PostedEntry postedEntry) {
        ArrayMap<String, Long> arrayMap = this.mEntriesBindingUntil;
        Objects.requireNonNull(postedEntry);
        arrayMap.put(postedEntry.key, Long.valueOf(this.mNow + 1000));
        this.mHeadsUpViewBinder.bindHeadsUpView(postedEntry.entry, new NotifBindPipeline.BindCallback() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$bindForAsyncHeadsUp$1
            @Override // com.android.systemui.statusbar.notification.row.NotifBindPipeline.BindCallback
            public final void onBindFinished(NotificationEntry notificationEntry) {
                HeadsUpCoordinator headsUpCoordinator = HeadsUpCoordinator.this;
                Objects.requireNonNull(headsUpCoordinator);
                headsUpCoordinator.mHeadsUpManager.showNotification(notificationEntry);
                headsUpCoordinator.mEntriesBindingUntil.remove(notificationEntry.mKey);
            }
        });
    }

    public final void handlePostedEntry(PostedEntry postedEntry, String str) {
        boolean z;
        HeadsUpCoordinatorLogger headsUpCoordinatorLogger = this.mLogger;
        Objects.requireNonNull(headsUpCoordinatorLogger);
        if (headsUpCoordinatorLogger.verbose) {
            LogBuffer logBuffer = headsUpCoordinatorLogger.buffer;
            LogLevel logLevel = LogLevel.VERBOSE;
            HeadsUpCoordinatorLogger$logPostedEntryWillEvaluate$2 headsUpCoordinatorLogger$logPostedEntryWillEvaluate$2 = HeadsUpCoordinatorLogger$logPostedEntryWillEvaluate$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                LogMessageImpl obtain = logBuffer.obtain("HeadsUpCoordinator", logLevel, headsUpCoordinatorLogger$logPostedEntryWillEvaluate$2);
                obtain.str1 = postedEntry.key;
                obtain.str2 = str;
                obtain.bool1 = postedEntry.shouldHeadsUpEver;
                obtain.bool2 = postedEntry.shouldHeadsUpAgain;
                logBuffer.push(obtain);
            }
        }
        if (!postedEntry.wasAdded) {
            boolean z2 = postedEntry.isAlerting;
            if (z2 || postedEntry.isBinding) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                if (postedEntry.shouldHeadsUpEver) {
                    if (z2) {
                        this.mHeadsUpManager.updateNotification(postedEntry.key, postedEntry.shouldHeadsUpAgain);
                    }
                } else if (z2) {
                    this.mHeadsUpManager.removeNotification(postedEntry.key, false);
                } else {
                    NotificationEntry notificationEntry = postedEntry.entry;
                    ArrayMap<String, Long> arrayMap = this.mEntriesBindingUntil;
                    Objects.requireNonNull(notificationEntry);
                    arrayMap.remove(notificationEntry.mKey);
                    this.mHeadsUpViewBinder.abortBindCallback(notificationEntry);
                }
            } else if (postedEntry.shouldHeadsUpEver && postedEntry.shouldHeadsUpAgain) {
                bindForAsyncHeadsUp(postedEntry);
            }
        } else if (postedEntry.shouldHeadsUpEver) {
            bindForAsyncHeadsUp(postedEntry);
        }
    }

    public final boolean isEntryBinding(ListEntry listEntry) {
        Long l = this.mEntriesBindingUntil.get(listEntry.getKey());
        if (l == null || l.longValue() < this.mNow) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Type inference failed for: r1v5, types: [com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$mNotifCollectionListener$1] */
    /* JADX WARN: Type inference failed for: r1v6, types: [com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$mLifetimeExtender$1] */
    /* JADX WARN: Type inference failed for: r1v7, types: [com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$mNotifPromoter$1] */
    /* JADX WARN: Type inference failed for: r1v8, types: [com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$sectioner$1] */
    /* JADX WARN: Type inference failed for: r1v9, types: [com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$mOnHeadsUpChangedListener$1] */
    public HeadsUpCoordinator(HeadsUpCoordinatorLogger headsUpCoordinatorLogger, SystemClock systemClock, HeadsUpManager headsUpManager, HeadsUpViewBinder headsUpViewBinder, NotificationInterruptStateProvider notificationInterruptStateProvider, NotificationRemoteInputManager notificationRemoteInputManager, NodeController nodeController, DelayableExecutor delayableExecutor) {
        this.mLogger = headsUpCoordinatorLogger;
        this.mSystemClock = systemClock;
        this.mHeadsUpManager = headsUpManager;
        this.mHeadsUpViewBinder = headsUpViewBinder;
        this.mNotificationInterruptStateProvider = notificationInterruptStateProvider;
        this.mRemoteInputManager = notificationRemoteInputManager;
        this.mIncomingHeaderController = nodeController;
        this.mExecutor = delayableExecutor;
    }

    public static final boolean access$isGoingToShowHunNoRetract(HeadsUpCoordinator headsUpCoordinator, ListEntry listEntry) {
        Boolean bool;
        boolean z;
        boolean z2;
        Objects.requireNonNull(headsUpCoordinator);
        PostedEntry postedEntry = headsUpCoordinator.mPostedEntries.get(listEntry.getKey());
        if (postedEntry == null) {
            bool = null;
        } else {
            if (postedEntry.isAlerting || postedEntry.isBinding) {
                z = true;
            } else {
                z = false;
            }
            if (z || (postedEntry.shouldHeadsUpEver && (postedEntry.wasAdded || postedEntry.shouldHeadsUpAgain))) {
                z2 = true;
            } else {
                z2 = false;
            }
            bool = Boolean.valueOf(z2);
        }
        if (bool != null) {
            return bool.booleanValue();
        }
        if (headsUpCoordinator.mHeadsUpManager.isAlerting(listEntry.getKey()) || headsUpCoordinator.isEntryBinding(listEntry)) {
            return true;
        }
        return false;
    }
}
