package com.android.systemui.navigationbar;

import android.os.SystemClock;
import android.view.View;
import com.android.systemui.statusbar.phone.StatusBar;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NavigationBarView$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ View f$0;

    public /* synthetic */ NavigationBarView$$ExternalSyntheticLambda2(View view, int i) {
        this.$r8$classId = i;
        this.f$0 = view;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                NavigationBarView navigationBarView = (NavigationBarView) this.f$0;
                int i = NavigationBarView.$r8$clinit;
                Objects.requireNonNull(navigationBarView);
                if (((Boolean) obj).booleanValue()) {
                    navigationBarView.mAutoHideController.touchAutoHide();
                }
                navigationBarView.notifyActiveTouchRegions();
                return;
            default:
                ((StatusBar) obj).wakeUpIfDozing(SystemClock.uptimeMillis(), this.f$0, "NOTIFICATION_CLICK");
                return;
        }
    }
}
