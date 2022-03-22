package com.google.android.systemui.columbus.sensors;

import android.content.Context;
import android.os.Handler;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.google.android.systemui.columbus.sensors.config.GestureConfiguration;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class CHREGestureSensor_Factory implements Factory<CHREGestureSensor> {
    public final Provider<Handler> bgHandlerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<GestureConfiguration> gestureConfigurationProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new CHREGestureSensor(this.contextProvider.mo144get(), this.uiEventLoggerProvider.mo144get(), this.gestureConfigurationProvider.mo144get(), this.statusBarStateControllerProvider.mo144get(), this.wakefulnessLifecycleProvider.mo144get(), this.bgHandlerProvider.mo144get());
    }

    public CHREGestureSensor_Factory(Provider<Context> provider, Provider<UiEventLogger> provider2, Provider<GestureConfiguration> provider3, Provider<StatusBarStateController> provider4, Provider<WakefulnessLifecycle> provider5, Provider<Handler> provider6) {
        this.contextProvider = provider;
        this.uiEventLoggerProvider = provider2;
        this.gestureConfigurationProvider = provider3;
        this.statusBarStateControllerProvider = provider4;
        this.wakefulnessLifecycleProvider = provider5;
        this.bgHandlerProvider = provider6;
    }
}
