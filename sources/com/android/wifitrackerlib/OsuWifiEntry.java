package com.android.wifitrackerlib;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.hotspot2.OsuProvider;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.net.wifi.hotspot2.ProvisioningCallback;
import android.os.Build;
import android.os.Handler;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Pair;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline1;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import com.android.wifitrackerlib.WifiEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public final class OsuWifiEntry extends WifiEntry {
    public final Context mContext;
    public boolean mHasAddConfigUserRestriction;
    public final String mKey;
    public final OsuProvider mOsuProvider;
    public String mOsuStatusString;
    public String mSsid;
    public final ArrayList mCurrentScanResults = new ArrayList();
    public boolean mIsAlreadyProvisioned = false;

    /* loaded from: classes.dex */
    public class OsuWifiEntryProvisioningCallback extends ProvisioningCallback {
        public final void onProvisioningStatus(int i) {
            String str;
            boolean z = false;
            switch (i) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case FalsingManager.VERSION /* 6 */:
                case 7:
                    str = String.format(OsuWifiEntry.this.mContext.getString(2131953622), OsuWifiEntry.this.getTitle());
                    break;
                case 8:
                case 9:
                case 10:
                case QSTileImpl.H.STALE /* 11 */:
                    str = OsuWifiEntry.this.mContext.getString(2131953620);
                    break;
                default:
                    str = null;
                    break;
            }
            synchronized (OsuWifiEntry.this) {
                if (!TextUtils.equals(OsuWifiEntry.this.mOsuStatusString, str)) {
                    z = true;
                }
                OsuWifiEntry osuWifiEntry = OsuWifiEntry.this;
                osuWifiEntry.mOsuStatusString = str;
                if (z) {
                    osuWifiEntry.notifyOnUpdated();
                }
            }
        }

        public OsuWifiEntryProvisioningCallback() {
        }

        public final void onProvisioningComplete() {
            ScanResult scanResult;
            synchronized (OsuWifiEntry.this) {
                OsuWifiEntry osuWifiEntry = OsuWifiEntry.this;
                osuWifiEntry.mOsuStatusString = osuWifiEntry.mContext.getString(2131953623);
            }
            OsuWifiEntry.this.notifyOnUpdated();
            OsuWifiEntry osuWifiEntry2 = OsuWifiEntry.this;
            PasspointConfiguration passpointConfiguration = (PasspointConfiguration) osuWifiEntry2.mWifiManager.getMatchingPasspointConfigsForOsuProviders(Collections.singleton(osuWifiEntry2.mOsuProvider)).get(OsuWifiEntry.this.mOsuProvider);
            WifiEntry.ConnectCallback connectCallback = OsuWifiEntry.this.mConnectCallback;
            if (passpointConfiguration != null) {
                String uniqueId = passpointConfiguration.getUniqueId();
                WifiManager wifiManager = OsuWifiEntry.this.mWifiManager;
                Iterator it = wifiManager.getAllMatchingWifiConfigs(wifiManager.getScanResults()).iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Pair pair = (Pair) it.next();
                    WifiConfiguration wifiConfiguration = (WifiConfiguration) pair.first;
                    if (TextUtils.equals(wifiConfiguration.getKey(), uniqueId)) {
                        List list = (List) ((Map) pair.second).get(0);
                        List list2 = (List) ((Map) pair.second).get(1);
                        if (list != null && !list.isEmpty()) {
                            scanResult = Utils.getBestScanResultByLevel(list);
                        } else if (list2 != null && !list2.isEmpty()) {
                            scanResult = Utils.getBestScanResultByLevel(list2);
                        }
                        wifiConfiguration.SSID = MotionController$$ExternalSyntheticOutline1.m(VendorAtomValue$$ExternalSyntheticOutline1.m("\""), scanResult.SSID, "\"");
                        OsuWifiEntry.this.mWifiManager.connect(wifiConfiguration, null);
                        return;
                    }
                }
                if (connectCallback != null) {
                    ((InternetDialogController.WifiEntryConnectCallback) connectCallback).onConnectResult(2);
                }
            } else if (connectCallback != null) {
                ((InternetDialogController.WifiEntryConnectCallback) connectCallback).onConnectResult(2);
            }
        }

        public final void onProvisioningFailure(int i) {
            synchronized (OsuWifiEntry.this) {
                OsuWifiEntry osuWifiEntry = OsuWifiEntry.this;
                if (TextUtils.equals(osuWifiEntry.mOsuStatusString, osuWifiEntry.mContext.getString(2131953620))) {
                    OsuWifiEntry osuWifiEntry2 = OsuWifiEntry.this;
                    osuWifiEntry2.mOsuStatusString = osuWifiEntry2.mContext.getString(2131953624);
                } else {
                    OsuWifiEntry osuWifiEntry3 = OsuWifiEntry.this;
                    osuWifiEntry3.mOsuStatusString = osuWifiEntry3.mContext.getString(2131953621);
                }
            }
            WifiEntry.ConnectCallback connectCallback = OsuWifiEntry.this.mConnectCallback;
            if (connectCallback != null) {
                ((InternetDialogController.WifiEntryConnectCallback) connectCallback).onConnectResult(2);
            }
            OsuWifiEntry.this.notifyOnUpdated();
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public OsuWifiEntry(WifiTrackerInjector wifiTrackerInjector, Context context, Handler handler, OsuProvider osuProvider, WifiManager wifiManager) throws IllegalArgumentException {
        super(handler, wifiManager, false);
        boolean z = false;
        this.mHasAddConfigUserRestriction = false;
        Objects.requireNonNull(osuProvider, "Cannot construct with null osuProvider!");
        this.mContext = context;
        this.mOsuProvider = osuProvider;
        this.mKey = osuProviderToOsuWifiEntryKey(osuProvider);
        Objects.requireNonNull(wifiTrackerInjector);
        UserManager userManager = wifiTrackerInjector.mUserManager;
        String str = Build.VERSION.CODENAME;
        if (!"REL".equals(str) && str.compareTo("T") >= 0) {
            z = true;
        }
        if (z && userManager != null) {
            this.mHasAddConfigUserRestriction = userManager.hasUserRestriction("no_add_wifi_config");
        }
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final synchronized void connect(InternetDialogController.WifiEntryConnectCallback wifiEntryConnectCallback) {
        this.mConnectCallback = wifiEntryConnectCallback;
        this.mWifiManager.stopRestrictingAutoJoinToSubscriptionId();
        this.mWifiManager.startSubscriptionProvisioning(this.mOsuProvider, this.mContext.getMainExecutor(), new OsuWifiEntryProvisioningCallback());
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final String getScanResultDescription() {
        return "";
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final synchronized String getSummary(boolean z) {
        boolean z2;
        String str;
        if (!this.mHasAddConfigUserRestriction || this.mIsAlreadyProvisioned) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (z2) {
            return this.mContext.getString(2131953612);
        }
        String str2 = this.mOsuStatusString;
        if (str2 != null) {
            return str2;
        }
        synchronized (this) {
            if (this.mIsAlreadyProvisioned) {
                if (z) {
                    str = this.mContext.getString(2131953646);
                } else {
                    str = this.mContext.getString(2131953629);
                }
                return str;
            }
            return this.mContext.getString(2131953630);
        }
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final synchronized String getTitle() {
        String friendlyName = this.mOsuProvider.getFriendlyName();
        if (!TextUtils.isEmpty(friendlyName)) {
            return friendlyName;
        }
        if (!TextUtils.isEmpty(this.mSsid)) {
            return this.mSsid;
        }
        Uri serverUri = this.mOsuProvider.getServerUri();
        if (serverUri == null) {
            return "";
        }
        return serverUri.toString();
    }

    public final synchronized void updateScanResultInfo(List<ScanResult> list) throws IllegalArgumentException {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.mCurrentScanResults.clear();
        this.mCurrentScanResults.addAll(list);
        ScanResult bestScanResultByLevel = Utils.getBestScanResultByLevel(list);
        if (bestScanResultByLevel != null) {
            this.mSsid = bestScanResultByLevel.SSID;
            if (getConnectedState() == 0) {
                this.mLevel = this.mWifiManager.calculateSignalLevel(bestScanResultByLevel.level);
            }
        } else {
            this.mLevel = -1;
        }
        notifyOnUpdated();
    }

    public static String osuProviderToOsuWifiEntryKey(OsuProvider osuProvider) {
        Objects.requireNonNull(osuProvider, "Cannot create key with null OsuProvider!");
        return "OsuWifiEntry:" + osuProvider.getFriendlyName() + "," + osuProvider.getServerUri().toString();
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final boolean connectionInfoMatches(WifiInfo wifiInfo) {
        if (!wifiInfo.isOsuAp() || !TextUtils.equals(wifiInfo.getPasspointProviderFriendlyName(), this.mOsuProvider.getFriendlyName())) {
            return false;
        }
        return true;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final String getKey() {
        return this.mKey;
    }
}
