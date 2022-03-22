package com.android.systemui.dock;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class DockManagerImpl_Factory implements Factory<DockManagerImpl> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final DockManagerImpl_Factory INSTANCE = new DockManagerImpl_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DockManagerImpl();
    }
}
