package com.google.android.systemui.columbus.gates;

import android.content.Context;
import com.android.systemui.doze.DozeLog;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.DozeScrimController;
import com.android.systemui.statusbar.policy.DeviceStateRotationLockSettingController;
import com.android.systemui.statusbar.policy.RotationLockControllerImpl;
import com.android.systemui.util.wrapper.RotationPolicyWrapper;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SetupWizard_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;
    public final Provider exceptionsProvider;
    public final Provider provisionedControllerProvider;

    public /* synthetic */ SetupWizard_Factory(Provider provider, Provider provider2, Provider provider3, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.exceptionsProvider = provider2;
        this.provisionedControllerProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new SetupWizard((Context) this.contextProvider.mo144get(), (Set) this.exceptionsProvider.mo144get(), DoubleCheck.lazy(this.provisionedControllerProvider));
            case 1:
                return new DozeScrimController((DozeParameters) this.contextProvider.mo144get(), (DozeLog) this.exceptionsProvider.mo144get(), (StatusBarStateController) this.provisionedControllerProvider.mo144get());
            default:
                return new RotationLockControllerImpl((RotationPolicyWrapper) this.contextProvider.mo144get(), (DeviceStateRotationLockSettingController) this.exceptionsProvider.mo144get(), (String[]) this.provisionedControllerProvider.mo144get());
        }
    }
}
