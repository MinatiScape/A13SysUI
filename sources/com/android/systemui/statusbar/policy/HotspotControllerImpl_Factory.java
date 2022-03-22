package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class HotspotControllerImpl_Factory implements Factory<HotspotControllerImpl> {
    public final Provider<Handler> backgroundHandlerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<Handler> mainHandlerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new HotspotControllerImpl(this.contextProvider.mo144get(), this.mainHandlerProvider.mo144get(), this.backgroundHandlerProvider.mo144get(), this.dumpManagerProvider.mo144get());
    }

    public HotspotControllerImpl_Factory(Provider<Context> provider, Provider<Handler> provider2, Provider<Handler> provider3, Provider<DumpManager> provider4) {
        this.contextProvider = provider;
        this.mainHandlerProvider = provider2;
        this.backgroundHandlerProvider = provider3;
        this.dumpManagerProvider = provider4;
    }
}
