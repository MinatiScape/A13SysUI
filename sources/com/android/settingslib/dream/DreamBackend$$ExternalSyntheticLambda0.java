package com.android.settingslib.dream;

import android.net.wifi.ScanResult;
import com.android.wifitrackerlib.StandardWifiEntry;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class DreamBackend$$ExternalSyntheticLambda0 implements Function {
    public static final /* synthetic */ DreamBackend$$ExternalSyntheticLambda0 INSTANCE = new DreamBackend$$ExternalSyntheticLambda0(0);
    public static final /* synthetic */ DreamBackend$$ExternalSyntheticLambda0 INSTANCE$1 = new DreamBackend$$ExternalSyntheticLambda0(1);
    public final /* synthetic */ int $r8$classId;

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                return Integer.valueOf(Integer.parseInt((String) obj));
            default:
                return new StandardWifiEntry.ScanResultKey((ScanResult) obj);
        }
    }

    public /* synthetic */ DreamBackend$$ExternalSyntheticLambda0(int i) {
        this.$r8$classId = i;
    }
}
