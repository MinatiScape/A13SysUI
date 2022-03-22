package com.google.android.systemui.columbus.sensors.config;

import android.content.Context;
/* loaded from: classes.dex */
public final class SensorConfiguration {
    public final float defaultSensitivityValue;
    public final float lowSensitivityValue;

    public SensorConfiguration(Context context) {
        this.defaultSensitivityValue = context.getResources().getInteger(2131492877) * 0.01f;
        this.lowSensitivityValue = context.getResources().getInteger(2131492878) * 0.01f;
    }
}
