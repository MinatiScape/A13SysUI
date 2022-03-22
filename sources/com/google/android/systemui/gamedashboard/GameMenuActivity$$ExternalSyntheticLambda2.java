package com.google.android.systemui.gamedashboard;

import android.content.IntentFilter;
import android.view.View;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.statusbar.notification.row.FooterView;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController$$ExternalSyntheticLambda2;
import com.google.android.systemui.gamedashboard.GameDashboardUiEventLogger;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class GameMenuActivity$$ExternalSyntheticLambda2 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ View f$1;

    public /* synthetic */ GameMenuActivity$$ExternalSyntheticLambda2(Object obj, View view, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = view;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        GameDashboardUiEventLogger.GameDashboardEvent gameDashboardEvent;
        int i = 0;
        switch (this.$r8$classId) {
            case 0:
                GameMenuActivity gameMenuActivity = (GameMenuActivity) this.f$0;
                GameDashboardButton gameDashboardButton = (GameDashboardButton) this.f$1;
                IntentFilter intentFilter = GameMenuActivity.DND_FILTER;
                Objects.requireNonNull(gameMenuActivity);
                ShortcutBarController shortcutBarController = gameMenuActivity.mShortcutBarController;
                Objects.requireNonNull(gameDashboardButton);
                boolean z = !gameDashboardButton.mToggled;
                Objects.requireNonNull(shortcutBarController);
                if (z) {
                    shortcutBarController.mToast.showShortcutText(2131952377);
                }
                ShortcutBarView shortcutBarView = shortcutBarController.mView;
                Objects.requireNonNull(shortcutBarView);
                GameDashboardUiEventLogger gameDashboardUiEventLogger = shortcutBarView.mUiEventLogger;
                if (z) {
                    gameDashboardEvent = GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_MENU_ENABLE_SCREEN_RECORD;
                } else {
                    gameDashboardEvent = GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_MENU_DISABLE_SCREEN_RECORD;
                }
                gameDashboardUiEventLogger.log(gameDashboardEvent);
                shortcutBarView.mIsRecordVisible = z;
                ShortcutBarButton shortcutBarButton = shortcutBarView.mRecordButton;
                if (!z) {
                    i = 8;
                }
                shortcutBarButton.setVisibility(i);
                if (shortcutBarView.mRevealButton.getVisibility() == 0 && shortcutBarView.mIsAttached) {
                    shortcutBarView.mRevealButton.bounce(z);
                }
                if (shortcutBarView.mIsRecordVisible) {
                    ScreenRecordController screenRecordController = shortcutBarView.mScreenRecordController;
                    Objects.requireNonNull(screenRecordController);
                    RecordingController recordingController = screenRecordController.mController;
                    Objects.requireNonNull(recordingController);
                    recordingController.mListeners.add(shortcutBarView);
                    shortcutBarView.setScreenRecordButtonDrawable();
                    ScreenRecordController screenRecordController2 = shortcutBarView.mScreenRecordController;
                    Objects.requireNonNull(screenRecordController2);
                    boolean isRecording = screenRecordController2.mController.isRecording();
                    shortcutBarView.setScreenRecordButtonBackground(isRecording);
                    RevealButton revealButton = shortcutBarView.mRevealButton;
                    Objects.requireNonNull(revealButton);
                    revealButton.mIsRecording = isRecording;
                    revealButton.invalidate();
                }
                shortcutBarController.onButtonVisibilityChange(z);
                return;
            default:
                NotificationStackScrollLayout notificationStackScrollLayout = (NotificationStackScrollLayout) this.f$0;
                FooterView footerView = (FooterView) this.f$1;
                boolean z2 = NotificationStackScrollLayout.SPEW;
                Objects.requireNonNull(notificationStackScrollLayout);
                NotificationStackScrollLayout.FooterClearAllListener footerClearAllListener = notificationStackScrollLayout.mFooterClearAllListener;
                if (footerClearAllListener != null) {
                    NotificationStackScrollLayoutController notificationStackScrollLayoutController = ((NotificationStackScrollLayoutController$$ExternalSyntheticLambda2) footerClearAllListener).f$0;
                    Objects.requireNonNull(notificationStackScrollLayoutController);
                    notificationStackScrollLayoutController.mMetricsLogger.action(148);
                }
                notificationStackScrollLayout.clearNotifications(0, true);
                footerView.setSecondaryVisible(false, true);
                return;
        }
    }
}
