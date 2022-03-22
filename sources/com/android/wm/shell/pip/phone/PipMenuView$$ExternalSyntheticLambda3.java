package com.android.wm.shell.pip.phone;

import android.app.PendingIntent;
import android.content.IntentFilter;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.media.RoutingSessionInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.android.settingslib.media.InfoMediaManager;
import com.android.settingslib.media.LocalMediaManager;
import com.android.systemui.media.dialog.MediaOutputBaseDialog;
import com.android.systemui.media.dialog.MediaOutputController;
import com.android.systemui.recents.OverviewProxyService$$ExternalSyntheticLambda6;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.google.android.systemui.assist.uihints.ChipView;
import com.google.android.systemui.gamedashboard.GameDashboardUiEventLogger;
import com.google.android.systemui.gamedashboard.GameMenuActivity;
import com.google.android.systemui.gamedashboard.ShortcutBarController;
import com.google.android.systemui.gamedashboard.ShortcutBarView;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PipMenuView$$ExternalSyntheticLambda3 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ PipMenuView$$ExternalSyntheticLambda3(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                PipMenuView pipMenuView = (PipMenuView) this.f$0;
                Objects.requireNonNull(pipMenuView);
                if (pipMenuView.mMenuState != 0) {
                    PhonePipMenuController phonePipMenuController = pipMenuView.mController;
                    Objects.requireNonNull(phonePipMenuController);
                    phonePipMenuController.mListeners.forEach(OverviewProxyService$$ExternalSyntheticLambda6.INSTANCE$2);
                    pipMenuView.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_TAP_TO_REMOVE);
                    return;
                }
                return;
            case 1:
                MediaOutputBaseDialog mediaOutputBaseDialog = (MediaOutputBaseDialog) this.f$0;
                int i = MediaOutputBaseDialog.$r8$clinit;
                Objects.requireNonNull(mediaOutputBaseDialog);
                MediaOutputController mediaOutputController = mediaOutputBaseDialog.mMediaOutputController;
                Objects.requireNonNull(mediaOutputController);
                LocalMediaManager localMediaManager = mediaOutputController.mLocalMediaManager;
                Objects.requireNonNull(localMediaManager);
                InfoMediaManager infoMediaManager = localMediaManager.mInfoMediaManager;
                Objects.requireNonNull(infoMediaManager);
                if (TextUtils.isEmpty(infoMediaManager.mPackageName)) {
                    Log.w("InfoMediaManager", "releaseSession() package name is null or empty!");
                } else {
                    RoutingSessionInfo routingSessionInfo = infoMediaManager.getRoutingSessionInfo();
                    if (routingSessionInfo != null) {
                        infoMediaManager.mRouterManager.releaseSession(routingSessionInfo);
                    } else {
                        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("releaseSession() Ignoring release session : ");
                        m.append(infoMediaManager.mPackageName);
                        Log.w("InfoMediaManager", m.toString());
                    }
                }
                mediaOutputBaseDialog.dismiss();
                return;
            case 2:
                PendingIntent pendingIntent = (PendingIntent) this.f$0;
                int i2 = ChipView.$r8$clinit;
                try {
                    pendingIntent.send();
                    return;
                } catch (PendingIntent.CanceledException e) {
                    Log.w("ChipView", "Pending intent cancelled", e);
                    return;
                }
            default:
                GameMenuActivity gameMenuActivity = (GameMenuActivity) this.f$0;
                IntentFilter intentFilter = GameMenuActivity.DND_FILTER;
                Objects.requireNonNull(gameMenuActivity);
                gameMenuActivity.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_MENU_CLOSE);
                ShortcutBarController shortcutBarController = gameMenuActivity.mShortcutBarController;
                Objects.requireNonNull(shortcutBarController);
                if (shortcutBarController.mIsAttached) {
                    ShortcutBarView shortcutBarView = shortcutBarController.mView;
                    Objects.requireNonNull(shortcutBarView);
                    shortcutBarView.autoUndock(0.0f);
                }
                gameMenuActivity.finish();
                return;
        }
    }
}
