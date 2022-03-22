package com.android.systemui.qs;

import com.android.systemui.plugins.qs.QS;
import com.android.systemui.tuner.TunerService;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSAnimator_Factory implements Factory<QSAnimator> {
    public final Provider<Executor> executorProvider;
    public final Provider<QSFgsManagerFooter> fgsManagerFooterProvider;
    public final Provider<QSExpansionPathInterpolator> qsExpansionPathInterpolatorProvider;
    public final Provider<QSPanelController> qsPanelControllerProvider;
    public final Provider<QS> qsProvider;
    public final Provider<QSTileHost> qsTileHostProvider;
    public final Provider<QuickQSPanel> quickPanelProvider;
    public final Provider<QuickQSPanelController> quickQSPanelControllerProvider;
    public final Provider<QuickStatusBarHeader> quickStatusBarHeaderProvider;
    public final Provider<QSSecurityFooter> securityFooterProvider;
    public final Provider<TunerService> tunerServiceProvider;

    public static QSAnimator_Factory create(Provider<QS> provider, Provider<QuickQSPanel> provider2, Provider<QuickStatusBarHeader> provider3, Provider<QSPanelController> provider4, Provider<QuickQSPanelController> provider5, Provider<QSTileHost> provider6, Provider<QSFgsManagerFooter> provider7, Provider<QSSecurityFooter> provider8, Provider<Executor> provider9, Provider<TunerService> provider10, Provider<QSExpansionPathInterpolator> provider11) {
        return new QSAnimator_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new QSAnimator(this.qsProvider.mo144get(), this.quickPanelProvider.mo144get(), this.quickStatusBarHeaderProvider.mo144get(), this.qsPanelControllerProvider.mo144get(), this.quickQSPanelControllerProvider.mo144get(), this.qsTileHostProvider.mo144get(), this.fgsManagerFooterProvider.mo144get(), this.securityFooterProvider.mo144get(), this.executorProvider.mo144get(), this.tunerServiceProvider.mo144get(), this.qsExpansionPathInterpolatorProvider.mo144get());
    }

    public QSAnimator_Factory(Provider<QS> provider, Provider<QuickQSPanel> provider2, Provider<QuickStatusBarHeader> provider3, Provider<QSPanelController> provider4, Provider<QuickQSPanelController> provider5, Provider<QSTileHost> provider6, Provider<QSFgsManagerFooter> provider7, Provider<QSSecurityFooter> provider8, Provider<Executor> provider9, Provider<TunerService> provider10, Provider<QSExpansionPathInterpolator> provider11) {
        this.qsProvider = provider;
        this.quickPanelProvider = provider2;
        this.quickStatusBarHeaderProvider = provider3;
        this.qsPanelControllerProvider = provider4;
        this.quickQSPanelControllerProvider = provider5;
        this.qsTileHostProvider = provider6;
        this.fgsManagerFooterProvider = provider7;
        this.securityFooterProvider = provider8;
        this.executorProvider = provider9;
        this.tunerServiceProvider = provider10;
        this.qsExpansionPathInterpolatorProvider = provider11;
    }
}
