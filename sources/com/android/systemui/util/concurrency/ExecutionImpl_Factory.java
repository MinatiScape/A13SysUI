package com.android.systemui.util.concurrency;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class ExecutionImpl_Factory implements Factory<ExecutionImpl> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final ExecutionImpl_Factory INSTANCE = new ExecutionImpl_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ExecutionImpl();
    }
}
