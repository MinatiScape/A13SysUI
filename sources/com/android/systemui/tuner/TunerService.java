package com.android.systemui.tuner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda0;
import com.android.systemui.Dependency;
/* loaded from: classes.dex */
public abstract class TunerService {

    /* loaded from: classes.dex */
    public interface Tunable {
        void onTuningChanged(String str, String str2);
    }

    public abstract void addTunable(Tunable tunable, String... strArr);

    public abstract void clearAll();

    public abstract int getValue(String str, int i);

    public abstract String getValue(String str);

    public abstract void removeTunable(Tunable tunable);

    public abstract void setValue(String str, int i);

    public abstract void setValue(String str, String str2);

    public abstract void showResetRequest(CarrierTextManager$$ExternalSyntheticLambda0 carrierTextManager$$ExternalSyntheticLambda0);

    /* loaded from: classes.dex */
    public static class ClearReceiver extends BroadcastReceiver {
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            if ("com.android.systemui.action.CLEAR_TUNER".equals(intent.getAction())) {
                ((TunerService) Dependency.get(TunerService.class)).clearAll();
            }
        }
    }

    public static boolean parseIntegerSwitch(String str, boolean z) {
        if (str == null) {
            return z;
        }
        try {
            if (Integer.parseInt(str) != 0) {
                return true;
            }
            return false;
        } catch (NumberFormatException unused) {
            return z;
        }
    }
}
