package com.android.systemui.statusbar.connectivity;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.net.ConnectivityManager;
import android.net.NetworkScoreManager;
import android.net.wifi.WifiManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.SignalIcon$IconGroup;
import com.android.settingslib.SignalIcon$MobileIconGroup;
import com.android.settingslib.graph.SignalDrawable;
import com.android.settingslib.mobile.TelephonyIcons;
import com.android.settingslib.wifi.WifiStatusTracker;
import com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda3;
import java.io.PrintWriter;
import java.util.Objects;
/* loaded from: classes.dex */
public final class WifiSignalController extends SignalController<WifiState, SignalIcon$IconGroup> {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final SignalIcon$MobileIconGroup mCarrierMergedWifiIconGroup = TelephonyIcons.CARRIER_MERGED_WIFI;
    public final boolean mHasMobileDataFeature;
    public final SignalIcon$IconGroup mUnmergedWifiIconGroup;
    public final WifiManager mWifiManager;
    public final WifiStatusTracker mWifiTracker;

    /* loaded from: classes.dex */
    public class WifiTrafficStateCallback implements WifiManager.TrafficStateCallback {
        public WifiTrafficStateCallback() {
        }

        public final void onStateChanged(int i) {
            WifiSignalController.this.setActivity(i);
        }
    }

    public WifiSignalController(Context context, boolean z, CallbackHandler callbackHandler, NetworkControllerImpl networkControllerImpl, WifiManager wifiManager, ConnectivityManager connectivityManager, NetworkScoreManager networkScoreManager) {
        super("WifiSignalController", context, 1, callbackHandler, networkControllerImpl);
        SignalIcon$IconGroup signalIcon$IconGroup = WifiIcons.UNMERGED_WIFI;
        this.mUnmergedWifiIconGroup = signalIcon$IconGroup;
        this.mWifiManager = wifiManager;
        WifiStatusTracker wifiStatusTracker = new WifiStatusTracker(this.mContext, wifiManager, networkScoreManager, connectivityManager, new PipTaskOrganizer$$ExternalSyntheticLambda3(this, 2));
        this.mWifiTracker = wifiStatusTracker;
        wifiStatusTracker.mNetworkScoreManager.registerNetworkScoreCache(1, wifiStatusTracker.mWifiNetworkScoreCache, 1);
        wifiStatusTracker.mWifiNetworkScoreCache.registerListener(wifiStatusTracker.mCacheListener);
        wifiStatusTracker.mConnectivityManager.registerNetworkCallback(wifiStatusTracker.mNetworkRequest, wifiStatusTracker.mNetworkCallback, wifiStatusTracker.mHandler);
        wifiStatusTracker.mConnectivityManager.registerDefaultNetworkCallback(wifiStatusTracker.mDefaultNetworkCallback, wifiStatusTracker.mHandler);
        this.mHasMobileDataFeature = z;
        if (wifiManager != null) {
            wifiManager.registerTrafficStateCallback(context.getMainExecutor(), new WifiTrafficStateCallback());
        }
        ((WifiState) this.mLastState).iconGroup = signalIcon$IconGroup;
        ((WifiState) this.mCurrentState).iconGroup = signalIcon$IconGroup;
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalController
    public final WifiState cleanState() {
        return new WifiState();
    }

    public final void copyWifiStates() {
        SignalIcon$IconGroup signalIcon$IconGroup;
        WifiState wifiState = (WifiState) this.mCurrentState;
        WifiStatusTracker wifiStatusTracker = this.mWifiTracker;
        wifiState.enabled = wifiStatusTracker.enabled;
        wifiState.isDefault = wifiStatusTracker.isDefaultNetwork;
        wifiState.connected = wifiStatusTracker.connected;
        wifiState.ssid = wifiStatusTracker.ssid;
        wifiState.rssi = wifiStatusTracker.rssi;
        int i = wifiStatusTracker.level;
        if (i != wifiState.level) {
            NetworkControllerImpl networkControllerImpl = this.mNetworkController;
            Objects.requireNonNull(networkControllerImpl);
            for (int i2 = 0; i2 < networkControllerImpl.mMobileSignalControllers.size(); i2++) {
                MobileSignalController valueAt = networkControllerImpl.mMobileSignalControllers.valueAt(i2);
                Objects.requireNonNull(valueAt);
                if (valueAt.mProviderModelBehavior) {
                    valueAt.mLastWlanLevel = i;
                    if (valueAt.mImsType == 2) {
                        valueAt.notifyCallStateChange(new IconState(true, MobileSignalController.getCallStrengthIcon(i, true), valueAt.getCallStrengthDescription(i, true)), valueAt.mSubscriptionInfo.getSubscriptionId());
                    }
                }
            }
        }
        WifiState wifiState2 = (WifiState) this.mCurrentState;
        WifiStatusTracker wifiStatusTracker2 = this.mWifiTracker;
        wifiState2.level = wifiStatusTracker2.level;
        wifiState2.statusLabel = wifiStatusTracker2.statusLabel;
        boolean z = wifiStatusTracker2.isCarrierMerged;
        wifiState2.isCarrierMerged = z;
        wifiState2.subId = wifiStatusTracker2.subId;
        if (z) {
            signalIcon$IconGroup = this.mCarrierMergedWifiIconGroup;
        } else {
            signalIcon$IconGroup = this.mUnmergedWifiIconGroup;
        }
        wifiState2.iconGroup = signalIcon$IconGroup;
    }

    public final int getCurrentIconIdForCarrierWifi() {
        int i = ((WifiState) this.mCurrentState).level;
        boolean z = true;
        int maxSignalLevel = this.mWifiManager.getMaxSignalLevel() + 1;
        WifiState wifiState = (WifiState) this.mCurrentState;
        int i2 = 0;
        if (wifiState.inetCondition != 0) {
            z = false;
        }
        if (wifiState.connected) {
            int i3 = SignalDrawable.$r8$clinit;
            if (z) {
                i2 = 2;
            }
            return (i2 << 16) | (maxSignalLevel << 8) | i;
        } else if (!wifiState.enabled) {
            return 0;
        } else {
            int i4 = SignalDrawable.$r8$clinit;
            return 131072 | (maxSignalLevel << 8) | 0;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:60:0x014b, code lost:
        if (r2.mConnectedTransports.get(3) == false) goto L_0x0150;
     */
    @Override // com.android.systemui.statusbar.connectivity.SignalController
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void notifyListeners(com.android.systemui.statusbar.connectivity.SignalCallback r23) {
        /*
            Method dump skipped, instructions count: 431
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.connectivity.WifiSignalController.notifyListeners(com.android.systemui.statusbar.connectivity.SignalCallback):void");
    }

    @VisibleForTesting
    public void setActivity(int i) {
        boolean z;
        T t = this.mCurrentState;
        WifiState wifiState = (WifiState) t;
        boolean z2 = false;
        if (i == 3 || i == 1) {
            z = true;
        } else {
            z = false;
        }
        wifiState.activityIn = z;
        WifiState wifiState2 = (WifiState) t;
        if (i == 3 || i == 2) {
            z2 = true;
        }
        wifiState2.activityOut = z2;
        notifyListenersIfNecessary();
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalController
    public final void dump(PrintWriter printWriter) {
        super.dump(printWriter);
        WifiStatusTracker wifiStatusTracker = this.mWifiTracker;
        Objects.requireNonNull(wifiStatusTracker);
        printWriter.println("  - WiFi Network History ------");
        int i = 0;
        for (int i2 = 0; i2 < 32; i2++) {
            if (wifiStatusTracker.mHistory[i2] != null) {
                i++;
            }
        }
        int i3 = wifiStatusTracker.mHistoryIndex + 32;
        while (true) {
            i3--;
            if (i3 >= (wifiStatusTracker.mHistoryIndex + 32) - i) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("  Previous WiFiNetwork(");
                m.append((wifiStatusTracker.mHistoryIndex + 32) - i3);
                m.append("): ");
                m.append(wifiStatusTracker.mHistory[i3 & 31]);
                printWriter.println(m.toString());
            } else {
                return;
            }
        }
    }
}
