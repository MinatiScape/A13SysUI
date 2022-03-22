package com.google.android.systemui.lowlightclock;

import com.android.systemui.util.sensors.AsyncSensorManager;
import com.google.android.systemui.lowlightclock.AmbientLightModeMonitor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AmbientLightModeMonitor_Factory implements Factory<AmbientLightModeMonitor> {
    public final Provider<AmbientLightModeMonitor.DebounceAlgorithm> algorithmProvider;
    public final Provider<AsyncSensorManager> sensorManagerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new AmbientLightModeMonitor(this.algorithmProvider.mo144get(), this.sensorManagerProvider.mo144get());
    }

    public AmbientLightModeMonitor_Factory(Provider<AmbientLightModeMonitor.DebounceAlgorithm> provider, Provider<AsyncSensorManager> provider2) {
        this.algorithmProvider = provider;
        this.sensorManagerProvider = provider2;
    }
}
