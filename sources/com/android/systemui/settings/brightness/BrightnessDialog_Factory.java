package com.android.systemui.settings.brightness;

import android.os.Handler;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.brightness.BrightnessSliderController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class BrightnessDialog_Factory implements Factory<BrightnessDialog> {
    public final Provider<Handler> bgHandlerProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<BrightnessSliderController.Factory> factoryProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new BrightnessDialog(this.broadcastDispatcherProvider.mo144get(), this.factoryProvider.mo144get(), this.bgHandlerProvider.mo144get());
    }

    public BrightnessDialog_Factory(Provider<BroadcastDispatcher> provider, Provider<BrightnessSliderController.Factory> provider2, Provider<Handler> provider3) {
        this.broadcastDispatcherProvider = provider;
        this.factoryProvider = provider2;
        this.bgHandlerProvider = provider3;
    }
}
