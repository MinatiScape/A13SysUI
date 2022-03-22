package com.google.android.systemui.autorotate;

import android.content.Context;
import android.hardware.SensorManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.keyguard.ScreenLifecycle_Factory;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AutorotateDataService_Factory implements Factory<AutorotateDataService> {
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DataLogger> dataLoggerProvider;
    public final Provider<DeviceConfigProxy> deviceConfigProvider;
    public final Provider<DelayableExecutor> mainExecutorProvider;
    public final Provider<SensorManager> sensorManagerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new AutorotateDataService(this.contextProvider.mo144get(), this.sensorManagerProvider.mo144get(), this.dataLoggerProvider.mo144get(), this.broadcastDispatcherProvider.mo144get(), this.deviceConfigProvider.mo144get(), this.mainExecutorProvider.mo144get());
    }

    public AutorotateDataService_Factory(Provider provider, Provider provider2, ScreenLifecycle_Factory screenLifecycle_Factory, Provider provider3, Provider provider4, Provider provider5) {
        this.contextProvider = provider;
        this.sensorManagerProvider = provider2;
        this.dataLoggerProvider = screenLifecycle_Factory;
        this.broadcastDispatcherProvider = provider3;
        this.deviceConfigProvider = provider4;
        this.mainExecutorProvider = provider5;
    }
}
