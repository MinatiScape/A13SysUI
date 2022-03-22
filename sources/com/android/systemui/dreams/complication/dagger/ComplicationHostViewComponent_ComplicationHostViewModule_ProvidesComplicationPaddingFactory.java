package com.android.systemui.dreams.complication.dagger;

import android.content.res.Resources;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ComplicationHostViewComponent_ComplicationHostViewModule_ProvidesComplicationPaddingFactory implements Factory<Integer> {
    public final Provider<Resources> resourcesProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return Integer.valueOf(this.resourcesProvider.mo144get().getDimensionPixelSize(2131165689));
    }

    public ComplicationHostViewComponent_ComplicationHostViewModule_ProvidesComplicationPaddingFactory(Provider<Resources> provider) {
        this.resourcesProvider = provider;
    }
}
