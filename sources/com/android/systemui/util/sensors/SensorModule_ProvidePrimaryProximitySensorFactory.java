package com.android.systemui.util.sensors;

import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import com.android.systemui.util.sensors.ThresholdSensorImpl;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SensorModule_ProvidePrimaryProximitySensorFactory implements Factory<ThresholdSensor> {
    public final Provider<SensorManager> sensorManagerProvider;
    public final Provider<ThresholdSensorImpl.Builder> thresholdSensorBuilderProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        float f;
        SensorManager sensorManager = this.sensorManagerProvider.mo144get();
        ThresholdSensorImpl.Builder builder = this.thresholdSensorBuilderProvider.mo144get();
        try {
            Objects.requireNonNull(builder);
            builder.mSensorDelay = 3;
            Sensor findSensorByType = builder.findSensorByType(builder.mResources.getString(2131953036), true);
            if (findSensorByType != null) {
                builder.mSensor = findSensorByType;
                builder.mSensorSet = true;
            }
            try {
                float f2 = builder.mResources.getFloat(2131166832);
                builder.mThresholdValue = f2;
                builder.mThresholdSet = true;
                if (!builder.mThresholdLatchValueSet) {
                    builder.mThresholdLatchValue = f2;
                }
            } catch (Resources.NotFoundException unused) {
            }
            try {
                builder.mThresholdLatchValue = builder.mResources.getFloat(2131166833);
                builder.mThresholdLatchValueSet = true;
            } catch (Resources.NotFoundException unused2) {
            }
            return builder.build();
        } catch (IllegalStateException unused3) {
            Sensor defaultSensor = sensorManager.getDefaultSensor(8, true);
            Objects.requireNonNull(builder);
            builder.mSensor = defaultSensor;
            builder.mSensorSet = true;
            if (defaultSensor != null) {
                f = defaultSensor.getMaximumRange();
            } else {
                f = 0.0f;
            }
            builder.mThresholdValue = f;
            builder.mThresholdSet = true;
            if (!builder.mThresholdLatchValueSet) {
                builder.mThresholdLatchValue = f;
            }
            return builder.build();
        }
    }

    public SensorModule_ProvidePrimaryProximitySensorFactory(Provider<SensorManager> provider, Provider<ThresholdSensorImpl.Builder> provider2) {
        this.sensorManagerProvider = provider;
        this.thresholdSensorBuilderProvider = provider2;
    }
}
