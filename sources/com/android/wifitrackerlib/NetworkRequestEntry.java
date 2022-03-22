package com.android.wifitrackerlib;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import com.android.wifitrackerlib.StandardWifiEntry;
/* loaded from: classes.dex */
public class NetworkRequestEntry extends StandardWifiEntry {
    public NetworkRequestEntry(WifiTrackerInjector wifiTrackerInjector, Context context, Handler handler, StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey, WifiManager wifiManager) throws IllegalArgumentException {
        super(wifiTrackerInjector, context, handler, standardWifiEntryKey, wifiManager, false);
    }

    @Override // com.android.wifitrackerlib.StandardWifiEntry
    public final boolean canSetAutoJoinEnabled() {
        return false;
    }

    @Override // com.android.wifitrackerlib.StandardWifiEntry, com.android.wifitrackerlib.WifiEntry
    public final boolean canSetMeteredChoice() {
        return false;
    }

    @Override // com.android.wifitrackerlib.StandardWifiEntry, com.android.wifitrackerlib.WifiEntry
    public final void connect(InternetDialogController.WifiEntryConnectCallback wifiEntryConnectCallback) {
    }

    @Override // com.android.wifitrackerlib.StandardWifiEntry, com.android.wifitrackerlib.WifiEntry
    public final int getMeteredChoice() {
        return 0;
    }

    @Override // com.android.wifitrackerlib.StandardWifiEntry, com.android.wifitrackerlib.WifiEntry
    public final WifiConfiguration getWifiConfiguration() {
        return null;
    }

    @Override // com.android.wifitrackerlib.StandardWifiEntry
    public final boolean isAutoJoinEnabled() {
        return true;
    }

    @Override // com.android.wifitrackerlib.StandardWifiEntry, com.android.wifitrackerlib.WifiEntry
    public final boolean isMetered() {
        return false;
    }

    @Override // com.android.wifitrackerlib.StandardWifiEntry, com.android.wifitrackerlib.WifiEntry
    public final boolean isSaved() {
        return false;
    }

    @Override // com.android.wifitrackerlib.StandardWifiEntry, com.android.wifitrackerlib.WifiEntry
    public final boolean isSuggestion() {
        return false;
    }
}
