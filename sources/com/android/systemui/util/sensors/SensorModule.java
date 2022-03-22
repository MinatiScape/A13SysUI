package com.android.systemui.util.sensors;

import android.content.res.Resources;
import android.hardware.Sensor;
import android.text.TextUtils;
import android.util.Log;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.sensors.ThresholdSensorImpl;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SensorModule {
    public static ThresholdSensor[] createPostureToSensorMapping(ThresholdSensorImpl.BuilderFactory builderFactory, String[] strArr, int i, int i2) {
        boolean z;
        Objects.requireNonNull(builderFactory);
        AsyncSensorManager asyncSensorManager = builderFactory.mSensorManager;
        Execution execution = builderFactory.mExecution;
        if (0.0f <= 0.0f) {
            ThresholdSensor[] thresholdSensorArr = new ThresholdSensor[5];
            Arrays.fill(thresholdSensorArr, new ThresholdSensorImpl(asyncSensorManager, null, execution, 0.0f, 0.0f, 3));
            if (!(strArr == null || strArr.length == 0)) {
                for (String str : strArr) {
                    if (!TextUtils.isEmpty(str)) {
                        z = true;
                        break;
                    }
                }
            }
            z = false;
            if (!z) {
                Log.e("SensorModule", "config doesn't support postures, but attempting to retrieve proxSensorMapping");
                return thresholdSensorArr;
            }
            HashMap hashMap = new HashMap();
            for (int i3 = 0; i3 < strArr.length; i3++) {
                try {
                    String str2 = strArr[i3];
                    if (hashMap.containsKey(str2)) {
                        thresholdSensorArr[i3] = (ThresholdSensor) hashMap.get(str2);
                    } else {
                        Resources resources = builderFactory.mResources;
                        ThresholdSensorImpl.Builder builder = new ThresholdSensorImpl.Builder(resources, builderFactory.mSensorManager, builderFactory.mExecution);
                        Sensor findSensorByType = builder.findSensorByType(strArr[i3], true);
                        if (findSensorByType != null) {
                            builder.mSensor = findSensorByType;
                            builder.mSensorSet = true;
                        }
                        try {
                            float f = resources.getFloat(i);
                            builder.mThresholdValue = f;
                            builder.mThresholdSet = true;
                            if (!builder.mThresholdLatchValueSet) {
                                builder.mThresholdLatchValue = f;
                            }
                        } catch (Resources.NotFoundException unused) {
                        }
                        try {
                            builder.mThresholdLatchValue = builder.mResources.getFloat(i2);
                            builder.mThresholdLatchValueSet = true;
                        } catch (Resources.NotFoundException unused2) {
                        }
                        thresholdSensorArr[i3] = builder.build();
                        hashMap.put(str2, thresholdSensorArr[i3]);
                    }
                } catch (IllegalStateException unused3) {
                }
            }
            return thresholdSensorArr;
        }
        throw new IllegalStateException("Threshold must be less than or equal to Threshold Latch");
    }
}
