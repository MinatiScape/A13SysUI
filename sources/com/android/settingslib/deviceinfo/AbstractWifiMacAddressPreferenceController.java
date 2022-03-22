package com.android.settingslib.deviceinfo;
/* loaded from: classes.dex */
public abstract class AbstractWifiMacAddressPreferenceController extends AbstractConnectivityPreferenceController {
    public static final String[] CONNECTIVITY_INTENTS = {"android.net.conn.CONNECTIVITY_CHANGE", "android.net.wifi.LINK_CONFIGURATION_CHANGED", "android.net.wifi.STATE_CHANGE"};
    public static final String KEY_WIFI_MAC_ADDRESS = "wifi_mac_address";
    public static final int OFF = 0;
    public static final int ON = 1;

    @Override // com.android.settingslib.deviceinfo.AbstractConnectivityPreferenceController
    public final String[] getConnectivityIntents() {
        return CONNECTIVITY_INTENTS;
    }
}
