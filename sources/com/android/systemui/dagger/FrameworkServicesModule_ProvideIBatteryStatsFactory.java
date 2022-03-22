package com.android.systemui.dagger;

import android.os.ServiceManager;
import com.android.internal.app.IBatteryStats;
import dagger.internal.Factory;
import java.util.Objects;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideIBatteryStatsFactory implements Factory<IBatteryStats> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final FrameworkServicesModule_ProvideIBatteryStatsFactory INSTANCE = new FrameworkServicesModule_ProvideIBatteryStatsFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        IBatteryStats asInterface = IBatteryStats.Stub.asInterface(ServiceManager.getService("batterystats"));
        Objects.requireNonNull(asInterface, "Cannot return null from a non-@Nullable @Provides method");
        return asInterface;
    }
}
