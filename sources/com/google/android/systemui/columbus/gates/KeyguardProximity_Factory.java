package com.google.android.systemui.columbus.gates;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardProximity_Factory implements Factory<KeyguardProximity> {
    public final Provider<Context> contextProvider;
    public final Provider<KeyguardVisibility> keyguardGateProvider;
    public final Provider<Proximity> proximityProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new KeyguardProximity(this.contextProvider.mo144get(), this.keyguardGateProvider.mo144get(), this.proximityProvider.mo144get());
    }

    public KeyguardProximity_Factory(Provider<Context> provider, Provider<KeyguardVisibility> provider2, Provider<Proximity> provider3) {
        this.contextProvider = provider;
        this.keyguardGateProvider = provider2;
        this.proximityProvider = provider3;
    }
}
