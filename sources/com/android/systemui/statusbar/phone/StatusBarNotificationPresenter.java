package com.android.systemui.statusbar.phone;

import android.app.KeyguardManager;
import android.content.Context;
import android.media.session.MediaController;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.Trace;
import android.service.notification.StatusBarNotification;
import android.service.vr.IVrManager;
import android.service.vr.IVrStateCallbacks;
import android.text.TextUtils;
import android.util.Slog;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.widget.MessagingGroup;
import com.android.internal.widget.MessagingMessage;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.Dependency;
import com.android.systemui.ForegroundServiceNotificationListener;
import com.android.systemui.InitController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.screenshot.ScreenshotController$$ExternalSyntheticLambda3;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.KeyguardIndicationController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.NotificationLifetimeExtender;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.NotificationViewHierarchyManager;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.AboveShelfObserver;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.InflationException;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.collection.render.NotifShadeEventSource;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptSuppressor;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.StatusBarNotificationPresenter;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.tuner.NavBarTuner$$ExternalSyntheticLambda2;
import com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda2;
import com.android.systemui.wmshell.WMShell$8$$ExternalSyntheticLambda0;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class StatusBarNotificationPresenter implements NotificationPresenter, ConfigurationController.ConfigurationListener, NotificationRowBinderImpl.BindRowCallback, CommandQueue.Callbacks {
    public final AboveShelfObserver mAboveShelfObserver;
    public final AccessibilityManager mAccessibilityManager;
    public final CommandQueue mCommandQueue;
    public boolean mDispatchUiModeChangeOnUserSwitched;
    public final DozeScrimController mDozeScrimController;
    public final DynamicPrivacyController mDynamicPrivacyController;
    public final NotificationEntryManager mEntryManager;
    public final NotificationGutsManager mGutsManager;
    public final HeadsUpManagerPhone mHeadsUpManager;
    public final KeyguardIndicationController mKeyguardIndicationController;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final LockscreenGestureLogger mLockscreenGestureLogger;
    public final NotificationLockscreenUserManager mLockscreenUserManager;
    public final NotificationMediaManager mMediaManager;
    public final NotifPipelineFlags mNotifPipelineFlags;
    public final NotifShadeEventSource mNotifShadeEventSource;
    public final NotificationPanelViewController mNotificationPanel;
    public final NotificationShadeWindowController mNotificationShadeWindowController;
    public boolean mReinflateNotificationsOnUserSwitched;
    public final ScrimController mScrimController;
    public final ShadeController mShadeController;
    public final LockscreenShadeTransitionController mShadeTransitionController;
    public final StatusBar mStatusBar;
    public final SysuiStatusBarStateController mStatusBarStateController;
    public final NotificationViewHierarchyManager mViewHierarchyManager;
    public boolean mVrMode;
    public final AnonymousClass1 mVrStateCallbacks;
    public final ActivityStarter mActivityStarter = (ActivityStarter) Dependency.get(ActivityStarter.class);
    public final AnonymousClass2 mCheckSaveListener = new Object() { // from class: com.android.systemui.statusbar.phone.StatusBarNotificationPresenter.2
    };
    public final AnonymousClass3 mOnSettingsClickListener = new AnonymousClass3();
    public final AnonymousClass4 mInterruptSuppressor = new NotificationInterruptSuppressor() { // from class: com.android.systemui.statusbar.phone.StatusBarNotificationPresenter.4
        @Override // com.android.systemui.statusbar.notification.interruption.NotificationInterruptSuppressor
        public final String getName() {
            return "StatusBarNotificationPresenter";
        }

        @Override // com.android.systemui.statusbar.notification.interruption.NotificationInterruptSuppressor
        public final boolean suppressAwakeInterruptions() {
            StatusBarNotificationPresenter statusBarNotificationPresenter = StatusBarNotificationPresenter.this;
            Objects.requireNonNull(statusBarNotificationPresenter);
            return statusBarNotificationPresenter.mVrMode;
        }

        @Override // com.android.systemui.statusbar.notification.interruption.NotificationInterruptSuppressor
        public final boolean suppressInterruptions() {
            StatusBar statusBar = StatusBarNotificationPresenter.this.mStatusBar;
            Objects.requireNonNull(statusBar);
            if ((statusBar.mDisabled1 & 262144) != 0) {
                return true;
            }
            return false;
        }

        @Override // com.android.systemui.statusbar.notification.interruption.NotificationInterruptSuppressor
        public final boolean suppressAwakeHeadsUp(NotificationEntry notificationEntry) {
            boolean z;
            Objects.requireNonNull(notificationEntry);
            StatusBarNotification statusBarNotification = notificationEntry.mSbn;
            StatusBar statusBar = StatusBarNotificationPresenter.this.mStatusBar;
            Objects.requireNonNull(statusBar);
            if (statusBar.mIsOccluded) {
                NotificationLockscreenUserManager notificationLockscreenUserManager = StatusBarNotificationPresenter.this.mLockscreenUserManager;
                if (notificationLockscreenUserManager.isLockscreenPublicMode(notificationLockscreenUserManager.getCurrentUserId()) || StatusBarNotificationPresenter.this.mLockscreenUserManager.isLockscreenPublicMode(statusBarNotification.getUserId())) {
                    z = true;
                } else {
                    z = false;
                }
                boolean needsRedaction = StatusBarNotificationPresenter.this.mLockscreenUserManager.needsRedaction(notificationEntry);
                if (z && needsRedaction) {
                    return true;
                }
            }
            if (!StatusBarNotificationPresenter.this.mCommandQueue.panelsEnabled()) {
                return true;
            }
            if (statusBarNotification.getNotification().fullScreenIntent != null) {
                if (StatusBarNotificationPresenter.this.mKeyguardStateController.isShowing()) {
                    StatusBar statusBar2 = StatusBarNotificationPresenter.this.mStatusBar;
                    Objects.requireNonNull(statusBar2);
                    if (!statusBar2.mIsOccluded) {
                        return true;
                    }
                }
                if (StatusBarNotificationPresenter.this.mAccessibilityManager.isTouchExplorationEnabled()) {
                    return true;
                }
            }
            return false;
        }
    };
    public final IStatusBarService mBarService = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));

    /* renamed from: com.android.systemui.statusbar.phone.StatusBarNotificationPresenter$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass3 implements NotificationGutsManager.OnSettingsClickListener {
        public AnonymousClass3() {
        }
    }

    /* JADX WARN: Type inference failed for: r6v0, types: [com.android.systemui.statusbar.phone.StatusBarNotificationPresenter$2] */
    /* JADX WARN: Type inference failed for: r6v2, types: [com.android.systemui.statusbar.phone.StatusBarNotificationPresenter$4] */
    public StatusBarNotificationPresenter(Context context, NotificationPanelViewController notificationPanelViewController, HeadsUpManagerPhone headsUpManagerPhone, NotificationShadeWindowView notificationShadeWindowView, final NotificationStackScrollLayoutController notificationStackScrollLayoutController, DozeScrimController dozeScrimController, ScrimController scrimController, NotificationShadeWindowController notificationShadeWindowController, DynamicPrivacyController dynamicPrivacyController, KeyguardStateController keyguardStateController, KeyguardIndicationController keyguardIndicationController, StatusBar statusBar, ShadeController shadeController, LockscreenShadeTransitionController lockscreenShadeTransitionController, CommandQueue commandQueue, NotificationViewHierarchyManager notificationViewHierarchyManager, NotificationLockscreenUserManager notificationLockscreenUserManager, SysuiStatusBarStateController sysuiStatusBarStateController, NotifShadeEventSource notifShadeEventSource, NotificationEntryManager notificationEntryManager, NotificationMediaManager notificationMediaManager, NotificationGutsManager notificationGutsManager, KeyguardUpdateMonitor keyguardUpdateMonitor, LockscreenGestureLogger lockscreenGestureLogger, InitController initController, final NotificationInterruptStateProvider notificationInterruptStateProvider, final NotificationRemoteInputManager notificationRemoteInputManager, ConfigurationController configurationController, NotifPipelineFlags notifPipelineFlags) {
        IVrStateCallbacks.Stub stub = new IVrStateCallbacks.Stub() { // from class: com.android.systemui.statusbar.phone.StatusBarNotificationPresenter.1
            public final void onVrStateChanged(boolean z) {
                StatusBarNotificationPresenter.this.mVrMode = z;
            }
        };
        this.mVrStateCallbacks = stub;
        this.mKeyguardStateController = keyguardStateController;
        this.mNotificationPanel = notificationPanelViewController;
        this.mHeadsUpManager = headsUpManagerPhone;
        this.mDynamicPrivacyController = dynamicPrivacyController;
        this.mKeyguardIndicationController = keyguardIndicationController;
        this.mStatusBar = statusBar;
        this.mShadeController = shadeController;
        this.mShadeTransitionController = lockscreenShadeTransitionController;
        this.mCommandQueue = commandQueue;
        this.mViewHierarchyManager = notificationViewHierarchyManager;
        this.mLockscreenUserManager = notificationLockscreenUserManager;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mNotifShadeEventSource = notifShadeEventSource;
        this.mEntryManager = notificationEntryManager;
        this.mMediaManager = notificationMediaManager;
        this.mGutsManager = notificationGutsManager;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mLockscreenGestureLogger = lockscreenGestureLogger;
        Objects.requireNonNull(notificationStackScrollLayoutController);
        AboveShelfObserver aboveShelfObserver = new AboveShelfObserver(notificationStackScrollLayoutController.mView);
        this.mAboveShelfObserver = aboveShelfObserver;
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mNotifPipelineFlags = notifPipelineFlags;
        aboveShelfObserver.mListener = (AboveShelfObserver.HasViewAboveShelfChangedListener) notificationShadeWindowView.findViewById(2131428509);
        this.mAccessibilityManager = (AccessibilityManager) context.getSystemService(AccessibilityManager.class);
        this.mDozeScrimController = dozeScrimController;
        this.mScrimController = scrimController;
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(KeyguardManager.class);
        IVrManager asInterface = IVrManager.Stub.asInterface(ServiceManager.getService("vrmanager"));
        if (asInterface != null) {
            try {
                asInterface.registerListener(stub);
            } catch (RemoteException e) {
                Slog.e("StatusBarNotificationPresenter", "Failed to register VR mode state listener: " + e);
            }
        }
        NotificationPanelViewController notificationPanelViewController2 = this.mNotificationPanel;
        Objects.requireNonNull(notificationPanelViewController2);
        NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = notificationPanelViewController2.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController2);
        NotificationStackScrollLayoutController.AnonymousClass14 r6 = new NotificationStackScrollLayoutController.AnonymousClass14();
        Objects.requireNonNull(notificationRemoteInputManager);
        notificationRemoteInputManager.mCallback = (NotificationRemoteInputManager.Callback) Dependency.get(NotificationRemoteInputManager.Callback.class);
        RemoteInputController remoteInputController = new RemoteInputController(r6, notificationRemoteInputManager.mRemoteInputUriController);
        notificationRemoteInputManager.mRemoteInputController = remoteInputController;
        NotificationRemoteInputManager.RemoteInputListener remoteInputListener = notificationRemoteInputManager.mRemoteInputListener;
        if (remoteInputListener != null) {
            remoteInputListener.setRemoteInputController(remoteInputController);
        }
        Iterator it = notificationRemoteInputManager.mControllerCallbacks.iterator();
        while (it.hasNext()) {
            RemoteInputController.Callback callback = (RemoteInputController.Callback) it.next();
            RemoteInputController remoteInputController2 = notificationRemoteInputManager.mRemoteInputController;
            Objects.requireNonNull(remoteInputController2);
            Objects.requireNonNull(callback);
            remoteInputController2.mCallbacks.add(callback);
        }
        notificationRemoteInputManager.mControllerCallbacks.clear();
        RemoteInputController remoteInputController3 = notificationRemoteInputManager.mRemoteInputController;
        RemoteInputController.Callback callback2 = new RemoteInputController.Callback() { // from class: com.android.systemui.statusbar.NotificationRemoteInputManager.3
            @Override // com.android.systemui.statusbar.RemoteInputController.Callback
            public final void onRemoteInputSent(NotificationEntry notificationEntry) {
                boolean z;
                RemoteInputListener remoteInputListener2 = notificationRemoteInputManager.mRemoteInputListener;
                if (remoteInputListener2 != null) {
                    remoteInputListener2.onRemoteInputSent(notificationEntry);
                }
                try {
                    IStatusBarService iStatusBarService = notificationRemoteInputManager.mBarService;
                    Objects.requireNonNull(notificationEntry);
                    iStatusBarService.onNotificationDirectReplied(notificationEntry.mSbn.getKey());
                    NotificationEntry.EditedSuggestionInfo editedSuggestionInfo = notificationEntry.editedSuggestionInfo;
                    if (editedSuggestionInfo != null) {
                        if (!TextUtils.equals(notificationEntry.remoteInputText, editedSuggestionInfo.originalText)) {
                            z = true;
                        } else {
                            z = false;
                        }
                        IStatusBarService iStatusBarService2 = notificationRemoteInputManager.mBarService;
                        String key = notificationEntry.mSbn.getKey();
                        NotificationEntry.EditedSuggestionInfo editedSuggestionInfo2 = notificationEntry.editedSuggestionInfo;
                        iStatusBarService2.onNotificationSmartReplySent(key, editedSuggestionInfo2.index, editedSuggestionInfo2.originalText, NotificationLogger.getNotificationLocation(notificationEntry).toMetricsEventEnum(), z);
                    }
                } catch (RemoteException unused) {
                }
            }
        };
        Objects.requireNonNull(remoteInputController3);
        remoteInputController3.mCallbacks.add(callback2);
        if (!notificationRemoteInputManager.mNotifPipelineFlags.isNewPipelineEnabled()) {
            SmartReplyController smartReplyController = notificationRemoteInputManager.mSmartReplyController;
            SmartReplyController.Callback notificationRemoteInputManager$$ExternalSyntheticLambda1 = new SmartReplyController.Callback() { // from class: com.android.systemui.statusbar.NotificationRemoteInputManager$$ExternalSyntheticLambda1
                @Override // com.android.systemui.statusbar.SmartReplyController.Callback
                public final void onSmartReplySent(NotificationEntry notificationEntry, CharSequence charSequence) {
                    NotificationRemoteInputManager notificationRemoteInputManager2 = NotificationRemoteInputManager.this;
                    Objects.requireNonNull(notificationRemoteInputManager2);
                    RemoteInputNotificationRebuilder remoteInputNotificationRebuilder = notificationRemoteInputManager2.mRebuilder;
                    Objects.requireNonNull(remoteInputNotificationRebuilder);
                    StatusBarNotification rebuildWithRemoteInputInserted = remoteInputNotificationRebuilder.rebuildWithRemoteInputInserted(notificationEntry, charSequence, true, null, null);
                    NotificationEntryManager notificationEntryManager2 = notificationRemoteInputManager2.mEntryManager;
                    Objects.requireNonNull(notificationEntryManager2);
                    try {
                        notificationEntryManager2.updateNotificationInternal(rebuildWithRemoteInputInserted, null);
                    } catch (InflationException e2) {
                        notificationEntryManager2.handleInflationException(rebuildWithRemoteInputInserted, e2);
                    }
                }
            };
            Objects.requireNonNull(smartReplyController);
            smartReplyController.mCallback = notificationRemoteInputManager$$ExternalSyntheticLambda1;
        }
        Runnable statusBarNotificationPresenter$$ExternalSyntheticLambda1 = new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarNotificationPresenter$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                StatusBarNotificationPresenter statusBarNotificationPresenter = StatusBarNotificationPresenter.this;
                NotificationStackScrollLayoutController notificationStackScrollLayoutController3 = notificationStackScrollLayoutController;
                NotificationRemoteInputManager notificationRemoteInputManager2 = notificationRemoteInputManager;
                NotificationInterruptStateProvider notificationInterruptStateProvider2 = notificationInterruptStateProvider;
                Objects.requireNonNull(statusBarNotificationPresenter);
                statusBarNotificationPresenter.mKeyguardIndicationController.init();
                NotificationViewHierarchyManager notificationViewHierarchyManager2 = statusBarNotificationPresenter.mViewHierarchyManager;
                Objects.requireNonNull(notificationStackScrollLayoutController3);
                NotificationStackScrollLayoutController.NotifStackControllerImpl notifStackControllerImpl = notificationStackScrollLayoutController3.mNotifStackController;
                NotificationStackScrollLayoutController.NotificationListContainerImpl notificationListContainerImpl = notificationStackScrollLayoutController3.mNotificationListContainer;
                Objects.requireNonNull(notificationViewHierarchyManager2);
                notificationViewHierarchyManager2.mPresenter = statusBarNotificationPresenter;
                notificationViewHierarchyManager2.mStackController = notifStackControllerImpl;
                notificationViewHierarchyManager2.mListContainer = notificationListContainerImpl;
                if (!notificationViewHierarchyManager2.mNotifPipelineFlags.isNewPipelineEnabled()) {
                    DynamicPrivacyController dynamicPrivacyController2 = notificationViewHierarchyManager2.mDynamicPrivacyController;
                    Objects.requireNonNull(dynamicPrivacyController2);
                    dynamicPrivacyController2.mListeners.add(notificationViewHierarchyManager2);
                }
                statusBarNotificationPresenter.mNotifShadeEventSource.setShadeEmptiedCallback(new WMShell$7$$ExternalSyntheticLambda2(statusBarNotificationPresenter, 7));
                statusBarNotificationPresenter.mNotifShadeEventSource.setNotifRemovedByUserCallback(new WMShell$8$$ExternalSyntheticLambda0(statusBarNotificationPresenter, 4));
                if (!statusBarNotificationPresenter.mNotifPipelineFlags.isNewPipelineEnabled()) {
                    NotificationEntryManager notificationEntryManager2 = statusBarNotificationPresenter.mEntryManager;
                    Objects.requireNonNull(notificationEntryManager2);
                    notificationEntryManager2.mPresenter = statusBarNotificationPresenter;
                    NotificationEntryManager notificationEntryManager3 = statusBarNotificationPresenter.mEntryManager;
                    HeadsUpManagerPhone headsUpManagerPhone2 = statusBarNotificationPresenter.mHeadsUpManager;
                    Objects.requireNonNull(notificationEntryManager3);
                    notificationEntryManager3.mNotificationLifetimeExtenders.add(headsUpManagerPhone2);
                    headsUpManagerPhone2.setCallback(new ScreenshotController$$ExternalSyntheticLambda3(notificationEntryManager3));
                    NotificationEntryManager notificationEntryManager4 = statusBarNotificationPresenter.mEntryManager;
                    NotificationGutsManager notificationGutsManager2 = statusBarNotificationPresenter.mGutsManager;
                    Objects.requireNonNull(notificationEntryManager4);
                    notificationEntryManager4.mNotificationLifetimeExtenders.add(notificationGutsManager2);
                    notificationGutsManager2.setCallback(new ScreenshotController$$ExternalSyntheticLambda3(notificationEntryManager4));
                    NotificationEntryManager notificationEntryManager5 = statusBarNotificationPresenter.mEntryManager;
                    Objects.requireNonNull(notificationRemoteInputManager2);
                    ArrayList<NotificationLifetimeExtender> arrayList = ((NotificationRemoteInputManager.LegacyRemoteInputLifetimeExtender) notificationRemoteInputManager2.mRemoteInputListener).mLifetimeExtenders;
                    Objects.requireNonNull(notificationEntryManager5);
                    Iterator<NotificationLifetimeExtender> it2 = arrayList.iterator();
                    while (it2.hasNext()) {
                        NotificationLifetimeExtender next = it2.next();
                        notificationEntryManager5.mNotificationLifetimeExtenders.add(next);
                        next.setCallback(new ScreenshotController$$ExternalSyntheticLambda3(notificationEntryManager5));
                    }
                }
                notificationInterruptStateProvider2.addSuppressor(statusBarNotificationPresenter.mInterruptSuppressor);
                statusBarNotificationPresenter.mLockscreenUserManager.setUpWithPresenter(statusBarNotificationPresenter);
                NotificationMediaManager notificationMediaManager2 = statusBarNotificationPresenter.mMediaManager;
                Objects.requireNonNull(notificationMediaManager2);
                notificationMediaManager2.mPresenter = statusBarNotificationPresenter;
                NotificationGutsManager notificationGutsManager3 = statusBarNotificationPresenter.mGutsManager;
                NotificationStackScrollLayoutController.NotificationListContainerImpl notificationListContainerImpl2 = notificationStackScrollLayoutController3.mNotificationListContainer;
                StatusBarNotificationPresenter.AnonymousClass3 r2 = statusBarNotificationPresenter.mOnSettingsClickListener;
                Objects.requireNonNull(notificationGutsManager3);
                notificationGutsManager3.mPresenter = statusBarNotificationPresenter;
                notificationGutsManager3.mListContainer = notificationListContainerImpl2;
                notificationGutsManager3.mOnSettingsClickListener = r2;
                Dependency.get(ForegroundServiceNotificationListener.class);
                statusBarNotificationPresenter.onUserSwitched(statusBarNotificationPresenter.mLockscreenUserManager.getCurrentUserId());
            }
        };
        Objects.requireNonNull(initController);
        if (!initController.mTasksExecuted) {
            initController.mTasks.add(statusBarNotificationPresenter$$ExternalSyntheticLambda1);
            configurationController.addCallback(this);
            return;
        }
        throw new IllegalStateException("post init tasks have already been executed!");
    }

    public final boolean isCollapsing() {
        if (this.mNotificationPanel.isCollapsing() || this.mNotificationShadeWindowController.isLaunchingActivity()) {
            return true;
        }
        return false;
    }

    public final boolean isPresenterFullyCollapsed() {
        return this.mNotificationPanel.isFullyCollapsed();
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView.OnActivatedListener
    public final void onActivationReset(ActivatableNotificationView activatableNotificationView) {
        NotificationPanelViewController notificationPanelViewController = this.mNotificationPanel;
        Objects.requireNonNull(notificationPanelViewController);
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = notificationPanelViewController.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController);
        NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
        Objects.requireNonNull(notificationStackScrollLayout);
        AmbientState ambientState = notificationStackScrollLayout.mAmbientState;
        Objects.requireNonNull(ambientState);
        if (activatableNotificationView == ambientState.mActivatedChild) {
            NotificationPanelViewController notificationPanelViewController2 = this.mNotificationPanel;
            Objects.requireNonNull(notificationPanelViewController2);
            NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = notificationPanelViewController2.mNotificationStackScrollLayoutController;
            Objects.requireNonNull(notificationStackScrollLayoutController2);
            NotificationStackScrollLayout notificationStackScrollLayout2 = notificationStackScrollLayoutController2.mView;
            Objects.requireNonNull(notificationStackScrollLayout2);
            AmbientState ambientState2 = notificationStackScrollLayout2.mAmbientState;
            Objects.requireNonNull(ambientState2);
            ambientState2.mActivatedChild = null;
            if (notificationStackScrollLayout2.mAnimationsEnabled) {
                notificationStackScrollLayout2.mActivateNeedsAnimation = true;
                notificationStackScrollLayout2.mNeedsAnimation = true;
            }
            notificationStackScrollLayout2.requestChildrenUpdate();
            this.mKeyguardIndicationController.hideTransientIndication();
        }
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onDensityOrFontScaleChanged() {
        if (!this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            MessagingMessage.dropCache();
            MessagingGroup.dropCache();
            KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateMonitor;
            Objects.requireNonNull(keyguardUpdateMonitor);
            if (!keyguardUpdateMonitor.mSwitchingUser) {
                updateNotificationsOnDensityOrFontScaleChanged();
            } else {
                this.mReinflateNotificationsOnUserSwitched = true;
            }
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.OnExpandClickListener
    public final void onExpandClicked(NotificationEntry notificationEntry, View view, boolean z) {
        HeadsUpManagerPhone headsUpManagerPhone = this.mHeadsUpManager;
        Objects.requireNonNull(headsUpManagerPhone);
        Objects.requireNonNull(notificationEntry);
        HeadsUpManager.HeadsUpEntry headsUpEntry = headsUpManagerPhone.getHeadsUpEntry(notificationEntry.mKey);
        if (headsUpEntry != null && notificationEntry.isRowPinned()) {
            headsUpEntry.setExpanded(z);
        }
        this.mStatusBar.wakeUpIfDozing(SystemClock.uptimeMillis(), view, "NOTIFICATION_CLICK");
        if (!z) {
            return;
        }
        if (this.mStatusBarStateController.getState() == 1) {
            LockscreenShadeTransitionController lockscreenShadeTransitionController = this.mShadeTransitionController;
            ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
            Objects.requireNonNull(lockscreenShadeTransitionController);
            lockscreenShadeTransitionController.goToLockedShade(expandableNotificationRow, true);
        } else if (notificationEntry.mSensitive && this.mDynamicPrivacyController.isInLockedDownShade()) {
            this.mStatusBarStateController.setLeaveOpenOnKeyguardHide(true);
            this.mActivityStarter.dismissKeyguardThenExecute(NavBarTuner$$ExternalSyntheticLambda2.INSTANCE$1, null, false);
        }
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onUiModeChanged() {
        if (!this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateMonitor;
            Objects.requireNonNull(keyguardUpdateMonitor);
            if (!keyguardUpdateMonitor.mSwitchingUser) {
                updateNotificationsOnUiModeChanged();
            } else {
                this.mDispatchUiModeChangeOnUserSwitched = true;
            }
        }
    }

    public final void onUserSwitched(int i) {
        HeadsUpManagerPhone headsUpManagerPhone = this.mHeadsUpManager;
        Objects.requireNonNull(headsUpManagerPhone);
        headsUpManagerPhone.mUser = i;
        this.mCommandQueue.animateCollapsePanels();
        if (!this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            if (this.mReinflateNotificationsOnUserSwitched) {
                updateNotificationsOnDensityOrFontScaleChanged();
                this.mReinflateNotificationsOnUserSwitched = false;
            }
            if (this.mDispatchUiModeChangeOnUserSwitched) {
                updateNotificationsOnUiModeChanged();
                this.mDispatchUiModeChangeOnUserSwitched = false;
            }
            updateNotificationViews("user switched");
        }
        NotificationMediaManager notificationMediaManager = this.mMediaManager;
        Objects.requireNonNull(notificationMediaManager);
        notificationMediaManager.mMediaNotificationKey = null;
        Objects.requireNonNull(notificationMediaManager.mMediaArtworkProcessor);
        notificationMediaManager.mMediaMetadata = null;
        MediaController mediaController = notificationMediaManager.mMediaController;
        if (mediaController != null) {
            mediaController.unregisterCallback(notificationMediaManager.mMediaListener);
        }
        notificationMediaManager.mMediaController = null;
        this.mStatusBar.setLockscreenUser(i);
        updateMediaMetaData(true, false);
    }

    public final void updateMediaMetaData(boolean z, boolean z2) {
        this.mMediaManager.updateMediaMetaData(z, z2);
    }

    public final void updateNotificationViews(String str) {
        if (!this.mNotifPipelineFlags.checkLegacyPipelineEnabled() || this.mScrimController == null) {
            return;
        }
        if (isCollapsing()) {
            this.mShadeController.addPostCollapseAction(new NotificationIconAreaController$$ExternalSyntheticLambda0(this, str, 1));
            return;
        }
        this.mViewHierarchyManager.updateNotificationViews();
        NotificationPanelViewController notificationPanelViewController = this.mNotificationPanel;
        Objects.requireNonNull(notificationPanelViewController);
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = notificationPanelViewController.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController);
        if (!notificationStackScrollLayoutController.mNotifPipelineFlags.isNewPipelineEnabled()) {
            Trace.beginSection("NSSLC.updateSectionBoundaries");
            NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
            Objects.requireNonNull(notificationStackScrollLayout);
            notificationStackScrollLayout.mSectionsManager.updateSectionBoundaries(str);
            Trace.endSection();
        }
        notificationPanelViewController.mNotificationStackScrollLayoutController.updateFooter();
        NotificationIconAreaController notificationIconAreaController = notificationPanelViewController.mNotificationIconAreaController;
        ArrayList arrayList = new ArrayList(notificationPanelViewController.mNotificationStackScrollLayoutController.getChildCount());
        for (int i = 0; i < notificationPanelViewController.mNotificationStackScrollLayoutController.getChildCount(); i++) {
            NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = notificationPanelViewController.mNotificationStackScrollLayoutController;
            Objects.requireNonNull(notificationStackScrollLayoutController2);
            ExpandableView expandableView = (ExpandableView) notificationStackScrollLayoutController2.mView.getChildAt(i);
            if (expandableView instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) expandableView;
                Objects.requireNonNull(expandableNotificationRow);
                arrayList.add(expandableNotificationRow.mEntry);
            }
        }
        notificationIconAreaController.updateNotificationIcons(arrayList);
    }

    public final void updateNotificationsOnDensityOrFontScaleChanged() {
        NotificationGuts notificationGuts;
        if (!this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            ArrayList activeNotificationsForCurrentUser = this.mEntryManager.getActiveNotificationsForCurrentUser();
            for (int i = 0; i < activeNotificationsForCurrentUser.size(); i++) {
                NotificationEntry notificationEntry = (NotificationEntry) activeNotificationsForCurrentUser.get(i);
                Objects.requireNonNull(notificationEntry);
                ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
                if (expandableNotificationRow != null) {
                    expandableNotificationRow.onDensityOrFontScaleChanged();
                }
                if (notificationEntry.areGutsExposed()) {
                    NotificationGutsManager notificationGutsManager = this.mGutsManager;
                    Objects.requireNonNull(notificationGutsManager);
                    ExpandableNotificationRow expandableNotificationRow2 = notificationEntry.row;
                    if (expandableNotificationRow2 != null) {
                        notificationGuts = expandableNotificationRow2.mGuts;
                    } else {
                        notificationGuts = null;
                    }
                    notificationGutsManager.mNotificationGutsExposed = notificationGuts;
                    Objects.requireNonNull(expandableNotificationRow2);
                    if (expandableNotificationRow2.mGuts == null) {
                        expandableNotificationRow2.mGutsStub.inflate();
                    }
                    notificationGutsManager.bindGuts(expandableNotificationRow2, notificationGutsManager.mGutsMenuItem);
                }
            }
        }
    }

    public final void updateNotificationsOnUiModeChanged() {
        if (!this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            ArrayList activeNotificationsForCurrentUser = this.mEntryManager.getActiveNotificationsForCurrentUser();
            for (int i = 0; i < activeNotificationsForCurrentUser.size(); i++) {
                NotificationEntry notificationEntry = (NotificationEntry) activeNotificationsForCurrentUser.get(i);
                Objects.requireNonNull(notificationEntry);
                ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
                if (expandableNotificationRow != null) {
                    expandableNotificationRow.onUiModeChanged();
                }
            }
        }
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onThemeChanged() {
        onDensityOrFontScaleChanged();
    }
}
