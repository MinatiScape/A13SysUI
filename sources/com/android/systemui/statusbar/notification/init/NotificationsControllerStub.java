package com.android.systemui.statusbar.notification.init;

import android.service.notification.StatusBarNotification;
import android.util.IndentingPrintWriter;
import com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarNotificationPresenter;
import java.util.Optional;
/* compiled from: NotificationsControllerStub.kt */
/* loaded from: classes.dex */
public final class NotificationsControllerStub implements NotificationsController {
    public final NotificationListener notificationListener;

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public final int getActiveNotificationsCount() {
        return 0;
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public final void requestNotificationUpdate(String str) {
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public final void resetUserExpandedStates() {
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public final void setNotificationSnoozed(StatusBarNotification statusBarNotification, NotificationSwipeActionHelper.SnoozeOption snoozeOption) {
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public final void initialize(StatusBar statusBar, Optional optional, StatusBarNotificationPresenter statusBarNotificationPresenter, NotificationStackScrollLayoutController.NotificationListContainerImpl notificationListContainerImpl, NotificationStackScrollLayoutController.NotifStackControllerImpl notifStackControllerImpl, NotificationActivityStarter notificationActivityStarter, StatusBarNotificationPresenter statusBarNotificationPresenter2) {
        this.notificationListener.registerAsSystemService();
    }

    public NotificationsControllerStub(NotificationListener notificationListener) {
        this.notificationListener = notificationListener;
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public final void dump(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.println();
        indentingPrintWriter.println("Notification handling disabled");
        indentingPrintWriter.println();
    }
}
