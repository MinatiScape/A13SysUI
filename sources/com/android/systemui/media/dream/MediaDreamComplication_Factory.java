package com.android.systemui.media.dream;

import com.android.systemui.media.dream.dagger.MediaComplicationComponent$Factory;
import com.google.android.systemui.titan.DaggerTitanGlobalRootComponent;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaDreamComplication_Factory implements Factory<MediaDreamComplication> {
    public final Provider<MediaComplicationComponent$Factory> componentFactoryProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new MediaDreamComplication(this.componentFactoryProvider.mo144get());
    }

    public MediaDreamComplication_Factory(DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.AnonymousClass18 r1) {
        this.componentFactoryProvider = r1;
    }
}
