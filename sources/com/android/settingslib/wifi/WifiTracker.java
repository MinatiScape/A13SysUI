package com.android.settingslib.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkKey;
import android.net.NetworkRequest;
import android.net.NetworkScoreManager;
import android.net.ScoredNetwork;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkScoreCache;
import android.net.wifi.hotspot2.OsuProvider;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline1;
import androidx.recyclerview.R$dimen;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.android.systemui.ScreenDecorations$$ExternalSyntheticLambda2;
import com.android.wm.shell.bubbles.BubbleExpandedView$1$$ExternalSyntheticLambda0;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
@Deprecated
/* loaded from: classes.dex */
public class WifiTracker implements LifecycleObserver, OnStart, OnStop, OnDestroy {
    public static final long MAX_SCAN_RESULT_AGE_MILLIS = 15000;
    public static boolean sVerboseLogging;
    public final ConnectivityManager mConnectivityManager;
    public final Context mContext;
    public final IntentFilter mFilter;
    public WifiInfo mLastInfo;
    public NetworkInfo mLastNetworkInfo;
    public final WifiListenerExecutor mListener;
    public long mMaxSpeedLabelScoreCacheAge;
    public WifiTrackerNetworkCallback mNetworkCallback;
    public final NetworkRequest mNetworkRequest;
    public final NetworkScoreManager mNetworkScoreManager;
    public boolean mNetworkScoringUiEnabled;
    public boolean mRegistered;
    public Scanner mScanner;
    public WifiNetworkScoreCache mScoreCache;
    public final WifiManager mWifiManager;
    public Handler mWorkHandler;
    public HandlerThread mWorkThread;
    public final AtomicBoolean mConnected = new AtomicBoolean(false);
    public final Object mLock = new Object();
    public final ArrayList mInternalAccessPoints = new ArrayList();
    public final ArraySet mRequestedScores = new ArraySet();
    public boolean mStaleScanResults = true;
    public boolean mLastScanSucceeded = true;
    public final HashMap<String, ScanResult> mScanResultCache = new HashMap<>();
    public final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.settingslib.wifi.WifiTracker.2
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.net.wifi.WIFI_STATE_CHANGED".equals(action)) {
                WifiTracker wifiTracker = WifiTracker.this;
                int intExtra = intent.getIntExtra("wifi_state", 4);
                Objects.requireNonNull(wifiTracker);
                if (WifiTracker.isVerboseLoggingEnabled()) {
                    ExifInterface$$ExternalSyntheticOutline1.m("updateWifiState: ", intExtra, "WifiTracker");
                }
                if (intExtra == 3) {
                    synchronized (wifiTracker.mLock) {
                        Scanner scanner = wifiTracker.mScanner;
                        if (scanner != null) {
                            if (WifiTracker.isVerboseLoggingEnabled()) {
                                Log.d("WifiTracker", "Scanner resume");
                            }
                            if (!scanner.hasMessages(0)) {
                                scanner.sendEmptyMessage(0);
                            }
                        }
                    }
                } else {
                    synchronized (wifiTracker.mLock) {
                        if (!wifiTracker.mInternalAccessPoints.isEmpty()) {
                            wifiTracker.mInternalAccessPoints.clear();
                            if (!wifiTracker.mStaleScanResults) {
                                wifiTracker.mListener.onAccessPointsChanged();
                            }
                        }
                    }
                    wifiTracker.mLastInfo = null;
                    wifiTracker.mLastNetworkInfo = null;
                    synchronized (wifiTracker.mLock) {
                        Scanner scanner2 = wifiTracker.mScanner;
                        if (scanner2 != null) {
                            if (WifiTracker.isVerboseLoggingEnabled()) {
                                Log.d("WifiTracker", "Scanner pause");
                            }
                            scanner2.mRetry = 0;
                            scanner2.removeMessages(0);
                        }
                    }
                    wifiTracker.mStaleScanResults = true;
                }
                WifiListenerExecutor wifiListenerExecutor = wifiTracker.mListener;
                Objects.requireNonNull(wifiListenerExecutor);
                R$dimen.postOnMainThread(new WifiTracker$WifiListenerExecutor$$ExternalSyntheticLambda1(wifiListenerExecutor, String.format("Invoking onWifiStateChanged callback with state %d", Integer.valueOf(intExtra)), new WifiTracker$WifiListenerExecutor$$ExternalSyntheticLambda0(wifiListenerExecutor, intExtra)));
            } else if ("android.net.wifi.SCAN_RESULTS".equals(action)) {
                WifiTracker wifiTracker2 = WifiTracker.this;
                wifiTracker2.mStaleScanResults = false;
                wifiTracker2.mLastScanSucceeded = intent.getBooleanExtra("resultsUpdated", true);
                WifiTracker.this.fetchScansAndConfigsAndUpdateAccessPoints();
            } else if ("android.net.wifi.CONFIGURED_NETWORKS_CHANGE".equals(action) || "android.net.wifi.LINK_CONFIGURATION_CHANGED".equals(action)) {
                WifiTracker.this.fetchScansAndConfigsAndUpdateAccessPoints();
            } else if ("android.net.wifi.STATE_CHANGE".equals(action)) {
                WifiTracker.m24$$Nest$mupdateNetworkInfo(WifiTracker.this, (NetworkInfo) intent.getParcelableExtra("networkInfo"));
                WifiTracker.this.fetchScansAndConfigsAndUpdateAccessPoints();
            } else if ("android.net.wifi.RSSI_CHANGED".equals(action)) {
                WifiTracker.m24$$Nest$mupdateNetworkInfo(WifiTracker.this, null);
            }
        }
    };

    /* loaded from: classes.dex */
    public class Scanner extends Handler {
        public int mRetry = 0;

        public boolean isScanning() {
            return hasMessages(0);
        }

        public Scanner() {
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            if (message.what == 0) {
                if (WifiTracker.this.mWifiManager.startScan()) {
                    this.mRetry = 0;
                } else {
                    int i = this.mRetry + 1;
                    this.mRetry = i;
                    if (i >= 3) {
                        this.mRetry = 0;
                        Context context = WifiTracker.this.mContext;
                        if (context != null) {
                            Toast.makeText(context, 2131953554, 1).show();
                            return;
                        }
                        return;
                    }
                }
                sendEmptyMessageDelayed(0, 10000L);
            }
        }
    }

    /* loaded from: classes.dex */
    public interface WifiListener {
        void onAccessPointsChanged();

        void onConnectedChanged();
    }

    /* loaded from: classes.dex */
    public class WifiListenerExecutor implements WifiListener {
        public final WifiListener mDelegatee;

        public WifiListenerExecutor(WifiListener wifiListener) {
            this.mDelegatee = wifiListener;
        }

        @Override // com.android.settingslib.wifi.WifiTracker.WifiListener
        public final void onAccessPointsChanged() {
            WifiListener wifiListener = this.mDelegatee;
            Objects.requireNonNull(wifiListener);
            R$dimen.postOnMainThread(new WifiTracker$WifiListenerExecutor$$ExternalSyntheticLambda1(this, "Invoking onAccessPointsChanged callback", new BubbleExpandedView$1$$ExternalSyntheticLambda0(wifiListener, 1)));
        }

        @Override // com.android.settingslib.wifi.WifiTracker.WifiListener
        public final void onConnectedChanged() {
            WifiListener wifiListener = this.mDelegatee;
            Objects.requireNonNull(wifiListener);
            R$dimen.postOnMainThread(new WifiTracker$WifiListenerExecutor$$ExternalSyntheticLambda1(this, "Invoking onConnectedChanged callback", new ScreenDecorations$$ExternalSyntheticLambda2(wifiListener, 1)));
        }
    }

    /* loaded from: classes.dex */
    public final class WifiTrackerNetworkCallback extends ConnectivityManager.NetworkCallback {
        public WifiTrackerNetworkCallback() {
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public final void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            if (network.equals(WifiTracker.this.mWifiManager.getCurrentNetwork())) {
                WifiTracker.m24$$Nest$mupdateNetworkInfo(WifiTracker.this, null);
            }
        }
    }

    public static boolean isVerboseLoggingEnabled() {
        if (sVerboseLogging || Log.isLoggable("WifiTracker", 2)) {
            return true;
        }
        return false;
    }

    public final void fetchScansAndConfigsAndUpdateAccessPoints() {
        ArrayList arrayList;
        WifiConfiguration wifiConfiguration;
        boolean z;
        List<ScanResult> scanResults = this.mWifiManager.getScanResults();
        if (scanResults == null) {
            arrayList = null;
        } else {
            boolean isEnhancedOpenSupported = this.mWifiManager.isEnhancedOpenSupported();
            boolean isWpa3SaeSupported = this.mWifiManager.isWpa3SaeSupported();
            boolean isWpa3SuiteBSupported = this.mWifiManager.isWpa3SuiteBSupported();
            arrayList = new ArrayList();
            for (ScanResult scanResult : scanResults) {
                if (scanResult.capabilities.contains("PSK")) {
                    arrayList.add(scanResult);
                } else if ((!scanResult.capabilities.contains("SUITE_B_192") || isWpa3SuiteBSupported) && ((!scanResult.capabilities.contains("SAE") || isWpa3SaeSupported) && (!scanResult.capabilities.contains("OWE") || isEnhancedOpenSupported))) {
                    arrayList.add(scanResult);
                } else if (isVerboseLoggingEnabled()) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("filterScanResultsByCapabilities: Filtering SSID ");
                    m.append(scanResult.SSID);
                    m.append(" with capabilities: ");
                    m.append(scanResult.capabilities);
                    Log.v("WifiTracker", m.toString());
                }
            }
        }
        if (isVerboseLoggingEnabled()) {
            Log.i("WifiTracker", "Fetched scan results: " + arrayList);
        }
        List<WifiConfiguration> configuredNetworks = this.mWifiManager.getConfiguredNetworks();
        WifiInfo wifiInfo = this.mLastInfo;
        if (wifiInfo != null) {
            wifiConfiguration = getWifiConfigurationForNetworkId(wifiInfo.getNetworkId(), configuredNetworks);
        } else {
            wifiConfiguration = null;
        }
        synchronized (this.mLock) {
            ArrayMap updateScanResultCache = updateScanResultCache(arrayList);
            ArrayList arrayList2 = new ArrayList(this.mInternalAccessPoints);
            ArrayList arrayList3 = new ArrayList();
            ArrayList arrayList4 = new ArrayList();
            for (Map.Entry entry : updateScanResultCache.entrySet()) {
                for (ScanResult scanResult2 : (List) entry.getValue()) {
                    NetworkKey createFromScanResult = NetworkKey.createFromScanResult(scanResult2);
                    if (createFromScanResult != null && !this.mRequestedScores.contains(createFromScanResult)) {
                        arrayList4.add(createFromScanResult);
                    }
                }
                List list = (List) entry.getValue();
                Context context = this.mContext;
                ScanResult scanResult3 = (ScanResult) list.get(0);
                int i = AccessPoint.$r8$clinit;
                AccessPoint cachedByKey = getCachedByKey(arrayList2, AccessPoint.getKey(scanResult3.SSID, scanResult3.BSSID, AccessPoint.getSecurity(context, scanResult3)));
                if (cachedByKey == null) {
                    cachedByKey = new AccessPoint(this.mContext, list);
                } else {
                    cachedByKey.setScanResults(list);
                }
                List list2 = (List) configuredNetworks.stream().filter(new WifiTracker$$ExternalSyntheticLambda0(cachedByKey, 0)).collect(Collectors.toList());
                int size = list2.size();
                if (size == 0) {
                    cachedByKey.update(null);
                } else if (size == 1) {
                    cachedByKey.update((WifiConfiguration) list2.get(0));
                } else {
                    Optional findFirst = list2.stream().filter(WifiTracker$$ExternalSyntheticLambda1.INSTANCE).findFirst();
                    if (findFirst.isPresent()) {
                        cachedByKey.update((WifiConfiguration) findFirst.get());
                    } else {
                        cachedByKey.update((WifiConfiguration) list2.get(0));
                    }
                }
                arrayList3.add(cachedByKey);
            }
            List arrayList5 = new ArrayList(this.mScanResultCache.values());
            arrayList3.addAll(updatePasspointAccessPoints(this.mWifiManager.getAllMatchingWifiConfigs(arrayList5), arrayList2));
            arrayList3.addAll(updateOsuAccessPoints(this.mWifiManager.getMatchingOsuProviders(arrayList5), arrayList2));
            if (!(this.mLastInfo == null || this.mLastNetworkInfo == null)) {
                Iterator it = arrayList3.iterator();
                while (it.hasNext()) {
                    ((AccessPoint) it.next()).update(wifiConfiguration, this.mLastInfo, this.mLastNetworkInfo);
                }
            }
            if (arrayList3.isEmpty() && wifiConfiguration != null) {
                AccessPoint accessPoint = new AccessPoint(this.mContext, wifiConfiguration);
                accessPoint.update(wifiConfiguration, this.mLastInfo, this.mLastNetworkInfo);
                arrayList3.add(accessPoint);
                arrayList4.add(NetworkKey.createFromWifiInfo(this.mLastInfo));
            }
            requestScoresForNetworkKeys(arrayList4);
            Iterator it2 = arrayList3.iterator();
            while (it2.hasNext()) {
                ((AccessPoint) it2.next()).update(this.mScoreCache, this.mNetworkScoringUiEnabled, this.mMaxSpeedLabelScoreCacheAge);
            }
            Collections.sort(arrayList3);
            if (Log.isLoggable("WifiTracker", 3)) {
                Log.d("WifiTracker", "------ Dumping AccessPoints that were not seen on this scan ------");
                Iterator it3 = this.mInternalAccessPoints.iterator();
                while (it3.hasNext()) {
                    String title = ((AccessPoint) it3.next()).getTitle();
                    Iterator it4 = arrayList3.iterator();
                    while (true) {
                        if (!it4.hasNext()) {
                            z = false;
                            break;
                        }
                        AccessPoint accessPoint2 = (AccessPoint) it4.next();
                        if (accessPoint2.getTitle() != null && accessPoint2.getTitle().equals(title)) {
                            z = true;
                            break;
                        }
                    }
                    if (!z) {
                        Log.d("WifiTracker", "Did not find " + title + " in this scan");
                    }
                }
                Log.d("WifiTracker", "---- Done dumping AccessPoints that were not seen on this scan ----");
            }
            this.mInternalAccessPoints.clear();
            this.mInternalAccessPoints.addAll(arrayList3);
        }
        if (!this.mStaleScanResults) {
            this.mListener.onAccessPointsChanged();
        }
    }

    public void forceUpdate() {
        this.mLastInfo = this.mWifiManager.getConnectionInfo();
        this.mLastNetworkInfo = this.mConnectivityManager.getNetworkInfo(this.mWifiManager.getCurrentNetwork());
        fetchScansAndConfigsAndUpdateAccessPoints();
    }

    public final WifiConfiguration getWifiConfigurationForNetworkId(int i, List<WifiConfiguration> list) {
        if (list == null) {
            return null;
        }
        for (WifiConfiguration wifiConfiguration : list) {
            if (this.mLastInfo != null && i == wifiConfiguration.networkId) {
                return wifiConfiguration;
            }
        }
        return null;
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnDestroy
    public final void onDestroy() {
        this.mWorkThread.quit();
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStop
    public final void onStop() {
        if (this.mRegistered) {
            this.mContext.unregisterReceiver(this.mReceiver);
            this.mConnectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
            this.mRegistered = false;
        }
        this.mNetworkScoreManager.unregisterNetworkScoreCache(1, this.mScoreCache);
        synchronized (this.mLock) {
            this.mRequestedScores.clear();
        }
        synchronized (this.mLock) {
            try {
                Scanner scanner = this.mScanner;
                if (scanner != null) {
                    if (isVerboseLoggingEnabled()) {
                        Log.d("WifiTracker", "Scanner pause");
                    }
                    scanner.mRetry = 0;
                    scanner.removeMessages(0);
                    this.mScanner = null;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        this.mStaleScanResults = true;
        this.mWorkHandler.removeCallbacksAndMessages(null);
    }

    public void setWorkThread(HandlerThread handlerThread) {
        this.mWorkThread = handlerThread;
        this.mWorkHandler = new Handler(handlerThread.getLooper());
        this.mScoreCache = new WifiNetworkScoreCache(this.mContext, new WifiNetworkScoreCache.CacheListener(this.mWorkHandler) { // from class: com.android.settingslib.wifi.WifiTracker.1
            public final void networkCacheUpdated(List<ScoredNetwork> list) {
                if (WifiTracker.this.mRegistered) {
                    if (Log.isLoggable("WifiTracker", 2)) {
                        Log.v("WifiTracker", "Score cache was updated with networks: " + list);
                    }
                    WifiTracker wifiTracker = WifiTracker.this;
                    Objects.requireNonNull(wifiTracker);
                    synchronized (wifiTracker.mLock) {
                        boolean z = false;
                        for (int i = 0; i < wifiTracker.mInternalAccessPoints.size(); i++) {
                            if (((AccessPoint) wifiTracker.mInternalAccessPoints.get(i)).update(wifiTracker.mScoreCache, wifiTracker.mNetworkScoringUiEnabled, wifiTracker.mMaxSpeedLabelScoreCacheAge)) {
                                z = true;
                            }
                        }
                        if (z) {
                            Collections.sort(wifiTracker.mInternalAccessPoints);
                            if (!wifiTracker.mStaleScanResults) {
                                wifiTracker.mListener.onAccessPointsChanged();
                            }
                        }
                    }
                }
            }
        });
    }

    public List<AccessPoint> updateOsuAccessPoints(Map<OsuProvider, List<ScanResult>> map, List<AccessPoint> list) {
        ArrayList arrayList = new ArrayList();
        Set keySet = this.mWifiManager.getMatchingPasspointConfigsForOsuProviders(map.keySet()).keySet();
        for (OsuProvider osuProvider : map.keySet()) {
            if (!keySet.contains(osuProvider)) {
                List<ScanResult> list2 = map.get(osuProvider);
                AccessPoint cachedByKey = getCachedByKey(list, AccessPoint.getKey(osuProvider));
                if (cachedByKey == null) {
                    cachedByKey = new AccessPoint(this.mContext, osuProvider, list2);
                } else {
                    cachedByKey.setScanResults(list2);
                }
                arrayList.add(cachedByKey);
            }
        }
        return arrayList;
    }

    public List<AccessPoint> updatePasspointAccessPoints(List<Pair<WifiConfiguration, Map<Integer, List<ScanResult>>>> list, List<AccessPoint> list2) {
        ArrayList arrayList = new ArrayList();
        ArraySet arraySet = new ArraySet();
        for (Pair<WifiConfiguration, Map<Integer, List<ScanResult>>> pair : list) {
            WifiConfiguration wifiConfiguration = (WifiConfiguration) pair.first;
            if (arraySet.add(wifiConfiguration.FQDN)) {
                List list3 = (List) ((Map) pair.second).get(0);
                List list4 = (List) ((Map) pair.second).get(1);
                AccessPoint cachedByKey = getCachedByKey(list2, AccessPoint.getKey(wifiConfiguration));
                if (cachedByKey == null) {
                    cachedByKey = new AccessPoint(this.mContext, wifiConfiguration, list3, list4);
                } else {
                    cachedByKey.update(wifiConfiguration);
                    cachedByKey.setScanResultsPasspoint(list3, list4);
                }
                arrayList.add(cachedByKey);
            }
        }
        return arrayList;
    }

    /* renamed from: -$$Nest$mupdateNetworkInfo  reason: not valid java name */
    public static void m24$$Nest$mupdateNetworkInfo(WifiTracker wifiTracker, NetworkInfo networkInfo) {
        boolean z;
        Objects.requireNonNull(wifiTracker);
        WifiManager wifiManager = wifiTracker.mWifiManager;
        boolean z2 = false;
        if (wifiManager == null || !wifiManager.isWifiEnabled()) {
            z = false;
        } else {
            z = true;
        }
        if (!z) {
            synchronized (wifiTracker.mLock) {
                if (!wifiTracker.mInternalAccessPoints.isEmpty()) {
                    wifiTracker.mInternalAccessPoints.clear();
                    if (!wifiTracker.mStaleScanResults) {
                        wifiTracker.mListener.onAccessPointsChanged();
                    }
                }
            }
            return;
        }
        if (networkInfo != null) {
            wifiTracker.mLastNetworkInfo = networkInfo;
            if (Log.isLoggable("WifiTracker", 3)) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("mLastNetworkInfo set: ");
                m.append(wifiTracker.mLastNetworkInfo);
                Log.d("WifiTracker", m.toString());
            }
            if (networkInfo.isConnected() != wifiTracker.mConnected.getAndSet(networkInfo.isConnected())) {
                wifiTracker.mListener.onConnectedChanged();
            }
        }
        WifiConfiguration wifiConfiguration = null;
        wifiTracker.mLastInfo = wifiTracker.mWifiManager.getConnectionInfo();
        if (Log.isLoggable("WifiTracker", 3)) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("mLastInfo set as: ");
            m2.append(wifiTracker.mLastInfo);
            Log.d("WifiTracker", m2.toString());
        }
        WifiInfo wifiInfo = wifiTracker.mLastInfo;
        if (wifiInfo != null) {
            wifiConfiguration = wifiTracker.getWifiConfigurationForNetworkId(wifiInfo.getNetworkId(), wifiTracker.mWifiManager.getConfiguredNetworks());
        }
        synchronized (wifiTracker.mLock) {
            boolean z3 = false;
            for (int size = wifiTracker.mInternalAccessPoints.size() - 1; size >= 0; size--) {
                AccessPoint accessPoint = (AccessPoint) wifiTracker.mInternalAccessPoints.get(size);
                boolean isActive = accessPoint.isActive();
                if (accessPoint.update(wifiConfiguration, wifiTracker.mLastInfo, wifiTracker.mLastNetworkInfo)) {
                    if (isActive != accessPoint.isActive()) {
                        z2 = true;
                        z3 = true;
                    } else {
                        z3 = true;
                    }
                }
                if (accessPoint.update(wifiTracker.mScoreCache, wifiTracker.mNetworkScoringUiEnabled, wifiTracker.mMaxSpeedLabelScoreCacheAge)) {
                    z2 = true;
                    z3 = true;
                }
            }
            if (z2) {
                Collections.sort(wifiTracker.mInternalAccessPoints);
            }
            if (z3 && !wifiTracker.mStaleScanResults) {
                wifiTracker.mListener.onAccessPointsChanged();
            }
        }
    }

    public WifiTracker(Context context, WifiListener wifiListener, WifiManager wifiManager, ConnectivityManager connectivityManager, NetworkScoreManager networkScoreManager, IntentFilter intentFilter) {
        boolean z = false;
        this.mContext = context;
        this.mWifiManager = wifiManager;
        this.mListener = new WifiListenerExecutor(wifiListener);
        this.mConnectivityManager = connectivityManager;
        if (wifiManager != null && wifiManager.isVerboseLoggingEnabled()) {
            z = true;
        }
        sVerboseLogging = z;
        this.mFilter = intentFilter;
        this.mNetworkRequest = new NetworkRequest.Builder().clearCapabilities().addCapability(15).addTransportType(1).build();
        this.mNetworkScoreManager = networkScoreManager;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("WifiTracker{");
        m.append(Integer.toHexString(System.identityHashCode(this)));
        m.append("}");
        HandlerThread handlerThread = new HandlerThread(m.toString(), 10);
        handlerThread.start();
        setWorkThread(handlerThread);
    }

    public static AccessPoint getCachedByKey(List list, String str) {
        ListIterator listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            AccessPoint accessPoint = (AccessPoint) listIterator.next();
            Objects.requireNonNull(accessPoint);
            if (accessPoint.mKey.equals(str)) {
                listIterator.remove();
                return accessPoint;
            }
        }
        return null;
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStart
    public final void onStart() {
        boolean z;
        boolean z2;
        forceUpdate();
        this.mNetworkScoreManager.registerNetworkScoreCache(1, this.mScoreCache, 2);
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "network_scoring_ui_enabled", 0) == 1) {
            z = true;
        } else {
            z = false;
        }
        this.mNetworkScoringUiEnabled = z;
        this.mMaxSpeedLabelScoreCacheAge = Settings.Global.getLong(this.mContext.getContentResolver(), "speed_label_cache_eviction_age_millis", 1200000L);
        synchronized (this.mLock) {
            try {
                if (this.mScanner == null) {
                    this.mScanner = new Scanner();
                }
                WifiManager wifiManager = this.mWifiManager;
                if (wifiManager == null || !wifiManager.isWifiEnabled()) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                if (z2) {
                    Scanner scanner = this.mScanner;
                    Objects.requireNonNull(scanner);
                    if (isVerboseLoggingEnabled()) {
                        Log.d("WifiTracker", "Scanner resume");
                    }
                    if (!scanner.hasMessages(0)) {
                        scanner.sendEmptyMessage(0);
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (!this.mRegistered) {
            this.mContext.registerReceiver(this.mReceiver, this.mFilter, null, this.mWorkHandler, 2);
            WifiTrackerNetworkCallback wifiTrackerNetworkCallback = new WifiTrackerNetworkCallback();
            this.mNetworkCallback = wifiTrackerNetworkCallback;
            this.mConnectivityManager.registerNetworkCallback(this.mNetworkRequest, wifiTrackerNetworkCallback, this.mWorkHandler);
            this.mRegistered = true;
        }
    }

    public final void requestScoresForNetworkKeys(ArrayList arrayList) {
        if (!arrayList.isEmpty()) {
            if (Log.isLoggable("WifiTracker", 3)) {
                Log.d("WifiTracker", "Requesting scores for Network Keys: " + arrayList);
            }
            this.mNetworkScoreManager.requestScores((NetworkKey[]) arrayList.toArray(new NetworkKey[arrayList.size()]));
            synchronized (this.mLock) {
                this.mRequestedScores.addAll(arrayList);
            }
        }
    }

    public final ArrayMap updateScanResultCache(ArrayList arrayList) {
        long j;
        List list;
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ScanResult scanResult = (ScanResult) it.next();
            String str = scanResult.SSID;
            if (str != null && !str.isEmpty()) {
                this.mScanResultCache.put(scanResult.BSSID, scanResult);
            }
        }
        if (this.mLastScanSucceeded) {
            j = MAX_SCAN_RESULT_AGE_MILLIS;
        } else {
            j = 30000;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        Iterator<ScanResult> it2 = this.mScanResultCache.values().iterator();
        while (it2.hasNext()) {
            if (elapsedRealtime - (it2.next().timestamp / 1000) > j) {
                it2.remove();
            }
        }
        ArrayMap arrayMap = new ArrayMap();
        for (ScanResult scanResult2 : this.mScanResultCache.values()) {
            String str2 = scanResult2.SSID;
            if (!(str2 == null || str2.length() == 0 || scanResult2.capabilities.contains("[IBSS]"))) {
                Context context = this.mContext;
                int i = AccessPoint.$r8$clinit;
                String key = AccessPoint.getKey(scanResult2.SSID, scanResult2.BSSID, AccessPoint.getSecurity(context, scanResult2));
                if (arrayMap.containsKey(key)) {
                    list = (List) arrayMap.get(key);
                } else {
                    ArrayList arrayList2 = new ArrayList();
                    arrayMap.put(key, arrayList2);
                    list = arrayList2;
                }
                list.add(scanResult2);
            }
        }
        return arrayMap;
    }
}
