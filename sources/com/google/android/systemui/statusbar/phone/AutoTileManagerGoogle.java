package com.google.android.systemui.statusbar.phone;

import android.content.Context;
import android.hardware.display.NightDisplayListener;
import android.os.Build;
import android.os.Handler;
import com.android.systemui.ImageWallpaper$GLEngine$$ExternalSyntheticLambda0;
import com.android.systemui.qs.AutoAddTracker;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.qs.ReduceBrightColorsController;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.ManagedProfileController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DeviceControlsController;
import com.android.systemui.statusbar.policy.HotspotController;
import com.android.systemui.statusbar.policy.WalletController;
import com.android.systemui.util.settings.SecureSettings;
import java.util.Objects;
/* loaded from: classes.dex */
public final class AutoTileManagerGoogle extends AutoTileManager {
    public final BatteryController mBatteryController;
    public final AnonymousClass1 mBatteryControllerCallback = new AnonymousClass1();

    /* renamed from: com.google.android.systemui.statusbar.phone.AutoTileManagerGoogle$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements BatteryController.BatteryStateChangeCallback {
        public AnonymousClass1() {
        }

        @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
        public final void onReverseChanged(boolean z, int i, String str) {
            if (!AutoTileManagerGoogle.this.mAutoTracker.isAdded("reverse") && z) {
                QSTileHost qSTileHost = AutoTileManagerGoogle.this.mHost;
                Objects.requireNonNull(qSTileHost);
                qSTileHost.addTile("reverse", -1);
                AutoTileManagerGoogle.this.mAutoTracker.setTileAdded("reverse");
                AutoTileManagerGoogle.this.mHandler.post(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda0(this, 11));
            }
        }
    }

    public AutoTileManagerGoogle(Context context, AutoAddTracker.Builder builder, QSTileHost qSTileHost, Handler handler, SecureSettings secureSettings, HotspotController hotspotController, DataSaverController dataSaverController, ManagedProfileController managedProfileController, NightDisplayListener nightDisplayListener, CastController castController, BatteryController batteryController, ReduceBrightColorsController reduceBrightColorsController, DeviceControlsController deviceControlsController, WalletController walletController, boolean z) {
        super(context, builder, qSTileHost, handler, secureSettings, hotspotController, dataSaverController, managedProfileController, nightDisplayListener, castController, reduceBrightColorsController, deviceControlsController, walletController, z);
        this.mBatteryController = batteryController;
    }

    @Override // com.android.systemui.statusbar.phone.AutoTileManager
    public final void init() {
        super.init();
        if (!this.mAutoTracker.isAdded("ott") && Build.IS_DEBUGGABLE) {
            this.mAutoTracker.setTileAdded("ott");
            QSTileHost qSTileHost = this.mHost;
            Objects.requireNonNull(qSTileHost);
            qSTileHost.addTile("ott", -1);
        }
    }

    @Override // com.android.systemui.statusbar.phone.AutoTileManager
    public final void startControllersAndSettingsListeners() {
        super.startControllersAndSettingsListeners();
        if (!this.mAutoTracker.isAdded("reverse")) {
            this.mBatteryController.addCallback(this.mBatteryControllerCallback);
        }
    }

    @Override // com.android.systemui.statusbar.phone.AutoTileManager
    public final void stopListening() {
        super.stopListening();
        this.mBatteryController.removeCallback(this.mBatteryControllerCallback);
    }
}
