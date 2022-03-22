package com.google.android.systemui.communal.dock.dagger;

import com.android.systemui.util.condition.Condition;
import com.android.systemui.util.condition.Monitor;
import com.android.systemui.util.condition.dagger.MonitorComponent;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DockModule_ProvideConditionsMonitorFactory implements Factory<Monitor> {
    public final Provider<Set<Monitor.Callback>> callbacksProvider;
    public final Provider<Set<Condition>> conditionsProvider;
    public final Provider<MonitorComponent.Factory> factoryProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        Monitor monitor = this.factoryProvider.mo144get().create(this.conditionsProvider.mo144get(), this.callbacksProvider.mo144get()).getMonitor();
        Objects.requireNonNull(monitor, "Cannot return null from a non-@Nullable @Provides method");
        return monitor;
    }

    public DockModule_ProvideConditionsMonitorFactory(Provider<MonitorComponent.Factory> provider, Provider<Set<Condition>> provider2, Provider<Set<Monitor.Callback>> provider3) {
        this.factoryProvider = provider;
        this.conditionsProvider = provider2;
        this.callbacksProvider = provider3;
    }
}
