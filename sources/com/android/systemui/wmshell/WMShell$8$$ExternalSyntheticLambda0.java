package com.android.systemui.wmshell;

import com.android.systemui.accessibility.SystemActions$$ExternalSyntheticLambda2;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.carrier.QSCarrierGroupController;
import com.android.systemui.screenshot.TimeoutHandler;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.DozeScrimController;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.StatusBarNotificationPresenter;
import com.android.systemui.wallet.controller.QuickAccessWalletController;
import com.android.systemui.wmshell.WMShell;
import com.android.wm.shell.bubbles.BubbleStackView;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WMShell$8$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ WMShell$8$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                WMShell.AnonymousClass8 r1 = (WMShell.AnonymousClass8) this.f$0;
                Objects.requireNonNull(r1);
                WMShell.this.mCommandQueue.handleSystemKey(281);
                return;
            case 1:
                ((TimeoutHandler) this.f$0).resetTimeout();
                return;
            case 2:
                ((QSCarrierGroupController) this.f$0).handleUpdateState();
                return;
            case 3:
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) this.f$0;
                ExpandableNotificationRow.AnonymousClass2 r0 = ExpandableNotificationRow.TRANSLATE_CONTENT;
                Objects.requireNonNull(expandableNotificationRow);
                expandableNotificationRow.applyAudiblyAlertedRecently(false);
                return;
            case 4:
                StatusBarNotificationPresenter statusBarNotificationPresenter = (StatusBarNotificationPresenter) this.f$0;
                Objects.requireNonNull(statusBarNotificationPresenter);
                NotificationPanelViewController notificationPanelViewController = statusBarNotificationPresenter.mNotificationPanel;
                Objects.requireNonNull(notificationPanelViewController);
                NotificationStackScrollLayoutController notificationStackScrollLayoutController = notificationPanelViewController.mNotificationStackScrollLayoutController;
                Objects.requireNonNull(notificationStackScrollLayoutController);
                NotificationStackScrollLayoutController.NotificationListContainerImpl notificationListContainerImpl = notificationStackScrollLayoutController.mNotificationListContainer;
                Objects.requireNonNull(notificationListContainerImpl);
                NotificationStackScrollLayout notificationStackScrollLayout = NotificationStackScrollLayoutController.this.mView;
                Objects.requireNonNull(notificationStackScrollLayout);
                if (notificationStackScrollLayout.mPulsing) {
                    HeadsUpManagerPhone headsUpManagerPhone = statusBarNotificationPresenter.mHeadsUpManager;
                    Objects.requireNonNull(headsUpManagerPhone);
                    if (!(!headsUpManagerPhone.mAlertEntries.isEmpty())) {
                        DozeScrimController dozeScrimController = statusBarNotificationPresenter.mDozeScrimController;
                        Objects.requireNonNull(dozeScrimController);
                        if (dozeScrimController.mPulseCallback != null && dozeScrimController.mFullyPulsing) {
                            dozeScrimController.mPulseOut.run();
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            case 5:
                QuickAccessWalletController.AnonymousClass2 r12 = (QuickAccessWalletController.AnonymousClass2) this.f$0;
                int i = QuickAccessWalletController.AnonymousClass2.$r8$clinit;
                Objects.requireNonNull(r12);
                QuickAccessWalletController.this.updateWalletPreference();
                return;
            case FalsingManager.VERSION /* 6 */:
                BubbleStackView.$r8$lambda$k7rtryEI4rtW4AORrn7yrpeihz4((BubbleStackView) this.f$0);
                return;
            case 7:
                LegacySplitScreenController.SplitScreenImpl splitScreenImpl = (LegacySplitScreenController.SplitScreenImpl) this.f$0;
                Objects.requireNonNull(splitScreenImpl);
                LegacySplitScreenController.this.onUndockingTask();
                return;
            default:
                PhonePipMenuController phonePipMenuController = (PhonePipMenuController) this.f$0;
                Objects.requireNonNull(phonePipMenuController);
                phonePipMenuController.mListeners.forEach(SystemActions$$ExternalSyntheticLambda2.INSTANCE$3);
                return;
        }
    }
}
