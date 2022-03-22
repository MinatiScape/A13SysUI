package com.android.systemui.statusbar;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class DisableFlagsLogger_Factory implements Factory<DisableFlagsLogger> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final DisableFlagsLogger_Factory INSTANCE = new DisableFlagsLogger_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DisableFlagsLogger();
    }
}
