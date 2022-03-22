package com.android.settingslib.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkKey;
import android.net.NetworkRequest;
import android.net.NetworkScoreManager;
import android.net.ScoredNetwork;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkScoreCache;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import com.android.settingslib.Utils;
import com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda3;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class WifiStatusTracker {
    public static final SimpleDateFormat SSDF = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
    public boolean connected;
    public boolean enabled;
    public boolean isCaptivePortal;
    public boolean isCarrierMerged;
    public boolean isDefaultNetwork;
    public int level;
    public final AnonymousClass1 mCacheListener;
    public final Runnable mCallback;
    public final ConnectivityManager mConnectivityManager;
    public final Context mContext;
    public final Handler mHandler;
    public int mHistoryIndex;
    public final NetworkScoreManager mNetworkScoreManager;
    public WifiInfo mWifiInfo;
    public final WifiManager mWifiManager;
    public final WifiNetworkScoreCache mWifiNetworkScoreCache;
    public int rssi;
    public String ssid;
    public String statusLabel;
    public int subId;
    public final HashSet mNetworks = new HashSet();
    public final String[] mHistory = new String[32];
    public final NetworkRequest mNetworkRequest = new NetworkRequest.Builder().clearCapabilities().addCapability(15).addTransportType(1).addTransportType(0).build();
    public final AnonymousClass2 mNetworkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.android.settingslib.wifi.WifiStatusTracker.2
        @Override // android.net.ConnectivityManager.NetworkCallback
        public final void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            WifiInfo wifiInfo;
            boolean z;
            boolean z2;
            boolean z3 = false;
            if (networkCapabilities.hasTransport(0)) {
                WifiInfo tryGetWifiInfoForVcn = Utils.tryGetWifiInfoForVcn(networkCapabilities);
                if (tryGetWifiInfoForVcn != null) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                z = false;
                z3 = z2;
                wifiInfo = tryGetWifiInfoForVcn;
            } else if (networkCapabilities.hasTransport(1)) {
                wifiInfo = (WifiInfo) networkCapabilities.getTransportInfo();
                z = true;
            } else {
                wifiInfo = null;
                z = false;
            }
            if (z3 || z) {
                WifiStatusTracker wifiStatusTracker = WifiStatusTracker.this;
                Objects.requireNonNull(wifiStatusTracker);
                String[] strArr = wifiStatusTracker.mHistory;
                int i = wifiStatusTracker.mHistoryIndex;
                strArr[i] = WifiStatusTracker.SSDF.format(Long.valueOf(System.currentTimeMillis())) + ",onCapabilitiesChanged: network=" + network + ",networkCapabilities=" + networkCapabilities;
                wifiStatusTracker.mHistoryIndex = (i + 1) % 32;
            }
            if (wifiInfo != null && wifiInfo.isPrimary()) {
                if (!WifiStatusTracker.this.mNetworks.contains(Integer.valueOf(network.getNetId()))) {
                    WifiStatusTracker.this.mNetworks.add(Integer.valueOf(network.getNetId()));
                }
                WifiStatusTracker.m23$$Nest$mupdateWifiInfo(WifiStatusTracker.this, wifiInfo);
                WifiStatusTracker.this.updateStatusLabel();
                WifiStatusTracker.this.mCallback.run();
            } else if (WifiStatusTracker.this.mNetworks.contains(Integer.valueOf(network.getNetId()))) {
                WifiStatusTracker.this.mNetworks.remove(Integer.valueOf(network.getNetId()));
            }
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public final void onLost(Network network) {
            WifiStatusTracker wifiStatusTracker = WifiStatusTracker.this;
            Objects.requireNonNull(wifiStatusTracker);
            String[] strArr = wifiStatusTracker.mHistory;
            int i = wifiStatusTracker.mHistoryIndex;
            strArr[i] = WifiStatusTracker.SSDF.format(Long.valueOf(System.currentTimeMillis())) + ",onLost: network=" + network;
            wifiStatusTracker.mHistoryIndex = (i + 1) % 32;
            if (WifiStatusTracker.this.mNetworks.contains(Integer.valueOf(network.getNetId()))) {
                WifiStatusTracker.this.mNetworks.remove(Integer.valueOf(network.getNetId()));
                WifiStatusTracker.m23$$Nest$mupdateWifiInfo(WifiStatusTracker.this, null);
                WifiStatusTracker.this.updateStatusLabel();
                WifiStatusTracker.this.mCallback.run();
            }
        }
    };
    public final AnonymousClass3 mDefaultNetworkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.android.settingslib.wifi.WifiStatusTracker.3
        @Override // android.net.ConnectivityManager.NetworkCallback
        public final void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            Objects.requireNonNull(WifiStatusTracker.this);
            WifiStatusTracker wifiStatusTracker = WifiStatusTracker.this;
            wifiStatusTracker.mDefaultNetworkCapabilities = networkCapabilities;
            wifiStatusTracker.updateStatusLabel();
            WifiStatusTracker.this.mCallback.run();
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public final void onLost(Network network) {
            Objects.requireNonNull(WifiStatusTracker.this);
            WifiStatusTracker wifiStatusTracker = WifiStatusTracker.this;
            wifiStatusTracker.mDefaultNetworkCapabilities = null;
            wifiStatusTracker.updateStatusLabel();
            WifiStatusTracker.this.mCallback.run();
        }
    };
    public NetworkCapabilities mDefaultNetworkCapabilities = null;

    public final void updateStatusLabel() {
        NetworkCapabilities networkCapabilities;
        String str;
        NetworkCapabilities networkCapabilities2;
        boolean z;
        if (this.mWifiManager != null) {
            int i = 0;
            this.isDefaultNetwork = false;
            NetworkCapabilities networkCapabilities3 = this.mDefaultNetworkCapabilities;
            if (networkCapabilities3 != null) {
                boolean hasTransport = networkCapabilities3.hasTransport(1);
                if (!this.mDefaultNetworkCapabilities.hasTransport(0) || Utils.tryGetWifiInfoForVcn(this.mDefaultNetworkCapabilities) == null) {
                    z = false;
                } else {
                    z = true;
                }
                if (hasTransport || z) {
                    this.isDefaultNetwork = true;
                }
            }
            if (this.isDefaultNetwork) {
                networkCapabilities = this.mDefaultNetworkCapabilities;
            } else {
                networkCapabilities = this.mConnectivityManager.getNetworkCapabilities(this.mWifiManager.getCurrentNetwork());
            }
            this.isCaptivePortal = false;
            if (networkCapabilities != null) {
                if (networkCapabilities.hasCapability(17)) {
                    this.statusLabel = this.mContext.getString(2131953606);
                    this.isCaptivePortal = true;
                    return;
                } else if (networkCapabilities.hasCapability(24)) {
                    this.statusLabel = this.mContext.getString(2131953561);
                    return;
                } else if (!networkCapabilities.hasCapability(16)) {
                    Settings.Global.getString(this.mContext.getContentResolver(), "private_dns_mode");
                    if (networkCapabilities.isPrivateDnsBroken()) {
                        this.statusLabel = this.mContext.getString(2131953026);
                        return;
                    } else {
                        this.statusLabel = this.mContext.getString(2131953605);
                        return;
                    }
                } else if (!this.isDefaultNetwork && (networkCapabilities2 = this.mDefaultNetworkCapabilities) != null && networkCapabilities2.hasTransport(0)) {
                    this.statusLabel = this.mContext.getString(2131953535);
                    return;
                }
            }
            ScoredNetwork scoredNetwork = this.mWifiNetworkScoreCache.getScoredNetwork(NetworkKey.createFromWifiInfo(this.mWifiInfo));
            if (scoredNetwork == null) {
                str = null;
            } else {
                Context context = this.mContext;
                int i2 = this.rssi;
                int i3 = AccessPoint.$r8$clinit;
                int calculateBadge = scoredNetwork.calculateBadge(i2);
                if (calculateBadge >= 5) {
                    if (calculateBadge < 7) {
                        i = 5;
                    } else if (calculateBadge < 15) {
                        i = 10;
                    } else if (calculateBadge < 25) {
                        i = 20;
                    } else {
                        i = 30;
                    }
                }
                str = AccessPoint.getSpeedLabel(context, i);
            }
            this.statusLabel = str;
        }
    }

    public final void updateWifiState() {
        boolean z;
        if (this.mWifiManager.getWifiState() == 3) {
            z = true;
        } else {
            z = false;
        }
        this.enabled = z;
    }

    /* renamed from: -$$Nest$mupdateWifiInfo  reason: not valid java name */
    public static void m23$$Nest$mupdateWifiInfo(WifiStatusTracker wifiStatusTracker, WifiInfo wifiInfo) {
        boolean z;
        Objects.requireNonNull(wifiStatusTracker);
        wifiStatusTracker.updateWifiState();
        if (wifiInfo != null) {
            z = true;
        } else {
            z = false;
        }
        wifiStatusTracker.connected = z;
        wifiStatusTracker.mWifiInfo = wifiInfo;
        String str = null;
        wifiStatusTracker.ssid = null;
        if (wifiInfo != null) {
            if (wifiInfo.isPasspointAp() || wifiStatusTracker.mWifiInfo.isOsuAp()) {
                wifiStatusTracker.ssid = wifiStatusTracker.mWifiInfo.getPasspointProviderFriendlyName();
            } else {
                String ssid = wifiStatusTracker.mWifiInfo.getSSID();
                if (ssid != null && !"<unknown ssid>".equals(ssid)) {
                    str = ssid;
                }
                wifiStatusTracker.ssid = str;
            }
            wifiStatusTracker.isCarrierMerged = wifiStatusTracker.mWifiInfo.isCarrierMerged();
            wifiStatusTracker.subId = wifiStatusTracker.mWifiInfo.getSubscriptionId();
            int rssi = wifiStatusTracker.mWifiInfo.getRssi();
            wifiStatusTracker.rssi = rssi;
            wifiStatusTracker.level = wifiStatusTracker.mWifiManager.calculateSignalLevel(rssi);
            NetworkKey createFromWifiInfo = NetworkKey.createFromWifiInfo(wifiStatusTracker.mWifiInfo);
            if (wifiStatusTracker.mWifiNetworkScoreCache.getScoredNetwork(createFromWifiInfo) == null) {
                wifiStatusTracker.mNetworkScoreManager.requestScores(new NetworkKey[]{createFromWifiInfo});
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v7, types: [com.android.settingslib.wifi.WifiStatusTracker$2] */
    /* JADX WARN: Type inference failed for: r0v8, types: [com.android.settingslib.wifi.WifiStatusTracker$3] */
    public WifiStatusTracker(Context context, WifiManager wifiManager, NetworkScoreManager networkScoreManager, ConnectivityManager connectivityManager, PipTaskOrganizer$$ExternalSyntheticLambda3 pipTaskOrganizer$$ExternalSyntheticLambda3) {
        Handler handler = new Handler(Looper.getMainLooper());
        this.mHandler = handler;
        this.mCacheListener = new WifiNetworkScoreCache.CacheListener(handler) { // from class: com.android.settingslib.wifi.WifiStatusTracker.1
            public final void networkCacheUpdated(List<ScoredNetwork> list) {
                WifiStatusTracker.this.updateStatusLabel();
                WifiStatusTracker.this.mCallback.run();
            }
        };
        this.mContext = context;
        this.mWifiManager = wifiManager;
        this.mWifiNetworkScoreCache = new WifiNetworkScoreCache(context);
        this.mNetworkScoreManager = networkScoreManager;
        this.mConnectivityManager = connectivityManager;
        this.mCallback = pipTaskOrganizer$$ExternalSyntheticLambda3;
    }
}
