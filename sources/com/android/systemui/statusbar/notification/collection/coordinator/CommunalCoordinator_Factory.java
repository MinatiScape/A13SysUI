package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.communal.CommunalStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class CommunalCoordinator_Factory implements Factory<CommunalCoordinator> {
    public final Provider<CommunalStateController> communalStateControllerProvider;
    public final Provider<Executor> executorProvider;
    public final Provider<NotificationEntryManager> notificationEntryManagerProvider;
    public final Provider<NotificationLockscreenUserManager> notificationLockscreenUserManagerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new CommunalCoordinator(this.executorProvider.mo144get(), this.notificationEntryManagerProvider.mo144get(), this.notificationLockscreenUserManagerProvider.mo144get(), this.communalStateControllerProvider.mo144get());
    }

    public CommunalCoordinator_Factory(Provider<Executor> provider, Provider<NotificationEntryManager> provider2, Provider<NotificationLockscreenUserManager> provider3, Provider<CommunalStateController> provider4) {
        this.executorProvider = provider;
        this.notificationEntryManagerProvider = provider2;
        this.notificationLockscreenUserManagerProvider = provider3;
        this.communalStateControllerProvider = provider4;
    }
}
