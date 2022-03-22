package com.android.wifitrackerlib;

import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda21 implements Predicate {
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda21 INSTANCE = new WifiPickerTracker$$ExternalSyntheticLambda21();

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        boolean z;
        OsuWifiEntry osuWifiEntry = (OsuWifiEntry) obj;
        if (osuWifiEntry.getConnectedState() == 0) {
            synchronized (osuWifiEntry) {
                z = osuWifiEntry.mIsAlreadyProvisioned;
            }
            if (!z) {
                return true;
            }
        }
        return false;
    }
}
