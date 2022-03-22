package com.android.settingslib.users;

import android.app.Activity;
import android.content.Intent;
import android.service.notification.StatusBarNotification;
import android.view.View;
import com.android.settingslib.RestrictedLockUtils;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager$$ExternalSyntheticLambda2;
import com.android.systemui.statusbar.notification.row.NotificationInfo;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class EditUserInfoController$$ExternalSyntheticLambda3 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ EditUserInfoController$$ExternalSyntheticLambda3(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                RestrictedLockUtils.sendShowAdminSupportDetailsIntent((Activity) this.f$0, (RestrictedLockUtils.EnforcedAdmin) this.f$1);
                return;
            default:
                NotificationInfo notificationInfo = (NotificationInfo) this.f$0;
                Intent intent = (Intent) this.f$1;
                int i = NotificationInfo.$r8$clinit;
                Objects.requireNonNull(notificationInfo);
                NotificationGutsManager$$ExternalSyntheticLambda2 notificationGutsManager$$ExternalSyntheticLambda2 = (NotificationGutsManager$$ExternalSyntheticLambda2) notificationInfo.mAppSettingsClickListener;
                Objects.requireNonNull(notificationGutsManager$$ExternalSyntheticLambda2);
                NotificationGutsManager notificationGutsManager = notificationGutsManager$$ExternalSyntheticLambda2.f$0;
                NotificationGuts notificationGuts = notificationGutsManager$$ExternalSyntheticLambda2.f$1;
                StatusBarNotification statusBarNotification = notificationGutsManager$$ExternalSyntheticLambda2.f$2;
                ExpandableNotificationRow expandableNotificationRow = notificationGutsManager$$ExternalSyntheticLambda2.f$3;
                Objects.requireNonNull(notificationGutsManager);
                notificationGutsManager.mMetricsLogger.action(206);
                notificationGuts.resetFalsingCheck();
                NotificationActivityStarter notificationActivityStarter = notificationGutsManager.mNotificationActivityStarter;
                int uid = statusBarNotification.getUid();
                StatusBarNotificationActivityStarter statusBarNotificationActivityStarter = (StatusBarNotificationActivityStarter) notificationActivityStarter;
                Objects.requireNonNull(statusBarNotificationActivityStarter);
                StatusBar statusBar = statusBarNotificationActivityStarter.mStatusBar;
                Objects.requireNonNull(statusBar);
                statusBarNotificationActivityStarter.mActivityStarter.dismissKeyguardThenExecute(new StatusBarNotificationActivityStarter.AnonymousClass4(expandableNotificationRow, statusBar.shouldAnimateLaunch(true, false), intent, uid), null, false);
                return;
        }
    }
}
