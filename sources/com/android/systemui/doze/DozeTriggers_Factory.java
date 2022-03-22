package com.android.systemui.doze;

import android.content.Context;
import android.hardware.display.AmbientDisplayConfiguration;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dock.DockManager;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.android.systemui.util.sensors.ProximityCheck;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.util.wakelock.WakeLock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeTriggers_Factory implements Factory<DozeTriggers> {
    public final Provider<AuthController> authControllerProvider;
    public final Provider<BatteryController> batteryControllerProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<AmbientDisplayConfiguration> configProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DevicePostureController> devicePostureControllerProvider;
    public final Provider<DockManager> dockManagerProvider;
    public final Provider<DozeHost> dozeHostProvider;
    public final Provider<DozeLog> dozeLogProvider;
    public final Provider<DozeParameters> dozeParametersProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<DelayableExecutor> mainExecutorProvider;
    public final Provider<ProximityCheck> proxCheckProvider;
    public final Provider<ProximitySensor> proximitySensorProvider;
    public final Provider<SecureSettings> secureSettingsProvider;
    public final Provider<AsyncSensorManager> sensorManagerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<WakeLock> wakeLockProvider;

    public DozeTriggers_Factory(Provider<Context> provider, Provider<DozeHost> provider2, Provider<AmbientDisplayConfiguration> provider3, Provider<DozeParameters> provider4, Provider<AsyncSensorManager> provider5, Provider<WakeLock> provider6, Provider<DockManager> provider7, Provider<ProximitySensor> provider8, Provider<ProximityCheck> provider9, Provider<DozeLog> provider10, Provider<BroadcastDispatcher> provider11, Provider<SecureSettings> provider12, Provider<AuthController> provider13, Provider<DelayableExecutor> provider14, Provider<UiEventLogger> provider15, Provider<KeyguardStateController> provider16, Provider<DevicePostureController> provider17, Provider<BatteryController> provider18) {
        this.contextProvider = provider;
        this.dozeHostProvider = provider2;
        this.configProvider = provider3;
        this.dozeParametersProvider = provider4;
        this.sensorManagerProvider = provider5;
        this.wakeLockProvider = provider6;
        this.dockManagerProvider = provider7;
        this.proximitySensorProvider = provider8;
        this.proxCheckProvider = provider9;
        this.dozeLogProvider = provider10;
        this.broadcastDispatcherProvider = provider11;
        this.secureSettingsProvider = provider12;
        this.authControllerProvider = provider13;
        this.mainExecutorProvider = provider14;
        this.uiEventLoggerProvider = provider15;
        this.keyguardStateControllerProvider = provider16;
        this.devicePostureControllerProvider = provider17;
        this.batteryControllerProvider = provider18;
    }

    public static DozeTriggers_Factory create(Provider<Context> provider, Provider<DozeHost> provider2, Provider<AmbientDisplayConfiguration> provider3, Provider<DozeParameters> provider4, Provider<AsyncSensorManager> provider5, Provider<WakeLock> provider6, Provider<DockManager> provider7, Provider<ProximitySensor> provider8, Provider<ProximityCheck> provider9, Provider<DozeLog> provider10, Provider<BroadcastDispatcher> provider11, Provider<SecureSettings> provider12, Provider<AuthController> provider13, Provider<DelayableExecutor> provider14, Provider<UiEventLogger> provider15, Provider<KeyguardStateController> provider16, Provider<DevicePostureController> provider17, Provider<BatteryController> provider18) {
        return new DozeTriggers_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DozeTriggers(this.contextProvider.mo144get(), this.dozeHostProvider.mo144get(), this.configProvider.mo144get(), this.dozeParametersProvider.mo144get(), this.sensorManagerProvider.mo144get(), this.wakeLockProvider.mo144get(), this.dockManagerProvider.mo144get(), this.proximitySensorProvider.mo144get(), this.proxCheckProvider.mo144get(), this.dozeLogProvider.mo144get(), this.broadcastDispatcherProvider.mo144get(), this.secureSettingsProvider.mo144get(), this.authControllerProvider.mo144get(), this.mainExecutorProvider.mo144get(), this.uiEventLoggerProvider.mo144get(), this.keyguardStateControllerProvider.mo144get(), this.devicePostureControllerProvider.mo144get(), this.batteryControllerProvider.mo144get());
    }
}
