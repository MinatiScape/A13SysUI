package com.google.android.systemui.statusbar;

import android.app.IActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.text.TextUtils;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.IBatteryStats;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.settingslib.fuelgauge.BatteryStatus;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dock.DockManager;
import com.android.systemui.keyguard.KeyguardIndication;
import com.android.systemui.keyguard.KeyguardIndicationRotateTextViewController;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.KeyguardIndicationController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.wakelock.WakeLock;
import com.google.android.systemui.adaptivecharging.AdaptiveChargingManager;
import java.text.NumberFormat;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public final class KeyguardIndicationControllerGoogle extends KeyguardIndicationController {
    public boolean mAdaptiveChargingActive;
    public boolean mAdaptiveChargingEnabledInSettings;
    @VisibleForTesting
    public AdaptiveChargingManager mAdaptiveChargingManager;
    public int mBatteryLevel;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final Context mContext;
    public final DeviceConfigProxy mDeviceConfig;
    public long mEstimatedChargeCompletion;
    public boolean mInited;
    public boolean mIsCharging;
    public final TunerService mTunerService;
    public GoogleKeyguardCallback mUpdateMonitorCallback;
    public final AnonymousClass1 mBroadcastReceiver = new BroadcastReceiver() { // from class: com.google.android.systemui.statusbar.KeyguardIndicationControllerGoogle.1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            if ("com.google.android.systemui.adaptivecharging.ADAPTIVE_CHARGING_DEADLINE_SET".equals(intent.getAction())) {
                KeyguardIndicationControllerGoogle.this.triggerAdaptiveChargingStatusUpdate();
            }
        }
    };
    @VisibleForTesting
    public AdaptiveChargingManager.AdaptiveChargingStatusReceiver mAdaptiveChargingStatusReceiver = new AdaptiveChargingManager.AdaptiveChargingStatusReceiver() { // from class: com.google.android.systemui.statusbar.KeyguardIndicationControllerGoogle.2
        @Override // com.google.android.systemui.adaptivecharging.AdaptiveChargingManager.AdaptiveChargingStatusReceiver
        public final void onDestroyInterface() {
        }

        @Override // com.google.android.systemui.adaptivecharging.AdaptiveChargingManager.AdaptiveChargingStatusReceiver
        public final void onReceiveStatus(String str, int i) {
            boolean z;
            KeyguardIndicationControllerGoogle keyguardIndicationControllerGoogle = KeyguardIndicationControllerGoogle.this;
            boolean z2 = keyguardIndicationControllerGoogle.mAdaptiveChargingActive;
            boolean z3 = AdaptiveChargingManager.DEBUG;
            boolean z4 = false;
            if ("Active".equals(str) || "Enabled".equals(str)) {
                z = true;
            } else {
                z = false;
            }
            if (z && i > 0) {
                z4 = true;
            }
            keyguardIndicationControllerGoogle.mAdaptiveChargingActive = z4;
            KeyguardIndicationControllerGoogle keyguardIndicationControllerGoogle2 = KeyguardIndicationControllerGoogle.this;
            long j = keyguardIndicationControllerGoogle2.mEstimatedChargeCompletion;
            long currentTimeMillis = System.currentTimeMillis();
            TimeUnit timeUnit = TimeUnit.SECONDS;
            keyguardIndicationControllerGoogle2.mEstimatedChargeCompletion = timeUnit.toMillis(i + 29) + currentTimeMillis;
            long abs = Math.abs(KeyguardIndicationControllerGoogle.this.mEstimatedChargeCompletion - j);
            boolean z5 = KeyguardIndicationControllerGoogle.this.mAdaptiveChargingActive;
            if (z2 != z5 || (z5 && abs > timeUnit.toMillis(30L))) {
                KeyguardIndicationControllerGoogle.this.updateIndication(true);
            }
        }
    };

    /* loaded from: classes.dex */
    public class GoogleKeyguardCallback extends KeyguardIndicationController.BaseKeyguardCallback {
        public GoogleKeyguardCallback() {
            super();
        }

        @Override // com.android.systemui.statusbar.KeyguardIndicationController.BaseKeyguardCallback, com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onRefreshBatteryInfo(BatteryStatus batteryStatus) {
            boolean z;
            KeyguardIndicationControllerGoogle keyguardIndicationControllerGoogle = KeyguardIndicationControllerGoogle.this;
            if (batteryStatus.status == 2) {
                z = true;
            } else {
                z = false;
            }
            keyguardIndicationControllerGoogle.mIsCharging = z;
            keyguardIndicationControllerGoogle.mBatteryLevel = batteryStatus.level;
            super.onRefreshBatteryInfo(batteryStatus);
            KeyguardIndicationControllerGoogle keyguardIndicationControllerGoogle2 = KeyguardIndicationControllerGoogle.this;
            if (keyguardIndicationControllerGoogle2.mIsCharging) {
                keyguardIndicationControllerGoogle2.triggerAdaptiveChargingStatusUpdate();
            } else {
                keyguardIndicationControllerGoogle2.mAdaptiveChargingActive = false;
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.google.android.systemui.statusbar.KeyguardIndicationControllerGoogle$1] */
    public KeyguardIndicationControllerGoogle(Context context, WakeLock.Builder builder, KeyguardStateController keyguardStateController, StatusBarStateController statusBarStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, DockManager dockManager, BroadcastDispatcher broadcastDispatcher, DevicePolicyManager devicePolicyManager, IBatteryStats iBatteryStats, UserManager userManager, TunerService tunerService, DeviceConfigProxy deviceConfigProxy, DelayableExecutor delayableExecutor, DelayableExecutor delayableExecutor2, FalsingManager falsingManager, LockPatternUtils lockPatternUtils, ScreenLifecycle screenLifecycle, IActivityManager iActivityManager, KeyguardBypassController keyguardBypassController) {
        super(context, builder, keyguardStateController, statusBarStateController, keyguardUpdateMonitor, dockManager, broadcastDispatcher, devicePolicyManager, iBatteryStats, userManager, delayableExecutor, delayableExecutor2, falsingManager, lockPatternUtils, screenLifecycle, iActivityManager, keyguardBypassController);
        this.mContext = context;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mTunerService = tunerService;
        this.mDeviceConfig = deviceConfigProxy;
        this.mAdaptiveChargingManager = new AdaptiveChargingManager(context);
    }

    @Override // com.android.systemui.statusbar.KeyguardIndicationController
    public final String computePowerIndication() {
        if (!this.mIsCharging || !this.mAdaptiveChargingEnabledInSettings || !this.mAdaptiveChargingActive) {
            return super.computePowerIndication();
        }
        String formatTimeToFull = this.mAdaptiveChargingManager.formatTimeToFull(this.mEstimatedChargeCompletion);
        return this.mContext.getResources().getString(2131951839, NumberFormat.getPercentInstance().format(this.mBatteryLevel / 100.0f), formatTimeToFull);
    }

    @Override // com.android.systemui.statusbar.KeyguardIndicationController
    public final KeyguardUpdateMonitorCallback getKeyguardCallback() {
        if (this.mUpdateMonitorCallback == null) {
            this.mUpdateMonitorCallback = new GoogleKeyguardCallback();
        }
        return this.mUpdateMonitorCallback;
    }

    public final void refreshAdaptiveChargingEnabled() {
        boolean z;
        boolean z2;
        AdaptiveChargingManager adaptiveChargingManager = this.mAdaptiveChargingManager;
        Objects.requireNonNull(adaptiveChargingManager);
        boolean z3 = false;
        if (!adaptiveChargingManager.hasAdaptiveChargingFeature() || !DeviceConfig.getBoolean("adaptive_charging", "adaptive_charging_enabled", true)) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            AdaptiveChargingManager adaptiveChargingManager2 = this.mAdaptiveChargingManager;
            Objects.requireNonNull(adaptiveChargingManager2);
            if (Settings.Secure.getInt(adaptiveChargingManager2.mContext.getContentResolver(), "adaptive_charging_enabled", 1) == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2) {
                z3 = true;
            }
        }
        this.mAdaptiveChargingEnabledInSettings = z3;
    }

    @Override // com.android.systemui.statusbar.KeyguardIndicationController
    public final void init() {
        super.init();
        if (!this.mInited) {
            this.mInited = true;
            this.mTunerService.addTunable(new TunerService.Tunable() { // from class: com.google.android.systemui.statusbar.KeyguardIndicationControllerGoogle$$ExternalSyntheticLambda1
                @Override // com.android.systemui.tuner.TunerService.Tunable
                public final void onTuningChanged(String str, String str2) {
                    KeyguardIndicationControllerGoogle keyguardIndicationControllerGoogle = KeyguardIndicationControllerGoogle.this;
                    Objects.requireNonNull(keyguardIndicationControllerGoogle);
                    keyguardIndicationControllerGoogle.refreshAdaptiveChargingEnabled();
                }
            }, "adaptive_charging_enabled");
            DeviceConfigProxy deviceConfigProxy = this.mDeviceConfig;
            DelayableExecutor delayableExecutor = this.mExecutor;
            DeviceConfig.OnPropertiesChangedListener keyguardIndicationControllerGoogle$$ExternalSyntheticLambda0 = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.google.android.systemui.statusbar.KeyguardIndicationControllerGoogle$$ExternalSyntheticLambda0
                public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                    KeyguardIndicationControllerGoogle keyguardIndicationControllerGoogle = KeyguardIndicationControllerGoogle.this;
                    Objects.requireNonNull(keyguardIndicationControllerGoogle);
                    if (properties.getKeyset().contains("adaptive_charging_enabled")) {
                        keyguardIndicationControllerGoogle.triggerAdaptiveChargingStatusUpdate();
                    }
                }
            };
            Objects.requireNonNull(deviceConfigProxy);
            DeviceConfig.addOnPropertiesChangedListener("adaptive_charging", delayableExecutor, keyguardIndicationControllerGoogle$$ExternalSyntheticLambda0);
            triggerAdaptiveChargingStatusUpdate();
            this.mBroadcastDispatcher.registerReceiver(this.mBroadcastReceiver, new IntentFilter("com.google.android.systemui.adaptivecharging.ADAPTIVE_CHARGING_DEADLINE_SET"), null, UserHandle.ALL);
        }
    }

    public final void setReverseChargingMessage(String str) {
        if (TextUtils.isEmpty(str)) {
            this.mRotateTextViewController.hideIndication(10);
            return;
        }
        KeyguardIndicationRotateTextViewController keyguardIndicationRotateTextViewController = this.mRotateTextViewController;
        Drawable drawable = this.mContext.getDrawable(2130772553);
        ColorStateList colorStateList = this.mInitialTextColorState;
        if (TextUtils.isEmpty(str) && drawable == null) {
            throw new IllegalStateException("message or icon must be set");
        } else if (colorStateList != null) {
            keyguardIndicationRotateTextViewController.updateIndication(10, new KeyguardIndication(str, colorStateList, drawable, null, null, null), false);
        } else {
            throw new IllegalStateException("text color must be set");
        }
    }

    public final void triggerAdaptiveChargingStatusUpdate() {
        refreshAdaptiveChargingEnabled();
        if (this.mAdaptiveChargingEnabledInSettings) {
            this.mAdaptiveChargingManager.queryStatus(this.mAdaptiveChargingStatusReceiver);
        } else {
            this.mAdaptiveChargingActive = false;
        }
    }
}
