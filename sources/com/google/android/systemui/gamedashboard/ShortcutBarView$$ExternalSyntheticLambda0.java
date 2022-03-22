package com.google.android.systemui.gamedashboard;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.android.systemui.power.PowerUI$$ExternalSyntheticLambda0;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.statusbar.notification.row.FeedbackInfo;
import com.google.android.systemui.gamedashboard.GameDashboardUiEventLogger;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ShortcutBarView$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ ViewGroup f$0;

    public /* synthetic */ ShortcutBarView$$ExternalSyntheticLambda0(ViewGroup viewGroup, int i) {
        this.$r8$classId = i;
        this.f$0 = viewGroup;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                ShortcutBarView shortcutBarView = (ShortcutBarView) this.f$0;
                int i = ShortcutBarView.SHORTCUT_BAR_BACKGROUND_COLOR;
                Objects.requireNonNull(shortcutBarView);
                ScreenRecordController screenRecordController = shortcutBarView.mScreenRecordController;
                Objects.requireNonNull(screenRecordController);
                if (!screenRecordController.mController.isRecording()) {
                    ScreenRecordController screenRecordController2 = shortcutBarView.mScreenRecordController;
                    Objects.requireNonNull(screenRecordController2);
                    RecordingController recordingController = screenRecordController2.mController;
                    Objects.requireNonNull(recordingController);
                    if (!recordingController.mIsStarting) {
                        ScreenRecordController screenRecordController3 = shortcutBarView.mScreenRecordController;
                        Objects.requireNonNull(screenRecordController3);
                        RecordingController recordingController2 = screenRecordController3.mController;
                        Objects.requireNonNull(recordingController2);
                        recordingController2.mListeners.add(shortcutBarView);
                        shortcutBarView.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_SCREEN_RECORD);
                    }
                }
                ScreenRecordController screenRecordController4 = shortcutBarView.mScreenRecordController;
                Objects.requireNonNull(screenRecordController4);
                RecordingController recordingController3 = screenRecordController4.mController;
                Objects.requireNonNull(recordingController3);
                if (recordingController3.mIsStarting) {
                    screenRecordController4.mController.cancelCountdown();
                    return;
                } else if (screenRecordController4.mController.isRecording()) {
                    screenRecordController4.mController.stopRecording();
                    ToastController toastController = screenRecordController4.mToast;
                    Objects.requireNonNull(toastController);
                    String str = (String) toastController.mContext.getResources().getText(2131952402);
                    toastController.mRecordSaveView.setText(str);
                    int margin = toastController.getMargin();
                    toastController.mRecordSaveView.measure(0, 0);
                    toastController.removeViewImmediate();
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, toastController.mRecordSaveView.getMeasuredHeight() + margin, 0, 0, 2024, 8, -3);
                    layoutParams.privateFlags |= 16;
                    layoutParams.layoutInDisplayCutoutMode = 3;
                    layoutParams.setTitle("ToastText");
                    layoutParams.setFitInsetsTypes(0);
                    layoutParams.gravity = 80;
                    toastController.show(layoutParams, 3);
                    toastController.mRecordSaveView.announceForAccessibility(str);
                    return;
                } else {
                    screenRecordController4.mUiHandler.post(new PowerUI$$ExternalSyntheticLambda0(screenRecordController4, 7));
                    return;
                }
            default:
                FeedbackInfo.$r8$lambda$v7f9kE2ar9FL2Q9Wqe6UnRy2T6A((FeedbackInfo) this.f$0, view);
                return;
        }
    }
}
