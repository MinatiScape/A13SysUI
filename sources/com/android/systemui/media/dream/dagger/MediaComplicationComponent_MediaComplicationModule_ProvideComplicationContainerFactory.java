package com.android.systemui.media.dream.dagger;

import android.content.Context;
import android.widget.FrameLayout;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaComplicationComponent_MediaComplicationModule_ProvideComplicationContainerFactory implements Factory<FrameLayout> {
    public final Provider<Context> contextProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new FrameLayout(this.contextProvider.mo144get());
    }

    public MediaComplicationComponent_MediaComplicationModule_ProvideComplicationContainerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }
}
