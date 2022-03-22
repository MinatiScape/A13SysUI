package com.google.android.systemui.communal.dock.callbacks.mediashell;

import com.android.systemui.dreams.DreamOverlayStateController;
import com.google.android.systemui.communal.dock.callbacks.mediashell.dagger.MediaShellComponent$Factory;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaShellCallback_Factory implements Factory<MediaShellCallback> {
    public final Provider<DreamOverlayStateController> dreamOverlayStateControllerProvider;
    public final Provider<MediaShellComponent$Factory> factoryProvider;
    public final Provider<MediaShellComplication> mediaShellComplicationProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new MediaShellCallback(this.factoryProvider.mo144get(), this.dreamOverlayStateControllerProvider.mo144get(), this.mediaShellComplicationProvider.mo144get());
    }

    public MediaShellCallback_Factory(Provider<MediaShellComponent$Factory> provider, Provider<DreamOverlayStateController> provider2, Provider<MediaShellComplication> provider3) {
        this.factoryProvider = provider;
        this.dreamOverlayStateControllerProvider = provider2;
        this.mediaShellComplicationProvider = provider3;
    }
}
