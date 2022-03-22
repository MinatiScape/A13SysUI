package com.android.systemui.qs.tiles;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.BluetoothController;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class BluetoothTile extends QSTileImpl<QSTile.BooleanState> {
    public final AnonymousClass1 mCallback;
    public final BluetoothController mController;

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final int getMetricsCategory() {
        return 113;
    }

    /* loaded from: classes.dex */
    public class BluetoothConnectedTileIcon extends QSTile.Icon {
        @Override // com.android.systemui.plugins.qs.QSTile.Icon
        public final Drawable getDrawable(Context context) {
            return context.getDrawable(2131231769);
        }
    }

    static {
        new Intent("android.settings.BLUETOOTH_SETTINGS");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        return new Intent("android.settings.BLUETOOTH_SETTINGS");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return this.mContext.getString(2131953055);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleClick(View view) {
        Object obj;
        boolean z = ((QSTile.BooleanState) this.mState).value;
        if (z) {
            obj = null;
        } else {
            obj = QSTileImpl.ARG_SHOW_TRANSIENT_ENABLING;
        }
        refreshState(obj);
        this.mController.setBluetoothEnabled(!z);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleSecondaryClick(View view) {
        if (!this.mController.canConfigBluetooth()) {
            this.mActivityStarter.postStartActivityDismissingKeyguard(new Intent("android.settings.BLUETOOTH_SETTINGS"), 0);
        } else if (!((QSTile.BooleanState) this.mState).value) {
            this.mController.setBluetoothEnabled(true);
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        boolean z;
        boolean z2;
        boolean z3;
        String str;
        boolean z4;
        QSTile.BooleanState booleanState2 = booleanState;
        if (obj == QSTileImpl.ARG_SHOW_TRANSIENT_ENABLING) {
            z = true;
        } else {
            z = false;
        }
        if (z || this.mController.isBluetoothEnabled()) {
            z2 = true;
        } else {
            z2 = false;
        }
        boolean isBluetoothConnected = this.mController.isBluetoothConnected();
        boolean isBluetoothConnecting = this.mController.isBluetoothConnecting();
        if (z || isBluetoothConnecting || this.mController.getBluetoothState() == 11) {
            z3 = true;
        } else {
            z3 = false;
        }
        booleanState2.isTransient = z3;
        booleanState2.dualTarget = true;
        booleanState2.value = z2;
        if (booleanState2.slash == null) {
            booleanState2.slash = new QSTile.SlashState();
        }
        booleanState2.slash.isSlashed = !z2;
        booleanState2.label = this.mContext.getString(2131953055);
        boolean z5 = booleanState2.isTransient;
        if (isBluetoothConnecting) {
            str = this.mContext.getString(2131953082);
        } else if (z5) {
            str = this.mContext.getString(2131953061);
        } else {
            ArrayList connectedDevices = this.mController.getConnectedDevices();
            if (z2 && isBluetoothConnected && !connectedDevices.isEmpty()) {
                if (connectedDevices.size() > 1) {
                    str = this.mContext.getResources().getQuantityString(2131820555, connectedDevices.size(), Integer.valueOf(connectedDevices.size()));
                } else {
                    CachedBluetoothDevice cachedBluetoothDevice = (CachedBluetoothDevice) connectedDevices.get(0);
                    Objects.requireNonNull(cachedBluetoothDevice);
                    int batteryLevel = cachedBluetoothDevice.mDevice.getBatteryLevel();
                    if (batteryLevel > -1) {
                        str = this.mContext.getString(2131953057, NumberFormat.getPercentInstance().format(batteryLevel / 100.0d));
                    } else {
                        BluetoothClass bluetoothClass = cachedBluetoothDevice.mDevice.getBluetoothClass();
                        if (bluetoothClass != null) {
                            if (cachedBluetoothDevice.mHiSyncId != 0) {
                                z4 = true;
                            } else {
                                z4 = false;
                            }
                            if (z4) {
                                str = this.mContext.getString(2131953059);
                            } else if (bluetoothClass.doesClassMatch(1)) {
                                str = this.mContext.getString(2131953056);
                            } else if (bluetoothClass.doesClassMatch(0)) {
                                str = this.mContext.getString(2131953058);
                            } else if (bluetoothClass.doesClassMatch(3)) {
                                str = this.mContext.getString(2131953060);
                            }
                        }
                    }
                }
            }
            str = null;
        }
        booleanState2.secondaryLabel = TextUtils.emptyIfNull(str);
        booleanState2.contentDescription = this.mContext.getString(2131951784);
        booleanState2.stateDescription = "";
        if (z2) {
            if (isBluetoothConnected) {
                booleanState2.icon = new BluetoothConnectedTileIcon();
                if (!TextUtils.isEmpty(this.mController.getConnectedDeviceName())) {
                    booleanState2.label = this.mController.getConnectedDeviceName();
                }
                booleanState2.stateDescription = this.mContext.getString(2131951682, booleanState2.label) + ", " + ((Object) booleanState2.secondaryLabel);
            } else if (booleanState2.isTransient) {
                booleanState2.icon = QSTileImpl.ResourceIcon.get(17302327);
                booleanState2.stateDescription = booleanState2.secondaryLabel;
            } else {
                booleanState2.icon = QSTileImpl.ResourceIcon.get(17302823);
                booleanState2.stateDescription = this.mContext.getString(2131951765);
            }
            booleanState2.state = 2;
        } else {
            booleanState2.icon = QSTileImpl.ResourceIcon.get(17302823);
            booleanState2.state = 1;
        }
        booleanState2.dualLabelContentDescription = this.mContext.getResources().getString(2131951795, getTileLabel());
        booleanState2.expandedAccessibilityClassName = Switch.class.getName();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final boolean isAvailable() {
        return this.mController.isBluetoothSupported();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.qs.tiles.BluetoothTile$1, java.lang.Object] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public BluetoothTile(com.android.systemui.qs.QSHost r1, android.os.Looper r2, android.os.Handler r3, com.android.systemui.plugins.FalsingManager r4, com.android.internal.logging.MetricsLogger r5, com.android.systemui.plugins.statusbar.StatusBarStateController r6, com.android.systemui.plugins.ActivityStarter r7, com.android.systemui.qs.logging.QSLogger r8, com.android.systemui.statusbar.policy.BluetoothController r9) {
        /*
            r0 = this;
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            com.android.systemui.qs.tiles.BluetoothTile$1 r1 = new com.android.systemui.qs.tiles.BluetoothTile$1
            r1.<init>()
            r0.mCallback = r1
            r0.mController = r9
            androidx.lifecycle.LifecycleRegistry r0 = r0.mLifecycle
            r9.observe(r0, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.tiles.BluetoothTile.<init>(com.android.systemui.qs.QSHost, android.os.Looper, android.os.Handler, com.android.systemui.plugins.FalsingManager, com.android.internal.logging.MetricsLogger, com.android.systemui.plugins.statusbar.StatusBarStateController, com.android.systemui.plugins.ActivityStarter, com.android.systemui.qs.logging.QSLogger, com.android.systemui.statusbar.policy.BluetoothController):void");
    }
}
