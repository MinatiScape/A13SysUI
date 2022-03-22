package com.google.android.systemui.assist.uihints;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class GoBackHandler_Factory implements Factory<GoBackHandler> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final GoBackHandler_Factory INSTANCE = new GoBackHandler_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new GoBackHandler();
    }
}
