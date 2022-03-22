package com.android.systemui.controls;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class ControlsMetricsLoggerImpl_Factory implements Factory<ControlsMetricsLoggerImpl> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final ControlsMetricsLoggerImpl_Factory INSTANCE = new ControlsMetricsLoggerImpl_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ControlsMetricsLoggerImpl();
    }
}
