package com.android.wifitrackerlib;

import android.net.wifi.WifiConfiguration;
import android.view.View;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class SavedNetworkTracker$$ExternalSyntheticLambda3 implements Predicate {
    public final /* synthetic */ int $r8$classId;
    public static final /* synthetic */ SavedNetworkTracker$$ExternalSyntheticLambda3 INSTANCE$1 = new SavedNetworkTracker$$ExternalSyntheticLambda3(1);
    public static final /* synthetic */ SavedNetworkTracker$$ExternalSyntheticLambda3 INSTANCE = new SavedNetworkTracker$$ExternalSyntheticLambda3(0);

    public /* synthetic */ SavedNetworkTracker$$ExternalSyntheticLambda3(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                return !((WifiConfiguration) obj).carrierMerged;
            default:
                return ((View) obj).isAttachedToWindow();
        }
    }
}
