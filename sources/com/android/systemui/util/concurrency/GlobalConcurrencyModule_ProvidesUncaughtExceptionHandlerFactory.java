package com.android.systemui.util.concurrency;

import dagger.internal.Factory;
import java.lang.Thread;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public final class GlobalConcurrencyModule_ProvidesUncaughtExceptionHandlerFactory implements Factory<Optional<Thread.UncaughtExceptionHandler>> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final GlobalConcurrencyModule_ProvidesUncaughtExceptionHandlerFactory INSTANCE = new GlobalConcurrencyModule_ProvidesUncaughtExceptionHandlerFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        Optional ofNullable = Optional.ofNullable(Thread.getUncaughtExceptionPreHandler());
        Objects.requireNonNull(ofNullable, "Cannot return null from a non-@Nullable @Provides method");
        return ofNullable;
    }
}
