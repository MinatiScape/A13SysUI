package com.android.systemui.statusbar.notification.init;

import android.service.notification.StatusBarNotification;
import android.util.IndentingPrintWriter;
import com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarNotificationPresenter;
import java.util.Optional;
/* compiled from: NotificationsController.kt */
/* loaded from: classes.dex */
public interface NotificationsController {
    void dump(IndentingPrintWriter indentingPrintWriter);

    int getActiveNotificationsCount();

    void initialize(StatusBar statusBar, Optional optional, StatusBarNotificationPresenter statusBarNotificationPresenter, NotificationStackScrollLayoutController.NotificationListContainerImpl notificationListContainerImpl, NotificationStackScrollLayoutController.NotifStackControllerImpl notifStackControllerImpl, NotificationActivityStarter notificationActivityStarter, StatusBarNotificationPresenter statusBarNotificationPresenter2);

    void requestNotificationUpdate(String str);

    void resetUserExpandedStates();

    void setNotificationSnoozed(StatusBarNotification statusBarNotification, NotificationSwipeActionHelper.SnoozeOption snoozeOption);
}
