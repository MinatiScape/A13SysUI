package com.android.wm.shell.dagger;

import android.content.Context;
import android.os.Handler;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.tv.TvPipNotificationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TvPipModule_ProvideTvPipNotificationControllerFactory implements Factory<TvPipNotificationController> {
    public final Provider<Context> contextProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<PipMediaController> pipMediaControllerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new TvPipNotificationController(this.contextProvider.mo144get(), this.pipMediaControllerProvider.mo144get(), this.mainHandlerProvider.mo144get());
    }

    public TvPipModule_ProvideTvPipNotificationControllerFactory(Provider<Context> provider, Provider<PipMediaController> provider2, Provider<Handler> provider3) {
        this.contextProvider = provider;
        this.pipMediaControllerProvider = provider2;
        this.mainHandlerProvider = provider3;
    }
}
