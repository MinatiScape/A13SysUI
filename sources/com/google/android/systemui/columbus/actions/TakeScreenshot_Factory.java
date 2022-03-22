package com.google.android.systemui.columbus.actions;

import android.content.Context;
import android.os.Handler;
import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TakeScreenshot_Factory implements Factory<TakeScreenshot> {
    public final Provider<Context> contextProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new TakeScreenshot(this.contextProvider.mo144get(), this.handlerProvider.mo144get(), this.uiEventLoggerProvider.mo144get());
    }

    public TakeScreenshot_Factory(Provider<Context> provider, Provider<Handler> provider2, Provider<UiEventLogger> provider3) {
        this.contextProvider = provider;
        this.handlerProvider = provider2;
        this.uiEventLoggerProvider = provider3;
    }
}
