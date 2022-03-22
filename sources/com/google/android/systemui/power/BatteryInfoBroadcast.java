package com.google.android.systemui.power;

import android.content.Context;
import android.os.PowerManager;
/* loaded from: classes.dex */
public final class BatteryInfoBroadcast {
    public final Context mContext;
    public final PowerManager mPowerManager;

    public BatteryInfoBroadcast(Context context) {
        this.mContext = context;
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
    }
}
