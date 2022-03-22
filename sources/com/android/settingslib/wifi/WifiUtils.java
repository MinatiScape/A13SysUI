package com.android.settingslib.wifi;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.SystemClock;
import android.util.ArraySet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class WifiUtils {
    public static final int[] WIFI_PIE = {17302891, 17302892, 17302893, 17302894, 17302895};
    public static final int[] NO_INTERNET_WIFI_PIE = {2131232198, 2131232199, 2131232200, 2131232201, 2131232202};

    /* loaded from: classes.dex */
    public static class InternetIconInjector {
        public final Context mContext;

        public final Drawable getIcon(boolean z, int i) {
            int i2;
            Context context = this.mContext;
            if (i >= 0) {
                int[] iArr = WifiUtils.WIFI_PIE;
                if (i < 5) {
                    if (z) {
                        i2 = WifiUtils.NO_INTERNET_WIFI_PIE[i];
                    } else {
                        i2 = iArr[i];
                    }
                    return context.getDrawable(i2);
                }
            }
            throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("No Wifi icon found for level: ", i));
        }

        public InternetIconInjector(Context context) {
            this.mContext = context;
        }
    }

    public static String getVisibilityStatus(AccessPoint accessPoint) {
        String str;
        Objects.requireNonNull(accessPoint);
        WifiInfo wifiInfo = accessPoint.mInfo;
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        StringBuilder sb4 = new StringBuilder();
        int i = 0;
        if (!accessPoint.isActive() || wifiInfo == null) {
            str = null;
        } else {
            str = wifiInfo.getBSSID();
            if (str != null) {
                sb.append(" ");
                sb.append(str);
            }
            sb.append(" standard = ");
            sb.append(wifiInfo.getWifiStandard());
            sb.append(" rssi=");
            sb.append(wifiInfo.getRssi());
            sb.append(" ");
            sb.append(" score=");
            sb.append(wifiInfo.getScore());
            if (accessPoint.mSpeed != 0) {
                sb.append(" speed=");
                sb.append(AccessPoint.getSpeedLabel(accessPoint.mContext, accessPoint.mSpeed));
            }
            sb.append(String.format(" tx=%.1f,", Double.valueOf(wifiInfo.getSuccessfulTxPacketsPerSecond())));
            sb.append(String.format("%.1f,", Double.valueOf(wifiInfo.getRetriedTxPacketsPerSecond())));
            sb.append(String.format("%.1f ", Double.valueOf(wifiInfo.getLostTxPacketsPerSecond())));
            sb.append(String.format("rx=%.1f", Double.valueOf(wifiInfo.getSuccessfulRxPacketsPerSecond())));
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        ArraySet arraySet = new ArraySet();
        synchronized (accessPoint.mLock) {
            arraySet.addAll((Collection) accessPoint.mScanResults);
            arraySet.addAll((Collection) accessPoint.mExtraScanResults);
        }
        Iterator it = arraySet.iterator();
        int i2 = 0;
        int i3 = -127;
        int i4 = -127;
        int i5 = -127;
        int i6 = 0;
        while (it.hasNext()) {
            ScanResult scanResult = (ScanResult) it.next();
            if (scanResult == null) {
                sb = sb;
            } else {
                int i7 = scanResult.frequency;
                if (i7 >= 4900 && i7 <= 5900) {
                    i6++;
                    int i8 = scanResult.level;
                    if (i8 > i4) {
                        i4 = i8;
                    }
                    if (i6 <= 4) {
                        sb3.append(verboseScanResultSummary(accessPoint, scanResult, str, elapsedRealtime));
                    }
                } else if (i7 >= 2400 && i7 <= 2500) {
                    i++;
                    int i9 = scanResult.level;
                    if (i9 > i3) {
                        i3 = i9;
                    }
                    if (i <= 4) {
                        sb2.append(verboseScanResultSummary(accessPoint, scanResult, str, elapsedRealtime));
                    }
                } else if (i7 >= 58320 && i7 <= 70200) {
                    i2++;
                    int i10 = scanResult.level;
                    if (i10 > i5) {
                        i5 = i10;
                    }
                    if (i2 <= 4) {
                        sb4.append(verboseScanResultSummary(accessPoint, scanResult, str, elapsedRealtime));
                    }
                }
                sb = sb;
                it = it;
            }
        }
        sb.append(" [");
        if (i > 0) {
            sb.append("(");
            sb.append(i);
            sb.append(")");
            if (i > 4) {
                sb.append("max=");
                sb.append(i3);
                sb.append(",");
            }
            sb.append(sb2.toString());
        }
        sb.append(";");
        if (i6 > 0) {
            sb.append("(");
            sb.append(i6);
            sb.append(")");
            if (i6 > 4) {
                sb.append("max=");
                sb.append(i4);
                sb.append(",");
            }
            sb.append(sb3.toString());
        }
        sb.append(";");
        if (i2 > 0) {
            sb.append("(");
            sb.append(i2);
            sb.append(")");
            if (i2 > 4) {
                sb.append("max=");
                sb.append(i5);
                sb.append(",");
            }
            sb.append(sb4.toString());
        }
        sb.append("]");
        return sb.toString();
    }

    public static String verboseScanResultSummary(AccessPoint accessPoint, ScanResult scanResult, String str, long j) {
        int i;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m(" \n{");
        m.append(scanResult.BSSID);
        if (scanResult.BSSID.equals(str)) {
            m.append("*");
        }
        m.append("=");
        m.append(scanResult.frequency);
        m.append(",");
        m.append(scanResult.level);
        Objects.requireNonNull(accessPoint);
        TimestampedScoredNetwork timestampedScoredNetwork = (TimestampedScoredNetwork) accessPoint.mScoredNetworkCache.get(scanResult.BSSID);
        if (timestampedScoredNetwork == null) {
            i = 0;
        } else {
            i = timestampedScoredNetwork.mScore.calculateBadge(scanResult.level);
        }
        if (i != 0) {
            m.append(",");
            m.append(AccessPoint.getSpeedLabel(accessPoint.mContext, i));
        }
        m.append(",");
        m.append(((int) (j - (scanResult.timestamp / 1000))) / 1000);
        m.append("s");
        m.append("}");
        return m.toString();
    }
}
