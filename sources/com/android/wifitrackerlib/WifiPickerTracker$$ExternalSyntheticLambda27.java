package com.android.wifitrackerlib;

import com.android.systemui.statusbar.phone.StatusBarWindowCallback;
import java.util.Objects;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda27 implements Predicate {
    public final /* synthetic */ int $r8$classId;
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda27 INSTANCE$1 = new WifiPickerTracker$$ExternalSyntheticLambda27(1);
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda27 INSTANCE$2 = new WifiPickerTracker$$ExternalSyntheticLambda27(2);
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda27 INSTANCE = new WifiPickerTracker$$ExternalSyntheticLambda27(0);

    public /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda27(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                if (((WifiEntry) obj).getConnectedState() == 0) {
                    return true;
                }
                return false;
            case 1:
                return ((Boolean) obj).booleanValue();
            default:
                return Objects.nonNull((StatusBarWindowCallback) obj);
        }
    }
}
