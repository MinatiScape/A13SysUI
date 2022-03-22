package com.android.systemui.statusbar.notification.collection.coordinator;

import android.app.Notification;
import android.os.RemoteException;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.notification.SectionClassifier;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManagerImpl;
import com.android.systemui.statusbar.notification.collection.inflation.NotifInflater;
import com.android.systemui.statusbar.notification.collection.inflation.NotifUiAdjustment;
import com.android.systemui.statusbar.notification.collection.inflation.NotifUiAdjustmentProvider;
import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeFinalizeFilterListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.render.NotifViewBarn;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController;
import com.android.systemui.statusbar.notification.row.NotifInflationErrorManager;
import com.android.systemui.statusbar.phone.StatusBar$2$Callback$$ExternalSyntheticLambda0;
import com.google.android.systemui.lowlightclock.LowLightDockManager$$ExternalSyntheticLambda0;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes.dex */
public final class PreparationCoordinator implements Coordinator {
    public final NotifUiAdjustmentProvider mAdjustmentProvider;
    public final BindEventManagerImpl mBindEventManager;
    public final int mChildBindCutoff;
    public final PreparationCoordinatorLogger mLogger;
    public final long mMaxGroupInflationDelay;
    public final NotifInflationErrorManager mNotifErrorManager;
    public final NotifInflater mNotifInflater;
    public final IStatusBarService mStatusBarService;
    public final NotifViewBarn mViewBarn;
    public final ArrayMap<NotificationEntry, Integer> mInflationStates = new ArrayMap<>();
    public final ArrayMap<NotificationEntry, NotifUiAdjustment> mInflationAdjustments = new ArrayMap<>();
    public final ArraySet<NotificationEntry> mInflatingNotifs = new ArraySet<>();
    public final AnonymousClass1 mNotifCollectionListener = new NotifCollectionListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinator.1
        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onEntryCleanUp(NotificationEntry notificationEntry) {
            PreparationCoordinator.this.mInflationStates.remove(notificationEntry);
            NotifViewBarn notifViewBarn = PreparationCoordinator.this.mViewBarn;
            Objects.requireNonNull(notifViewBarn);
            notifViewBarn.rowMap.remove(notificationEntry.getKey());
            PreparationCoordinator.this.mInflationAdjustments.remove(notificationEntry);
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onEntryInit(NotificationEntry notificationEntry) {
            PreparationCoordinator.this.mInflationStates.put(notificationEntry, 0);
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onEntryRemoved(NotificationEntry notificationEntry, int i) {
            PreparationCoordinator preparationCoordinator = PreparationCoordinator.this;
            preparationCoordinator.abortInflation(notificationEntry, "entryRemoved reason=" + i);
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onEntryUpdated(NotificationEntry notificationEntry) {
            PreparationCoordinator.this.abortInflation(notificationEntry, "entryUpdated");
            int inflationState = PreparationCoordinator.this.getInflationState(notificationEntry);
            if (inflationState == 1) {
                PreparationCoordinator.this.mInflationStates.put(notificationEntry, 2);
            } else if (inflationState == -1) {
                PreparationCoordinator.this.mInflationStates.put(notificationEntry, 0);
            }
        }
    };
    public final AnonymousClass2 mNotifInflationErrorFilter = new NotifFilter() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinator.2
        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter
        public final boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            if (PreparationCoordinator.this.getInflationState(notificationEntry) == -1) {
                return true;
            }
            return false;
        }
    };
    public final AnonymousClass3 mNotifInflatingFilter = new NotifFilter() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinator.3
        public final ArrayMap mIsDelayedGroupCache = new ArrayMap();

        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Pluggable
        public final void onCleanup() {
            this.mIsDelayedGroupCache.clear();
        }

        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter
        public final boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            boolean z;
            boolean z2;
            boolean z3;
            GroupEntry parent = notificationEntry.getParent();
            Objects.requireNonNull(parent);
            Boolean bool = (Boolean) this.mIsDelayedGroupCache.get(parent);
            if (bool == null) {
                PreparationCoordinator preparationCoordinator = PreparationCoordinator.this;
                Objects.requireNonNull(preparationCoordinator);
                if (parent != GroupEntry.ROOT_ENTRY && !parent.wasAttachedInPreviousPass()) {
                    if (j - parent.mCreationTime > preparationCoordinator.mMaxGroupInflationDelay) {
                        z3 = true;
                    } else {
                        z3 = false;
                    }
                    if (z3) {
                        PreparationCoordinatorLogger preparationCoordinatorLogger = preparationCoordinator.mLogger;
                        String str = parent.mKey;
                        Objects.requireNonNull(preparationCoordinatorLogger);
                        LogBuffer logBuffer = preparationCoordinatorLogger.buffer;
                        LogLevel logLevel = LogLevel.WARNING;
                        PreparationCoordinatorLogger$logGroupInflationTookTooLong$2 preparationCoordinatorLogger$logGroupInflationTookTooLong$2 = PreparationCoordinatorLogger$logGroupInflationTookTooLong$2.INSTANCE;
                        Objects.requireNonNull(logBuffer);
                        if (!logBuffer.frozen) {
                            LogMessageImpl obtain = logBuffer.obtain("PreparationCoordinator", logLevel, preparationCoordinatorLogger$logGroupInflationTookTooLong$2);
                            obtain.str1 = str;
                            logBuffer.push(obtain);
                        }
                    } else {
                        if (preparationCoordinator.mInflatingNotifs.contains(parent.mSummary)) {
                            PreparationCoordinatorLogger preparationCoordinatorLogger2 = preparationCoordinator.mLogger;
                            String str2 = parent.mKey;
                            NotificationEntry notificationEntry2 = parent.mSummary;
                            Objects.requireNonNull(notificationEntry2);
                            preparationCoordinatorLogger2.logDelayingGroupRelease(str2, notificationEntry2.mKey);
                        } else {
                            for (NotificationEntry notificationEntry3 : parent.mUnmodifiableChildren) {
                                if (preparationCoordinator.mInflatingNotifs.contains(notificationEntry3) && !notificationEntry3.wasAttachedInPreviousPass()) {
                                    preparationCoordinator.mLogger.logDelayingGroupRelease(parent.mKey, notificationEntry3.mKey);
                                }
                            }
                            PreparationCoordinatorLogger preparationCoordinatorLogger3 = preparationCoordinator.mLogger;
                            String str3 = parent.mKey;
                            Objects.requireNonNull(preparationCoordinatorLogger3);
                            LogBuffer logBuffer2 = preparationCoordinatorLogger3.buffer;
                            LogLevel logLevel2 = LogLevel.DEBUG;
                            PreparationCoordinatorLogger$logDoneWaitingForGroupInflation$2 preparationCoordinatorLogger$logDoneWaitingForGroupInflation$2 = PreparationCoordinatorLogger$logDoneWaitingForGroupInflation$2.INSTANCE;
                            Objects.requireNonNull(logBuffer2);
                            if (!logBuffer2.frozen) {
                                LogMessageImpl obtain2 = logBuffer2.obtain("PreparationCoordinator", logLevel2, preparationCoordinatorLogger$logDoneWaitingForGroupInflation$2);
                                obtain2.str1 = str3;
                                logBuffer2.push(obtain2);
                            }
                        }
                        z2 = true;
                        bool = Boolean.valueOf(z2);
                        this.mIsDelayedGroupCache.put(parent, bool);
                    }
                }
                z2 = false;
                bool = Boolean.valueOf(z2);
                this.mIsDelayedGroupCache.put(parent, bool);
            }
            PreparationCoordinator preparationCoordinator2 = PreparationCoordinator.this;
            Objects.requireNonNull(preparationCoordinator2);
            int inflationState = preparationCoordinator2.getInflationState(notificationEntry);
            if (inflationState == 1 || inflationState == 2) {
                z = true;
            } else {
                z = false;
            }
            if (!z || bool.booleanValue()) {
                return true;
            }
            return false;
        }
    };
    public final AnonymousClass4 mInflationErrorListener = new NotifInflationErrorManager.NotifInflationErrorListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinator.4
        @Override // com.android.systemui.statusbar.notification.row.NotifInflationErrorManager.NotifInflationErrorListener
        public final void onNotifInflationError(NotificationEntry notificationEntry, Exception exc) {
            NotifViewBarn notifViewBarn = PreparationCoordinator.this.mViewBarn;
            Objects.requireNonNull(notifViewBarn);
            notifViewBarn.rowMap.remove(notificationEntry.getKey());
            PreparationCoordinator.this.mInflationStates.put(notificationEntry, -1);
            try {
                StatusBarNotification statusBarNotification = notificationEntry.mSbn;
                PreparationCoordinator.this.mStatusBarService.onNotificationError(statusBarNotification.getPackageName(), statusBarNotification.getTag(), statusBarNotification.getId(), statusBarNotification.getUid(), statusBarNotification.getInitialPid(), exc.getMessage(), statusBarNotification.getUser().getIdentifier());
            } catch (RemoteException unused) {
            }
            invalidateList();
        }

        @Override // com.android.systemui.statusbar.notification.row.NotifInflationErrorManager.NotifInflationErrorListener
        public final void onNotifInflationErrorCleared() {
            invalidateList();
        }
    };

    public final void abortInflation(NotificationEntry notificationEntry, String str) {
        PreparationCoordinatorLogger preparationCoordinatorLogger = this.mLogger;
        Objects.requireNonNull(notificationEntry);
        String str2 = notificationEntry.mKey;
        Objects.requireNonNull(preparationCoordinatorLogger);
        LogBuffer logBuffer = preparationCoordinatorLogger.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        PreparationCoordinatorLogger$logInflationAborted$2 preparationCoordinatorLogger$logInflationAborted$2 = PreparationCoordinatorLogger$logInflationAborted$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("PreparationCoordinator", logLevel, preparationCoordinatorLogger$logInflationAborted$2);
            obtain.str1 = str2;
            obtain.str2 = str;
            logBuffer.push(obtain);
        }
        this.mNotifInflater.abortInflation(notificationEntry);
        this.mInflatingNotifs.remove(notificationEntry);
    }

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public final void attach(NotifPipeline notifPipeline) {
        NotifInflationErrorManager notifInflationErrorManager = this.mNotifErrorManager;
        AnonymousClass4 r1 = this.mInflationErrorListener;
        Objects.requireNonNull(notifInflationErrorManager);
        notifInflationErrorManager.mListeners.add(r1);
        notifPipeline.addCollectionListener(this.mNotifCollectionListener);
        notifPipeline.addOnBeforeFinalizeFilterListener(new OnBeforeFinalizeFilterListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinator$$ExternalSyntheticLambda0
            @Override // com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeFinalizeFilterListener
            public final void onBeforeFinalizeFilter(List list) {
                boolean z;
                PreparationCoordinator preparationCoordinator = PreparationCoordinator.this;
                Objects.requireNonNull(preparationCoordinator);
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    ListEntry listEntry = (ListEntry) list.get(i);
                    if (listEntry instanceof GroupEntry) {
                        GroupEntry groupEntry = (GroupEntry) listEntry;
                        Objects.requireNonNull(groupEntry);
                        NotificationEntry notificationEntry = groupEntry.mSummary;
                        List<NotificationEntry> list2 = groupEntry.mUnmodifiableChildren;
                        preparationCoordinator.inflateRequiredNotifViews(notificationEntry);
                        for (int i2 = 0; i2 < list2.size(); i2++) {
                            NotificationEntry notificationEntry2 = list2.get(i2);
                            boolean z2 = true;
                            if (i2 < preparationCoordinator.mChildBindCutoff) {
                                z = true;
                            } else {
                                z = false;
                            }
                            if (z) {
                                preparationCoordinator.inflateRequiredNotifViews(notificationEntry2);
                            } else {
                                if (preparationCoordinator.mInflatingNotifs.contains(notificationEntry2)) {
                                    preparationCoordinator.abortInflation(notificationEntry2, "Past last visible group child");
                                }
                                int inflationState = preparationCoordinator.getInflationState(notificationEntry2);
                                if (!(inflationState == 1 || inflationState == 2)) {
                                    z2 = false;
                                }
                                if (z2) {
                                    NotifViewBarn notifViewBarn = preparationCoordinator.mViewBarn;
                                    Objects.requireNonNull(notifViewBarn);
                                    notifViewBarn.rowMap.remove(notificationEntry2.getKey());
                                    preparationCoordinator.mInflationStates.put(notificationEntry2, 0);
                                }
                            }
                        }
                    } else {
                        preparationCoordinator.inflateRequiredNotifViews((NotificationEntry) listEntry);
                    }
                }
            }
        });
        notifPipeline.addFinalizeFilter(this.mNotifInflationErrorFilter);
        notifPipeline.addFinalizeFilter(this.mNotifInflatingFilter);
    }

    public final int getInflationState(NotificationEntry notificationEntry) {
        Integer num = this.mInflationStates.get(notificationEntry);
        Objects.requireNonNull(num, "Asking state of a notification preparation coordinator doesn't know about");
        return num.intValue();
    }

    public final void inflateRequiredNotifViews(NotificationEntry notificationEntry) {
        boolean z;
        NotifUiAdjustmentProvider notifUiAdjustmentProvider = this.mAdjustmentProvider;
        Objects.requireNonNull(notifUiAdjustmentProvider);
        String str = notificationEntry.mKey;
        List<Notification.Action> smartActions = notificationEntry.mRanking.getSmartActions();
        List<CharSequence> smartReplies = notificationEntry.mRanking.getSmartReplies();
        boolean isConversation = notificationEntry.mRanking.isConversation();
        NotifSection section = notificationEntry.getSection();
        if (section != null) {
            GroupEntry parent = notificationEntry.getParent();
            if (parent != null) {
                SectionClassifier sectionClassifier = notifUiAdjustmentProvider.sectionClassifier;
                Objects.requireNonNull(sectionClassifier);
                Set<? extends NotifSectioner> set = sectionClassifier.lowPrioritySections;
                if (set == null) {
                    set = null;
                }
                boolean contains = set.contains(section.sectioner);
                boolean areEqual = Intrinsics.areEqual(parent, GroupEntry.ROOT_ENTRY);
                boolean areEqual2 = Intrinsics.areEqual(parent.mSummary, notificationEntry);
                if (!contains || (!areEqual && !areEqual2)) {
                    z = false;
                } else {
                    z = true;
                }
                NotifUiAdjustment notifUiAdjustment = new NotifUiAdjustment(smartActions, smartReplies, isConversation, z);
                if (!this.mInflatingNotifs.contains(notificationEntry)) {
                    int intValue = this.mInflationStates.get(notificationEntry).intValue();
                    if (intValue != -1) {
                        if (intValue == 0) {
                            inflateEntry(notificationEntry, notifUiAdjustment, "entryAdded");
                        } else if (intValue != 1) {
                            if (intValue == 2) {
                                this.mInflationAdjustments.put(notificationEntry, notifUiAdjustment);
                                this.mInflatingNotifs.add(notificationEntry);
                                this.mNotifInflater.rebindViews(notificationEntry, new NotifInflater.Params(z), new LowLightDockManager$$ExternalSyntheticLambda0(this));
                            }
                        } else if (needToReinflate(notificationEntry, notifUiAdjustment, "Fully inflated notification has no adjustments")) {
                            this.mInflationAdjustments.put(notificationEntry, notifUiAdjustment);
                            this.mInflatingNotifs.add(notificationEntry);
                            this.mNotifInflater.rebindViews(notificationEntry, new NotifInflater.Params(z), new LowLightDockManager$$ExternalSyntheticLambda0(this));
                        }
                    } else if (needToReinflate(notificationEntry, notifUiAdjustment, null)) {
                        inflateEntry(notificationEntry, notifUiAdjustment, "adjustment changed after error");
                    }
                } else if (needToReinflate(notificationEntry, notifUiAdjustment, "Inflating notification has no adjustments")) {
                    inflateEntry(notificationEntry, notifUiAdjustment, "adjustment changed while inflating");
                }
            } else {
                throw new IllegalStateException("Entry must have a parent to determine if minimized".toString());
            }
        } else {
            throw new IllegalStateException("Entry must have a section to determine if minimized".toString());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:102:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x01b7  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x01c3  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x01ce  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean needToReinflate(com.android.systemui.statusbar.notification.collection.NotificationEntry r13, com.android.systemui.statusbar.notification.collection.inflation.NotifUiAdjustment r14, java.lang.String r15) {
        /*
            Method dump skipped, instructions count: 475
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinator.needToReinflate(com.android.systemui.statusbar.notification.collection.NotificationEntry, com.android.systemui.statusbar.notification.collection.inflation.NotifUiAdjustment, java.lang.String):boolean");
    }

    public static void $r8$lambda$OBZgOZcphwYSTtPwW4dGUoKs3OA(PreparationCoordinator preparationCoordinator, NotificationEntry notificationEntry, ExpandableNotificationRowController expandableNotificationRowController) {
        Objects.requireNonNull(preparationCoordinator);
        PreparationCoordinatorLogger preparationCoordinatorLogger = preparationCoordinator.mLogger;
        Objects.requireNonNull(notificationEntry);
        String str = notificationEntry.mKey;
        Objects.requireNonNull(preparationCoordinatorLogger);
        LogBuffer logBuffer = preparationCoordinatorLogger.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        PreparationCoordinatorLogger$logNotifInflated$2 preparationCoordinatorLogger$logNotifInflated$2 = PreparationCoordinatorLogger$logNotifInflated$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("PreparationCoordinator", logLevel, preparationCoordinatorLogger$logNotifInflated$2);
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
        preparationCoordinator.mInflatingNotifs.remove(notificationEntry);
        NotifViewBarn notifViewBarn = preparationCoordinator.mViewBarn;
        Objects.requireNonNull(notifViewBarn);
        notifViewBarn.rowMap.put(notificationEntry.mKey, expandableNotificationRowController);
        preparationCoordinator.mInflationStates.put(notificationEntry, 1);
        preparationCoordinator.mBindEventManager.notifyViewBound(notificationEntry);
        preparationCoordinator.mNotifInflatingFilter.invalidateList();
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinator$1] */
    /* JADX WARN: Type inference failed for: r0v4, types: [com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinator$2] */
    /* JADX WARN: Type inference failed for: r0v5, types: [com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinator$3] */
    /* JADX WARN: Type inference failed for: r0v6, types: [com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinator$4] */
    @VisibleForTesting
    public PreparationCoordinator(PreparationCoordinatorLogger preparationCoordinatorLogger, NotifInflater notifInflater, NotifInflationErrorManager notifInflationErrorManager, NotifViewBarn notifViewBarn, NotifUiAdjustmentProvider notifUiAdjustmentProvider, IStatusBarService iStatusBarService, BindEventManagerImpl bindEventManagerImpl, int i, long j) {
        this.mLogger = preparationCoordinatorLogger;
        this.mNotifInflater = notifInflater;
        this.mNotifErrorManager = notifInflationErrorManager;
        this.mViewBarn = notifViewBarn;
        this.mAdjustmentProvider = notifUiAdjustmentProvider;
        this.mStatusBarService = iStatusBarService;
        this.mChildBindCutoff = i;
        this.mMaxGroupInflationDelay = j;
        this.mBindEventManager = bindEventManagerImpl;
    }

    public final void inflateEntry(NotificationEntry notificationEntry, NotifUiAdjustment notifUiAdjustment, String str) {
        abortInflation(notificationEntry, str);
        this.mInflationAdjustments.put(notificationEntry, notifUiAdjustment);
        this.mInflatingNotifs.add(notificationEntry);
        this.mNotifInflater.inflateViews(notificationEntry, new NotifInflater.Params(notifUiAdjustment.isMinimized), new StatusBar$2$Callback$$ExternalSyntheticLambda0(this));
    }
}
