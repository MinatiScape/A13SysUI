package com.android.systemui.util.concurrency;

import android.os.HandlerThread;
import android.os.Looper;
import dagger.internal.Factory;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SysUIConcurrencyModule_ProvideBgLooperFactory implements Factory<Looper> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final SysUIConcurrencyModule_ProvideBgLooperFactory INSTANCE = new SysUIConcurrencyModule_ProvideBgLooperFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        HandlerThread handlerThread = new HandlerThread("SysUiBg", 10);
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        Objects.requireNonNull(looper, "Cannot return null from a non-@Nullable @Provides method");
        return looper;
    }
}
