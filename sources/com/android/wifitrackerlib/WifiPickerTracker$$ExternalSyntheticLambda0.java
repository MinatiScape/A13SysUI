package com.android.wifitrackerlib;

import android.util.ArrayMap;
import com.android.systemui.dreams.complication.ComplicationHostViewController;
import com.android.systemui.dreams.complication.ComplicationId;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                WifiPickerTracker wifiPickerTracker = (WifiPickerTracker) this.f$0;
                StandardWifiEntry standardWifiEntry = (StandardWifiEntry) obj;
                Objects.requireNonNull(wifiPickerTracker);
                ArrayMap arrayMap = wifiPickerTracker.mStandardWifiConfigCache;
                Objects.requireNonNull(standardWifiEntry);
                standardWifiEntry.updateConfig((List) arrayMap.get(standardWifiEntry.mKey));
                return;
            case 1:
                ComplicationHostViewController complicationHostViewController = (ComplicationHostViewController) this.f$0;
                ComplicationId complicationId = (ComplicationId) obj;
                Objects.requireNonNull(complicationHostViewController);
                complicationHostViewController.mLayoutEngine.removeComplication(complicationId);
                complicationHostViewController.mComplications.remove(complicationId);
                return;
            default:
                NavigationBarView navigationBarView = (NavigationBarView) this.f$0;
                Objects.requireNonNull(navigationBarView);
                ((LegacySplitScreen) obj).registerInSplitScreenListener(navigationBarView.mDockedListener);
                return;
        }
    }
}
