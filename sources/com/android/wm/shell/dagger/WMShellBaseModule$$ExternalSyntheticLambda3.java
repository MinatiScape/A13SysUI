package com.android.wm.shell.dagger;

import com.android.systemui.accessibility.floatingmenu.AnnotationLinkSpan;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.wm.shell.bubbles.BubbleController;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WMShellBaseModule$$ExternalSyntheticLambda3 implements Function {
    public final /* synthetic */ int $r8$classId;
    public static final /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda3 INSTANCE$1 = new WMShellBaseModule$$ExternalSyntheticLambda3(1);
    public static final /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda3 INSTANCE$2 = new WMShellBaseModule$$ExternalSyntheticLambda3(2);
    public static final /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda3 INSTANCE = new WMShellBaseModule$$ExternalSyntheticLambda3(0);

    public /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda3(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                return ((BubbleController) obj).asBubbles();
            case 1:
                int i = AnnotationLinkSpan.$r8$clinit;
                return ((AnnotationLinkSpan.LinkInfo) obj).mListener;
            default:
                return Boolean.valueOf(((StatusBar) obj).toggleSplitScreenMode(271, 286));
        }
    }
}
