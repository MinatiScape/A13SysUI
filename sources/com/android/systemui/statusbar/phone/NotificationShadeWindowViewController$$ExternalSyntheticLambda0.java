package com.android.systemui.statusbar.phone;

import android.hardware.display.AmbientDisplayConfiguration;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.tuner.TunerService;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationShadeWindowViewController$$ExternalSyntheticLambda0 implements TunerService.Tunable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ NotificationShadeWindowViewController$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public final void onTuningChanged(String str, String str2) {
        switch (this.$r8$classId) {
            case 0:
                NotificationShadeWindowViewController notificationShadeWindowViewController = (NotificationShadeWindowViewController) this.f$0;
                Objects.requireNonNull(notificationShadeWindowViewController);
                AmbientDisplayConfiguration ambientDisplayConfiguration = new AmbientDisplayConfiguration(notificationShadeWindowViewController.mView.getContext());
                Objects.requireNonNull(str);
                if (str.equals("doze_tap_gesture")) {
                    notificationShadeWindowViewController.mSingleTapEnabled = ambientDisplayConfiguration.tapGestureEnabled(-2);
                    return;
                } else if (str.equals("doze_pulse_on_double_tap")) {
                    notificationShadeWindowViewController.mDoubleTapEnabled = ambientDisplayConfiguration.doubleTapGestureEnabled(-2);
                    return;
                } else {
                    return;
                }
            default:
                NotificationStackScrollLayoutController notificationStackScrollLayoutController = (NotificationStackScrollLayoutController) this.f$0;
                Objects.requireNonNull(notificationStackScrollLayoutController);
                Objects.requireNonNull(str);
                if (str.equals("high_priority")) {
                    NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
                    boolean equals = "1".equals(str2);
                    Objects.requireNonNull(notificationStackScrollLayout);
                    notificationStackScrollLayout.mHighPriorityBeforeSpeedBump = equals;
                    return;
                } else if (str.equals("notification_history_enabled")) {
                    notificationStackScrollLayoutController.mHistoryEnabled = null;
                    notificationStackScrollLayoutController.updateFooter();
                    return;
                } else {
                    return;
                }
        }
    }
}
