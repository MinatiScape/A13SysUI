package com.android.wm.shell.dagger;

import android.content.Context;
import android.content.om.OverlayManager;
import android.os.Handler;
import android.os.Looper;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.notification.NotificationEntryManagerLogger;
import com.android.systemui.statusbar.phone.NotificationListenerWithPlugins;
import com.android.systemui.statusbar.policy.DeviceProvisionedControllerImpl;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellBaseModule_ProvideRecentTasksFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider recentTasksControllerProvider;

    public /* synthetic */ WMShellBaseModule_ProvideRecentTasksFactory(Provider provider, int i) {
        this.$r8$classId = i;
        this.recentTasksControllerProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                Optional map = ((Optional) this.recentTasksControllerProvider.mo144get()).map(WMShellBaseModule$$ExternalSyntheticLambda1.INSTANCE);
                Objects.requireNonNull(map, "Cannot return null from a non-@Nullable @Provides method");
                return map;
            case 1:
                OverlayManager overlayManager = (OverlayManager) ((Context) this.recentTasksControllerProvider.mo144get()).getSystemService(OverlayManager.class);
                Objects.requireNonNull(overlayManager, "Cannot return null from a non-@Nullable @Provides method");
                return overlayManager;
            case 2:
                return new NotificationEntryManagerLogger((LogBuffer) this.recentTasksControllerProvider.mo144get());
            case 3:
                return new NotificationListenerWithPlugins((PluginManager) this.recentTasksControllerProvider.mo144get());
            case 4:
                return new Handler((Looper) this.recentTasksControllerProvider.mo144get());
            default:
                DeviceProvisionedControllerImpl deviceProvisionedControllerImpl = (DeviceProvisionedControllerImpl) this.recentTasksControllerProvider.mo144get();
                deviceProvisionedControllerImpl.init();
                return deviceProvisionedControllerImpl;
        }
    }
}
