package com.android.systemui.util.leak;

import android.content.Context;
import com.android.systemui.util.leak.GarbageMonitor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class GarbageMonitor_Service_Factory implements Factory<GarbageMonitor.Service> {
    public final Provider<Context> contextProvider;
    public final Provider<GarbageMonitor> garbageMonitorProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new GarbageMonitor.Service(this.contextProvider.mo144get(), this.garbageMonitorProvider.mo144get());
    }

    public GarbageMonitor_Service_Factory(Provider<Context> provider, Provider<GarbageMonitor> provider2) {
        this.contextProvider = provider;
        this.garbageMonitorProvider = provider2;
    }
}
