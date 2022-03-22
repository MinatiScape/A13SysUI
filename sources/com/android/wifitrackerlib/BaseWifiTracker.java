package com.android.wifitrackerlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.TransportInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import com.android.systemui.doze.DozeScreenState$$ExternalSyntheticLambda0;
import com.android.systemui.qs.QSFgsManagerFooter$$ExternalSyntheticLambda0;
import java.time.Clock;
import java.util.Objects;
/* loaded from: classes.dex */
public class BaseWifiTracker implements LifecycleObserver {
    public static boolean sVerboseLogging;
    public final ConnectivityManager mConnectivityManager;
    public final Context mContext;
    public final WifiTrackerInjector mInjector;
    public boolean mIsCellDefaultRoute;
    public boolean mIsStarted;
    public boolean mIsWifiDefaultRoute;
    public boolean mIsWifiValidated;
    public final BaseWifiTrackerCallback mListener;
    public final Handler mMainHandler;
    public final long mMaxScanAgeMillis;
    public final long mScanIntervalMillis;
    public final ScanResultUpdater mScanResultUpdater;
    public final Scanner mScanner;
    public final String mTag;
    public final WifiManager mWifiManager;
    public final Handler mWorkerHandler;
    public final AnonymousClass1 mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.wifitrackerlib.BaseWifiTracker.1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            BaseWifiTracker baseWifiTracker = BaseWifiTracker.this;
            if (!baseWifiTracker.mIsStarted) {
                baseWifiTracker.mIsStarted = true;
                baseWifiTracker.handleOnStart();
            }
            String action = intent.getAction();
            if (BaseWifiTracker.sVerboseLogging) {
                String str = BaseWifiTracker.this.mTag;
                Log.v(str, "Received broadcast: " + action);
            }
            if ("android.net.wifi.WIFI_STATE_CHANGED".equals(action)) {
                if (BaseWifiTracker.this.mWifiManager.getWifiState() == 3) {
                    Scanner scanner = BaseWifiTracker.this.mScanner;
                    int i = Scanner.$r8$clinit;
                    Objects.requireNonNull(scanner);
                    if (!scanner.mIsActive) {
                        scanner.mIsActive = true;
                        if (BaseWifiTracker.sVerboseLogging) {
                            Log.v(BaseWifiTracker.this.mTag, "Scanner start");
                        }
                        scanner.postScan();
                    }
                } else {
                    Scanner scanner2 = BaseWifiTracker.this.mScanner;
                    int i2 = Scanner.$r8$clinit;
                    Objects.requireNonNull(scanner2);
                    scanner2.mIsActive = false;
                    if (BaseWifiTracker.sVerboseLogging) {
                        Log.v(BaseWifiTracker.this.mTag, "Scanner stop");
                    }
                    scanner2.mRetry = 0;
                    scanner2.removeCallbacksAndMessages(null);
                }
                BaseWifiTracker baseWifiTracker2 = BaseWifiTracker.this;
                Objects.requireNonNull(baseWifiTracker2);
                BaseWifiTrackerCallback baseWifiTrackerCallback = baseWifiTracker2.mListener;
                if (baseWifiTrackerCallback != null) {
                    baseWifiTracker2.mMainHandler.post(new QSFgsManagerFooter$$ExternalSyntheticLambda0(baseWifiTrackerCallback, 7));
                }
                BaseWifiTracker.this.handleWifiStateChangedAction();
            } else if ("android.net.wifi.SCAN_RESULTS".equals(action)) {
                BaseWifiTracker.this.handleScanResultsAvailableAction(intent);
            } else if ("android.net.wifi.CONFIGURED_NETWORKS_CHANGE".equals(action)) {
                BaseWifiTracker.this.handleConfiguredNetworksChangedAction(intent);
            } else if ("android.net.wifi.STATE_CHANGE".equals(action)) {
                BaseWifiTracker.this.handleNetworkStateChangedAction(intent);
            } else if ("android.net.wifi.RSSI_CHANGED".equals(action)) {
                BaseWifiTracker.this.handleRssiChangedAction();
            } else if ("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED".equals(action)) {
                BaseWifiTracker.this.handleDefaultSubscriptionChanged(intent.getIntExtra("subscription", -1));
            }
        }
    };
    public final NetworkRequest mNetworkRequest = new NetworkRequest.Builder().clearCapabilities().addCapability(15).addTransportType(1).build();
    public final AnonymousClass2 mNetworkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.android.wifitrackerlib.BaseWifiTracker.2
        @Override // android.net.ConnectivityManager.NetworkCallback
        public final void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            BaseWifiTracker baseWifiTracker = BaseWifiTracker.this;
            if (!baseWifiTracker.mIsStarted) {
                baseWifiTracker.mIsStarted = true;
                baseWifiTracker.handleOnStart();
            }
            if (BaseWifiTracker.m149$$Nest$misPrimaryWifiNetwork(BaseWifiTracker.this, networkCapabilities)) {
                BaseWifiTracker baseWifiTracker2 = BaseWifiTracker.this;
                boolean z = baseWifiTracker2.mIsWifiValidated;
                baseWifiTracker2.mIsWifiValidated = networkCapabilities.hasCapability(16);
                if (BaseWifiTracker.sVerboseLogging) {
                    BaseWifiTracker baseWifiTracker3 = BaseWifiTracker.this;
                    if (baseWifiTracker3.mIsWifiValidated != z) {
                        String str = baseWifiTracker3.mTag;
                        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Is Wifi validated: ");
                        m.append(BaseWifiTracker.this.mIsWifiValidated);
                        Log.v(str, m.toString());
                    }
                }
                BaseWifiTracker.this.handleNetworkCapabilitiesChanged(networkCapabilities);
            }
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public final void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            BaseWifiTracker baseWifiTracker = BaseWifiTracker.this;
            if (!baseWifiTracker.mIsStarted) {
                baseWifiTracker.mIsStarted = true;
                baseWifiTracker.handleOnStart();
            }
            BaseWifiTracker baseWifiTracker2 = BaseWifiTracker.this;
            if (BaseWifiTracker.m149$$Nest$misPrimaryWifiNetwork(baseWifiTracker2, baseWifiTracker2.mConnectivityManager.getNetworkCapabilities(network))) {
                BaseWifiTracker.this.handleLinkPropertiesChanged(linkProperties);
            }
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public final void onLost(Network network) {
            BaseWifiTracker baseWifiTracker = BaseWifiTracker.this;
            if (!baseWifiTracker.mIsStarted) {
                baseWifiTracker.mIsStarted = true;
                baseWifiTracker.handleOnStart();
            }
            BaseWifiTracker baseWifiTracker2 = BaseWifiTracker.this;
            if (BaseWifiTracker.m149$$Nest$misPrimaryWifiNetwork(baseWifiTracker2, baseWifiTracker2.mConnectivityManager.getNetworkCapabilities(network))) {
                BaseWifiTracker.this.mIsWifiValidated = false;
            }
        }
    };
    public final AnonymousClass3 mDefaultNetworkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.android.wifitrackerlib.BaseWifiTracker.3
        /* JADX WARN: Removed duplicated region for block: B:29:0x0054  */
        @Override // android.net.ConnectivityManager.NetworkCallback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onCapabilitiesChanged(android.net.Network r7, android.net.NetworkCapabilities r8) {
            /*
                r6 = this;
                com.android.wifitrackerlib.BaseWifiTracker r7 = com.android.wifitrackerlib.BaseWifiTracker.this
                boolean r0 = r7.mIsStarted
                r1 = 1
                if (r0 != 0) goto L_0x000c
                r7.mIsStarted = r1
                r7.handleOnStart()
            L_0x000c:
                com.android.wifitrackerlib.BaseWifiTracker r7 = com.android.wifitrackerlib.BaseWifiTracker.this
                boolean r0 = r7.mIsWifiDefaultRoute
                boolean r2 = r7.mIsCellDefaultRoute
                boolean r3 = r8.hasTransport(r1)
                r4 = 0
                if (r3 != 0) goto L_0x0033
                android.net.TransportInfo r3 = r8.getTransportInfo()
                if (r3 == 0) goto L_0x002d
                boolean r5 = r3 instanceof android.net.vcn.VcnTransportInfo
                if (r5 == 0) goto L_0x002d
                android.net.vcn.VcnTransportInfo r3 = (android.net.vcn.VcnTransportInfo) r3
                android.net.wifi.WifiInfo r3 = r3.getWifiInfo()
                if (r3 == 0) goto L_0x002d
                r3 = r1
                goto L_0x002e
            L_0x002d:
                r3 = r4
            L_0x002e:
                if (r3 == 0) goto L_0x0031
                goto L_0x0033
            L_0x0031:
                r3 = r4
                goto L_0x0034
            L_0x0033:
                r3 = r1
            L_0x0034:
                r7.mIsWifiDefaultRoute = r3
                com.android.wifitrackerlib.BaseWifiTracker r7 = com.android.wifitrackerlib.BaseWifiTracker.this
                boolean r3 = r7.mIsWifiDefaultRoute
                if (r3 != 0) goto L_0x0043
                boolean r8 = r8.hasTransport(r4)
                if (r8 == 0) goto L_0x0043
                goto L_0x0044
            L_0x0043:
                r1 = r4
            L_0x0044:
                r7.mIsCellDefaultRoute = r1
                com.android.wifitrackerlib.BaseWifiTracker r7 = com.android.wifitrackerlib.BaseWifiTracker.this
                boolean r8 = r7.mIsWifiDefaultRoute
                if (r8 != r0) goto L_0x0050
                boolean r8 = r7.mIsCellDefaultRoute
                if (r8 == r2) goto L_0x0087
            L_0x0050:
                boolean r8 = com.android.wifitrackerlib.BaseWifiTracker.sVerboseLogging
                if (r8 == 0) goto L_0x0082
                java.lang.String r7 = r7.mTag
                java.lang.String r8 = "Wifi is the default route: "
                java.lang.StringBuilder r8 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1.m(r8)
                com.android.wifitrackerlib.BaseWifiTracker r0 = com.android.wifitrackerlib.BaseWifiTracker.this
                boolean r0 = r0.mIsWifiDefaultRoute
                r8.append(r0)
                java.lang.String r8 = r8.toString()
                android.util.Log.v(r7, r8)
                com.android.wifitrackerlib.BaseWifiTracker r7 = com.android.wifitrackerlib.BaseWifiTracker.this
                java.lang.String r7 = r7.mTag
                java.lang.String r8 = "Cell is the default route: "
                java.lang.StringBuilder r8 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1.m(r8)
                com.android.wifitrackerlib.BaseWifiTracker r0 = com.android.wifitrackerlib.BaseWifiTracker.this
                boolean r0 = r0.mIsCellDefaultRoute
                r8.append(r0)
                java.lang.String r8 = r8.toString()
                android.util.Log.v(r7, r8)
            L_0x0082:
                com.android.wifitrackerlib.BaseWifiTracker r6 = com.android.wifitrackerlib.BaseWifiTracker.this
                r6.handleDefaultRouteChanged()
            L_0x0087:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.BaseWifiTracker.AnonymousClass3.onCapabilitiesChanged(android.net.Network, android.net.NetworkCapabilities):void");
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public final void onLost(Network network) {
            BaseWifiTracker baseWifiTracker = BaseWifiTracker.this;
            if (!baseWifiTracker.mIsStarted) {
                baseWifiTracker.mIsStarted = true;
                baseWifiTracker.handleOnStart();
            }
            BaseWifiTracker baseWifiTracker2 = BaseWifiTracker.this;
            baseWifiTracker2.mIsWifiDefaultRoute = false;
            baseWifiTracker2.mIsCellDefaultRoute = false;
            if (BaseWifiTracker.sVerboseLogging) {
                Log.v(baseWifiTracker2.mTag, "Wifi is the default route: false");
                Log.v(BaseWifiTracker.this.mTag, "Cell is the default route: false");
            }
            BaseWifiTracker.this.handleDefaultRouteChanged();
        }
    };

    /* loaded from: classes.dex */
    public interface BaseWifiTrackerCallback {
    }

    /* loaded from: classes.dex */
    public class Scanner extends Handler {
        public static final /* synthetic */ int $r8$clinit = 0;
        public boolean mIsActive;
        public int mRetry = 0;

        public Scanner(Looper looper) {
            super(looper);
        }

        public final void postScan() {
            if (BaseWifiTracker.this.mWifiManager.startScan()) {
                this.mRetry = 0;
            } else {
                int i = this.mRetry + 1;
                this.mRetry = i;
                if (i >= 3) {
                    if (BaseWifiTracker.sVerboseLogging) {
                        String str = BaseWifiTracker.this.mTag;
                        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Scanner failed to start scan ");
                        m.append(this.mRetry);
                        m.append(" times!");
                        Log.v(str, m.toString());
                    }
                    this.mRetry = 0;
                    return;
                }
            }
            postDelayed(new DozeScreenState$$ExternalSyntheticLambda0(this, 6), BaseWifiTracker.this.mScanIntervalMillis);
        }
    }

    /* JADX WARN: Type inference failed for: r5v0, types: [com.android.wifitrackerlib.BaseWifiTracker$1] */
    /* JADX WARN: Type inference failed for: r5v6, types: [com.android.wifitrackerlib.BaseWifiTracker$2] */
    /* JADX WARN: Type inference failed for: r5v7, types: [com.android.wifitrackerlib.BaseWifiTracker$3] */
    public BaseWifiTracker(WifiTrackerInjector wifiTrackerInjector, Lifecycle lifecycle, Context context, WifiManager wifiManager, ConnectivityManager connectivityManager, Handler handler, Handler handler2, Clock clock, long j, long j2, BaseWifiTrackerCallback baseWifiTrackerCallback, String str) {
        this.mInjector = wifiTrackerInjector;
        lifecycle.addObserver(this);
        this.mContext = context;
        this.mWifiManager = wifiManager;
        this.mConnectivityManager = connectivityManager;
        this.mMainHandler = handler;
        this.mWorkerHandler = handler2;
        this.mMaxScanAgeMillis = j;
        this.mScanIntervalMillis = j2;
        this.mListener = baseWifiTrackerCallback;
        this.mTag = str;
        this.mScanResultUpdater = new ScanResultUpdater(clock, j + j2);
        this.mScanner = new Scanner(handler2.getLooper());
        sVerboseLogging = wifiManager.isVerboseLoggingEnabled();
    }

    public void handleConfiguredNetworksChangedAction(Intent intent) {
    }

    public void handleDefaultRouteChanged() {
    }

    public void handleDefaultSubscriptionChanged(int i) {
    }

    public void handleLinkPropertiesChanged(LinkProperties linkProperties) {
    }

    public void handleNetworkCapabilitiesChanged(NetworkCapabilities networkCapabilities) {
    }

    public void handleNetworkStateChangedAction(Intent intent) {
    }

    public void handleOnStart() {
    }

    public void handleRssiChangedAction() {
    }

    public void handleScanResultsAvailableAction(Intent intent) {
    }

    public void handleWifiStateChangedAction() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter.addAction("android.net.wifi.SCAN_RESULTS");
        intentFilter.addAction("android.net.wifi.CONFIGURED_NETWORKS_CHANGE");
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        intentFilter.addAction("android.net.wifi.RSSI_CHANGED");
        intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, null, this.mWorkerHandler);
        this.mConnectivityManager.registerNetworkCallback(this.mNetworkRequest, this.mNetworkCallback, this.mWorkerHandler);
        this.mConnectivityManager.registerDefaultNetworkCallback(this.mDefaultNetworkCallback, this.mWorkerHandler);
        ConnectivityManager connectivityManager = this.mConnectivityManager;
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        if (networkCapabilities != null) {
            this.mIsWifiDefaultRoute = networkCapabilities.hasTransport(1);
            this.mIsCellDefaultRoute = networkCapabilities.hasTransport(0);
        } else {
            this.mIsWifiDefaultRoute = false;
            this.mIsCellDefaultRoute = false;
        }
        if (sVerboseLogging) {
            String str = this.mTag;
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Wifi is the default route: ");
            m.append(this.mIsWifiDefaultRoute);
            Log.v(str, m.toString());
            String str2 = this.mTag;
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Cell is the default route: ");
            m2.append(this.mIsCellDefaultRoute);
            Log.v(str2, m2.toString());
        }
        this.mWorkerHandler.post(new BaseWifiTracker$$ExternalSyntheticLambda1(this, 0));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        Handler handler = this.mWorkerHandler;
        Scanner scanner = this.mScanner;
        Objects.requireNonNull(scanner);
        handler.post(new BaseWifiTracker$$ExternalSyntheticLambda0(scanner, 0));
        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        this.mConnectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
        this.mConnectivityManager.unregisterNetworkCallback(this.mDefaultNetworkCallback);
        this.mIsStarted = false;
    }

    /* renamed from: -$$Nest$misPrimaryWifiNetwork  reason: not valid java name */
    public static boolean m149$$Nest$misPrimaryWifiNetwork(BaseWifiTracker baseWifiTracker, NetworkCapabilities networkCapabilities) {
        Objects.requireNonNull(baseWifiTracker);
        if (networkCapabilities == null) {
            return false;
        }
        TransportInfo transportInfo = networkCapabilities.getTransportInfo();
        if (!(transportInfo instanceof WifiInfo)) {
            return false;
        }
        return ((WifiInfo) transportInfo).isPrimary();
    }
}
