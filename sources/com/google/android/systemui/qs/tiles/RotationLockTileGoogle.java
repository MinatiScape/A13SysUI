package com.google.android.systemui.qs.tiles;

import android.hardware.SensorPrivacyManager;
import android.os.Handler;
import android.os.Looper;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tiles.RotationLockTile;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.statusbar.policy.RotationLockController;
import com.android.systemui.util.settings.SecureSettings;
/* compiled from: RotationLockTileGoogle.kt */
/* loaded from: classes.dex */
public final class RotationLockTileGoogle extends RotationLockTile {
    public final DevicePostureController mDevicePostureController;
    public final boolean mIsPerDeviceStateRotationLockEnabled;

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.systemui.qs.tiles.RotationLockTile
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        super.handleUpdateState(booleanState, obj);
        if (this.mIsPerDeviceStateRotationLockEnabled) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.mContext.getResources().getStringArray(2130903158)[booleanState.state]);
            sb.append(" / ");
            if (this.mDevicePostureController.getDevicePosture() == 1) {
                sb.append(this.mContext.getString(2131953131));
            } else {
                sb.append(this.mContext.getString(2131953132));
            }
            String sb2 = sb.toString();
            booleanState.secondaryLabel = sb2;
            booleanState.stateDescription = sb2;
        }
    }

    public RotationLockTileGoogle(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, RotationLockController rotationLockController, SensorPrivacyManager sensorPrivacyManager, BatteryController batteryController, SecureSettings secureSettings, String[] strArr, DevicePostureController devicePostureController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger, rotationLockController, sensorPrivacyManager, batteryController, secureSettings);
        boolean z;
        this.mDevicePostureController = devicePostureController;
        if (strArr.length == 0) {
            z = true;
        } else {
            z = false;
        }
        this.mIsPerDeviceStateRotationLockEnabled = !z;
    }
}
