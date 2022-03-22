package com.android.systemui.recents;

import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class OverviewProxyService$$ExternalSyntheticLambda6 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public static final /* synthetic */ OverviewProxyService$$ExternalSyntheticLambda6 INSTANCE$1 = new OverviewProxyService$$ExternalSyntheticLambda6(1);
    public static final /* synthetic */ OverviewProxyService$$ExternalSyntheticLambda6 INSTANCE = new OverviewProxyService$$ExternalSyntheticLambda6(0);
    public static final /* synthetic */ OverviewProxyService$$ExternalSyntheticLambda6 INSTANCE$2 = new OverviewProxyService$$ExternalSyntheticLambda6(2);

    public /* synthetic */ OverviewProxyService$$ExternalSyntheticLambda6(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                ((LegacySplitScreen) obj).setMinimized(false);
                return;
            case 1:
                ((WakefulnessLifecycle.Observer) obj).onPostFinishedWakingUp();
                return;
            default:
                ((PhonePipMenuController.Listener) obj).onPipDismiss();
                return;
        }
    }
}
