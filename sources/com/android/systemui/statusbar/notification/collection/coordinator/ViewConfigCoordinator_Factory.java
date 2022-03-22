package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ViewConfigCoordinator_Factory implements Factory<ViewConfigCoordinator> {
    public final Provider<ConfigurationController> mConfigurationControllerProvider;
    public final Provider<NotificationGutsManager> mGutsManagerProvider;
    public final Provider<KeyguardUpdateMonitor> mKeyguardUpdateMonitorProvider;
    public final Provider<NotificationLockscreenUserManagerImpl> mLockscreenUserManagerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ViewConfigCoordinator(this.mConfigurationControllerProvider.mo144get(), this.mLockscreenUserManagerProvider.mo144get(), this.mGutsManagerProvider.mo144get(), this.mKeyguardUpdateMonitorProvider.mo144get());
    }

    public ViewConfigCoordinator_Factory(Provider<ConfigurationController> provider, Provider<NotificationLockscreenUserManagerImpl> provider2, Provider<NotificationGutsManager> provider3, Provider<KeyguardUpdateMonitor> provider4) {
        this.mConfigurationControllerProvider = provider;
        this.mLockscreenUserManagerProvider = provider2;
        this.mGutsManagerProvider = provider3;
        this.mKeyguardUpdateMonitorProvider = provider4;
    }
}
