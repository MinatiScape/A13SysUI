package com.android.wifitrackerlib;

import android.content.Context;
import android.content.pm.PackageManager;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;
/* loaded from: classes.dex */
public final class Utils {
    /* JADX WARN: Removed duplicated region for block: B:32:0x00a2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String getConnectedDescription(android.content.Context r4, android.net.wifi.WifiConfiguration r5, android.net.NetworkCapabilities r6, boolean r7, boolean r8) {
        /*
            java.util.StringJoiner r0 = new java.util.StringJoiner
            r1 = 2131953628(0x7f1307dc, float:1.9543732E38)
            java.lang.String r1 = r4.getString(r1)
            r0.<init>(r1)
            if (r5 == 0) goto L_0x0041
            boolean r1 = r5.fromWifiNetworkSuggestion
            if (r1 != 0) goto L_0x0016
            boolean r1 = r5.fromWifiNetworkSpecifier
            if (r1 == 0) goto L_0x0041
        L_0x0016:
            java.lang.String r5 = getSuggestionOrSpecifierLabel(r4, r5)
            boolean r1 = android.text.TextUtils.isEmpty(r5)
            if (r1 != 0) goto L_0x0041
            r1 = 0
            r2 = 1
            if (r7 != 0) goto L_0x0033
            r3 = 2131953614(0x7f1307ce, float:1.9543704E38)
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r2[r1] = r5
            java.lang.String r5 = r4.getString(r3, r2)
            r0.add(r5)
            goto L_0x0041
        L_0x0033:
            r3 = 2131953616(0x7f1307d0, float:1.9543708E38)
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r2[r1] = r5
            java.lang.String r5 = r4.getString(r3, r2)
            r0.add(r5)
        L_0x0041:
            if (r8 == 0) goto L_0x004d
            r5 = 2131953535(0x7f13077f, float:1.9543544E38)
            java.lang.String r5 = r4.getString(r5)
            r0.add(r5)
        L_0x004d:
            if (r6 != 0) goto L_0x0050
            goto L_0x009a
        L_0x0050:
            r5 = 17
            boolean r5 = r6.hasCapability(r5)
            if (r5 == 0) goto L_0x006c
            android.content.res.Resources r5 = r4.getResources()
            java.lang.String r6 = "network_available_sign_in"
            java.lang.String r8 = "string"
            java.lang.String r1 = "android"
            int r5 = r5.getIdentifier(r6, r8, r1)
            java.lang.String r5 = r4.getString(r5)
            goto L_0x009c
        L_0x006c:
            r5 = 24
            boolean r5 = r6.hasCapability(r5)
            if (r5 == 0) goto L_0x007c
            r5 = 2131953638(0x7f1307e6, float:1.9543753E38)
            java.lang.String r5 = r4.getString(r5)
            goto L_0x009c
        L_0x007c:
            r5 = 16
            boolean r5 = r6.hasCapability(r5)
            if (r5 != 0) goto L_0x009a
            boolean r5 = r6.isPrivateDnsBroken()
            if (r5 == 0) goto L_0x0092
            r5 = 2131953625(0x7f1307d9, float:1.9543726E38)
            java.lang.String r5 = r4.getString(r5)
            goto L_0x009c
        L_0x0092:
            r5 = 2131953633(0x7f1307e1, float:1.9543743E38)
            java.lang.String r5 = r4.getString(r5)
            goto L_0x009c
        L_0x009a:
            java.lang.String r5 = ""
        L_0x009c:
            boolean r6 = android.text.TextUtils.isEmpty(r5)
            if (r6 != 0) goto L_0x00a5
            r0.add(r5)
        L_0x00a5:
            int r5 = r0.length()
            if (r5 != 0) goto L_0x00c1
            if (r7 == 0) goto L_0x00c1
            android.content.res.Resources r4 = r4.getResources()
            r5 = 2130903176(0x7f030088, float:1.7413163E38)
            java.lang.String[] r4 = r4.getStringArray(r5)
            android.net.NetworkInfo$DetailedState r5 = android.net.NetworkInfo.DetailedState.CONNECTED
            int r5 = r5.ordinal()
            r4 = r4[r5]
            return r4
        L_0x00c1:
            java.lang.String r4 = r0.toString()
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.Utils.getConnectedDescription(android.content.Context, android.net.wifi.WifiConfiguration, android.net.NetworkCapabilities, boolean, boolean):java.lang.String");
    }

    public static String getConnectingDescription(Context context, NetworkInfo networkInfo) {
        NetworkInfo.DetailedState detailedState;
        if (context == null || networkInfo == null || (detailedState = networkInfo.getDetailedState()) == null) {
            return "";
        }
        String[] stringArray = context.getResources().getStringArray(2130903176);
        int ordinal = detailedState.ordinal();
        return ordinal >= stringArray.length ? "" : stringArray[ordinal];
    }

    /* JADX WARN: Code restructure failed: missing block: B:46:0x00cf, code lost:
        if (r4 != 9) goto L_0x00f7;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String getDisconnectedDescription(com.android.wifitrackerlib.WifiTrackerInjector r4, android.content.Context r5, android.net.wifi.WifiConfiguration r6, boolean r7, boolean r8) {
        /*
            Method dump skipped, instructions count: 346
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.Utils.getDisconnectedDescription(com.android.wifitrackerlib.WifiTrackerInjector, android.content.Context, android.net.wifi.WifiConfiguration, boolean, boolean):java.lang.String");
    }

    public static String getMeteredDescription(Context context, WifiEntry wifiEntry) {
        if (context == null || wifiEntry == null) {
            return "";
        }
        if (!wifiEntry.canSetMeteredChoice() && wifiEntry.getMeteredChoice() != 1) {
            return "";
        }
        if (wifiEntry.getMeteredChoice() == 1) {
            return context.getString(2131953642);
        }
        if (wifiEntry.getMeteredChoice() == 2) {
            return context.getString(2131953668);
        }
        return wifiEntry.isMetered() ? context.getString(2131953642) : "";
    }

    public static String getNetworkSelectionDescription(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        WifiConfiguration.NetworkSelectionStatus networkSelectionStatus = wifiConfiguration.getNetworkSelectionStatus();
        if (networkSelectionStatus.getNetworkSelectionStatus() != 0) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m(" (");
            m.append(networkSelectionStatus.getNetworkStatusString());
            sb.append(m.toString());
            if (networkSelectionStatus.getDisableTime() > 0) {
                StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m(" ");
                m2.append(DateUtils.formatElapsedTime((System.currentTimeMillis() - networkSelectionStatus.getDisableTime()) / 1000));
                sb.append(m2.toString());
            }
            sb.append(")");
        }
        int maxNetworkSelectionDisableReason = WifiConfiguration.NetworkSelectionStatus.getMaxNetworkSelectionDisableReason();
        for (int i = 0; i <= maxNetworkSelectionDisableReason; i++) {
            int disableReasonCounter = networkSelectionStatus.getDisableReasonCounter(i);
            if (disableReasonCounter != 0) {
                sb.append(" ");
                sb.append(WifiConfiguration.NetworkSelectionStatus.getNetworkSelectionDisableReasonString(i));
                sb.append("=");
                sb.append(disableReasonCounter);
            }
        }
        return sb.toString();
    }

    public static List<Integer> getSecurityTypesFromWifiConfiguration(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration.allowedKeyManagement.get(14)) {
            return Arrays.asList(8);
        }
        if (wifiConfiguration.allowedKeyManagement.get(13)) {
            return Arrays.asList(7);
        }
        if (wifiConfiguration.allowedKeyManagement.get(10)) {
            return Arrays.asList(5);
        }
        if (wifiConfiguration.allowedKeyManagement.get(9)) {
            return Arrays.asList(6);
        }
        if (wifiConfiguration.allowedKeyManagement.get(8)) {
            return Arrays.asList(4);
        }
        if (wifiConfiguration.allowedKeyManagement.get(4)) {
            return Arrays.asList(2);
        }
        if (wifiConfiguration.allowedKeyManagement.get(2)) {
            if (!wifiConfiguration.requirePmf || wifiConfiguration.allowedPairwiseCiphers.get(1) || !wifiConfiguration.allowedProtocols.get(1)) {
                return Arrays.asList(3, 9);
            }
            return Arrays.asList(9);
        } else if (wifiConfiguration.allowedKeyManagement.get(1)) {
            return Arrays.asList(2);
        } else {
            if (wifiConfiguration.allowedKeyManagement.get(0) && wifiConfiguration.wepKeys != null) {
                int i = 0;
                while (true) {
                    String[] strArr = wifiConfiguration.wepKeys;
                    if (i >= strArr.length) {
                        break;
                    } else if (strArr[i] != null) {
                        return Arrays.asList(1);
                    } else {
                        i++;
                    }
                }
            }
            return Arrays.asList(0);
        }
    }

    public static String getSuggestionOrSpecifierLabel(Context context, WifiConfiguration wifiConfiguration) {
        int i;
        TelephonyManager telephonyManager;
        TelephonyManager createForSubscriptionId;
        CharSequence simCarrierIdName;
        SubscriptionManager subscriptionManager;
        List<SubscriptionInfo> activeSubscriptionInfoList;
        String str = "";
        if (context == null || wifiConfiguration == null) {
            return str;
        }
        if (wifiConfiguration.carrierId != -1 && (subscriptionManager = (SubscriptionManager) context.getSystemService("telephony_subscription_service")) != null && (activeSubscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList()) != null && !activeSubscriptionInfoList.isEmpty()) {
            int defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
            i = -1;
            for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
                if (subscriptionInfo.getCarrierId() == wifiConfiguration.carrierId && (i = subscriptionInfo.getSubscriptionId()) == defaultDataSubscriptionId) {
                    break;
                }
            }
        } else {
            i = -1;
        }
        String str2 = null;
        if (!(i == -1 || (telephonyManager = (TelephonyManager) context.getSystemService("phone")) == null || (createForSubscriptionId = telephonyManager.createForSubscriptionId(i)) == null || (simCarrierIdName = createForSubscriptionId.getSimCarrierIdName()) == null)) {
            str2 = simCarrierIdName.toString();
        }
        if (!TextUtils.isEmpty(str2)) {
            return str2;
        }
        try {
            str = context.getPackageManager().getApplicationInfo(wifiConfiguration.creatorName, 0).loadLabel(context.getPackageManager()).toString();
        } catch (PackageManager.NameNotFoundException unused) {
        }
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        return wifiConfiguration.creatorName;
    }

    public static String getVerboseLoggingDescription(WifiEntry wifiEntry) {
        String stringJoiner;
        if (!BaseWifiTracker.sVerboseLogging || wifiEntry == null) {
            return "";
        }
        StringJoiner stringJoiner2 = new StringJoiner(" ");
        synchronized (wifiEntry) {
            StringJoiner stringJoiner3 = new StringJoiner(" ");
            if (wifiEntry.getConnectedState() == 2 && wifiEntry.mWifiInfo != null) {
                stringJoiner3.add("f = " + wifiEntry.mWifiInfo.getFrequency());
                String bssid = wifiEntry.mWifiInfo.getBSSID();
                if (bssid != null) {
                    stringJoiner3.add(bssid);
                }
                stringJoiner3.add("standard = " + wifiEntry.mWifiInfo.getWifiStandard());
                stringJoiner3.add("rssi = " + wifiEntry.mWifiInfo.getRssi());
                stringJoiner3.add("score = " + wifiEntry.mWifiInfo.getScore());
                stringJoiner3.add(String.format(" tx=%.1f,", Double.valueOf(wifiEntry.mWifiInfo.getSuccessfulTxPacketsPerSecond())));
                stringJoiner3.add(String.format("%.1f,", Double.valueOf(wifiEntry.mWifiInfo.getRetriedTxPacketsPerSecond())));
                stringJoiner3.add(String.format("%.1f ", Double.valueOf(wifiEntry.mWifiInfo.getLostTxPacketsPerSecond())));
                stringJoiner3.add(String.format("rx=%.1f", Double.valueOf(wifiEntry.mWifiInfo.getSuccessfulRxPacketsPerSecond())));
            }
            stringJoiner = stringJoiner3.toString();
        }
        if (!TextUtils.isEmpty(stringJoiner)) {
            stringJoiner2.add(stringJoiner);
        }
        StringBuilder sb = new StringBuilder();
        if (wifiEntry.getConnectedState() == 2) {
            sb.append("isValidated:");
            sb.append(wifiEntry.mIsValidated);
            sb.append(", isDefaultNetwork:");
            sb.append(wifiEntry.mIsDefaultNetwork);
            sb.append(", isLowQuality:");
            sb.append(wifiEntry.mIsLowQuality);
        }
        String sb2 = sb.toString();
        if (!TextUtils.isEmpty(sb2)) {
            stringJoiner2.add(sb2);
        }
        String scanResultDescription = wifiEntry.getScanResultDescription();
        if (!TextUtils.isEmpty(scanResultDescription)) {
            stringJoiner2.add(scanResultDescription);
        }
        String networkSelectionDescription = wifiEntry.getNetworkSelectionDescription();
        if (!TextUtils.isEmpty(networkSelectionDescription)) {
            stringJoiner2.add(networkSelectionDescription);
        }
        return stringJoiner2.toString();
    }

    public static ScanResult getBestScanResultByLevel(List<ScanResult> list) {
        if (list.isEmpty()) {
            return null;
        }
        return (ScanResult) Collections.max(list, Comparator.comparingInt(Utils$$ExternalSyntheticLambda1.INSTANCE));
    }

    public static InetAddress getNetworkPart(InetAddress inetAddress, int i) {
        byte[] address = inetAddress.getAddress();
        if (i < 0 || i > address.length * 8) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("IP address with ");
            m.append(address.length);
            m.append(" bytes has invalid prefix length ");
            m.append(i);
            throw new RuntimeException(m.toString());
        }
        int i2 = i / 8;
        byte b = (byte) (255 << (8 - (i % 8)));
        if (i2 < address.length) {
            address[i2] = (byte) (b & address[i2]);
        }
        while (true) {
            i2++;
            if (i2 < address.length) {
                address[i2] = 0;
            } else {
                try {
                    return InetAddress.getByAddress(address);
                } catch (UnknownHostException e) {
                    StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("getNetworkPart error - ");
                    m2.append(e.toString());
                    throw new RuntimeException(m2.toString());
                }
            }
        }
    }

    public static int getSingleSecurityTypeFromMultipleSecurityTypes(List<Integer> list) {
        if (list.size() == 1) {
            return list.get(0).intValue();
        }
        if (list.size() != 2) {
            return -1;
        }
        if (list.contains(0)) {
            return 0;
        }
        if (list.contains(2)) {
            return 2;
        }
        if (list.contains(3)) {
            return 3;
        }
        return -1;
    }
}
