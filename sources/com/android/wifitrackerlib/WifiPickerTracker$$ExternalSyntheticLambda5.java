package com.android.wifitrackerlib;

import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor;
import com.android.wifitrackerlib.StandardWifiEntry;
import java.util.Objects;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda5 implements Function {
    public final /* synthetic */ int $r8$classId;
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda5 INSTANCE$1 = new WifiPickerTracker$$ExternalSyntheticLambda5(1);
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda5 INSTANCE = new WifiPickerTracker$$ExternalSyntheticLambda5(0);

    public /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda5(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                StandardWifiEntry standardWifiEntry = (StandardWifiEntry) obj;
                Objects.requireNonNull(standardWifiEntry);
                StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey = standardWifiEntry.mKey;
                Objects.requireNonNull(standardWifiEntryKey);
                return standardWifiEntryKey.mScanResultKey;
            default:
                DreamOverlayTouchMonitor.TouchSessionImpl touchSessionImpl = (DreamOverlayTouchMonitor.TouchSessionImpl) obj;
                Objects.requireNonNull(touchSessionImpl);
                return touchSessionImpl.mGestureListeners;
        }
    }
}
