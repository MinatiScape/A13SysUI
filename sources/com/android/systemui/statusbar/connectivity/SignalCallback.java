package com.android.systemui.statusbar.connectivity;

import android.telephony.SubscriptionInfo;
import java.util.List;
/* compiled from: SignalCallback.kt */
/* loaded from: classes.dex */
public interface SignalCallback {
    default void setCallIndicator(IconState iconState, int i) {
    }

    default void setConnectivityStatus(boolean z, boolean z2, boolean z3) {
    }

    default void setEthernetIndicators(IconState iconState) {
    }

    default void setIsAirplaneMode(IconState iconState) {
    }

    default void setMobileDataEnabled(boolean z) {
    }

    default void setMobileDataIndicators(MobileDataIndicators mobileDataIndicators) {
    }

    default void setNoSims(boolean z, boolean z2) {
    }

    default void setSubs(List<SubscriptionInfo> list) {
    }

    default void setWifiIndicators(WifiIndicators wifiIndicators) {
    }
}
