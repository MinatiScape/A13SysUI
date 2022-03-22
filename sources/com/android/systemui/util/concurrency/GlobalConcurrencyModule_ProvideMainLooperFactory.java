package com.android.systemui.util.concurrency;

import android.os.Looper;
import dagger.internal.Factory;
import java.util.Objects;
/* loaded from: classes.dex */
public final class GlobalConcurrencyModule_ProvideMainLooperFactory implements Factory<Looper> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final GlobalConcurrencyModule_ProvideMainLooperFactory INSTANCE = new GlobalConcurrencyModule_ProvideMainLooperFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        Looper mainLooper = Looper.getMainLooper();
        Objects.requireNonNull(mainLooper, "Cannot return null from a non-@Nullable @Provides method");
        return mainLooper;
    }
}
