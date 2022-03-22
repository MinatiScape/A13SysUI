package com.android.systemui.statusbar.phone;

import android.view.View;
import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.qs.HeaderPrivacyIconsController;
import com.android.systemui.qs.carrier.QSCarrierGroupController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SplitShadeHeaderController_Factory implements Factory<SplitShadeHeaderController> {
    public final Provider<BatteryMeterViewController> batteryMeterViewControllerProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<FeatureFlags> featureFlagsProvider;
    public final Provider<HeaderPrivacyIconsController> privacyIconsControllerProvider;
    public final Provider<QSCarrierGroupController.Builder> qsCarrierGroupControllerBuilderProvider;
    public final Provider<StatusBarIconController> statusBarIconControllerProvider;
    public final Provider<View> statusBarProvider;

    public static SplitShadeHeaderController_Factory create(Provider<View> provider, Provider<StatusBarIconController> provider2, Provider<HeaderPrivacyIconsController> provider3, Provider<QSCarrierGroupController.Builder> provider4, Provider<FeatureFlags> provider5, Provider<BatteryMeterViewController> provider6, Provider<DumpManager> provider7) {
        return new SplitShadeHeaderController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new SplitShadeHeaderController(this.statusBarProvider.mo144get(), this.statusBarIconControllerProvider.mo144get(), this.privacyIconsControllerProvider.mo144get(), this.qsCarrierGroupControllerBuilderProvider.mo144get(), this.featureFlagsProvider.mo144get(), this.batteryMeterViewControllerProvider.mo144get(), this.dumpManagerProvider.mo144get());
    }

    public SplitShadeHeaderController_Factory(Provider<View> provider, Provider<StatusBarIconController> provider2, Provider<HeaderPrivacyIconsController> provider3, Provider<QSCarrierGroupController.Builder> provider4, Provider<FeatureFlags> provider5, Provider<BatteryMeterViewController> provider6, Provider<DumpManager> provider7) {
        this.statusBarProvider = provider;
        this.statusBarIconControllerProvider = provider2;
        this.privacyIconsControllerProvider = provider3;
        this.qsCarrierGroupControllerBuilderProvider = provider4;
        this.featureFlagsProvider = provider5;
        this.batteryMeterViewControllerProvider = provider6;
        this.dumpManagerProvider = provider7;
    }
}
