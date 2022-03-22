package com.android.systemui.tuner;

import com.android.systemui.Dependency;
import com.android.systemui.tuner.TunerService;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NavBarTuner$$ExternalSyntheticLambda9 implements Consumer {
    public static final /* synthetic */ NavBarTuner$$ExternalSyntheticLambda9 INSTANCE = new NavBarTuner$$ExternalSyntheticLambda9();

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        int[][] iArr = NavBarTuner.ICONS;
        ((TunerService) Dependency.get(TunerService.class)).removeTunable((TunerService.Tunable) obj);
    }
}
