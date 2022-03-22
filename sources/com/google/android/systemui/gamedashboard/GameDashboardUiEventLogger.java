package com.google.android.systemui.gamedashboard;

import com.android.internal.logging.UiEventLogger;
/* loaded from: classes.dex */
public final class GameDashboardUiEventLogger {
    public EntryPointController mEntryPointController;
    public final UiEventLogger mUiEventLogger;

    /* loaded from: classes.dex */
    public enum GameDashboardEvent implements UiEventLogger.UiEventEnum {
        GAME_DASHBOARD_LAUNCH(723),
        /* JADX INFO: Fake field, exist only in values array */
        GAME_DASHBOARD_STARTUP(726),
        GAME_DASHBOARD_MENU_ENABLE_SCREENSHOT(727),
        GAME_DASHBOARD_MENU_DISABLE_SCREENSHOT(728),
        GAME_DASHBOARD_MENU_ENABLE_SCREEN_RECORD(729),
        GAME_DASHBOARD_MENU_DISABLE_SCREEN_RECORD(730),
        GAME_DASHBOARD_MENU_ENABLE_FPS(731),
        GAME_DASHBOARD_MENU_DISABLE_FPS(732),
        GAME_DASHBOARD_MENU_ENABLE_DND(733),
        GAME_DASHBOARD_MENU_DISABLE_DND(734),
        GAME_DASHBOARD_MENU_CLOSE(735),
        GAME_DASHBOARD_MENU_GAME_MODE_WIDGET(736),
        GAME_DASHBOARD_MENU_YOUTUBE_LIVE_WIDGET(737),
        GAME_DASHBOARD_MENU_PLAY_GAMES_WIDGET(738),
        GAME_DASHBOARD_PLAY_GAMES_IMPRESSION(835),
        /* JADX INFO: Fake field, exist only in values array */
        GAME_DASHBOARD_MENU_SHOW_LEARN_MORE(739),
        /* JADX INFO: Fake field, exist only in values array */
        GAME_DASHBOARD_MENU_LEARN_MORE(740),
        GAME_DASHBOARD_SCREENSHOT(741),
        GAME_DASHBOARD_SCREEN_RECORD(742),
        GAME_DASHBOARD_GAME_MODE_PERFORMANCE(743),
        GAME_DASHBOARD_GAME_MODE_STANDARD(744),
        GAME_DASHBOARD_GAME_MODE_BATTERY(745),
        GAME_DASHBOARD_RESTART_NOW(746),
        GAME_DASHBOARD_RESTART_CANCELLED(747),
        /* JADX INFO: Fake field, exist only in values array */
        GAME_DASHBOARD_WALKTHROUGH_SKIPPED(748),
        /* JADX INFO: Fake field, exist only in values array */
        GAME_DASHBOARD_WALKTHROUGH_LAUNCH(749),
        /* JADX INFO: Fake field, exist only in values array */
        GAME_DASHBOARD_WALKTHROUGH_OPTIMIZE(750),
        /* JADX INFO: Fake field, exist only in values array */
        GAME_DASHBOARD_WALKTHROUGH_SCREEN_CAPTURE(751),
        /* JADX INFO: Fake field, exist only in values array */
        GAME_DASHBOARD_WALKTHROUGH_DND(752),
        /* JADX INFO: Fake field, exist only in values array */
        GAME_DASHBOARD_WALKTHROUGH_FINISH(753);
        
        private final int mId;

        GameDashboardEvent(int i) {
            this.mId = i;
        }

        public final int getId() {
            return this.mId;
        }
    }

    public final void log(GameDashboardEvent gameDashboardEvent) {
        String str;
        UiEventLogger uiEventLogger = this.mUiEventLogger;
        EntryPointController entryPointController = this.mEntryPointController;
        if (entryPointController == null) {
            str = null;
        } else {
            str = entryPointController.mGamePackageName;
        }
        uiEventLogger.log(gameDashboardEvent, 0, str);
    }

    public GameDashboardUiEventLogger(UiEventLogger uiEventLogger) {
        this.mUiEventLogger = uiEventLogger;
    }
}
