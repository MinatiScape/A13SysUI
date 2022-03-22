package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.util.LinkedHashMap;
import java.util.Objects;
/* compiled from: SmartspaceDedupingCoordinator.kt */
/* loaded from: classes.dex */
public final class SmartspaceDedupingCoordinator implements Coordinator {
    public final SystemClock clock;
    public final DelayableExecutor executor;
    public boolean isOnLockscreen;
    public final NotifPipeline notifPipeline;
    public final NotificationEntryManager notificationEntryManager;
    public final NotificationLockscreenUserManager notificationLockscreenUserManager;
    public final LockscreenSmartspaceController smartspaceController;
    public final SysuiStatusBarStateController statusBarStateController;
    public LinkedHashMap trackedSmartspaceTargets = new LinkedHashMap();
    public final SmartspaceDedupingCoordinator$filter$1 filter = new NotifFilter() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator$filter$1
        {
            super("SmartspaceDedupingFilter");
        }

        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter
        public final boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            boolean z;
            SmartspaceDedupingCoordinator smartspaceDedupingCoordinator = SmartspaceDedupingCoordinator.this;
            if (!smartspaceDedupingCoordinator.isOnLockscreen) {
                return false;
            }
            LinkedHashMap linkedHashMap = smartspaceDedupingCoordinator.trackedSmartspaceTargets;
            Objects.requireNonNull(notificationEntry);
            TrackedSmartspaceTarget trackedSmartspaceTarget = (TrackedSmartspaceTarget) linkedHashMap.get(notificationEntry.mKey);
            if (trackedSmartspaceTarget == null) {
                z = false;
            } else {
                z = trackedSmartspaceTarget.shouldFilter;
            }
            if (z) {
                return true;
            }
            return false;
        }
    };
    public final SmartspaceDedupingCoordinator$collectionListener$1 collectionListener = new NotifCollectionListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator$collectionListener$1
        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onEntryAdded(NotificationEntry notificationEntry) {
            TrackedSmartspaceTarget trackedSmartspaceTarget = (TrackedSmartspaceTarget) SmartspaceDedupingCoordinator.this.trackedSmartspaceTargets.get(notificationEntry.mKey);
            if (trackedSmartspaceTarget != null) {
                SmartspaceDedupingCoordinator.this.updateFilterStatus(trackedSmartspaceTarget);
            }
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onEntryRemoved(NotificationEntry notificationEntry, int i) {
            TrackedSmartspaceTarget trackedSmartspaceTarget = (TrackedSmartspaceTarget) SmartspaceDedupingCoordinator.this.trackedSmartspaceTargets.get(notificationEntry.mKey);
            if (trackedSmartspaceTarget != null) {
                Objects.requireNonNull(SmartspaceDedupingCoordinator.this);
                Runnable runnable = trackedSmartspaceTarget.cancelTimeoutRunnable;
                if (runnable != null) {
                    runnable.run();
                }
                trackedSmartspaceTarget.cancelTimeoutRunnable = null;
                trackedSmartspaceTarget.alertExceptionExpires = 0L;
            }
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onEntryUpdated(NotificationEntry notificationEntry) {
            TrackedSmartspaceTarget trackedSmartspaceTarget = (TrackedSmartspaceTarget) SmartspaceDedupingCoordinator.this.trackedSmartspaceTargets.get(notificationEntry.mKey);
            if (trackedSmartspaceTarget != null) {
                SmartspaceDedupingCoordinator.this.updateFilterStatus(trackedSmartspaceTarget);
            }
        }
    };
    public final SmartspaceDedupingCoordinator$statusBarStateListener$1 statusBarStateListener = new StatusBarStateController.StateListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator$statusBarStateListener$1
        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public final void onStateChanged(int i) {
            SmartspaceDedupingCoordinator smartspaceDedupingCoordinator = SmartspaceDedupingCoordinator.this;
            Objects.requireNonNull(smartspaceDedupingCoordinator);
            boolean z = smartspaceDedupingCoordinator.isOnLockscreen;
            boolean z2 = true;
            if (i != 1) {
                z2 = false;
            }
            smartspaceDedupingCoordinator.isOnLockscreen = z2;
            if (z2 != z) {
                smartspaceDedupingCoordinator.filter.invalidateList();
            }
        }
    };

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public final void attach(NotifPipeline notifPipeline) {
        notifPipeline.addPreGroupFilter(this.filter);
        notifPipeline.addCollectionListener(this.collectionListener);
        this.statusBarStateController.addCallback(this.statusBarStateListener);
        LockscreenSmartspaceController lockscreenSmartspaceController = this.smartspaceController;
        BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceDedupingCoordinator$attach$1 = new BcSmartspaceDataPlugin.SmartspaceTargetListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator$attach$1
            /* JADX WARN: Removed duplicated region for block: B:19:0x0052  */
            /* JADX WARN: Removed duplicated region for block: B:28:0x0073  */
            @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceTargetListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public final void onSmartspaceTargetsUpdated(java.util.List<? extends android.os.Parcelable> r6) {
                /*
                    r5 = this;
                    com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator r5 = com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator.this
                    java.util.Objects.requireNonNull(r5)
                    java.util.LinkedHashMap r0 = new java.util.LinkedHashMap
                    r0.<init>()
                    java.util.LinkedHashMap r1 = r5.trackedSmartspaceTargets
                    java.util.Iterator r6 = r6.iterator()
                    boolean r2 = r6.hasNext()
                    if (r2 == 0) goto L_0x0043
                    java.lang.Object r6 = r6.next()
                    android.os.Parcelable r6 = (android.os.Parcelable) r6
                    boolean r2 = r6 instanceof android.app.smartspace.SmartspaceTarget
                    if (r2 == 0) goto L_0x0023
                    android.app.smartspace.SmartspaceTarget r6 = (android.app.smartspace.SmartspaceTarget) r6
                    goto L_0x0024
                L_0x0023:
                    r6 = 0
                L_0x0024:
                    if (r6 != 0) goto L_0x0027
                    goto L_0x0043
                L_0x0027:
                    java.lang.String r6 = r6.getSourceNotificationKey()
                    if (r6 != 0) goto L_0x002e
                    goto L_0x0043
                L_0x002e:
                    java.lang.Object r2 = r1.get(r6)
                    if (r2 != 0) goto L_0x0039
                    com.android.systemui.statusbar.notification.collection.coordinator.TrackedSmartspaceTarget r2 = new com.android.systemui.statusbar.notification.collection.coordinator.TrackedSmartspaceTarget
                    r2.<init>(r6)
                L_0x0039:
                    com.android.systemui.statusbar.notification.collection.coordinator.TrackedSmartspaceTarget r2 = (com.android.systemui.statusbar.notification.collection.coordinator.TrackedSmartspaceTarget) r2
                    r0.put(r6, r2)
                    boolean r6 = r5.updateFilterStatus(r2)
                    goto L_0x0044
                L_0x0043:
                    r6 = 0
                L_0x0044:
                    java.util.Set r2 = r1.keySet()
                    java.util.Iterator r2 = r2.iterator()
                L_0x004c:
                    boolean r3 = r2.hasNext()
                    if (r3 == 0) goto L_0x0071
                    java.lang.Object r3 = r2.next()
                    java.lang.String r3 = (java.lang.String) r3
                    boolean r4 = r0.containsKey(r3)
                    if (r4 != 0) goto L_0x004c
                    java.lang.Object r6 = r1.get(r3)
                    com.android.systemui.statusbar.notification.collection.coordinator.TrackedSmartspaceTarget r6 = (com.android.systemui.statusbar.notification.collection.coordinator.TrackedSmartspaceTarget) r6
                    if (r6 != 0) goto L_0x0067
                    goto L_0x006f
                L_0x0067:
                    java.lang.Runnable r6 = r6.cancelTimeoutRunnable
                    if (r6 != 0) goto L_0x006c
                    goto L_0x006f
                L_0x006c:
                    r6.run()
                L_0x006f:
                    r6 = 1
                    goto L_0x004c
                L_0x0071:
                    if (r6 == 0) goto L_0x007f
                    com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator$filter$1 r6 = r5.filter
                    r6.invalidateList()
                    com.android.systemui.statusbar.notification.NotificationEntryManager r6 = r5.notificationEntryManager
                    java.lang.String r1 = "Smartspace targets changed"
                    r6.updateNotifications(r1)
                L_0x007f:
                    r5.trackedSmartspaceTargets = r0
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator$attach$1.onSmartspaceTargetsUpdated(java.util.List):void");
            }
        };
        Objects.requireNonNull(lockscreenSmartspaceController);
        lockscreenSmartspaceController.execution.assertIsMainThread();
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = lockscreenSmartspaceController.plugin;
        if (bcSmartspaceDataPlugin != null) {
            bcSmartspaceDataPlugin.registerListener(smartspaceDedupingCoordinator$attach$1);
        }
        if (!notifPipeline.isNewPipelineEnabled) {
            this.notificationLockscreenUserManager.addKeyguardNotificationSuppressor(new NotificationLockscreenUserManager.KeyguardNotificationSuppressor() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator$attach$2
                @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager.KeyguardNotificationSuppressor
                public final boolean shouldSuppressOnKeyguard(NotificationEntry notificationEntry) {
                    SmartspaceDedupingCoordinator smartspaceDedupingCoordinator = SmartspaceDedupingCoordinator.this;
                    Objects.requireNonNull(smartspaceDedupingCoordinator);
                    LinkedHashMap linkedHashMap = smartspaceDedupingCoordinator.trackedSmartspaceTargets;
                    Objects.requireNonNull(notificationEntry);
                    TrackedSmartspaceTarget trackedSmartspaceTarget = (TrackedSmartspaceTarget) linkedHashMap.get(notificationEntry.mKey);
                    if (trackedSmartspaceTarget == null) {
                        return false;
                    }
                    return trackedSmartspaceTarget.shouldFilter;
                }
            });
        }
        int state = this.statusBarStateController.getState();
        boolean z = this.isOnLockscreen;
        boolean z2 = true;
        if (state != 1) {
            z2 = false;
        }
        this.isOnLockscreen = z2;
        if (z2 != z) {
            invalidateList();
        }
    }

    public final boolean updateFilterStatus(final TrackedSmartspaceTarget trackedSmartspaceTarget) {
        boolean z;
        boolean z2 = trackedSmartspaceTarget.shouldFilter;
        NotificationEntry entry = this.notifPipeline.getEntry(trackedSmartspaceTarget.key);
        if (entry != null) {
            long currentTimeMillis = this.clock.currentTimeMillis();
            long lastAudiblyAlertedMillis = entry.mRanking.getLastAudiblyAlertedMillis();
            long j = SmartspaceDedupingCoordinatorKt.ALERT_WINDOW;
            long j2 = lastAudiblyAlertedMillis + j;
            if (j2 != trackedSmartspaceTarget.alertExceptionExpires && j2 > currentTimeMillis) {
                Runnable runnable = trackedSmartspaceTarget.cancelTimeoutRunnable;
                if (runnable != null) {
                    runnable.run();
                }
                trackedSmartspaceTarget.alertExceptionExpires = j2;
                trackedSmartspaceTarget.cancelTimeoutRunnable = this.executor.executeDelayed(new Runnable() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator$updateAlertException$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        TrackedSmartspaceTarget trackedSmartspaceTarget2 = TrackedSmartspaceTarget.this;
                        Objects.requireNonNull(trackedSmartspaceTarget2);
                        trackedSmartspaceTarget2.cancelTimeoutRunnable = null;
                        TrackedSmartspaceTarget trackedSmartspaceTarget3 = TrackedSmartspaceTarget.this;
                        Objects.requireNonNull(trackedSmartspaceTarget3);
                        trackedSmartspaceTarget3.shouldFilter = true;
                        invalidateList();
                        this.notificationEntryManager.updateNotifications("deduping timeout expired");
                    }
                }, j2 - currentTimeMillis);
            }
            if (this.clock.currentTimeMillis() - entry.mRanking.getLastAudiblyAlertedMillis() <= j) {
                z = true;
            } else {
                z = false;
            }
            trackedSmartspaceTarget.shouldFilter = !z;
        }
        if (trackedSmartspaceTarget.shouldFilter == z2 || !this.isOnLockscreen) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator$filter$1] */
    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator$collectionListener$1] */
    /* JADX WARN: Type inference failed for: r1v4, types: [com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator$statusBarStateListener$1] */
    public SmartspaceDedupingCoordinator(SysuiStatusBarStateController sysuiStatusBarStateController, LockscreenSmartspaceController lockscreenSmartspaceController, NotificationEntryManager notificationEntryManager, NotificationLockscreenUserManager notificationLockscreenUserManager, NotifPipeline notifPipeline, DelayableExecutor delayableExecutor, SystemClock systemClock) {
        this.statusBarStateController = sysuiStatusBarStateController;
        this.smartspaceController = lockscreenSmartspaceController;
        this.notificationEntryManager = notificationEntryManager;
        this.notificationLockscreenUserManager = notificationLockscreenUserManager;
        this.notifPipeline = notifPipeline;
        this.executor = delayableExecutor;
        this.clock = systemClock;
    }
}
