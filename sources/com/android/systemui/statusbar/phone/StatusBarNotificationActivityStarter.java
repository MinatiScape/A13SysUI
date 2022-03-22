package com.android.systemui.statusbar.phone;

import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.os.UserHandle;
import android.service.dreams.IDreamManager;
import android.util.EventLog;
import android.view.RemoteAnimationAdapter;
import android.view.View;
import androidx.coordinatorlayout.widget.DirectedAcyclicGraph;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.ActivityIntentHelper;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.GhostedViewLaunchAnimatorController;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationClickNotifier;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationLaunchAnimatorController;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.OnUserInteractionCallback;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.wmshell.BubblesManager;
import com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda2;
import dagger.Lazy;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import kotlin.jvm.functions.Function1;
/* loaded from: classes.dex */
public final class StatusBarNotificationActivityStarter implements NotificationActivityStarter {
    public final ActivityIntentHelper mActivityIntentHelper;
    public final ActivityLaunchAnimator mActivityLaunchAnimator;
    public final ActivityStarter mActivityStarter;
    public final Lazy<AssistManager> mAssistManagerLazy;
    public final Optional<BubblesManager> mBubblesManagerOptional;
    public final NotificationClickNotifier mClickNotifier;
    public final Context mContext;
    public final IDreamManager mDreamManager;
    public final GroupMembershipManager mGroupMembershipManager;
    public final HeadsUpManagerPhone mHeadsUpManager;
    public boolean mIsCollapsingToShowActivityOverLockscreen;
    public final KeyguardManager mKeyguardManager;
    public final KeyguardStateController mKeyguardStateController;
    public final LockPatternUtils mLockPatternUtils;
    public final NotificationLockscreenUserManager mLockscreenUserManager;
    public final StatusBarNotificationActivityStarterLogger mLogger;
    public final Handler mMainThreadHandler;
    public final MetricsLogger mMetricsLogger;
    public final DirectedAcyclicGraph mNotificationAnimationProvider;
    public final NotificationInterruptStateProvider mNotificationInterruptStateProvider;
    public final NotificationPanelViewController mNotificationPanel;
    public final OnUserInteractionCallback mOnUserInteractionCallback;
    public final NotificationPresenter mPresenter;
    public final NotificationRemoteInputManager mRemoteInputManager;
    public final ShadeController mShadeController;
    public final StatusBar mStatusBar;
    public final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    public final StatusBarRemoteInputCallback mStatusBarRemoteInputCallback;
    public final StatusBarStateController mStatusBarStateController;
    public final Executor mUiBgExecutor;
    public final NotificationVisibilityProvider mVisibilityProvider;

    /* renamed from: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter$4  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass4 implements ActivityStarter.OnDismissAction {
        public final /* synthetic */ boolean val$animate;
        public final /* synthetic */ int val$appUid;
        public final /* synthetic */ Intent val$intent;
        public final /* synthetic */ ExpandableNotificationRow val$row;

        public AnonymousClass4(ExpandableNotificationRow expandableNotificationRow, boolean z, Intent intent, int i) {
            this.val$row = expandableNotificationRow;
            this.val$animate = z;
            this.val$intent = intent;
            this.val$appUid = i;
        }

        @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
        public final boolean onDismiss() {
            final ExpandableNotificationRow expandableNotificationRow = this.val$row;
            final boolean z = this.val$animate;
            final Intent intent = this.val$intent;
            final int i = this.val$appUid;
            AsyncTask.execute(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter$4$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    final StatusBarNotificationActivityStarter.AnonymousClass4 r0 = StatusBarNotificationActivityStarter.AnonymousClass4.this;
                    ExpandableNotificationRow expandableNotificationRow2 = expandableNotificationRow;
                    boolean z2 = z;
                    final Intent intent2 = intent;
                    final int i2 = i;
                    Objects.requireNonNull(r0);
                    NotificationLaunchAnimatorController animatorController = StatusBarNotificationActivityStarter.this.mNotificationAnimationProvider.getAnimatorController(expandableNotificationRow2);
                    StatusBarNotificationActivityStarter statusBarNotificationActivityStarter = StatusBarNotificationActivityStarter.this;
                    StatusBarLaunchAnimatorController statusBarLaunchAnimatorController = new StatusBarLaunchAnimatorController(animatorController, statusBarNotificationActivityStarter.mStatusBar, true);
                    ActivityLaunchAnimator activityLaunchAnimator = statusBarNotificationActivityStarter.mActivityLaunchAnimator;
                    String str = intent2.getPackage();
                    Function1<? super RemoteAnimationAdapter, Integer> statusBarNotificationActivityStarter$4$$ExternalSyntheticLambda1 = new Function1() { // from class: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter$4$$ExternalSyntheticLambda1
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            StatusBarNotificationActivityStarter.AnonymousClass4 r02 = StatusBarNotificationActivityStarter.AnonymousClass4.this;
                            Intent intent3 = intent2;
                            int i3 = i2;
                            Objects.requireNonNull(r02);
                            TaskStackBuilder addNextIntentWithParentStack = TaskStackBuilder.create(StatusBarNotificationActivityStarter.this.mContext).addNextIntentWithParentStack(intent3);
                            StatusBar statusBar = StatusBarNotificationActivityStarter.this.mStatusBar;
                            Objects.requireNonNull(statusBar);
                            return Integer.valueOf(addNextIntentWithParentStack.startActivities(StatusBar.getActivityOptions(statusBar.mDisplayId, (RemoteAnimationAdapter) obj), new UserHandle(UserHandle.getUserId(i3))));
                        }
                    };
                    Objects.requireNonNull(activityLaunchAnimator);
                    activityLaunchAnimator.startIntentWithAnimation(statusBarLaunchAnimatorController, z2, str, false, statusBarNotificationActivityStarter$4$$ExternalSyntheticLambda1);
                }
            });
            return true;
        }

        @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
        public final boolean willRunAnimationOnKeyguard() {
            return this.val$animate;
        }
    }

    /* renamed from: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter$5  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass5 implements ActivityStarter.OnDismissAction {
        public final /* synthetic */ boolean val$animate;
        public final /* synthetic */ boolean val$showHistory;
        public final /* synthetic */ View val$view;

        public AnonymousClass5(boolean z, View view, boolean z2) {
            this.val$showHistory = z;
            this.val$view = view;
            this.val$animate = z2;
        }

        @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
        public final boolean onDismiss() {
            final boolean z = this.val$showHistory;
            final View view = this.val$view;
            final boolean z2 = this.val$animate;
            AsyncTask.execute(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter$5$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Intent intent;
                    StatusBarLaunchAnimatorController statusBarLaunchAnimatorController;
                    final StatusBarNotificationActivityStarter.AnonymousClass5 r0 = StatusBarNotificationActivityStarter.AnonymousClass5.this;
                    boolean z3 = z;
                    View view2 = view;
                    boolean z4 = z2;
                    Objects.requireNonNull(r0);
                    if (z3) {
                        intent = new Intent("android.settings.NOTIFICATION_HISTORY");
                    } else {
                        intent = new Intent("android.settings.NOTIFICATION_SETTINGS");
                    }
                    final TaskStackBuilder addNextIntent = TaskStackBuilder.create(StatusBarNotificationActivityStarter.this.mContext).addNextIntent(new Intent("android.settings.NOTIFICATION_SETTINGS"));
                    if (z3) {
                        addNextIntent.addNextIntent(intent);
                    }
                    GhostedViewLaunchAnimatorController fromView = ActivityLaunchAnimator.Controller.fromView(view2, 30);
                    if (fromView == null) {
                        statusBarLaunchAnimatorController = null;
                    } else {
                        statusBarLaunchAnimatorController = new StatusBarLaunchAnimatorController(fromView, StatusBarNotificationActivityStarter.this.mStatusBar, true);
                    }
                    ActivityLaunchAnimator activityLaunchAnimator = StatusBarNotificationActivityStarter.this.mActivityLaunchAnimator;
                    String str = intent.getPackage();
                    Function1<? super RemoteAnimationAdapter, Integer> statusBarNotificationActivityStarter$5$$ExternalSyntheticLambda1 = new Function1() { // from class: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter$5$$ExternalSyntheticLambda1
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            StatusBarNotificationActivityStarter.AnonymousClass5 r02 = StatusBarNotificationActivityStarter.AnonymousClass5.this;
                            TaskStackBuilder taskStackBuilder = addNextIntent;
                            Objects.requireNonNull(r02);
                            StatusBar statusBar = StatusBarNotificationActivityStarter.this.mStatusBar;
                            Objects.requireNonNull(statusBar);
                            return Integer.valueOf(taskStackBuilder.startActivities(StatusBar.getActivityOptions(statusBar.mDisplayId, (RemoteAnimationAdapter) obj), UserHandle.CURRENT));
                        }
                    };
                    Objects.requireNonNull(activityLaunchAnimator);
                    activityLaunchAnimator.startIntentWithAnimation(statusBarLaunchAnimatorController, z4, str, false, statusBarNotificationActivityStarter$5$$ExternalSyntheticLambda1);
                }
            });
            return true;
        }

        @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
        public final boolean willRunAnimationOnKeyguard() {
            return this.val$animate;
        }
    }

    /* loaded from: classes.dex */
    public static class Builder {
        public final ActivityIntentHelper mActivityIntentHelper;
        public final ActivityStarter mActivityStarter;
        public final Lazy<AssistManager> mAssistManagerLazy;
        public final Optional<BubblesManager> mBubblesManagerOptional;
        public final NotificationClickNotifier mClickNotifier;
        public final CommandQueue mCommandQueue;
        public final Context mContext;
        public final IDreamManager mDreamManager;
        public final NotificationEntryManager mEntryManager;
        public final GroupMembershipManager mGroupMembershipManager;
        public final HeadsUpManagerPhone mHeadsUpManager;
        public final KeyguardManager mKeyguardManager;
        public final KeyguardStateController mKeyguardStateController;
        public final LockPatternUtils mLockPatternUtils;
        public final NotificationLockscreenUserManager mLockscreenUserManager;
        public final StatusBarNotificationActivityStarterLogger mLogger;
        public final Handler mMainThreadHandler;
        public final MetricsLogger mMetricsLogger;
        public final NotifPipeline mNotifPipeline;
        public final NotifPipelineFlags mNotifPipelineFlags;
        public final NotificationInterruptStateProvider mNotificationInterruptStateProvider;
        public final OnUserInteractionCallback mOnUserInteractionCallback;
        public final StatusBarRemoteInputCallback mRemoteInputCallback;
        public final NotificationRemoteInputManager mRemoteInputManager;
        public final ShadeController mShadeController;
        public final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
        public final StatusBarStateController mStatusBarStateController;
        public final Executor mUiBgExecutor;
        public final NotificationVisibilityProvider mVisibilityProvider;

        public Builder(Context context, CommandQueue commandQueue, Handler handler, Executor executor, NotificationEntryManager notificationEntryManager, NotifPipeline notifPipeline, NotificationVisibilityProvider notificationVisibilityProvider, HeadsUpManagerPhone headsUpManagerPhone, ActivityStarter activityStarter, NotificationClickNotifier notificationClickNotifier, StatusBarStateController statusBarStateController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, KeyguardManager keyguardManager, IDreamManager iDreamManager, Optional<BubblesManager> optional, Lazy<AssistManager> lazy, NotificationRemoteInputManager notificationRemoteInputManager, GroupMembershipManager groupMembershipManager, NotificationLockscreenUserManager notificationLockscreenUserManager, ShadeController shadeController, KeyguardStateController keyguardStateController, NotificationInterruptStateProvider notificationInterruptStateProvider, LockPatternUtils lockPatternUtils, StatusBarRemoteInputCallback statusBarRemoteInputCallback, ActivityIntentHelper activityIntentHelper, NotifPipelineFlags notifPipelineFlags, MetricsLogger metricsLogger, StatusBarNotificationActivityStarterLogger statusBarNotificationActivityStarterLogger, OnUserInteractionCallback onUserInteractionCallback) {
            this.mContext = context;
            this.mCommandQueue = commandQueue;
            this.mMainThreadHandler = handler;
            this.mUiBgExecutor = executor;
            this.mEntryManager = notificationEntryManager;
            this.mNotifPipeline = notifPipeline;
            this.mVisibilityProvider = notificationVisibilityProvider;
            this.mHeadsUpManager = headsUpManagerPhone;
            this.mActivityStarter = activityStarter;
            this.mClickNotifier = notificationClickNotifier;
            this.mStatusBarStateController = statusBarStateController;
            this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
            this.mKeyguardManager = keyguardManager;
            this.mDreamManager = iDreamManager;
            this.mBubblesManagerOptional = optional;
            this.mAssistManagerLazy = lazy;
            this.mRemoteInputManager = notificationRemoteInputManager;
            this.mGroupMembershipManager = groupMembershipManager;
            this.mLockscreenUserManager = notificationLockscreenUserManager;
            this.mShadeController = shadeController;
            this.mKeyguardStateController = keyguardStateController;
            this.mNotificationInterruptStateProvider = notificationInterruptStateProvider;
            this.mLockPatternUtils = lockPatternUtils;
            this.mRemoteInputCallback = statusBarRemoteInputCallback;
            this.mActivityIntentHelper = activityIntentHelper;
            this.mNotifPipelineFlags = notifPipelineFlags;
            this.mMetricsLogger = metricsLogger;
            this.mLogger = statusBarNotificationActivityStarterLogger;
            this.mOnUserInteractionCallback = onUserInteractionCallback;
        }
    }

    public StatusBarNotificationActivityStarter() {
        throw null;
    }

    public StatusBarNotificationActivityStarter(Context context, Handler handler, Executor executor, NotificationEntryManager notificationEntryManager, NotifPipeline notifPipeline, NotificationVisibilityProvider notificationVisibilityProvider, HeadsUpManagerPhone headsUpManagerPhone, ActivityStarter activityStarter, NotificationClickNotifier notificationClickNotifier, StatusBarStateController statusBarStateController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, KeyguardManager keyguardManager, IDreamManager iDreamManager, Optional optional, Lazy lazy, NotificationRemoteInputManager notificationRemoteInputManager, GroupMembershipManager groupMembershipManager, NotificationLockscreenUserManager notificationLockscreenUserManager, ShadeController shadeController, KeyguardStateController keyguardStateController, NotificationInterruptStateProvider notificationInterruptStateProvider, LockPatternUtils lockPatternUtils, StatusBarRemoteInputCallback statusBarRemoteInputCallback, ActivityIntentHelper activityIntentHelper, NotifPipelineFlags notifPipelineFlags, MetricsLogger metricsLogger, StatusBarNotificationActivityStarterLogger statusBarNotificationActivityStarterLogger, OnUserInteractionCallback onUserInteractionCallback, StatusBar statusBar, NotificationPresenter notificationPresenter, NotificationPanelViewController notificationPanelViewController, ActivityLaunchAnimator activityLaunchAnimator, DirectedAcyclicGraph directedAcyclicGraph) {
        this.mContext = context;
        this.mMainThreadHandler = handler;
        this.mUiBgExecutor = executor;
        this.mVisibilityProvider = notificationVisibilityProvider;
        this.mHeadsUpManager = headsUpManagerPhone;
        this.mActivityStarter = activityStarter;
        this.mClickNotifier = notificationClickNotifier;
        this.mStatusBarStateController = statusBarStateController;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.mKeyguardManager = keyguardManager;
        this.mDreamManager = iDreamManager;
        this.mBubblesManagerOptional = optional;
        this.mAssistManagerLazy = lazy;
        this.mRemoteInputManager = notificationRemoteInputManager;
        this.mGroupMembershipManager = groupMembershipManager;
        this.mLockscreenUserManager = notificationLockscreenUserManager;
        this.mShadeController = shadeController;
        this.mKeyguardStateController = keyguardStateController;
        this.mNotificationInterruptStateProvider = notificationInterruptStateProvider;
        this.mLockPatternUtils = lockPatternUtils;
        this.mStatusBarRemoteInputCallback = statusBarRemoteInputCallback;
        this.mActivityIntentHelper = activityIntentHelper;
        this.mMetricsLogger = metricsLogger;
        this.mLogger = statusBarNotificationActivityStarterLogger;
        this.mOnUserInteractionCallback = onUserInteractionCallback;
        this.mStatusBar = statusBar;
        this.mPresenter = notificationPresenter;
        this.mNotificationPanel = notificationPanelViewController;
        this.mActivityLaunchAnimator = activityLaunchAnimator;
        this.mNotificationAnimationProvider = directedAcyclicGraph;
        if (!notifPipelineFlags.isNewPipelineEnabled()) {
            notificationEntryManager.addNotificationEntryListener(new NotificationEntryListener() { // from class: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter.1
                @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
                public final void onPendingEntryAdded(NotificationEntry notificationEntry) {
                    StatusBarNotificationActivityStarter.this.handleFullScreenIntent(notificationEntry);
                }
            });
        } else {
            notifPipeline.addCollectionListener(new NotifCollectionListener() { // from class: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter.2
                @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
                public final void onEntryAdded(NotificationEntry notificationEntry) {
                    StatusBarNotificationActivityStarter.this.handleFullScreenIntent(notificationEntry);
                }
            });
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x00e1 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0105  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0114  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x011a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onNotificationClicked(android.service.notification.StatusBarNotification r15, final com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r16) {
        /*
            Method dump skipped, instructions count: 288
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter.onNotificationClicked(android.service.notification.StatusBarNotification, com.android.systemui.statusbar.notification.row.ExpandableNotificationRow):void");
    }

    public void handleFullScreenIntent(NotificationEntry notificationEntry) {
        boolean z;
        LogLevel logLevel = LogLevel.DEBUG;
        if (this.mNotificationInterruptStateProvider.shouldLaunchFullScreenIntentWhenAdded(notificationEntry)) {
            StatusBarNotificationPresenter statusBarNotificationPresenter = (StatusBarNotificationPresenter) this.mPresenter;
            Objects.requireNonNull(statusBarNotificationPresenter);
            if (statusBarNotificationPresenter.mVrMode) {
                z = true;
            } else {
                Objects.requireNonNull(notificationEntry);
                z = notificationEntry.shouldSuppressVisualEffect(4);
            }
            if (z) {
                StatusBarNotificationActivityStarterLogger statusBarNotificationActivityStarterLogger = this.mLogger;
                Objects.requireNonNull(notificationEntry);
                String str = notificationEntry.mKey;
                Objects.requireNonNull(statusBarNotificationActivityStarterLogger);
                LogBuffer logBuffer = statusBarNotificationActivityStarterLogger.buffer;
                StatusBarNotificationActivityStarterLogger$logFullScreenIntentSuppressedByDnD$2 statusBarNotificationActivityStarterLogger$logFullScreenIntentSuppressedByDnD$2 = StatusBarNotificationActivityStarterLogger$logFullScreenIntentSuppressedByDnD$2.INSTANCE;
                Objects.requireNonNull(logBuffer);
                if (!logBuffer.frozen) {
                    LogMessageImpl obtain = logBuffer.obtain("NotifActivityStarter", logLevel, statusBarNotificationActivityStarterLogger$logFullScreenIntentSuppressedByDnD$2);
                    obtain.str1 = str;
                    logBuffer.push(obtain);
                }
            } else if (notificationEntry.getImportance() < 4) {
                StatusBarNotificationActivityStarterLogger statusBarNotificationActivityStarterLogger2 = this.mLogger;
                String str2 = notificationEntry.mKey;
                Objects.requireNonNull(statusBarNotificationActivityStarterLogger2);
                LogBuffer logBuffer2 = statusBarNotificationActivityStarterLogger2.buffer;
                StatusBarNotificationActivityStarterLogger$logFullScreenIntentNotImportantEnough$2 statusBarNotificationActivityStarterLogger$logFullScreenIntentNotImportantEnough$2 = StatusBarNotificationActivityStarterLogger$logFullScreenIntentNotImportantEnough$2.INSTANCE;
                Objects.requireNonNull(logBuffer2);
                if (!logBuffer2.frozen) {
                    LogMessageImpl obtain2 = logBuffer2.obtain("NotifActivityStarter", logLevel, statusBarNotificationActivityStarterLogger$logFullScreenIntentNotImportantEnough$2);
                    obtain2.str1 = str2;
                    logBuffer2.push(obtain2);
                }
            } else {
                this.mUiBgExecutor.execute(new WifiEntry$$ExternalSyntheticLambda2(this, 8));
                PendingIntent pendingIntent = notificationEntry.mSbn.getNotification().fullScreenIntent;
                StatusBarNotificationActivityStarterLogger statusBarNotificationActivityStarterLogger3 = this.mLogger;
                String str3 = notificationEntry.mKey;
                Objects.requireNonNull(statusBarNotificationActivityStarterLogger3);
                LogBuffer logBuffer3 = statusBarNotificationActivityStarterLogger3.buffer;
                LogLevel logLevel2 = LogLevel.INFO;
                StatusBarNotificationActivityStarterLogger$logSendingFullScreenIntent$2 statusBarNotificationActivityStarterLogger$logSendingFullScreenIntent$2 = StatusBarNotificationActivityStarterLogger$logSendingFullScreenIntent$2.INSTANCE;
                Objects.requireNonNull(logBuffer3);
                if (!logBuffer3.frozen) {
                    LogMessageImpl obtain3 = logBuffer3.obtain("NotifActivityStarter", logLevel2, statusBarNotificationActivityStarterLogger$logSendingFullScreenIntent$2);
                    obtain3.str1 = str3;
                    obtain3.str2 = pendingIntent.getIntent().toString();
                    logBuffer3.push(obtain3);
                }
                try {
                    EventLog.writeEvent(36002, notificationEntry.mKey);
                    this.mStatusBar.wakeUpForFullScreenIntent();
                    pendingIntent.send();
                    notificationEntry.interruption = true;
                    notificationEntry.lastFullScreenIntentLaunchTime = SystemClock.elapsedRealtime();
                    this.mMetricsLogger.count("note_fullscreen", 1);
                } catch (PendingIntent.CanceledException unused) {
                }
            }
        }
    }

    public final void removeHunAfterClick(ExpandableNotificationRow expandableNotificationRow) {
        Objects.requireNonNull(expandableNotificationRow);
        NotificationEntry notificationEntry = expandableNotificationRow.mEntry;
        Objects.requireNonNull(notificationEntry);
        String key = notificationEntry.mSbn.getKey();
        HeadsUpManagerPhone headsUpManagerPhone = this.mHeadsUpManager;
        if (headsUpManagerPhone != null && headsUpManagerPhone.isAlerting(key)) {
            if (((StatusBarNotificationPresenter) this.mPresenter).isPresenterFullyCollapsed()) {
                expandableNotificationRow.setTag(2131428138, Boolean.TRUE);
            }
            this.mHeadsUpManager.removeNotification(key, true);
        }
    }
}
