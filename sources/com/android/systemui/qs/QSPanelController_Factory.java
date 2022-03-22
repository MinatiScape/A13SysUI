package com.android.systemui.qs;

import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.media.MediaHost;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.QSTileRevealController;
import com.android.systemui.qs.customize.QSCustomizerController;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.settings.brightness.BrightnessController;
import com.android.systemui.settings.brightness.BrightnessController_Factory_Factory;
import com.android.systemui.settings.brightness.BrightnessSliderController;
import com.android.systemui.tuner.TunerService;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSPanelController_Factory implements Factory<QSPanelController> {
    public final Provider<BrightnessController.Factory> brightnessControllerFactoryProvider;
    public final Provider<BrightnessSliderController.Factory> brightnessSliderFactoryProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<FeatureFlags> featureFlagsProvider;
    public final Provider<MediaHost> mediaHostProvider;
    public final Provider<MetricsLogger> metricsLoggerProvider;
    public final Provider<QSCustomizerController> qsCustomizerControllerProvider;
    public final Provider<QSFgsManagerFooter> qsFgsManagerFooterProvider;
    public final Provider<QSLogger> qsLoggerProvider;
    public final Provider<QSSecurityFooter> qsSecurityFooterProvider;
    public final Provider<QSTileRevealController.Factory> qsTileRevealControllerFactoryProvider;
    public final Provider<QSTileHost> qstileHostProvider;
    public final Provider<TunerService> tunerServiceProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<Boolean> usingMediaPlayerProvider;
    public final Provider<QSPanel> viewProvider;

    public QSPanelController_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11, Provider provider12, Provider provider13, BrightnessController_Factory_Factory brightnessController_Factory_Factory, Provider provider14, Provider provider15, Provider provider16) {
        this.viewProvider = provider;
        this.qsFgsManagerFooterProvider = provider2;
        this.qsSecurityFooterProvider = provider3;
        this.tunerServiceProvider = provider4;
        this.qstileHostProvider = provider5;
        this.qsCustomizerControllerProvider = provider6;
        this.usingMediaPlayerProvider = provider7;
        this.mediaHostProvider = provider8;
        this.qsTileRevealControllerFactoryProvider = provider9;
        this.dumpManagerProvider = provider10;
        this.metricsLoggerProvider = provider11;
        this.uiEventLoggerProvider = provider12;
        this.qsLoggerProvider = provider13;
        this.brightnessControllerFactoryProvider = brightnessController_Factory_Factory;
        this.brightnessSliderFactoryProvider = provider14;
        this.falsingManagerProvider = provider15;
        this.featureFlagsProvider = provider16;
    }

    public static QSPanelController_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11, Provider provider12, Provider provider13, BrightnessController_Factory_Factory brightnessController_Factory_Factory, Provider provider14, Provider provider15, Provider provider16) {
        return new QSPanelController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, brightnessController_Factory_Factory, provider14, provider15, provider16);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        QSPanel qSPanel = this.viewProvider.mo144get();
        QSFgsManagerFooter qSFgsManagerFooter = this.qsFgsManagerFooterProvider.mo144get();
        QSSecurityFooter qSSecurityFooter = this.qsSecurityFooterProvider.mo144get();
        return new QSPanelController(qSPanel, qSFgsManagerFooter, qSSecurityFooter, this.tunerServiceProvider.mo144get(), this.qstileHostProvider.mo144get(), this.qsCustomizerControllerProvider.mo144get(), this.usingMediaPlayerProvider.mo144get().booleanValue(), this.mediaHostProvider.mo144get(), this.qsTileRevealControllerFactoryProvider.mo144get(), this.dumpManagerProvider.mo144get(), this.metricsLoggerProvider.mo144get(), this.uiEventLoggerProvider.mo144get(), this.qsLoggerProvider.mo144get(), this.brightnessControllerFactoryProvider.mo144get(), this.brightnessSliderFactoryProvider.mo144get(), this.falsingManagerProvider.mo144get(), this.featureFlagsProvider.mo144get());
    }
}
