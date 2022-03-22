package com.google.android.systemui.power;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.power.PowerUI;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PowerModuleGoogle_ProvideWarningsUiFactory implements Factory<PowerUI.WarningsUI> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new PowerNotificationWarningsGoogleImpl(this.contextProvider.mo144get(), this.activityStarterProvider.mo144get(), this.broadcastDispatcherProvider.mo144get(), this.uiEventLoggerProvider.mo144get());
    }

    public PowerModuleGoogle_ProvideWarningsUiFactory(Provider<Context> provider, Provider<ActivityStarter> provider2, Provider<BroadcastDispatcher> provider3, Provider<UiEventLogger> provider4) {
        this.contextProvider = provider;
        this.activityStarterProvider = provider2;
        this.broadcastDispatcherProvider = provider3;
        this.uiEventLoggerProvider = provider4;
    }
}
