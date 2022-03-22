package com.android.systemui.statusbar.phone;

import com.android.systemui.ScreenDecorations$2$$ExternalSyntheticLambda0;
import com.android.systemui.doze.DozeHost;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSFgsManagerFooter$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.init.NotificationsController;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.wifitrackerlib.BaseWifiTracker$$ExternalSyntheticLambda1;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class StatusBarHeadsUpChangeListener implements OnHeadsUpChangedListener {
    public final DozeScrimController mDozeScrimController;
    public final DozeServiceHost mDozeServiceHost;
    public final HeadsUpManagerPhone mHeadsUpManager;
    public final KeyguardBypassController mKeyguardBypassController;
    public final NotificationPanelViewController mNotificationPanelViewController;
    public final NotificationRemoteInputManager mNotificationRemoteInputManager;
    public final NotificationShadeWindowController mNotificationShadeWindowController;
    public final NotificationsController mNotificationsController;
    public final StatusBarStateController mStatusBarStateController;
    public final StatusBarWindowController mStatusBarWindowController;

    @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
    public final void onHeadsUpPinnedModeChanged(boolean z) {
        boolean z2;
        if (z) {
            this.mNotificationShadeWindowController.setHeadsUpShowing(true);
            StatusBarWindowController statusBarWindowController = this.mStatusBarWindowController;
            Objects.requireNonNull(statusBarWindowController);
            StatusBarWindowController.State state = statusBarWindowController.mCurrentState;
            state.mForceStatusBarVisible = true;
            statusBarWindowController.apply(state);
            if (this.mNotificationPanelViewController.isFullyCollapsed()) {
                NotificationPanelViewController notificationPanelViewController = this.mNotificationPanelViewController;
                Objects.requireNonNull(notificationPanelViewController);
                ((PanelViewController) notificationPanelViewController).mView.requestLayout();
                this.mNotificationShadeWindowController.setForceWindowCollapsed(true);
                NotificationPanelViewController notificationPanelViewController2 = this.mNotificationPanelViewController;
                Objects.requireNonNull(notificationPanelViewController2);
                ((PanelViewController) notificationPanelViewController2).mView.post(new BaseWifiTracker$$ExternalSyntheticLambda1(this, 5));
                return;
            }
            return;
        }
        if (!this.mKeyguardBypassController.getBypassEnabled() || this.mStatusBarStateController.getState() != 1) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (this.mNotificationPanelViewController.isFullyCollapsed()) {
            NotificationPanelViewController notificationPanelViewController3 = this.mNotificationPanelViewController;
            Objects.requireNonNull(notificationPanelViewController3);
            if (!notificationPanelViewController3.mTracking && !z2) {
                this.mHeadsUpManager.setHeadsUpGoingAway(true);
                NotificationPanelViewController notificationPanelViewController4 = this.mNotificationPanelViewController;
                QSFgsManagerFooter$$ExternalSyntheticLambda0 qSFgsManagerFooter$$ExternalSyntheticLambda0 = new QSFgsManagerFooter$$ExternalSyntheticLambda0(this, 5);
                Objects.requireNonNull(notificationPanelViewController4);
                NotificationStackScrollLayoutController notificationStackScrollLayoutController = notificationPanelViewController4.mNotificationStackScrollLayoutController;
                Objects.requireNonNull(notificationStackScrollLayoutController);
                NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
                Objects.requireNonNull(notificationStackScrollLayout);
                notificationStackScrollLayout.mAnimationFinishedRunnables.add(qSFgsManagerFooter$$ExternalSyntheticLambda0);
                return;
            }
        }
        this.mNotificationShadeWindowController.setHeadsUpShowing(false);
        if (z2) {
            StatusBarWindowController statusBarWindowController2 = this.mStatusBarWindowController;
            Objects.requireNonNull(statusBarWindowController2);
            StatusBarWindowController.State state2 = statusBarWindowController2.mCurrentState;
            state2.mForceStatusBarVisible = false;
            statusBarWindowController2.apply(state2);
        }
    }

    @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
    public final void onHeadsUpStateChanged(NotificationEntry notificationEntry, boolean z) {
        this.mNotificationsController.requestNotificationUpdate("onHeadsUpStateChanged");
        if (this.mStatusBarStateController.isDozing() && z) {
            notificationEntry.mPulseSupressed = false;
            DozeServiceHost dozeServiceHost = this.mDozeServiceHost;
            Objects.requireNonNull(dozeServiceHost);
            ScreenDecorations$2$$ExternalSyntheticLambda0 screenDecorations$2$$ExternalSyntheticLambda0 = new ScreenDecorations$2$$ExternalSyntheticLambda0(dozeServiceHost, notificationEntry, 1);
            Iterator<DozeHost.Callback> it = dozeServiceHost.mCallbacks.iterator();
            while (it.hasNext()) {
                it.next().onNotificationAlerted(screenDecorations$2$$ExternalSyntheticLambda0);
            }
            DozeServiceHost dozeServiceHost2 = this.mDozeServiceHost;
            Objects.requireNonNull(dozeServiceHost2);
            if (dozeServiceHost2.mPulsing) {
                DozeScrimController dozeScrimController = this.mDozeScrimController;
                Objects.requireNonNull(dozeScrimController);
                dozeScrimController.mHandler.removeCallbacks(dozeScrimController.mPulseOut);
                dozeScrimController.mHandler.removeCallbacks(dozeScrimController.mPulseOutExtended);
            }
        }
        if (!z) {
            HeadsUpManagerPhone headsUpManagerPhone = this.mHeadsUpManager;
            Objects.requireNonNull(headsUpManagerPhone);
            if (!(!headsUpManagerPhone.mAlertEntries.isEmpty())) {
                DozeScrimController dozeScrimController2 = this.mDozeScrimController;
                Objects.requireNonNull(dozeScrimController2);
                if (dozeScrimController2.mPulseCallback != null && dozeScrimController2.mFullyPulsing) {
                    dozeScrimController2.mPulseOut.run();
                }
            }
        }
    }

    public StatusBarHeadsUpChangeListener(NotificationShadeWindowController notificationShadeWindowController, StatusBarWindowController statusBarWindowController, NotificationPanelViewController notificationPanelViewController, KeyguardBypassController keyguardBypassController, HeadsUpManagerPhone headsUpManagerPhone, StatusBarStateController statusBarStateController, NotificationRemoteInputManager notificationRemoteInputManager, NotificationsController notificationsController, DozeServiceHost dozeServiceHost, DozeScrimController dozeScrimController) {
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mStatusBarWindowController = statusBarWindowController;
        this.mNotificationPanelViewController = notificationPanelViewController;
        this.mKeyguardBypassController = keyguardBypassController;
        this.mHeadsUpManager = headsUpManagerPhone;
        this.mStatusBarStateController = statusBarStateController;
        this.mNotificationRemoteInputManager = notificationRemoteInputManager;
        this.mNotificationsController = notificationsController;
        this.mDozeServiceHost = dozeServiceHost;
        this.mDozeScrimController = dozeScrimController;
    }
}
