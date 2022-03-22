package com.android.systemui.doze.dagger;

import android.os.Handler;
import com.android.systemui.util.wakelock.DelayedWakeLock;
import com.android.systemui.util.wakelock.WakeLock;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeModule_ProvidesDozeWakeLockFactory implements Factory<WakeLock> {
    public final Provider<DelayedWakeLock.Builder> delayedWakeLockBuilderProvider;
    public final Provider<Handler> handlerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        DelayedWakeLock.Builder builder = this.delayedWakeLockBuilderProvider.mo144get();
        Handler handler = this.handlerProvider.mo144get();
        Objects.requireNonNull(builder);
        builder.mHandler = handler;
        builder.mTag = "Doze";
        return new DelayedWakeLock(handler, WakeLock.createPartial(builder.mContext, "Doze"));
    }

    public DozeModule_ProvidesDozeWakeLockFactory(Provider<DelayedWakeLock.Builder> provider, Provider<Handler> provider2) {
        this.delayedWakeLockBuilderProvider = provider;
        this.handlerProvider = provider2;
    }
}
