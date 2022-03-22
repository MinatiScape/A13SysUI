package com.android.wifitrackerlib;

import android.app.admin.DevicePolicyManager;
import android.app.admin.WifiSsidPolicy;
import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiSsid;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class StandardWifiEntry extends WifiEntry {
    public final Context mContext;
    public final DevicePolicyManager mDevicePolicyManager;
    public boolean mHasAddConfigUserRestriction;
    public final WifiTrackerInjector mInjector;
    public boolean mIsAdminRestricted;
    public final boolean mIsEnhancedOpenSupported;
    public boolean mIsUserShareable;
    public final boolean mIsWpa3SaeSupported;
    public final boolean mIsWpa3SuiteBSupported;
    public final StandardWifiEntryKey mKey;
    public final HashMap mMatchingScanResults;
    public final HashMap mMatchingWifiConfigs;
    public boolean mShouldAutoOpenCaptivePortal;
    public final ArrayList mTargetScanResults;
    public ArrayList mTargetSecurityTypes;
    public WifiConfiguration mTargetWifiConfig;

    /* loaded from: classes.dex */
    public static class ScanResultKey {
        public ArraySet mSecurityTypes;
        public String mSsid;

        public ScanResultKey() {
            this.mSecurityTypes = new ArraySet();
        }

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || ScanResultKey.class != obj.getClass()) {
                return false;
            }
            ScanResultKey scanResultKey = (ScanResultKey) obj;
            return TextUtils.equals(this.mSsid, scanResultKey.mSsid) && this.mSecurityTypes.equals(scanResultKey.mSecurityTypes);
        }

        public final int hashCode() {
            return Objects.hash(this.mSsid, this.mSecurityTypes);
        }

        public final String toString() {
            JSONObject jSONObject = new JSONObject();
            try {
                String str = this.mSsid;
                if (str != null) {
                    jSONObject.put("SSID", str);
                }
                if (!this.mSecurityTypes.isEmpty()) {
                    JSONArray jSONArray = new JSONArray();
                    Iterator it = this.mSecurityTypes.iterator();
                    while (it.hasNext()) {
                        jSONArray.put(((Integer) it.next()).intValue());
                    }
                    jSONObject.put("SECURITY_TYPES", jSONArray);
                }
            } catch (JSONException e) {
                Log.e("StandardWifiEntry", "JSONException while converting ScanResultKey to string: " + e);
            }
            return jSONObject.toString();
        }

        public ScanResultKey(String str, List<Integer> list) {
            this.mSecurityTypes = new ArraySet();
            this.mSsid = str;
            for (Integer num : list) {
                int intValue = num.intValue();
                this.mSecurityTypes.add(Integer.valueOf(intValue));
                if (intValue == 0) {
                    this.mSecurityTypes.add(6);
                } else if (intValue == 6) {
                    this.mSecurityTypes.add(0);
                } else if (intValue == 9) {
                    this.mSecurityTypes.add(3);
                } else if (intValue == 2) {
                    this.mSecurityTypes.add(4);
                } else if (intValue == 3) {
                    this.mSecurityTypes.add(9);
                } else if (intValue == 4) {
                    this.mSecurityTypes.add(2);
                }
            }
        }

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public ScanResultKey(android.net.wifi.ScanResult r6) {
            /*
                r5 = this;
                java.lang.String r0 = r6.SSID
                java.util.ArrayList r1 = new java.util.ArrayList
                r1.<init>()
                int[] r6 = r6.getSecurityTypes()
                int r2 = r6.length
                r3 = 0
            L_0x000d:
                if (r3 >= r2) goto L_0x001b
                r4 = r6[r3]
                java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
                r1.add(r4)
                int r3 = r3 + 1
                goto L_0x000d
            L_0x001b:
                r5.<init>(r0, r1)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.ScanResultKey.<init>(android.net.wifi.ScanResult):void");
        }

        public ScanResultKey(String str) {
            this.mSecurityTypes = new ArraySet();
            try {
                JSONObject jSONObject = new JSONObject(str);
                this.mSsid = jSONObject.getString("SSID");
                JSONArray jSONArray = jSONObject.getJSONArray("SECURITY_TYPES");
                for (int i = 0; i < jSONArray.length(); i++) {
                    this.mSecurityTypes.add(Integer.valueOf(jSONArray.getInt(i)));
                }
            } catch (JSONException e) {
                Log.wtf("StandardWifiEntry", "JSONException while constructing ScanResultKey from string: " + e);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class StandardWifiEntryKey {
        public boolean mIsNetworkRequest;
        public boolean mIsTargetingNewNetworks;
        public ScanResultKey mScanResultKey;
        public String mSuggestionProfileKey;

        public StandardWifiEntryKey(ScanResultKey scanResultKey) {
            this.mScanResultKey = scanResultKey;
            this.mIsTargetingNewNetworks = true;
        }

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || StandardWifiEntryKey.class != obj.getClass()) {
                return false;
            }
            StandardWifiEntryKey standardWifiEntryKey = (StandardWifiEntryKey) obj;
            return Objects.equals(this.mScanResultKey, standardWifiEntryKey.mScanResultKey) && TextUtils.equals(this.mSuggestionProfileKey, standardWifiEntryKey.mSuggestionProfileKey) && this.mIsNetworkRequest == standardWifiEntryKey.mIsNetworkRequest;
        }

        public final int hashCode() {
            return Objects.hash(this.mScanResultKey, this.mSuggestionProfileKey, Boolean.valueOf(this.mIsNetworkRequest));
        }

        public final String toString() {
            JSONObject jSONObject = new JSONObject();
            try {
                ScanResultKey scanResultKey = this.mScanResultKey;
                if (scanResultKey != null) {
                    jSONObject.put("SCAN_RESULT_KEY", scanResultKey.toString());
                }
                String str = this.mSuggestionProfileKey;
                if (str != null) {
                    jSONObject.put("SUGGESTION_PROFILE_KEY", str);
                }
                boolean z = this.mIsNetworkRequest;
                if (z) {
                    jSONObject.put("IS_NETWORK_REQUEST", z);
                }
                boolean z2 = this.mIsTargetingNewNetworks;
                if (z2) {
                    jSONObject.put("IS_TARGETING_NEW_NETWORKS", z2);
                }
            } catch (JSONException e) {
                Log.wtf("StandardWifiEntry", "JSONException while converting StandardWifiEntryKey to string: " + e);
            }
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("StandardWifiEntry:");
            m.append(jSONObject.toString());
            return m.toString();
        }

        public StandardWifiEntryKey(WifiConfiguration wifiConfiguration, boolean z) {
            this.mIsTargetingNewNetworks = false;
            this.mScanResultKey = new ScanResultKey(WifiInfo.sanitizeSsid(wifiConfiguration.SSID), Utils.getSecurityTypesFromWifiConfiguration(wifiConfiguration));
            if (wifiConfiguration.fromWifiNetworkSuggestion) {
                this.mSuggestionProfileKey = new StringJoiner(",").add(wifiConfiguration.creatorName).add(String.valueOf(wifiConfiguration.carrierId)).add(String.valueOf(wifiConfiguration.subscriptionId)).toString();
            } else if (wifiConfiguration.fromWifiNetworkSpecifier) {
                this.mIsNetworkRequest = true;
            }
            this.mIsTargetingNewNetworks = z;
        }

        public StandardWifiEntryKey(String str) {
            this.mIsTargetingNewNetworks = false;
            this.mScanResultKey = new ScanResultKey();
            if (!str.startsWith("StandardWifiEntry:")) {
                Log.e("StandardWifiEntry", "String key does not start with key prefix!");
                return;
            }
            try {
                JSONObject jSONObject = new JSONObject(str.substring(18));
                if (jSONObject.has("SCAN_RESULT_KEY")) {
                    this.mScanResultKey = new ScanResultKey(jSONObject.getString("SCAN_RESULT_KEY"));
                }
                if (jSONObject.has("SUGGESTION_PROFILE_KEY")) {
                    this.mSuggestionProfileKey = jSONObject.getString("SUGGESTION_PROFILE_KEY");
                }
                if (jSONObject.has("IS_NETWORK_REQUEST")) {
                    this.mIsNetworkRequest = jSONObject.getBoolean("IS_NETWORK_REQUEST");
                }
                if (jSONObject.has("IS_TARGETING_NEW_NETWORKS")) {
                    this.mIsTargetingNewNetworks = jSONObject.getBoolean("IS_TARGETING_NEW_NETWORKS");
                }
            } catch (JSONException e) {
                Log.e("StandardWifiEntry", "JSONException while converting StandardWifiEntryKey to string: " + e);
            }
        }
    }

    public StandardWifiEntry(WifiTrackerInjector wifiTrackerInjector, Context context, Handler handler, StandardWifiEntryKey standardWifiEntryKey, WifiManager wifiManager, boolean z) {
        super(handler, wifiManager, z);
        int i;
        this.mMatchingScanResults = new HashMap();
        this.mMatchingWifiConfigs = new HashMap();
        this.mTargetScanResults = new ArrayList();
        this.mTargetSecurityTypes = new ArrayList();
        boolean z2 = false;
        this.mIsUserShareable = false;
        this.mShouldAutoOpenCaptivePortal = false;
        this.mIsAdminRestricted = false;
        this.mHasAddConfigUserRestriction = false;
        this.mInjector = wifiTrackerInjector;
        this.mContext = context;
        this.mKey = standardWifiEntryKey;
        this.mIsWpa3SaeSupported = wifiManager.isWpa3SaeSupported();
        this.mIsWpa3SuiteBSupported = wifiManager.isWpa3SuiteBSupported();
        this.mIsEnhancedOpenSupported = wifiManager.isEnhancedOpenSupported();
        Objects.requireNonNull(wifiTrackerInjector);
        UserManager userManager = wifiTrackerInjector.mUserManager;
        DevicePolicyManager devicePolicyManager = wifiTrackerInjector.mDevicePolicyManager;
        this.mDevicePolicyManager = devicePolicyManager;
        updateSecurityTypes();
        String str = Build.VERSION.CODENAME;
        if (!"REL".equals(str) && str.compareTo("T") >= 0) {
            if (userManager != null) {
                this.mHasAddConfigUserRestriction = userManager.hasUserRestriction("no_add_wifi_config");
            }
            if (devicePolicyManager != null) {
                int minimumRequiredWifiSecurityLevel = devicePolicyManager.getMinimumRequiredWifiSecurityLevel();
                if (minimumRequiredWifiSecurityLevel != 0) {
                    Iterator it = ((ArrayList) getSecurityTypes()).iterator();
                    while (true) {
                        if (it.hasNext()) {
                            switch (((Integer) it.next()).intValue()) {
                                case 0:
                                case FalsingManager.VERSION /* 6 */:
                                    i = 0;
                                    break;
                                case 1:
                                case 2:
                                case 4:
                                case 7:
                                    i = 1;
                                    break;
                                case 3:
                                case 8:
                                case 9:
                                case QSTileImpl.H.STALE /* 11 */:
                                case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                                    i = 2;
                                    break;
                                case 5:
                                    i = 3;
                                    break;
                                case 10:
                                default:
                                    i = -1;
                                    break;
                            }
                            if (i != -1 && minimumRequiredWifiSecurityLevel <= i) {
                                z2 = true;
                            }
                        }
                    }
                    if (!z2) {
                        this.mIsAdminRestricted = true;
                        return;
                    }
                }
                WifiSsidPolicy wifiSsidPolicy = this.mDevicePolicyManager.getWifiSsidPolicy();
                if (wifiSsidPolicy != null) {
                    int policyType = wifiSsidPolicy.getPolicyType();
                    Set ssids = wifiSsidPolicy.getSsids();
                    if (policyType == 0) {
                        StandardWifiEntryKey standardWifiEntryKey2 = this.mKey;
                        Objects.requireNonNull(standardWifiEntryKey2);
                        ScanResultKey scanResultKey = standardWifiEntryKey2.mScanResultKey;
                        Objects.requireNonNull(scanResultKey);
                        if (!ssids.contains(WifiSsid.fromBytes(scanResultKey.mSsid.getBytes(StandardCharsets.UTF_8)))) {
                            this.mIsAdminRestricted = true;
                            return;
                        }
                    }
                    if (policyType == 1) {
                        StandardWifiEntryKey standardWifiEntryKey3 = this.mKey;
                        Objects.requireNonNull(standardWifiEntryKey3);
                        ScanResultKey scanResultKey2 = standardWifiEntryKey3.mScanResultKey;
                        Objects.requireNonNull(scanResultKey2);
                        if (ssids.contains(WifiSsid.fromBytes(scanResultKey2.mSsid.getBytes(StandardCharsets.UTF_8)))) {
                            this.mIsAdminRestricted = true;
                        }
                    }
                }
            }
        }
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

    /* JADX WARN: Removed duplicated region for block: B:38:0x0131 A[Catch: all -> 0x014f, TRY_LEAVE, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0012, B:8:0x001a, B:10:0x0027, B:12:0x0069, B:13:0x009d, B:15:0x00a9, B:16:0x00e1, B:17:0x00ec, B:19:0x00f2, B:24:0x00fd, B:27:0x010f, B:29:0x0115, B:35:0x0120, B:38:0x0131, B:41:0x013f), top: B:46:0x0001 }] */
    @Override // com.android.wifitrackerlib.WifiEntry
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized void connect(com.android.systemui.qs.tiles.dialog.InternetDialogController.WifiEntryConnectCallback r6) {
        /*
            Method dump skipped, instructions count: 338
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.connect(com.android.systemui.qs.tiles.dialog.InternetDialogController$WifiEntryConnectCallback):void");
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final synchronized boolean connectionInfoMatches(WifiInfo wifiInfo) {
        if (!wifiInfo.isPasspointAp() && !wifiInfo.isOsuAp()) {
            for (WifiConfiguration wifiConfiguration : this.mMatchingWifiConfigs.values()) {
                if (wifiConfiguration.networkId == wifiInfo.getNetworkId()) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public synchronized int getMeteredChoice() {
        WifiConfiguration wifiConfiguration;
        if (!isSuggestion() && (wifiConfiguration = this.mTargetWifiConfig) != null) {
            int i = wifiConfiguration.meteredOverride;
            if (i == 1) {
                return 1;
            }
            if (i == 2) {
                return 2;
            }
        }
        return 0;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final synchronized String getScanResultDescription() {
        if (this.mTargetScanResults.size() == 0) {
            return "";
        }
        return "[" + getScanResultDescription(2400, 2500) + ";" + getScanResultDescription(4900, 5900) + ";" + getScanResultDescription(5925, 7125) + ";" + getScanResultDescription(58320, 70200) + "]";
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final synchronized List<Integer> getSecurityTypes() {
        return new ArrayList(this.mTargetSecurityTypes);
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final synchronized String getSummary(boolean z) {
        boolean z2;
        String str;
        if ((!this.mHasAddConfigUserRestriction || isSaved() || isSuggestion()) && !this.mIsAdminRestricted) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (z2) {
            return this.mContext.getString(2131953612);
        }
        StringJoiner stringJoiner = new StringJoiner(this.mContext.getString(2131953628));
        int connectedState = getConnectedState();
        if (connectedState == 0) {
            str = Utils.getDisconnectedDescription(this.mInjector, this.mContext, this.mTargetWifiConfig, this.mForSavedNetworksPage, z);
        } else if (connectedState == 1) {
            str = Utils.getConnectingDescription(this.mContext, this.mNetworkInfo);
        } else if (connectedState != 2) {
            Log.e("StandardWifiEntry", "getConnectedState() returned unknown state: " + connectedState);
            str = null;
        } else {
            str = Utils.getConnectedDescription(this.mContext, this.mTargetWifiConfig, this.mNetworkCapabilities, this.mIsDefaultNetwork, this.mIsLowQuality);
        }
        if (!TextUtils.isEmpty(str)) {
            stringJoiner.add(str);
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

    @Override // com.android.wifitrackerlib.WifiEntry
    public synchronized WifiConfiguration getWifiConfiguration() {
        if (!isSaved()) {
            return null;
        }
        return this.mTargetWifiConfig;
    }

    public synchronized boolean isAutoJoinEnabled() {
        WifiConfiguration wifiConfiguration = this.mTargetWifiConfig;
        if (wifiConfiguration == null) {
            return false;
        }
        return wifiConfiguration.allowAutojoin;
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x000e, code lost:
        if (r0.meteredHint != false) goto L_0x0012;
     */
    @Override // com.android.wifitrackerlib.WifiEntry
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized boolean isMetered() {
        /*
            r2 = this;
            monitor-enter(r2)
            int r0 = r2.getMeteredChoice()     // Catch: all -> 0x0014
            r1 = 1
            if (r0 == r1) goto L_0x0012
            android.net.wifi.WifiConfiguration r0 = r2.mTargetWifiConfig     // Catch: all -> 0x0014
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
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.isMetered():boolean");
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public synchronized boolean isSaved() {
        boolean z;
        WifiConfiguration wifiConfiguration = this.mTargetWifiConfig;
        if (wifiConfiguration != null && !wifiConfiguration.fromWifiNetworkSuggestion) {
            if (!wifiConfiguration.isEphemeral()) {
                z = true;
            }
        }
        z = false;
        return z;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public synchronized boolean isSuggestion() {
        boolean z;
        WifiConfiguration wifiConfiguration = this.mTargetWifiConfig;
        if (wifiConfiguration != null) {
            if (wifiConfiguration.fromWifiNetworkSuggestion) {
                z = true;
            }
        }
        z = false;
        return z;
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0028, code lost:
        if (r0.getDisableReasonCounter(5) > 0) goto L_0x002a;
     */
    @Override // com.android.wifitrackerlib.WifiEntry
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final synchronized boolean shouldEditBeforeConnect() {
        /*
            r3 = this;
            monitor-enter(r3)
            android.net.wifi.WifiConfiguration r0 = r3.getWifiConfiguration()     // Catch: all -> 0x002f
            r1 = 0
            if (r0 != 0) goto L_0x000a
            monitor-exit(r3)
            return r1
        L_0x000a:
            android.net.wifi.WifiConfiguration$NetworkSelectionStatus r0 = r0.getNetworkSelectionStatus()     // Catch: all -> 0x002f
            int r2 = r0.getNetworkSelectionStatus()     // Catch: all -> 0x002f
            if (r2 == 0) goto L_0x002d
            r2 = 2
            int r2 = r0.getDisableReasonCounter(r2)     // Catch: all -> 0x002f
            if (r2 > 0) goto L_0x002a
            r2 = 8
            int r2 = r0.getDisableReasonCounter(r2)     // Catch: all -> 0x002f
            if (r2 > 0) goto L_0x002a
            r2 = 5
            int r0 = r0.getDisableReasonCounter(r2)     // Catch: all -> 0x002f
            if (r0 <= 0) goto L_0x002d
        L_0x002a:
            r0 = 1
            monitor-exit(r3)
            return r0
        L_0x002d:
            monitor-exit(r3)
            return r1
        L_0x002f:
            r0 = move-exception
            monitor-exit(r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.shouldEditBeforeConnect():boolean");
    }

    public final synchronized void updateConfig(List<WifiConfiguration> list) throws IllegalArgumentException {
        boolean z;
        if (list == null) {
            list = Collections.emptyList();
        }
        StandardWifiEntryKey standardWifiEntryKey = this.mKey;
        Objects.requireNonNull(standardWifiEntryKey);
        ScanResultKey scanResultKey = standardWifiEntryKey.mScanResultKey;
        Objects.requireNonNull(scanResultKey);
        String str = scanResultKey.mSsid;
        ArraySet arraySet = scanResultKey.mSecurityTypes;
        this.mMatchingWifiConfigs.clear();
        for (WifiConfiguration wifiConfiguration : list) {
            if (TextUtils.equals(str, WifiInfo.sanitizeSsid(wifiConfiguration.SSID))) {
                for (Integer num : Utils.getSecurityTypesFromWifiConfiguration(wifiConfiguration)) {
                    int intValue = num.intValue();
                    if (arraySet.contains(Integer.valueOf(intValue))) {
                        if (intValue == 4) {
                            z = this.mIsWpa3SaeSupported;
                        } else if (intValue == 5) {
                            z = this.mIsWpa3SuiteBSupported;
                        } else if (intValue != 6) {
                            z = true;
                        } else {
                            z = this.mIsEnhancedOpenSupported;
                        }
                        if (z) {
                            this.mMatchingWifiConfigs.put(Integer.valueOf(intValue), wifiConfiguration);
                        }
                    } else {
                        throw new IllegalArgumentException("Attempted to update with wrong security! Expected one of: " + arraySet + ", Actual: " + intValue + ", Config: " + wifiConfiguration);
                    }
                }
            } else {
                throw new IllegalArgumentException("Attempted to update with wrong SSID! Expected: " + str + ", Actual: " + WifiInfo.sanitizeSsid(wifiConfiguration.SSID) + ", Config: " + wifiConfiguration);
            }
        }
        updateSecurityTypes();
        updateTargetScanResultInfo();
        notifyOnUpdated();
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

    public final synchronized void updateScanResultInfo(List<ScanResult> list) throws IllegalArgumentException {
        boolean z;
        if (list == null) {
            list = new ArrayList<>();
        }
        StandardWifiEntryKey standardWifiEntryKey = this.mKey;
        Objects.requireNonNull(standardWifiEntryKey);
        ScanResultKey scanResultKey = standardWifiEntryKey.mScanResultKey;
        Objects.requireNonNull(scanResultKey);
        String str = scanResultKey.mSsid;
        for (ScanResult scanResult : list) {
            if (!TextUtils.equals(scanResult.SSID, str)) {
                throw new IllegalArgumentException("Attempted to update with wrong SSID! Expected: " + str + ", Actual: " + scanResult.SSID + ", ScanResult: " + scanResult);
            }
        }
        this.mMatchingScanResults.clear();
        StandardWifiEntryKey standardWifiEntryKey2 = this.mKey;
        Objects.requireNonNull(standardWifiEntryKey2);
        ScanResultKey scanResultKey2 = standardWifiEntryKey2.mScanResultKey;
        Objects.requireNonNull(scanResultKey2);
        ArraySet arraySet = scanResultKey2.mSecurityTypes;
        for (ScanResult scanResult2 : list) {
            ArrayList arrayList = new ArrayList();
            for (int i : scanResult2.getSecurityTypes()) {
                arrayList.add(Integer.valueOf(i));
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                int intValue = ((Integer) it.next()).intValue();
                if (arraySet.contains(Integer.valueOf(intValue))) {
                    if (intValue == 4) {
                        z = this.mIsWpa3SaeSupported;
                    } else if (intValue == 5) {
                        z = this.mIsWpa3SuiteBSupported;
                    } else if (intValue != 6) {
                        z = true;
                    } else {
                        z = this.mIsEnhancedOpenSupported;
                    }
                    if (z) {
                        if (!this.mMatchingScanResults.containsKey(Integer.valueOf(intValue))) {
                            this.mMatchingScanResults.put(Integer.valueOf(intValue), new ArrayList());
                        }
                        ((List) this.mMatchingScanResults.get(Integer.valueOf(intValue))).add(scanResult2);
                    }
                }
            }
        }
        updateSecurityTypes();
        updateTargetScanResultInfo();
        notifyOnUpdated();
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final synchronized void updateSecurityTypes() {
        this.mTargetSecurityTypes.clear();
        WifiInfo wifiInfo = this.mWifiInfo;
        if (!(wifiInfo == null || wifiInfo.getCurrentSecurityType() == -1)) {
            this.mTargetSecurityTypes.add(Integer.valueOf(this.mWifiInfo.getCurrentSecurityType()));
        }
        Set keySet = this.mMatchingWifiConfigs.keySet();
        if (this.mTargetSecurityTypes.isEmpty()) {
            StandardWifiEntryKey standardWifiEntryKey = this.mKey;
            Objects.requireNonNull(standardWifiEntryKey);
            if (standardWifiEntryKey.mIsTargetingNewNetworks) {
                boolean z = false;
                Set keySet2 = this.mMatchingScanResults.keySet();
                Iterator it = keySet.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (keySet2.contains(Integer.valueOf(((Integer) it.next()).intValue()))) {
                            z = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (!z) {
                    this.mTargetSecurityTypes.addAll(keySet2);
                }
            }
        }
        if (this.mTargetSecurityTypes.isEmpty()) {
            this.mTargetSecurityTypes.addAll(keySet);
        }
        if (this.mTargetSecurityTypes.isEmpty()) {
            ArrayList arrayList = this.mTargetSecurityTypes;
            StandardWifiEntryKey standardWifiEntryKey2 = this.mKey;
            Objects.requireNonNull(standardWifiEntryKey2);
            ScanResultKey scanResultKey = standardWifiEntryKey2.mScanResultKey;
            Objects.requireNonNull(scanResultKey);
            arrayList.addAll(scanResultKey.mSecurityTypes);
        }
        this.mTargetWifiConfig = (WifiConfiguration) this.mMatchingWifiConfigs.get(Integer.valueOf(Utils.getSingleSecurityTypeFromMultipleSecurityTypes(this.mTargetSecurityTypes)));
        ArraySet arraySet = new ArraySet();
        Iterator it2 = this.mTargetSecurityTypes.iterator();
        while (it2.hasNext()) {
            int intValue = ((Integer) it2.next()).intValue();
            if (this.mMatchingScanResults.containsKey(Integer.valueOf(intValue))) {
                arraySet.addAll((Collection) this.mMatchingScanResults.get(Integer.valueOf(intValue)));
            }
        }
        this.mTargetScanResults.clear();
        this.mTargetScanResults.addAll(arraySet);
    }

    public final synchronized void updateTargetScanResultInfo() {
        int i;
        ScanResult bestScanResultByLevel = Utils.getBestScanResultByLevel(this.mTargetScanResults);
        if (getConnectedState() == 0) {
            if (bestScanResultByLevel != null) {
                i = this.mWifiManager.calculateSignalLevel(bestScanResultByLevel.level);
            } else {
                i = -1;
            }
            this.mLevel = i;
        }
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final String getKey() {
        return this.mKey.toString();
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final String getTitle() {
        StandardWifiEntryKey standardWifiEntryKey = this.mKey;
        Objects.requireNonNull(standardWifiEntryKey);
        ScanResultKey scanResultKey = standardWifiEntryKey.mScanResultKey;
        Objects.requireNonNull(scanResultKey);
        return scanResultKey.mSsid;
    }

    public boolean canSetAutoJoinEnabled() {
        if (isSaved() || isSuggestion()) {
            return true;
        }
        return false;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public boolean canSetMeteredChoice() {
        if (getWifiConfiguration() != null) {
            return true;
        }
        return false;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final String getNetworkSelectionDescription() {
        return Utils.getNetworkSelectionDescription(getWifiConfiguration());
    }

    public final synchronized String getScanResultDescription(final int i, final int i2) {
        List list = (List) this.mTargetScanResults.stream().filter(new Predicate() { // from class: com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                int i3 = i;
                int i4 = i2;
                int i5 = ((ScanResult) obj).frequency;
                if (i5 < i3 || i5 > i4) {
                    return false;
                }
                return true;
            }
        }).sorted(Comparator.comparingInt(StandardWifiEntry$$ExternalSyntheticLambda3.INSTANCE)).collect(Collectors.toList());
        int size = list.size();
        if (size == 0) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(size);
        sb.append(")");
        if (size > 4) {
            int asInt = list.stream().mapToInt(StandardWifiEntry$$ExternalSyntheticLambda4.INSTANCE).max().getAsInt();
            sb.append("max=");
            sb.append(asInt);
            sb.append(",");
        }
        final long elapsedRealtime = SystemClock.elapsedRealtime();
        list.forEach(new Consumer() { // from class: com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                String sb2;
                StandardWifiEntry standardWifiEntry = StandardWifiEntry.this;
                StringBuilder sb3 = sb;
                long j = elapsedRealtime;
                ScanResult scanResult = (ScanResult) obj;
                Objects.requireNonNull(standardWifiEntry);
                synchronized (standardWifiEntry) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(" \n{");
                    sb4.append(scanResult.BSSID);
                    WifiInfo wifiInfo = standardWifiEntry.mWifiInfo;
                    if (wifiInfo != null && scanResult.BSSID.equals(wifiInfo.getBSSID())) {
                        sb4.append("*");
                    }
                    sb4.append("=");
                    sb4.append(scanResult.frequency);
                    sb4.append(",");
                    sb4.append(scanResult.level);
                    sb4.append(",");
                    sb4.append(((int) (j - (scanResult.timestamp / 1000))) / 1000);
                    sb4.append("s");
                    sb4.append("}");
                    sb2 = sb4.toString();
                }
                sb3.append(sb2);
            }
        });
        return sb.toString();
    }

    public StandardWifiEntry(WifiTrackerInjector wifiTrackerInjector, Context context, Handler handler, StandardWifiEntryKey standardWifiEntryKey, List<WifiConfiguration> list, List<ScanResult> list2, WifiManager wifiManager, boolean z) throws IllegalArgumentException {
        this(wifiTrackerInjector, context, handler, standardWifiEntryKey, wifiManager, z);
        if (list != null && !list.isEmpty()) {
            updateConfig(list);
        }
        if (list2 != null && !list2.isEmpty()) {
            updateScanResultInfo(list2);
        }
    }
}
