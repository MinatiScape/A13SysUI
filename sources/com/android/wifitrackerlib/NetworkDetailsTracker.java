package com.android.wifitrackerlib;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import androidx.lifecycle.Lifecycle;
import java.time.Clock;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class NetworkDetailsTracker extends BaseWifiTracker {
    public NetworkInfo mCurrentNetworkInfo;

    public NetworkDetailsTracker(WifiTrackerInjector wifiTrackerInjector, Lifecycle lifecycle, Context context, WifiManager wifiManager, ConnectivityManager connectivityManager, Handler handler, Handler handler2, Clock clock, long j, long j2, String str) {
        super(wifiTrackerInjector, lifecycle, context, wifiManager, connectivityManager, handler, handler2, clock, j, j2, null, str);
    }

    public abstract WifiEntry getWifiEntry();

    public static NetworkDetailsTracker createNetworkDetailsTracker(WifiTrackerInjector wifiTrackerInjector, Lifecycle lifecycle, Context context, WifiManager wifiManager, ConnectivityManager connectivityManager, Handler handler, Handler handler2, Clock clock, long j, long j2, String str) {
        if (str.startsWith("StandardWifiEntry:")) {
            return new StandardNetworkDetailsTracker(wifiTrackerInjector, lifecycle, context, wifiManager, connectivityManager, handler, handler2, clock, j, j2, str);
        }
        if (str.startsWith("PasspointWifiEntry:")) {
            return new PasspointNetworkDetailsTracker(wifiTrackerInjector, lifecycle, context, wifiManager, connectivityManager, handler, handler2, clock, j, j2, str);
        }
        throw new IllegalArgumentException("Key does not contain valid key prefix!");
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    public final void handleNetworkStateChangedAction(Intent intent) {
        Objects.requireNonNull(intent, "Intent cannot be null!");
        this.mCurrentNetworkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
        getWifiEntry().updateConnectionInfo(this.mWifiManager.getConnectionInfo(), this.mCurrentNetworkInfo);
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    public final void handleDefaultRouteChanged() {
        boolean z;
        WifiEntry wifiEntry = getWifiEntry();
        if (wifiEntry.getConnectedState() == 2) {
            wifiEntry.setIsDefaultNetwork(this.mIsWifiDefaultRoute);
            if (!this.mIsWifiValidated || !this.mIsCellDefaultRoute) {
                z = false;
            } else {
                z = true;
            }
            wifiEntry.setIsLowQuality(z);
        }
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    public final void handleLinkPropertiesChanged(LinkProperties linkProperties) {
        WifiEntry wifiEntry = getWifiEntry();
        if (wifiEntry.getConnectedState() == 2) {
            wifiEntry.updateLinkProperties(linkProperties);
        }
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    public final void handleNetworkCapabilitiesChanged(NetworkCapabilities networkCapabilities) {
        boolean z;
        WifiEntry wifiEntry = getWifiEntry();
        if (wifiEntry.getConnectedState() == 2) {
            wifiEntry.updateNetworkCapabilities(networkCapabilities);
            if (!this.mIsWifiValidated || !this.mIsCellDefaultRoute) {
                z = false;
            } else {
                z = true;
            }
            wifiEntry.setIsLowQuality(z);
        }
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    public final void handleRssiChangedAction() {
        getWifiEntry().updateConnectionInfo(this.mWifiManager.getConnectionInfo(), this.mCurrentNetworkInfo);
    }
}
