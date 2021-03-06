package com.android.settingslib.mobile;

import androidx.leanback.R$layout;
import com.android.settingslib.SignalIcon$MobileIconGroup;
import java.util.HashMap;
/* loaded from: classes.dex */
public final class TelephonyIcons {
    public static final SignalIcon$MobileIconGroup CARRIER_MERGED_WIFI;
    public static final SignalIcon$MobileIconGroup CARRIER_NETWORK_CHANGE;
    public static final SignalIcon$MobileIconGroup DATA_DISABLED;
    public static final SignalIcon$MobileIconGroup E;
    public static final SignalIcon$MobileIconGroup FOUR_G;
    public static final SignalIcon$MobileIconGroup FOUR_G_PLUS;
    public static final SignalIcon$MobileIconGroup G;
    public static final SignalIcon$MobileIconGroup H;
    public static final SignalIcon$MobileIconGroup H_PLUS;
    public static final HashMap ICON_NAME_TO_ICON;
    public static final SignalIcon$MobileIconGroup LTE;
    public static final SignalIcon$MobileIconGroup LTE_CA_5G_E;
    public static final SignalIcon$MobileIconGroup LTE_PLUS;
    public static final SignalIcon$MobileIconGroup NOT_DEFAULT_DATA;
    public static final SignalIcon$MobileIconGroup NR_5G;
    public static final SignalIcon$MobileIconGroup NR_5G_PLUS;
    public static final SignalIcon$MobileIconGroup ONE_X;
    public static final SignalIcon$MobileIconGroup THREE_G;
    public static final SignalIcon$MobileIconGroup UNKNOWN;
    public static final SignalIcon$MobileIconGroup WFC;
    public static final int[] WIFI_CALL_STRENGTH_ICONS = {2131232333, 2131232334, 2131232335, 2131232336, 2131232337};
    public static final int[] MOBILE_CALL_STRENGTH_ICONS = {2131232055, 2131232056, 2131232057, 2131232058, 2131232059};

    static {
        int[] iArr = R$layout.PHONE_SIGNAL_STRENGTH;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup = new SignalIcon$MobileIconGroup("CARRIER_NETWORK_CHANGE", iArr, iArr[0], 2131952095, 0);
        CARRIER_NETWORK_CHANGE = signalIcon$MobileIconGroup;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup2 = new SignalIcon$MobileIconGroup("3G", iArr, iArr[0], 2131952223, 2131231731);
        THREE_G = signalIcon$MobileIconGroup2;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup3 = new SignalIcon$MobileIconGroup("WFC", iArr, iArr[0], 0, 0);
        WFC = signalIcon$MobileIconGroup3;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup4 = new SignalIcon$MobileIconGroup("Unknown", iArr, iArr[0], 0, 0);
        UNKNOWN = signalIcon$MobileIconGroup4;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup5 = new SignalIcon$MobileIconGroup("E", iArr, iArr[0], 2131952231, 2131231944);
        E = signalIcon$MobileIconGroup5;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup6 = new SignalIcon$MobileIconGroup("1X", iArr, iArr[0], 2131952230, 2131231730);
        ONE_X = signalIcon$MobileIconGroup6;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup7 = new SignalIcon$MobileIconGroup("G", iArr, iArr[0], 2131952232, 2131231957);
        G = signalIcon$MobileIconGroup7;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup8 = new SignalIcon$MobileIconGroup("H", iArr, iArr[0], 2131952221, 2131231999);
        H = signalIcon$MobileIconGroup8;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup9 = new SignalIcon$MobileIconGroup("H+", iArr, iArr[0], 2131952222, 2131232000);
        H_PLUS = signalIcon$MobileIconGroup9;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup10 = new SignalIcon$MobileIconGroup("4G", iArr, iArr[0], 2131952224, 2131231732);
        FOUR_G = signalIcon$MobileIconGroup10;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup11 = new SignalIcon$MobileIconGroup("4G+", iArr, iArr[0], 2131952225, 2131231733);
        FOUR_G_PLUS = signalIcon$MobileIconGroup11;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup12 = new SignalIcon$MobileIconGroup("LTE", iArr, iArr[0], 2131952233, 2131232031);
        LTE = signalIcon$MobileIconGroup12;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup13 = new SignalIcon$MobileIconGroup("LTE+", iArr, iArr[0], 2131952234, 2131232032);
        LTE_PLUS = signalIcon$MobileIconGroup13;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup14 = new SignalIcon$MobileIconGroup("5Ge", iArr, iArr[0], 2131952228, 2131231734);
        LTE_CA_5G_E = signalIcon$MobileIconGroup14;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup15 = new SignalIcon$MobileIconGroup("5G", iArr, iArr[0], 2131952226, 2131231735);
        NR_5G = signalIcon$MobileIconGroup15;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup16 = new SignalIcon$MobileIconGroup("5G_PLUS", iArr, iArr[0], 2131952227, 2131231736);
        NR_5G_PLUS = signalIcon$MobileIconGroup16;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup17 = new SignalIcon$MobileIconGroup("DataDisabled", iArr, iArr[0], 2131952099, 0);
        DATA_DISABLED = signalIcon$MobileIconGroup17;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup18 = new SignalIcon$MobileIconGroup("NotDefaultData", iArr, iArr[0], 2131952876, 0);
        NOT_DEFAULT_DATA = signalIcon$MobileIconGroup18;
        CARRIER_MERGED_WIFI = new SignalIcon$MobileIconGroup("CWF", iArr, iArr[0], 2131952229, 2131231777);
        HashMap hashMap = new HashMap();
        ICON_NAME_TO_ICON = hashMap;
        hashMap.put("carrier_network_change", signalIcon$MobileIconGroup);
        hashMap.put("3g", signalIcon$MobileIconGroup2);
        hashMap.put("wfc", signalIcon$MobileIconGroup3);
        hashMap.put("unknown", signalIcon$MobileIconGroup4);
        hashMap.put("e", signalIcon$MobileIconGroup5);
        hashMap.put("1x", signalIcon$MobileIconGroup6);
        hashMap.put("g", signalIcon$MobileIconGroup7);
        hashMap.put("h", signalIcon$MobileIconGroup8);
        hashMap.put("h+", signalIcon$MobileIconGroup9);
        hashMap.put("4g", signalIcon$MobileIconGroup10);
        hashMap.put("4g+", signalIcon$MobileIconGroup11);
        hashMap.put("5ge", signalIcon$MobileIconGroup14);
        hashMap.put("lte", signalIcon$MobileIconGroup12);
        hashMap.put("lte+", signalIcon$MobileIconGroup13);
        hashMap.put("5g", signalIcon$MobileIconGroup15);
        hashMap.put("5g_plus", signalIcon$MobileIconGroup16);
        hashMap.put("datadisable", signalIcon$MobileIconGroup17);
        hashMap.put("notdefaultdata", signalIcon$MobileIconGroup18);
    }
}
