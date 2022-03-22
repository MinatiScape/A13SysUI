package com.android.wifitrackerlib;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.hotspot2.OsuProvider;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Pair;
import androidx.lifecycle.Lifecycle;
import com.android.systemui.theme.ThemeOverlayApplier$$ExternalSyntheticLambda4;
import com.android.systemui.theme.ThemeOverlayApplier$$ExternalSyntheticLambda5;
import com.android.systemui.theme.ThemeOverlayApplier$$ExternalSyntheticLambda6;
import com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda4;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public class PasspointNetworkDetailsTracker extends NetworkDetailsTracker {
    public final PasspointWifiEntry mChosenEntry;
    public WifiConfiguration mCurrentWifiConfig;
    public OsuWifiEntry mOsuWifiEntry;

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    public final void handleWifiStateChangedAction() {
        conditionallyUpdateScanResults(true);
    }

    public final void updateStartInfo() {
        boolean z = true;
        conditionallyUpdateScanResults(true);
        conditionallyUpdateConfig();
        WifiInfo connectionInfo = this.mWifiManager.getConnectionInfo();
        Network currentNetwork = this.mWifiManager.getCurrentNetwork();
        this.mChosenEntry.updateConnectionInfo(connectionInfo, this.mConnectivityManager.getNetworkInfo(currentNetwork));
        handleNetworkCapabilitiesChanged(this.mConnectivityManager.getNetworkCapabilities(currentNetwork));
        handleLinkPropertiesChanged(this.mConnectivityManager.getLinkProperties(currentNetwork));
        this.mChosenEntry.setIsDefaultNetwork(this.mIsWifiDefaultRoute);
        PasspointWifiEntry passpointWifiEntry = this.mChosenEntry;
        if (!this.mIsWifiValidated || !this.mIsCellDefaultRoute) {
            z = false;
        }
        passpointWifiEntry.setIsLowQuality(z);
    }

    public PasspointNetworkDetailsTracker(WifiTrackerInjector wifiTrackerInjector, Lifecycle lifecycle, Context context, WifiManager wifiManager, ConnectivityManager connectivityManager, Handler handler, Handler handler2, Clock clock, long j, long j2, String str) {
        super(wifiTrackerInjector, lifecycle, context, wifiManager, connectivityManager, handler, handler2, clock, j, j2, "PasspointNetworkDetailsTracker");
        Optional<PasspointConfiguration> findAny = this.mWifiManager.getPasspointConfigurations().stream().filter(new ThemeOverlayApplier$$ExternalSyntheticLambda5(str, 2)).findAny();
        if (findAny.isPresent()) {
            this.mChosenEntry = new PasspointWifiEntry(this.mInjector, this.mContext, this.mMainHandler, findAny.get(), this.mWifiManager, false);
        } else {
            Optional findAny2 = this.mWifiManager.getPrivilegedConfiguredNetworks().stream().filter(new ThemeOverlayApplier$$ExternalSyntheticLambda6(str, 2)).findAny();
            if (findAny2.isPresent()) {
                this.mChosenEntry = new PasspointWifiEntry(this.mInjector, this.mContext, this.mMainHandler, (WifiConfiguration) findAny2.get(), this.mWifiManager);
            } else {
                throw new IllegalArgumentException("Cannot find config for given PasspointWifiEntry key!");
            }
        }
        updateStartInfo();
    }

    public final void conditionallyUpdateConfig() {
        this.mWifiManager.getPasspointConfigurations().stream().filter(new ThemeOverlayApplier$$ExternalSyntheticLambda4(this, 2)).findAny().ifPresent(new CreateUserActivity$$ExternalSyntheticLambda4(this, 2));
    }

    public final void conditionallyUpdateScanResults(boolean z) {
        if (this.mWifiManager.getWifiState() == 1) {
            this.mChosenEntry.updateScanResultInfo(this.mCurrentWifiConfig, null, null);
            return;
        }
        long j = this.mMaxScanAgeMillis;
        if (z) {
            this.mScanResultUpdater.update(this.mWifiManager.getScanResults());
        } else {
            j += this.mScanIntervalMillis;
        }
        ArrayList scanResults = this.mScanResultUpdater.getScanResults(j);
        Iterator it = this.mWifiManager.getAllMatchingWifiConfigs(scanResults).iterator();
        while (true) {
            if (!it.hasNext()) {
                this.mChosenEntry.updateScanResultInfo(this.mCurrentWifiConfig, null, null);
                break;
            }
            Pair pair = (Pair) it.next();
            WifiConfiguration wifiConfiguration = (WifiConfiguration) pair.first;
            String uniqueIdToPasspointWifiEntryKey = PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(wifiConfiguration.getKey());
            PasspointWifiEntry passpointWifiEntry = this.mChosenEntry;
            Objects.requireNonNull(passpointWifiEntry);
            if (TextUtils.equals(uniqueIdToPasspointWifiEntryKey, passpointWifiEntry.mKey)) {
                this.mCurrentWifiConfig = wifiConfiguration;
                this.mChosenEntry.updateScanResultInfo(wifiConfiguration, (List) ((Map) pair.second).get(0), (List) ((Map) pair.second).get(1));
                break;
            }
        }
        Map matchingOsuProviders = this.mWifiManager.getMatchingOsuProviders(scanResults);
        Map matchingPasspointConfigsForOsuProviders = this.mWifiManager.getMatchingPasspointConfigsForOsuProviders(matchingOsuProviders.keySet());
        OsuWifiEntry osuWifiEntry = this.mOsuWifiEntry;
        if (osuWifiEntry != null) {
            osuWifiEntry.updateScanResultInfo((List) matchingOsuProviders.get(osuWifiEntry.mOsuProvider));
        } else {
            for (OsuProvider osuProvider : matchingOsuProviders.keySet()) {
                PasspointConfiguration passpointConfiguration = (PasspointConfiguration) matchingPasspointConfigsForOsuProviders.get(osuProvider);
                if (passpointConfiguration != null) {
                    PasspointWifiEntry passpointWifiEntry2 = this.mChosenEntry;
                    Objects.requireNonNull(passpointWifiEntry2);
                    if (TextUtils.equals(passpointWifiEntry2.mKey, PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(passpointConfiguration.getUniqueId()))) {
                        OsuWifiEntry osuWifiEntry2 = new OsuWifiEntry(this.mInjector, this.mContext, this.mMainHandler, osuProvider, this.mWifiManager);
                        this.mOsuWifiEntry = osuWifiEntry2;
                        osuWifiEntry2.updateScanResultInfo((List) matchingOsuProviders.get(osuProvider));
                        OsuWifiEntry osuWifiEntry3 = this.mOsuWifiEntry;
                        Objects.requireNonNull(osuWifiEntry3);
                        synchronized (osuWifiEntry3) {
                            osuWifiEntry3.mIsAlreadyProvisioned = true;
                        }
                        PasspointWifiEntry passpointWifiEntry3 = this.mChosenEntry;
                        OsuWifiEntry osuWifiEntry4 = this.mOsuWifiEntry;
                        Objects.requireNonNull(passpointWifiEntry3);
                        synchronized (passpointWifiEntry3) {
                            passpointWifiEntry3.mOsuWifiEntry = osuWifiEntry4;
                            if (osuWifiEntry4 != null) {
                                synchronized (osuWifiEntry4) {
                                    osuWifiEntry4.mListener = passpointWifiEntry3;
                                }
                            }
                        }
                        return;
                    }
                }
            }
        }
        OsuWifiEntry osuWifiEntry5 = this.mOsuWifiEntry;
        if (osuWifiEntry5 != null && osuWifiEntry5.mLevel == -1) {
            PasspointWifiEntry passpointWifiEntry4 = this.mChosenEntry;
            Objects.requireNonNull(passpointWifiEntry4);
            synchronized (passpointWifiEntry4) {
                passpointWifiEntry4.mOsuWifiEntry = null;
            }
            this.mOsuWifiEntry = null;
        }
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    public final void handleConfiguredNetworksChangedAction(Intent intent) {
        Objects.requireNonNull(intent, "Intent cannot be null!");
        conditionallyUpdateConfig();
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    public final void handleScanResultsAvailableAction(Intent intent) {
        Objects.requireNonNull(intent, "Intent cannot be null!");
        conditionallyUpdateScanResults(intent.getBooleanExtra("resultsUpdated", true));
    }

    @Override // com.android.wifitrackerlib.NetworkDetailsTracker
    public final WifiEntry getWifiEntry() {
        return this.mChosenEntry;
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    public final void handleOnStart() {
        updateStartInfo();
    }
}
