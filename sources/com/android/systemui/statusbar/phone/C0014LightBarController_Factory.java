package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.policy.BatteryController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* renamed from: com.android.systemui.statusbar.phone.LightBarController_Factory  reason: case insensitive filesystem */
/* loaded from: classes.dex */
public final class C0014LightBarController_Factory implements Factory<LightBarController> {
    public final Provider<BatteryController> batteryControllerProvider;
    public final Provider<Context> ctxProvider;
    public final Provider<DarkIconDispatcher> darkIconDispatcherProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<NavigationModeController> navModeControllerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new LightBarController(this.ctxProvider.mo144get(), this.darkIconDispatcherProvider.mo144get(), this.batteryControllerProvider.mo144get(), this.navModeControllerProvider.mo144get(), this.dumpManagerProvider.mo144get());
    }

    public C0014LightBarController_Factory(Provider<Context> provider, Provider<DarkIconDispatcher> provider2, Provider<BatteryController> provider3, Provider<NavigationModeController> provider4, Provider<DumpManager> provider5) {
        this.ctxProvider = provider;
        this.darkIconDispatcherProvider = provider2;
        this.batteryControllerProvider = provider3;
        this.navModeControllerProvider = provider4;
        this.dumpManagerProvider = provider5;
    }
}
