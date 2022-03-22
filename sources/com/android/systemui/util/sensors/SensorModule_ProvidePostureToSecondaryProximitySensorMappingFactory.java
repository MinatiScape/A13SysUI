package com.android.systemui.util.sensors;

import android.content.res.Resources;
import com.android.systemui.util.sensors.ThresholdSensorImpl;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SensorModule_ProvidePostureToSecondaryProximitySensorMappingFactory implements Factory<ThresholdSensor[]> {
    public final Provider<Resources> resourcesProvider;
    public final Provider<ThresholdSensorImpl.BuilderFactory> thresholdSensorImplBuilderFactoryProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return SensorModule.createPostureToSensorMapping(this.thresholdSensorImplBuilderFactoryProvider.mo144get(), this.resourcesProvider.mo144get().getStringArray(2130903123), 2131166830, 2131166831);
    }

    public SensorModule_ProvidePostureToSecondaryProximitySensorMappingFactory(ThresholdSensorImpl_BuilderFactory_Factory thresholdSensorImpl_BuilderFactory_Factory, Provider provider) {
        this.thresholdSensorImplBuilderFactoryProvider = thresholdSensorImpl_BuilderFactory_Factory;
        this.resourcesProvider = provider;
    }
}
