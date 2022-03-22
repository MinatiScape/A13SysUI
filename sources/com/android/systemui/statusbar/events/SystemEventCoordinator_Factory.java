package com.android.systemui.statusbar.events;

import android.content.Context;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SystemEventCoordinator_Factory implements Factory<SystemEventCoordinator> {
    public final Provider<BatteryController> batteryControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<PrivacyItemController> privacyControllerProvider;
    public final Provider<SystemClock> systemClockProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new SystemEventCoordinator(this.systemClockProvider.mo144get(), this.batteryControllerProvider.mo144get(), this.privacyControllerProvider.mo144get(), this.contextProvider.mo144get());
    }

    public SystemEventCoordinator_Factory(Provider<SystemClock> provider, Provider<BatteryController> provider2, Provider<PrivacyItemController> provider3, Provider<Context> provider4) {
        this.systemClockProvider = provider;
        this.batteryControllerProvider = provider2;
        this.privacyControllerProvider = provider3;
        this.contextProvider = provider4;
    }
}
