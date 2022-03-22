package com.android.settingslib.wifi;

import android.net.wifi.WifiConfiguration;
import com.android.systemui.dreams.complication.ComplicationId;
import java.util.Collection;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WifiTracker$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ WifiTracker$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                return ((AccessPoint) this.f$0).matches((WifiConfiguration) obj);
            default:
                return !((Collection) this.f$0).contains((ComplicationId) obj);
        }
    }
}
