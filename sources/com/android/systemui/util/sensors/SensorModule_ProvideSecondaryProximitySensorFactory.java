package com.android.systemui.util.sensors;

import android.content.res.Resources;
import android.hardware.Sensor;
import com.android.systemui.util.sensors.ThresholdSensorImpl;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SensorModule_ProvideSecondaryProximitySensorFactory implements Factory<ThresholdSensor> {
    public final Provider<ThresholdSensorImpl.Builder> thresholdSensorBuilderProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        ThresholdSensorImpl.Builder builder = this.thresholdSensorBuilderProvider.mo144get();
        try {
            Objects.requireNonNull(builder);
            Sensor findSensorByType = builder.findSensorByType(builder.mResources.getString(2131953035), true);
            if (findSensorByType != null) {
                builder.mSensor = findSensorByType;
                builder.mSensorSet = true;
            }
            try {
                float f = builder.mResources.getFloat(2131166830);
                builder.mThresholdValue = f;
                builder.mThresholdSet = true;
                if (!builder.mThresholdLatchValueSet) {
                    builder.mThresholdLatchValue = f;
                }
            } catch (Resources.NotFoundException unused) {
            }
            try {
                builder.mThresholdLatchValue = builder.mResources.getFloat(2131166831);
                builder.mThresholdLatchValueSet = true;
            } catch (Resources.NotFoundException unused2) {
            }
            return builder.build();
        } catch (IllegalStateException unused3) {
            Objects.requireNonNull(builder);
            builder.mSensor = null;
            builder.mSensorSet = true;
            builder.mThresholdValue = 0.0f;
            builder.mThresholdSet = true;
            if (!builder.mThresholdLatchValueSet) {
                builder.mThresholdLatchValue = 0.0f;
            }
            return builder.build();
        }
    }

    public SensorModule_ProvideSecondaryProximitySensorFactory(Provider<ThresholdSensorImpl.Builder> provider) {
        this.thresholdSensorBuilderProvider = provider;
    }
}
