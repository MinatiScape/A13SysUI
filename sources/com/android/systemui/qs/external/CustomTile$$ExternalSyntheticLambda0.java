package com.android.systemui.qs.external;

import android.app.WallpaperColors;
import android.service.quicksettings.Tile;
import android.util.Log;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.theme.ThemeOverlayController;
import com.android.systemui.wmshell.BubblesManager;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class CustomTile$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ CustomTile$$ExternalSyntheticLambda0(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                CustomTile customTile = (CustomTile) this.f$0;
                Tile tile = (Tile) this.f$1;
                Objects.requireNonNull(customTile);
                customTile.applyTileState(tile, true);
                if (customTile.mServiceManager.isActiveTile()) {
                    CustomTileStatePersister customTileStatePersister = customTile.mCustomTileStatePersister;
                    TileServiceKey tileServiceKey = customTile.mKey;
                    Objects.requireNonNull(customTileStatePersister);
                    customTileStatePersister.sharedPreferences.edit().putString(tileServiceKey.string, CustomTileStatePersisterKt.writeToString(tile)).apply();
                    return;
                }
                return;
            case 1:
                ThemeOverlayController themeOverlayController = (ThemeOverlayController) this.f$0;
                WallpaperColors wallpaperColors = (WallpaperColors) this.f$1;
                Objects.requireNonNull(themeOverlayController);
                Log.d("ThemeOverlayController", "Boot colors: " + wallpaperColors);
                themeOverlayController.mCurrentColors.put(themeOverlayController.mUserTracker.getUserId(), wallpaperColors);
                themeOverlayController.reevaluateSystemTheme(false);
                return;
            default:
                BubblesManager.AnonymousClass5 r0 = (BubblesManager.AnonymousClass5) this.f$0;
                Objects.requireNonNull(r0);
                NotificationEntry entry = BubblesManager.this.mCommonNotifCollection.getEntry((String) this.f$1);
                if (entry != null) {
                    NotificationGroupManagerLegacy notificationGroupManagerLegacy = BubblesManager.this.mNotificationGroupManager;
                    Objects.requireNonNull(notificationGroupManagerLegacy);
                    NotificationGroupManagerLegacy.NotificationGroup notificationGroup = notificationGroupManagerLegacy.mGroupMap.get(notificationGroupManagerLegacy.getGroupKey(entry.mSbn));
                    if (notificationGroup != null) {
                        notificationGroupManagerLegacy.updateSuppression(notificationGroup);
                        return;
                    }
                    return;
                }
                return;
        }
    }
}
