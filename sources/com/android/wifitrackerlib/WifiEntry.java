package com.android.wifitrackerlib;

import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.RouteInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda0;
import com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda1;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
/* loaded from: classes.dex */
public class WifiEntry implements Comparable<WifiEntry> {
    public final Handler mCallbackHandler;
    public ConnectCallback mConnectCallback;
    public ConnectedInfo mConnectedInfo;
    public final boolean mForSavedNetworksPage;
    public boolean mIsDefaultNetwork;
    public boolean mIsLowQuality;
    public boolean mIsValidated;
    public WifiEntryCallback mListener;
    public NetworkCapabilities mNetworkCapabilities;
    public NetworkInfo mNetworkInfo;
    public WifiInfo mWifiInfo;
    public final WifiManager mWifiManager;
    public int mLevel = -1;
    public boolean mCalledConnect = false;

    /* loaded from: classes.dex */
    public class ConnectActionListener implements WifiManager.ActionListener {
        public ConnectActionListener() {
        }

        public final void onFailure(int i) {
            WifiEntry.this.mCallbackHandler.post(new AccessPoint$$ExternalSyntheticLambda0(this, 10));
        }

        public final void onSuccess() {
            WifiEntry wifiEntry;
            synchronized (WifiEntry.this) {
                wifiEntry = WifiEntry.this;
                wifiEntry.mCalledConnect = true;
            }
            wifiEntry.mCallbackHandler.postDelayed(new AccessPoint$$ExternalSyntheticLambda1(this, 10), 10000L);
        }
    }

    /* loaded from: classes.dex */
    public interface ConnectCallback {
    }

    /* loaded from: classes.dex */
    public interface WifiEntryCallback {
        void onUpdated();
    }

    public boolean canSetMeteredChoice() {
        return false;
    }

    public boolean canSignIn() {
        return false;
    }

    public void connect(InternetDialogController.WifiEntryConnectCallback wifiEntryConnectCallback) {
    }

    public boolean connectionInfoMatches(WifiInfo wifiInfo) {
        return false;
    }

    public synchronized int getConnectedState() {
        NetworkInfo networkInfo = this.mNetworkInfo;
        if (networkInfo == null) {
            return 0;
        }
        switch (AnonymousClass1.$SwitchMap$android$net$NetworkInfo$DetailedState[networkInfo.getDetailedState().ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case FalsingManager.VERSION /* 6 */:
                return 1;
            case 7:
                return 2;
            default:
                return 0;
        }
    }

    public String getKey() {
        return "";
    }

    public int getMeteredChoice() {
        return 0;
    }

    public String getNetworkSelectionDescription() {
        return "";
    }

    public String getScanResultDescription() {
        return "";
    }

    public String getSummary(boolean z) {
        return "";
    }

    public String getTitle() {
        return "";
    }

    public WifiConfiguration getWifiConfiguration() {
        return null;
    }

    public boolean isMetered() {
        return false;
    }

    public boolean isSaved() {
        return false;
    }

    public boolean isSubscription() {
        return false;
    }

    public boolean isSuggestion() {
        return false;
    }

    public final synchronized void setIsDefaultNetwork(boolean z) {
        this.mIsDefaultNetwork = z;
        notifyOnUpdated();
    }

    public final synchronized void setIsLowQuality(boolean z) {
        this.mIsLowQuality = z;
    }

    public boolean shouldEditBeforeConnect() {
        return false;
    }

    public final synchronized void updateConnectionInfo(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        if (!(wifiInfo == null || networkInfo == null)) {
            if (connectionInfoMatches(wifiInfo)) {
                this.mWifiInfo = wifiInfo;
                this.mNetworkInfo = networkInfo;
                int rssi = wifiInfo.getRssi();
                if (rssi != -127) {
                    this.mLevel = this.mWifiManager.calculateSignalLevel(rssi);
                }
                if (getConnectedState() == 2) {
                    if (this.mCalledConnect) {
                        this.mCalledConnect = false;
                        this.mCallbackHandler.post(new WifiEntry$$ExternalSyntheticLambda1(this, 0));
                    }
                    if (this.mConnectedInfo == null) {
                        this.mConnectedInfo = new ConnectedInfo();
                    }
                    ConnectedInfo connectedInfo = this.mConnectedInfo;
                    wifiInfo.getFrequency();
                    Objects.requireNonNull(connectedInfo);
                    ConnectedInfo connectedInfo2 = this.mConnectedInfo;
                    wifiInfo.getLinkSpeed();
                    Objects.requireNonNull(connectedInfo2);
                    ConnectedInfo connectedInfo3 = this.mConnectedInfo;
                    wifiInfo.getWifiStandard();
                    Objects.requireNonNull(connectedInfo3);
                }
                updateSecurityTypes();
                notifyOnUpdated();
            }
        }
        this.mWifiInfo = null;
        this.mNetworkInfo = null;
        this.mNetworkCapabilities = null;
        this.mConnectedInfo = null;
        this.mIsValidated = false;
        this.mIsDefaultNetwork = false;
        this.mIsLowQuality = false;
        updateSecurityTypes();
        notifyOnUpdated();
    }

    public final synchronized void updateLinkProperties(LinkProperties linkProperties) {
        if (linkProperties != null) {
            if (getConnectedState() == 2) {
                if (this.mConnectedInfo == null) {
                    this.mConnectedInfo = new ConnectedInfo();
                }
                ArrayList arrayList = new ArrayList();
                for (LinkAddress linkAddress : linkProperties.getLinkAddresses()) {
                    if (linkAddress.getAddress() instanceof Inet4Address) {
                        ConnectedInfo connectedInfo = this.mConnectedInfo;
                        linkAddress.getAddress().getHostAddress();
                        Objects.requireNonNull(connectedInfo);
                        try {
                            InetAddress byAddress = InetAddress.getByAddress(new byte[]{-1, -1, -1, -1});
                            ConnectedInfo connectedInfo2 = this.mConnectedInfo;
                            Utils.getNetworkPart(byAddress, linkAddress.getPrefixLength()).getHostAddress();
                            Objects.requireNonNull(connectedInfo2);
                        } catch (UnknownHostException unused) {
                        }
                    } else if (linkAddress.getAddress() instanceof Inet6Address) {
                        arrayList.add(linkAddress.getAddress().getHostAddress());
                    }
                }
                this.mConnectedInfo.ipv6Addresses = arrayList;
                Iterator<RouteInfo> it = linkProperties.getRoutes().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    RouteInfo next = it.next();
                    if (next.isDefaultRoute() && (next.getDestination().getAddress() instanceof Inet4Address) && next.hasGateway()) {
                        ConnectedInfo connectedInfo3 = this.mConnectedInfo;
                        next.getGateway().getHostAddress();
                        Objects.requireNonNull(connectedInfo3);
                        break;
                    }
                }
                this.mConnectedInfo.dnsServers = (List) linkProperties.getDnsServers().stream().map(WifiEntry$$ExternalSyntheticLambda3.INSTANCE).collect(Collectors.toList());
                notifyOnUpdated();
                return;
            }
        }
        this.mConnectedInfo = null;
        notifyOnUpdated();
    }

    public synchronized void updateNetworkCapabilities(NetworkCapabilities networkCapabilities) {
        boolean z;
        this.mNetworkCapabilities = networkCapabilities;
        if (this.mConnectedInfo != null) {
            if (networkCapabilities == null || !networkCapabilities.hasCapability(16)) {
                z = false;
            } else {
                z = true;
            }
            this.mIsValidated = z;
            notifyOnUpdated();
        }
    }

    public void updateSecurityTypes() {
    }

    /* renamed from: com.android.wifitrackerlib.WifiEntry$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$android$net$NetworkInfo$DetailedState;

        static {
            int[] iArr = new int[NetworkInfo.DetailedState.values().length];
            $SwitchMap$android$net$NetworkInfo$DetailedState = iArr;
            try {
                iArr[NetworkInfo.DetailedState.SCANNING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$android$net$NetworkInfo$DetailedState[NetworkInfo.DetailedState.CONNECTING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$android$net$NetworkInfo$DetailedState[NetworkInfo.DetailedState.AUTHENTICATING.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$android$net$NetworkInfo$DetailedState[NetworkInfo.DetailedState.OBTAINING_IPADDR.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$android$net$NetworkInfo$DetailedState[NetworkInfo.DetailedState.VERIFYING_POOR_LINK.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$android$net$NetworkInfo$DetailedState[NetworkInfo.DetailedState.CAPTIVE_PORTAL_CHECK.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$android$net$NetworkInfo$DetailedState[NetworkInfo.DetailedState.CONNECTED.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x001a, code lost:
        if (r5.mLevel != (-1)) goto L_0x001c;
     */
    @Override // java.lang.Comparable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int compareTo(com.android.wifitrackerlib.WifiEntry r5) {
        /*
            r4 = this;
            com.android.wifitrackerlib.WifiEntry r5 = (com.android.wifitrackerlib.WifiEntry) r5
            int r0 = r4.mLevel
            r1 = -1
            if (r0 == r1) goto L_0x0010
            java.util.Objects.requireNonNull(r5)
            int r0 = r5.mLevel
            if (r0 != r1) goto L_0x0010
            goto L_0x0088
        L_0x0010:
            int r0 = r4.mLevel
            r2 = 1
            if (r0 != r1) goto L_0x001f
            java.util.Objects.requireNonNull(r5)
            int r0 = r5.mLevel
            if (r0 == r1) goto L_0x001f
        L_0x001c:
            r1 = r2
            goto L_0x0088
        L_0x001f:
            boolean r0 = r4.isSubscription()
            if (r0 == 0) goto L_0x002c
            boolean r0 = r5.isSubscription()
            if (r0 != 0) goto L_0x002c
            goto L_0x0088
        L_0x002c:
            boolean r0 = r4.isSubscription()
            if (r0 != 0) goto L_0x0039
            boolean r0 = r5.isSubscription()
            if (r0 == 0) goto L_0x0039
            goto L_0x001c
        L_0x0039:
            boolean r0 = r4.isSaved()
            if (r0 == 0) goto L_0x0046
            boolean r0 = r5.isSaved()
            if (r0 != 0) goto L_0x0046
            goto L_0x0088
        L_0x0046:
            boolean r0 = r4.isSaved()
            if (r0 != 0) goto L_0x0053
            boolean r0 = r5.isSaved()
            if (r0 == 0) goto L_0x0053
            goto L_0x001c
        L_0x0053:
            boolean r0 = r4.isSuggestion()
            if (r0 == 0) goto L_0x0060
            boolean r0 = r5.isSuggestion()
            if (r0 != 0) goto L_0x0060
            goto L_0x0088
        L_0x0060:
            boolean r0 = r4.isSuggestion()
            if (r0 != 0) goto L_0x006d
            boolean r0 = r5.isSuggestion()
            if (r0 == 0) goto L_0x006d
            goto L_0x001c
        L_0x006d:
            int r0 = r4.mLevel
            java.util.Objects.requireNonNull(r5)
            int r3 = r5.mLevel
            if (r0 <= r3) goto L_0x0077
            goto L_0x0088
        L_0x0077:
            int r0 = r4.mLevel
            if (r0 >= r3) goto L_0x007c
            goto L_0x001c
        L_0x007c:
            java.lang.String r4 = r4.getTitle()
            java.lang.String r5 = r5.getTitle()
            int r1 = r4.compareTo(r5)
        L_0x0088:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.WifiEntry.compareTo(java.lang.Object):int");
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof WifiEntry)) {
            return false;
        }
        return getKey().equals(((WifiEntry) obj).getKey());
    }

    public final void notifyOnUpdated() {
        if (this.mListener != null) {
            this.mCallbackHandler.post(new WifiEntry$$ExternalSyntheticLambda0(this, 0));
        }
    }

    public final String toString() {
        String str;
        String str2;
        ConnectedInfo connectedInfo;
        StringBuilder sb = new StringBuilder();
        sb.append(getKey());
        sb.append(",title:");
        sb.append(getTitle());
        sb.append(",summary:");
        sb.append(getSummary(true));
        sb.append(",isSaved:");
        sb.append(isSaved());
        sb.append(",isSubscription:");
        sb.append(isSubscription());
        sb.append(",isSuggestion:");
        sb.append(isSuggestion());
        sb.append(",level:");
        sb.append(this.mLevel);
        if (shouldShowXLevelIcon()) {
            str = "X";
        } else {
            str = "";
        }
        sb.append(str);
        sb.append(",security:");
        sb.append(getSecurityTypes());
        sb.append(",connected:");
        if (getConnectedState() == 2) {
            str2 = "true";
        } else {
            str2 = "false";
        }
        sb.append(str2);
        sb.append(",connectedInfo:");
        synchronized (this) {
            if (getConnectedState() != 2) {
                connectedInfo = null;
            } else {
                connectedInfo = new ConnectedInfo(this.mConnectedInfo);
            }
        }
        sb.append(connectedInfo);
        sb.append(",isValidated:");
        sb.append(this.mIsValidated);
        sb.append(",isDefaultNetwork:");
        sb.append(this.mIsDefaultNetwork);
        return sb.toString();
    }

    /* loaded from: classes.dex */
    public static class ConnectedInfo {
        public List<String> dnsServers;
        public ArrayList ipv6Addresses;

        public ConnectedInfo() {
            this.dnsServers = new ArrayList();
            this.ipv6Addresses = new ArrayList();
        }

        public ConnectedInfo(ConnectedInfo connectedInfo) {
            this.dnsServers = new ArrayList();
            this.ipv6Addresses = new ArrayList();
            Objects.requireNonNull(connectedInfo);
            this.dnsServers = new ArrayList(this.dnsServers);
            this.ipv6Addresses = new ArrayList(connectedInfo.ipv6Addresses);
        }
    }

    public WifiEntry(Handler handler, WifiManager wifiManager, boolean z) throws IllegalArgumentException {
        Optional.empty();
        Objects.requireNonNull(handler, "Cannot construct with null handler!");
        Objects.requireNonNull(wifiManager, "Cannot construct with null WifiManager!");
        this.mCallbackHandler = handler;
        this.mForSavedNetworksPage = z;
        this.mWifiManager = wifiManager;
    }

    public List<Integer> getSecurityTypes() {
        return Collections.emptyList();
    }

    public final int hashCode() {
        return getKey().hashCode();
    }

    public final boolean shouldShowXLevelIcon() {
        if (getConnectedState() == 0 || ((this.mIsValidated && this.mIsDefaultNetwork) || canSignIn())) {
            return false;
        }
        return true;
    }
}
