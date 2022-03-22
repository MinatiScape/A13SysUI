package com.google.android.systemui.columbus.sensors.config;

import android.content.Context;
import android.provider.Settings;
import com.google.android.systemui.columbus.ColumbusSettings;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
/* compiled from: LowSensitivitySettingAdjustment.kt */
/* loaded from: classes.dex */
public final class LowSensitivitySettingAdjustment extends Adjustment {
    public final SensorConfiguration sensorConfiguration;
    public boolean useLowSensitivity;

    @Override // com.google.android.systemui.columbus.sensors.config.Adjustment
    public final float adjustSensitivity(float f) {
        if (this.useLowSensitivity) {
            return this.sensorConfiguration.lowSensitivityValue;
        }
        return f;
    }

    public LowSensitivitySettingAdjustment(Context context, ColumbusSettings columbusSettings, SensorConfiguration sensorConfiguration) {
        this.sensorConfiguration = sensorConfiguration;
        columbusSettings.registerColumbusSettingsChangeListener(new ColumbusSettings.ColumbusSettingsChangeListener() { // from class: com.google.android.systemui.columbus.sensors.config.LowSensitivitySettingAdjustment$settingsChangeListener$1
            @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
            public final void onAlertSilenceEnabledChange(boolean z) {
            }

            @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
            public final void onColumbusEnabledChange(boolean z) {
            }

            @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
            public final void onSelectedActionChange(String str) {
            }

            @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
            public final void onSelectedAppChange(String str) {
            }

            @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
            public final void onSelectedAppShortcutChange(String str) {
            }

            @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
            public final void onUseApSensorChange() {
            }

            @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
            public final void onLowSensitivityChange(boolean z) {
                LowSensitivitySettingAdjustment lowSensitivitySettingAdjustment = LowSensitivitySettingAdjustment.this;
                if (lowSensitivitySettingAdjustment.useLowSensitivity != z) {
                    lowSensitivitySettingAdjustment.useLowSensitivity = z;
                    Function1<? super Adjustment, Unit> function1 = lowSensitivitySettingAdjustment.callback;
                    if (function1 != null) {
                        function1.invoke(lowSensitivitySettingAdjustment);
                    }
                }
            }
        });
        this.useLowSensitivity = Settings.Secure.getIntForUser(columbusSettings.contentResolver, "columbus_low_sensitivity", 0, columbusSettings.userTracker.getUserId()) != 0;
        Function1<? super Adjustment, Unit> function1 = this.callback;
        if (function1 != null) {
            function1.invoke(this);
        }
    }
}
