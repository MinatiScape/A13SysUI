package com.android.settingslib.fuelgauge;

import android.content.Intent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
/* loaded from: classes.dex */
public final class BatteryStatus {
    public final int health;
    public final int level;
    public final int maxChargingWattage;
    public final int plugged;
    public final boolean present;
    public final int status;

    public BatteryStatus() {
        this.status = 1;
        this.level = 100;
        this.plugged = 0;
        this.health = 0;
        this.maxChargingWattage = 0;
        this.present = true;
    }

    public final boolean isPluggedIn() {
        int i = this.plugged;
        if (i == 1 || i == 2 || i == 4 || i == 8) {
            return true;
        }
        return false;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("BatteryStatus{status=");
        m.append(this.status);
        m.append(",level=");
        m.append(this.level);
        m.append(",plugged=");
        m.append(this.plugged);
        m.append(",health=");
        m.append(this.health);
        m.append(",maxChargingWattage=");
        m.append(this.maxChargingWattage);
        m.append("}");
        return m.toString();
    }

    public BatteryStatus(Intent intent) {
        this.status = intent.getIntExtra("status", 1);
        this.plugged = intent.getIntExtra("plugged", 0);
        this.level = intent.getIntExtra("level", 0);
        this.health = intent.getIntExtra("health", 1);
        this.present = intent.getBooleanExtra("present", true);
        int intExtra = intent.getIntExtra("max_charging_current", -1);
        int intExtra2 = intent.getIntExtra("max_charging_voltage", -1);
        intExtra2 = intExtra2 <= 0 ? 5000000 : intExtra2;
        if (intExtra > 0) {
            this.maxChargingWattage = (intExtra2 / 1000) * (intExtra / 1000);
        } else {
            this.maxChargingWattage = -1;
        }
    }
}
