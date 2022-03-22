package com.android.systemui.recents;

import com.android.systemui.statusbar.phone.StatusBar;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class OverviewProxyRecentsImpl$$ExternalSyntheticLambda0 implements Function {
    public static final /* synthetic */ OverviewProxyRecentsImpl$$ExternalSyntheticLambda0 INSTANCE = new OverviewProxyRecentsImpl$$ExternalSyntheticLambda0();

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return Boolean.valueOf(((StatusBar) obj).isKeyguardShowing());
    }
}
