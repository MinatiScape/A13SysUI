package com.android.systemui.util.wakelock;

import android.content.Context;
import com.android.systemui.util.wakelock.DelayedWakeLock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DelayedWakeLock_Builder_Factory implements Factory<DelayedWakeLock.Builder> {
    public final Provider<Context> contextProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DelayedWakeLock.Builder(this.contextProvider.mo144get());
    }

    public DelayedWakeLock_Builder_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }
}
