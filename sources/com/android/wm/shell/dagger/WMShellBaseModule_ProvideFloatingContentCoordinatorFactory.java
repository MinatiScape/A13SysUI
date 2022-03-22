package com.android.wm.shell.dagger;

import com.android.wm.shell.common.FloatingContentCoordinator;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class WMShellBaseModule_ProvideFloatingContentCoordinatorFactory implements Factory<FloatingContentCoordinator> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final WMShellBaseModule_ProvideFloatingContentCoordinatorFactory INSTANCE = new WMShellBaseModule_ProvideFloatingContentCoordinatorFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new FloatingContentCoordinator();
    }
}
