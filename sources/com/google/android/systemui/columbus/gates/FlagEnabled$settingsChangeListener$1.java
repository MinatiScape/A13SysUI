package com.google.android.systemui.columbus.gates;

import com.google.android.systemui.columbus.ColumbusSettings;
/* compiled from: FlagEnabled.kt */
/* loaded from: classes.dex */
public final class FlagEnabled$settingsChangeListener$1 implements ColumbusSettings.ColumbusSettingsChangeListener {
    public final /* synthetic */ FlagEnabled this$0;

    @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
    public final void onAlertSilenceEnabledChange(boolean z) {
    }

    @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
    public final void onLowSensitivityChange(boolean z) {
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

    public FlagEnabled$settingsChangeListener$1(FlagEnabled flagEnabled) {
        this.this$0 = flagEnabled;
    }

    @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
    public final void onColumbusEnabledChange(boolean z) {
        FlagEnabled flagEnabled = this.this$0;
        flagEnabled.columbusEnabled = z;
        flagEnabled.setBlocking(!z);
    }
}
