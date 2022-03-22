package com.android.systemui.tuner;

import android.app.Dialog;
import android.view.View;
import com.android.systemui.biometrics.AuthBiometricView;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.row.FooterView;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class RadioListPreference$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ RadioListPreference$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        boolean z;
        switch (this.$r8$classId) {
            case 0:
                ((Dialog) this.f$0).dismiss();
                return;
            case 1:
                AuthBiometricView.$r8$lambda$WF3RgVIOlK7RYqAvt14uiLwyRTM((AuthBiometricView) this.f$0);
                return;
            default:
                NotificationStackScrollLayoutController notificationStackScrollLayoutController = (NotificationStackScrollLayoutController) this.f$0;
                Objects.requireNonNull(notificationStackScrollLayoutController);
                NotificationActivityStarter notificationActivityStarter = notificationStackScrollLayoutController.mNotificationActivityStarter;
                if (notificationActivityStarter != null) {
                    NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
                    Objects.requireNonNull(notificationStackScrollLayout);
                    FooterView footerView = notificationStackScrollLayout.mFooterView;
                    if (footerView == null || !footerView.mShowHistory) {
                        z = false;
                    } else {
                        z = true;
                    }
                    StatusBarNotificationActivityStarter statusBarNotificationActivityStarter = (StatusBarNotificationActivityStarter) notificationActivityStarter;
                    StatusBar statusBar = statusBarNotificationActivityStarter.mStatusBar;
                    Objects.requireNonNull(statusBar);
                    statusBarNotificationActivityStarter.mActivityStarter.dismissKeyguardThenExecute(new StatusBarNotificationActivityStarter.AnonymousClass5(z, view, statusBar.shouldAnimateLaunch(true, false)), null, false);
                    return;
                }
                return;
        }
    }
}
