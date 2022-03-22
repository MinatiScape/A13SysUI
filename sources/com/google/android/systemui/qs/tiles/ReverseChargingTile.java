package com.google.android.systemui.qs.tiles;

import android.content.Intent;
import android.database.ContentObserver;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.os.IThermalEventListener;
import android.os.IThermalService;
import android.os.Looper;
import android.os.Temperature;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline1;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline0;
import com.android.internal.logging.MetricsLogger;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline3;
import com.android.systemui.Prefs;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.theme.ThemeOverlayApplier;
/* loaded from: classes.dex */
public final class ReverseChargingTile extends QSTileImpl<QSTile.BooleanState> implements BatteryController.BatteryStateChangeCallback {
    public static final boolean DEBUG = Log.isLoggable("ReverseChargingTile", 3);
    public final BatteryController mBatteryController;
    public int mBatteryLevel;
    public boolean mListening;
    public boolean mOverHeat;
    public boolean mPowerSave;
    public boolean mReverse;
    public final IThermalService mThermalService;
    public int mThresholdLevel;
    public final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(2131232232);
    public final AnonymousClass1 mThermalEventListener = new IThermalEventListener.Stub() { // from class: com.google.android.systemui.qs.tiles.ReverseChargingTile.1
        public final void notifyThrottling(Temperature temperature) {
            boolean z;
            int status = temperature.getStatus();
            ReverseChargingTile reverseChargingTile = ReverseChargingTile.this;
            if (status >= 5) {
                z = true;
            } else {
                z = false;
            }
            reverseChargingTile.mOverHeat = z;
            if (ReverseChargingTile.DEBUG) {
                ExifInterface$$ExternalSyntheticOutline1.m("notifyThrottling(): status=", status, "ReverseChargingTile");
            }
        }
    };
    public final AnonymousClass2 mSettingsObserver = new ContentObserver(this.mHandler) { // from class: com.google.android.systemui.qs.tiles.ReverseChargingTile.2
        @Override // android.database.ContentObserver
        public final void onChange(boolean z) {
            ReverseChargingTile.this.updateThresholdLevel();
        }
    };

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final int getMetricsCategory() {
        return 0;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        Intent intent = new Intent("android.settings.REVERSE_CHARGING_SETTINGS");
        intent.setPackage(ThemeOverlayApplier.SETTINGS_PACKAGE);
        return intent;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return this.mContext.getString(2131953166);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleClick(View view) {
        if (((QSTile.BooleanState) this.mState).state != 0) {
            this.mReverse = !this.mReverse;
            if (DEBUG) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("handleClick(): rtx=");
                m.append(this.mReverse ? 1 : 0);
                m.append(",this=");
                m.append(this);
                Log.d("ReverseChargingTile", m.toString());
            }
            this.mBatteryController.setReverseState(this.mReverse);
            if (!Prefs.getBoolean(this.mHost.getUserContext(), "HasSeenReverseBottomSheet")) {
                Intent intent = new Intent("android.settings.REVERSE_CHARGING_BOTTOM_SHEET");
                intent.setPackage(ThemeOverlayApplier.SETTINGS_PACKAGE);
                this.mActivityStarter.postStartActivityDismissingKeyguard(intent, 0);
                Prefs.putBoolean(this.mHost.getUserContext(), "HasSeenReverseBottomSheet", true);
            }
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        int i;
        boolean z;
        String str;
        QSTile.BooleanState booleanState2 = booleanState;
        boolean isWirelessCharging = this.mBatteryController.isWirelessCharging();
        int i2 = 1;
        if (this.mBatteryLevel <= this.mThresholdLevel) {
            i = 1;
        } else {
            i = 0;
        }
        boolean z2 = this.mOverHeat;
        if (z2 || this.mPowerSave || isWirelessCharging || i != 0 || !this.mReverse) {
            z = false;
        } else {
            z = true;
        }
        booleanState2.value = z;
        if (z2 || this.mPowerSave || isWirelessCharging || i != 0) {
            i2 = 0;
        } else if (this.mReverse) {
            i2 = 2;
        }
        booleanState2.state = i2;
        booleanState2.icon = this.mIcon;
        CharSequence tileLabel = getTileLabel();
        booleanState2.label = tileLabel;
        booleanState2.contentDescription = tileLabel;
        booleanState2.expandedAccessibilityClassName = Switch.class.getName();
        if (this.mOverHeat) {
            str = this.mContext.getString(2131953374);
        } else if (this.mPowerSave) {
            str = this.mContext.getString(2131953083);
        } else if (isWirelessCharging) {
            str = this.mContext.getString(2131953672);
        } else if (i != 0) {
            str = this.mContext.getString(2131952674);
        } else {
            str = null;
        }
        booleanState2.secondaryLabel = str;
        if (DEBUG) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("handleUpdateState(): ps=");
            m.append(this.mPowerSave ? 1 : 0);
            m.append(",wlc=");
            m.append(isWirelessCharging ? 1 : 0);
            m.append(",low=");
            m.append(i);
            m.append(",over=");
            m.append(this.mOverHeat ? 1 : 0);
            m.append(",rtx=");
            m.append(this.mReverse ? 1 : 0);
            m.append(",this=");
            m.append(this);
            Log.d("ReverseChargingTile", m.toString());
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final boolean isAvailable() {
        return this.mBatteryController.isReverseSupported();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public final void onBatteryLevelChanged(int i, boolean z, boolean z2) {
        this.mBatteryLevel = i;
        this.mReverse = this.mBatteryController.isReverseOn();
        if (DEBUG) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("onBatteryLevelChanged(): rtx=");
            m.append(this.mReverse ? 1 : 0);
            m.append(",level=");
            m.append(this.mBatteryLevel);
            m.append(",threshold=");
            KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(m, this.mThresholdLevel, "ReverseChargingTile");
        }
        refreshState(null);
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public final void onPowerSaveChanged(boolean z) {
        this.mPowerSave = z;
        refreshState(null);
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public final void onReverseChanged(boolean z, int i, String str) {
        if (DEBUG) {
            StringBuilder m = GridLayoutManager$$ExternalSyntheticOutline0.m("onReverseChanged(): rtx=", z ? 1 : 0, ",level=", i, ",name=");
            m.append(str);
            m.append(",this=");
            m.append(this);
            Log.d("ReverseChargingTile", m.toString());
        }
        this.mReverse = z;
        refreshState(null);
    }

    public final void updateThresholdLevel() {
        this.mThresholdLevel = Settings.Global.getInt(this.mContext.getContentResolver(), "advanced_battery_usage_amount", 2) * 5;
        if (DEBUG) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("updateThresholdLevel(): rtx=");
            m.append(this.mReverse ? 1 : 0);
            m.append(",level=");
            m.append(this.mBatteryLevel);
            m.append(",threshold=");
            KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(m, this.mThresholdLevel, "ReverseChargingTile");
        }
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [com.google.android.systemui.qs.tiles.ReverseChargingTile$2] */
    public ReverseChargingTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, BatteryController batteryController, IThermalService iThermalService) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mBatteryController = batteryController;
        batteryController.observe((Lifecycle) this.mLifecycle, (LifecycleRegistry) this);
        this.mThermalService = iThermalService;
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0054, code lost:
        android.util.Log.w("ReverseChargingTile", "isOverHeat(): current skin status = " + r5.getStatus() + ", temperature = " + r5.getValue());
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0078, code lost:
        r4 = true;
     */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void handleSetListening(boolean r9) {
        /*
            r8 = this;
            super.handleSetListening(r9)
            boolean r0 = r8.mListening
            if (r0 != r9) goto L_0x0008
            return
        L_0x0008:
            r8.mListening = r9
            java.lang.String r0 = "ReverseChargingTile"
            if (r9 == 0) goto L_0x0095
            r8.updateThresholdLevel()
            android.content.Context r1 = r8.mContext
            android.content.ContentResolver r1 = r1.getContentResolver()
            java.lang.String r2 = "advanced_battery_usage_amount"
            android.net.Uri r2 = android.provider.Settings.Global.getUriFor(r2)
            com.google.android.systemui.qs.tiles.ReverseChargingTile$2 r3 = r8.mSettingsObserver
            r4 = 0
            r1.registerContentObserver(r2, r4, r3)
            r1 = 3
            android.os.IThermalService r2 = r8.mThermalService     // Catch: RemoteException -> 0x002c
            com.google.android.systemui.qs.tiles.ReverseChargingTile$1 r3 = r8.mThermalEventListener     // Catch: RemoteException -> 0x002c
            r2.registerThermalEventListenerWithType(r3, r1)     // Catch: RemoteException -> 0x002c
            goto L_0x0041
        L_0x002c:
            r2 = move-exception
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = "Could not register thermal event listener, exception: "
            r3.append(r5)
            r3.append(r2)
            java.lang.String r2 = r3.toString()
            android.util.Log.e(r0, r2)
        L_0x0041:
            android.os.IThermalService r2 = r8.mThermalService     // Catch: RemoteException -> 0x007d
            android.os.Temperature[] r1 = r2.getCurrentTemperaturesWithType(r1)     // Catch: RemoteException -> 0x007d
            int r2 = r1.length     // Catch: RemoteException -> 0x007d
            r3 = r4
        L_0x0049:
            if (r3 >= r2) goto L_0x0092
            r5 = r1[r3]     // Catch: RemoteException -> 0x007d
            int r6 = r5.getStatus()     // Catch: RemoteException -> 0x007d
            r7 = 5
            if (r6 < r7) goto L_0x007a
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: RemoteException -> 0x007d
            r1.<init>()     // Catch: RemoteException -> 0x007d
            java.lang.String r2 = "isOverHeat(): current skin status = "
            r1.append(r2)     // Catch: RemoteException -> 0x007d
            int r2 = r5.getStatus()     // Catch: RemoteException -> 0x007d
            r1.append(r2)     // Catch: RemoteException -> 0x007d
            java.lang.String r2 = ", temperature = "
            r1.append(r2)     // Catch: RemoteException -> 0x007d
            float r2 = r5.getValue()     // Catch: RemoteException -> 0x007d
            r1.append(r2)     // Catch: RemoteException -> 0x007d
            java.lang.String r1 = r1.toString()     // Catch: RemoteException -> 0x007d
            android.util.Log.w(r0, r1)     // Catch: RemoteException -> 0x007d
            r4 = 1
            goto L_0x0092
        L_0x007a:
            int r3 = r3 + 1
            goto L_0x0049
        L_0x007d:
            r1 = move-exception
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "isOverHeat(): "
            r2.append(r3)
            r2.append(r1)
            java.lang.String r1 = r2.toString()
            android.util.Log.w(r0, r1)
        L_0x0092:
            r8.mOverHeat = r4
            goto L_0x00bd
        L_0x0095:
            android.content.Context r1 = r8.mContext
            android.content.ContentResolver r1 = r1.getContentResolver()
            com.google.android.systemui.qs.tiles.ReverseChargingTile$2 r2 = r8.mSettingsObserver
            r1.unregisterContentObserver(r2)
            android.os.IThermalService r1 = r8.mThermalService     // Catch: RemoteException -> 0x00a8
            com.google.android.systemui.qs.tiles.ReverseChargingTile$1 r2 = r8.mThermalEventListener     // Catch: RemoteException -> 0x00a8
            r1.unregisterThermalEventListener(r2)     // Catch: RemoteException -> 0x00a8
            goto L_0x00bd
        L_0x00a8:
            r1 = move-exception
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Could not unregister thermal event listener, exception: "
            r2.append(r3)
            r2.append(r1)
            java.lang.String r1 = r2.toString()
            android.util.Log.e(r0, r1)
        L_0x00bd:
            boolean r1 = com.google.android.systemui.qs.tiles.ReverseChargingTile.DEBUG
            if (r1 == 0) goto L_0x00ef
            java.lang.String r1 = "handleSetListening(): rtx="
            java.lang.StringBuilder r1 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1.m(r1)
            boolean r2 = r8.mReverse
            r1.append(r2)
            java.lang.String r2 = ",level="
            r1.append(r2)
            int r2 = r8.mBatteryLevel
            r1.append(r2)
            java.lang.String r2 = ",threshold="
            r1.append(r2)
            int r8 = r8.mThresholdLevel
            r1.append(r8)
            java.lang.String r8 = ",listening="
            r1.append(r8)
            r1.append(r9)
            java.lang.String r8 = r1.toString()
            android.util.Log.d(r0, r8)
        L_0x00ef:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.qs.tiles.ReverseChargingTile.handleSetListening(boolean):void");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleDestroy() {
        super.handleDestroy();
    }
}
