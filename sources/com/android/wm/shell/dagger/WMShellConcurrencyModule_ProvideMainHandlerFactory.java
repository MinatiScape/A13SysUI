package com.android.wm.shell.dagger;

import android.os.Handler;
import android.os.Looper;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class WMShellConcurrencyModule_ProvideMainHandlerFactory implements Factory<Handler> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final WMShellConcurrencyModule_ProvideMainHandlerFactory INSTANCE = new WMShellConcurrencyModule_ProvideMainHandlerFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new Handler(Looper.getMainLooper());
    }
}
