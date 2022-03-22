package com.android.systemui.people.widget;

import android.net.wifi.WifiConfiguration;
import com.android.wifitrackerlib.StandardNetworkDetailsTracker;
import com.android.wifitrackerlib.StandardWifiEntry;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PeopleBackupHelper$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ PeopleBackupHelper$$ExternalSyntheticLambda1(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                return ((List) this.f$0).contains((String) obj);
            default:
                StandardNetworkDetailsTracker standardNetworkDetailsTracker = (StandardNetworkDetailsTracker) this.f$0;
                WifiConfiguration wifiConfiguration = (WifiConfiguration) obj;
                Objects.requireNonNull(standardNetworkDetailsTracker);
                if (wifiConfiguration.isPasspoint()) {
                    return false;
                }
                StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey = standardNetworkDetailsTracker.mKey;
                Objects.requireNonNull(standardWifiEntryKey);
                return standardWifiEntryKey.equals(new StandardWifiEntry.StandardWifiEntryKey(wifiConfiguration, standardWifiEntryKey.mIsTargetingNewNetworks));
        }
    }
}
