package com.android.systemui.qs.tiles;

import android.app.UiModeManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Switch;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.LocationController;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
/* loaded from: classes.dex */
public final class UiModeNightTile extends QSTileImpl<QSTile.BooleanState> implements ConfigurationController.ConfigurationListener, BatteryController.BatteryStateChangeCallback {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
    public final BatteryController mBatteryController;
    public final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(17302828);
    public final LocationController mLocationController;
    public UiModeManager mUiModeManager;

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final int getMetricsCategory() {
        return 1706;
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public final void onPowerSaveChanged(boolean z) {
        refreshState(null);
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onUiModeChanged() {
        refreshState(null);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        return new Intent("android.settings.DARK_THEME_SETTINGS");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return ((QSTile.BooleanState) this.mState).label;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleClick(View view) {
        TState tstate = this.mState;
        if (((QSTile.BooleanState) tstate).state != 0) {
            boolean z = !((QSTile.BooleanState) tstate).value;
            this.mUiModeManager.setNightModeActivated(z);
            refreshState(Boolean.valueOf(z));
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        boolean z;
        CharSequence charSequence;
        int i;
        LocalTime localTime;
        int i2;
        String str;
        int i3;
        QSTile.BooleanState booleanState2 = booleanState;
        int nightMode = this.mUiModeManager.getNightMode();
        boolean isPowerSave = this.mBatteryController.isPowerSave();
        int i4 = 1;
        if ((this.mContext.getResources().getConfiguration().uiMode & 48) == 32) {
            z = true;
        } else {
            z = false;
        }
        if (isPowerSave) {
            booleanState2.secondaryLabel = this.mContext.getResources().getString(2131953083);
        } else if (nightMode == 0 && this.mLocationController.isLocationEnabled()) {
            Resources resources = this.mContext.getResources();
            if (z) {
                i3 = 2131953089;
            } else {
                i3 = 2131953086;
            }
            booleanState2.secondaryLabel = resources.getString(i3);
        } else if (nightMode == 3) {
            int nightModeCustomType = this.mUiModeManager.getNightModeCustomType();
            if (nightModeCustomType == 0) {
                boolean is24HourFormat = DateFormat.is24HourFormat(this.mContext);
                if (z) {
                    localTime = this.mUiModeManager.getCustomNightModeEnd();
                } else {
                    localTime = this.mUiModeManager.getCustomNightModeStart();
                }
                Resources resources2 = this.mContext.getResources();
                if (z) {
                    i2 = 2131953087;
                } else {
                    i2 = 2131953084;
                }
                Object[] objArr = new Object[1];
                if (is24HourFormat) {
                    str = localTime.toString();
                } else {
                    str = formatter.format(localTime);
                }
                objArr[0] = str;
                booleanState2.secondaryLabel = resources2.getString(i2, objArr);
            } else if (nightModeCustomType == 1) {
                Resources resources3 = this.mContext.getResources();
                if (z) {
                    i = 2131953088;
                } else {
                    i = 2131953085;
                }
                booleanState2.secondaryLabel = resources3.getString(i);
            } else {
                booleanState2.secondaryLabel = null;
            }
        } else {
            booleanState2.secondaryLabel = null;
        }
        booleanState2.value = z;
        booleanState2.label = this.mContext.getString(2131953142);
        booleanState2.icon = this.mIcon;
        if (TextUtils.isEmpty(booleanState2.secondaryLabel)) {
            charSequence = booleanState2.label;
        } else {
            charSequence = TextUtils.concat(booleanState2.label, ", ", booleanState2.secondaryLabel);
        }
        booleanState2.contentDescription = charSequence;
        if (isPowerSave) {
            booleanState2.state = 0;
        } else {
            if (booleanState2.value) {
                i4 = 2;
            }
            booleanState2.state = i4;
        }
        booleanState2.showRippleEffect = false;
        booleanState2.expandedAccessibilityClassName = Switch.class.getName();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public UiModeNightTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, ConfigurationController configurationController, BatteryController batteryController, LocationController locationController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mBatteryController = batteryController;
        this.mUiModeManager = (UiModeManager) qSHost.getUserContext().getSystemService(UiModeManager.class);
        this.mLocationController = locationController;
        configurationController.observe((Lifecycle) this.mLifecycle, (LifecycleRegistry) this);
        batteryController.observe((Lifecycle) this.mLifecycle, (LifecycleRegistry) this);
    }
}
