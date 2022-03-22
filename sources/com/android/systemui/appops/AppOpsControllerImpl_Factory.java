package com.android.systemui.appops;

import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AppOpsControllerImpl_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider audioManagerProvider;
    public final Provider bgLooperProvider;
    public final Provider clockProvider;
    public final Provider contextProvider;
    public final Provider dispatcherProvider;
    public final Provider dumpManagerProvider;
    public final Provider sensorPrivacyControllerProvider;

    public /* synthetic */ AppOpsControllerImpl_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.bgLooperProvider = provider2;
        this.dumpManagerProvider = provider3;
        this.audioManagerProvider = provider4;
        this.sensorPrivacyControllerProvider = provider5;
        this.dispatcherProvider = provider6;
        this.clockProvider = provider7;
    }

    public static AppOpsControllerImpl_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7) {
        return new AppOpsControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, 0);
    }

    public static AppOpsControllerImpl_Factory create$1(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7) {
        return new AppOpsControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, 1);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new AppOpsControllerImpl((Context) this.contextProvider.mo144get(), (Looper) this.bgLooperProvider.mo144get(), (DumpManager) this.dumpManagerProvider.mo144get(), (AudioManager) this.audioManagerProvider.mo144get(), (IndividualSensorPrivacyController) this.sensorPrivacyControllerProvider.mo144get(), (BroadcastDispatcher) this.dispatcherProvider.mo144get(), (SystemClock) this.clockProvider.mo144get());
            default:
                return new BatteryMeterViewController((BatteryMeterView) this.contextProvider.mo144get(), (ConfigurationController) this.bgLooperProvider.mo144get(), (TunerService) this.dumpManagerProvider.mo144get(), (BroadcastDispatcher) this.audioManagerProvider.mo144get(), (Handler) this.sensorPrivacyControllerProvider.mo144get(), (ContentResolver) this.dispatcherProvider.mo144get(), (BatteryController) this.clockProvider.mo144get());
        }
    }
}
