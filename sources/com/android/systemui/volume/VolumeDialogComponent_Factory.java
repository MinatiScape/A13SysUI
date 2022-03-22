package com.android.systemui.volume;

import android.content.Context;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.PluginDependencyProvider;
import com.android.systemui.plugins.VolumeDialog;
import com.android.systemui.statusbar.policy.ExtensionController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.volume.dagger.VolumeModule_ProvideVolumeDialogFactory;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class VolumeDialogComponent_Factory implements Factory<VolumeDialogComponent> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DemoModeController> demoModeControllerProvider;
    public final Provider<ExtensionController> extensionControllerProvider;
    public final Provider<KeyguardViewMediator> keyguardViewMediatorProvider;
    public final Provider<PluginDependencyProvider> pluginDependencyProvider;
    public final Provider<TunerService> tunerServiceProvider;
    public final Provider<VolumeDialogControllerImpl> volumeDialogControllerProvider;
    public final Provider<VolumeDialog> volumeDialogProvider;

    public static VolumeDialogComponent_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, VolumeModule_ProvideVolumeDialogFactory volumeModule_ProvideVolumeDialogFactory) {
        return new VolumeDialogComponent_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, volumeModule_ProvideVolumeDialogFactory);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new VolumeDialogComponent(this.contextProvider.mo144get(), this.keyguardViewMediatorProvider.mo144get(), this.activityStarterProvider.mo144get(), this.volumeDialogControllerProvider.mo144get(), this.demoModeControllerProvider.mo144get(), this.pluginDependencyProvider.mo144get(), this.extensionControllerProvider.mo144get(), this.tunerServiceProvider.mo144get(), this.volumeDialogProvider.mo144get());
    }

    public VolumeDialogComponent_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, VolumeModule_ProvideVolumeDialogFactory volumeModule_ProvideVolumeDialogFactory) {
        this.contextProvider = provider;
        this.keyguardViewMediatorProvider = provider2;
        this.activityStarterProvider = provider3;
        this.volumeDialogControllerProvider = provider4;
        this.demoModeControllerProvider = provider5;
        this.pluginDependencyProvider = provider6;
        this.extensionControllerProvider = provider7;
        this.tunerServiceProvider = provider8;
        this.volumeDialogProvider = volumeModule_ProvideVolumeDialogFactory;
    }
}
