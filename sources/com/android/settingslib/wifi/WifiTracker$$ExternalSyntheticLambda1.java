package com.android.settingslib.wifi;

import android.net.wifi.WifiConfiguration;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WifiTracker$$ExternalSyntheticLambda1 implements Predicate {
    public static final /* synthetic */ WifiTracker$$ExternalSyntheticLambda1 INSTANCE = new WifiTracker$$ExternalSyntheticLambda1();

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        int security = AccessPoint.getSecurity((WifiConfiguration) obj);
        if (security == 5 || security == 4) {
            return true;
        }
        return false;
    }
}
