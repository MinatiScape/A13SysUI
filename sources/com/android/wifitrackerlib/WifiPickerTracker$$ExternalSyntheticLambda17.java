package com.android.wifitrackerlib;

import android.net.wifi.ScanResult;
import android.text.TextUtils;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda17 implements Predicate {
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda17 INSTANCE = new WifiPickerTracker$$ExternalSyntheticLambda17();

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return !TextUtils.isEmpty(((ScanResult) obj).SSID);
    }
}
