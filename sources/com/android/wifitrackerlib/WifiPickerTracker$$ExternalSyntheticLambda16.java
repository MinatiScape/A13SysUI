package com.android.wifitrackerlib;

import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda16 implements Predicate {
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda16 INSTANCE = new WifiPickerTracker$$ExternalSyntheticLambda16();

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        if (((PasspointWifiEntry) obj).getConnectedState() == 0) {
            return true;
        }
        return false;
    }
}
