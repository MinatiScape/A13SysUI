package com.google.android.systemui.assist.uihints;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class LightnessProvider_Factory implements Factory<LightnessProvider> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final LightnessProvider_Factory INSTANCE = new LightnessProvider_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new LightnessProvider();
    }
}
