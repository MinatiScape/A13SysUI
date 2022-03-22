package com.android.settingslib.deviceinfo;
/* loaded from: classes.dex */
public abstract class AbstractBluetoothAddressPreferenceController extends AbstractConnectivityPreferenceController {
    public static final String[] CONNECTIVITY_INTENTS = {"android.bluetooth.adapter.action.STATE_CHANGED"};
    public static final String KEY_BT_ADDRESS = "bt_address";

    @Override // com.android.settingslib.deviceinfo.AbstractConnectivityPreferenceController
    public final String[] getConnectivityIntents() {
        return CONNECTIVITY_INTENTS;
    }
}
