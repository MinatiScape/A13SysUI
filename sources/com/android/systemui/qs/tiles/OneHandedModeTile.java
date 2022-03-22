package com.android.systemui.qs.tiles;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.SettingObserver;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.settings.SecureSettings;
import com.android.wm.shell.onehanded.OneHanded;
import java.util.Objects;
/* loaded from: classes.dex */
public final class OneHandedModeTile extends QSTileImpl<QSTile.BooleanState> {
    public final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(17302827);
    public final AnonymousClass1 mSetting;

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final int getMetricsCategory() {
        return 0;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        return new Intent("android.settings.action.ONE_HANDED_SETTINGS");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return this.mContext.getString(2131953130);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleClick(View view) {
        AnonymousClass1 r3 = this.mSetting;
        int i = !((QSTile.BooleanState) this.mState).value ? 1 : 0;
        Objects.requireNonNull(r3);
        r3.mSettingsProxy.putIntForUser(r3.mSettingName, i, r3.mUserId);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        int i;
        boolean z;
        QSTile.BooleanState booleanState2 = booleanState;
        if (obj instanceof Integer) {
            i = ((Integer) obj).intValue();
        } else {
            i = getValue();
        }
        int i2 = 1;
        if (i != 0) {
            z = true;
        } else {
            z = false;
        }
        booleanState2.value = z;
        booleanState2.label = this.mContext.getString(2131953130);
        booleanState2.icon = this.mIcon;
        if (booleanState2.slash == null) {
            booleanState2.slash = new QSTile.SlashState();
        }
        QSTile.SlashState slashState = booleanState2.slash;
        boolean z2 = booleanState2.value;
        slashState.isSlashed = !z2;
        if (z2) {
            i2 = 2;
        }
        booleanState2.state = i2;
        booleanState2.contentDescription = booleanState2.label;
        booleanState2.expandedAccessibilityClassName = Switch.class.getName();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUserSwitch(int i) {
        setUserId(i);
        handleRefreshState(Integer.valueOf(getValue()));
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.qs.tiles.OneHandedModeTile$1] */
    public OneHandedModeTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, UserTracker userTracker, SecureSettings secureSettings) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mSetting = new SettingObserver(secureSettings, this.mHandler, userTracker.getUserId()) { // from class: com.android.systemui.qs.tiles.OneHandedModeTile.1
            @Override // com.android.systemui.qs.SettingObserver
            public final void handleValueChanged(int i, boolean z) {
                OneHandedModeTile.this.handleRefreshState(Integer.valueOf(i));
            }
        };
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleDestroy() {
        super.handleDestroy();
        setListening(false);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleSetListening(boolean z) {
        super.handleSetListening(z);
        setListening(z);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final boolean isAvailable() {
        return isSupportOneHandedMode();
    }

    @VisibleForTesting
    public boolean isSupportOneHandedMode() {
        return OneHanded.sIsSupportOneHandedMode;
    }
}
