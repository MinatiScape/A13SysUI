package com.android.wifitrackerlib;

import android.net.wifi.WifiConfiguration;
import com.android.systemui.qs.QSPanelControllerBase;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda11 implements Function {
    public final /* synthetic */ int $r8$classId;
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda11 INSTANCE$1 = new WifiPickerTracker$$ExternalSyntheticLambda11(1);
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda11 INSTANCE$2 = new WifiPickerTracker$$ExternalSyntheticLambda11(2);
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda11 INSTANCE = new WifiPickerTracker$$ExternalSyntheticLambda11(0);

    public /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda11(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                return Integer.valueOf(((WifiConfiguration) obj).networkId);
            case 1:
                return Integer.valueOf(Integer.parseInt((String) obj));
            default:
                return ((QSPanelControllerBase.TileRecord) obj).tile.getTileSpec();
        }
    }
}
