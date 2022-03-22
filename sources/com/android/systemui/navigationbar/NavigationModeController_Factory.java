package com.android.systemui.navigationbar;

import android.content.Context;
import com.android.settingslib.dream.DreamBackend;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dreams.complication.ComplicationTypesUpdater;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NavigationModeController_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider configurationControllerProvider;
    public final Provider contextProvider;
    public final Provider deviceProvisionedControllerProvider;
    public final Provider dumpManagerProvider;
    public final Provider uiBgExecutorProvider;

    public /* synthetic */ NavigationModeController_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.deviceProvisionedControllerProvider = provider2;
        this.configurationControllerProvider = provider3;
        this.uiBgExecutorProvider = provider4;
        this.dumpManagerProvider = provider5;
    }

    public static NavigationModeController_Factory create$1(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5) {
        return new NavigationModeController_Factory(provider, provider2, provider3, provider4, provider5, 0);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new NavigationModeController((Context) this.contextProvider.mo144get(), (DeviceProvisionedController) this.deviceProvisionedControllerProvider.mo144get(), (ConfigurationController) this.configurationControllerProvider.mo144get(), (Executor) this.uiBgExecutorProvider.mo144get(), (DumpManager) this.dumpManagerProvider.mo144get());
            default:
                return new ComplicationTypesUpdater((Context) this.contextProvider.mo144get(), (DreamBackend) this.deviceProvisionedControllerProvider.mo144get(), (Executor) this.configurationControllerProvider.mo144get(), (SecureSettings) this.uiBgExecutorProvider.mo144get(), (DreamOverlayStateController) this.dumpManagerProvider.mo144get());
        }
    }
}
