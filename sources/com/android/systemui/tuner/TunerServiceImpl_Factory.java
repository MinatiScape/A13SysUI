package com.android.systemui.tuner;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.leak.LeakDetector;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TunerServiceImpl_Factory implements Factory<TunerServiceImpl> {
    public final Provider<Context> contextProvider;
    public final Provider<DemoModeController> demoModeControllerProvider;
    public final Provider<LeakDetector> leakDetectorProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<UserTracker> userTrackerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new TunerServiceImpl(this.contextProvider.mo144get(), this.mainHandlerProvider.mo144get(), this.leakDetectorProvider.mo144get(), this.demoModeControllerProvider.mo144get(), this.userTrackerProvider.mo144get());
    }

    public TunerServiceImpl_Factory(Provider<Context> provider, Provider<Handler> provider2, Provider<LeakDetector> provider3, Provider<DemoModeController> provider4, Provider<UserTracker> provider5) {
        this.contextProvider = provider;
        this.mainHandlerProvider = provider2;
        this.leakDetectorProvider = provider3;
        this.demoModeControllerProvider = provider4;
        this.userTrackerProvider = provider5;
    }
}
