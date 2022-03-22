package com.android.systemui.statusbar.notification.collection.provider;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class VisualStabilityProvider_Factory implements Factory<VisualStabilityProvider> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final VisualStabilityProvider_Factory INSTANCE = new VisualStabilityProvider_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new VisualStabilityProvider();
    }
}
