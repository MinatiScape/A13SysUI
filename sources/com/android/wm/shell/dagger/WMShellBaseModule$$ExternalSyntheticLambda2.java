package com.android.wm.shell.dagger;

import com.android.systemui.statusbar.phone.StatusBar;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutController;
import java.util.Objects;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WMShellBaseModule$$ExternalSyntheticLambda2 implements Function {
    public final /* synthetic */ int $r8$classId;
    public static final /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda2 INSTANCE$1 = new WMShellBaseModule$$ExternalSyntheticLambda2(1);
    public static final /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda2 INSTANCE = new WMShellBaseModule$$ExternalSyntheticLambda2(0);

    public /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda2(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                HideDisplayCutoutController hideDisplayCutoutController = (HideDisplayCutoutController) obj;
                Objects.requireNonNull(hideDisplayCutoutController);
                return hideDisplayCutoutController.mImpl;
            default:
                return ((StatusBar) obj).getNavigationBarView();
        }
    }
}
