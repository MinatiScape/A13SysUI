package com.android.systemui.dreams;

import android.os.Handler;
import android.view.ViewGroup;
import com.android.systemui.dreams.complication.dagger.ComplicationHostViewComponent;
import com.android.systemui.media.MediaBrowserFactory_Factory;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DreamOverlayContainerViewController_Factory implements Factory<DreamOverlayContainerViewController> {
    public final Provider<Long> burnInProtectionUpdateIntervalProvider;
    public final Provider<ComplicationHostViewComponent.Factory> complicationHostViewFactoryProvider;
    public final Provider<DreamOverlayContainerView> containerViewProvider;
    public final Provider<ViewGroup> contentViewProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<Integer> maxBurnInOffsetProvider;
    public final Provider<DreamOverlayStatusBarViewController> statusBarViewControllerProvider;

    public static DreamOverlayContainerViewController_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, MediaBrowserFactory_Factory mediaBrowserFactory_Factory) {
        return new DreamOverlayContainerViewController_Factory(provider, provider2, provider3, provider4, provider5, provider6, mediaBrowserFactory_Factory);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DreamOverlayContainerViewController(this.containerViewProvider.mo144get(), this.complicationHostViewFactoryProvider.mo144get(), this.contentViewProvider.mo144get(), this.statusBarViewControllerProvider.mo144get(), this.handlerProvider.mo144get(), this.maxBurnInOffsetProvider.mo144get().intValue(), this.burnInProtectionUpdateIntervalProvider.mo144get().longValue());
    }

    public DreamOverlayContainerViewController_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, MediaBrowserFactory_Factory mediaBrowserFactory_Factory) {
        this.containerViewProvider = provider;
        this.complicationHostViewFactoryProvider = provider2;
        this.contentViewProvider = provider3;
        this.statusBarViewControllerProvider = provider4;
        this.handlerProvider = provider5;
        this.maxBurnInOffsetProvider = provider6;
        this.burnInProtectionUpdateIntervalProvider = mediaBrowserFactory_Factory;
    }
}
