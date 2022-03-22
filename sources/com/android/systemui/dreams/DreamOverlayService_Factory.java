package com.android.systemui.dreams;

import android.content.Context;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.dreams.dagger.DreamOverlayComponent;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DreamOverlayService_Factory implements Factory<DreamOverlayService> {
    public final Provider<Context> contextProvider;
    public final Provider<DreamOverlayComponent.Factory> dreamOverlayComponentFactoryProvider;
    public final Provider<Executor> executorProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<DreamOverlayStateController> stateControllerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DreamOverlayService(this.contextProvider.mo144get(), this.executorProvider.mo144get(), this.dreamOverlayComponentFactoryProvider.mo144get(), this.stateControllerProvider.mo144get(), this.keyguardUpdateMonitorProvider.mo144get());
    }

    public DreamOverlayService_Factory(Provider<Context> provider, Provider<Executor> provider2, Provider<DreamOverlayComponent.Factory> provider3, Provider<DreamOverlayStateController> provider4, Provider<KeyguardUpdateMonitor> provider5) {
        this.contextProvider = provider;
        this.executorProvider = provider2;
        this.dreamOverlayComponentFactoryProvider = provider3;
        this.stateControllerProvider = provider4;
        this.keyguardUpdateMonitorProvider = provider5;
    }
}
