package com.android.systemui.qs.customize;

import android.content.Context;
import com.android.systemui.settings.UserTracker;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TileQueryHelper_Factory implements Factory<TileQueryHelper> {
    public final Provider<Executor> bgExecutorProvider;
    public final Provider<Context> contextProvider;
    public final Provider<Executor> mainExecutorProvider;
    public final Provider<UserTracker> userTrackerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new TileQueryHelper(this.contextProvider.mo144get(), this.userTrackerProvider.mo144get(), this.mainExecutorProvider.mo144get(), this.bgExecutorProvider.mo144get());
    }

    public TileQueryHelper_Factory(Provider<Context> provider, Provider<UserTracker> provider2, Provider<Executor> provider3, Provider<Executor> provider4) {
        this.contextProvider = provider;
        this.userTrackerProvider = provider2;
        this.mainExecutorProvider = provider3;
        this.bgExecutorProvider = provider4;
    }
}
