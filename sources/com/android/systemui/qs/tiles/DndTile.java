package com.android.systemui.qs.tiles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.service.notification.ZenModeConfig;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.qs.tiles.dialog.QSZenModeDialogMetricsLogger;
import com.android.systemui.statusbar.policy.ZenModeController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DndTile extends QSTileImpl<QSTile.BooleanState> {
    public static final Intent ZEN_SETTINGS = new Intent("android.settings.ZEN_MODE_SETTINGS");
    public final ZenModeController mController;
    public final DialogLaunchAnimator mDialogLaunchAnimator;
    public boolean mListening;
    public final AnonymousClass2 mPrefListener = new SharedPreferences.OnSharedPreferenceChangeListener() { // from class: com.android.systemui.qs.tiles.DndTile.2
        @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
        public final void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
            if ("DndTileCombinedIcon".equals(str) || "DndTileVisible".equals(str)) {
                DndTile dndTile = DndTile.this;
                Objects.requireNonNull(dndTile);
                dndTile.refreshState(null);
            }
        }
    };
    public final QSZenModeDialogMetricsLogger mQSZenDialogMetricsLogger = new QSZenModeDialogMetricsLogger(this.mContext);
    public final AnonymousClass1 mSettingZenDuration;
    public final SharedPreferences mSharedPreferences;
    public final AnonymousClass3 mZenCallback;

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final int getMetricsCategory() {
        return 118;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUserSwitch(int i) {
        handleRefreshState(null);
        setUserId(i);
    }

    static {
        new Intent("android.settings.ZEN_MODE_PRIORITY_SETTINGS");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return this.mContext.getString(2131953107);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleClick(View view) {
        boolean z;
        if (((QSTile.BooleanState) this.mState).value) {
            this.mController.setZen(0, null, this.TAG);
            return;
        }
        int value = getValue();
        if (Settings.Secure.getInt(this.mContext.getContentResolver(), "show_zen_upgrade_notification", 0) == 0 || Settings.Secure.getInt(this.mContext.getContentResolver(), "zen_settings_updated", 0) == 1) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            Settings.Secure.putInt(this.mContext.getContentResolver(), "show_zen_upgrade_notification", 0);
            this.mController.setZen(1, null, this.TAG);
            Intent intent = new Intent("android.settings.ZEN_MODE_ONBOARDING");
            intent.addFlags(268468224);
            this.mActivityStarter.postStartActivityDismissingKeyguard(intent, 0);
        } else if (value == -1) {
            this.mUiHandler.post(new DndTile$$ExternalSyntheticLambda0(this, view, 0));
        } else if (value != 0) {
            this.mController.setZen(1, ZenModeConfig.toTimeCondition(this.mContext, value, this.mHost.getUserId(), true).id, this.TAG);
        } else {
            this.mController.setZen(1, null, this.TAG);
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        int i;
        boolean z;
        int i2;
        boolean z2;
        QSTile.BooleanState booleanState2 = booleanState;
        ZenModeController zenModeController = this.mController;
        if (zenModeController != null) {
            if (obj instanceof Integer) {
                i = ((Integer) obj).intValue();
            } else {
                i = zenModeController.getZen();
            }
            boolean z3 = false;
            if (i != 0) {
                z = true;
            } else {
                z = false;
            }
            boolean z4 = booleanState2.value;
            if (booleanState2.slash == null) {
                booleanState2.slash = new QSTile.SlashState();
            }
            booleanState2.dualTarget = true;
            booleanState2.value = z;
            if (z) {
                i2 = 2;
            } else {
                i2 = 1;
            }
            booleanState2.state = i2;
            booleanState2.slash.isSlashed = !z;
            booleanState2.label = getTileLabel();
            Context context = this.mContext;
            if (i != 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            booleanState2.secondaryLabel = TextUtils.emptyIfNull(ZenModeConfig.getDescription(context, z2, this.mController.getConfig(), false));
            booleanState2.icon = QSTileImpl.ResourceIcon.get(17302824);
            checkIfRestrictionEnforcedByAdminOnly(booleanState2, "no_adjust_volume");
            if (i == 1) {
                booleanState2.contentDescription = this.mContext.getString(2131951788) + ", " + ((Object) booleanState2.secondaryLabel);
            } else if (i == 2) {
                booleanState2.contentDescription = this.mContext.getString(2131951788) + ", " + this.mContext.getString(2131951790) + ", " + ((Object) booleanState2.secondaryLabel);
            } else if (i != 3) {
                booleanState2.contentDescription = this.mContext.getString(2131951788);
            } else {
                booleanState2.contentDescription = this.mContext.getString(2131951788) + ", " + this.mContext.getString(2131951789) + ", " + ((Object) booleanState2.secondaryLabel);
            }
            booleanState2.dualLabelContentDescription = this.mContext.getResources().getString(2131951795, getTileLabel());
            booleanState2.expandedAccessibilityClassName = Switch.class.getName();
            if (getValue() == -1) {
                z3 = true;
            }
            booleanState2.forceExpandIcon = z3;
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final boolean isAvailable() {
        return this.mSharedPreferences.getBoolean("DndTileVisible", false);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.qs.tiles.DndTile$2] */
    /* JADX WARN: Type inference failed for: r1v2, types: [java.lang.Object, com.android.systemui.qs.tiles.DndTile$3] */
    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.qs.tiles.DndTile$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public DndTile(com.android.systemui.qs.QSHost r1, android.os.Looper r2, android.os.Handler r3, com.android.systemui.plugins.FalsingManager r4, com.android.internal.logging.MetricsLogger r5, com.android.systemui.plugins.statusbar.StatusBarStateController r6, com.android.systemui.plugins.ActivityStarter r7, com.android.systemui.qs.logging.QSLogger r8, com.android.systemui.statusbar.policy.ZenModeController r9, android.content.SharedPreferences r10, com.android.systemui.util.settings.SecureSettings r11, com.android.systemui.animation.DialogLaunchAnimator r12) {
        /*
            r0 = this;
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            com.android.systemui.qs.tiles.DndTile$2 r1 = new com.android.systemui.qs.tiles.DndTile$2
            r1.<init>()
            r0.mPrefListener = r1
            com.android.systemui.qs.tiles.DndTile$3 r1 = new com.android.systemui.qs.tiles.DndTile$3
            r1.<init>()
            r0.mZenCallback = r1
            r0.mController = r9
            r0.mSharedPreferences = r10
            androidx.lifecycle.LifecycleRegistry r2 = r0.mLifecycle
            r9.observe(r2, r1)
            r0.mDialogLaunchAnimator = r12
            com.android.systemui.qs.tiles.DndTile$1 r1 = new com.android.systemui.qs.tiles.DndTile$1
            android.os.Handler r2 = r0.mUiHandler
            com.android.systemui.qs.QSHost r3 = r0.mHost
            int r3 = r3.getUserId()
            r1.<init>(r11, r2, r3)
            r0.mSettingZenDuration = r1
            com.android.systemui.qs.tiles.dialog.QSZenModeDialogMetricsLogger r1 = new com.android.systemui.qs.tiles.dialog.QSZenModeDialogMetricsLogger
            android.content.Context r2 = r0.mContext
            r1.<init>(r2)
            r0.mQSZenDialogMetricsLogger = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.tiles.DndTile.<init>(com.android.systemui.qs.QSHost, android.os.Looper, android.os.Handler, com.android.systemui.plugins.FalsingManager, com.android.internal.logging.MetricsLogger, com.android.systemui.plugins.statusbar.StatusBarStateController, com.android.systemui.plugins.ActivityStarter, com.android.systemui.qs.logging.QSLogger, com.android.systemui.statusbar.policy.ZenModeController, android.content.SharedPreferences, com.android.systemui.util.settings.SecureSettings, com.android.systemui.animation.DialogLaunchAnimator):void");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleDestroy() {
        super.handleDestroy();
        setListening(false);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleSetListening(boolean z) {
        super.handleSetListening(z);
        if (this.mListening != z) {
            this.mListening = z;
            if (z) {
                Context context = this.mContext;
                context.getSharedPreferences(context.getPackageName(), 0).registerOnSharedPreferenceChangeListener(this.mPrefListener);
            } else {
                Context context2 = this.mContext;
                context2.getSharedPreferences(context2.getPackageName(), 0).unregisterOnSharedPreferenceChangeListener(this.mPrefListener);
            }
            setListening(z);
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleSecondaryClick(View view) {
        handleLongClick(view);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        return ZEN_SETTINGS;
    }
}
