package com.google.android.systemui.assist.uihints;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class FlingVelocityWrapper_Factory implements Factory<FlingVelocityWrapper> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final FlingVelocityWrapper_Factory INSTANCE = new FlingVelocityWrapper_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new FlingVelocityWrapper();
    }
}
