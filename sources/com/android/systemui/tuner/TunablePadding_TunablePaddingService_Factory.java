package com.android.systemui.tuner;

import com.android.systemui.tuner.TunablePadding;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TunablePadding_TunablePaddingService_Factory implements Factory<TunablePadding.TunablePaddingService> {
    public final Provider<TunerService> tunerServiceProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        this.tunerServiceProvider.mo144get();
        return new TunablePadding.TunablePaddingService();
    }

    public TunablePadding_TunablePaddingService_Factory(Provider<TunerService> provider) {
        this.tunerServiceProvider = provider;
    }
}
