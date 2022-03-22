package com.android.systemui.statusbar.policy;

import android.os.Handler;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.util.settings.SecureSettingsImpl_Factory;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DeviceProvisionedControllerImpl_Factory implements Factory<DeviceProvisionedControllerImpl> {
    public final Provider<Handler> backgroundHandlerProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<GlobalSettings> globalSettingsProvider;
    public final Provider<Executor> mainExecutorProvider;
    public final Provider<SecureSettings> secureSettingsProvider;
    public final Provider<UserTracker> userTrackerProvider;

    public static DeviceProvisionedControllerImpl_Factory create(SecureSettingsImpl_Factory secureSettingsImpl_Factory, Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5) {
        return new DeviceProvisionedControllerImpl_Factory(secureSettingsImpl_Factory, provider, provider2, provider3, provider4, provider5);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DeviceProvisionedControllerImpl(this.secureSettingsProvider.mo144get(), this.globalSettingsProvider.mo144get(), this.userTrackerProvider.mo144get(), this.dumpManagerProvider.mo144get(), this.backgroundHandlerProvider.mo144get(), this.mainExecutorProvider.mo144get());
    }

    public DeviceProvisionedControllerImpl_Factory(SecureSettingsImpl_Factory secureSettingsImpl_Factory, Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5) {
        this.secureSettingsProvider = secureSettingsImpl_Factory;
        this.globalSettingsProvider = provider;
        this.userTrackerProvider = provider2;
        this.dumpManagerProvider = provider3;
        this.backgroundHandlerProvider = provider4;
        this.mainExecutorProvider = provider5;
    }
}
