package com.android.systemui.qs;

import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSContainerImplController_Factory implements Factory<QSContainerImplController> {
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<QSPanelController> qsPanelControllerProvider;
    public final Provider<QuickStatusBarHeaderController> quickStatusBarHeaderControllerProvider;
    public final Provider<QSContainerImpl> viewProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new QSContainerImplController(this.viewProvider.mo144get(), this.qsPanelControllerProvider.mo144get(), this.quickStatusBarHeaderControllerProvider.mo144get(), this.configurationControllerProvider.mo144get());
    }

    public QSContainerImplController_Factory(Provider<QSContainerImpl> provider, Provider<QSPanelController> provider2, Provider<QuickStatusBarHeaderController> provider3, Provider<ConfigurationController> provider4) {
        this.viewProvider = provider;
        this.qsPanelControllerProvider = provider2;
        this.quickStatusBarHeaderControllerProvider = provider3;
        this.configurationControllerProvider = provider4;
    }
}
