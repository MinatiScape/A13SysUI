package com.google.android.systemui.columbus.actions;

import android.content.Context;
import android.os.Handler;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.statusbar.policy.FlashlightController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ToggleFlashlight_Factory implements Factory<ToggleFlashlight> {
    public final Provider<Context> contextProvider;
    public final Provider<FlashlightController> flashlightControllerProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ToggleFlashlight(this.contextProvider.mo144get(), this.flashlightControllerProvider.mo144get(), this.handlerProvider.mo144get(), this.uiEventLoggerProvider.mo144get());
    }

    public ToggleFlashlight_Factory(Provider<Context> provider, Provider<FlashlightController> provider2, Provider<Handler> provider3, Provider<UiEventLogger> provider4) {
        this.contextProvider = provider;
        this.flashlightControllerProvider = provider2;
        this.handlerProvider = provider3;
        this.uiEventLoggerProvider = provider4;
    }
}
