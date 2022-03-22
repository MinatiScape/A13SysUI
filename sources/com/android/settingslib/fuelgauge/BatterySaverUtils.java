package com.android.settingslib.fuelgauge;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.KeyValueListParser;
import android.util.Slog;
import com.android.systemui.theme.ThemeOverlayApplier;
/* loaded from: classes.dex */
public final class BatterySaverUtils {
    public static synchronized boolean setPowerSaveMode(Context context, boolean z, boolean z2) {
        boolean z3;
        synchronized (BatterySaverUtils.class) {
            ContentResolver contentResolver = context.getContentResolver();
            Bundle bundle = new Bundle(1);
            bundle.putBoolean("extra_confirm_only", false);
            if (z && z2) {
                if (Settings.Secure.getInt(context.getContentResolver(), "low_power_warning_acknowledged", 0) != 0) {
                    z3 = false;
                } else {
                    Intent intent = new Intent("PNW.startSaverConfirmation");
                    intent.setFlags(268435456);
                    intent.setPackage(ThemeOverlayApplier.SYSUI_PACKAGE);
                    intent.putExtras(bundle);
                    context.sendBroadcast(intent);
                    z3 = true;
                }
                if (z3) {
                    return false;
                }
            }
            if (z && !z2) {
                Settings.Secure.putIntForUser(context.getContentResolver(), "low_power_warning_acknowledged", 1, -2);
            }
            if (!((PowerManager) context.getSystemService(PowerManager.class)).setPowerSaveModeEnabled(z)) {
                return false;
            }
            if (z) {
                int i = Settings.Secure.getInt(contentResolver, "low_power_manual_activation_count", 0) + 1;
                Settings.Secure.putInt(contentResolver, "low_power_manual_activation_count", i);
                String string = Settings.Global.getString(context.getContentResolver(), "low_power_mode_suggestion_params");
                KeyValueListParser keyValueListParser = new KeyValueListParser(',');
                try {
                    keyValueListParser.setString(string);
                } catch (IllegalArgumentException unused) {
                    Slog.wtf("BatterySaverUtils", "Bad constants: " + string);
                }
                int i2 = keyValueListParser.getInt("start_nth", 4);
                int i3 = keyValueListParser.getInt("end_nth", 8);
                if (i >= i2 && i <= i3 && Settings.Global.getInt(contentResolver, "low_power_trigger_level", 0) == 0 && Settings.Secure.getInt(contentResolver, "suppress_auto_battery_saver_suggestion", 0) == 0) {
                    Intent intent2 = new Intent("PNW.autoSaverSuggestion");
                    intent2.setFlags(268435456);
                    intent2.setPackage(ThemeOverlayApplier.SYSUI_PACKAGE);
                    intent2.putExtras(bundle);
                    context.sendBroadcast(intent2);
                }
            }
            return true;
        }
    }
}
