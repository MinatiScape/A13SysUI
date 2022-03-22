package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarTouchableRegionManager_Factory implements Factory<StatusBarTouchableRegionManager> {
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<HeadsUpManagerPhone> headsUpManagerProvider;
    public final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new StatusBarTouchableRegionManager(this.contextProvider.mo144get(), this.notificationShadeWindowControllerProvider.mo144get(), this.configurationControllerProvider.mo144get(), this.headsUpManagerProvider.mo144get());
    }

    public StatusBarTouchableRegionManager_Factory(Provider<Context> provider, Provider<NotificationShadeWindowController> provider2, Provider<ConfigurationController> provider3, Provider<HeadsUpManagerPhone> provider4) {
        this.contextProvider = provider;
        this.notificationShadeWindowControllerProvider = provider2;
        this.configurationControllerProvider = provider3;
        this.headsUpManagerProvider = provider4;
    }
}
