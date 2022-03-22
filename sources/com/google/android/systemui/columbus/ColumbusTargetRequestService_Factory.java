package com.google.android.systemui.columbus;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.settings.UserTracker;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ColumbusTargetRequestService_Factory implements Factory<ColumbusTargetRequestService> {
    public final Provider<ColumbusSettings> columbusSettingsProvider;
    public final Provider<ColumbusStructuredDataManager> columbusStructuredDataManagerProvider;
    public final Provider<Looper> looperProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<Context> sysUIContextProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<UserTracker> userTrackerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ColumbusTargetRequestService(this.sysUIContextProvider.mo144get(), this.userTrackerProvider.mo144get(), this.columbusSettingsProvider.mo144get(), this.columbusStructuredDataManagerProvider.mo144get(), this.uiEventLoggerProvider.mo144get(), this.mainHandlerProvider.mo144get(), this.looperProvider.mo144get());
    }

    public ColumbusTargetRequestService_Factory(Provider<Context> provider, Provider<UserTracker> provider2, Provider<ColumbusSettings> provider3, Provider<ColumbusStructuredDataManager> provider4, Provider<UiEventLogger> provider5, Provider<Handler> provider6, Provider<Looper> provider7) {
        this.sysUIContextProvider = provider;
        this.userTrackerProvider = provider2;
        this.columbusSettingsProvider = provider3;
        this.columbusStructuredDataManagerProvider = provider4;
        this.uiEventLoggerProvider = provider5;
        this.mainHandlerProvider = provider6;
        this.looperProvider = provider7;
    }
}
