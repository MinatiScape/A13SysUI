package com.android.wm.shell.dagger;

import com.android.systemui.classifier.HistoryTracker;
import com.android.wm.shell.apppairs.AppPairsController;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import java.util.Objects;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WMShellBaseModule$$ExternalSyntheticLambda4 implements Function {
    public final /* synthetic */ int $r8$classId;
    public static final /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda4 INSTANCE$1 = new WMShellBaseModule$$ExternalSyntheticLambda4(1);
    public static final /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda4 INSTANCE$2 = new WMShellBaseModule$$ExternalSyntheticLambda4(2);
    public static final /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda4 INSTANCE = new WMShellBaseModule$$ExternalSyntheticLambda4(0);

    public /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda4(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                AppPairsController appPairsController = (AppPairsController) obj;
                Objects.requireNonNull(appPairsController);
                return appPairsController.mImpl;
            case 1:
                HistoryTracker.CombinedResult combinedResult = (HistoryTracker.CombinedResult) obj;
                Objects.requireNonNull(combinedResult);
                return Double.valueOf(combinedResult.mScore);
            default:
                return Boolean.valueOf(((LegacySplitScreen) obj).getDividerView().getSnapAlgorithm().isSplitScreenFeasible());
        }
    }
}
