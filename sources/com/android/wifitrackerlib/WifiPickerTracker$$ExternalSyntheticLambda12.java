package com.android.wifitrackerlib;

import android.graphics.drawable.ColorDrawable;
import android.util.ArrayMap;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.complication.ComplicationHostViewController;
import com.android.systemui.dreams.complication.ComplicationId;
import com.android.systemui.dreams.complication.ComplicationViewModel;
import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda12 implements Predicate {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda12(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        boolean add;
        switch (this.$r8$classId) {
            case 0:
                WifiPickerTracker wifiPickerTracker = (WifiPickerTracker) this.f$0;
                StandardWifiEntry standardWifiEntry = (StandardWifiEntry) obj;
                Objects.requireNonNull(wifiPickerTracker);
                ArrayMap arrayMap = wifiPickerTracker.mSuggestedConfigCache;
                Objects.requireNonNull(standardWifiEntry);
                standardWifiEntry.updateConfig((List) arrayMap.get(standardWifiEntry.mKey));
                add = standardWifiEntry.isSuggestion();
                break;
            case 1:
                ComplicationHostViewController complicationHostViewController = (ComplicationHostViewController) this.f$0;
                ComplicationViewModel complicationViewModel = (ComplicationViewModel) obj;
                Objects.requireNonNull(complicationHostViewController);
                HashMap<ComplicationId, Complication.ViewHolder> hashMap = complicationHostViewController.mComplications;
                Objects.requireNonNull(complicationViewModel);
                add = hashMap.containsKey(complicationViewModel.mId);
                break;
            default:
                ColorDrawable colorDrawable = InternetDialogController.EMPTY_DRAWABLE;
                add = ((Set) this.f$0).add(((InternetDialogController.C1DisplayInfo) obj).uniqueName);
                break;
        }
        return !add;
    }
}
