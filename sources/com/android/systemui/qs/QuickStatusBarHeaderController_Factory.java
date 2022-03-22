package com.android.systemui.qs;

import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.battery.BatteryMeterViewController_Factory;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.qs.carrier.QSCarrierGroupController;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.policy.VariableDateViewController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QuickStatusBarHeaderController_Factory implements Factory<QuickStatusBarHeaderController> {
    public final Provider<BatteryMeterViewController> batteryMeterViewControllerProvider;
    public final Provider<SysuiColorExtractor> colorExtractorProvider;
    public final Provider<DemoModeController> demoModeControllerProvider;
    public final Provider<FeatureFlags> featureFlagsProvider;
    public final Provider<HeaderPrivacyIconsController> headerPrivacyIconsControllerProvider;
    public final Provider<QSCarrierGroupController.Builder> qsCarrierGroupControllerBuilderProvider;
    public final Provider<QSExpansionPathInterpolator> qsExpansionPathInterpolatorProvider;
    public final Provider<QuickQSPanelController> quickQSPanelControllerProvider;
    public final Provider<StatusBarContentInsetsProvider> statusBarContentInsetsProvider;
    public final Provider<StatusBarIconController> statusBarIconControllerProvider;
    public final Provider<VariableDateViewController.Factory> variableDateViewControllerFactoryProvider;
    public final Provider<QuickStatusBarHeader> viewProvider;

    public static QuickStatusBarHeaderController_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, BatteryMeterViewController_Factory batteryMeterViewController_Factory, Provider provider11) {
        return new QuickStatusBarHeaderController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, batteryMeterViewController_Factory, provider11);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new QuickStatusBarHeaderController(this.viewProvider.mo144get(), this.headerPrivacyIconsControllerProvider.mo144get(), this.statusBarIconControllerProvider.mo144get(), this.demoModeControllerProvider.mo144get(), this.quickQSPanelControllerProvider.mo144get(), this.qsCarrierGroupControllerBuilderProvider.mo144get(), this.colorExtractorProvider.mo144get(), this.qsExpansionPathInterpolatorProvider.mo144get(), this.featureFlagsProvider.mo144get(), this.variableDateViewControllerFactoryProvider.mo144get(), this.batteryMeterViewControllerProvider.mo144get(), this.statusBarContentInsetsProvider.mo144get());
    }

    public QuickStatusBarHeaderController_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, BatteryMeterViewController_Factory batteryMeterViewController_Factory, Provider provider11) {
        this.viewProvider = provider;
        this.headerPrivacyIconsControllerProvider = provider2;
        this.statusBarIconControllerProvider = provider3;
        this.demoModeControllerProvider = provider4;
        this.quickQSPanelControllerProvider = provider5;
        this.qsCarrierGroupControllerBuilderProvider = provider6;
        this.colorExtractorProvider = provider7;
        this.qsExpansionPathInterpolatorProvider = provider8;
        this.featureFlagsProvider = provider9;
        this.variableDateViewControllerFactoryProvider = provider10;
        this.batteryMeterViewControllerProvider = batteryMeterViewController_Factory;
        this.statusBarContentInsetsProvider = provider11;
    }
}
