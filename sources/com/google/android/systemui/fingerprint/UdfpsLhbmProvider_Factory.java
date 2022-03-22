package com.google.android.systemui.fingerprint;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class UdfpsLhbmProvider_Factory implements Factory<UdfpsLhbmProvider> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final UdfpsLhbmProvider_Factory INSTANCE = new UdfpsLhbmProvider_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new UdfpsLhbmProvider();
    }
}
