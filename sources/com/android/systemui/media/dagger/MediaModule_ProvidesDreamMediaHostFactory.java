package com.android.systemui.media.dagger;

import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaHierarchyManager;
import com.android.systemui.media.MediaHost;
import com.android.systemui.media.MediaHostStatesManager;
import com.android.systemui.media.MediaHost_MediaHostStateHolder_Factory;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaModule_ProvidesDreamMediaHostFactory implements Factory<MediaHost> {
    public final Provider<MediaDataManager> dataManagerProvider;
    public final Provider<MediaHierarchyManager> hierarchyManagerProvider;
    public final Provider<MediaHost.MediaHostStateHolder> stateHolderProvider = MediaHost_MediaHostStateHolder_Factory.InstanceHolder.INSTANCE;
    public final Provider<MediaHostStatesManager> statesManagerProvider;

    public MediaModule_ProvidesDreamMediaHostFactory(Provider provider, Provider provider2, Provider provider3) {
        this.hierarchyManagerProvider = provider;
        this.dataManagerProvider = provider2;
        this.statesManagerProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new MediaHost(this.stateHolderProvider.mo144get(), this.hierarchyManagerProvider.mo144get(), this.dataManagerProvider.mo144get(), this.statesManagerProvider.mo144get());
    }
}
