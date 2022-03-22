package com.android.systemui.statusbar.phone;

import android.app.AlarmManager;
import android.os.Handler;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.dock.DockManager;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.wakelock.DelayedWakeLock;
import com.android.systemui.util.wakelock.DelayedWakeLock_Builder_Factory;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ScrimController_Factory implements Factory<ScrimController> {
    public final Provider<AlarmManager> alarmManagerProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<DelayedWakeLock.Builder> delayedWakeLockBuilderProvider;
    public final Provider<DockManager> dockManagerProvider;
    public final Provider<DozeParameters> dozeParametersProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<LightBarController> lightBarControllerProvider;
    public final Provider<Executor> mainExecutorProvider;
    public final Provider<PanelExpansionStateManager> panelExpansionStateManagerProvider;
    public final Provider<ScreenOffAnimationController> screenOffAnimationControllerProvider;

    public static ScrimController_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, DelayedWakeLock_Builder_Factory delayedWakeLock_Builder_Factory, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11) {
        return new ScrimController_Factory(provider, provider2, provider3, provider4, delayedWakeLock_Builder_Factory, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ScrimController(this.lightBarControllerProvider.mo144get(), this.dozeParametersProvider.mo144get(), this.alarmManagerProvider.mo144get(), this.keyguardStateControllerProvider.mo144get(), this.delayedWakeLockBuilderProvider.mo144get(), this.handlerProvider.mo144get(), this.keyguardUpdateMonitorProvider.mo144get(), this.dockManagerProvider.mo144get(), this.configurationControllerProvider.mo144get(), this.mainExecutorProvider.mo144get(), this.screenOffAnimationControllerProvider.mo144get(), this.panelExpansionStateManagerProvider.mo144get());
    }

    public ScrimController_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, DelayedWakeLock_Builder_Factory delayedWakeLock_Builder_Factory, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11) {
        this.lightBarControllerProvider = provider;
        this.dozeParametersProvider = provider2;
        this.alarmManagerProvider = provider3;
        this.keyguardStateControllerProvider = provider4;
        this.delayedWakeLockBuilderProvider = delayedWakeLock_Builder_Factory;
        this.handlerProvider = provider5;
        this.keyguardUpdateMonitorProvider = provider6;
        this.dockManagerProvider = provider7;
        this.configurationControllerProvider = provider8;
        this.mainExecutorProvider = provider9;
        this.screenOffAnimationControllerProvider = provider10;
        this.panelExpansionStateManagerProvider = provider11;
    }
}
