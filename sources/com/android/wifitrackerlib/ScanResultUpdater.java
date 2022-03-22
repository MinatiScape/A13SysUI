package com.android.wifitrackerlib;

import android.net.wifi.ScanResult;
import android.util.Pair;
import com.android.wm.shell.bubbles.BubbleData$$ExternalSyntheticLambda6;
import java.time.Clock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/* loaded from: classes.dex */
public final class ScanResultUpdater {
    public final Clock mClock;
    public final long mMaxScanAgeMillis;
    public HashMap mScanResultsBySsidAndBssid = new HashMap();
    public final Object mLock = new Object();

    public final ArrayList getScanResults(long j) throws IllegalArgumentException {
        ArrayList arrayList;
        if (j <= this.mMaxScanAgeMillis) {
            synchronized (this.mLock) {
                arrayList = new ArrayList();
                for (ScanResult scanResult : this.mScanResultsBySsidAndBssid.values()) {
                    if (this.mClock.millis() - (scanResult.timestamp / 1000) <= j) {
                        arrayList.add(scanResult);
                    }
                }
            }
            return arrayList;
        }
        throw new IllegalArgumentException("maxScanAgeMillis argument cannot be greater than mMaxScanAgeMillis!");
    }

    public final void update(List<ScanResult> list) {
        synchronized (this.mLock) {
            synchronized (this.mLock) {
                this.mScanResultsBySsidAndBssid.entrySet().removeIf(new BubbleData$$ExternalSyntheticLambda6(this, 1));
            }
            for (ScanResult scanResult : list) {
                Pair pair = new Pair(scanResult.SSID, scanResult.BSSID);
                ScanResult scanResult2 = (ScanResult) this.mScanResultsBySsidAndBssid.get(pair);
                if (scanResult2 == null || scanResult2.timestamp < scanResult.timestamp) {
                    this.mScanResultsBySsidAndBssid.put(pair, scanResult);
                }
            }
        }
    }

    public ScanResultUpdater(Clock clock, long j) {
        this.mMaxScanAgeMillis = j;
        this.mClock = clock;
    }
}
