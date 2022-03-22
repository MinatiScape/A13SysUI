package com.google.android.systemui.reversecharging;

import android.util.Log;
import java.util.Objects;
import vendor.google.wireless_charger.V1_2.IWirelessCharger;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ReverseChargingController$$ExternalSyntheticLambda8 implements Runnable {
    public final /* synthetic */ ReverseChargingController f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ ReverseChargingController$$ExternalSyntheticLambda8(ReverseChargingController reverseChargingController, boolean z) {
        this.f$0 = reverseChargingController;
        this.f$1 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ReverseChargingController reverseChargingController = this.f$0;
        boolean z = this.f$1;
        Objects.requireNonNull(reverseChargingController);
        Log.i("ReverseChargingControl", "setRtxMode(): rtx=" + (z ? 1 : 0));
        ReverseWirelessCharger reverseWirelessCharger = reverseChargingController.mRtxChargerManagerOptional.get();
        Objects.requireNonNull(reverseWirelessCharger);
        reverseWirelessCharger.initHALInterface();
        IWirelessCharger iWirelessCharger = reverseWirelessCharger.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                iWirelessCharger.setRtxMode(z);
            } catch (Exception e) {
                Log.i("ReverseWirelessCharger", "setRtxMode fail: ", e);
            }
        }
    }
}
