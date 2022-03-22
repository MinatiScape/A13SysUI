package com.android.wm.shell.dagger;

import com.android.systemui.statusbar.phone.StatusBar;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import java.util.Objects;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WMShellBaseModule$$ExternalSyntheticLambda5 implements Function {
    public final /* synthetic */ int $r8$classId;
    public static final /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda5 INSTANCE$1 = new WMShellBaseModule$$ExternalSyntheticLambda5(1);
    public static final /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda5 INSTANCE$2 = new WMShellBaseModule$$ExternalSyntheticLambda5(2);
    public static final /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda5 INSTANCE = new WMShellBaseModule$$ExternalSyntheticLambda5(0);

    public /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda5(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                LegacySplitScreenController legacySplitScreenController = (LegacySplitScreenController) obj;
                Objects.requireNonNull(legacySplitScreenController);
                return legacySplitScreenController.mImpl;
            case 1:
                return Boolean.valueOf(((StatusBar) obj).isKeyguardShowing());
            default:
                return Boolean.valueOf(((LegacySplitScreen) obj).isDividerVisible());
        }
    }
}
