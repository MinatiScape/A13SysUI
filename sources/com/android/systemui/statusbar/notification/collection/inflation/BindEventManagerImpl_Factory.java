package com.android.systemui.statusbar.notification.collection.inflation;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class BindEventManagerImpl_Factory implements Factory<BindEventManagerImpl> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final BindEventManagerImpl_Factory INSTANCE = new BindEventManagerImpl_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new BindEventManagerImpl();
    }
}
