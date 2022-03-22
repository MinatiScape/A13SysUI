package com.android.keyguard;

import android.content.Context;
import android.os.Handler;
import com.android.keyguard.AdminSecondaryLockScreenController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AdminSecondaryLockScreenController_Factory_Factory implements Factory<AdminSecondaryLockScreenController.Factory> {
    public final Provider<Context> contextProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<KeyguardSecurityContainer> parentProvider;
    public final Provider<KeyguardUpdateMonitor> updateMonitorProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new AdminSecondaryLockScreenController.Factory(this.contextProvider.mo144get(), this.parentProvider.mo144get(), this.updateMonitorProvider.mo144get(), this.handlerProvider.mo144get());
    }

    public AdminSecondaryLockScreenController_Factory_Factory(Provider<Context> provider, Provider<KeyguardSecurityContainer> provider2, Provider<KeyguardUpdateMonitor> provider3, Provider<Handler> provider4) {
        this.contextProvider = provider;
        this.parentProvider = provider2;
        this.updateMonitorProvider = provider3;
        this.handlerProvider = provider4;
    }
}
