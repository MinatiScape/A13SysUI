package com.android.systemui.recents;

import com.android.systemui.recents.OverviewProxyService;
import com.android.wm.shell.legacysplitscreen.DividerView;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import java.util.Objects;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class OverviewProxyService$1$$ExternalSyntheticLambda17 implements Function {
    public static final /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda17 INSTANCE = new OverviewProxyService$1$$ExternalSyntheticLambda17();

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        int i = OverviewProxyService.AnonymousClass1.$r8$clinit;
        DividerView dividerView = ((LegacySplitScreen) obj).getDividerView();
        Objects.requireNonNull(dividerView);
        dividerView.mOtherTaskRect.set(dividerView.mSplitLayout.mSecondary);
        return dividerView.mOtherTaskRect;
    }
}
