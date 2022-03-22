package com.android.systemui.navigationbar;

import com.android.systemui.statusbar.KeyguardIndicationController;
import java.util.Objects;
import java.util.function.Supplier;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NavigationBarView$$ExternalSyntheticLambda3 implements Supplier {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ NavigationBarView$$ExternalSyntheticLambda3(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Supplier
    public final Object get() {
        boolean z;
        switch (this.$r8$classId) {
            case 0:
                NavigationBarView navigationBarView = (NavigationBarView) this.f$0;
                int i = NavigationBarView.$r8$clinit;
                Objects.requireNonNull(navigationBarView);
                return Integer.valueOf(navigationBarView.getDisplay().getRotation());
            default:
                KeyguardIndicationController keyguardIndicationController = (KeyguardIndicationController) this.f$0;
                Objects.requireNonNull(keyguardIndicationController);
                if (keyguardIndicationController.mDevicePolicyManager.isDeviceManaged() || keyguardIndicationController.mDevicePolicyManager.isOrganizationOwnedDeviceWithManagedProfile()) {
                    z = true;
                } else {
                    z = false;
                }
                return Boolean.valueOf(z);
        }
    }
}
