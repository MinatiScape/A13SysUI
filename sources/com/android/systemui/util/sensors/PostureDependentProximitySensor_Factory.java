package com.android.systemui.util.sensors;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.Execution;
import com.google.android.systemui.smartspace.KeyguardMediaViewController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PostureDependentProximitySensor_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider delayableExecutorProvider;
    public final Provider devicePostureControllerProvider;
    public final Provider executionProvider;
    public final Provider postureToPrimaryProxSensorMapProvider;
    public final Provider postureToSecondaryProxSensorMapProvider;

    public /* synthetic */ PostureDependentProximitySensor_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, int i) {
        this.$r8$classId = i;
        this.postureToPrimaryProxSensorMapProvider = provider;
        this.postureToSecondaryProxSensorMapProvider = provider2;
        this.delayableExecutorProvider = provider3;
        this.executionProvider = provider4;
        this.devicePostureControllerProvider = provider5;
    }

    public static PostureDependentProximitySensor_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5) {
        return new PostureDependentProximitySensor_Factory(provider, provider2, provider3, provider4, provider5, 0);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new PostureDependentProximitySensor((ThresholdSensor[]) this.postureToPrimaryProxSensorMapProvider.mo144get(), (ThresholdSensor[]) this.postureToSecondaryProxSensorMapProvider.mo144get(), (DelayableExecutor) this.delayableExecutorProvider.mo144get(), (Execution) this.executionProvider.mo144get(), (DevicePostureController) this.devicePostureControllerProvider.mo144get());
            default:
                return new KeyguardMediaViewController((Context) this.postureToPrimaryProxSensorMapProvider.mo144get(), (BcSmartspaceDataPlugin) this.postureToSecondaryProxSensorMapProvider.mo144get(), (DelayableExecutor) this.delayableExecutorProvider.mo144get(), (NotificationMediaManager) this.executionProvider.mo144get(), (BroadcastDispatcher) this.devicePostureControllerProvider.mo144get());
        }
    }
}
