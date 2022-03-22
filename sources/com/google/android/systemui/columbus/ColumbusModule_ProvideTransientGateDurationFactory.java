package com.google.android.systemui.columbus;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class ColumbusModule_ProvideTransientGateDurationFactory implements Factory<Long> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final ColumbusModule_ProvideTransientGateDurationFactory INSTANCE = new ColumbusModule_ProvideTransientGateDurationFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return 500L;
    }
}
