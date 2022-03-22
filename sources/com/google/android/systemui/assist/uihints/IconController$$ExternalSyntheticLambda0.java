package com.google.android.systemui.assist.uihints;

import android.app.PendingIntent;
import android.util.Log;
import android.view.View;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class IconController$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ IconController$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                IconController iconController = (IconController) this.f$0;
                Objects.requireNonNull(iconController);
                PendingIntent pendingIntent = iconController.mOnKeyboardIconTap;
                if (pendingIntent != null) {
                    try {
                        pendingIntent.send();
                        return;
                    } catch (PendingIntent.CanceledException e) {
                        Log.e("IconController", "Pending intent cancelled", e);
                        return;
                    }
                } else {
                    return;
                }
            default:
                NotificationStackScrollLayoutController notificationStackScrollLayoutController = (NotificationStackScrollLayoutController) this.f$0;
                Objects.requireNonNull(notificationStackScrollLayoutController);
                notificationStackScrollLayoutController.mView.clearNotifications(2, true ^ notificationStackScrollLayoutController.hasNotifications(1, true));
                return;
        }
    }
}
