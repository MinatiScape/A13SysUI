package com.android.systemui.statusbar.notification.collection.coordinator;

import android.content.pm.IPackageManager;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DeviceProvisionedCoordinator_Factory implements Factory<DeviceProvisionedCoordinator> {
    public final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    public final Provider<IPackageManager> packageManagerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DeviceProvisionedCoordinator(this.deviceProvisionedControllerProvider.mo144get(), this.packageManagerProvider.mo144get());
    }

    public DeviceProvisionedCoordinator_Factory(Provider<DeviceProvisionedController> provider, Provider<IPackageManager> provider2) {
        this.deviceProvisionedControllerProvider = provider;
        this.packageManagerProvider = provider2;
    }
}
