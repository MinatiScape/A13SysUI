package com.android.wifitrackerlib;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import androidx.lifecycle.Lifecycle;
import com.android.systemui.people.widget.PeopleBackupHelper$$ExternalSyntheticLambda1;
import com.android.wifitrackerlib.StandardWifiEntry;
import java.time.Clock;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
/* loaded from: classes.dex */
public class StandardNetworkDetailsTracker extends NetworkDetailsTracker {
    public final StandardWifiEntry mChosenEntry;
    public final StandardWifiEntry.StandardWifiEntryKey mKey;

    public StandardNetworkDetailsTracker(WifiTrackerInjector wifiTrackerInjector, Lifecycle lifecycle, Context context, WifiManager wifiManager, ConnectivityManager connectivityManager, Handler handler, Handler handler2, Clock clock, long j, long j2, String str) {
        super(wifiTrackerInjector, lifecycle, context, wifiManager, connectivityManager, handler, handler2, clock, j, j2, "StandardNetworkDetailsTracker");
        StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey = new StandardWifiEntry.StandardWifiEntryKey(str);
        this.mKey = standardWifiEntryKey;
        if (standardWifiEntryKey.mIsNetworkRequest) {
            this.mChosenEntry = new NetworkRequestEntry(this.mInjector, this.mContext, this.mMainHandler, standardWifiEntryKey, this.mWifiManager);
        } else {
            this.mChosenEntry = new StandardWifiEntry(this.mInjector, this.mContext, this.mMainHandler, standardWifiEntryKey, this.mWifiManager, false);
        }
        updateStartInfo();
    }

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
        StandardWifiEntry standardWifiEntry = this.mChosenEntry;
        if (!this.mIsWifiValidated || !this.mIsCellDefaultRoute) {
            z = false;
        }
        standardWifiEntry.setIsLowQuality(z);
    }

    public final void conditionallyUpdateConfig() {
        this.mChosenEntry.updateConfig((List) this.mWifiManager.getPrivilegedConfiguredNetworks().stream().filter(new PeopleBackupHelper$$ExternalSyntheticLambda1(this, 1)).collect(Collectors.toList()));
    }

    public final void conditionallyUpdateScanResults(boolean z) {
        if (this.mWifiManager.getWifiState() == 1) {
            this.mChosenEntry.updateScanResultInfo(Collections.emptyList());
            return;
        }
        long j = this.mMaxScanAgeMillis;
        if (z) {
            this.mScanResultUpdater.update((List) this.mWifiManager.getScanResults().stream().filter(new Predicate() { // from class: com.android.wifitrackerlib.StandardNetworkDetailsTracker$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    StandardNetworkDetailsTracker standardNetworkDetailsTracker = StandardNetworkDetailsTracker.this;
                    Objects.requireNonNull(standardNetworkDetailsTracker);
                    StandardWifiEntry.ScanResultKey scanResultKey = new StandardWifiEntry.ScanResultKey((ScanResult) obj);
                    StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey = standardNetworkDetailsTracker.mKey;
                    Objects.requireNonNull(standardWifiEntryKey);
                    return scanResultKey.equals(standardWifiEntryKey.mScanResultKey);
                }
            }).collect(Collectors.toList()));
        } else {
            j += this.mScanIntervalMillis;
        }
        this.mChosenEntry.updateScanResultInfo(this.mScanResultUpdater.getScanResults(j));
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
