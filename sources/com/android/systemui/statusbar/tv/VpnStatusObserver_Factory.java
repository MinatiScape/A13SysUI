package com.android.systemui.statusbar.tv;

import android.content.Context;
import com.android.systemui.statusbar.policy.SecurityController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class VpnStatusObserver_Factory implements Factory<VpnStatusObserver> {
    public final Provider<Context> contextProvider;
    public final Provider<SecurityController> securityControllerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new VpnStatusObserver(this.contextProvider.mo144get(), this.securityControllerProvider.mo144get());
    }

    public VpnStatusObserver_Factory(Provider<Context> provider, Provider<SecurityController> provider2) {
        this.contextProvider = provider;
        this.securityControllerProvider = provider2;
    }
}
