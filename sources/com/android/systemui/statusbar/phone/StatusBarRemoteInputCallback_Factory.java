package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.ActionClickLogger;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarRemoteInputCallback_Factory implements Factory<StatusBarRemoteInputCallback> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<ActionClickLogger> clickLoggerProvider;
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<Context> contextProvider;
    public final Provider<Executor> executorProvider;
    public final Provider<GroupExpansionManager> groupExpansionManagerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<NotificationLockscreenUserManager> notificationLockscreenUserManagerProvider;
    public final Provider<ShadeController> shadeControllerProvider;
    public final Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public static StatusBarRemoteInputCallback_Factory create(Provider<Context> provider, Provider<GroupExpansionManager> provider2, Provider<NotificationLockscreenUserManager> provider3, Provider<KeyguardStateController> provider4, Provider<StatusBarStateController> provider5, Provider<StatusBarKeyguardViewManager> provider6, Provider<ActivityStarter> provider7, Provider<ShadeController> provider8, Provider<CommandQueue> provider9, Provider<ActionClickLogger> provider10, Provider<Executor> provider11) {
        return new StatusBarRemoteInputCallback_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new StatusBarRemoteInputCallback(this.contextProvider.mo144get(), this.groupExpansionManagerProvider.mo144get(), this.notificationLockscreenUserManagerProvider.mo144get(), this.keyguardStateControllerProvider.mo144get(), this.statusBarStateControllerProvider.mo144get(), this.statusBarKeyguardViewManagerProvider.mo144get(), this.activityStarterProvider.mo144get(), this.shadeControllerProvider.mo144get(), this.commandQueueProvider.mo144get(), this.clickLoggerProvider.mo144get(), this.executorProvider.mo144get());
    }

    public StatusBarRemoteInputCallback_Factory(Provider<Context> provider, Provider<GroupExpansionManager> provider2, Provider<NotificationLockscreenUserManager> provider3, Provider<KeyguardStateController> provider4, Provider<StatusBarStateController> provider5, Provider<StatusBarKeyguardViewManager> provider6, Provider<ActivityStarter> provider7, Provider<ShadeController> provider8, Provider<CommandQueue> provider9, Provider<ActionClickLogger> provider10, Provider<Executor> provider11) {
        this.contextProvider = provider;
        this.groupExpansionManagerProvider = provider2;
        this.notificationLockscreenUserManagerProvider = provider3;
        this.keyguardStateControllerProvider = provider4;
        this.statusBarStateControllerProvider = provider5;
        this.statusBarKeyguardViewManagerProvider = provider6;
        this.activityStarterProvider = provider7;
        this.shadeControllerProvider = provider8;
        this.commandQueueProvider = provider9;
        this.clickLoggerProvider = provider10;
        this.executorProvider = provider11;
    }
}
