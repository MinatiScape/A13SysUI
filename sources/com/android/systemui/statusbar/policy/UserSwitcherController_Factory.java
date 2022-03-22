package com.android.systemui.statusbar.policy;

import android.app.IActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.Handler;
import android.os.UserManager;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.util.LatencyTracker;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class UserSwitcherController_Factory implements Factory<UserSwitcherController> {
    public final Provider<IActivityManager> activityManagerProvider;
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<Executor> bgExecutorProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DevicePolicyManager> devicePolicyManagerProvider;
    public final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    public final Provider<DialogLaunchAnimator> dialogLaunchAnimatorProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<InteractionJankMonitor> interactionJankMonitorProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<LatencyTracker> latencyTrackerProvider;
    public final Provider<SecureSettings> secureSettingsProvider;
    public final Provider<ShadeController> shadeControllerProvider;
    public final Provider<TelephonyListenerManager> telephonyListenerManagerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<Executor> uiExecutorProvider;
    public final Provider<UserManager> userManagerProvider;
    public final Provider<UserTracker> userTrackerProvider;

    public UserSwitcherController_Factory(Provider<Context> provider, Provider<IActivityManager> provider2, Provider<UserManager> provider3, Provider<UserTracker> provider4, Provider<KeyguardStateController> provider5, Provider<DeviceProvisionedController> provider6, Provider<DevicePolicyManager> provider7, Provider<Handler> provider8, Provider<ActivityStarter> provider9, Provider<BroadcastDispatcher> provider10, Provider<UiEventLogger> provider11, Provider<FalsingManager> provider12, Provider<TelephonyListenerManager> provider13, Provider<SecureSettings> provider14, Provider<Executor> provider15, Provider<Executor> provider16, Provider<InteractionJankMonitor> provider17, Provider<LatencyTracker> provider18, Provider<DumpManager> provider19, Provider<ShadeController> provider20, Provider<DialogLaunchAnimator> provider21) {
        this.contextProvider = provider;
        this.activityManagerProvider = provider2;
        this.userManagerProvider = provider3;
        this.userTrackerProvider = provider4;
        this.keyguardStateControllerProvider = provider5;
        this.deviceProvisionedControllerProvider = provider6;
        this.devicePolicyManagerProvider = provider7;
        this.handlerProvider = provider8;
        this.activityStarterProvider = provider9;
        this.broadcastDispatcherProvider = provider10;
        this.uiEventLoggerProvider = provider11;
        this.falsingManagerProvider = provider12;
        this.telephonyListenerManagerProvider = provider13;
        this.secureSettingsProvider = provider14;
        this.bgExecutorProvider = provider15;
        this.uiExecutorProvider = provider16;
        this.interactionJankMonitorProvider = provider17;
        this.latencyTrackerProvider = provider18;
        this.dumpManagerProvider = provider19;
        this.shadeControllerProvider = provider20;
        this.dialogLaunchAnimatorProvider = provider21;
    }

    public static UserSwitcherController_Factory create(Provider<Context> provider, Provider<IActivityManager> provider2, Provider<UserManager> provider3, Provider<UserTracker> provider4, Provider<KeyguardStateController> provider5, Provider<DeviceProvisionedController> provider6, Provider<DevicePolicyManager> provider7, Provider<Handler> provider8, Provider<ActivityStarter> provider9, Provider<BroadcastDispatcher> provider10, Provider<UiEventLogger> provider11, Provider<FalsingManager> provider12, Provider<TelephonyListenerManager> provider13, Provider<SecureSettings> provider14, Provider<Executor> provider15, Provider<Executor> provider16, Provider<InteractionJankMonitor> provider17, Provider<LatencyTracker> provider18, Provider<DumpManager> provider19, Provider<ShadeController> provider20, Provider<DialogLaunchAnimator> provider21) {
        return new UserSwitcherController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new UserSwitcherController(this.contextProvider.mo144get(), this.activityManagerProvider.mo144get(), this.userManagerProvider.mo144get(), this.userTrackerProvider.mo144get(), this.keyguardStateControllerProvider.mo144get(), this.deviceProvisionedControllerProvider.mo144get(), this.devicePolicyManagerProvider.mo144get(), this.handlerProvider.mo144get(), this.activityStarterProvider.mo144get(), this.broadcastDispatcherProvider.mo144get(), this.uiEventLoggerProvider.mo144get(), this.falsingManagerProvider.mo144get(), this.telephonyListenerManagerProvider.mo144get(), this.secureSettingsProvider.mo144get(), this.bgExecutorProvider.mo144get(), this.uiExecutorProvider.mo144get(), this.interactionJankMonitorProvider.mo144get(), this.latencyTrackerProvider.mo144get(), this.dumpManagerProvider.mo144get(), DoubleCheck.lazy(this.shadeControllerProvider), this.dialogLaunchAnimatorProvider.mo144get());
    }
}
