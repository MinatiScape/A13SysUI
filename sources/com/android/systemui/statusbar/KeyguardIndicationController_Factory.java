package com.android.systemui.statusbar;

import android.app.IActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.UserManager;
import com.android.internal.app.IBatteryStats;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dock.DockManager;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.wakelock.WakeLock;
import com.android.systemui.util.wakelock.WakeLock_Builder_Factory;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardIndicationController_Factory implements Factory<KeyguardIndicationController> {
    public final Provider<DelayableExecutor> bgExecutorProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DevicePolicyManager> devicePolicyManagerProvider;
    public final Provider<DockManager> dockManagerProvider;
    public final Provider<DelayableExecutor> executorProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<IActivityManager> iActivityManagerProvider;
    public final Provider<IBatteryStats> iBatteryStatsProvider;
    public final Provider<KeyguardBypassController> keyguardBypassControllerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<LockPatternUtils> lockPatternUtilsProvider;
    public final Provider<ScreenLifecycle> screenLifecycleProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<UserManager> userManagerProvider;
    public final Provider<WakeLock.Builder> wakeLockBuilderProvider;

    public KeyguardIndicationController_Factory(Provider provider, WakeLock_Builder_Factory wakeLock_Builder_Factory, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11, Provider provider12, Provider provider13, Provider provider14, Provider provider15, Provider provider16) {
        this.contextProvider = provider;
        this.wakeLockBuilderProvider = wakeLock_Builder_Factory;
        this.keyguardStateControllerProvider = provider2;
        this.statusBarStateControllerProvider = provider3;
        this.keyguardUpdateMonitorProvider = provider4;
        this.dockManagerProvider = provider5;
        this.broadcastDispatcherProvider = provider6;
        this.devicePolicyManagerProvider = provider7;
        this.iBatteryStatsProvider = provider8;
        this.userManagerProvider = provider9;
        this.executorProvider = provider10;
        this.bgExecutorProvider = provider11;
        this.falsingManagerProvider = provider12;
        this.lockPatternUtilsProvider = provider13;
        this.screenLifecycleProvider = provider14;
        this.iActivityManagerProvider = provider15;
        this.keyguardBypassControllerProvider = provider16;
    }

    public static KeyguardIndicationController_Factory create(Provider provider, WakeLock_Builder_Factory wakeLock_Builder_Factory, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11, Provider provider12, Provider provider13, Provider provider14, Provider provider15, Provider provider16) {
        return new KeyguardIndicationController_Factory(provider, wakeLock_Builder_Factory, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new KeyguardIndicationController(this.contextProvider.mo144get(), this.wakeLockBuilderProvider.mo144get(), this.keyguardStateControllerProvider.mo144get(), this.statusBarStateControllerProvider.mo144get(), this.keyguardUpdateMonitorProvider.mo144get(), this.dockManagerProvider.mo144get(), this.broadcastDispatcherProvider.mo144get(), this.devicePolicyManagerProvider.mo144get(), this.iBatteryStatsProvider.mo144get(), this.userManagerProvider.mo144get(), this.executorProvider.mo144get(), this.bgExecutorProvider.mo144get(), this.falsingManagerProvider.mo144get(), this.lockPatternUtilsProvider.mo144get(), this.screenLifecycleProvider.mo144get(), this.iActivityManagerProvider.mo144get(), this.keyguardBypassControllerProvider.mo144get());
    }
}
