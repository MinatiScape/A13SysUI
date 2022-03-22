package com.android.systemui.util.concurrency;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class ThreadFactoryImpl_Factory implements Factory<ThreadFactoryImpl> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final ThreadFactoryImpl_Factory INSTANCE = new ThreadFactoryImpl_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ThreadFactoryImpl();
    }
}
