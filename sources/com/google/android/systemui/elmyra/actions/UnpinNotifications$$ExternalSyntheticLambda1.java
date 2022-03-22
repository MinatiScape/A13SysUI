package com.google.android.systemui.elmyra.actions;

import android.view.WindowInsets;
import com.android.systemui.statusbar.phone.NotificationsQuickSettingsContainer;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class UnpinNotifications$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public static final /* synthetic */ UnpinNotifications$$ExternalSyntheticLambda1 INSTANCE$1 = new UnpinNotifications$$ExternalSyntheticLambda1(1);
    public static final /* synthetic */ UnpinNotifications$$ExternalSyntheticLambda1 INSTANCE = new UnpinNotifications$$ExternalSyntheticLambda1(0);

    public /* synthetic */ UnpinNotifications$$ExternalSyntheticLambda1(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                ((HeadsUpManager) obj).unpinAll();
                return;
            default:
                WindowInsets windowInsets = (WindowInsets) obj;
                int i = NotificationsQuickSettingsContainer.$r8$clinit;
                return;
        }
    }
}
