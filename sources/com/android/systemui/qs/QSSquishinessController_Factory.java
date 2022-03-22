package com.android.systemui.qs;

import android.util.DisplayMetrics;
import com.android.systemui.classifier.FalsingDataProvider;
import com.android.systemui.dock.DockManager;
import com.android.systemui.statusbar.policy.BatteryController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSSquishinessController_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider qsAnimatorProvider;
    public final Provider qsPanelControllerProvider;
    public final Provider quickQSPanelControllerProvider;

    public /* synthetic */ QSSquishinessController_Factory(Provider provider, Provider provider2, Provider provider3, int i) {
        this.$r8$classId = i;
        this.qsAnimatorProvider = provider;
        this.qsPanelControllerProvider = provider2;
        this.quickQSPanelControllerProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new QSSquishinessController((QSAnimator) this.qsAnimatorProvider.mo144get(), (QSPanelController) this.qsPanelControllerProvider.mo144get(), (QuickQSPanelController) this.quickQSPanelControllerProvider.mo144get());
            default:
                return new FalsingDataProvider((DisplayMetrics) this.qsAnimatorProvider.mo144get(), (BatteryController) this.qsPanelControllerProvider.mo144get(), (DockManager) this.quickQSPanelControllerProvider.mo144get());
        }
    }
}
