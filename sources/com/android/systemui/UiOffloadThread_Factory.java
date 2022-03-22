package com.android.systemui;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class UiOffloadThread_Factory implements Factory<UiOffloadThread> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final UiOffloadThread_Factory INSTANCE = new UiOffloadThread_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new UiOffloadThread();
    }
}
