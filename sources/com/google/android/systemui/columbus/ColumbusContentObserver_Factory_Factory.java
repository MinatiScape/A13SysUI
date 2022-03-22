package com.google.android.systemui.columbus;

import android.os.Handler;
import com.android.systemui.settings.UserTracker;
import com.google.android.systemui.columbus.ColumbusContentObserver;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ColumbusContentObserver_Factory_Factory implements Factory<ColumbusContentObserver.Factory> {
    public final Provider<ContentResolverWrapper> contentResolverProvider;
    public final Provider<Executor> executorProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<UserTracker> userTrackerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ColumbusContentObserver.Factory(this.contentResolverProvider.mo144get(), this.userTrackerProvider.mo144get(), this.handlerProvider.mo144get(), this.executorProvider.mo144get());
    }

    public ColumbusContentObserver_Factory_Factory(Provider<ContentResolverWrapper> provider, Provider<UserTracker> provider2, Provider<Handler> provider3, Provider<Executor> provider4) {
        this.contentResolverProvider = provider;
        this.userTrackerProvider = provider2;
        this.handlerProvider = provider3;
        this.executorProvider = provider4;
    }
}
