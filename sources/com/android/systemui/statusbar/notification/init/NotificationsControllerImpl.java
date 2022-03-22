package com.android.systemui.statusbar.notification.init;

import android.content.pm.PackageManager;
import android.os.Trace;
import android.service.notification.StatusBarNotification;
import android.util.IndentingPrintWriter;
import android.util.Log;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationInteractionTracker;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.notification.AnimatedImageNotificationManager;
import com.android.systemui.statusbar.notification.AnimatedImageNotificationManager$bind$3;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.NotificationClicker;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationListController;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifInflaterImpl;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStore;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.NotificationRankingManager;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.TargetSdkResolver;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescer;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManager;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManagerImpl;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.collection.init.NotifPipelineInitializer;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderListListener;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.provider.DebugModeFilterProvider;
import com.android.systemui.statusbar.notification.collection.render.RenderStageManager;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewManager;
import com.android.systemui.statusbar.notification.icon.IconManager;
import com.android.systemui.statusbar.notification.interruption.HeadsUpController;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinder;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotifBindPipelineInitializer;
import com.android.systemui.statusbar.notification.stack.NotificationChildrenContainer;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarNotificationPresenter;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import com.android.systemui.statusbar.policy.RemoteInputUriController;
import com.android.systemui.util.Assert;
import com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda1;
import dagger.Lazy;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationsControllerImpl.kt */
/* loaded from: classes.dex */
public final class NotificationsControllerImpl implements NotificationsController {
    public final AnimatedImageNotificationManager animatedImageNotificationManager;
    public final BindEventManagerImpl bindEventManagerImpl;
    public final NotificationClicker.Builder clickerBuilder;
    public final Lazy<CommonNotifCollection> commonNotifCollection;
    public final DebugModeFilterProvider debugModeFilterProvider;
    public final DeviceProvisionedController deviceProvisionedController;
    public final NotificationEntryManager entryManager;
    public final NotificationGroupAlertTransferHelper groupAlertTransferHelper;
    public final Lazy<NotificationGroupManagerLegacy> groupManagerLegacy;
    public final HeadsUpController headsUpController;
    public final HeadsUpManager headsUpManager;
    public final HeadsUpViewBinder headsUpViewBinder;
    public final NotificationRankingManager legacyRanker;
    public final Lazy<NotifPipelineInitializer> newNotifPipelineInitializer;
    public final NotifBindPipelineInitializer notifBindPipelineInitializer;
    public final NotifLiveDataStore notifLiveDataStore;
    public final Lazy<NotifPipeline> notifPipeline;
    public final NotifPipelineFlags notifPipelineFlags;
    public final NotificationListener notificationListener;
    public final NotificationRowBinderImpl notificationRowBinder;
    public final PeopleSpaceWidgetManager peopleSpaceWidgetManager;
    public final RemoteInputUriController remoteInputUriController;
    public final TargetSdkResolver targetSdkResolver;

    public NotificationsControllerImpl(NotifPipelineFlags notifPipelineFlags, NotificationListener notificationListener, NotificationEntryManager notificationEntryManager, DebugModeFilterProvider debugModeFilterProvider, NotificationRankingManager notificationRankingManager, Lazy<CommonNotifCollection> lazy, Lazy<NotifPipeline> lazy2, NotifLiveDataStore notifLiveDataStore, TargetSdkResolver targetSdkResolver, Lazy<NotifPipelineInitializer> lazy3, NotifBindPipelineInitializer notifBindPipelineInitializer, DeviceProvisionedController deviceProvisionedController, NotificationRowBinderImpl notificationRowBinderImpl, BindEventManagerImpl bindEventManagerImpl, RemoteInputUriController remoteInputUriController, Lazy<NotificationGroupManagerLegacy> lazy4, NotificationGroupAlertTransferHelper notificationGroupAlertTransferHelper, HeadsUpManager headsUpManager, HeadsUpController headsUpController, HeadsUpViewBinder headsUpViewBinder, NotificationClicker.Builder builder, AnimatedImageNotificationManager animatedImageNotificationManager, PeopleSpaceWidgetManager peopleSpaceWidgetManager) {
        this.notifPipelineFlags = notifPipelineFlags;
        this.notificationListener = notificationListener;
        this.entryManager = notificationEntryManager;
        this.debugModeFilterProvider = debugModeFilterProvider;
        this.legacyRanker = notificationRankingManager;
        this.commonNotifCollection = lazy;
        this.notifPipeline = lazy2;
        this.notifLiveDataStore = notifLiveDataStore;
        this.targetSdkResolver = targetSdkResolver;
        this.newNotifPipelineInitializer = lazy3;
        this.notifBindPipelineInitializer = notifBindPipelineInitializer;
        this.deviceProvisionedController = deviceProvisionedController;
        this.notificationRowBinder = notificationRowBinderImpl;
        this.bindEventManagerImpl = bindEventManagerImpl;
        this.remoteInputUriController = remoteInputUriController;
        this.groupManagerLegacy = lazy4;
        this.groupAlertTransferHelper = notificationGroupAlertTransferHelper;
        this.headsUpManager = headsUpManager;
        this.headsUpController = headsUpController;
        this.headsUpViewBinder = headsUpViewBinder;
        this.clickerBuilder = builder;
        this.animatedImageNotificationManager = animatedImageNotificationManager;
        this.peopleSpaceWidgetManager = peopleSpaceWidgetManager;
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public final void dump(IndentingPrintWriter indentingPrintWriter) {
        NotificationEntryManager notificationEntryManager = this.entryManager;
        Objects.requireNonNull(notificationEntryManager);
        indentingPrintWriter.println("NotificationEntryManager (Legacy)");
        int size = notificationEntryManager.mSortedAndFiltered.size();
        indentingPrintWriter.print("  ");
        indentingPrintWriter.println("active notifications: " + size);
        int i = 0;
        while (i < size) {
            NotificationEntryManager.dumpEntry(indentingPrintWriter, i, notificationEntryManager.mSortedAndFiltered.get(i));
            i++;
        }
        synchronized (notificationEntryManager.mActiveNotifications) {
            int size2 = notificationEntryManager.mActiveNotifications.size();
            indentingPrintWriter.print("  ");
            indentingPrintWriter.println("inactive notifications: " + (size2 - i));
            int i2 = 0;
            for (int i3 = 0; i3 < size2; i3++) {
                NotificationEntry valueAt = notificationEntryManager.mActiveNotifications.valueAt(i3);
                if (!notificationEntryManager.mSortedAndFiltered.contains(valueAt)) {
                    NotificationEntryManager.dumpEntry(indentingPrintWriter, i2, valueAt);
                    i2++;
                }
            }
        }
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public final int getActiveNotificationsCount() {
        return ((Number) this.notifLiveDataStore.getActiveNotifCount().getValue()).intValue();
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public final void initialize(StatusBar statusBar, Optional optional, StatusBarNotificationPresenter statusBarNotificationPresenter, NotificationStackScrollLayoutController.NotificationListContainerImpl notificationListContainerImpl, NotificationStackScrollLayoutController.NotifStackControllerImpl notifStackControllerImpl, NotificationActivityStarter notificationActivityStarter, StatusBarNotificationPresenter statusBarNotificationPresenter2) {
        this.notificationListener.registerAsSystemService();
        NotificationListController notificationListController = new NotificationListController(this.entryManager, notificationListContainerImpl, this.deviceProvisionedController);
        notificationListController.mEntryManager.addNotificationEntryListener(notificationListController.mEntryListener);
        notificationListController.mDeviceProvisionedController.addCallback(notificationListController.mDeviceProvisionedListener);
        NotificationRowBinderImpl notificationRowBinderImpl = this.notificationRowBinder;
        NotificationClicker.Builder builder = this.clickerBuilder;
        Optional of = Optional.of(statusBar);
        Objects.requireNonNull(builder);
        NotificationClicker notificationClicker = new NotificationClicker(builder.mLogger, of, optional, notificationActivityStarter);
        Objects.requireNonNull(notificationRowBinderImpl);
        notificationRowBinderImpl.mNotificationClicker = notificationClicker;
        NotificationRowBinderImpl notificationRowBinderImpl2 = this.notificationRowBinder;
        Objects.requireNonNull(notificationRowBinderImpl2);
        notificationRowBinderImpl2.mPresenter = statusBarNotificationPresenter;
        notificationRowBinderImpl2.mListContainer = notificationListContainerImpl;
        notificationRowBinderImpl2.mBindRowCallback = statusBarNotificationPresenter2;
        IconManager iconManager = notificationRowBinderImpl2.mIconManager;
        Objects.requireNonNull(iconManager);
        iconManager.notifCollection.addCollectionListener(iconManager.entryListener);
        HeadsUpViewBinder headsUpViewBinder = this.headsUpViewBinder;
        Objects.requireNonNull(headsUpViewBinder);
        headsUpViewBinder.mNotificationPresenter = statusBarNotificationPresenter;
        this.notifBindPipelineInitializer.initialize();
        final AnimatedImageNotificationManager animatedImageNotificationManager = this.animatedImageNotificationManager;
        Objects.requireNonNull(animatedImageNotificationManager);
        animatedImageNotificationManager.headsUpManager.addListener(new OnHeadsUpChangedListener() { // from class: com.android.systemui.statusbar.notification.AnimatedImageNotificationManager$bind$1
            @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
            public final void onHeadsUpStateChanged(NotificationEntry notificationEntry, boolean z) {
                AnimatedImageNotificationManager.this.updateAnimatedImageDrawables(notificationEntry);
            }
        });
        animatedImageNotificationManager.statusBarStateController.addCallback(new StatusBarStateController.StateListener() { // from class: com.android.systemui.statusbar.notification.AnimatedImageNotificationManager$bind$2
            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public final void onExpandedChanged(boolean z) {
                AnimatedImageNotificationManager animatedImageNotificationManager2 = AnimatedImageNotificationManager.this;
                animatedImageNotificationManager2.isStatusBarExpanded = z;
                Collection<NotificationEntry> allNotifs = animatedImageNotificationManager2.notifCollection.getAllNotifs();
                AnimatedImageNotificationManager animatedImageNotificationManager3 = AnimatedImageNotificationManager.this;
                for (NotificationEntry notificationEntry : allNotifs) {
                    animatedImageNotificationManager3.updateAnimatedImageDrawables(notificationEntry);
                }
            }
        });
        BindEventManager bindEventManager = animatedImageNotificationManager.bindEventManager;
        AnimatedImageNotificationManager$bind$3 animatedImageNotificationManager$bind$3 = new AnimatedImageNotificationManager$bind$3(animatedImageNotificationManager);
        Objects.requireNonNull(bindEventManager);
        bindEventManager.listeners.addIfAbsent(animatedImageNotificationManager$bind$3);
        NotifPipelineInitializer notifPipelineInitializer = this.newNotifPipelineInitializer.get();
        NotificationListener notificationListener = this.notificationListener;
        NotificationRowBinderImpl notificationRowBinderImpl3 = this.notificationRowBinder;
        Objects.requireNonNull(notifPipelineInitializer);
        notifPipelineInitializer.mDumpManager.registerDumpable("NotifPipeline", notifPipelineInitializer);
        if (notifPipelineInitializer.mNotifPipelineFlags.isNewPipelineEnabled()) {
            NotifInflaterImpl notifInflaterImpl = notifPipelineInitializer.mNotifInflater;
            Objects.requireNonNull(notifInflaterImpl);
            notifInflaterImpl.mNotificationRowBinder = notificationRowBinderImpl3;
        }
        notifPipelineInitializer.mNotifPluggableCoordinators.attach(notifPipelineInitializer.mPipelineWrapper);
        if (notifPipelineInitializer.mNotifPipelineFlags.isNewPipelineEnabled()) {
            ShadeViewManager create = notifPipelineInitializer.mShadeViewManagerFactory.create(notificationListContainerImpl, notifStackControllerImpl);
            RenderStageManager renderStageManager = notifPipelineInitializer.mRenderStageManager;
            Objects.requireNonNull(create);
            renderStageManager.viewRenderer = create.viewRenderer;
        }
        final RenderStageManager renderStageManager2 = notifPipelineInitializer.mRenderStageManager;
        ShadeListBuilder shadeListBuilder = notifPipelineInitializer.mListBuilder;
        Objects.requireNonNull(renderStageManager2);
        ShadeListBuilder.OnRenderListListener renderStageManager$attach$1 = new ShadeListBuilder.OnRenderListListener() { // from class: com.android.systemui.statusbar.notification.collection.render.RenderStageManager$attach$1
            @Override // com.android.systemui.statusbar.notification.collection.ShadeListBuilder.OnRenderListListener
            public final void onRenderList(List<? extends ListEntry> list) {
                RenderStageManager renderStageManager3 = RenderStageManager.this;
                Objects.requireNonNull(renderStageManager3);
                Trace.beginSection("RenderStageManager.onRenderList");
                try {
                    NotifViewRenderer notifViewRenderer = renderStageManager3.viewRenderer;
                    if (notifViewRenderer != null) {
                        notifViewRenderer.onRenderList(list);
                        Trace.beginSection("RenderStageManager.dispatchOnAfterRenderList");
                        NotifStackController stackController = notifViewRenderer.getStackController();
                        Iterator it = renderStageManager3.onAfterRenderListListeners.iterator();
                        while (it.hasNext()) {
                            ((OnAfterRenderListListener) it.next()).onAfterRenderList(list, stackController);
                        }
                        Trace.endSection();
                        renderStageManager3.dispatchOnAfterRenderGroups(notifViewRenderer, list);
                        renderStageManager3.dispatchOnAfterRenderEntries(notifViewRenderer, list);
                        notifViewRenderer.onDispatchComplete();
                    }
                } finally {
                    Trace.endSection();
                }
            }
        };
        Assert.isMainThread();
        shadeListBuilder.mPipelineState.requireState();
        shadeListBuilder.mOnRenderListListener = renderStageManager$attach$1;
        ShadeListBuilder shadeListBuilder2 = notifPipelineInitializer.mListBuilder;
        NotifCollection notifCollection = notifPipelineInitializer.mNotifCollection;
        Objects.requireNonNull(shadeListBuilder2);
        Assert.isMainThread();
        NotificationInteractionTracker notificationInteractionTracker = shadeListBuilder2.mInteractionTracker;
        Objects.requireNonNull(notifCollection);
        Assert.isMainThread();
        notifCollection.mNotifCollectionListeners.add(notificationInteractionTracker);
        ShadeListBuilder.AnonymousClass1 r8 = shadeListBuilder2.mReadyForBuildListener;
        Assert.isMainThread();
        notifCollection.mBuildListener = r8;
        shadeListBuilder2.mChoreographer.addOnEvalListener(new WMShell$7$$ExternalSyntheticLambda1(shadeListBuilder2, 4));
        NotifCollection notifCollection2 = notifPipelineInitializer.mNotifCollection;
        GroupCoalescer groupCoalescer = notifPipelineInitializer.mGroupCoalescer;
        Objects.requireNonNull(notifCollection2);
        Assert.isMainThread();
        if (!notifCollection2.mAttached) {
            notifCollection2.mAttached = true;
            NotifCollection.AnonymousClass1 r6 = notifCollection2.mNotifHandler;
            Objects.requireNonNull(groupCoalescer);
            groupCoalescer.mHandler = r6;
            GroupCoalescer groupCoalescer2 = notifPipelineInitializer.mGroupCoalescer;
            Objects.requireNonNull(groupCoalescer2);
            notificationListener.addNotificationHandler(groupCoalescer2.mListener);
            Log.d("NotifPipeline", "Notif pipeline initialized. rendering=" + notifPipelineInitializer.mNotifPipelineFlags.isNewPipelineEnabled());
            if (this.notifPipelineFlags.isNewPipelineEnabled()) {
                final TargetSdkResolver targetSdkResolver = this.targetSdkResolver;
                Objects.requireNonNull(targetSdkResolver);
                this.notifPipeline.get().addCollectionListener(new NotifCollectionListener() { // from class: com.android.systemui.statusbar.notification.collection.TargetSdkResolver$initialize$1
                    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
                    public final void onEntryBind(NotificationEntry notificationEntry, StatusBarNotification statusBarNotification) {
                        TargetSdkResolver targetSdkResolver2 = TargetSdkResolver.this;
                        Objects.requireNonNull(targetSdkResolver2);
                        int i = 0;
                        try {
                            i = StatusBar.getPackageManagerForUser(targetSdkResolver2.context, statusBarNotification.getUser().getIdentifier()).getApplicationInfo(statusBarNotification.getPackageName(), 0).targetSdkVersion;
                        } catch (PackageManager.NameNotFoundException e) {
                            Log.e("TargetSdkResolver", Intrinsics.stringPlus("Failed looking up ApplicationInfo for ", statusBarNotification.getPackageName()), e);
                        }
                        notificationEntry.targetSdk = i;
                    }
                });
            } else {
                final TargetSdkResolver targetSdkResolver2 = this.targetSdkResolver;
                NotificationEntryManager notificationEntryManager = this.entryManager;
                Objects.requireNonNull(targetSdkResolver2);
                notificationEntryManager.addCollectionListener(new NotifCollectionListener() { // from class: com.android.systemui.statusbar.notification.collection.TargetSdkResolver$initialize$1
                    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
                    public final void onEntryBind(NotificationEntry notificationEntry, StatusBarNotification statusBarNotification) {
                        TargetSdkResolver targetSdkResolver22 = TargetSdkResolver.this;
                        Objects.requireNonNull(targetSdkResolver22);
                        int i = 0;
                        try {
                            i = StatusBar.getPackageManagerForUser(targetSdkResolver22.context, statusBarNotification.getUser().getIdentifier()).getApplicationInfo(statusBarNotification.getPackageName(), 0).targetSdkVersion;
                        } catch (PackageManager.NameNotFoundException e) {
                            Log.e("TargetSdkResolver", Intrinsics.stringPlus("Failed looking up ApplicationInfo for ", statusBarNotification.getPackageName()), e);
                        }
                        notificationEntry.targetSdk = i;
                    }
                });
                RemoteInputUriController remoteInputUriController = this.remoteInputUriController;
                NotificationEntryManager notificationEntryManager2 = this.entryManager;
                Objects.requireNonNull(remoteInputUriController);
                notificationEntryManager2.addCollectionListener(remoteInputUriController.mInlineUriListener);
                NotificationGroupAlertTransferHelper notificationGroupAlertTransferHelper = this.groupAlertTransferHelper;
                NotificationEntryManager notificationEntryManager3 = this.entryManager;
                NotificationGroupManagerLegacy notificationGroupManagerLegacy = this.groupManagerLegacy.get();
                Objects.requireNonNull(notificationGroupAlertTransferHelper);
                if (notificationGroupAlertTransferHelper.mEntryManager == null) {
                    notificationGroupAlertTransferHelper.mEntryManager = notificationEntryManager3;
                    notificationEntryManager3.addNotificationEntryListener(notificationGroupAlertTransferHelper.mNotificationEntryListener);
                    NotificationGroupAlertTransferHelper.AnonymousClass1 r4 = notificationGroupAlertTransferHelper.mOnGroupChangeListener;
                    Objects.requireNonNull(notificationGroupManagerLegacy);
                    NotificationGroupManagerLegacy.GroupEventDispatcher groupEventDispatcher = notificationGroupManagerLegacy.mEventDispatcher;
                    Objects.requireNonNull(groupEventDispatcher);
                    groupEventDispatcher.mGroupChangeListeners.add(r4);
                    final BindEventManagerImpl bindEventManagerImpl = this.bindEventManagerImpl;
                    NotificationEntryManager notificationEntryManager4 = this.entryManager;
                    Objects.requireNonNull(bindEventManagerImpl);
                    notificationEntryManager4.addNotificationEntryListener(new NotificationEntryListener() { // from class: com.android.systemui.statusbar.notification.collection.inflation.BindEventManagerImpl$attachToLegacyPipeline$1
                        @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
                        public final void onEntryInflated(NotificationEntry notificationEntry) {
                            BindEventManagerImpl.this.notifyViewBound(notificationEntry);
                        }

                        @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
                        public final void onEntryReinflated(NotificationEntry notificationEntry) {
                            BindEventManagerImpl.this.notifyViewBound(notificationEntry);
                        }
                    });
                    this.headsUpManager.addListener(this.groupManagerLegacy.get());
                    this.headsUpManager.addListener(this.groupAlertTransferHelper);
                    HeadsUpController headsUpController = this.headsUpController;
                    NotificationEntryManager notificationEntryManager5 = this.entryManager;
                    HeadsUpManager headsUpManager = this.headsUpManager;
                    Objects.requireNonNull(headsUpController);
                    notificationEntryManager5.addCollectionListener(headsUpController.mCollectionListener);
                    headsUpManager.addListener(headsUpController.mOnHeadsUpChangedListener);
                    NotificationGroupManagerLegacy notificationGroupManagerLegacy2 = this.groupManagerLegacy.get();
                    HeadsUpManager headsUpManager2 = this.headsUpManager;
                    Objects.requireNonNull(notificationGroupManagerLegacy2);
                    notificationGroupManagerLegacy2.mHeadsUpManager = headsUpManager2;
                    NotificationGroupAlertTransferHelper notificationGroupAlertTransferHelper2 = this.groupAlertTransferHelper;
                    HeadsUpManager headsUpManager3 = this.headsUpManager;
                    Objects.requireNonNull(notificationGroupAlertTransferHelper2);
                    notificationGroupAlertTransferHelper2.mHeadsUpManager = headsUpManager3;
                    this.debugModeFilterProvider.registerInvalidationListener(new Runnable() { // from class: com.android.systemui.statusbar.notification.init.NotificationsControllerImpl$initialize$1
                        @Override // java.lang.Runnable
                        public final void run() {
                            NotificationsControllerImpl.this.entryManager.updateNotifications("debug mode filter changed");
                        }
                    });
                    NotificationEntryManager notificationEntryManager6 = this.entryManager;
                    NotificationListener notificationListener2 = this.notificationListener;
                    NotificationRankingManager notificationRankingManager = this.legacyRanker;
                    Objects.requireNonNull(notificationEntryManager6);
                    notificationEntryManager6.mRanker = notificationRankingManager;
                    notificationListener2.addNotificationHandler(notificationEntryManager6.mNotifListener);
                    notificationEntryManager6.mDumpManager.registerDumpable(notificationEntryManager6);
                } else {
                    throw new IllegalStateException("Already bound.");
                }
            }
            PeopleSpaceWidgetManager peopleSpaceWidgetManager = this.peopleSpaceWidgetManager;
            NotificationListener notificationListener3 = this.notificationListener;
            Objects.requireNonNull(peopleSpaceWidgetManager);
            notificationListener3.addNotificationHandler(peopleSpaceWidgetManager.mListener);
            return;
        }
        throw new RuntimeException("attach() called twice");
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public final void requestNotificationUpdate(String str) {
        this.entryManager.updateNotifications(str);
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public final void resetUserExpandedStates() {
        for (NotificationEntry notificationEntry : this.commonNotifCollection.get().getAllNotifs()) {
            Objects.requireNonNull(notificationEntry);
            ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
            if (expandableNotificationRow != null) {
                boolean isExpanded = expandableNotificationRow.isExpanded(false);
                expandableNotificationRow.mHasUserChangedExpansion = false;
                expandableNotificationRow.mUserExpanded = false;
                if (isExpanded != expandableNotificationRow.isExpanded(false)) {
                    if (expandableNotificationRow.mIsSummaryWithChildren) {
                        NotificationChildrenContainer notificationChildrenContainer = expandableNotificationRow.mChildrenContainer;
                        Objects.requireNonNull(notificationChildrenContainer);
                        if (notificationChildrenContainer.mIsLowPriority) {
                            boolean z = notificationChildrenContainer.mUserLocked;
                            if (z) {
                                notificationChildrenContainer.setUserLocked(z);
                            }
                            notificationChildrenContainer.updateHeaderVisibility(true);
                        }
                    }
                    expandableNotificationRow.notifyHeightChanged(false);
                }
                expandableNotificationRow.updateShelfIconColor();
            }
        }
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public final void setNotificationSnoozed(StatusBarNotification statusBarNotification, NotificationSwipeActionHelper.SnoozeOption snoozeOption) {
        if (snoozeOption.getSnoozeCriterion() != null) {
            this.notificationListener.snoozeNotification(statusBarNotification.getKey(), snoozeOption.getSnoozeCriterion().getId());
        } else {
            this.notificationListener.snoozeNotification(statusBarNotification.getKey(), snoozeOption.getMinutesToSnoozeFor() * 60 * 1000);
        }
    }
}
