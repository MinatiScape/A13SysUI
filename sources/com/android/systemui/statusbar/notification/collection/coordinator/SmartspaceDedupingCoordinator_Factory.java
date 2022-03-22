package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SmartspaceDedupingCoordinator_Factory implements Factory<SmartspaceDedupingCoordinator> {
    public final Provider<SystemClock> clockProvider;
    public final Provider<DelayableExecutor> executorProvider;
    public final Provider<NotifPipeline> notifPipelineProvider;
    public final Provider<NotificationEntryManager> notificationEntryManagerProvider;
    public final Provider<NotificationLockscreenUserManager> notificationLockscreenUserManagerProvider;
    public final Provider<LockscreenSmartspaceController> smartspaceControllerProvider;
    public final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;

    public static SmartspaceDedupingCoordinator_Factory create(Provider<SysuiStatusBarStateController> provider, Provider<LockscreenSmartspaceController> provider2, Provider<NotificationEntryManager> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<NotifPipeline> provider5, Provider<DelayableExecutor> provider6, Provider<SystemClock> provider7) {
        return new SmartspaceDedupingCoordinator_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new SmartspaceDedupingCoordinator(this.statusBarStateControllerProvider.mo144get(), this.smartspaceControllerProvider.mo144get(), this.notificationEntryManagerProvider.mo144get(), this.notificationLockscreenUserManagerProvider.mo144get(), this.notifPipelineProvider.mo144get(), this.executorProvider.mo144get(), this.clockProvider.mo144get());
    }

    public SmartspaceDedupingCoordinator_Factory(Provider<SysuiStatusBarStateController> provider, Provider<LockscreenSmartspaceController> provider2, Provider<NotificationEntryManager> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<NotifPipeline> provider5, Provider<DelayableExecutor> provider6, Provider<SystemClock> provider7) {
        this.statusBarStateControllerProvider = provider;
        this.smartspaceControllerProvider = provider2;
        this.notificationEntryManagerProvider = provider3;
        this.notificationLockscreenUserManagerProvider = provider4;
        this.notifPipelineProvider = provider5;
        this.executorProvider = provider6;
        this.clockProvider = provider7;
    }
}
