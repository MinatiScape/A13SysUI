package com.android.keyguard;

import android.content.Context;
import android.os.Looper;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.util.concurrency.GlobalConcurrencyModule_ProvideMainLooperFactory;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardUpdateMonitor_Factory implements Factory<KeyguardUpdateMonitor> {
    public final Provider<AuthController> authControllerProvider;
    public final Provider<Executor> backgroundExecutorProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<InteractionJankMonitor> interactionJankMonitorProvider;
    public final Provider<LatencyTracker> latencyTrackerProvider;
    public final Provider<LockPatternUtils> lockPatternUtilsProvider;
    public final Provider<Executor> mainExecutorProvider;
    public final Provider<Looper> mainLooperProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<TelephonyListenerManager> telephonyListenerManagerProvider;

    public KeyguardUpdateMonitor_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11) {
        GlobalConcurrencyModule_ProvideMainLooperFactory globalConcurrencyModule_ProvideMainLooperFactory = GlobalConcurrencyModule_ProvideMainLooperFactory.InstanceHolder.INSTANCE;
        this.contextProvider = provider;
        this.mainLooperProvider = globalConcurrencyModule_ProvideMainLooperFactory;
        this.broadcastDispatcherProvider = provider2;
        this.dumpManagerProvider = provider3;
        this.backgroundExecutorProvider = provider4;
        this.mainExecutorProvider = provider5;
        this.statusBarStateControllerProvider = provider6;
        this.lockPatternUtilsProvider = provider7;
        this.authControllerProvider = provider8;
        this.telephonyListenerManagerProvider = provider9;
        this.interactionJankMonitorProvider = provider10;
        this.latencyTrackerProvider = provider11;
    }

    public static KeyguardUpdateMonitor_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11) {
        return new KeyguardUpdateMonitor_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new KeyguardUpdateMonitor(this.contextProvider.mo144get(), this.mainLooperProvider.mo144get(), this.broadcastDispatcherProvider.mo144get(), this.dumpManagerProvider.mo144get(), this.backgroundExecutorProvider.mo144get(), this.mainExecutorProvider.mo144get(), this.statusBarStateControllerProvider.mo144get(), this.lockPatternUtilsProvider.mo144get(), this.authControllerProvider.mo144get(), this.telephonyListenerManagerProvider.mo144get(), this.interactionJankMonitorProvider.mo144get(), this.latencyTrackerProvider.mo144get());
    }
}
