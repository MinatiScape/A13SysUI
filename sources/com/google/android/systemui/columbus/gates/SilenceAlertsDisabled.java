package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.provider.Settings;
import com.google.android.systemui.columbus.ColumbusSettings;
import java.util.Objects;
/* compiled from: SilenceAlertsDisabled.kt */
/* loaded from: classes.dex */
public final class SilenceAlertsDisabled extends Gate {
    public final ColumbusSettings columbusSettings;
    public final SilenceAlertsDisabled$settingsChangeListener$1 settingsChangeListener = new ColumbusSettings.ColumbusSettingsChangeListener() { // from class: com.google.android.systemui.columbus.gates.SilenceAlertsDisabled$settingsChangeListener$1
        @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
        public final void onColumbusEnabledChange(boolean z) {
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

        @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
        public final void onAlertSilenceEnabledChange(boolean z) {
            SilenceAlertsDisabled silenceAlertsDisabled = SilenceAlertsDisabled.this;
            Objects.requireNonNull(silenceAlertsDisabled);
            silenceAlertsDisabled.setBlocking(!z);
        }
    };

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onActivate() {
        boolean z;
        this.columbusSettings.registerColumbusSettingsChangeListener(this.settingsChangeListener);
        ColumbusSettings columbusSettings = this.columbusSettings;
        Objects.requireNonNull(columbusSettings);
        if (Settings.Secure.getIntForUser(columbusSettings.contentResolver, "columbus_silence_alerts", 1, columbusSettings.userTracker.getUserId()) != 0) {
            z = true;
        } else {
            z = false;
        }
        setBlocking(!z);
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onDeactivate() {
        ColumbusSettings columbusSettings = this.columbusSettings;
        SilenceAlertsDisabled$settingsChangeListener$1 silenceAlertsDisabled$settingsChangeListener$1 = this.settingsChangeListener;
        Objects.requireNonNull(columbusSettings);
        columbusSettings.listeners.remove(silenceAlertsDisabled$settingsChangeListener$1);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.google.android.systemui.columbus.gates.SilenceAlertsDisabled$settingsChangeListener$1] */
    public SilenceAlertsDisabled(Context context, ColumbusSettings columbusSettings) {
        super(context);
        this.columbusSettings = columbusSettings;
    }
}
