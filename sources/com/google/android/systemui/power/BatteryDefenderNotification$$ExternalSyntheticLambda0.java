package com.google.android.systemui.power;

import android.os.IHwBinder;
import android.util.Log;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BatteryDefenderNotification$$ExternalSyntheticLambda0 implements IHwBinder.DeathRecipient {
    public static final /* synthetic */ BatteryDefenderNotification$$ExternalSyntheticLambda0 INSTANCE = new BatteryDefenderNotification$$ExternalSyntheticLambda0();

    public final void serviceDied(long j) {
        Log.d("BatteryDefenderNotification", "IHwBinder serviceDied");
    }
}
