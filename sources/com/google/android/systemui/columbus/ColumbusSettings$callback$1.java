package com.google.android.systemui.columbus;

import android.app.backup.BackupManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import com.google.android.systemui.columbus.ColumbusSettings;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ColumbusSettings.kt */
/* loaded from: classes.dex */
public final class ColumbusSettings$callback$1 extends Lambda implements Function1<Uri, Unit> {
    public final /* synthetic */ ColumbusSettings this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ColumbusSettings$callback$1(ColumbusSettings columbusSettings) {
        super(1);
        this.this$0 = columbusSettings;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Unit invoke(Uri uri) {
        Uri uri2 = uri;
        if (Intrinsics.areEqual(uri2, ColumbusSettings.COLUMBUS_ENABLED_URI)) {
            boolean isColumbusEnabled = this.this$0.isColumbusEnabled();
            for (ColumbusSettings.ColumbusSettingsChangeListener columbusSettingsChangeListener : this.this$0.listeners) {
                columbusSettingsChangeListener.onColumbusEnabledChange(isColumbusEnabled);
            }
            BackupManager.dataChangedForUser(this.this$0.userTracker.getUserId(), this.this$0.backupPackage);
        } else {
            boolean z = false;
            if (Intrinsics.areEqual(uri2, ColumbusSettings.COLUMBUS_AP_SENSOR_URI)) {
                ColumbusSettings columbusSettings = this.this$0;
                Objects.requireNonNull(columbusSettings);
                Settings.Secure.getIntForUser(columbusSettings.contentResolver, "columbus_ap_sensor", 0, columbusSettings.userTracker.getUserId());
                for (ColumbusSettings.ColumbusSettingsChangeListener columbusSettingsChangeListener2 : this.this$0.listeners) {
                    columbusSettingsChangeListener2.onUseApSensorChange();
                }
            } else if (Intrinsics.areEqual(uri2, ColumbusSettings.COLUMBUS_ACTION_URI)) {
                String selectedAction = this.this$0.selectedAction();
                for (ColumbusSettings.ColumbusSettingsChangeListener columbusSettingsChangeListener3 : this.this$0.listeners) {
                    columbusSettingsChangeListener3.onSelectedActionChange(selectedAction);
                }
                BackupManager.dataChangedForUser(this.this$0.userTracker.getUserId(), this.this$0.backupPackage);
            } else if (Intrinsics.areEqual(uri2, ColumbusSettings.COLUMBUS_LAUNCH_APP_URI)) {
                String selectedApp = this.this$0.selectedApp();
                for (ColumbusSettings.ColumbusSettingsChangeListener columbusSettingsChangeListener4 : this.this$0.listeners) {
                    columbusSettingsChangeListener4.onSelectedAppChange(selectedApp);
                }
                BackupManager.dataChangedForUser(this.this$0.userTracker.getUserId(), this.this$0.backupPackage);
            } else if (Intrinsics.areEqual(uri2, ColumbusSettings.COLUMBUS_LAUNCH_APP_SHORTCUT_URI)) {
                ColumbusSettings columbusSettings2 = this.this$0;
                Objects.requireNonNull(columbusSettings2);
                String stringForUser = Settings.Secure.getStringForUser(columbusSettings2.contentResolver, "columbus_launch_app_shortcut", columbusSettings2.userTracker.getUserId());
                if (stringForUser == null) {
                    stringForUser = "";
                }
                for (ColumbusSettings.ColumbusSettingsChangeListener columbusSettingsChangeListener5 : this.this$0.listeners) {
                    columbusSettingsChangeListener5.onSelectedAppShortcutChange(stringForUser);
                }
                BackupManager.dataChangedForUser(this.this$0.userTracker.getUserId(), this.this$0.backupPackage);
            } else if (Intrinsics.areEqual(uri2, ColumbusSettings.COLUMBUS_LOW_SENSITIVITY_URI)) {
                ColumbusSettings columbusSettings3 = this.this$0;
                Objects.requireNonNull(columbusSettings3);
                if (Settings.Secure.getIntForUser(columbusSettings3.contentResolver, "columbus_low_sensitivity", 0, columbusSettings3.userTracker.getUserId()) != 0) {
                    z = true;
                }
                for (ColumbusSettings.ColumbusSettingsChangeListener columbusSettingsChangeListener6 : this.this$0.listeners) {
                    columbusSettingsChangeListener6.onLowSensitivityChange(z);
                }
                BackupManager.dataChangedForUser(this.this$0.userTracker.getUserId(), this.this$0.backupPackage);
            } else if (Intrinsics.areEqual(uri2, ColumbusSettings.COLUMBUS_SILENCE_ALERTS_URI)) {
                ColumbusSettings columbusSettings4 = this.this$0;
                Objects.requireNonNull(columbusSettings4);
                if (Settings.Secure.getIntForUser(columbusSettings4.contentResolver, "columbus_silence_alerts", 1, columbusSettings4.userTracker.getUserId()) != 0) {
                    z = true;
                }
                for (ColumbusSettings.ColumbusSettingsChangeListener columbusSettingsChangeListener7 : this.this$0.listeners) {
                    columbusSettingsChangeListener7.onAlertSilenceEnabledChange(z);
                }
            } else {
                Log.w("Columbus/Settings", Intrinsics.stringPlus("Unknown setting change: ", uri2));
            }
        }
        return Unit.INSTANCE;
    }
}
