package com.android.systemui.util.wakelock;

import android.content.Context;
import com.android.systemui.util.wakelock.WakeLock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WakeLock_Builder_Factory implements Factory<WakeLock.Builder> {
    public final Provider<Context> contextProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new WakeLock.Builder(this.contextProvider.mo144get());
    }

    public WakeLock_Builder_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }
}
