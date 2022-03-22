package com.android.systemui.dump;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class DumpManager_Factory implements Factory<DumpManager> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final DumpManager_Factory INSTANCE = new DumpManager_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DumpManager();
    }
}
