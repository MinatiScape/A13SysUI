package com.android.systemui.accessibility;

import com.android.systemui.accessibility.MagnificationModeSwitch;
import com.android.systemui.statusbar.notification.VisibilityLocationProvider;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ModeSwitchesController$$ExternalSyntheticLambda0 implements MagnificationModeSwitch.SwitchListener, VisibilityLocationProvider {
    public final /* synthetic */ Object f$0;

    @Override // com.android.systemui.statusbar.notification.VisibilityLocationProvider
    public final boolean isInVisibleLocation(NotificationEntry notificationEntry) {
        Objects.requireNonNull((NotificationStackScrollLayoutController) this.f$0);
        return NotificationStackScrollLayoutController.isInVisibleLocation(notificationEntry);
    }

    @Override // com.android.systemui.accessibility.MagnificationModeSwitch.SwitchListener
    public final void onSwitch(int i, int i2) {
        ((ModeSwitchesController) this.f$0).onSwitch(i, i2);
    }

    public /* synthetic */ ModeSwitchesController$$ExternalSyntheticLambda0(Object obj) {
        this.f$0 = obj;
    }
}
