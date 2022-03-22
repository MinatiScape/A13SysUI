package com.google.android.systemui.communal.dock;

import android.content.Context;
import com.google.android.systemui.communal.dock.DockEventSimulator;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DockEventSimulator_DockingCondition_Factory implements Factory<DockEventSimulator.DockingCondition> {
    public final Provider<Context> contextProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DockEventSimulator.DockingCondition(this.contextProvider.mo144get());
    }

    public DockEventSimulator_DockingCondition_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }
}
