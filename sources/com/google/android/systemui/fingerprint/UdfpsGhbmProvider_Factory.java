package com.google.android.systemui.fingerprint;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class UdfpsGhbmProvider_Factory implements Factory<UdfpsGhbmProvider> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final UdfpsGhbmProvider_Factory INSTANCE = new UdfpsGhbmProvider_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new UdfpsGhbmProvider();
    }
}
