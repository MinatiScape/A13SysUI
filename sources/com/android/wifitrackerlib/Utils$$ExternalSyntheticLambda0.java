package com.android.wifitrackerlib;

import android.telephony.SubscriptionInfo;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class Utils$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ int f$0;

    public /* synthetic */ Utils$$ExternalSyntheticLambda0(int i, int i2) {
        this.$r8$classId = i2;
        this.f$0 = i;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                if (((SubscriptionInfo) obj).getCarrierId() == this.f$0) {
                    return true;
                }
                return false;
            case 1:
                if (((Integer) obj).intValue() == this.f$0) {
                    return true;
                }
                return false;
            default:
                if (((Integer) obj).intValue() == this.f$0) {
                    return true;
                }
                return false;
        }
    }
}
