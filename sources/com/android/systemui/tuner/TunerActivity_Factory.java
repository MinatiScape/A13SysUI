package com.android.systemui.tuner;

import com.android.systemui.demomode.DemoModeController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TunerActivity_Factory implements Factory<TunerActivity> {
    public final Provider<DemoModeController> demoModeControllerProvider;
    public final Provider<TunerService> tunerServiceProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new TunerActivity(this.demoModeControllerProvider.mo144get(), this.tunerServiceProvider.mo144get());
    }

    public TunerActivity_Factory(Provider<DemoModeController> provider, Provider<TunerService> provider2) {
        this.demoModeControllerProvider = provider;
        this.tunerServiceProvider = provider2;
    }
}
