package com.android.systemui;

import android.content.Context;
import android.os.Handler;
import android.provider.Settings;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.tv.notifications.TvNotificationPanel;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.sensors.ProximityCheck;
import com.android.systemui.util.sensors.ProximitySensor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ForegroundServiceController_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider appOpsControllerProvider;
    public final Provider mainHandlerProvider;

    public /* synthetic */ ForegroundServiceController_Factory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.appOpsControllerProvider = provider;
        this.mainHandlerProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        boolean z;
        switch (this.$r8$classId) {
            case 0:
                return new ForegroundServiceController((AppOpsController) this.appOpsControllerProvider.mo144get(), (Handler) this.mainHandlerProvider.mo144get());
            case 1:
                return new TvNotificationPanel((Context) this.appOpsControllerProvider.mo144get(), (CommandQueue) this.mainHandlerProvider.mo144get());
            case 2:
                return new ProximityCheck((ProximitySensor) this.appOpsControllerProvider.mo144get(), (DelayableExecutor) this.mainHandlerProvider.mo144get());
            default:
                if (Settings.Secure.getString(((Context) this.appOpsControllerProvider.mo144get()).getContentResolver(), (String) this.mainHandlerProvider.mo144get()) != null) {
                    z = true;
                } else {
                    z = false;
                }
                return Boolean.valueOf(z);
        }
    }
}
