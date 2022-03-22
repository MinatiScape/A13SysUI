package com.android.systemui.power;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class EnhancedEstimatesImpl_Factory implements Factory<EnhancedEstimatesImpl> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final EnhancedEstimatesImpl_Factory INSTANCE = new EnhancedEstimatesImpl_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new EnhancedEstimatesImpl();
    }
}
