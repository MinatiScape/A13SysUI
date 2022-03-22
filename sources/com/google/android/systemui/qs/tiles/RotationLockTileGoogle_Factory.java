package com.google.android.systemui.qs.tiles;

import android.hardware.SensorPrivacyManager;
import android.os.Handler;
import android.os.Looper;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.statusbar.policy.RotationLockController;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class RotationLockTileGoogle_Factory implements Factory<RotationLockTileGoogle> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<Looper> backgroundLooperProvider;
    public final Provider<BatteryController> batteryControllerProvider;
    public final Provider<DevicePostureController> devicePostureControllerProvider;
    public final Provider<String[]> deviceStateRotationLockDefaultsProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<QSHost> hostProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<MetricsLogger> metricsLoggerProvider;
    public final Provider<SensorPrivacyManager> privacyManagerProvider;
    public final Provider<QSLogger> qsLoggerProvider;
    public final Provider<RotationLockController> rotationLockControllerProvider;
    public final Provider<SecureSettings> secureSettingsProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public static RotationLockTileGoogle_Factory create(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<FalsingManager> provider4, Provider<MetricsLogger> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<RotationLockController> provider9, Provider<SensorPrivacyManager> provider10, Provider<BatteryController> provider11, Provider<SecureSettings> provider12, Provider<String[]> provider13, Provider<DevicePostureController> provider14) {
        return new RotationLockTileGoogle_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new RotationLockTileGoogle(this.hostProvider.mo144get(), this.backgroundLooperProvider.mo144get(), this.mainHandlerProvider.mo144get(), this.falsingManagerProvider.mo144get(), this.metricsLoggerProvider.mo144get(), this.statusBarStateControllerProvider.mo144get(), this.activityStarterProvider.mo144get(), this.qsLoggerProvider.mo144get(), this.rotationLockControllerProvider.mo144get(), this.privacyManagerProvider.mo144get(), this.batteryControllerProvider.mo144get(), this.secureSettingsProvider.mo144get(), this.deviceStateRotationLockDefaultsProvider.mo144get(), this.devicePostureControllerProvider.mo144get());
    }

    public RotationLockTileGoogle_Factory(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<FalsingManager> provider4, Provider<MetricsLogger> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<RotationLockController> provider9, Provider<SensorPrivacyManager> provider10, Provider<BatteryController> provider11, Provider<SecureSettings> provider12, Provider<String[]> provider13, Provider<DevicePostureController> provider14) {
        this.hostProvider = provider;
        this.backgroundLooperProvider = provider2;
        this.mainHandlerProvider = provider3;
        this.falsingManagerProvider = provider4;
        this.metricsLoggerProvider = provider5;
        this.statusBarStateControllerProvider = provider6;
        this.activityStarterProvider = provider7;
        this.qsLoggerProvider = provider8;
        this.rotationLockControllerProvider = provider9;
        this.privacyManagerProvider = provider10;
        this.batteryControllerProvider = provider11;
        this.secureSettingsProvider = provider12;
        this.deviceStateRotationLockDefaultsProvider = provider13;
        this.devicePostureControllerProvider = provider14;
    }
}
