package com.android.systemui.dagger;

import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.unfold.FoldStateLoggingProvider;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class SysUIComponent$$ExternalSyntheticLambda0 implements Consumer {
    public static final /* synthetic */ SysUIComponent$$ExternalSyntheticLambda0 INSTANCE = new SysUIComponent$$ExternalSyntheticLambda0(0);
    public static final /* synthetic */ SysUIComponent$$ExternalSyntheticLambda0 INSTANCE$1 = new SysUIComponent$$ExternalSyntheticLambda0(1);
    public final /* synthetic */ int $r8$classId;

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                ((FoldStateLoggingProvider) obj).init();
                return;
            default:
                ((ScreenLifecycle.Observer) obj).onScreenTurnedOff();
                return;
        }
    }

    public /* synthetic */ SysUIComponent$$ExternalSyntheticLambda0(int i) {
        this.$r8$classId = i;
    }
}
