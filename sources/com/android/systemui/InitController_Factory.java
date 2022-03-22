package com.android.systemui;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class InitController_Factory implements Factory<InitController> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final InitController_Factory INSTANCE = new InitController_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new InitController();
    }
}
