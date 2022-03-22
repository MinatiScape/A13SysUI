package com.android.systemui.util.sensors;

import android.content.res.Resources;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.sensors.ThresholdSensorImpl;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ThresholdSensorImpl_BuilderFactory_Factory implements Factory<ThresholdSensorImpl.BuilderFactory> {
    public final Provider<Execution> executionProvider;
    public final Provider<Resources> resourcesProvider;
    public final Provider<AsyncSensorManager> sensorManagerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ThresholdSensorImpl.BuilderFactory(this.resourcesProvider.mo144get(), this.sensorManagerProvider.mo144get(), this.executionProvider.mo144get());
    }

    public ThresholdSensorImpl_BuilderFactory_Factory(Provider<Resources> provider, Provider<AsyncSensorManager> provider2, Provider<Execution> provider3) {
        this.resourcesProvider = provider;
        this.sensorManagerProvider = provider2;
        this.executionProvider = provider3;
    }
}
