package com.android.systemui.statusbar.phone;

import android.content.res.Resources;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.PowerManager;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.doze.AlwaysOnDisplayPolicy;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeParameters_Factory implements Factory<DozeParameters> {
    public final Provider<AlwaysOnDisplayPolicy> alwaysOnDisplayPolicyProvider;
    public final Provider<AmbientDisplayConfiguration> ambientDisplayConfigurationProvider;
    public final Provider<BatteryController> batteryControllerProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<FeatureFlags> featureFlagsProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<PowerManager> powerManagerProvider;
    public final Provider<Resources> resourcesProvider;
    public final Provider<ScreenOffAnimationController> screenOffAnimationControllerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<Optional<SysUIUnfoldComponent>> sysUiUnfoldComponentProvider;
    public final Provider<TunerService> tunerServiceProvider;
    public final Provider<UnlockedScreenOffAnimationController> unlockedScreenOffAnimationControllerProvider;

    public static DozeParameters_Factory create(Provider<Resources> provider, Provider<AmbientDisplayConfiguration> provider2, Provider<AlwaysOnDisplayPolicy> provider3, Provider<PowerManager> provider4, Provider<BatteryController> provider5, Provider<TunerService> provider6, Provider<DumpManager> provider7, Provider<FeatureFlags> provider8, Provider<ScreenOffAnimationController> provider9, Provider<Optional<SysUIUnfoldComponent>> provider10, Provider<UnlockedScreenOffAnimationController> provider11, Provider<KeyguardUpdateMonitor> provider12, Provider<ConfigurationController> provider13, Provider<StatusBarStateController> provider14) {
        return new DozeParameters_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DozeParameters(this.resourcesProvider.mo144get(), this.ambientDisplayConfigurationProvider.mo144get(), this.alwaysOnDisplayPolicyProvider.mo144get(), this.powerManagerProvider.mo144get(), this.batteryControllerProvider.mo144get(), this.tunerServiceProvider.mo144get(), this.dumpManagerProvider.mo144get(), this.featureFlagsProvider.mo144get(), this.screenOffAnimationControllerProvider.mo144get(), this.sysUiUnfoldComponentProvider.mo144get(), this.unlockedScreenOffAnimationControllerProvider.mo144get(), this.keyguardUpdateMonitorProvider.mo144get(), this.configurationControllerProvider.mo144get(), this.statusBarStateControllerProvider.mo144get());
    }

    public DozeParameters_Factory(Provider<Resources> provider, Provider<AmbientDisplayConfiguration> provider2, Provider<AlwaysOnDisplayPolicy> provider3, Provider<PowerManager> provider4, Provider<BatteryController> provider5, Provider<TunerService> provider6, Provider<DumpManager> provider7, Provider<FeatureFlags> provider8, Provider<ScreenOffAnimationController> provider9, Provider<Optional<SysUIUnfoldComponent>> provider10, Provider<UnlockedScreenOffAnimationController> provider11, Provider<KeyguardUpdateMonitor> provider12, Provider<ConfigurationController> provider13, Provider<StatusBarStateController> provider14) {
        this.resourcesProvider = provider;
        this.ambientDisplayConfigurationProvider = provider2;
        this.alwaysOnDisplayPolicyProvider = provider3;
        this.powerManagerProvider = provider4;
        this.batteryControllerProvider = provider5;
        this.tunerServiceProvider = provider6;
        this.dumpManagerProvider = provider7;
        this.featureFlagsProvider = provider8;
        this.screenOffAnimationControllerProvider = provider9;
        this.sysUiUnfoldComponentProvider = provider10;
        this.unlockedScreenOffAnimationControllerProvider = provider11;
        this.keyguardUpdateMonitorProvider = provider12;
        this.configurationControllerProvider = provider13;
        this.statusBarStateControllerProvider = provider14;
    }
}
