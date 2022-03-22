package com.android.systemui.communal;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class CommunalStateController_Factory implements Factory<CommunalStateController> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final CommunalStateController_Factory INSTANCE = new CommunalStateController_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new CommunalStateController();
    }
}
