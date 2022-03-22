package com.android.systemui.recents;

import com.android.systemui.plugins.qs.QS;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.phone.NotificationsQuickSettingsContainer;
import com.android.wm.shell.pip.Pip;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class OverviewProxyService$1$$ExternalSyntheticLambda15 implements Consumer {
    public static final /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda15 INSTANCE = new OverviewProxyService$1$$ExternalSyntheticLambda15(0);
    public static final /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda15 INSTANCE$1 = new OverviewProxyService$1$$ExternalSyntheticLambda15(1);
    public final /* synthetic */ int $r8$classId;

    public /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda15(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                int i = OverviewProxyService.AnonymousClass1.$r8$clinit;
                ((Pip) obj).setPinnedStackAnimationType();
                return;
            default:
                QS qs = (QS) obj;
                int i2 = NotificationsQuickSettingsContainer.$r8$clinit;
                return;
        }
    }
}
