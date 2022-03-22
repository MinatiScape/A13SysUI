package com.android.systemui.util.time;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class SystemClockImpl_Factory implements Factory<SystemClockImpl> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final SystemClockImpl_Factory INSTANCE = new SystemClockImpl_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new SystemClockImpl();
    }
}
