package com.google.android.systemui.columbus.actions;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.statusbar.phone.StatusBar;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SettingsAction_Factory implements Factory<SettingsAction> {
    public final Provider<Context> contextProvider;
    public final Provider<StatusBar> statusBarProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new SettingsAction(this.contextProvider.mo144get(), this.statusBarProvider.mo144get(), this.uiEventLoggerProvider.mo144get());
    }

    public SettingsAction_Factory(Provider<Context> provider, Provider<StatusBar> provider2, Provider<UiEventLogger> provider3) {
        this.contextProvider = provider;
        this.statusBarProvider = provider2;
        this.uiEventLoggerProvider = provider3;
    }
}
