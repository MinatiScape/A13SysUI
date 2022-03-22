package com.android.wm.shell.dagger;

import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory implements Factory<PipSurfaceTransactionHelper> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory INSTANCE = new WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new PipSurfaceTransactionHelper();
    }
}
