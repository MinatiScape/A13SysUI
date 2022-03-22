package com.android.systemui.doze;

import android.content.Context;
import android.hardware.Sensor;
import android.os.Handler;
import com.android.systemui.LatencyTester_Factory;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.util.sensors.AsyncSensorManager;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeScreenBrightness_Factory implements Factory<DozeScreenBrightness> {
    public final Provider<AlwaysOnDisplayPolicy> alwaysOnDisplayPolicyProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DevicePostureController> devicePostureControllerProvider;
    public final Provider<DozeLog> dozeLogProvider;
    public final Provider<DozeParameters> dozeParametersProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<DozeHost> hostProvider;
    public final Provider<Optional<Sensor>[]> lightSensorOptionalProvider;
    public final Provider<AsyncSensorManager> sensorManagerProvider;
    public final Provider<DozeMachine.Service> serviceProvider;
    public final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public static DozeScreenBrightness_Factory create(Provider provider, Provider provider2, Provider provider3, LatencyTester_Factory latencyTester_Factory, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10) {
        return new DozeScreenBrightness_Factory(provider, provider2, provider3, latencyTester_Factory, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DozeScreenBrightness(this.contextProvider.mo144get(), this.serviceProvider.mo144get(), this.sensorManagerProvider.mo144get(), this.lightSensorOptionalProvider.mo144get(), this.hostProvider.mo144get(), this.handlerProvider.mo144get(), this.alwaysOnDisplayPolicyProvider.mo144get(), this.wakefulnessLifecycleProvider.mo144get(), this.dozeParametersProvider.mo144get(), this.devicePostureControllerProvider.mo144get(), this.dozeLogProvider.mo144get());
    }

    public DozeScreenBrightness_Factory(Provider provider, Provider provider2, Provider provider3, LatencyTester_Factory latencyTester_Factory, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10) {
        this.contextProvider = provider;
        this.serviceProvider = provider2;
        this.sensorManagerProvider = provider3;
        this.lightSensorOptionalProvider = latencyTester_Factory;
        this.hostProvider = provider4;
        this.handlerProvider = provider5;
        this.alwaysOnDisplayPolicyProvider = provider6;
        this.wakefulnessLifecycleProvider = provider7;
        this.dozeParametersProvider = provider8;
        this.devicePostureControllerProvider = provider9;
        this.dozeLogProvider = provider10;
    }
}
