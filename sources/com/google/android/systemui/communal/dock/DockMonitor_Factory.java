package com.google.android.systemui.communal.dock;

import android.content.Context;
import com.android.systemui.util.condition.Monitor;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DockMonitor_Factory implements Factory<DockMonitor> {
    public final Provider<Monitor> conditionMonitorProvider;
    public final Provider<Context> contextProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DockMonitor(this.contextProvider.mo144get(), DoubleCheck.lazy(this.conditionMonitorProvider));
    }

    public DockMonitor_Factory(Provider<Context> provider, Provider<Monitor> provider2) {
        this.contextProvider = provider;
        this.conditionMonitorProvider = provider2;
    }
}
