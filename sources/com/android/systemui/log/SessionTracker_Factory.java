package com.android.systemui.log;

import android.content.Context;
import com.android.internal.statusbar.IStatusBarService;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SessionTracker_Factory implements Factory<SessionTracker> {
    public final Provider<AuthController> authControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<IStatusBarService> statusBarServiceProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new SessionTracker(this.contextProvider.mo144get(), this.statusBarServiceProvider.mo144get(), this.authControllerProvider.mo144get(), this.keyguardUpdateMonitorProvider.mo144get(), this.keyguardStateControllerProvider.mo144get());
    }

    public SessionTracker_Factory(Provider<Context> provider, Provider<IStatusBarService> provider2, Provider<AuthController> provider3, Provider<KeyguardUpdateMonitor> provider4, Provider<KeyguardStateController> provider5) {
        this.contextProvider = provider;
        this.statusBarServiceProvider = provider2;
        this.authControllerProvider = provider3;
        this.keyguardUpdateMonitorProvider = provider4;
        this.keyguardStateControllerProvider = provider5;
    }
}
