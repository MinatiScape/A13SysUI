package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.policy.SecurityController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.CarrierConfigTracker;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarSignalPolicy_Factory implements Factory<StatusBarSignalPolicy> {
    public final Provider<CarrierConfigTracker> carrierConfigTrackerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<FeatureFlags> featureFlagsProvider;
    public final Provider<StatusBarIconController> iconControllerProvider;
    public final Provider<NetworkController> networkControllerProvider;
    public final Provider<SecurityController> securityControllerProvider;
    public final Provider<TunerService> tunerServiceProvider;

    public static StatusBarSignalPolicy_Factory create(Provider<Context> provider, Provider<StatusBarIconController> provider2, Provider<CarrierConfigTracker> provider3, Provider<NetworkController> provider4, Provider<SecurityController> provider5, Provider<TunerService> provider6, Provider<FeatureFlags> provider7) {
        return new StatusBarSignalPolicy_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new StatusBarSignalPolicy(this.contextProvider.mo144get(), this.iconControllerProvider.mo144get(), this.carrierConfigTrackerProvider.mo144get(), this.networkControllerProvider.mo144get(), this.securityControllerProvider.mo144get(), this.tunerServiceProvider.mo144get(), this.featureFlagsProvider.mo144get());
    }

    public StatusBarSignalPolicy_Factory(Provider<Context> provider, Provider<StatusBarIconController> provider2, Provider<CarrierConfigTracker> provider3, Provider<NetworkController> provider4, Provider<SecurityController> provider5, Provider<TunerService> provider6, Provider<FeatureFlags> provider7) {
        this.contextProvider = provider;
        this.iconControllerProvider = provider2;
        this.carrierConfigTrackerProvider = provider3;
        this.networkControllerProvider = provider4;
        this.securityControllerProvider = provider5;
        this.tunerServiceProvider = provider6;
        this.featureFlagsProvider = provider7;
    }
}
