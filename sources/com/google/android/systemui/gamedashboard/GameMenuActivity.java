package com.google.android.systemui.gamedashboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AutomaticZenRule;
import android.app.GameManager;
import android.app.IActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.constraintlayout.motion.widget.MotionLayout$$ExternalSyntheticOutline0;
import com.android.settingslib.inputmethod.InputMethodPreference$$ExternalSyntheticLambda1;
import com.android.settingslib.users.AvatarPickerActivity$$ExternalSyntheticLambda1;
import com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda5;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda3;
import com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda10;
import com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda9;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.RemoteInputView$$ExternalSyntheticLambda0;
import com.android.systemui.wallet.ui.WalletActivity$$ExternalSyntheticLambda0;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda21;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda4;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda5;
import com.android.wm.shell.pip.phone.PipMenuView$$ExternalSyntheticLambda1;
import com.android.wm.shell.pip.phone.PipMenuView$$ExternalSyntheticLambda2;
import com.android.wm.shell.pip.phone.PipMenuView$$ExternalSyntheticLambda3;
import com.google.android.systemui.gamedashboard.GameDashboardUiEventLogger;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class GameMenuActivity extends Activity implements View.OnApplyWindowInsetsListener {
    public static final IntentFilter DND_FILTER = new IntentFilter("android.app.action.INTERRUPTION_FILTER_CHANGED_INTERNAL");
    public final ActivityStarter mActivityStarter;
    public View mBackNavigationButton;
    public RadioButton mBatteryModeRadioButton;
    public final Context mContext;
    public final EntryPointController mController;
    public View mCurrentView;
    public WidgetContainer mCurrentWidgetContainer;
    public GameDashboardButton mDndButton;
    public final GameModeDndController mDndController;
    public GameManager mGameManager;
    public View mGameMenuMainView;
    public View mGameModeView;
    public WidgetView mGameModeWidget;
    public boolean mGameModesSupported;
    public final LayoutInflater mLayoutInflater;
    public final Handler mMainHandler;
    public int mMaxWidgetsPerContainer;
    public RadioButton mPerformanceModeRadioButton;
    public PlayGamesWidget mPlayGamesWidget;
    public View mSettingsButton;
    public int mShortAnimationDuration;
    public final ShortcutBarController mShortcutBarController;
    public RadioButton mStandardModeRadioButton;
    public final GameDashboardUiEventLogger mUiEventLogger;
    public LinearLayout mWidgetsView;
    public YouTubeLiveWidget mYouTubeLiveWidget;
    public final ArraySet<Integer> mAvailableModes = new ArraySet<>();
    public final AnonymousClass3 mDndReceiver = new BroadcastReceiver() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity.3
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            boolean z;
            GameDashboardUiEventLogger.GameDashboardEvent gameDashboardEvent;
            if (GameMenuActivity.this.mDndController != null && "android.app.action.INTERRUPTION_FILTER_CHANGED_INTERNAL".equals(intent.getAction())) {
                GameModeDndController gameModeDndController = GameMenuActivity.this.mDndController;
                Objects.requireNonNull(gameModeDndController);
                AutomaticZenRule fetchRule = gameModeDndController.fetchRule();
                if (fetchRule == null || fetchRule.getInterruptionFilter() != 2) {
                    z = false;
                } else {
                    z = true;
                }
                gameModeDndController.mFilterActive = z;
                gameModeDndController.mGameActiveOld = gameModeDndController.mGameActive;
                gameModeDndController.mUserActiveOld = gameModeDndController.mUserActive;
                gameModeDndController.mFilterActiveOld = z;
                GameDashboardButton gameDashboardButton = GameMenuActivity.this.mDndButton;
                Objects.requireNonNull(gameDashboardButton);
                if (gameDashboardButton.mToggled != GameMenuActivity.this.mDndController.isGameModeDndOn()) {
                    GameMenuActivity gameMenuActivity = GameMenuActivity.this;
                    gameMenuActivity.mDndButton.setToggled(gameMenuActivity.mDndController.isGameModeDndOn(), true);
                }
                GameMenuActivity gameMenuActivity2 = GameMenuActivity.this;
                GameDashboardUiEventLogger gameDashboardUiEventLogger = gameMenuActivity2.mUiEventLogger;
                if (gameMenuActivity2.mDndController.isGameModeDndOn()) {
                    gameDashboardEvent = GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_MENU_ENABLE_DND;
                } else {
                    gameDashboardEvent = GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_MENU_DISABLE_DND;
                }
                gameDashboardUiEventLogger.log(gameDashboardEvent);
            }
        }
    };

    @Override // android.app.Activity
    public final void onActivityResult(int i, int i2, Intent intent) {
        if (i2 == -1) {
            finish();
        }
    }

    public final void navigateToGameModeView() {
        this.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_MENU_GAME_MODE_WIDGET);
        navigateToView(this.mGameModeView, 2131952384);
        GameManager gameManager = this.mGameManager;
        EntryPointController entryPointController = this.mController;
        Objects.requireNonNull(entryPointController);
        toggleGameModeRadioButtons(gameManager.getGameMode(entryPointController.mGamePackageName));
    }

    public final void navigateToView(final View view, final int i) {
        if (view != null) {
            view.setAlpha(0.0f);
            view.setVisibility(0);
            view.animate().alpha(1.0f).setDuration(this.mShortAnimationDuration).setListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    if (GameMenuActivity.this.mCurrentView.getId() == 2131428006) {
                        GameMenuActivity.this.mBackNavigationButton.setVisibility(0);
                        GameMenuActivity.this.mSettingsButton.setVisibility(4);
                    } else {
                        GameMenuActivity.this.mBackNavigationButton.setVisibility(4);
                        GameMenuActivity.this.mSettingsButton.setVisibility(0);
                    }
                    ((TextView) GameMenuActivity.this.findViewById(2131428011)).setText(i);
                    GameMenuActivity gameMenuActivity = GameMenuActivity.this;
                    View view2 = view;
                    gameMenuActivity.mCurrentView = view2;
                    view2.animate().setListener(null);
                }
            });
            final View view2 = this.mCurrentView;
            view2.animate().alpha(0.0f).setDuration(this.mShortAnimationDuration).setListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    view2.setVisibility(8);
                    view2.animate().setListener(null);
                }
            });
        }
    }

    @Override // android.view.View.OnApplyWindowInsetsListener
    public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
        DisplayCutout displayCutout;
        int i;
        int i2;
        if (windowInsets != null) {
            displayCutout = windowInsets.getDisplayCutout();
        } else {
            displayCutout = null;
        }
        if (displayCutout != null) {
            int safeInsetLeft = displayCutout.getSafeInsetLeft();
            int safeInsetRight = displayCutout.getSafeInsetRight();
            if (safeInsetLeft >= safeInsetRight) {
                i2 = safeInsetLeft - safeInsetRight;
                i = 0;
            } else {
                i = safeInsetRight - safeInsetLeft;
                i2 = 0;
            }
            view.setPadding(i, 0, i2, 0);
            windowInsets.consumeDisplayCutout();
        }
        return windowInsets;
    }

    @Override // android.app.Activity
    public final void onBackPressed() {
        if (this.mCurrentView.getId() == 2131428006) {
            super.onBackPressed();
            return;
        }
        navigateToView(this.mGameMenuMainView, 2131952376);
        updateWidgets();
    }

    public final void toggleGameModeRadioButtons(int i) {
        boolean z;
        boolean z2;
        RadioButton radioButton = this.mPerformanceModeRadioButton;
        boolean z3 = false;
        if (i == 2) {
            z = true;
        } else {
            z = false;
        }
        radioButton.setChecked(z);
        RadioButton radioButton2 = this.mStandardModeRadioButton;
        if (i == 1) {
            z2 = true;
        } else {
            z2 = false;
        }
        radioButton2.setChecked(z2);
        RadioButton radioButton3 = this.mBatteryModeRadioButton;
        if (i == 3) {
            z3 = true;
        }
        radioButton3.setChecked(z3);
    }

    public final void updateWidgets() {
        boolean z;
        EntryPointController entryPointController = this.mController;
        Objects.requireNonNull(entryPointController);
        final String str = entryPointController.mGamePackageName;
        int gameMode = this.mGameManager.getGameMode(str);
        if (!this.mGameModesSupported) {
            this.mGameModeWidget.setEnabled(false);
            this.mGameModeWidget.update(getDrawable(2131231959), 2131952384, 2131952401, null);
        } else if (gameMode == 2) {
            this.mGameModeWidget.setEnabled(true);
            this.mGameModeWidget.update(getDrawable(2131231963), 2131952391, 2131952390, new WalletActivity$$ExternalSyntheticLambda0(this, 1));
        } else if (gameMode == 3) {
            this.mGameModeWidget.setEnabled(true);
            this.mGameModeWidget.update(getDrawable(2131231961), 2131952388, 2131952387, new BubbleStackView$$ExternalSyntheticLambda4(this, 5));
        } else {
            this.mGameModeWidget.update(getDrawable(2131231959), 2131952384, 2131952383, new BubbleStackView$$ExternalSyntheticLambda5(this, 2));
        }
        final YouTubeLiveWidget youTubeLiveWidget = this.mYouTubeLiveWidget;
        Objects.requireNonNull(youTubeLiveWidget);
        List<ResolveInfo> queryIntentActivities = youTubeLiveWidget.mContext.getPackageManager().queryIntentActivities(new Intent("com.google.android.youtube.intent.action.CREATE_LIVE_STREAM").setPackage("com.google.android.youtube"), 65536);
        if (queryIntentActivities == null || queryIntentActivities.isEmpty()) {
            z = false;
        } else {
            z = true;
        }
        if (!z) {
            youTubeLiveWidget.mWidgetView.update(youTubeLiveWidget.mContext.getDrawable(2131232339), 2131952386, 2131952385, null);
            youTubeLiveWidget.mWidgetView.setEnabled(false);
        } else {
            youTubeLiveWidget.mWidgetView.update(youTubeLiveWidget.mContext.getDrawable(2131232339), 2131952386, 2131952385, new View.OnClickListener() { // from class: com.google.android.systemui.gamedashboard.YouTubeLiveWidget$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    YouTubeLiveWidget youTubeLiveWidget2 = YouTubeLiveWidget.this;
                    String str2 = str;
                    Objects.requireNonNull(youTubeLiveWidget2);
                    youTubeLiveWidget2.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_MENU_YOUTUBE_LIVE_WIDGET);
                    Intent intent = new Intent("com.google.android.youtube.intent.action.CREATE_LIVE_STREAM").setPackage("com.google.android.youtube");
                    intent.putExtra("android.intent.extra.REFERRER", new Uri.Builder().scheme("android-app").appendPath(youTubeLiveWidget2.mContext.getPackageName()).build());
                    if (!TextUtils.isEmpty(str2)) {
                        intent.putExtra("GAME_PACKAGE_NAME", str2);
                        PackageManager packageManager = youTubeLiveWidget2.mContext.getPackageManager();
                        ApplicationInfo applicationInfo = null;
                        try {
                            applicationInfo = packageManager.getApplicationInfo(str2, 0);
                        } catch (PackageManager.NameNotFoundException unused) {
                            MotionLayout$$ExternalSyntheticOutline0.m("Fail to query application info for ", str2, "YouTubeLiveWidget");
                        }
                        if (applicationInfo != null) {
                            intent.putExtra("GAME_TITLE", packageManager.getApplicationLabel(applicationInfo).toString());
                        }
                    }
                    intent.putExtra("CAPTURE_MODE", "SCREEN");
                    ((Activity) youTubeLiveWidget2.mContext).startActivityForResult(intent, 0);
                }
            });
        }
        PlayGamesWidget playGamesWidget = this.mPlayGamesWidget;
        if (playGamesWidget != null) {
            Objects.requireNonNull(playGamesWidget);
            playGamesWidget.mWidgetView.setLoading(true);
            playGamesWidget.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_PLAY_GAMES_IMPRESSION);
            playGamesWidget.mMainHandler.post(new BubbleStackView$$ExternalSyntheticLambda21(playGamesWidget, str, 6));
        }
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.google.android.systemui.gamedashboard.GameMenuActivity$3] */
    public GameMenuActivity(Context context, EntryPointController entryPointController, ActivityStarter activityStarter, ShortcutBarController shortcutBarController, GameModeDndController gameModeDndController, LayoutInflater layoutInflater, Handler handler, GameDashboardUiEventLogger gameDashboardUiEventLogger) {
        this.mContext = context;
        this.mController = entryPointController;
        this.mShortcutBarController = shortcutBarController;
        this.mActivityStarter = activityStarter;
        this.mDndController = gameModeDndController;
        this.mLayoutInflater = layoutInflater;
        this.mMainHandler = handler;
        this.mUiEventLogger = gameDashboardUiEventLogger;
        Objects.requireNonNull(gameDashboardUiEventLogger);
        gameDashboardUiEventLogger.mEntryPointController = entryPointController;
    }

    @Override // android.app.Activity
    public final void onCreate(Bundle bundle) {
        boolean z;
        PlayGamesWidget playGamesWidget;
        ProviderInfo[] providerInfoArr;
        super.onCreate(bundle);
        setContentView(2131624106);
        View decorView = getWindow().getDecorView();
        decorView.getWindowInsetsController().hide(WindowInsets.Type.navigationBars());
        decorView.setOnApplyWindowInsetsListener(this);
        GameManager gameManager = (GameManager) getSystemService(GameManager.class);
        this.mGameManager = gameManager;
        EntryPointController entryPointController = this.mController;
        Objects.requireNonNull(entryPointController);
        for (int i : gameManager.getAvailableGameModes(entryPointController.mGamePackageName)) {
            this.mAvailableModes.add(Integer.valueOf(i));
        }
        if (this.mAvailableModes.size() > 0) {
            this.mAvailableModes.add(1);
            this.mGameModesSupported = true;
        }
        this.mShortAnimationDuration = getResources().getInteger(17694720);
        View findViewById = findViewById(2131428002);
        this.mBackNavigationButton = findViewById;
        findViewById.setOnClickListener(new ScreenshotView$$ExternalSyntheticLambda10(this, 2));
        final View view = this.mBackNavigationButton;
        final View view2 = (View) view.getParent();
        view2.post(new Runnable() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                GameMenuActivity gameMenuActivity = GameMenuActivity.this;
                int i2 = r2;
                View view3 = view;
                View view4 = view2;
                IntentFilter intentFilter = GameMenuActivity.DND_FILTER;
                Objects.requireNonNull(gameMenuActivity);
                int dimensionPixelSize = gameMenuActivity.getResources().getDimensionPixelSize(i2);
                Rect rect = new Rect();
                view3.getHitRect(rect);
                rect.top -= dimensionPixelSize;
                rect.bottom += dimensionPixelSize;
                rect.left -= dimensionPixelSize;
                rect.right += dimensionPixelSize;
                view4.setTouchDelegate(new TouchDelegate(rect, view3));
            }
        });
        View findViewById2 = findViewById(2131428009);
        this.mSettingsButton = findViewById2;
        findViewById2.setOnClickListener(new PipMenuView$$ExternalSyntheticLambda2(this, 2));
        final View view3 = this.mSettingsButton;
        final View view4 = (View) view3.getParent();
        view4.post(new Runnable() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                GameMenuActivity gameMenuActivity = GameMenuActivity.this;
                int i2 = r2;
                View view32 = view3;
                View view42 = view4;
                IntentFilter intentFilter = GameMenuActivity.DND_FILTER;
                Objects.requireNonNull(gameMenuActivity);
                int dimensionPixelSize = gameMenuActivity.getResources().getDimensionPixelSize(i2);
                Rect rect = new Rect();
                view32.getHitRect(rect);
                rect.top -= dimensionPixelSize;
                rect.bottom += dimensionPixelSize;
                rect.left -= dimensionPixelSize;
                rect.right += dimensionPixelSize;
                view42.setTouchDelegate(new TouchDelegate(rect, view32));
            }
        });
        final View findViewById3 = findViewById(2131428003);
        findViewById3.setOnClickListener(new PipMenuView$$ExternalSyntheticLambda3(this, 3));
        final View view5 = (View) findViewById3.getParent();
        view5.post(new Runnable() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                GameMenuActivity gameMenuActivity = GameMenuActivity.this;
                int i2 = r2;
                View view32 = findViewById3;
                View view42 = view5;
                IntentFilter intentFilter = GameMenuActivity.DND_FILTER;
                Objects.requireNonNull(gameMenuActivity);
                int dimensionPixelSize = gameMenuActivity.getResources().getDimensionPixelSize(i2);
                Rect rect = new Rect();
                view32.getHitRect(rect);
                rect.top -= dimensionPixelSize;
                rect.bottom += dimensionPixelSize;
                rect.left -= dimensionPixelSize;
                rect.right += dimensionPixelSize;
                view42.setTouchDelegate(new TouchDelegate(rect, view32));
            }
        });
        this.mGameMenuMainView = findViewById(2131428006);
        GameDashboardButton gameDashboardButton = (GameDashboardButton) findViewById(2131428008);
        gameDashboardButton.setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda5(this, gameDashboardButton, 1));
        ShortcutBarController shortcutBarController = this.mShortcutBarController;
        Objects.requireNonNull(shortcutBarController);
        ShortcutBarView shortcutBarView = shortcutBarController.mView;
        Objects.requireNonNull(shortcutBarView);
        gameDashboardButton.setToggled(shortcutBarView.mIsScreenshotVisible, false);
        GameDashboardButton gameDashboardButton2 = (GameDashboardButton) findViewById(2131428007);
        gameDashboardButton2.setOnClickListener(new GameMenuActivity$$ExternalSyntheticLambda2(this, gameDashboardButton2, 0));
        ShortcutBarController shortcutBarController2 = this.mShortcutBarController;
        Objects.requireNonNull(shortcutBarController2);
        ShortcutBarView shortcutBarView2 = shortcutBarController2.mView;
        Objects.requireNonNull(shortcutBarView2);
        gameDashboardButton2.setToggled(shortcutBarView2.mIsRecordVisible, false);
        final GameDashboardButton gameDashboardButton3 = (GameDashboardButton) findViewById(2131428005);
        gameDashboardButton3.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view6) {
                GameDashboardUiEventLogger.GameDashboardEvent gameDashboardEvent;
                int i2;
                int i3;
                GameMenuActivity gameMenuActivity = GameMenuActivity.this;
                GameDashboardButton gameDashboardButton4 = gameDashboardButton3;
                IntentFilter intentFilter = GameMenuActivity.DND_FILTER;
                Objects.requireNonNull(gameMenuActivity);
                Objects.requireNonNull(gameDashboardButton4);
                boolean z2 = !gameDashboardButton4.mToggled;
                ShortcutBarController shortcutBarController3 = gameMenuActivity.mShortcutBarController;
                Objects.requireNonNull(shortcutBarController3);
                if (z2) {
                    shortcutBarController3.mToast.showShortcutText(2131952375);
                }
                ShortcutBarView shortcutBarView3 = shortcutBarController3.mView;
                Objects.requireNonNull(shortcutBarView3);
                GameDashboardUiEventLogger gameDashboardUiEventLogger = shortcutBarView3.mUiEventLogger;
                if (z2) {
                    gameDashboardEvent = GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_MENU_ENABLE_FPS;
                } else {
                    gameDashboardEvent = GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_MENU_DISABLE_FPS;
                }
                gameDashboardUiEventLogger.log(gameDashboardEvent);
                shortcutBarView3.mIsFpsVisible = z2;
                TextView textView = shortcutBarView3.mFpsView;
                if (z2) {
                    i2 = 0;
                } else {
                    i2 = 8;
                }
                textView.setVisibility(i2);
                if (shortcutBarView3.mRevealButton.getVisibility() == 0 && shortcutBarView3.mIsAttached) {
                    shortcutBarView3.mRevealButton.bounce(z2);
                }
                shortcutBarController3.onButtonVisibilityChange(z2);
                if (z2) {
                    ShortcutBarController shortcutBarController4 = gameMenuActivity.mShortcutBarController;
                    EntryPointController entryPointController2 = gameMenuActivity.mController;
                    Objects.requireNonNull(entryPointController2);
                    ActivityManager.RunningTaskInfo runningTaskInfo = entryPointController2.mGameTaskInfo;
                    if (runningTaskInfo == null) {
                        i3 = -1;
                    } else {
                        i3 = runningTaskInfo.taskId;
                    }
                    shortcutBarController4.registerFps(i3);
                    return;
                }
                ShortcutBarController shortcutBarController5 = gameMenuActivity.mShortcutBarController;
                Objects.requireNonNull(shortcutBarController5);
                FpsController fpsController = shortcutBarController5.mFpsController;
                Objects.requireNonNull(fpsController);
                fpsController.mWindowManager.unregisterTaskFpsCallback(fpsController.mListener);
                if (fpsController.mCallback != null) {
                    fpsController.mCallback = null;
                }
            }
        });
        ShortcutBarController shortcutBarController3 = this.mShortcutBarController;
        Objects.requireNonNull(shortcutBarController3);
        ShortcutBarView shortcutBarView3 = shortcutBarController3.mView;
        Objects.requireNonNull(shortcutBarView3);
        gameDashboardButton3.setToggled(shortcutBarView3.mIsFpsVisible, false);
        this.mContext.registerReceiver(this.mDndReceiver, DND_FILTER);
        this.mGameModeView = findViewById(2131428016);
        this.mPerformanceModeRadioButton = (RadioButton) findViewById(2131428581);
        if (this.mAvailableModes.contains(2)) {
            this.mPerformanceModeRadioButton.setOnClickListener(new AvatarPickerActivity$$ExternalSyntheticLambda1(this, 3));
            this.mPerformanceModeRadioButton.setEnabled(true);
            findViewById(2131428580).setOnClickListener(new InternetDialog$$ExternalSyntheticLambda3(this, 4));
        } else {
            this.mPerformanceModeRadioButton.setEnabled(false);
        }
        RadioButton radioButton = (RadioButton) findViewById(2131428914);
        this.mStandardModeRadioButton = radioButton;
        radioButton.setOnClickListener(new PipMenuView$$ExternalSyntheticLambda1(this, 4));
        this.mBatteryModeRadioButton = (RadioButton) findViewById(2131427574);
        this.mBatteryModeRadioButton = (RadioButton) findViewById(2131427574);
        if (this.mAvailableModes.contains(3)) {
            this.mBatteryModeRadioButton.setOnClickListener(new GameMenuActivity$$ExternalSyntheticLambda1(this, 0));
            findViewById(2131427573).setOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public final void onClick(View view6) {
                    GameMenuActivity gameMenuActivity = GameMenuActivity.this;
                    IntentFilter intentFilter = GameMenuActivity.DND_FILTER;
                    Objects.requireNonNull(gameMenuActivity);
                    gameMenuActivity.onGameModeSelectionChanged(view6);
                }
            });
            this.mBatteryModeRadioButton.setEnabled(true);
        } else {
            this.mBatteryModeRadioButton.setEnabled(false);
        }
        this.mDndButton = (GameDashboardButton) findViewById(2131428004);
        findViewById(2131428913).setOnClickListener(new ScreenshotView$$ExternalSyntheticLambda9(this, 2));
        this.mCurrentView = this.mGameMenuMainView;
        this.mWidgetsView = (LinearLayout) findViewById(2131428014);
        if (this.mContext.getResources().getConfiguration().orientation != 2) {
            this.mMaxWidgetsPerContainer = 2;
        } else {
            this.mMaxWidgetsPerContainer = 3;
        }
        WidgetContainer widgetContainer = (WidgetContainer) this.mLayoutInflater.inflate(2131624112, (ViewGroup) this.mWidgetsView, false);
        this.mCurrentWidgetContainer = widgetContainer;
        WidgetView widgetView = (WidgetView) this.mLayoutInflater.inflate(2131624111, (ViewGroup) widgetContainer, false);
        this.mGameModeWidget = widgetView;
        this.mCurrentWidgetContainer.addWidget(widgetView);
        WidgetContainer widgetContainer2 = this.mCurrentWidgetContainer;
        GameDashboardUiEventLogger gameDashboardUiEventLogger = this.mUiEventLogger;
        WidgetView widgetView2 = (WidgetView) LayoutInflater.from(this).inflate(2131624111, (ViewGroup) widgetContainer2, false);
        this.mYouTubeLiveWidget = new YouTubeLiveWidget(this, widgetView2, gameDashboardUiEventLogger);
        this.mCurrentWidgetContainer.addWidget(widgetView2);
        WidgetContainer widgetContainer3 = this.mCurrentWidgetContainer;
        Objects.requireNonNull(widgetContainer3);
        if (widgetContainer3.mWidgetCount == this.mMaxWidgetsPerContainer) {
            this.mWidgetsView.addView(this.mCurrentWidgetContainer);
            this.mCurrentWidgetContainer = (WidgetContainer) this.mLayoutInflater.inflate(2131624112, (ViewGroup) this.mWidgetsView, false);
        }
        WidgetContainer widgetContainer4 = this.mCurrentWidgetContainer;
        Handler handler = this.mMainHandler;
        GameDashboardUiEventLogger gameDashboardUiEventLogger2 = this.mUiEventLogger;
        try {
            for (ProviderInfo providerInfo : getPackageManager().getPackageInfo("com.google.android.play.games", 8).providers) {
                if (providerInfo.authority.equals("com.google.android.play.games.dashboard.tile.provider") && providerInfo.enabled) {
                    z = true;
                    break;
                }
            }
        } catch (PackageManager.NameNotFoundException unused) {
            Log.v("PlayGamesWidget", "Play Games package not found.");
        }
        z = false;
        if (!z) {
            playGamesWidget = null;
        } else {
            playGamesWidget = new PlayGamesWidget(this, (WidgetView) LayoutInflater.from(this).inflate(2131624111, (ViewGroup) widgetContainer4, false), handler, gameDashboardUiEventLogger2);
        }
        this.mPlayGamesWidget = playGamesWidget;
        if (playGamesWidget != null) {
            this.mCurrentWidgetContainer.addWidget(playGamesWidget.mWidgetView);
        }
        WidgetContainer widgetContainer5 = this.mCurrentWidgetContainer;
        Objects.requireNonNull(widgetContainer5);
        if (widgetContainer5.mWidgetCount == 1 && this.mMaxWidgetsPerContainer == 2) {
            while (true) {
                WidgetContainer widgetContainer6 = this.mCurrentWidgetContainer;
                Objects.requireNonNull(widgetContainer6);
                if (widgetContainer6.mWidgetCount >= this.mMaxWidgetsPerContainer) {
                    break;
                }
                WidgetView widgetView3 = (WidgetView) LayoutInflater.from(this).inflate(2131624111, (ViewGroup) this.mCurrentWidgetContainer, false);
                widgetView3.setVisibility(4);
                this.mCurrentWidgetContainer.addWidget(widgetView3);
            }
        }
        this.mWidgetsView.addView(this.mCurrentWidgetContainer);
    }

    @Override // android.app.Activity
    public final void onDestroy() {
        super.onDestroy();
        this.mContext.unregisterReceiver(this.mDndReceiver);
    }

    public final void onGameModeSelectionChanged(View view) {
        final int i;
        int id = view.getId();
        if (id == 2131428580 || id == 2131428581) {
            this.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_GAME_MODE_PERFORMANCE);
            i = 2;
        } else if (id == 2131428913 || id == 2131428914) {
            this.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_GAME_MODE_STANDARD);
            i = 1;
        } else if (id == 2131427573 || id == 2131427574) {
            this.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_GAME_MODE_BATTERY);
            i = 3;
        } else {
            i = 0;
        }
        EntryPointController entryPointController = this.mController;
        Objects.requireNonNull(entryPointController);
        final String str = entryPointController.mGamePackageName;
        int gameMode = this.mGameManager.getGameMode(str);
        if (gameMode != i) {
            toggleGameModeRadioButtons(gameMode);
            SystemUIDialog systemUIDialog = new SystemUIDialog(this);
            systemUIDialog.setTitle(2131952397);
            if (i == 1) {
                systemUIDialog.setMessage(2131952396);
            } else if (i == 2) {
                systemUIDialog.setMessage(2131952395);
            } else if (i == 3) {
                systemUIDialog.setMessage(2131952394);
            }
            systemUIDialog.setPositiveButton(2131952393, new DialogInterface.OnClickListener() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i2) {
                    GameMenuActivity gameMenuActivity = GameMenuActivity.this;
                    int i3 = i;
                    String str2 = str;
                    IntentFilter intentFilter = GameMenuActivity.DND_FILTER;
                    Objects.requireNonNull(gameMenuActivity);
                    gameMenuActivity.toggleGameModeRadioButtons(i3);
                    gameMenuActivity.mGameManager.setGameMode(str2, i3);
                    gameMenuActivity.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_RESTART_NOW);
                    try {
                        EntryPointController entryPointController2 = gameMenuActivity.mController;
                        Objects.requireNonNull(entryPointController2);
                        String str3 = entryPointController2.mGamePackageName;
                        IActivityManager service = ActivityManager.getService();
                        EntryPointController entryPointController3 = gameMenuActivity.mController;
                        Objects.requireNonNull(entryPointController3);
                        service.forceStopPackage(entryPointController3.mGamePackageName, -2);
                        gameMenuActivity.mActivityStarter.startActivity(gameMenuActivity.getPackageManager().getLaunchIntentForPackage(str3), true);
                    } catch (RemoteException e) {
                        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Fail to restart the game: ");
                        m.append(e.getMessage());
                        Log.e("GameMenuActivity", m.toString());
                    }
                    gameMenuActivity.finish();
                }
            });
            systemUIDialog.setNegativeButton(2131952392, new InputMethodPreference$$ExternalSyntheticLambda1(this, 1));
            systemUIDialog.show();
        }
    }

    @Override // android.app.Activity
    public final void onResume() {
        boolean z;
        super.onResume();
        GameModeDndController gameModeDndController = this.mDndController;
        Objects.requireNonNull(gameModeDndController);
        AutomaticZenRule fetchRule = gameModeDndController.fetchRule();
        if (fetchRule == null || fetchRule.getInterruptionFilter() != 2) {
            z = false;
        } else {
            z = true;
        }
        gameModeDndController.mFilterActive = z;
        gameModeDndController.mGameActiveOld = gameModeDndController.mGameActive;
        gameModeDndController.mUserActiveOld = gameModeDndController.mUserActive;
        gameModeDndController.mFilterActiveOld = z;
        this.mDndButton.setToggled(this.mDndController.isGameModeDndOn(), false);
        this.mDndButton.setOnClickListener(new RemoteInputView$$ExternalSyntheticLambda0(this, 2));
        updateWidgets();
    }
}
