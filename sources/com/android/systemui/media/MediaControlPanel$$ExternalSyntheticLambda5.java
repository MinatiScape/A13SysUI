package com.android.systemui.media;

import android.content.IntentFilter;
import android.view.View;
import com.google.android.systemui.gamedashboard.GameDashboardButton;
import com.google.android.systemui.gamedashboard.GameDashboardUiEventLogger;
import com.google.android.systemui.gamedashboard.GameMenuActivity;
import com.google.android.systemui.gamedashboard.ShortcutBarButton;
import com.google.android.systemui.gamedashboard.ShortcutBarController;
import com.google.android.systemui.gamedashboard.ShortcutBarView;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class MediaControlPanel$$ExternalSyntheticLambda5 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ MediaControlPanel$$ExternalSyntheticLambda5(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        GameDashboardUiEventLogger.GameDashboardEvent gameDashboardEvent;
        int i = 0;
        switch (this.$r8$classId) {
            case 0:
                MediaControlPanel mediaControlPanel = (MediaControlPanel) this.f$0;
                Runnable runnable = (Runnable) this.f$1;
                Objects.requireNonNull(mediaControlPanel);
                if (!mediaControlPanel.mFalsingManager.isFalseTap(1)) {
                    mediaControlPanel.logSmartspaceCardReported(760, false, 0, 0);
                    runnable.run();
                    return;
                }
                return;
            default:
                GameMenuActivity gameMenuActivity = (GameMenuActivity) this.f$0;
                GameDashboardButton gameDashboardButton = (GameDashboardButton) this.f$1;
                IntentFilter intentFilter = GameMenuActivity.DND_FILTER;
                Objects.requireNonNull(gameMenuActivity);
                ShortcutBarController shortcutBarController = gameMenuActivity.mShortcutBarController;
                Objects.requireNonNull(gameDashboardButton);
                boolean z = !gameDashboardButton.mToggled;
                Objects.requireNonNull(shortcutBarController);
                if (z) {
                    shortcutBarController.mToast.showShortcutText(2131952378);
                }
                ShortcutBarView shortcutBarView = shortcutBarController.mView;
                Objects.requireNonNull(shortcutBarView);
                GameDashboardUiEventLogger gameDashboardUiEventLogger = shortcutBarView.mUiEventLogger;
                if (z) {
                    gameDashboardEvent = GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_MENU_ENABLE_SCREENSHOT;
                } else {
                    gameDashboardEvent = GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_MENU_DISABLE_SCREENSHOT;
                }
                gameDashboardUiEventLogger.log(gameDashboardEvent);
                shortcutBarView.mIsScreenshotVisible = z;
                ShortcutBarButton shortcutBarButton = shortcutBarView.mScreenshotButton;
                if (!z) {
                    i = 8;
                }
                shortcutBarButton.setVisibility(i);
                if (shortcutBarView.mRevealButton.getVisibility() == 0 && shortcutBarView.mIsAttached) {
                    shortcutBarView.mRevealButton.bounce(z);
                }
                shortcutBarController.onButtonVisibilityChange(z);
                return;
        }
    }
}
