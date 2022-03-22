package com.android.systemui.dagger;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.dagger.NightDisplayListenerModule;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NightDisplayListenerModule_Builder_Factory implements Factory<NightDisplayListenerModule.Builder> {
    public final Provider<Handler> bgHandlerProvider;
    public final Provider<Context> contextProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NightDisplayListenerModule.Builder(this.contextProvider.mo144get(), this.bgHandlerProvider.mo144get());
    }

    public NightDisplayListenerModule_Builder_Factory(Provider<Context> provider, Provider<Handler> provider2) {
        this.contextProvider = provider;
        this.bgHandlerProvider = provider2;
    }
}
