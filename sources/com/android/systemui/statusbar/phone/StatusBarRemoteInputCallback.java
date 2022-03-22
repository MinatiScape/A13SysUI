package com.android.systemui.statusbar.phone;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.view.View;
import com.android.systemui.ActivityIntentHelper;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSFooterView$$ExternalSyntheticLambda0;
import com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.ActionClickLogger;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager$$ExternalSyntheticLambda2;
import com.android.systemui.statusbar.NotificationRemoteInputManager$1$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class StatusBarRemoteInputCallback implements NotificationRemoteInputManager.Callback, CommandQueue.Callbacks, StatusBarStateController.StateListener {
    public final ActionClickLogger mActionClickLogger;
    public final ActivityIntentHelper mActivityIntentHelper;
    public final ActivityStarter mActivityStarter;
    public ChallengeReceiver mChallengeReceiver;
    public final CommandQueue mCommandQueue;
    public final Context mContext;
    public int mDisabled2;
    public Executor mExecutor;
    public final GroupExpansionManager mGroupExpansionManager;
    public KeyguardManager mKeyguardManager;
    public final KeyguardStateController mKeyguardStateController;
    public final NotificationLockscreenUserManager mLockscreenUserManager;
    public View mPendingRemoteInputView;
    public View mPendingWorkRemoteInputView;
    public final ShadeController mShadeController;
    public final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    public final SysuiStatusBarStateController mStatusBarStateController;

    /* loaded from: classes.dex */
    public class ChallengeReceiver extends BroadcastReceiver {
        public ChallengeReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int intExtra = intent.getIntExtra("android.intent.extra.user_handle", -10000);
            if ("android.intent.action.DEVICE_LOCKED_CHANGED".equals(action) && intExtra != StatusBarRemoteInputCallback.this.mLockscreenUserManager.getCurrentUserId() && StatusBarRemoteInputCallback.this.mLockscreenUserManager.isCurrentProfile(intExtra)) {
                StatusBarRemoteInputCallback statusBarRemoteInputCallback = StatusBarRemoteInputCallback.this;
                Objects.requireNonNull(statusBarRemoteInputCallback);
                statusBarRemoteInputCallback.mLockscreenUserManager.updatePublicMode();
                if (statusBarRemoteInputCallback.mPendingWorkRemoteInputView != null && !statusBarRemoteInputCallback.mLockscreenUserManager.isAnyProfilePublicMode()) {
                    statusBarRemoteInputCallback.mShadeController.postOnShadeExpanded(new QSFooterView$$ExternalSyntheticLambda0(statusBarRemoteInputCallback, 5));
                    statusBarRemoteInputCallback.mShadeController.instantExpandNotificationsPanel();
                }
            }
        }
    }

    public StatusBarRemoteInputCallback(Context context, GroupExpansionManager groupExpansionManager, NotificationLockscreenUserManager notificationLockscreenUserManager, KeyguardStateController keyguardStateController, StatusBarStateController statusBarStateController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, ActivityStarter activityStarter, ShadeController shadeController, CommandQueue commandQueue, ActionClickLogger actionClickLogger, Executor executor) {
        ChallengeReceiver challengeReceiver = new ChallengeReceiver();
        this.mChallengeReceiver = challengeReceiver;
        this.mContext = context;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.mShadeController = shadeController;
        this.mExecutor = executor;
        context.registerReceiverAsUser(challengeReceiver, UserHandle.ALL, new IntentFilter("android.intent.action.DEVICE_LOCKED_CHANGED"), null, null);
        this.mLockscreenUserManager = notificationLockscreenUserManager;
        this.mKeyguardStateController = keyguardStateController;
        SysuiStatusBarStateController sysuiStatusBarStateController = (SysuiStatusBarStateController) statusBarStateController;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mActivityStarter = activityStarter;
        sysuiStatusBarStateController.addCallback(this);
        this.mKeyguardManager = (KeyguardManager) context.getSystemService(KeyguardManager.class);
        this.mCommandQueue = commandQueue;
        commandQueue.addCallback((CommandQueue.Callbacks) this);
        this.mActionClickLogger = actionClickLogger;
        this.mActivityIntentHelper = new ActivityIntentHelper(context);
        this.mGroupExpansionManager = groupExpansionManager;
    }

    public final boolean startWorkChallengeIfNecessary(int i, IntentSender intentSender, String str) {
        this.mPendingWorkRemoteInputView = null;
        Intent createConfirmDeviceCredentialIntent = this.mKeyguardManager.createConfirmDeviceCredentialIntent(null, null, i);
        if (createConfirmDeviceCredentialIntent == null) {
            return false;
        }
        Intent intent = new Intent("com.android.systemui.statusbar.work_challenge_unlocked_notification_action");
        intent.putExtra("android.intent.extra.INTENT", intentSender);
        intent.putExtra("android.intent.extra.INDEX", str);
        intent.setPackage(this.mContext.getPackageName());
        createConfirmDeviceCredentialIntent.putExtra("android.intent.extra.INTENT", PendingIntent.getBroadcast(this.mContext, 0, intent, 1409286144).getIntentSender());
        try {
            ActivityManager.getService().startConfirmDeviceCredentialIntent(createConfirmDeviceCredentialIntent, (Bundle) null);
            return true;
        } catch (RemoteException unused) {
            return true;
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void disable(int i, int i2, int i3, boolean z) {
        if (i == this.mContext.getDisplayId()) {
            this.mDisabled2 = i3;
        }
    }

    @Override // com.android.systemui.statusbar.NotificationRemoteInputManager.Callback
    public final void onLockedWorkRemoteInput(int i, View view) {
        this.mCommandQueue.animateCollapsePanels();
        startWorkChallengeIfNecessary(i, null, null);
        this.mPendingWorkRemoteInputView = view;
    }

    @Override // com.android.systemui.statusbar.NotificationRemoteInputManager.Callback
    public final void onMakeExpandedVisibleForRemoteInput(ExpandableNotificationRow expandableNotificationRow, View view, boolean z, NotificationRemoteInputManager$$ExternalSyntheticLambda2 notificationRemoteInputManager$$ExternalSyntheticLambda2) {
        if (z || !this.mKeyguardStateController.isShowing()) {
            if (expandableNotificationRow.isChildInGroup() && !expandableNotificationRow.mChildrenExpanded) {
                this.mGroupExpansionManager.toggleGroupExpansion(expandableNotificationRow.mEntry);
            }
            expandableNotificationRow.setUserExpanded(true, false);
            NotificationContentView notificationContentView = expandableNotificationRow.mPrivateLayout;
            Objects.requireNonNull(notificationContentView);
            notificationContentView.mExpandedVisibleListener = notificationRemoteInputManager$$ExternalSyntheticLambda2;
            notificationContentView.fireExpandedVisibleListenerIfVisible();
            return;
        }
        onLockedRemoteInput(expandableNotificationRow, view);
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public final void onStateChanged(int i) {
        boolean z;
        if (this.mPendingRemoteInputView != null) {
            z = true;
        } else {
            z = false;
        }
        if (i != 0) {
            return;
        }
        if ((this.mStatusBarStateController.leaveOpenOnKeyguardHide() || z) && !this.mStatusBarStateController.isKeyguardRequested() && this.mKeyguardStateController.isUnlocked()) {
            if (z) {
                Executor executor = this.mExecutor;
                View view = this.mPendingRemoteInputView;
                Objects.requireNonNull(view);
                executor.execute(new QSTileImpl$$ExternalSyntheticLambda0(view, 6));
            }
            this.mPendingRemoteInputView = null;
        }
    }

    @Override // com.android.systemui.statusbar.NotificationRemoteInputManager.Callback
    public final boolean shouldHandleRemoteInput() {
        if ((this.mDisabled2 & 4) != 0) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.NotificationRemoteInputManager.Callback
    public final boolean handleRemoteViewClick(final PendingIntent pendingIntent, boolean z, final NotificationRemoteInputManager$1$$ExternalSyntheticLambda0 notificationRemoteInputManager$1$$ExternalSyntheticLambda0) {
        if (!pendingIntent.isActivity() && !z) {
            return notificationRemoteInputManager$1$$ExternalSyntheticLambda0.handleClick();
        }
        this.mActionClickLogger.logWaitingToCloseKeyguard(pendingIntent);
        ActivityIntentHelper activityIntentHelper = this.mActivityIntentHelper;
        Intent intent = pendingIntent.getIntent();
        int currentUserId = this.mLockscreenUserManager.getCurrentUserId();
        Objects.requireNonNull(activityIntentHelper);
        boolean z2 = false;
        if (activityIntentHelper.getTargetActivityInfo(intent, currentUserId, false) == null) {
            z2 = true;
        }
        this.mActivityStarter.dismissKeyguardThenExecute(new ActivityStarter.OnDismissAction() { // from class: com.android.systemui.statusbar.phone.StatusBarRemoteInputCallback$$ExternalSyntheticLambda0
            @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
            public final boolean onDismiss() {
                StatusBarRemoteInputCallback statusBarRemoteInputCallback = StatusBarRemoteInputCallback.this;
                PendingIntent pendingIntent2 = pendingIntent;
                NotificationRemoteInputManager.ClickHandler clickHandler = notificationRemoteInputManager$1$$ExternalSyntheticLambda0;
                Objects.requireNonNull(statusBarRemoteInputCallback);
                statusBarRemoteInputCallback.mActionClickLogger.logKeyguardGone(pendingIntent2);
                try {
                    ActivityManager.getService().resumeAppSwitches();
                } catch (RemoteException unused) {
                }
                if (!((NotificationRemoteInputManager$1$$ExternalSyntheticLambda0) clickHandler).handleClick()) {
                    return false;
                }
                statusBarRemoteInputCallback.mShadeController.closeShadeIfOpen();
                return false;
            }
        }, null, z2);
        return true;
    }

    @Override // com.android.systemui.statusbar.NotificationRemoteInputManager.Callback
    public final void onLockedRemoteInput(ExpandableNotificationRow expandableNotificationRow, View view) {
        Objects.requireNonNull(expandableNotificationRow);
        if (!expandableNotificationRow.mIsPinned) {
            this.mStatusBarStateController.setLeaveOpenOnKeyguardHide(true);
        }
        this.mStatusBarKeyguardViewManager.showGenericBouncer();
        this.mPendingRemoteInputView = view;
    }
}
