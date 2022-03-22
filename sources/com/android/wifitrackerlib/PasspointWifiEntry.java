package com.android.wifitrackerlib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import com.android.wifitrackerlib.WifiEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
/* loaded from: classes.dex */
public class PasspointWifiEntry extends WifiEntry implements WifiEntry.WifiEntryCallback {
    public final Context mContext;
    public final ArrayList mCurrentHomeScanResults;
    public final ArrayList mCurrentRoamingScanResults;
    public final String mFqdn;
    public final String mFriendlyName;
    public final WifiTrackerInjector mInjector;
    public final String mKey;
    public int mMeteredOverride;
    public OsuWifiEntry mOsuWifiEntry;
    public PasspointConfiguration mPasspointConfig;
    public boolean mShouldAutoOpenCaptivePortal;
    public long mSubscriptionExpirationTimeInMillis;
    public List<Integer> mTargetSecurityTypes;
    public WifiConfiguration mWifiConfig;

    public PasspointWifiEntry(WifiTrackerInjector wifiTrackerInjector, Context context, Handler handler, PasspointConfiguration passpointConfiguration, WifiManager wifiManager, boolean z) throws IllegalArgumentException {
        super(handler, wifiManager, z);
        this.mCurrentHomeScanResults = new ArrayList();
        this.mCurrentRoamingScanResults = new ArrayList();
        this.mTargetSecurityTypes = List.of(11, 12);
        this.mShouldAutoOpenCaptivePortal = false;
        this.mMeteredOverride = 0;
        Objects.requireNonNull(passpointConfiguration, "Cannot construct with null PasspointConfiguration!");
        this.mInjector = wifiTrackerInjector;
        this.mContext = context;
        this.mPasspointConfig = passpointConfiguration;
        this.mKey = uniqueIdToPasspointWifiEntryKey(passpointConfiguration.getUniqueId());
        String fqdn = passpointConfiguration.getHomeSp().getFqdn();
        this.mFqdn = fqdn;
        Objects.requireNonNull(fqdn, "Cannot construct with null PasspointConfiguration FQDN!");
        this.mFriendlyName = passpointConfiguration.getHomeSp().getFriendlyName();
        this.mSubscriptionExpirationTimeInMillis = passpointConfiguration.getSubscriptionExpirationTimeMillis();
        this.mMeteredOverride = this.mPasspointConfig.getMeteredOverride();
    }

    public final synchronized boolean canSetAutoJoinEnabled() {
        boolean z;
        if (this.mPasspointConfig == null) {
            if (this.mWifiConfig == null) {
                z = false;
            }
        }
        z = true;
        return z;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final synchronized boolean canSetMeteredChoice() {
        boolean z;
        if (!isSuggestion()) {
            if (this.mPasspointConfig != null) {
                z = true;
            }
        }
        z = false;
        return z;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final synchronized boolean canSignIn() {
        boolean z;
        NetworkCapabilities networkCapabilities = this.mNetworkCapabilities;
        if (networkCapabilities != null) {
            if (networkCapabilities.hasCapability(17)) {
                z = true;
            }
        }
        z = false;
        return z;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final synchronized void connect(InternetDialogController.WifiEntryConnectCallback wifiEntryConnectCallback) {
        OsuWifiEntry osuWifiEntry;
        if (!isExpired() || (osuWifiEntry = this.mOsuWifiEntry) == null) {
            this.mShouldAutoOpenCaptivePortal = true;
            this.mConnectCallback = wifiEntryConnectCallback;
            if (this.mWifiConfig == null) {
                new WifiEntry.ConnectActionListener().onFailure(0);
            }
            this.mWifiManager.stopRestrictingAutoJoinToSubscriptionId();
            this.mWifiManager.connect(this.mWifiConfig, new WifiEntry.ConnectActionListener());
            return;
        }
        osuWifiEntry.connect(wifiEntryConnectCallback);
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final synchronized int getConnectedState() {
        OsuWifiEntry osuWifiEntry;
        if (!isExpired() || super.getConnectedState() != 0 || (osuWifiEntry = this.mOsuWifiEntry) == null) {
            return super.getConnectedState();
        }
        return osuWifiEntry.getConnectedState();
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final synchronized int getMeteredChoice() {
        int i = this.mMeteredOverride;
        if (i == 1) {
            return 1;
        }
        if (i == 2) {
            return 2;
        }
        return 0;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final synchronized String getNetworkSelectionDescription() {
        return Utils.getNetworkSelectionDescription(this.mWifiConfig);
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final String getScanResultDescription() {
        return "";
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final synchronized List<Integer> getSecurityTypes() {
        return new ArrayList(this.mTargetSecurityTypes);
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final synchronized String getSummary(boolean z) {
        StringJoiner stringJoiner;
        String str;
        stringJoiner = new StringJoiner(this.mContext.getString(2131953628));
        if (isExpired()) {
            OsuWifiEntry osuWifiEntry = this.mOsuWifiEntry;
            if (osuWifiEntry != null) {
                stringJoiner.add(osuWifiEntry.getSummary(z));
            } else {
                stringJoiner.add(this.mContext.getString(2131953646));
            }
        } else {
            int connectedState = getConnectedState();
            if (connectedState == 0) {
                str = Utils.getDisconnectedDescription(this.mInjector, this.mContext, this.mWifiConfig, this.mForSavedNetworksPage, z);
            } else if (connectedState == 1) {
                str = Utils.getConnectingDescription(this.mContext, this.mNetworkInfo);
            } else if (connectedState != 2) {
                Log.e("PasspointWifiEntry", "getConnectedState() returned unknown state: " + connectedState);
                str = null;
            } else {
                str = Utils.getConnectedDescription(this.mContext, this.mWifiConfig, this.mNetworkCapabilities, this.mIsDefaultNetwork, this.mIsLowQuality);
            }
            if (!TextUtils.isEmpty(str)) {
                stringJoiner.add(str);
            }
        }
        Context context = this.mContext;
        String str2 = "";
        if (context != null && canSetAutoJoinEnabled() && !isAutoJoinEnabled()) {
            str2 = context.getString(2131953613);
        }
        if (!TextUtils.isEmpty(str2)) {
            stringJoiner.add(str2);
        }
        String meteredDescription = Utils.getMeteredDescription(this.mContext, this);
        if (!TextUtils.isEmpty(meteredDescription)) {
            stringJoiner.add(meteredDescription);
        }
        if (!z) {
            String verboseLoggingDescription = Utils.getVerboseLoggingDescription(this);
            if (!TextUtils.isEmpty(verboseLoggingDescription)) {
                stringJoiner.add(verboseLoggingDescription);
            }
        }
        return stringJoiner.toString();
    }

    public final synchronized boolean isAutoJoinEnabled() {
        PasspointConfiguration passpointConfiguration = this.mPasspointConfig;
        if (passpointConfiguration != null) {
            return passpointConfiguration.isAutojoinEnabled();
        }
        WifiConfiguration wifiConfiguration = this.mWifiConfig;
        if (wifiConfiguration == null) {
            return false;
        }
        return wifiConfiguration.allowAutojoin;
    }

    public final synchronized boolean isExpired() {
        boolean z = false;
        if (this.mSubscriptionExpirationTimeInMillis <= 0) {
            return false;
        }
        if (System.currentTimeMillis() >= this.mSubscriptionExpirationTimeInMillis) {
            z = true;
        }
        return z;
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x000e, code lost:
        if (r0.meteredHint != false) goto L_0x0012;
     */
    @Override // com.android.wifitrackerlib.WifiEntry
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final synchronized boolean isMetered() {
        /*
            r2 = this;
            monitor-enter(r2)
            int r0 = r2.getMeteredChoice()     // Catch: all -> 0x0014
            r1 = 1
            if (r0 == r1) goto L_0x0012
            android.net.wifi.WifiConfiguration r0 = r2.mWifiConfig     // Catch: all -> 0x0014
            if (r0 == 0) goto L_0x0011
            boolean r0 = r0.meteredHint     // Catch: all -> 0x0014
            if (r0 == 0) goto L_0x0011
            goto L_0x0012
        L_0x0011:
            r1 = 0
        L_0x0012:
            monitor-exit(r2)
            return r1
        L_0x0014:
            r0 = move-exception
            monitor-exit(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.PasspointWifiEntry.isMetered():boolean");
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final synchronized boolean isSubscription() {
        boolean z;
        if (this.mPasspointConfig != null) {
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final synchronized boolean isSuggestion() {
        boolean z;
        WifiConfiguration wifiConfiguration = this.mWifiConfig;
        if (wifiConfiguration != null) {
            if (wifiConfiguration.fromWifiNetworkSuggestion) {
                z = true;
            }
        }
        z = false;
        return z;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final synchronized void updateNetworkCapabilities(NetworkCapabilities networkCapabilities) {
        super.updateNetworkCapabilities(networkCapabilities);
        if (canSignIn() && this.mShouldAutoOpenCaptivePortal) {
            this.mShouldAutoOpenCaptivePortal = false;
            if (canSignIn()) {
                ((ConnectivityManager) this.mContext.getSystemService(ConnectivityManager.class)).startCaptivePortalApp(this.mWifiManager.getCurrentNetwork());
            }
        }
    }

    public final synchronized void updatePasspointConfig(PasspointConfiguration passpointConfiguration) {
        this.mPasspointConfig = passpointConfiguration;
        if (passpointConfiguration != null) {
            this.mSubscriptionExpirationTimeInMillis = passpointConfiguration.getSubscriptionExpirationTimeMillis();
            this.mMeteredOverride = passpointConfiguration.getMeteredOverride();
        }
        notifyOnUpdated();
    }

    public final synchronized void updateScanResultInfo(WifiConfiguration wifiConfiguration, List<ScanResult> list, List<ScanResult> list2) throws IllegalArgumentException {
        this.mWifiConfig = wifiConfiguration;
        this.mCurrentHomeScanResults.clear();
        this.mCurrentRoamingScanResults.clear();
        if (list != null) {
            this.mCurrentHomeScanResults.addAll(list);
        }
        if (list2 != null) {
            this.mCurrentRoamingScanResults.addAll(list2);
        }
        int i = -1;
        if (this.mWifiConfig != null) {
            ArrayList arrayList = new ArrayList();
            if (list != null && !list.isEmpty()) {
                arrayList.addAll(list);
            } else if (list2 != null && !list2.isEmpty()) {
                arrayList.addAll(list2);
            }
            ScanResult bestScanResultByLevel = Utils.getBestScanResultByLevel(arrayList);
            if (bestScanResultByLevel != null) {
                WifiConfiguration wifiConfiguration2 = this.mWifiConfig;
                wifiConfiguration2.SSID = "\"" + bestScanResultByLevel.SSID + "\"";
            }
            if (getConnectedState() == 0) {
                if (bestScanResultByLevel != null) {
                    i = this.mWifiManager.calculateSignalLevel(bestScanResultByLevel.level);
                }
                this.mLevel = i;
            }
        } else {
            this.mLevel = -1;
        }
        notifyOnUpdated();
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final synchronized void updateSecurityTypes() {
        int currentSecurityType;
        WifiInfo wifiInfo = this.mWifiInfo;
        if (wifiInfo != null && (currentSecurityType = wifiInfo.getCurrentSecurityType()) != -1) {
            this.mTargetSecurityTypes = Collections.singletonList(Integer.valueOf(currentSecurityType));
        }
    }

    public static String uniqueIdToPasspointWifiEntryKey(String str) {
        Objects.requireNonNull(str, "Cannot create key with null unique id!");
        return "PasspointWifiEntry:" + str;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final boolean connectionInfoMatches(WifiInfo wifiInfo) {
        if (!wifiInfo.isPasspointAp()) {
            return false;
        }
        return TextUtils.equals(wifiInfo.getPasspointFqdn(), this.mFqdn);
    }

    public PasspointWifiEntry(WifiTrackerInjector wifiTrackerInjector, Context context, Handler handler, WifiConfiguration wifiConfiguration, WifiManager wifiManager) throws IllegalArgumentException {
        super(handler, wifiManager, false);
        this.mCurrentHomeScanResults = new ArrayList();
        this.mCurrentRoamingScanResults = new ArrayList();
        this.mTargetSecurityTypes = List.of(11, 12);
        this.mShouldAutoOpenCaptivePortal = false;
        this.mMeteredOverride = 0;
        Objects.requireNonNull(wifiConfiguration, "Cannot construct with null WifiConfiguration!");
        if (wifiConfiguration.isPasspoint()) {
            this.mInjector = wifiTrackerInjector;
            this.mContext = context;
            this.mWifiConfig = wifiConfiguration;
            this.mKey = uniqueIdToPasspointWifiEntryKey(wifiConfiguration.getKey());
            String str = wifiConfiguration.FQDN;
            this.mFqdn = str;
            Objects.requireNonNull(str, "Cannot construct with null WifiConfiguration FQDN!");
            this.mFriendlyName = this.mWifiConfig.providerFriendlyName;
            return;
        }
        throw new IllegalArgumentException("Given WifiConfiguration is not for Passpoint!");
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final String getKey() {
        return this.mKey;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final String getTitle() {
        return this.mFriendlyName;
    }

    @Override // com.android.wifitrackerlib.WifiEntry.WifiEntryCallback
    public final void onUpdated() {
        notifyOnUpdated();
    }
}
